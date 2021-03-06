package ait.sistemas.proyecto.activos.view.rrhh.autorizado;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ait.sistemas.proyecto.activos.data.model.TipoAutorizacionModel;
import ait.sistemas.proyecto.activos.data.model.Tipos_Movimiento;
import ait.sistemas.proyecto.activos.data.model_rrhh.Dependencia;
import ait.sistemas.proyecto.activos.data.model_rrhh.PersonalModel;
import ait.sistemas.proyecto.activos.data.model_rrhh.Unidades_Organizacionale;
import ait.sistemas.proyecto.activos.data.service.Impl.DependenciaImpl;
import ait.sistemas.proyecto.activos.data.service.Impl.PersonalImpl;
import ait.sistemas.proyecto.activos.data.service.Impl.TiposmovImpl;
import ait.sistemas.proyecto.activos.data.service.Impl.UnidadImpl;
import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.common.component.Messages;
import ait.sistemas.proyecto.common.theme.AitTheme;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.Validator.EmptyValueException;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;
import com.vaadin.data.validator.NullValidator;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;

public class FormAutorizado extends GridLayout implements ValueChangeListener {
	private static final long serialVersionUID = 1L;
	
	public ComboBox cb_tipo_movimiento = new ComboBox("Tipo de  Movimiento");
	private TextField txt_orden = new TextField("Orden");
	public ComboBox cb_dependencia = new ComboBox("Dependencia");
	public ComboBox cb_dependencia_movimiento = new ComboBox("Dependencia");
	public ComboBox cb_dependencia_transferencia = new ComboBox("Dependencia Transferencia");
	public ComboBox cb_unidad_movimiento = new ComboBox("Unidad Organizacional");
	
	public ComboBox cb_unidad = new ComboBox("Unidad Organizacional");
	public ComboBox cb_servidor_publico = new ComboBox("Sevidor Publico");
	public ComboBox cb_nivel_autorizacion = new ComboBox("Nivel de Autorizacion");
	private List<BarMessage> mensajes = new ArrayList<BarMessage>();
	
	private final DependenciaImpl dependencia_impl = new DependenciaImpl();
	private final UnidadImpl unidad_impl = new UnidadImpl();
	private final PersonalImpl personal_impl = new PersonalImpl();
	private final TiposmovImpl tipomov_impl = new TiposmovImpl();
	
	private PropertysetItem pitm_autorizacion = new PropertysetItem();
	private FieldGroup binder_autorizacion;
	
