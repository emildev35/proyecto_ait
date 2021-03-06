package ait.sistemas.proyecto.activos.view.rrhh.autorizado;

import java.util.List;

import ait.sistemas.proyecto.activos.data.service.Impl.TipoAutorizacionImpl;
import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.common.component.Messages;
import ait.sistemas.proyecto.common.theme.AitTheme;
import ait.sistemas.proyecto.common.view.AitView;
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

public class VAutorizadoA extends VerticalLayout implements View, ClickListener {
	
	private static final long serialVersionUID = 1L;
	
	private FormAutorizado frm_autorizacion = new FormAutorizado();
	private CssLayout hl_errores = new CssLayout();
	private Button btn_limpiar = new Button("Salir");
	private Button btn_agregar = new Button("Guardar");
	private GridAutorizado grid_autorizacion = new GridAutorizado();
	private final TipoAutorizacionImpl tipo_autorizacionimpl = new TipoAutorizacionImpl();
	
	private final Arbol_menus menu = (Arbol_menus)UI.getCurrent().getSession().getAttribute("nav");
	
	public VAutorizadoA() {
		this.btn_agregar.addClickListener(this);
		this.btn_limpiar.addClickListener(this);
		this.hl_errores = new CssLayout();
		
		addComponent(buildNavBar());
		addComponent(buildFormContent());
		addComponent(buildButtonBar());
	}
	
	private Component buildButtonBar() {
		CssLayout buttonContent = new CssLayout();
		this.btn_agregar.setStyleName(AitTheme.BTN_SUBMIT);
		this.btn_agregar.setIcon(FontAwesome.SAVE);
		buttonContent.addComponent(this.btn_agregar);
		
		this.btn_limpiar.setStyleName(AitTheme.BTN_EXIT);
		this.btn_limpiar.setIcon(FontAwesome.UNDO);
		buttonContent.addComponent(this.btn_limpiar);

		buttonContent.addStyleName(AitTheme.BUTTONS_BAR);
		return buttonContent;
	}
	
	private Component buildFormContent() {
		
		VerticalLayout formContent = new VerticalLayout();
		formContent.setSpacing(true);
		formContent.setMargin(true);
		Panel gridPanel = new Panel();
		gridPanel.setStyleName(AitTheme.PANEL_GRID);
		gridPanel.setIcon(FontAwesome.TABLE);
		gridPanel.setWidth("100%");
		gridPanel.setCaption("AUTORIZACIONES");
		gridPanel.setCaption("AUTORIZACIONES");
		gridPanel.setContent(this.grid_autorizacion);
		formContent.setMargin(true);

		formContent.addComponent(frm_autorizacion);
		formContent.addComponent(gridPanel);
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
			Label lbError = new Label(new Label(barMessage.getComponetName() + ":" + barMessage.getErrorName()));
			lbError.setStyleName(barMessage.getType());
			this.hl_errores.addComponent(lbError);
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == this.btn_agregar) {
			if (this.frm_autorizacion.validate()) {
				if (this.tipo_autorizacionimpl.add(this.frm_autorizacion.getData())) {
					grid_autorizacion.update();
					this.frm_autorizacion.update();
					
					Notification.show(Messages.SUCCESS_MESSAGE);
				} else {
					Notification.show(Messages.NOT_SUCCESS_MESSAGE, Type.ERROR_MESSAGE);
				}
			} else {
				Notification.show(Messages.NOT_SUCCESS_MESSAGE, Type.ERROR_MESSAGE);
			}
			buildMessages(this.frm_autorizacion.getMensajes());
			this.frm_autorizacion.clearMessages();
		}
		if (event.getButton() == this.btn_limpiar) {
			this.frm_autorizacion.update();
		}
	}
	
}