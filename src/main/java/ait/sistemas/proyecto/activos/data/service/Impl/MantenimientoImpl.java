package ait.sistemas.proyecto.activos.data.service.Impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import com.google.web.bindery.requestfactory.server.Logging;
import com.vaadin.ui.Notification;

import ait.sistemas.proyecto.activos.component.model.CmovimientoDocumento;
import ait.sistemas.proyecto.activos.component.model.Detalle;
import ait.sistemas.proyecto.activos.component.model.Mantenimiento;
import ait.sistemas.proyecto.activos.component.model.Movimiento;
import ait.sistemas.proyecto.activos.data.model.C_Movimiento;

public class MantenimientoImpl {
	
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public MantenimientoImpl() {
		this.emf = Persistence.createEntityManagerFactory("AIT-Activos");
		this.em = emf.createEntityManager();
		// this.em.lock(C_Movimiento.class, LockModeType.NONE);
	}
	
	public int addMantenimiento(Mantenimiento data) {
		this.em.clear();
		String str_mantenimiento = "EXEC Mvac_Mantenimiento_I " + "@Dependencia_Id=?1," + "@Unidad_Organizacional_Id=?2,"
				+ "@Numero_Documento=?3," + "@Fecha_Registro=?4," + "@Fecha_Movimiento=?5," + "@CI_Usuario=?6";
		Query query_mantenimiento = this.em.createNativeQuery(str_mantenimiento);
		query_mantenimiento.setParameter(1, data.getId_dependencia());
		query_mantenimiento.setParameter(2, data.getId_unidad_organizacional_origen());
		query_mantenimiento.setParameter(3, data.getNro_documento());
		query_mantenimiento.setParameter(4, data.getFecha_registro());
		query_mantenimiento.setParameter(5, data.getFecha_movimiento());
		query_mantenimiento.setParameter(6, data.getId_usuario());
		int result_cabezera = (Integer) query_mantenimiento.getSingleResult();
		this.em.clear();
		int result_detalle = 0;
		if (result_cabezera > 0) {
			for (Detalle detalle : data.getDetalles()) {
				String str_detalle = "EXEC Mvac_DMovimiento_I " + "@Dependencia_Id=?1," + "@Unidad_Organizacional_Id=?2,"
						+ "@Numero_Documento=?3," + "@Fecha_Registro=?4," + "@Tipo_Movimiento=?5," + "@Activo_Id=?6,"
						+ "@Observaciones=?7";
				Query query_detalle = this.em.createNativeQuery(str_detalle);
				query_detalle.setParameter(1, detalle.getId_dependencia());
				query_detalle.setParameter(2, detalle.getId_unidad_organizacional_origen());
				query_detalle.setParameter(3, detalle.getNro_documento());
				query_detalle.setParameter(4, detalle.getFecha_registro());
				query_detalle.setParameter(5, detalle.getTipo_movimiento());
				query_detalle.setParameter(6, detalle.getId_activo());
				
				query_detalle.setParameter(7, detalle.getObservacion());
				result_detalle += (Integer) query_detalle.getSingleResult();
				this.em.clear();
			}
			if (result_detalle == data.getDetalles().size()) {
				return 1;
			} else {
				// dropmovimiento(data);
				return 0;
			}
			
		} else {
			return 0;
		}
	}
	
	public boolean addMantenimiento(Movimiento mantenimiento) throws SQLException {
		String str_conn = "jdbc:sqlserver://192.168.97.99;instanceName=ACTIVOS;databaseName=Activos;user=sa;password=sa";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			Connection conn = DriverManager.getConnection(str_conn);
			System.out.println("Coneccion Realizada con Exito");
			Statement sta = conn.createStatement();
			String Sql = String.format("exec [dbo].[Mvac_Mantenimiento_I] " + "@Dependencia_Id=%d,"
					+ "@Unidad_Organizacional_Id=%d," + "@Numero_Documento=%d," + "@Fecha_Registro='%s',"
					+ "@Fecha_Movimiento='%s'," + "@CI_Usuario='%s' ", mantenimiento.getId_dependencia(), mantenimiento
					.getId_unidad_organizacional_origen(), mantenimiento.getNro_documento(), new SimpleDateFormat("yyyy-dd-MM")
					.format(mantenimiento.getFecha_registro()), new SimpleDateFormat("yyyy-dd-MM").format(mantenimiento
					.getFecha_movimiento()), mantenimiento.getUsuario());
			System.out.println(Sql);
			ResultSet rs = sta.executeQuery(Sql);
			rs.next();
			int resultado_cabecera = rs.getInt("res");
			conn.close();
			int result_detalle = 0;
			if (resultado_cabecera > 0) {
				for (Detalle detalle : mantenimiento.getDetalles()) {
					Connection conn_detalle = DriverManager.getConnection(str_conn);
					Statement sta_detalle = conn_detalle.createStatement();
					String str_detalle = String.format("EXEC Mvac_DMovimiento_I " + "@Dependencia_Id=%d,"
							+ "@Unidad_Organizacional_Id=%d," + "@Numero_Documento=%d," + "@Fecha_Registro='%s',"
							+ "@Tipo_Movimiento=%d," + "@Activo_Id=%d," + "@Observaciones='%s'",
							detalle.getId_dependencia(),
							detalle.getId_unidad_organizacional_origen(), 
							detalle.getNro_documento(), 
							new SimpleDateFormat("yyyy-dd-MM").format(detalle.getFecha_registro()), 
							detalle.getTipo_movimiento(), 
							detalle.getId_activo(), detalle.getObservacion());
					ResultSet rs_detalle = sta_detalle.executeQuery(str_detalle);
					rs_detalle.next();
					System.out.println(detalle.getNombre_activo());
					result_detalle += rs_detalle.getInt("res");
					conn_detalle.close();
				}
				if (result_detalle == mantenimiento.getDetalles().size()) {
					return true;
				} else {
					dropmovimiento(mantenimiento);
					return false;
				}
				
			} else {
				return false;
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			Notification.show("Clase No entontrada");
			return false;
		}
	}
	
	public int dropmovimiento(Movimiento data) {
		int result_cabezera;
		for (Detalle detalle : data.getDetalles()) {
			String str_detalle = "EXEC Mvac_DMovimiento_D " + "@Dependencia_Id=?1," + "@Unidad_Organizacional_Id=?2,"
					+ "@Numero_Documento=?3," + "@Tipo_Movimiento=?4," + "@Activo_Id=?4";
			Query query_detalle = this.em.createNativeQuery(str_detalle);
			query_detalle.setParameter(1, detalle.getId_dependencia());
			query_detalle.setParameter(2, detalle.getId_unidad_organizacional_origen());
			query_detalle.setParameter(3, detalle.getNro_documento());
			query_detalle.setParameter(4, detalle.getTipo_movimiento());
			query_detalle.setParameter(5, detalle.getId_activo());
			result_cabezera = (Integer) query_detalle.getSingleResult();
		}
		
		String str_cabezera = "EXEC Mvac_CMovimiento_D " + "@Dependencia_Id=?1," + "@Unidad_Organizacional_Id=?2,"
				+ "@Numero_Documento=?3," + "@Tipo_Movimiento=?4";
		Query query_cabezera = this.em.createNativeQuery(str_cabezera);
		query_cabezera.setParameter(1, data.getId_dependencia());
		query_cabezera.setParameter(2, data.getId_unidad_organizacional_origen());
		query_cabezera.setParameter(3, data.getNro_documento());
		query_cabezera.setParameter(4, data.getTipo_movimiento());
		result_cabezera = (Integer) query_cabezera.getSingleResult();
		return result_cabezera;
	}
	
}