	public FormAutorizado() {
		super(4, 2);
		setSpacing(true);
		setMargin(true);
		setWidth("100%");
		
		pitm_autorizacion.addItemProperty("tipo_movimiento", new ObjectProperty<Tipos_Movimiento>(new Tipos_Movimiento()));
		pitm_autorizacion.addItemProperty("dependencia", new ObjectProperty<Dependencia>(new Dependencia()));
		pitm_autorizacion.addItemProperty("dependencia_transferencia", new ObjectProperty<Dependencia>(new Dependencia()));
		pitm_autorizacion.addItemProperty("unidad", new ObjectProperty<Unidades_Organizacionale>(new Unidades_Organizacionale()));
		pitm_autorizacion.addItemProperty("servidor_publico", new ObjectProperty<PersonalModel>(new PersonalModel()));
		pitm_autorizacion.addItemProperty("nivel_autorizacion", new ObjectProperty<Integer>(0));
		pitm_autorizacion.addItemProperty("orden", new ObjectProperty<Integer>(1));
		
		this.binder_autorizacion = new FieldGroup(this.pitm_autorizacion);
		
		binder_autorizacion.bind(this.cb_nivel_autorizacion, "nivel_autorizacion");
		binder_autorizacion.bind(this.cb_dependencia_movimiento, "dependencia");
		binder_autorizacion.bind(this.cb_dependencia_transferencia, "dependencia_transferencia");
		binder_autorizacion.bind(this.cb_unidad_movimiento, "unidad");
		binder_autorizacion.bind(this.cb_tipo_movimiento, "tipo_movimiento");
		binder_autorizacion.bind(this.cb_servidor_publico, "servidor_publico");
		binder_autorizacion.bind(this.txt_orden, "orden");
		
		this.cb_servidor_publico.setRequired(true);
		this.cb_servidor_publico.addValidator(new NullValidator("No Nulo", false));
		
		this.cb_dependencia_movimiento.setRequired(true);
		this.cb_dependencia_movimiento.addValidator(new NullValidator("No Nulo", false));
		
		this.cb_dependencia_transferencia.setRequired(true);
		this.cb_dependencia_transferencia.addValidator(new NullValidator("No Nulo", false));
		
		this.cb_unidad_movimiento.setRequired(true);
		this.cb_unidad_movimiento.addValidator(new NullValidator("No Nulo", false));
		
		this.txt_orden.setRequired(true);
		this.txt_orden.addValidator(new NullValidator("No Nulo", false));
		// this.txt_orden.addValidator(new RegexpValidator("[0-9]",
		// Messages.NUMBER_VALUE));
		
		this.cb_nivel_autorizacion.setRequired(true);
		this.cb_nivel_autorizacion.addValidator(new NullValidator("No Nulo", false));
		
		this.cb_tipo_movimiento.setRequired(true);
		this.cb_tipo_movimiento.addValidator(new NullValidator("No Nulo", false));
		
		this.cb_dependencia.addValueChangeListener(this);
		this.cb_unidad.addValueChangeListener(this);
		this.cb_dependencia_movimiento.addValueChangeListener(this);
		this.cb_tipo_movimiento.addValueChangeListener(this);
		this.cb_unidad_movimiento.addValueChangeListener(this);
		this.cb_dependencia_transferencia.addValueChangeListener(this);
		
		txt_orden.setWidth("100%");
		cb_dependencia.setWidth("100%");
		cb_dependencia_movimiento.setWidth("100%");
		cb_dependencia_transferencia.setWidth("100%");
		cb_unidad.setWidth("100%");
		cb_servidor_publico.setWidth("100%");
		cb_nivel_autorizacion.setWidth("100%");
		cb_tipo_movimiento.setWidth("100%");
		cb_unidad_movimiento.setWidth("100%");
		
		this.txt_orden.setEnabled(false);
		
		buildContent();
		buildCbDependencia();
		buildCbTipoMovimiento();
		buildCbNivelAutorizacion();
		Responsive.makeResponsive(this);
		
		cb_unidad.setInputPrompt("Seleccione una Unidad Organizacional");
		cb_unidad_movimiento.setInputPrompt("Seleccione una Unidad Organizacional");
		cb_servidor_publico.setInputPrompt("Seleccion un Servidor Publico");
		
		setColumnExpandRatio(0, 1);
		setColumnExpandRatio(1, 1);
		setColumnExpandRatio(2, 1);
		setColumnExpandRatio(3, 0.5f);
		
	}
	
