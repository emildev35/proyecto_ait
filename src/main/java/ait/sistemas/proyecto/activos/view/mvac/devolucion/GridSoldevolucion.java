package ait.sistemas.proyecto.activos.view.mvac.devolucion;

import java.util.Locale;

import ait.sistemas.proyecto.activos.component.model.Movimiento;
import ait.sistemas.proyecto.activos.data.service.Impl.ActasImpl;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;
import com.vaadin.ui.renderers.DateRenderer;

public class GridSoldevolucion extends Grid{

	private ActasImpl acta_impl = new ActasImpl();
	private static final long serialVersionUID = 1L;
	private BeanItemContainer<Movimiento> bean_Movimiento;

	public GridSoldevolucion() {
		buildGrid();
	}
	public void update() {
		this.removeAllColumns();
		bean_Movimiento = null;
		buildGrid();
	}
	public void buildGrid(){
		bean_Movimiento = new BeanItemContainer<Movimiento>(Movimiento.class);
		Movimiento movimiento = new Movimiento();
		movimiento.setTipo_movimiento((short) 3);
		bean_Movimiento.addAll(acta_impl.getSolicitudDevolucion(movimiento));
		setContainerDataSource(bean_Movimiento);
		setHeightMode(HeightMode.ROW);
		setHeightByRows(5);
		setWidth("100%");
		
		
		removeColumn("detalles");
		removeColumn("id_dependencia");
		removeColumn("id_unidad_organizacional_origen");
		removeColumn("fecha_movimiento");
		removeColumn("nro_documento_referencia");
		removeColumn("fecha_nro_referencia");
		removeColumn("observacion");
		removeColumn("tipo_movimiento");
		removeColumn("usuario");
		removeColumn("no_acta");
		removeColumn("fecha_acta");
		removeColumn("id_estado_soporte");
		removeColumn("id_subsistema");
		removeColumn("tipo_soporte");
		removeColumn("tipo_movimiento_referencia");
		removeColumn("dependencia");
		removeColumn ("dependencia_destino");
		removeColumn ("id_dependencia_destino");
		removeColumn ("tipo_movimiento_nuevo");
		
		setColumnOrder("fecha_registro","nro_documento", "solicitante");
		
		Grid.Column fechaColumn = this.getColumn("fecha_registro");
		Grid.Column no_solicitudColumn = this.getColumn("nro_documento");
		Grid.Column solicitanteColumn = this.getColumn("solicitante");

		//fechaColumn.setHeaderCaption("Fecha").setExpandRatio(1);
		fechaColumn.setRenderer(new DateRenderer("%1$te de %1$tB,  %1$tY",
				new Locale("es", "BO")));
		no_solicitudColumn.setHeaderCaption("N° Solicitud");
		solicitanteColumn.setHeaderCaption("Solicitante");
		
	//	sort("AUC_Auxiliar_Contable", SortDirection.ASCENDING);
		Responsive.makeResponsive(this);
	}
}

