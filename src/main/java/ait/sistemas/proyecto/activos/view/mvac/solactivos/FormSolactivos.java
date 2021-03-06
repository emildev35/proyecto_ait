package ait.sistemas.proyecto.activos.view.mvac.solactivos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ait.sistemas.proyecto.activos.component.model.ActivoGrid;
import ait.sistemas.proyecto.activos.component.model.Movimiento;
import ait.sistemas.proyecto.activos.component.model.Detalle;
import ait.sistemas.proyecto.activos.data.model.AuxiliaresContablesModel;
import ait.sistemas.proyecto.activos.data.model.GruposContablesModel;
import ait.sistemas.proyecto.activos.data.service.Impl.AuxiliarImpl;
import ait.sistemas.proyecto.activos.data.service.Impl.GrupoImpl;
import ait.sistemas.proyecto.activos.data.service.Impl.MovimientoImpl;
import ait.sistemas.proyecto.common.component.BarMessage;
import ait.sistemas.proyecto.common.component.Messages;
import ait.sistemas.proyecto.common.theme.AitTheme;
import ait.sistemas.proyecto.seguridad.component.model.SessionModel;

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
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;

public class FormSolactivos extends GridLayout implements ValueChangeListener {
	private static final long serialVersionUID = 1L;
	
	private TextField txt_solicitante = new TextField("Funcionario Solicitante");
	private TextField txt_id_solicitud = new TextField("No. Solicitud");
	public DateField dtf_fecha_soliciud = new DateField("Fecha Solicitud");
	
	public ComboBox cb_grupo_contable = new ComboBox("Grupo Contable");
	public ComboBox cb_auxiliar_contable = new ComboBox("Auxiliar Contable");
	
	private List<BarMessage> mensajes = new ArrayList<BarMessage>();
	
	private PropertysetItem pitm_solicitud = new PropertysetItem();
	private FieldGroup binder_solicitud;
	
	private MovimientoImpl movimientoimpl = new MovimientoImpl();
	private final GrupoImpl grupoimpl = new GrupoImpl();
	private final AuxiliarImpl auxiliarimpl = new AuxiliarImpl();
	
	private GridSolactivos grid_solicitud = new GridSolactivos();
	private GridSolactivos grid_solicitados = new GridSolactivos();
	
	private final SessionModel session = (SessionModel) UI.getCurrent().getSession().getAttribute("user");
	private List<ActivoGrid> activosSolicitados = new ArrayList<ActivoGrid>();
	
	public FormSolactivos() {
		
		super(6, 2);
		setSpacing(true);
		setWidth("100%");
		setMargin(true);
		pitm_solicitud.addItemProperty("id_solicitud", new ObjectProperty<String>(""));
		pitm_solicitud.addItemProperty("fecha_solicitud", new ObjectProperty<Date>(new Date()));
		pitm_solicitud.addItemProperty("grupo_contable", new ObjectProperty<GruposContablesModel>(new GruposContablesModel()));
		pitm_solicitud.addItemProperty("auxiliar_contable", new ObjectProperty<AuxiliaresContablesModel>(
				new AuxiliaresContablesModel()));
		
		this.binder_solicitud = new FieldGroup(this.pitm_solicitud);
		
		binder_solicitud.bind(this.txt_id_solicitud, "id_solicitud");
		binder_solicitud.bind(this.dtf_fecha_soliciud, "fecha_solicitud");
		binder_solicitud.bind(this.cb_grupo_contable, "grupo_contable");
		binder_solicitud.bind(this.cb_auxiliar_contable, "auxiliar_contable");
		binder_solicitud.clear();
		
		this.txt_id_solicitud.setEnabled(false);
		this.txt_id_solicitud.addValidator(new NullValidator("No Nulo", false));
		
		this.dtf_fecha_soliciud.setEnabled(false);
		this.dtf_fecha_soliciud.addValidator(new NullValidator("No Nulo", false));
		
		this.cb_grupo_contable.setRequired(true);
		this.cb_grupo_contable.addValidator(new NullValidator("No Nulo", false));
		cb_grupo_contable.setInputPrompt("Seleccione un Grupo Contable");
		cb_grupo_contable.addValueChangeListener(this);
		this.cb_auxiliar_contable.setRequired(true);
		cb_auxiliar_contable.addValueChangeListener(this);
		this.cb_auxiliar_contable.addValidator(new NullValidator("No Nulo", false));
		cb_auxiliar_contable.setInputPrompt("Seleccione un Auxiliar Contable");
		
		txt_id_solicitud.setWidth("90%");
		dtf_fecha_soliciud.setWidth("90%");
		cb_grupo_contable.setWidth("90%");
		cb_auxiliar_contable.setWidth("90%");
		txt_solicitante.setWidth("90%");
		txt_solicitante.setEnabled(false);
		txt_solicitante.setValue(session.getFull_name());
		
		fillcbGrupoContable();
		buildContent();
		buildId();
		Responsive.makeResponsive(this);
	}
	
