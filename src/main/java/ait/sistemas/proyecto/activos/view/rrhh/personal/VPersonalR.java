package ait.sistemas.proyecto.activos.view.rrhh.personal;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ait.sistemas.proyecto.activos.data.model_rrhh.PersonalModel;
import ait.sistemas.proyecto.activos.data.service.Impl.PersonalImpl;
import ait.sistemas.proyecto.activos.view.rrhh.personal.reporte.FormReporte;
import ait.sistemas.proyecto.activos.view.rrhh.personal.reporte.ReportPdf;
import ait.sistemas.proyecto.common.component.BarMessage;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VPersonalR extends VerticalLayout implements View, ClickListener {

	private static final long serialVersionUID = 1L;
	public static final String ID = "/act/rrhh/personal/reporte";

	private Button btn_imprimir;
	private FormReporte frmReporte = new FormReporte();
	private String[][] data;
	int r = 0;
	private final PersonalImpl personal_impl = new PersonalImpl();
	private CssLayout hl_errores = new CssLayout();
	public VPersonalR() {

		this.btn_imprimir = new Button("Imprimir");
		addComponent(buildNavBar());
		addComponent(buildFormContent());
		addComponent(buildButtonBar());

	}

	private Component buildButtonBar() {
		CssLayout buttonContent = new CssLayout();
		buttonContent.addComponent(this.btn_imprimir);
		this.btn_imprimir.addStyleName("ait-buttons-btn");
		this.btn_imprimir.addClickListener(this);
		buttonContent.addStyleName("ait-buttons");

		Responsive.makeResponsive(buttonContent);
		return buttonContent;
	}

	private Component buildFormContent() {
		VerticalLayout formContent = new VerticalLayout();
		formContent.setSpacing(true);
		Panel frmPanel = new Panel();
		frmPanel.setWidth("100%");
		frmPanel.setCaption("Formulario de Impresion");
		frmPanel.setContent(this.frmReporte);
		formContent.setMargin(true);
		formContent.addComponent(frmPanel);
		Responsive.makeResponsive(formContent);
		return formContent;
	}

	private Component buildNavBar() {
		Panel navPanel = new Panel();
		HorizontalLayout nav = new HorizontalLayout();
		nav.addStyleName("ait-content-nav");
		nav.addComponent(new Label("Activos » "));
		nav.addComponent(new Label("Recursos Humanos » "));
		nav.addComponent(new Label("Personal » "));
		nav.addComponent(new Label("<strong>Reporte</strong>", ContentMode.HTML));
		navPanel.setContent(nav);
		return navPanel;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	public String[][] getData() {
		
		List<PersonalModel> lista = personal_impl.getallreporte((Short) this.frmReporte.cbDependencia.getValue());
		
		this.data = new String[lista.size()][6];
		this.r = 0;
		for (PersonalModel personal : lista) {
			String fullname = String.format("%s %s %s",
					personal.getPER_Apellido_Paterno(),
					personal.getPER_Apellido_Materno(),
					personal.getPER_Nombres());
			String[] row = { 
					personal.getPER_CI_Empleado(), 
					fullname,
					personal.getPER_Dependencia(),
					personal.getPER_Unidad_Organizacional(),
					personal.getPER_No_Telefono_Oficina(),
					personal.getPER_No_Interno() };
			this.data[r] = row;
			this.r++;
		}
		return data;
	}
	public String[][] getDatosALL() {
		
		List<PersonalModel> lista = personal_impl.getPersonal();
		
		this.data = new String[lista.size()][6];
		this.r = 0;
		for (PersonalModel personal : lista) {
			String fullname = String.format("%s %s %s",
					personal.getPER_Apellido_Paterno(),
					personal.getPER_Apellido_Materno(),
					personal.getPER_Nombres());
			String[] row = { 
					personal.getPER_CI_Empleado(), 
					fullname,
					personal.getPER_Dependencia(),
					personal.getPER_Unidad_Organizacional(),
					personal.getPER_No_Telefono_Oficina(),
					personal.getPER_No_Interno() };
			this.data[r] = row;
			this.r++;
		}
		return data;
	}
	
	private void buildMessages(List<BarMessage> mensages) {
		this.hl_errores.removeAllComponents();
		hl_errores.addStyleName("ait-error-bar");
		this.addComponent(this.hl_errores);
		
		for (BarMessage barMessage : mensages) {
			Label lbError = new Label(new Label(barMessage.getComponetName()+":"+barMessage.getErrorName()));
			lbError.setStyleName(barMessage.getType());
			this.hl_errores.addComponent(lbError);
		}
			
	}
	@SuppressWarnings("deprecation")
	@Override
	public void buttonClick(ClickEvent event) {
		if (this.frmReporte.validate()) {
			ReportPdf reporte = new ReportPdf();
			try {
				short a = 0;
				if ((Short) this.frmReporte.cbDependencia.getValue() == a) {
					// int [][] datas = activo_impl.getProvedoreCuidad();
					reporte.getPdf(getDatosALL(), "Todas las Dependencias");
				}
				else{
				reporte.getPdf(getData(), this.frmReporte.cbDependencia
						.getItemCaption(this.frmReporte.cbDependencia
								.getValue()));
			}
				File pdfFile = new File(ReportPdf.SAVE_PATH);

				VerticalLayout vl_pdf = new VerticalLayout();
				Embedded pdf = new Embedded("", new FileResource(pdfFile));
				pdf.setMimeType("application/pdf");
				pdf.setType(Embedded.TYPE_BROWSER);
				pdf.setSizeFull();
				vl_pdf.setSizeFull();
				vl_pdf.addComponent(pdf);

				Window subWindow = new Window("Reporte Personal");
				VerticalLayout subContent = new VerticalLayout();
				subContent.setMargin(true);
				subWindow.setContent(vl_pdf);

				subWindow.setWidth("90%");
				subWindow.setHeight("90%");
				subWindow.center();

				// Open it in the UI
				getUI().addWindow(subWindow);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		buildMessages(this.frmReporte.getMessage());
	}
	

}