	private void buildContent() {
		this.binder_autorizacion.clear();
		this.txt_orden.setValue("0");
		
		GridLayout gridlMovimiento = new GridLayout(3, 1);
		gridlMovimiento.setMargin(true);
		gridlMovimiento.setSpacing(true);
		gridlMovimiento.setWidth("100%");
		
		gridlMovimiento.setColumnExpandRatio(0, 0.5f);
		gridlMovimiento.setColumnExpandRatio(1, 1);
		gridlMovimiento.setColumnExpandRatio(2, 1);
		
		gridlMovimiento.addComponent(this.cb_dependencia_movimiento, 0, 0);
		gridlMovimiento.addComponent(this.cb_unidad_movimiento, 1, 0);
		gridlMovimiento.addComponent(this.cb_tipo_movimiento, 2, 0);
		
		GridLayout gridlTransferencia = new GridLayout();
		gridlTransferencia.setMargin(true);
		gridlTransferencia.setSpacing(true);
		gridlTransferencia.setWidth("100%");
		gridlTransferencia.addComponent(this.cb_dependencia_transferencia);
		Panel pnTransferencia = new Panel("DEPENDENCIA DE LA TRANSFERENCIA");
		pnTransferencia.setStyleName(AitTheme.PANEL_FORM);
		pnTransferencia.setIcon(FontAwesome.EDIT);
		pnTransferencia.setContent(gridlTransferencia);
		
		GridLayout gridlFuncionario = new GridLayout(5, 1);
		gridlFuncionario.setMargin(true);
		gridlFuncionario.setSpacing(true);
		gridlFuncionario.setWidth("100%");
		gridlFuncionario.setColumnExpandRatio(0, 0.1f);
		gridlFuncionario.setColumnExpandRatio(1, 0.3f);
		gridlFuncionario.setColumnExpandRatio(2, 1);
		gridlFuncionario.setColumnExpandRatio(3, 1);
		gridlFuncionario.setColumnExpandRatio(4, 1);
		
		gridlFuncionario.addComponent(this.txt_orden, 0, 0);
		gridlFuncionario.addComponent(this.cb_dependencia, 1, 0);
		gridlFuncionario.addComponent(this.cb_unidad, 2, 0);
		gridlFuncionario.addComponent(this.cb_servidor_publico, 3, 0);
		gridlFuncionario.addComponent(this.cb_nivel_autorizacion, 4, 0);
		
		Panel pnMovimiento = new Panel("DEPENDENCIA EN CASO DE TRANSFERENCIA");
		pnMovimiento.setContent(gridlMovimiento);
		pnMovimiento.setStyleName(AitTheme.PANEL_FORM);
		pnMovimiento.setIcon(FontAwesome.EDIT);
		Panel pnFuncionario = new Panel("REGISTRO DE DATOS FUNCIONARIO CON AUTORIZACION");
		pnFuncionario.setContent(gridlFuncionario);
		pnFuncionario.setStyleName(AitTheme.PANEL_FORM);
		pnFuncionario.setIcon(FontAwesome.EDIT);
		
		addComponent(pnMovimiento, 0, 0, 2, 0);
		addComponent(pnTransferencia, 3, 0);
		addComponent(pnFuncionario, 0, 1, 3, 1);
	}
	
	public void update() {
		binder_autorizacion.clear();
		txt_orden.setValue("1");
		cb_unidad.clear();
		cb_servidor_publico.clear();
		cb_dependencia.clear();
	}
	
	public List<BarMessage> getMensajes() {
		return mensajes;
	}
	
	public void clearMessages() {
		this.mensajes = new ArrayList<BarMessage>();
	}
	
	public void buildCbDependencia() {
		cb_dependencia.setInputPrompt("Seleccione una Dependencia");
		cb_dependencia_transferencia.setInputPrompt("Seleccione una Dependencia");
		cb_dependencia.setNullSelectionAllowed(false);
		cb_dependencia_transferencia.setNullSelectionAllowed(false);
		cb_dependencia_movimiento.setInputPrompt("Seleccione una Dependencia");
		cb_dependencia_movimiento.setNullSelectionAllowed(false);
		for (Dependencia dependencia : dependencia_impl.getall()) {
			this.cb_dependencia.addItem(dependencia);
			this.cb_dependencia.setItemCaption(dependencia, dependencia.getDEP_Sigla());
			
			this.cb_dependencia_movimiento.addItem(dependencia);
			this.cb_dependencia_movimiento.setItemCaption(dependencia, dependencia.getDEP_Sigla());
			
			this.cb_dependencia_transferencia.addItem(dependencia);
			this.cb_dependencia_transferencia.setItemCaption(dependencia, dependencia.getDEP_Sigla());
		}
	}
	
