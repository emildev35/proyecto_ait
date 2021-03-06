package ait.sistemas.proyecto.activos.view.inve.tomainv;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ait.sistemas.proyecto.activos.component.model.ActivoInventario;
import ait.sistemas.proyecto.activos.data.service.Impl.InventarioImpl;
import ait.sistemas.proyecto.activos.view.inve.tomainv.reporte.PdfReport;
import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.common.theme.AitTheme;
import ait.sistemas.proyecto.common.view.HomeView;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VTomaInvR extends VerticalLayout implements View, ClickListener {
	
	private static final long serialVersionUID = 1L;
	
	private FormReporte frm_reporte = new FormReporte();
	
	private Button btn_guardar = new Button("IMPRIMIR");
	private Button btn_salir = new Button("SALIR");
	
	private CssLayout hl_errores = new CssLayout();
	
	private InventarioImpl inventarioimpl = new InventarioImpl();
	
	private List<BarMessage> msg = new ArrayList<BarMessage>();
	
	public VTomaInvR() {
		
		this.btn_guardar.addClickListener(this);
		this.btn_salir.addClickListener(this);
		
		addComponent(buildNavBar());
		addComponent(buildFormContent());
		addComponent(buildButtonBar());
		Responsive.makeResponsive(this);
		buildMessages(msg);
	}
	
	private Component buildFormContent() {
		VerticalLayout vl_form = new VerticalLayout();
		
		vl_form.addComponent(frm_reporte);
		
		return vl_form;
	}
	
	private Component buildNavBar() {
		Panel navPanel = new Panel();
		navPanel.addStyleName("ait-content-nav");
		HorizontalLayout nav = new HorizontalLayout();
		nav.addComponent(new Label("Activos>>"));
		nav.addComponent(new Label("Inventario>>"));
		nav.addComponent(new Label("<strong>Reporte de Inventario Fisico</strong>", ContentMode.HTML));
		navPanel.setContent(nav);
		return navPanel;
	}
	
	private Component buildButtonBar() {
		CssLayout buttonContent = new CssLayout();
		GridLayout btn_grid = new GridLayout(2, 1);
		btn_grid.setResponsive(true);
		btn_grid.setSizeFull();
		this.btn_guardar.setStyleName(AitTheme.BTN_SUBMIT);
		btn_grid.addComponent(this.btn_guardar);
		btn_grid.setComponentAlignment(btn_guardar, Alignment.TOP_CENTER);
		btn_guardar.setIcon(FontAwesome.SAVE);
		this.btn_salir.setStyleName(AitTheme.BTN_EXIT);
		buttonContent.addStyleName("ait-buttons");
		btn_grid.addComponent(this.btn_salir);
		btn_salir.setIcon(FontAwesome.UNDO);
		btn_grid.setComponentAlignment(btn_salir, Alignment.TOP_LEFT);
		buttonContent.addComponent(btn_grid);
		return buttonContent;
	}
	
	public void buildMessages(List<BarMessage> mensages) {
		this.hl_errores.removeAllComponents();
		hl_errores.addStyleName("ait-error-bar");
		this.addComponent(this.hl_errores);
		
		for (BarMessage barMessage : mensages) {
			Label lbError = new Label(barMessage.getComponetName() + ":" + barMessage.getErrorName());
			lbError.setStyleName(barMessage.getType());
			this.hl_errores.addComponent(lbError);
		}
		
	}
	public String[][] getDatos(List<ActivoInventario> activos){
		String [][] result =  new String[activos.size()][11];
		for (int i = 0; i < result.length; i++) {
			result[i][0] = String.valueOf(activos.get(i).getDependencia());
			result[i][1] = String.valueOf(activos.get(i).getDocumento_referencia());
			result[i][2] = String.valueOf(activos.get(i).getNumero_documento());
			result[i][3] = String.valueOf(activos.get(i).getFecha_registro());
			result[i][4] = String.valueOf(activos.get(i).getCodigo_activo());
			result[i][5] = String.valueOf(activos.get(i).getNombre_activo());
			result[i][6] = String.valueOf(activos.get(i).getNombre_funcionario());
			result[i][7] = String.valueOf(activos.get(i).getObservacion());
			result[i][8] = String.valueOf(activos.get(i).getSr().equals("-1")?"X":" ");
			result[i][9] = String.valueOf(activos.get(i).getDr().equals("-1")?"X":" ");
			result[i][10] = String.valueOf(activos.get(i).getMr().equals("-1")?"X":" ");
		}
		
		return result;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == this.btn_guardar) {
			if (this.frm_reporte.validate()) {
				List<ActivoInventario> activos = inventarioimpl.getReport(frm_reporte.getDependencia(), frm_reporte.getDocRef(),
						frm_reporte.getFechaRef());
				PdfReport reporte = new PdfReport();
				try {
					reporte.getPdf(getDatos(activos));
					File pdfFile = new File(PdfReport.SAVE_PATH);

					VerticalLayout vl_pdf = new VerticalLayout();
					Embedded pdf = new Embedded("", new FileResource(pdfFile));
					pdf.setMimeType("application/pdf");
					pdf.setType(Embedded.TYPE_BROWSER);
					pdf.setSizeFull();
					vl_pdf.setSizeFull();
					vl_pdf.addComponent(pdf);

					Window subWindow = new Window("Reporte Inmuebles");
					VerticalLayout subContent = new VerticalLayout();
					subContent.setMargin(true);
					subWindow.setContent(vl_pdf);

					subWindow.setWidth("90%");
					subWindow.setHeight("90%");
					subWindow.center();

					getUI().addWindow(subWindow);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			buildMessages(msg);
			buildMessages(this.frm_reporte.getMensajes());
			this.frm_reporte.clearMessages();
		}
		if (event.getButton() == this.btn_salir) {
			UI.getCurrent().getNavigator().navigateTo(HomeView.URL);
		}
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
}
