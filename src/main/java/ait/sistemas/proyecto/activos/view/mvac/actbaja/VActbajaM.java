package ait.sistemas.proyecto.activos.view.mvac.actbaja;

import java.util.List;

import ait.sistemas.proyecto.activos.component.model.CmovimientoDocumento;
import ait.sistemas.proyecto.activos.component.model.Movimiento;
import ait.sistemas.proyecto.activos.data.service.Impl.MovimientoImpl;
import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.common.component.Messages;

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

public class VActbajaM extends VerticalLayout implements View, ClickListener, SelectionListener {
	
	private static final long serialVersionUID = 1L;
	
	private FormActbaja frm_Actualizacion_baja;
	private CssLayout hl_errores;
	private Button btn_imprimir;
	private Button btn_actualizar;
	private GridResolucion grid_Actualizacion_baja;
	private GridActbaja grid_Detalle = new GridActbaja();
	private final MovimientoImpl movimiento_impl = new MovimientoImpl();
	private Movimiento data;
	
	public VActbajaM() {
		
		this.frm_Actualizacion_baja = new FormActbaja();
		this.btn_actualizar = new Button("Actualizacion Resolucion");
		this.btn_imprimir = new Button("Imprimir Resolucion");
		this.btn_actualizar.addClickListener(this);
		this.btn_imprimir.addClickListener(this);
		
		this.grid_Actualizacion_baja = new GridResolucion();
		this.grid_Actualizacion_baja.addSelectionListener(this);
		this.hl_errores = new CssLayout();
		
		addComponent(buildNavBar());
		addComponent(buildFormContent());
		addComponent(buildButtonBar());
	}
	
	private Component buildFormContent() {
		
		VerticalLayout formContent = new VerticalLayout();
		formContent.setSpacing(true);
		Panel frmPanel = new Panel();
		frmPanel.setWidth("100%");
		frmPanel.setCaption("Documento Resolucion de Baja");
		frmPanel.setContent(this.frm_Actualizacion_baja);
		formContent.setMargin(true);
		formContent.addComponent(frmPanel);
		Panel gridPanel = new Panel();
		gridPanel.setWidth("100%");
		gridPanel.setCaption("Seleccione la Resolucion");
		gridPanel.setContent(this.grid_Actualizacion_baja);
		formContent.setMargin(true);
		Panel grid2Panel = new Panel();
		grid2Panel.setWidth("100%");
		grid2Panel.setCaption("Activos Fijos para dar de baja");
		grid2Panel.setContent(this.grid_Detalle);
		formContent.setMargin(true);
		formContent.addComponent(gridPanel);
		formContent.addComponent(grid2Panel);
		formContent.addComponent(frmPanel);
		this.frm_Actualizacion_baja.update();
		Responsive.makeResponsive(formContent);
		return formContent;
		
	}
	
	private Component buildNavBar() {
		Panel navPanel = new Panel();
		HorizontalLayout nav = new HorizontalLayout();
		nav.addStyleName("ait-content-nav");
		nav.addComponent(new Label("Activos >>"));
		nav.addComponent(new Label("Movimiento de Activos >>"));
		nav.addComponent(new Label("Actualizacion de Baja de Activos >>"));
		nav.addComponent(new Label("<strong>Modificar</strong>", ContentMode.HTML));
		navPanel.setContent(nav);
		return navPanel;
	}
	
	private Component buildButtonBar() {
		CssLayout buttonContent = new CssLayout();
		this.btn_actualizar.setStyleName("ait-buttons-btn");
		buttonContent.addComponent(this.btn_actualizar);
		this.btn_imprimir.setStyleName("ait-buttons-btn");
		buttonContent.addStyleName("ait-buttons");
		buttonContent.addComponent(this.btn_imprimir);
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
	public void select(SelectionEvent event) {
		
		if ((Movimiento) this.grid_Actualizacion_baja.getSelectedRow() != null) {
			data = (Movimiento) this.grid_Actualizacion_baja.getSelectedRow();
			grid_Detalle.update(data.getNro_documento(), data.getId_dependencia());
		}
		
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == this.btn_actualizar) {
			if (this.frm_Actualizacion_baja.validate()) {
				CmovimientoDocumento movimiento = new CmovimientoDocumento();
				movimiento.setNro_documento_referencia(data.getNro_documento_referencia());
				movimiento.setId_dependencia(data.getId_dependencia());
				movimiento.setFecha_nro_referencia(data.getFecha_nro_referencia());
				movimiento.setNombre_documento(frm_Actualizacion_baja.getNombreDocumento());
				movimiento.setDireccion_documento(frm_Actualizacion_baja.getDireccionDocumento());
				movimiento.setTipo_movimiento(data.getTipo_movimiento());
				movimiento.setNo_documento(data.getNro_documento());
				this.movimiento_impl.update(movimiento);
				this.grid_Detalle = new GridActbaja();
				Notification.show(Messages.SUCCESS_MESSAGE);
			} else {
				Notification.show(Messages.NOT_SUCCESS_MESSAGE, Type.ERROR_MESSAGE);
			}
			buildMessages(this.frm_Actualizacion_baja.getMensajes());
			this.frm_Actualizacion_baja.clearMessages();
			
		}
		buildMessages(this.frm_Actualizacion_baja.getMensajes());
		this.frm_Actualizacion_baja.clearMessages();
		
		if (event.getButton() == this.btn_imprimir) {
		}
	}
	
}