	private void buildId() {
		this.txt_id_solicitud.setValue(String.valueOf(movimientoimpl.getId((short) 1)));
		this.dtf_fecha_soliciud.setValue(new Date());
	}
	
	private void fillcbGrupoContable() {
		cb_grupo_contable.setNullSelectionAllowed(false);
		
		for (GruposContablesModel grupo_contable : grupoimpl.getalls()) {
			cb_grupo_contable.addItem(grupo_contable);
			cb_grupo_contable.setItemCaption(grupo_contable, grupo_contable.getGRC_Nombre_Grupo_Contable());
		}
	}
	
	private void fillcbAuxiliarContable(String id_grupo) {
		cb_auxiliar_contable.removeAllItems();
		cb_auxiliar_contable.setNullSelectionAllowed(false);
		for (AuxiliaresContablesModel auxiliar_contable : auxiliarimpl.getreporte(id_grupo)) {
			cb_auxiliar_contable.addItem(auxiliar_contable);
			cb_auxiliar_contable.setItemCaption(auxiliar_contable, auxiliar_contable.getAUC_Nombre_Auxiliar_Contable());
		}
	}
	
	private void buildContent() {
		
		Panel pn_solicitud = new Panel("SOLICITUD DE ASIGNACION DE ACTIVOS");
		Panel pn_activos = new Panel("Seleccione un Grupo y Auxiliar Contable");
		pn_solicitud.setStyleName(AitTheme.PANEL_FORM);
		pn_solicitud.setIcon(FontAwesome.EDIT);
		
		pn_activos.setStyleName(AitTheme.PANEL_FORM);
		pn_activos.setIcon(FontAwesome.EDIT);
		
		GridLayout gridl_solicitud = new GridLayout(6, 1);
		gridl_solicitud.setSizeFull();
		gridl_solicitud.setMargin(true);
		gridl_solicitud.addComponent(this.txt_solicitante, 0, 0, 3, 0);
		gridl_solicitud.addComponent(this.txt_id_solicitud, 4, 0);
		gridl_solicitud.addComponent(this.dtf_fecha_soliciud, 5, 0);
		pn_solicitud.setContent(gridl_solicitud);
		
		this.addComponent(pn_solicitud, 0, 0, 5, 0);
		
		GridLayout gridl_activos = new GridLayout(2, 1);
		gridl_activos.setSizeFull();
		gridl_activos.setMargin(true);
		gridl_activos.addComponent(this.cb_grupo_contable, 0, 0);
		gridl_activos.addComponent(this.cb_auxiliar_contable, 1, 0);
		pn_activos.setContent(gridl_activos);
		
		this.addComponent(pn_activos, 0, 1, 5, 1);
		
	}
	
	public void update() {
		binder_solicitud.clear();
	}
	
	public List<BarMessage> getMensajes() {
		return mensajes;
	}
	
	public void clearMessages() {
		this.mensajes = new ArrayList<BarMessage>();
	}
	
