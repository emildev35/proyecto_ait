package ait.sistemas.proyecto.activos.view.mvac.actbaja;

import ait.sistemas.proyecto.activos.component.model.Detalle;
import ait.sistemas.proyecto.activos.data.service.Impl.MovimientoImpl;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;

	public class GridActbaja extends Grid{

		private MovimientoImpl movimiento_impl = new MovimientoImpl();
		private static final long serialVersionUID = 1L;
		private BeanItemContainer<Detalle> bean_detalle;

		public GridActbaja() {
			bean_detalle = new BeanItemContainer<Detalle>(Detalle.class);
			setContainerDataSource(bean_detalle);
			setHeightMode(HeightMode.ROW);
			setWidth("100%");
			removeColumn("id_dependencia");
			removeColumn("id_unidad_organizacional_origen");
			removeColumn("nro_documento");
			removeColumn("tipo_movimiento");
			removeColumn("fecha_registro");
			removeColumn("id_motivo_baja");
			setColumnOrder("id_activo", "nombre_activo","motivo_baja","observacion");
			 	
			Grid.Column id_activoColumn = this.getColumn("id_activo");
			Grid.Column nombre_activoColumn = this.getColumn("nombre_activo");
			Grid.Column motivo_bajaColumn = this.getColumn("motivo_baja");
			Grid.Column observacionColumn = this.getColumn("observacion");
			
			id_activoColumn.setHeaderCaption("Codigo");
			nombre_activoColumn.setHeaderCaption("Nombre del Activo");
			motivo_bajaColumn.setHeaderCaption("Causal o motivo de baja");
			observacionColumn.setHeaderCaption("Observaciones");
			
			setHeightByRows(5);
		}
		public void update(long nro_documenro, short id_dependencia) {
			this.removeAllColumns();
			bean_detalle = null;
			buildGrid( nro_documenro,  id_dependencia);
		}
		public void buildGrid(long nro_documenro, short id_dependencia){
			bean_detalle = new BeanItemContainer<Detalle>(Detalle.class);
			bean_detalle.addAll(movimiento_impl.getDetallesbyCmovimiento(nro_documenro, id_dependencia));
			setContainerDataSource(bean_detalle);

//			removeColumn("id_detalle");
			removeColumn("id_dependencia");
			removeColumn("id_unidad_organizacional_origen");
			removeColumn("nro_documento");
			removeColumn("tipo_movimiento");
			removeColumn("fecha_registro");
			removeColumn("id_motivo_baja");
			

			setColumnOrder("id_activo", "nombre_activo","motivo_baja","observacion");
			
			Grid.Column id_activoColumn = this.getColumn("id_activo");
			Grid.Column nombre_activoColumn = this.getColumn("nombre_activo");
			Grid.Column motivo_bajaColumn = this.getColumn("motivo_baja");
			Grid.Column observacionColumn = this.getColumn("observacion");
			
			id_activoColumn.setHeaderCaption("Codigo");
			nombre_activoColumn.setHeaderCaption("Nombre del Activo");
			motivo_bajaColumn.setHeaderCaption("Causal o motivo de baja");
			observacionColumn.setHeaderCaption("Observaciones");
			
			Responsive.makeResponsive(this);
		}
	}

