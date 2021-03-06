package ait.sistemas.proyecto.seguridad.view.perm.otorgaryr;

import java.util.Date;
import java.util.List;

import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.common.theme.AitTheme;
import ait.sistemas.proyecto.common.view.AitView;
import ait.sistemas.proyecto.seguridad.data.model.Arbol_menus;
import ait.sistemas.proyecto.seguridad.data.service.Impl.UsuarioImpl;

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
import com.vaadin.ui.UI;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class VOtorgaryRA extends VerticalLayout implements View, ClickListener {
	
	private static final long serialVersionUID = 1L;
	
	private FormOtorgar frm_otorgar;
	private Button btn_submit;
	private Button btn_limpiar;
	private CssLayout hl_errores = new CssLayout();
	private final UsuarioImpl usuarioimpl = new UsuarioImpl();
	
	private final Arbol_menus menu = (Arbol_menus) UI.getCurrent().getSession().getAttribute("nav");
	
	public VOtorgaryRA() {
		this.frm_otorgar = new FormOtorgar();
		this.btn_submit = new Button("Guardar");
		this.btn_limpiar = new Button("Limpiar");
		
		addComponent(buildNavBar());
		addComponent(builFormContent());
		addComponent(buildButtonBar());
	}
	
	private Component builFormContent() {
		final VerticalLayout vlfrmContent = new VerticalLayout();
		vlfrmContent.setMargin(true);
		vlfrmContent.setSpacing(true);
		
		Panel pnfrmOpcionPerfil = new Panel("Formulario de Permisos");
		pnfrmOpcionPerfil.setStyleName(AitTheme.PANEL_FORM);
		pnfrmOpcionPerfil.setIcon(FontAwesome.EDIT);
		pnfrmOpcionPerfil.setContent(this.frm_otorgar);
		
		Panel pngridOpcionPerfil = new Panel("Grid de Opciones");
		pngridOpcionPerfil.setStyleName(AitTheme.PANEL_GRID);
		pngridOpcionPerfil.setIcon(FontAwesome.TABLE);
		pngridOpcionPerfil.setContent(this.frm_otorgar.grid_otorgar);
		
		vlfrmContent.addComponent(pnfrmOpcionPerfil);
		vlfrmContent.addComponent(pngridOpcionPerfil);
		return vlfrmContent;
	}
	
	private Component buildNavBar() {
		Panel navPanel = new Panel();
		HorizontalLayout nav = new HorizontalLayout();
		nav.addStyleName(AitTheme.NAV_BAR);
		nav.addComponent(new Label(AitView.getNavText(menu), ContentMode.HTML));
		navPanel.setContent(nav);
		return navPanel;
	}
	
	private Component buildButtonBar() {
		CssLayout buttonContent = new CssLayout();
		buttonContent.addComponent(this.btn_submit);
		this.btn_submit.addStyleName(AitTheme.BTN_SUBMIT);
		this.btn_submit.setIcon(FontAwesome.SAVE);
		this.btn_submit.addClickListener(this);
		buttonContent.addStyleName(AitTheme.BUTTONS_BAR);
		buttonContent.addComponent(this.btn_limpiar);
		this.btn_limpiar.addStyleName(AitTheme.BTN_EXIT);
		this.btn_limpiar.setIcon(FontAwesome.UNDO);
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
			Label lbError = new Label(barMessage.getComponetName() + ":" + barMessage.getErrorName());
			lbError.setStyleName(barMessage.getType());
			this.hl_errores.addComponent(lbError);
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == this.btn_submit) {
			if (this.frm_otorgar.validate()) {
				java.sql.Date fechaRegistro = new java.sql.Date(new Date().getTime());
				try {
					usuarioimpl.otortgarPermisos(this.frm_otorgar.getUsuario(), this.frm_otorgar.getPermisos(),
							this.frm_otorgar.getIdPadre(), fechaRegistro);
				} catch (Exception e) {
					Notification.show(e.getMessage(), Type.ERROR_MESSAGE);
				}
			}
			buildMessages(this.frm_otorgar.getMensajes());
			this.frm_otorgar.clearMessages();
		}
	}
}
