package ait.sistemas.proyecto.activos.view.rrhh.inmueble;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ait.sistemas.proyecto.activos.data.model_rrhh.InmuebleModel;
import ait.sistemas.proyecto.activos.data.service.Impl.DependenciaImpl;
import ait.sistemas.proyecto.activos.data.service.Impl.InmuebleImpl;
import ait.sistemas.proyecto.activos.view.rrhh.inmueble.reporte.ReportPdf;
import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.seguridad.component.model.SessionModel;

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
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class VInmuebleR extends VerticalLayout implements View, ClickListener {

	private static final long serialVersionUID = 1L;

	private Button btn_imprimir;
	private String[][] data;
	int r = 0;
	private final InmuebleImpl inmueble_impl = new InmuebleImpl();
	private DependenciaImpl dependencia_impl = new DependenciaImpl();
	private CssLayout hl_errores = new CssLayout();

	public VInmuebleR() {

		this.btn_imprimir = new Button("Imprimir");
		addComponent(buildNavBar());
		addComponent(buildButtonBar());
		List<BarMessage> mensajes = new ArrayList<BarMessage>();
		mensajes.add(new BarMessage("",
				"Pulsar el Boton Imprimir para generar el reporte", "success"));
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
		nav.addComponent(new Label("Inmueble » "));
		nav.addComponent(new Label("<strong>Reporte</strong>", ContentMode.HTML));
		navPanel.setContent(nav);
		return navPanel;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	public String[][] getData() {
		SessionModel usuario = (SessionModel) UI.getCurrent().getSession()
				.getAttribute("user");

		short dependencia = dependencia_impl.getdependencia_ID(usuario
				.getDependecia());
		List<InmuebleModel> result = this.inmueble_impl.getalls(dependencia);

		this.data = new String[result.size()][5];
		this.r = 0;
		for (InmuebleModel row_mov : result) {
			String[] row = { String.valueOf(row_mov.getINM_Inmueble()),
					row_mov.getINM_Nombre_Inmueble(), row_mov.getINM_Ciudad(),
					row_mov.getINM_Domicilio_Inmueble() };
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
			Label lbError = new Label(barMessage.getComponetName()
					+ barMessage.getErrorName());
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

			Window subWindow = new Window("Reporte Inmuebles");
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
