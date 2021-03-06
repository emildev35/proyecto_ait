package ait.sistemas.proyecto.activos.view.rrhh.generadorpin;

import ait.sistemas.proyecto.activos.data.model.TipoAutorizacionModel;
import ait.sistemas.proyecto.activos.data.service.Impl.TipoAutorizacionImpl;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Grid;

public class GridTipoAutorizacion extends Grid {
	
	private static final long serialVersionUID = 1L;
	final TipoAutorizacionImpl tipo_autorizacionimpl = new TipoAutorizacionImpl();
	private BeanItemContainer<TipoAutorizacionModel> bean_autorizacion;
	
	public GridTipoAutorizacion(String usuario) {
		bean_autorizacion = new BeanItemContainer<TipoAutorizacionModel>(TipoAutorizacionModel.class,
				this.tipo_autorizacionimpl.getallbyusuario(usuario));
		setContainerDataSource(bean_autorizacion);
		setHeightMode(HeightMode.ROW);
		setSelectionMode(SelectionMode.NONE);
		setHeightByRows(7);
		removeColumn("dependencia_id");
		removeColumn("ci");
		removeColumn("nivel_autorizacion_id");
		removeColumn("tipo_movimiento_id");
		try {
			removeColumn("unidadOrganizacionalId");
			removeColumn("fecha_resgistro");
		} catch (Exception ex) {
		}
		removeColumn("usuario_id");
		removeColumn("unidadOrganizacional");
		removeColumn("dependencia");
		removeColumn("servidor_publico");
		
		setWidth("100%");
		
		setColumnOrder("tipo_movimiento", "orden", "nivel_autorizacion");
		
		getColumn("tipo_movimiento").setHeaderCaption("Tipos de Movimiento").setExpandRatio(3);
		getColumn("orden").setHeaderCaption("Orden").setExpandRatio(1);
		getColumn("nivel_autorizacion").setHeaderCaption("Nivel de Autorizacion").setExpandRatio(2);
		
		Responsive.makeResponsive(this);
	}
}
