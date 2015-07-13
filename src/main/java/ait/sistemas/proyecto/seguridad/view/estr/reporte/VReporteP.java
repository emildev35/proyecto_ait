package ait.sistemas.proyecto.seguridad.view.estr.reporte;

import java.io.IOException;

import ait.sistemas.proyecto.seguridad.data.model.Arbol_menus;
import ait.sistemas.proyecto.seguridad.data.service.Impl.MenuImpl;

import com.vaadin.cdi.CDIView;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@CDIView(value=VReporteP.ID)
public class VReporteP extends VerticalLayout implements View, ClickListener{
	

	private static final long serialVersionUID = 1L;
	public static final String ID = "/seg/estr/reporte";
	
	private Button btn_imprimir;
	private FormReporte frmReporte;
	
	private final MenuImpl menuiml = new MenuImpl();
	
	public VReporteP() {
		
		this.frmReporte = new FormReporte();
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
		nav.addComponent(new Label("Seguridad » "));
		nav.addComponent(new Label("Estructura del Sistema » "));
		nav.addComponent(new Label("Opcion » "));
		nav.addComponent(new Label("<strong>Agregar</strong>", ContentMode.HTML));
		navPanel.setContent(nav);
		return navPanel;
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}

	public String[][] getData() {
		String[][] result = new String[100][5];
		int r = 0;
		for (Arbol_menus menu : menuiml.getallMenu((long) 1)) {
			String[] row = { 
					String.valueOf(menu.getAME_Id_Identificador()),
					String.valueOf(menu.getAME_Id_Menus()),
					menu.getAME_Nombre(),
					"1",
					menu.getAME_Programa()};
			result[r] = row;
			r++;
		}
		return result;
	}
	@Override
	public void buttonClick(ClickEvent event) {
		// TODO Auto-generated method stub
		ReportPdf reporte = new ReportPdf();
		try {
			reporte.getPdf(getData());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
