package ait.sistemas.proyecto.activos.view.para.ciudad;

import java.util.List;

import org.vaadin.dialogs.ConfirmDialog;

import ait.sistemas.proyecto.activos.data.model_rrhh.Ciudade;
import ait.sistemas.proyecto.activos.data.service.Impl.CiudadImpl;
import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.common.component.Messages;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.PropertysetItem;
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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("unused")
public class VCiudadB extends VerticalLayout implements View,
		SelectionListener, ClickListener, org.vaadin.dialogs.ConfirmDialog.Listener {

	private static final long serialVersionUID = 1L;

	private CiudadImpl ciudad_impl = new CiudadImpl();
	private GridCiudad grid_ciudad;
	private Button btn_limpiar;
	private Button btn_eliminar;
	private FormCiudad frm_ciudad;
	private Ciudade ciudad;
	private CssLayout hl_errores;
	
	final PropertysetItem pitmCiudad = new PropertysetItem();
	private FieldGroup binderCiudad;

	public VCiudadB() {

		frm_ciudad= new FormCiudad();
		this.btn_limpiar= new Button("Limpiar");
		this.btn_eliminar= new Button("Eliminar");
		this.btn_eliminar.addClickListener(this);
		this.btn_limpiar.addClickListener(this);

		this.grid_ciudad = new GridCiudad();
		this.hl_errores = new CssLayout();
		
		addComponent(buildNavBar());
		addComponent(buildFormContent());
		addComponent(buildButtonBar());
		buildGrid();
		Responsive.makeResponsive(this);
	}
	private void buildGrid() {
		this.grid_ciudad.addSelectionListener(this);
	}
	private Component buildFormContent() {

		VerticalLayout formContent = new VerticalLayout();
		formContent.setSpacing(true	);				
				
		Panel frmPanel = new Panel();
		frmPanel.setWidth("100%");
		frmPanel.setCaption("Datos a registrar");
		frmPanel.setContent(this.frm_ciudad);
		this.frm_ciudad.enabled();
		formContent.setMargin(true);
		Panel gridPanel = new Panel();
		gridPanel.setWidth("100%");
		gridPanel.setCaption("Ciudad registradas");
		gridPanel.setContent(this.grid_ciudad);
		formContent.setMargin(true);
		formContent.addComponent(gridPanel);
		formContent.addComponent(frmPanel);

		this.frm_ciudad.update();
		Responsive.makeResponsive(formContent);
		return formContent;
	}

	private Component buildNavBar() {
		Panel navPanel = new Panel();
		HorizontalLayout nav = new HorizontalLayout();
		nav.addStyleName("ait-content-nav");
		nav.addComponent(new Label("Activos » "));
		nav.addComponent(new Label("Recursos Humanos » "));
		nav.addComponent(new Label("Ciudad » "));
		nav.addComponent(new Label("<strong>Eliminar</strong>",
				ContentMode.HTML));
		navPanel.setContent(nav);
		return navPanel;
	}

	private Component buildButtonBar() {
		CssLayout buttonContent = new CssLayout();
		this.btn_eliminar.setStyleName("ait-buttons-btn");
		buttonContent.addComponent(this.btn_eliminar);
		this.btn_limpiar.setStyleName("ait-buttons-btn");
		buttonContent.addStyleName("ait-buttons");
		buttonContent.addComponent(this.btn_limpiar);
		return buttonContent;
	}

	@Override
	public void enter(ViewChangeEvent event) {

	}

	@Override
	public void select(SelectionEvent event) {

		if ((Ciudade)this.grid_ciudad.getSelectedRow() != null) {
			this.frm_ciudad.setData((Ciudade)this.grid_ciudad.getSelectedRow());	
		}
	}
	private void  eliminar() {
		try {
		this.ciudad_impl.deletes(this.frm_ciudad.getData().getCIU_Ciudad());
		this.frm_ciudad.update();
		this.grid_ciudad.update();
		Notification.show(Messages.SUCCESS_MESSAGE);
	} catch (Exception e) {
		Notification.show(Messages.FOREIGN_RELATION_ERROR(frm_ciudad.txt_nombre_ciudad
				.getValue()), Type.ERROR_MESSAGE);
	}
		buildMessages(this.frm_ciudad.getMensajes());
		this.frm_ciudad.clearMessages();
	}
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == this.btn_eliminar) {

			ConfirmDialog.show(getUI(),"", Messages.CONFIRM_DELETE_MESSAGE, "SI", "NO", this); 
		}
		if (event.getButton() == this.btn_limpiar) {
			this.frm_ciudad.update();
		}
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
	public void onClose(ConfirmDialog dialog) {
		if(dialog.isConfirmed()){
			eliminar();
		}else{
			
		}
	}

}

