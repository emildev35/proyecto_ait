package ait.sistemas.proyecto.seguridad.view.estr.menu;

import java.util.List;

import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.common.component.Messages;
import ait.sistemas.proyecto.seguridad.data.service.Impl.MenuImpl;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;


public class VMenuA extends VerticalLayout implements View, ClickListener, ValueChangeListener{

	private static final long serialVersionUID = 1L;
	
	private Button btn_submit;
	private Button btn_limpiar;
	private FormMenu frm_menu;
	private GridMenu grid_menu;
	private CssLayout hl_errores;
	private MenuImpl menu;
	public VMenuA() {
		
		this.frm_menu = new FormMenu();
		this.menu = new MenuImpl();
		this.btn_submit = new Button("Guardar");
		this.btn_limpiar = new Button("Limpiar");
		this.hl_errores = new CssLayout();
		this.grid_menu = new GridMenu();
		
		this.frm_menu.cbSubsistema.addValueChangeListener(this);
		
		addComponent(buildNavBar());
		addComponent(buildFormContent());
		addComponent(buildButtonBar());
	}
	private Component buildFormContent() {
		
		VerticalLayout formContent = new VerticalLayout();
		formContent.setSpacing(true	);
		Panel frmPanel = new Panel();
		frmPanel.setWidth("100%");
		frmPanel.setCaption("Formulario Menu");
		frmPanel.setContent(this.frm_menu);
		formContent.setMargin(true);
		formContent.addComponent(frmPanel);
		Panel gridPanel = new Panel();
		gridPanel.setWidth("100%");
		gridPanel.setCaption("GRid Menu");
		gridPanel.setContent(this.grid_menu);
		formContent.setMargin(true);
		formContent.addComponent(frmPanel);
		formContent.addComponent(gridPanel);
		Responsive.makeResponsive(formContent);
		return formContent;
		
	}
	private Component buildNavBar() {
		Panel navPanel = new Panel();
		HorizontalLayout nav = new HorizontalLayout();
		nav.addStyleName("ait-content-nav");
		nav.addComponent(new Label("Seguridad » "));
		nav.addComponent(new Label("Estructura del Sistema » "));
		nav.addComponent(new Label("Menu » "));
		nav.addComponent(new Label("<strong>Agregar</strong>", ContentMode.HTML));
		navPanel.setContent(nav);
		return navPanel;
	}
	private Component buildButtonBar() {
		CssLayout buttonContent = new CssLayout();
		buttonContent.addComponent(this.btn_submit);
		this.btn_submit.addStyleName("ait-buttons-btn");
		this.btn_submit.addClickListener(this);
		buttonContent.addStyleName("ait-buttons");
		buttonContent.addComponent(this.btn_limpiar);
		this.btn_limpiar.addStyleName("ait-buttons-btn");
		this.btn_limpiar.addClickListener(this);
		Responsive.makeResponsive(buttonContent);
		return buttonContent;
	}
	@Override
	public void enter(ViewChangeEvent event) {
		
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
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == this.btn_submit) {
			if(this.frm_menu.validate()){
				this.menu.addMenu(this.frm_menu.getData());
				this.frm_menu.update();
				this.grid_menu.update(frm_menu.getSubsistema());
				this.frm_menu.txt_nombre_menu.focus();
				Notification.show(Messages.SUCCESS_MESSAGE);
			}else{
				Notification.show(Messages.NOT_SUCCESS_MESSAGE, Type.ERROR_MESSAGE);
			}
			buildMessages(this.frm_menu.getMensajes());
			this.frm_menu.clearMessages();
		}
		if (event.getButton() == this.btn_limpiar) {
			
		}	
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		grid_menu.update(frm_menu.getSubsistema());
	}

}
