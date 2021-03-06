package ait.sistemas.proyecto.seguridad.view.estr.subsistema;

import org.vaadin.dialogs.ConfirmDialog;

import ait.sistemas.proyecto.common.component.Messages;
import ait.sistemas.proyecto.seguridad.component.ComboBoxIcons;
import ait.sistemas.proyecto.seguridad.data.model.Arbol_menus;
import ait.sistemas.proyecto.seguridad.data.service.Impl.MenuImpl;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
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
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

public class VSubSistemaB extends VerticalLayout implements View, SelectionListener, ClickListener,
		org.vaadin.dialogs.ConfirmDialog.Listener {
	private static final long serialVersionUID = 1L;
	
	private Button btn_submit;
	private Button btn_limpiar;
	private TextField txt_identificador;
	private TextField txt_id_subsistema;
	private TextField txt_nombre_subsistema;
	private TextField txt_nombre_programa;
	private ComboBoxIcons cb_icons;
	private GridSubSistemas gridSubSistemas;
	
	final PropertysetItem pitmSubSistema = new PropertysetItem();
	private FieldGroup binderSubSistema;
	
	private CssLayout hl_errores;
	
	private MenuImpl menuImpl;
	private Arbol_menus subsistema;
	
	public VSubSistemaB() {
		
		this.hl_errores = new CssLayout();
		this.txt_identificador = new TextField("Identificador");
		this.txt_id_subsistema = new TextField("Id. Sub-Sistema");
		this.txt_nombre_subsistema = new TextField("Nombre del Sub-Sistema");
		this.txt_nombre_programa = new TextField("Nombre del Programa");
		this.cb_icons = new ComboBoxIcons("Icono del Sus-Sistema");
		this.btn_submit = new Button("Eliminar");
		this.btn_limpiar = new Button("Limpiar");
		this.gridSubSistemas = new GridSubSistemas();
		this.menuImpl = new MenuImpl();
		
		pitmSubSistema.addItemProperty("identificador", new ObjectProperty<Integer>(0));
		pitmSubSistema.addItemProperty("id_subsistema", new ObjectProperty<Integer>(0));
		pitmSubSistema.addItemProperty("nombre_subsistema", new ObjectProperty<String>(""));
		pitmSubSistema.addItemProperty("nombre_programa", new ObjectProperty<String>(""));
		pitmSubSistema.addItemProperty("iconos", new ObjectProperty<String>(""));
		
		binderSubSistema = new FieldGroup(pitmSubSistema);
		binderSubSistema.bind(this.txt_identificador, "identificador");
		binderSubSistema.bind(this.txt_id_subsistema, "id_subsistema");
		binderSubSistema.bind(this.txt_nombre_subsistema, "nombre_subsistema");
		binderSubSistema.bind(this.txt_nombre_programa, "nombre_programa");
		binderSubSistema.bind(this.cb_icons, "iconos");
		
		this.txt_identificador.setEnabled(false);
		this.txt_nombre_subsistema.setEnabled(false);
		this.txt_nombre_subsistema.setEnabled(false);
		this.txt_nombre_programa.setEnabled(false);
		this.cb_icons.setEnabled(false);
		
		this.txt_identificador.setEnabled(false);
		this.txt_id_subsistema.setEnabled(false);
		this.txt_identificador.setEnabled(false);
		
		this.txt_nombre_subsistema.setWidth("100%");
		this.txt_nombre_programa.setWidth("100%");
		this.cb_icons.setWidth("100%");
		
		generarIdentificador();
		generarIdSubsistema();
		buildGrid();
		
		addComponent(buildNavBar());
		addComponent(buildFormContent());
		addComponent(buildButtonBar());
		Responsive.makeResponsive(this);
	}
	
	private void buildGrid() {
		this.gridSubSistemas.addSelectionListener(this);
		this.gridSubSistemas.addSelectionListener(this);
	}
	
	private Component buildFormContent() {
		VerticalLayout vl_content = new VerticalLayout();
		vl_content.setMargin(true);
		GridLayout frmAddSubsistema = new GridLayout(4, 2);
		frmAddSubsistema.setWidth("85%");
		frmAddSubsistema.setSpacing(true);
		frmAddSubsistema.setColumnExpandRatio(0, 1);
		frmAddSubsistema.setColumnExpandRatio(1, 1);
		frmAddSubsistema.setColumnExpandRatio(2, 4);
		frmAddSubsistema.setColumnExpandRatio(3, 4);
		frmAddSubsistema.addComponent(this.txt_identificador, 0, 0);
		frmAddSubsistema.addComponent(this.txt_id_subsistema, 1, 0);
		frmAddSubsistema.addComponent(this.txt_nombre_subsistema, 2, 0);
		frmAddSubsistema.addComponent(this.txt_nombre_programa, 2, 1);
		frmAddSubsistema.addComponent(this.cb_icons, 3, 0);
		Responsive.makeResponsive(frmAddSubsistema);
		vl_content.addComponent(this.gridSubSistemas);
		vl_content.addComponent(frmAddSubsistema);
		Responsive.makeResponsive(vl_content);
		return vl_content;
	}
	
	private Component buildNavBar() {
		Panel navPanel = new Panel();
		HorizontalLayout nav = new HorizontalLayout();
		nav.addStyleName("ait-content-nav");
		nav.addComponent(new Label("Seguridad » "));
		nav.addComponent(new Label("Estructura del Sistema » "));
		nav.addComponent(new Label("Su-Sistema » "));
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
		return buttonContent;
	}
	
	private void generarIdentificador() {
		this.txt_identificador.setValue(this.menuImpl.generateId());
	}
	
	private void generarIdSubsistema() {
		this.txt_id_subsistema.setValue(this.menuImpl.generateSubSistemaId());
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		
	}
	
	private void eliminar() {
		try {
			this.menuImpl.deleteSubsistema(subsistema.getAME_Id_Identificador());
			Notification.show(Messages.SUCCESS_MESSAGE);
			this.binderSubSistema.clear();
			generarIdentificador();
			generarIdSubsistema();
			Notification.show(Messages.SUCCESS_MESSAGE);
			this.gridSubSistemas.update();
			
		} catch (Exception e) {
			Notification.show(Messages.NOT_SUCCESS_MESSAGE, Type.ERROR_MESSAGE);
			buildError(Messages.FOREIGN_RELATION_ERROR(subsistema.getAME_Nombre()));
		}
	}
	
	@Override
	public void buttonClick(ClickEvent event) {
		if (event.getButton() == this.btn_submit) {
			ConfirmDialog.show(getUI(), "Eliminar " + this.subsistema.getAME_Nombre(),
					"Esta Uds Seguro que Desea Eliminar el Sub-Sistema" + this.subsistema.getAME_Nombre(), "Si", "No", this);
		}
		if (event.getButton() == this.btn_limpiar) {
			this.binderSubSistema.clear();
			generarIdentificador();
			generarIdSubsistema();
		}
	}
	
	private void buildError(String strmessage) {
		this.hl_errores.removeAllComponents();
		hl_errores.addStyleName("ait-error-bar");
		this.addComponent(this.hl_errores);
		Label lbError = new Label(new Label(strmessage));
		this.hl_errores.addComponent(lbError);
		
	}
	
	@Override
	public void select(SelectionEvent event) {
		if ((Arbol_menus) this.gridSubSistemas.getSelectedRow() != null) {
			this.subsistema = (Arbol_menus) this.gridSubSistemas.getSelectedRow();
			this.txt_identificador.setValue(subsistema.getAME_Id_Identificador() + "");
			this.txt_id_subsistema.setValue(subsistema.getAME_Id_Subsistema() + "");
			this.txt_nombre_subsistema.setValue(subsistema.getAME_Nombre());
			this.txt_nombre_programa.setValue(subsistema.getAME_Programa());
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