	public boolean validate() {
		
		try {
			this.binder_solicitud.commit();
			return true;
		} catch (CommitException e) {
			try {
				this.cb_grupo_contable.validate();
			} catch (EmptyValueException ex) {
				this.mensajes.add(new BarMessage(cb_grupo_contable.getCaption(), Messages.EMPTY_MESSAGE));
			} catch (InvalidValueException ex) {
				this.mensajes.add(new BarMessage(cb_grupo_contable.getCaption(), ex.getMessage()));
			}
			try {
				this.cb_auxiliar_contable.validate();
			} catch (EmptyValueException ex) {
				this.mensajes.add(new BarMessage(cb_auxiliar_contable.getCaption(), ex.getMessage()));
				this.mensajes.add(new BarMessage(cb_auxiliar_contable.getCaption(), Messages.EMPTY_MESSAGE));
			} catch (InvalidValueException ex) {
				this.mensajes.add(new BarMessage(cb_auxiliar_contable.getCaption(), ex.getMessage()));
			}
			
			return false;
		}
	}
	
	public Movimiento getData() {
		Movimiento result = new Movimiento();
		SessionModel usuario = (SessionModel) UI.getCurrent().getSession().getAttribute("user");
		java.sql.Timestamp fecha_registro = new java.sql.Timestamp(new Date().getTime());
		
		result.setId_dependencia(usuario.getId_dependecia());
		result.setIdUnidadOrganizacional(usuario.getId_unidad_organizacional());
		result.setNro_documento(Long.parseLong(this.txt_id_solicitud.getValue()));
		result.setFecha_movimiento(fecha_registro);
		result.setFecha_registro(fecha_registro);
		result.setCiUsuario(usuario.getCi());
		result.setObservacion("");
		result.setTipo_movimiento((short) 1);
		for (ActivoGrid activo : this.activosSolicitados) {
			Detalle detalle = new Detalle();
			detalle.setId_activo(activo.getId_activo());
			detalle.setId_unidad_organizacional(usuario.getId_unidad_organizacional());
			detalle.setId_dependencia(usuario.getId_dependecia());
			detalle.setObservacion("");
			detalle.setNro_documento(Long.parseLong(this.txt_id_solicitud.getValue()));
			detalle.setFecha_registro(fecha_registro);
			detalle.setTipo_movimiento((short) 1);
			result.addDetalle(detalle);
		}
		
		return result;
	}
	
	@Override
	public void valueChange(ValueChangeEvent event) {
		if (event.getProperty() == cb_grupo_contable && this.cb_grupo_contable.getValue() != null) {
			GruposContablesModel grupo = (GruposContablesModel) cb_grupo_contable.getValue();
			fillcbAuxiliarContable(grupo.getGRC_Grupo_Contable());
		}
		if (event.getProperty() == cb_auxiliar_contable && this.cb_auxiliar_contable.getValue() != null && this.cb_grupo_contable.getValue() != null) {
			AuxiliaresContablesModel auxiliar = (AuxiliaresContablesModel) cb_auxiliar_contable.getValue();
			buildGrid(auxiliar.getAUC_Auxiliar_Contable());
		}
	}
	
	private void buildGrid(String auc_Auxiliar_Contable) {
		GruposContablesModel grupo = (GruposContablesModel) cb_grupo_contable.getValue();
		this.grid_solicitud.update(grupo.getGRC_Grupo_Contable(), auc_Auxiliar_Contable);
	}
	
	public Component getgrid_solicitud() {
		return this.grid_solicitud;
	}
	
	public Component getgridSolicitados() {
		return this.grid_solicitados;
	}
	
	public void clear() {
		this.binder_solicitud.clear();
		buildId();
		this.grid_solicitud.update("", "");
		this.grid_solicitados.update("", "");
		this.activosSolicitados.clear();
	}
	
	public void addActivo() {
		
		for (Object row : grid_solicitud.getSelectedRows()) {
			ActivoGrid activo = (ActivoGrid) row;
			if(!activosSolicitados.contains(activo)){
				this.activosSolicitados.add(activo);
			}
		}
		this.grid_solicitados.update(this.activosSolicitados);
	}
	
	public void deleteActivo() {
		for (Object row : grid_solicitados.getSelectedRows()) {
			ActivoGrid activo = (ActivoGrid) row;
			this.activosSolicitados.remove(activo);
		}
		this.grid_solicitados.update(this.activosSolicitados);
		
	}
}
