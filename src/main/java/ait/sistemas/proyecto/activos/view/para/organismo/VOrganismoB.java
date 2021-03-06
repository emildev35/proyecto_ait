package ait.sistemas.proyecto.activos.view.para.organismo;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import ait.sistemas.proyecto.activos.data.model.Organismo_Financiador;
import ait.sistemas.proyecto.activos.data.service.Impl.OrganismoImpl;
import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.common.component.Messages;
import ait.sistemas.proyecto.common.theme.AitTheme;
import ait.sistemas.proyecto.common.view.AitView;
import ait.sistemas.proyecto.seguridad.data.model.Arbol_menus;

import com.vaadin.data.util.PropertysetItem;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
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

public class VOrganismoB extends VerticalLayout implements View, SelectionListener, ClickListener,
		org.vaadin.dialogs.ConfirmDialog.Listener {
	
	private static final long serialVersionUID = 1L;
	
	private OrganismoImpl organismo_impl = new OrganismoImpl();
	private GridOrganismo grid_organismo;
	private Button btn_limpiar;
	private Button btn_eliminar;
	private FormOrganismo frm_organismo;
	private CssLayout hl_errores;
	private final Arbol_menus menu = (Arbol_menus) UI.getCurrent().getSession().getAttribute("nav");
	
	private List<BarMessage> msgs = new ArrayList<BarMessage>();
	final PropertysetItem pitm_Fuente = new PropertysetItem();
	
	public VOrganismoB() {
		
		this.frm_organismo = new FormOrganismo();
		this.btn_limpiar = new Button("Limpiar");
		this.btn_eliminar = new Button("Eliminar");
		this.btn_eliminar.addClickListener(this);
		this.btn_limpiar.addClickListener(this);
		this.grid_organismo = new GridOrganismo();
		this.hl_errores = new CssLayout();
		
		addComponent(buildNavBar());
		addComponent(buildFormContent());
		addComponent(buildButtonBar());
		buildGrid();
		Responsive.makeResponsive(this);
	}
	
	private void buildGrid() {
		this.grid_organismo.addSelectionListener(this);
	}
	
	private Component buildFormContent() {
		
		VerticalLayout formContent = new VerticalLayout();
		formContent.setSpacing(true);
		
		Panel frmPanel = new Panel();
		frmPanel.setStyleName(AitTheme.PANEL_FORM);
		frmPanel.setIcon(FontAwesome.EDIT);
		frmPanel.setWidth("100%");
		frmPanel.setCaption("Datos a eliminar");
		frmPanel.setContent(this.frm_organismo);
		this.frm_organismo.enabled();
		formContent.setMargin(true);
		Panel gridPanel = new Panel();
		gridPanel.setStyleName(AitTheme.PANEL_GRID);
		gridPanel.setIcon(FontAwesome.TABLE);
		gridPanel.setWidth("100%");
		gridPanel.setCaption("Organismos Financiadores registrados");
		gridPanel.setContent(this.grid_organismo);
		formContent.setMargin(true);
		formContent.addComponent(gridPanel);
		formContent.addComponent(frmPanel);
		
		this.frm_organismo.update();
		Responsive.makeResponsive(formContent);
		return formContent;
	}
	
	private Component buildNavBar() {
		Panel navPanel = new Panel();
		HorizontalLayout nav = new HorizontalLayout();
		nav.addStyleName("ait-content-nav");
		nav.addComponent(new Label(AitView.getNavText(menu), ContentMode.HTML));
		navPanel.setContent(nav);
		return navPanel;
	}
	
	private Component buildButtonBar() {
		CssLayout buttonContent = new CssLayout();
		this.btn_eliminar.setStyleName(AitTheme.BTN_SUBMIT);
		btn_eliminar.setIcon(FontAwesome.TRASH_O);
		buttonContent.addComponent(this.btn_eliminar);
		this.btn_limpiar.setStyleName("ait-buttons-btn");
		this.btn_limpiar.setStyleName(AitTheme.BTN_EXIT);
		btn_limpiar.setIcon(FontAwesome.TRASH_O);
		buttonContent.addStyleName(AitTheme.BUTTONS_BAR);
		buttonContent.addComponent(this.btn_limpiar);
		return buttonContent;
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	@Override
	public void select(SelectionEvent event) {
		
		if ((Organismo_Financiador) this.grid_organismo.getSelectedRow() != null) {
			this.frm_organismo.setData((Organismo_Financiador) this.grid_organismo.getSelectedRow());
			
			msgs = new ArrayList<BarMessage>();
			msgs.add(new BarMessage("Formulario", Messages.PRESS_BUTTON_DELETE));
			buildMessages(msgs);
			
		}
	}
	
	private void eliminar() {
		try {
			this.organismo_impl.deletes(this.frm_organismo.getData().getORF_Organismo_Financiador());
			this.frm_organismo.update();
			this.grid_organismo.update();
			Notification.show(Messages.SUCCESS_MESSAGE);
		} catch (Exception e) {
			Notification.show(Messages.FOREIGN_RELATION_ERROR(frm_organismo.txt_nombre_organismo.getValue()), Type.ERROR_MESSAGE);
		}
		buildMessages(this.frm_organismo.getMensajes());
		this.frm_organismo.clearMessages();
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == this.btn_eliminar) {
			
			ConfirmDialog.show(getUI(), "", Messages.CONFIRM_DELETE_MESSAGE, "SI", "NO", this);
		}
		if (event.getButton() == this.btn_limpiar) {
			this.frm_organismo.update();
		}
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
	public void onClose(ConfirmDialog dialog) {
		if (dialog.isConfirmed()) {
			eliminar();
		} else {
			
		}
	}
	
}
