package ait.sistemas.proyecto.activos.view.reva.actualiza;

import java.util.List;

import ait.sistemas.proyecto.activos.component.model.Actualizacion;
import ait.sistemas.proyecto.activos.data.service.Impl.ActualizacionImpl;
import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.common.component.Messages;
import ait.sistemas.proyecto.common.theme.AitTheme;
import ait.sistemas.proyecto.common.view.AitView;
import ait.sistemas.proyecto.common.view.HomeView;
import ait.sistemas.proyecto.seguridad.data.model.Arbol_menus;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

public class VActualizaP extends VerticalLayout implements View, ClickListener {
	
	private static final long serialVersionUID = 1L;
	
	private FormActualiza frm_Actualiza;
	private CssLayout hl_errores;
	private Button btn_agregar;
	private Button btn_salir = new Button("Salir");
	
	private final ActualizacionImpl actualiza_impl = new ActualizacionImpl();
	private final Arbol_menus menu = (Arbol_menus) UI.getCurrent().getSession().getAttribute("nav");
	
	public VActualizaP() {
		frm_Actualiza = new FormActualiza();
		this.btn_agregar = new Button("Actualiza Activos");
		this.btn_agregar.addClickListener(this);
		
		this.btn_salir.addClickListener(this);
		
		this.hl_errores = new CssLayout();
		
		addComponent(buildNavBar());
		addComponent(buildFormContent());
		addComponent(buildButtonBar());
	}
	
	private Component buildButtonBar() {
		CssLayout buttonContent = new CssLayout();
		this.btn_agregar.setStyleName(AitTheme.BTN_SUBMIT);
		btn_agregar.setIcon(FontAwesome.TRUCK);
		btn_salir.setStyleName(AitTheme.BTN_EXIT);
		btn_salir.setIcon(FontAwesome.UNDO);
		buttonContent.addComponent(btn_agregar);
		buttonContent.addComponent(btn_salir);
		buttonContent.addStyleName(AitTheme.BUTTONS_BAR);
		return buttonContent;
	}
	
	private Component buildFormContent() {
		
		VerticalLayout formContent = new VerticalLayout();
		formContent.setSpacing(true);
		formContent.setMargin(true);
		formContent.addComponent(this.frm_Actualiza);
		Responsive.makeResponsive(formContent);
		return formContent;
	}
	
	private Component buildNavBar() {
		Panel navPanel = new Panel();
		navPanel.addStyleName("ait-content-nav");
		HorizontalLayout nav = new HorizontalLayout();
		nav.addComponent(new Label(AitView.getNavText(menu), ContentMode.HTML));
		navPanel.setContent(nav);
		return navPanel;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
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
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == this.btn_agregar) {
			if (this.frm_Actualiza.validate()) {
				actualiza_impl.add_Actualizacion(frm_Actualiza.getData());
				this.frm_Actualiza.update();
				Notification.show(Messages.SUCCESS_MESSAGE);
			} else {
				Notification.show(Messages.NOT_SUCCESS_MESSAGE, Type.ERROR_MESSAGE);
			}
			buildMessages(this.frm_Actualiza.getMensajes());
			this.frm_Actualiza.clearMessages();
		} else {
			UI.getCurrent().getNavigator().navigateTo(HomeView.URL);
		}
	}
	
	public class DepreciacionWorker extends Thread {
	
		Actualizacion actualizacion;
		volatile double current = 0.0;
		
		public DepreciacionWorker(Actualizacion actu) {
			this.actualizacion = actu;
		}
		
		@Override
		public void run() {
			actualiza_impl.add_Actualizacion(actualizacion);
		}
	}
	
}