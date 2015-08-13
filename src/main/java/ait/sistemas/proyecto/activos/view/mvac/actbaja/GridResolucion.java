package ait.sistemas.proyecto.activos.view.mvac.actbaja;

import ait.sistemas.proyecto.activos.component.model.Movimiento;
import ait.sistemas.proyecto.activos.data.service.Impl.MovimientoImpl;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;

	public class GridResolucion extends Grid{

		private MovimientoImpl movimiento_impl = new MovimientoImpl();
		private static final long serialVersionUID = 1L;
		private BeanItemContainer<Movimiento> bean_actualizacion_baja;

		public GridResolucion() {
			buildGrid();
		}
		public void update() {
			this.removeAllColumns();
			bean_actualizacion_baja = null;
			buildGrid();
		}
		public void buildGrid(){
			bean_actualizacion_baja = new BeanItemContainer<Movimiento>(Movimiento.class);
			bean_actualizacion_baja.addAll(movimiento_impl.getActivos_Resolucion());
			setContainerDataSource(bean_actualizacion_baja);
			setHeightMode(HeightMode.ROW);
			setHeightByRows(5);

			removeColumn("PAP_Fecha_Registro");
			
			setWidth("100%");

			setColumnOrder("nro_documento_referencia", "fecha_nro_referencia","nro_documento","fecha_registro");
			
			Grid.Column No_Documento_ReferenciaColumn = this.getColumn("nro_documento_referencia");
			Grid.Column Fecha_Documento_ReferenciaColumn = this.getColumn("fecha_nro_referencia");
			Grid.Column No_DocumentoColumn = this.getColumn("nro_documento");
			Grid.Column Fecha_DocumentoColumn = this.getColumn("fecha_registro");
			
			No_Documento_ReferenciaColumn.setHeaderCaption("No Resolucion");
			Fecha_Documento_ReferenciaColumn.setHeaderCaption("Fecha Resolucion");
			No_DocumentoColumn.setHeaderCaption("No Informe");
			Fecha_DocumentoColumn.setHeaderCaption("Fecha Informe");
			
			Responsive.makeResponsive(this);
		}
	}

