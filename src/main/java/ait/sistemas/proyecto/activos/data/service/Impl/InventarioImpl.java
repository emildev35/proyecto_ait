package ait.sistemas.proyecto.activos.data.service.Impl;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import ait.sistemas.proyecto.activos.component.model.ActivoInventario;
import ait.sistemas.proyecto.activos.component.model.Detalle;
import ait.sistemas.proyecto.activos.component.model.InventarioGrupo;
import ait.sistemas.proyecto.activos.component.model.Movimiento;
import ait.sistemas.proyecto.activos.data.ConnecctionActivos;

public class InventarioImpl {
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public InventarioImpl() {
		this.emf = Persistence.createEntityManagerFactory("AIT-Activos");
		this.em = emf.createEntityManager();
	}
	
	public int add(Movimiento data) throws SQLException {
		ConnecctionActivos conn = new ConnecctionActivos();
		
		String str_cabezera = String.format("EXEC Mant_CInvFisico_I " + "@Dependencia_Id=%d," + "@Unidad_Organizacional_Id=%d,"
				+ "@Numero_Documento=%d," + "@Fecha_Registro='%s'," + "@Fecha_Movimiento='%s'," + "@CI_Usuario='%s',"
				+ "@Tipo_Movimiento=%d," + "@No_Documento_referencia=%s," + "@Fecha_Documento_referencia='%s'",
				data.getId_dependencia(), data.getIdUnidadOrganizacional(), data.getNro_documento(),
				new SimpleDateFormat("yyyy-dd-MM").format(data.getFecha_registro()),
				new SimpleDateFormat("yyyy-dd-MM  HH:mm:ss").format(data.getFecha_movimiento()), data.getUsuario(),
				data.getTipo_movimiento(), data.getNro_documento_referencia(),
				new SimpleDateFormat("yyyy-dd-MM  HH:mm:ss").format(data.getFecha_nro_referencia()));
		int result_cabezera = conn.callproc(str_cabezera);
		int result_detalle = 0;
		if (result_cabezera > 0) {
			for (Detalle detalle : data.getDetalles()) {
				String str_detalle = String.format("EXEC Mvac_DTomaInv_I " + "@Dependencia_Id=%d,"
						+ "@Unidad_Organizacional_Id=%d," + "@Numero_Documento=%d," + "@Fecha_Registro='%s',"
						+ "@Tipo_Movimiento=%d," + "@Activo_Id=%d," + "@Observaciones='%s', " + "@Nombre_Activo='%s', "
						+ "@CI_Empleado='%s' ", detalle.getId_dependencia(), detalle.getId_unidad_organizacional(),
						detalle.getNro_documento(), new SimpleDateFormat("yyyy-dd-MM").format(detalle.getFecha_registro()),
						detalle.getTipo_movimiento(), detalle.getId_activo(), detalle.getObservacion(),
						detalle.getNombre_activo(), data.getUsuario());
				result_detalle += conn.callproc(str_detalle);
			}
			if (result_detalle == data.getDetalles().size()) {
				return 1;
			} else {
				dropmovimiento(data);
				return 0;
			}
			
		} else {
			return 0;
		}
	}
	
	public int dropmovimiento(Movimiento data) {
		int result_cabezera;
		for (Detalle detalle : data.getDetalles()) {
			
			String str_detalle = "EXEC Mvac_DMovimiento_D " + "@Dependencia_Id=?1," + "@Unidad_Organizacional_Id=?2,"
					+ "@Numero_Documento=?3," + "@Tipo_Movimiento=?4," + "@Activo_Id=?4";
			
			Query query_detalle = this.em.createNativeQuery(str_detalle);
			query_detalle.setParameter(1, detalle.getId_dependencia());
			query_detalle.setParameter(2, detalle.getId_unidad_organizacional());
			query_detalle.setParameter(3, detalle.getNro_documento());
			query_detalle.setParameter(4, detalle.getTipo_movimiento());
			query_detalle.setParameter(5, detalle.getId_activo());
			result_cabezera = (Integer) query_detalle.getSingleResult();
		}
		
		String str_cabezera = "EXEC Mvac_CMovimiento_D " + "@Dependencia_Id=?1," + "@Unidad_Organizacional_Id=?2,"
				+ "@Numero_Documento=?3," + "@Tipo_Movimiento=?4";
		Query query_cabezera = this.em.createNativeQuery(str_cabezera);
		query_cabezera.setParameter(1, data.getId_dependencia());
		query_cabezera.setParameter(2, data.getIdUnidadOrganizacional());
		query_cabezera.setParameter(3, data.getNro_documento());
		query_cabezera.setParameter(4, data.getTipo_movimiento());
		result_cabezera = (Integer) query_cabezera.getSingleResult();
		return result_cabezera;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivoInventario> getReport(short id_dependencia, String doc_referencia, Date fecha_ref) {
		String str_detalle = "EXEC Inve_ReporteInvFisico_Q " + "@id_dependencia=?1," + "@nro_doc_ref=?2," + "@fecha_doc_ref=?3";
		Query query_r = this.em.createNativeQuery(str_detalle, "inv-activo");
		query_r.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query_r.setParameter(1, id_dependencia);
		query_r.setParameter(2, doc_referencia);
		query_r.setParameter(3, fecha_ref);
		List<ActivoInventario> result = (List<ActivoInventario>) query_r.getResultList();
		return result;
	}
	

	@SuppressWarnings("unchecked")
	public List<InventarioGrupo> getReport(short id_dependencia, String fecha) {
		String str_detalle = "EXEC Inv_InvGrupo_Q " + "@Id_Dependencia=?1,"
				+ "@Fecha=?2";
		Query query_r = this.em.createNativeQuery(str_detalle, "inventario-grupo");
		query_r.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query_r.setParameter(1, id_dependencia);
		query_r.setParameter(2, fecha);
		List<InventarioGrupo> result = (List<InventarioGrupo>) query_r.getResultList();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<InventarioGrupo> getReport(String fecha) {
		String str_detalle = "EXEC Inv_InvGrupoG_Q @Fecha=?1";
		Query query_r = this.em.createNativeQuery(str_detalle, "inventario-grupo");
		query_r.setParameter(1, fecha);
		query_r.setHint(QueryHints.REFRESH, HintValues.TRUE);
		List<InventarioGrupo> result = (List<InventarioGrupo>) query_r.getResultList();
		return result;
	}
	
}
