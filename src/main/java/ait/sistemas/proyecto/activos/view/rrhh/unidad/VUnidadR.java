package ait.sistemas.proyecto.activos.view.rrhh.unidad;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ait.sistemas.proyecto.activos.data.model_rrhh.UnidadesOrganizacionalesModel;
import ait.sistemas.proyecto.activos.data.service.Impl.UnidadImpl;
import ait.sistemas.proyecto.activos.view.rrhh.unidad.reporte.ReportPdf;
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

public class VUnidadR extends VerticalLayout implements View, ClickListener {

	private static final long serialVersionUID = 1L;

	private Button btn_imprimir;
	private String[][] data;
	int r = 0;
	private final UnidadImpl unidad_impl = new UnidadImpl();
	private CssLayout hl_errores = new CssLayout();

	public VUnidadR() {

		this.btn_imprimir = new Button("Imprimir");
		addComponent(buildNavBar());
		addComponent(buildButtonBar());
		List<BarMessage> mensajes = new ArrayList<BarMessage>();
		mensajes.add(new BarMessage("","Pulsar el Boton Imprimir para generar el reporte", "success"));
		buildMessages(mensajes);
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


	private Component buildNavBar() {
		Panel navPanel = new Panel();
		HorizontalLayout nav = new HorizontalLayout();
		nav.addStyleName("ait-content-nav");
		nav.addComponent(new Label("Activos » "));
		nav.addComponent(new Label("Recursos Humanos » "));
		nav.addComponent(new Label("Unidad Organizacional » "));
		nav.addComponent(new Label("<strong>Reporte</strong>", ContentMode.HTML));
		navPanel.setContent(nav);
		return navPanel;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}


	public String[][] getData() {
		List<UnidadesOrganizacionalesModel> result = this.unidad_impl.getalls();
		
		this.data = new String[result.size()][4];
		this.r = 0;
		for(UnidadesOrganizacionalesModel row_mov : result){
			String[] row = {String.valueOf(row_mov.getUNO_Dependencia_ID()),
					row_mov.getUNO_Dependencia(),
					String.valueOf(row_mov.getUNO_Unidad_Organizacional()),
					row_mov.getUNO_Nombre_Unidad_Organizacional()
			};
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
			Label lbError = new Label(barMessage.getComponetName() + barMessage.getErrorName());
			lbError.setStyleName(barMessage.getType());
			this.hl_errores.addComponent(lbError);
		}

	}
	@SuppressWarnings("deprecation")
	@Override
	public void buttonClick(ClickEvent event) {
		ReportPdf reporte = new ReportPdf();
		try {
			reporte.getPdf(getData());
			File pdfFile = new File(ReportPdf.SAVE_PATH);

			VerticalLayout vl_pdf = new VerticalLayout();
			Embedded pdf = new Embedded("", new FileResource(pdfFile));
			pdf.setMimeType("application/pdf");
			pdf.setType(Embedded.TYPE_BROWSER);
			pdf.setSizeFull();
			vl_pdf.setSizeFull();
			vl_pdf.addComponent(pdf);

			Window subWindow = new Window("Reporte Unidad Organizacional");
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

}