	public void buildCbNivelAutorizacion() {
		cb_nivel_autorizacion.setInputPrompt("Seleccione un Nivel de Autorizacion");
		cb_nivel_autorizacion.setNullSelectionAllowed(false);
		this.cb_nivel_autorizacion.addItem(1);
		this.cb_nivel_autorizacion.setItemCaption(1, "Unidad Organizacinal");
		this.cb_nivel_autorizacion.addItem(2);
		this.cb_nivel_autorizacion.setItemCaption(2, "Dependencia");
		this.cb_nivel_autorizacion.addItem(3);
		this.cb_nivel_autorizacion.setItemCaption(3, "Nacional");
	}
	
	public void buildCbTipoMovimiento() {
		cb_tipo_movimiento.setInputPrompt("Seleccione un Tipo de Movimiento");
		cb_tipo_movimiento.setNullSelectionAllowed(false);
		for (Tipos_Movimiento tipo_mov : tipomov_impl.getall()) {
			this.cb_tipo_movimiento.addItem(tipo_mov);
			this.cb_tipo_movimiento.setItemCaption(tipo_mov, tipo_mov.getTMV_Nombre_Tipo_Movimiento());
		}
	}
	
	public boolean validate() {
		try {
			this.binder_autorizacion.commit();
			return true;
		} catch (CommitException e) {
			try {
				this.cb_tipo_movimiento.validate();
			} catch (EmptyValueException ex) {
				this.mensajes.add(new BarMessage(cb_tipo_movimiento.getCaption(), Messages.EMPTY_MESSAGE));
			}
			try {
				this.txt_orden.validate();
			} catch (EmptyValueException ex) {
				this.mensajes.add(new BarMessage(txt_orden.getCaption(), Messages.EMPTY_MESSAGE));
			} catch (InvalidValueException ex) {
				this.mensajes.add(new BarMessage(txt_orden.getCaption(), ex.getMessage()));
			}
			try {
				this.cb_servidor_publico.validate();
			} catch (EmptyValueException ex) {
				this.mensajes.add(new BarMessage(cb_servidor_publico.getCaption(), Messages.EMPTY_MESSAGE));
			}
			try {
				this.cb_nivel_autorizacion.validate();
			} catch (EmptyValueException ex) {
				this.mensajes.add(new BarMessage(cb_nivel_autorizacion.getCaption(), Messages.EMPTY_MESSAGE));
			}
			try {
				this.cb_dependencia_movimiento.validate();
			} catch (EmptyValueException ex) {
				this.mensajes.add(new BarMessage(cb_dependencia_movimiento.getCaption(), Messages.EMPTY_MESSAGE));
			}
			return false;
		}
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty() == cb_dependencia && cb_dependencia.getValue() != null) {
			Dependencia dependencia = (Dependencia) cb_dependencia.getValue();
			buildCbUnidad(dependencia);
		}
		if (event.getProperty() == cb_unidad && cb_unidad.getValue() != null) {
			Unidades_Organizacionale unidad = (Unidades_Organizacionale) cb_unidad.getValue();
			buildCbServidor(unidad);
		}
		if (event.getProperty() == cb_dependencia_movimiento && cb_dependencia_movimiento.getValue() != null) {
			Dependencia dependencia = (Dependencia) cb_dependencia_movimiento.getValue();
			buildCbUnidadMovimiento(dependencia);
			cb_dependencia_transferencia.setValue(event.getProperty().getValue());
		}
			if (cb_unidad_movimiento.getValue() != null
				&& cb_dependencia_transferencia.getValue() != null 
				&& cb_tipo_movimiento.getValue() != null 
				&& cb_dependencia_movimiento.getValue() != null) {
			Dependencia dependencia = (Dependencia) cb_dependencia_movimiento.getValue();
			Dependencia dependenciaTransferencia = (Dependencia) cb_dependencia_transferencia.getValue();
			Unidades_Organizacionale unidad = (Unidades_Organizacionale) cb_unidad_movimiento.getValue();
			Tipos_Movimiento tipo_movimiento = (Tipos_Movimiento) cb_tipo_movimiento.getValue();
			buildNivelAutorizacion(dependencia.getDEP_Dependencia(), unidad.getUNO_Unidad_Organizacional(),
					dependenciaTransferencia.getDEP_Dependencia(), tipo_movimiento.getTMV_Tipo_Movimiento());
		}
		
	}
	
	private void buildCbUnidad(Dependencia dependencia) {
		cb_unidad.removeAllItems();
		cb_unidad.setInputPrompt("Seleccione una Unidad Organizacional");
		cb_unidad.setNullSelectionAllowed(false);
		for (Unidades_Organizacionale unidad : unidad_impl.getunidad(dependencia.getDEP_Dependencia())) {
			this.cb_unidad.addItem(unidad);
			this.cb_unidad.setItemCaption(unidad, unidad.getUNO_Nombre_Unidad_Organizacional());
		}
	}
	
	private void buildCbUnidadMovimiento(Dependencia dependencia) {
		cb_unidad_movimiento.removeAllItems();
		cb_unidad_movimiento.setInputPrompt("Seleccione una Unidad Organizacional");
		cb_unidad_movimiento.setNullSelectionAllowed(false);
		for (Unidades_Organizacionale unidad : unidad_impl.getunidad(dependencia.getDEP_Dependencia())) {
			this.cb_unidad_movimiento.addItem(unidad);
			this.cb_unidad_movimiento.setItemCaption(unidad, unidad.getUNO_Nombre_Unidad_Organizacional());
		}
	}
	
	private void buildCbServidor(Unidades_Organizacionale unidad) {
		cb_servidor_publico.removeAllItems();
		cb_servidor_publico.setInputPrompt("Seleccione un Servidor Publico");
		cb_servidor_publico.setNullSelectionAllowed(false);
		for (PersonalModel personal : personal_impl.getbyUnidad(unidad.getUNO_Unidad_Organizacional())) {
			this.cb_servidor_publico.addItem(personal);
			this.cb_servidor_publico.setItemCaption(
					personal,
					personal.getPER_Nombres() + " " + personal.getPER_Apellido_Paterno() + " "
							+ personal.getPER_Apellido_Materno());
		}
	}
	
	private void buildNivelAutorizacion(short dependencia, short unidad, short dependencia_transferencia, short tipo_movimiento) {
		this.txt_orden.setValue(String.valueOf(tipomov_impl.getNivelAutorizacion(dependencia, unidad, dependencia_transferencia,
				tipo_movimiento)));
	}
	
	public TipoAutorizacionModel getData() {
		TipoAutorizacionModel resul = new TipoAutorizacionModel();
		resul.setDependencia_id(((Dependencia) cb_dependencia_movimiento.getValue()).getDEP_Dependencia());
		resul.setTipo_movimiento_id(((Tipos_Movimiento) cb_tipo_movimiento.getValue()).getTMV_Tipo_Movimiento());
		resul.setDependencia_id_transferencia(((Dependencia) cb_dependencia_transferencia.getValue()).getDEP_Dependencia());
		resul.setOrden(Short.valueOf(txt_orden.getValue()));
		resul.setNivel_autorizacion_id(Short.parseShort(cb_nivel_autorizacion.getValue().toString()));
		resul.setCi(((PersonalModel) cb_servidor_publico.getValue()).getPER_CI_Empleado());
		resul.setUnidadOrganizacionalId(((Unidades_Organizacionale) cb_unidad_movimiento.getValue())
				.getUNO_Unidad_Organizacional());
		long lnMilis = new Date().getTime();
		resul.setFecha_registro(new java.sql.Date(lnMilis));
		return resul;
	}
	
}