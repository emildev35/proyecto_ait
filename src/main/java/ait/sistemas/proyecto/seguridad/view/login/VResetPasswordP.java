package ait.sistemas.proyecto.seguridad.view.login;

import java.util.List;

import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.common.component.Messages;
import ait.sistemas.proyecto.seguridad.component.model.SessionModel;
import ait.sistemas.proyecto.seguridad.data.service.Impl.UsuarioImpl;

import com.vaadin.cdi.CDIView;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
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

@CDIView(value = VResetPasswordP.ID)
public class VResetPasswordP extends VerticalLayout implements View, ClickListener {
	public static final String ID = "/seg/reset_password";
	private static final long serialVersionUID = 1L;
	
	private ChangePasswordForm frm_change_password = new ChangePasswordForm();
	private Button btn_submit = new Button("Cambiar");
	private Button btn_limpiar = new Button("Limpiar");
	private CssLayout hl_errores = new CssLayout();
	
	private final UsuarioImpl usuarioimpl = new UsuarioImpl();
	
	public VResetPasswordP() {
		setWidth("100%");
		this.btn_submit.addClickListener(this);
		
		addComponent(buildNavBar());
		addComponent(buildButtonBar());
		addComponent(buildForm());
	}
	
	private Component buildForm() {
		final VerticalLayout vl_content = new VerticalLayout();
		vl_content.setMargin(true);
		Panel pn_frm = new Panel();
		pn_frm.setContent(this.frm_change_password);
		vl_content.addComponent(pn_frm);
		return vl_content;
	}
	
	private Component buildNavBar() {
		Panel navPanel = new Panel();
		HorizontalLayout nav = new HorizontalLayout();
		nav.addStyleName("ait-content-nav");
		nav.addComponent(new Label("Seguridad » "));
		nav.addComponent(new Label("<strong>Login</strong>", ContentMode.HTML));
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
	public void enter(ViewChangeEvent event) {
		
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == this.btn_submit) {
			if (this.frm_change_password.validate()) {
				SessionModel session = (SessionModel)getUI().getSession().getAttribute("user");
				if(usuarioimpl.changePassword(session.getId(), this.frm_change_password.getPassword())){
					Notification.show(Messages.SUCCESS_MESSAGE);
				}else{
					Notification.show(Messages.NOT_SUCCESS_MESSAGE, Type.ERROR_MESSAGE);
				}
				buildMessages(this.frm_change_password.getMessages());
			}
		} else {
			
		}
	}
	
}