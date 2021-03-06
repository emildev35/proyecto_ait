package ait.sistemas.proyecto.seguridad.view.estr.menu;

import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.seguridad.data.model.Arbol_menus;
import ait.sistemas.proyecto.seguridad.data.service.Impl.MenuImpl;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
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

public class VMenuB extends VerticalLayout implements View, ValueChangeListener, ClickListener, SelectionListener,
		org.vaadin.dialogs.ConfirmDialog.Listener {
	
	private static final long serialVersionUID = 1L;
	
	private Button btn_submit;
	private Button btn_limpiar;
	private FormMenu frm_menu;
	private GridMenu grid_menu;
	private CssLayout hl_errores;
	private MenuImpl menu;
	
	public VMenuB() {
		
		this.frm_menu = new FormMenu();
		this.frm_menu.txt_nombre_menu.setEnabled(false);
		this.frm_menu.txt_nombre_programa.setEnabled(false);
		this.frm_menu.cb_icons.setEnabled(false);
		// this.frm_menu.cbSubsistema.setEnabled(false);
		this.menu = new MenuImpl();
		this.btn_submit = new Button("Eliminar");
		this.btn_limpiar = new Button("Limpiar");
		this.hl_errores = new CssLayout();
		this.grid_menu = new GridMenu();
		this.grid_menu.addSelectionListener(this);
		
		this.frm_menu.cbSubsistema.addValueChangeListener(this);
		addComponent(buildNavBar());
		addComponent(buildFormContent());
		addComponent(buildButtonBar());
	}
	
	private Component buildFormContent() {
		
		VerticalLayout formContent = new VerticalLayout();
		formContent.setSpacing(true);
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
		nav.addComponent(new Label("<strong>Eliminar</strong>", ContentMode.HTML));
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
			Label lbError = new Label(new Label(barMessage.getComponetName() + ":" + barMessage.getErrorName()));
			lbError.setStyleName(barMessage.getType());
			this.hl_errores.addComponent(lbError);
		}
		
	}
	
	private void eliminar() {
		buildMessages(menu.delete(this.frm_menu.getData().getAME_Id_Identificador()));
		this.frm_menu.update();
		this.grid_menu.update(frm_menu.getSubsistema());
		this.frm_menu.clearMessages();
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == this.btn_submit) {
			ConfirmDialog.show(getUI(), ("Eliminar " + this.frm_menu.getData().getAME_Nombre()),
					("Esta Uds Seguro que Desea Eliminar el Sub-Sistema" + this.frm_menu.getData().getAME_Nombre()), "Si", "No",
					this);
		}
		if (event.getButton() == this.btn_limpiar) {
			this.frm_menu.clearMessages();
		}
	}
	
	@Override
	public void select(SelectionEvent event) {
		if ((Arbol_menus) this.grid_menu.getSelectedRow() != null) {
			this.frm_menu.setData((Arbol_menus) this.grid_menu.getSelectedRow());
		}
	}
	
	@Override
	public void onClose(ConfirmDialog dialog) {
		if (dialog.isConfirmed()) {
			eliminar();
		} else {
			
		}
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		grid_menu.update(frm_menu.getSubsistema());
		
	}
}