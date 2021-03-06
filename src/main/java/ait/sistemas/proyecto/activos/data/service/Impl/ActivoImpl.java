package ait.sistemas.proyecto.activos.data.service.Impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

import ait.sistemas.proyecto.activos.component.model.ActivoGrid;
import ait.sistemas.proyecto.activos.component.model.CaracteristicasActivo;
import ait.sistemas.proyecto.activos.component.model.Componente;
import ait.sistemas.proyecto.activos.component.model.DatosGeneralesActivos;
import ait.sistemas.proyecto.activos.component.model.Documento;
import ait.sistemas.proyecto.activos.component.model.Movimiento;
import ait.sistemas.proyecto.activos.component.model.MovimientoReporte;
import ait.sistemas.proyecto.activos.component.session.ActivoSession;
import ait.sistemas.proyecto.activos.data.model.ActivosModel;
import ait.sistemas.proyecto.activos.data.model.ComponentesModel;
import ait.sistemas.proyecto.activos.data.model.DocumentosRespaldoModel;

public class ActivoImpl {
	private EntityManagerFactory emf;
	private EntityManager em;
	
	public ActivoImpl() {
		this.emf = Persistence.createEntityManagerFactory("AIT-Activos");
		this.em = emf.createEntityManager();
	}
	
	public long getIdAcivo() {
		long result = 0;
		Query query = this.em.createNativeQuery("EXEC MVAC_INGRESO_GET_ID");
		result = (Long) query.getSingleResult();
		return (result + 1);
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> activos_by_auxiliar(String id_auxiliar, String grupoContable) {
		Query query = em.createNativeQuery("Mvac_ActivosPorAuxiliar " + "@ACT_Auxiliar_Contable=?1, " + "@ACT_Grupo_Contable=?2",
				ActivosModel.class);
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, id_auxiliar);
		query.setParameter(2, grupoContable);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> ActivosbyAuxiliarFecha(String id_auxiliar, String grupoContable, Date fecha) {
		Query query = em.createNativeQuery("Mvac_ActivosbyAuxiliarFecha_Q " + "@ACT_Auxiliar_Contable=?1, "
				+ "@ACT_Grupo_Contable=?2, " + "@Fecha=?3 ", ActivosModel.class);
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, id_auxiliar);
		query.setParameter(2, grupoContable);
		query.setParameter(3, fecha);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> activosgrid_by_auxiliar(String id_auxiliar) {
		Query query = em.createNativeQuery("Mvac_ActivosbyAuxiliar " + "@ACT_Auxiliar_Contable=?1 ", ActivosModel.class);
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, id_auxiliar);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> activos_by_dependencia(short id_dependencia, Date fecha) {
		Query query = em.createNativeQuery("Mvac_ActivobyDependencia " + "@ACT_Dependencia=?1, " + "@fecha=?2 ",
				ActivosModel.class);
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, id_dependencia);
		query.setParameter(2, fecha);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	/**
	 * Retorna Lista de Activos Por Grupo Contable y Dependencia
	 * 
	 * @param id_dependencia
	 * @param grupo
	 * @param fecha
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ActivosModel> activosByGrupoDependencia(short id_dependencia, String grupo, Date fecha) {
		Query query = em.createNativeQuery("Mvac_ActivoByGrupoDependencia " + "@Dependencia=?1," + "@Grupo_Contable=?2, "
				+ "@fecha=?3 ", ActivosModel.class);
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, id_dependencia);
		query.setParameter(2, grupo);
		query.setParameter(3, fecha);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	/**
	 * Activos Sin Asignar Por Dependencia
	 * 
	 * @param id_dependencia
	 * @param fecha
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ActivosModel> activosingasig_by_dependencia(short id_dependencia, Date fecha) {
		Query query = em.createNativeQuery("Mvac_ActivobyDependenciaSinAsig " + "@ACT_Dependencia=?1, " + "@fecha=?2 ",
				ActivosModel.class);
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, id_dependencia);
		query.setParameter(2, fecha);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> ActivosNominal_by_dependencia(short id_dependencia, Date fecha) {
		Query query = em.createNativeQuery("Mvac_ActivoNominalbyDependencia " + "@ACT_Dependencia=?1, " + "@fecha=?2 ",
				ActivosModel.class);
		query.setParameter(1, id_dependencia);
		query.setParameter(2, fecha);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> activosbaja_by_dependencia(short id_dependencia, Date fecha) {
		Query query = em.createNativeQuery("Mvac_ActivoBajabyDependencia_Q " + "@ACT_Dependencia=?1, " + "@fecha=?2 ",
				ActivosModel.class);
		query.setParameter(1, id_dependencia);
		query.setParameter(2, fecha);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> activo_aux_grup(String auxiliar, String grupo, short id_dependencia) {
		Query query = em.createNativeQuery("Mvac_ActivobyAuxiliar-Grupo " + "@ACT_Dependencia=?1, "
				+ "@ACT_Auxiliar_Contable=?2," + "@ACT_Grupo_Contable=?3", ActivosModel.class);
		query.setParameter(1, id_dependencia);
		query.setParameter(2, auxiliar);
		query.setParameter(3, grupo);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> getActivosbyFechaCompra(Date fecha) {
		Query query = em.createNativeQuery("Mvac_ActivobyFechaCompra_Q " + "@fecha=?1 ", "mapeo-activo");
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, fecha);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	/**
	 * Lista de Activos sin Asignar
	 * 
	 * @param fecha
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ActivosModel> getActivosbyFechaCompraSinasig(Date fecha) {
		Query query = em.createNativeQuery("Mvac_ActivobyFechaCompraSinAsig_Q " + "@fecha=?1 ", "mapeo-activo");
		query.setParameter(1, fecha);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> getActivosNominalbyFechaCompra(Date fecha) {
		Query query = em.createNativeQuery("Mvac_ActivoNominalbyFechaCompra_Q " + "@fecha=?1 ", "mapeo-activo");
		query.setParameter(1, fecha);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> getActivosConsolidado(Date fecha) {
		Query query = em.createNativeQuery("Mvac_ActivoConsolidado_Q " + "@fecha=?1 ", "mapeo-activo");
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, fecha);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> getActivosBaja(Date fecha) {
		Query query = em.createNativeQuery("Mvac_ActivoBaja_Q " + "@fecha=?1 ", "mapeo-activo");
		query.setParameter(1, fecha);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	public int getResol(String resolucion) {
		Query query = em.createNativeQuery("Mvac_Resolucion_Q " + "@no_resolucion=?1 ");
		query.setParameter(1, resolucion);
		
		int resultlist = (int) query.getSingleResult();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> getActivosbyResol(Date fecha, String resolucion) {
		Query query = em.createNativeQuery("Mvac_ActivobyResolucion_Q " + "@fecha=?1, " + "@resolucion=?2 ", "mapeo-activo");
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, fecha);
		query.setParameter(2, resolucion);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> getActivosbyResolDependnecia(Date fecha, String resolucion, short id_dependencia) {
		Query query = em.createNativeQuery("Mvac_ActivobyResolucionDependencia_Q " + "@fecha=?1, " + "@resolucion=?2,"
				+ "@id_dependencia=?3 ", "mapeo-activo");
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, fecha);
		query.setParameter(2, resolucion);
		query.setParameter(3, id_dependencia);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> getactivosbydependencia(short id_dependencia) {
		Query query = em.createNativeQuery("Mvac_ActivobyDependencia_Q @Id_Dependencia=?1 ", "mapeo-activo");
		query.setParameter(1, id_dependencia);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> getactivos(short id_unidad_organizacional) {
		Query query = em.createNativeQuery("Mvac_ActivobyUnidad_Q @Id_Unidad=?1 ", "mapeo-activo");
		query.setParameter(1, id_unidad_organizacional);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> getactivos(String ci_usuario) {
		Query query = em.createNativeQuery("Mvac_ActivobyUsuario_Q @CI_Usuario=?1", "mapeo-activo");
		query.setParameter(1, ci_usuario);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivosModel> getall(long id_activo) {
		Query query = em.createNativeQuery("Mvac_Activo_Cod " + "@ACT_Codigo_Activo=?1 ", "mapeo-activo");
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, id_activo);
		List<ActivosModel> resultlist = query.getResultList();
		return resultlist;
	}
		@SuppressWarnings("unchecked")
		public List<ActivosModel> getActivosbyCodigoFecha(long id_activo, Date fecha) {
			Query query = em.createNativeQuery("Mvac_ActivobyCodigoFecha_Q " 
					+ "@ACT_Codigo_Activo=?1, "
					+ "@Fecha=?2", "mapeo-activo");
			query.setHint(QueryHints.REFRESH, HintValues.TRUE);
			query.setParameter(1, id_activo);
			query.setParameter(2, fecha);
			List<ActivosModel> resultlist = query.getResultList();
			return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<ComponentesModel> getcomponente(long id_activo, short id_dependencia) {
		Query query = em.createNativeQuery("Mvac_ActivobyComponente " + "@COM_Codigo_Activo=?1, " + "@COM_Dependencia=?2",
				"mapeo-componente");
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, id_activo);
		query.setParameter(2, id_dependencia);
		List<ComponentesModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	@SuppressWarnings("unchecked")
	public List<DocumentosRespaldoModel> getdocumento(long id_activo, short id_dependencia) {
		Query query = em.createNativeQuery("Mvac_ActivobyDocumentoRespaldo " + "@DOR_Codigo_Activo=?1," + "@DOR_Dependencia=?2 ",
				"mapeo-documento");
		query.setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, id_activo);
		query.setParameter(2, id_dependencia);
		List<DocumentosRespaldoModel> resultlist = query.getResultList();
		return resultlist;
	}
	
	public boolean add(DatosGeneralesActivos datos_generales) {
		
		String str_datos_generales = "EXEC Mvac_Activos_I" + " @Id_Activos=?1, " + "@Id_Dependencia=?2, " + "@Nombre_Activo=?3, "
				+ "@Tipo_Activo=?4, " + "@Fecha_Compra=?5, " + "@Valor=?6, " + "@Tipo_Cambio_UFV=?7, " + "@Grupo_Contable=?8, "
				+ "@Auxiliar_Contable=?9, " + "@Vida_Util=?10, " + "@Fuente_Financiamiento=?11, "
				+ "@Organismo_Financiador=?12, " + "@Ubicacion_Fisica=?13, " + "@Fecha_ComoDato=?14, "
				+ "@Fecha_Incorporacion=?15," + "@Tipo_Cambio_Dolar=?16";
		
		Query query = this.em.createNativeQuery(str_datos_generales).setParameter(1, datos_generales.getId_activo())
				.setParameter(2, datos_generales.getId_dependencia()).setParameter(3, datos_generales.getNombre_activo())
				.setParameter(4, datos_generales.getTipo_activo()).setParameter(5, datos_generales.getFecha_compra())
				.setParameter(6, datos_generales.getValor()).setParameter(7, datos_generales.getTipo_cambio_ufv())
				.setParameter(8, datos_generales.getId_grupo_contable())
				.setParameter(9, datos_generales.getId_auxiliar_contalbe()).setParameter(10, datos_generales.getVida_util())
				.setParameter(11, datos_generales.getId_fuente_financiamiento())
				.setParameter(12, datos_generales.getId_organimismo_financiador())
				.setParameter(13, datos_generales.getId_ubicacion_fisica())
				.setParameter(14, datos_generales.getFecha_como_dato())
				.setParameter(15, datos_generales.getFecha_incorporacion())
				.setParameter(16, datos_generales.getTipo_cambio_dolar());
		
		int result = (Integer) query.getSingleResult();
		
		return (result > 0) ? true : false;
	}
	
	public boolean update(DatosGeneralesActivos datos_generales) {
		
		String str_datos_generales = "EXEC Mvac_Activos_U" + " @Id_Activos=?1, " + "@Id_Dependencia=?2, " + "@Nombre_Activo=?3, "
				+ "@Tipo_Activo=?4, " + "@Fecha_Compra=?5, " + "@Valor=?6, " + "@Tipo_Cambio_UFV=?7, " + "@Grupo_Contable=?8, "
				+ "@Auxiliar_Contable=?9, " + "@Vida_Util=?10, " + "@Fuente_Financiamiento=?11, "
				+ "@Organismo_Financiador=?12, " + "@Ubicacion_Fisica=?13, " + "@Fecha_ComoDato=?14, "
				+ "@Fecha_Incorporacion=?15," + "@Tipo_Cambio_Dolar=?16";
		
		Query query = this.em.createNativeQuery(str_datos_generales).setParameter(1, datos_generales.getId_activo())
				.setParameter(2, datos_generales.getId_dependencia()).setParameter(3, datos_generales.getNombre_activo())
				.setParameter(4, datos_generales.getTipo_activo()).setParameter(5, datos_generales.getFecha_compra())
				.setParameter(6, datos_generales.getValor().setScale(3, RoundingMode.UP).toString())
				.setParameter(7, String.valueOf(datos_generales.getTipo_cambio_ufv()))
				.setParameter(8, datos_generales.getId_grupo_contable())
				.setParameter(9, datos_generales.getId_auxiliar_contalbe()).setParameter(10, datos_generales.getVida_util())
				.setParameter(11, datos_generales.getId_fuente_financiamiento())
				.setParameter(12, datos_generales.getId_organimismo_financiador())
				.setParameter(13, datos_generales.getId_ubicacion_fisica())
				.setParameter(14, datos_generales.getFecha_como_dato())
				.setParameter(15, datos_generales.getFecha_incorporacion())
				.setParameter(16, datos_generales.getTipo_cambio_dolar());
		System.out.println(datos_generales.getValor().doubleValue());
		System.out.println(String.valueOf(datos_generales.getTipo_cambio_ufv()));
		
		int result = (Integer) query.getSingleResult();
		
		return (result > 0) ? true : false;
	}
	
	public boolean addCaracteristica(CaracteristicasActivo caracteristicas) {
		String str_add_caracteristicas = "EXEC MVAC_INGRESO_CARACTERISTICA_A" + " @Id_Activos=?1, " + "@Id_Dependencia=?2, "
				+ "@Nit_Proveedor=?3, " + "@Marca=?4, " + "@Numero_Serie=?5, " + "@Numero_Garantia=?6, " + "@Numero_Ruat=?7, "
				+ "@Numero_Folio_Real=?8, " + "@Numero_Poliza_Seguro=?9, " + "@Numero_Contrato_Mantenimiento=?10, "
				+ "@Vencimiento_Garantia=?11, " + "@Vencimiento_Seguro=?12, " + "@Vencimiento_Contrato_Mantenumiento=?13, "
				+ "@Ubicacion_Imagen=?14," + "@Numero_Licencia=?15," + "@Vencimiento_Licencia=?16";
		
		Query query = this.em.createNativeQuery(str_add_caracteristicas).setParameter(1, caracteristicas.getCodigo())
				.setParameter(2, caracteristicas.getDependencia()).setParameter(3, caracteristicas.getNit_proveedor())
				.setParameter(4, caracteristicas.getMarca()).setParameter(5, caracteristicas.getNumero_serie())
				.setParameter(6, caracteristicas.getNumero_garantia()).setParameter(7, caracteristicas.getNumero_ruat())
				.setParameter(8, caracteristicas.getNumero_folio_real())
				.setParameter(9, caracteristicas.getNumero_poliza_seguro())
				.setParameter(10, caracteristicas.getNumero_contrato_mantenimiento())
				.setParameter(11, caracteristicas.getVencimiento_garantia())
				.setParameter(12, caracteristicas.getVencimiento_seguro())
				.setParameter(13, caracteristicas.getVencimiento_contrato_mantenumiento())
				.setParameter(14, caracteristicas.getUbicacion_imagen()).setParameter(15, caracteristicas.getNumeroLicencia())
				.setParameter(16, caracteristicas.getVencimientoLicencia());
		
		int result = (Integer) query.getSingleResult();
		
		return (result > 0) ? true : false;
	}
	
	public boolean addComponentes(List<Componente> componentes, ActivoSession sessionactivo) {
		
		Query query = this.em.createNativeQuery("Mvac_QuitarComponentes_Q @Id_Activo=?1").setParameter(1,
				sessionactivo.getCodigo());
		query.getSingleResult();
		
		final ComponenteImpl componenteimpl = new ComponenteImpl();
		for (Componente componente : componentes) {
			if (!componenteimpl.add(sessionactivo.getCodigo(), sessionactivo.getDependencia(), componente.getNombre(),
					componente.getCaracteristica())) {
				return false;
			}
		}
		return true;
	}
	
	public boolean addDocumentos(List<Documento> documentos, ActivoSession sessionactivo) {
		Query query = this.em.createNativeQuery("Mvac_QuitarDocumentos_Q @Id_Activo=?1").setParameter(1,
				sessionactivo.getCodigo());
		query.getSingleResult();
		final DocumentoRespaldoImpl documentoimpl = new DocumentoRespaldoImpl();
		for (Documento componente : documentos) {
			if (!documentoimpl.add(sessionactivo.getCodigo(), sessionactivo.getDependencia(), componente.getNombre(),
					componente.getDireccion())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Lista los Activo que no Esten Datos de Baja o esten Pendientes dentro de
	 * un Movimiento
	 * 
	 * @param grupo_contable
	 * @param auxiliar_contable
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<ActivoGrid> getDisponibles(String grupo_contable, String auxiliar_contable) {
		String str_query_act_disponibles = "EXEC Mvact_Select_Disponibles @Grupo_Contable_Id=?1,@Auxiliar_Contable_Id=?2";
		Query query = this.em.createNativeQuery(str_query_act_disponibles, "activo-simple").setParameter(1, grupo_contable)
				.setParameter(2, auxiliar_contable);
		List<ActivoGrid> result = (List<ActivoGrid>) query.getResultList();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivoGrid> getActivosbyACyGC(String grupo_contable, String auxiliar_contable) {
		String str_query_act_disponibles = "EXEC Mvac_ActivosbyACyGC @ACT_Auxiliar_Contable=?1,@ACT_Grupo_Contable=?2 ";
		Query query = this.em.createNativeQuery(str_query_act_disponibles, "activo-simple").setParameter(1, auxiliar_contable)
				.setParameter(2, grupo_contable);
		List<ActivoGrid> result = (List<ActivoGrid>) query.getResultList();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivoGrid> getActivosDisponibles(short dependencia, String grupo_contable, String auxiliar_contable) {
		String str_query_act_disponibles = "EXEC Mvac_Activos_Disponibles @Grupo_Contable_Id=?1,@Auxiliar_Contable_Id=?2,@Dependencia_Id=?3";
		Query query = this.em.createNativeQuery(str_query_act_disponibles, "activo-simple").setParameter(1, grupo_contable)
				.setParameter(2, auxiliar_contable).setParameter(3, dependencia);
		List<ActivoGrid> result = (List<ActivoGrid>) query.getResultList();
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public List<ActivoGrid> getAsignados(String ci_usuario) {
		Query query = em.createNativeQuery("exec Mvac_ActivoAsignadobyUsuario @CI_Usuario=?1", "activo-simple").setHint(
				QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, ci_usuario);
		List<ActivoGrid> resultlist = query.getResultList();
		return resultlist;
	}
	@SuppressWarnings("unchecked")
	public List<ActivoGrid> getActivoGrid(long cod_Activo) {
		Query query = em.createNativeQuery("exec Mvac_ActivobyCodigo_Q @Codigo_Activo=?1", "activo-simple").setHint(
				QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, cod_Activo);
		List<ActivoGrid> resultlist = query.getResultList();
		return resultlist;
	}
	@SuppressWarnings("unchecked")
	public List<MovimientoReporte> ActivosbyUsuario(String ci_usuario, Date fecha) {
		Query query = em.createNativeQuery("exec Mvac_ActivosbyFuncionario_Q @CI_Usuario=?1, " + "@fecha=?2 ",
				"reporte-movimiento").setHint(QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, ci_usuario);
		query.setParameter(2, fecha);
		List<MovimientoReporte> resultlist = query.getResultList();
		return resultlist;
	}
	
	public ActivoGrid getone(long id_activo, long id_dependencia) {
		Query query = em.createNativeQuery("exec Mvac_ActivosGetOne_Q @Id_Activo=?1 ", "activo-simple").setHint(
				QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, id_activo);
		ActivoGrid resultlist;
		try {
			resultlist = (ActivoGrid) query.getSingleResult();
		} catch (Exception ex) {
			return null;
		}
		return resultlist;
	}
	public ActivoGrid getActivoOne(long id_activo) {
		Query query = em.createNativeQuery("exec Mvac_ActivobyCodigo_Q @Codigo_Activo=?1", "activo-simple").setHint(
				QueryHints.REFRESH, HintValues.TRUE);
		query.setParameter(1, id_activo);
		ActivoGrid resultlist;
		try {
			resultlist = (ActivoGrid) query.getSingleResult();
		} catch (Exception ex) {
			return null;
		}
		return resultlist;
	}
	
	/**
	 * actualiza los campos revalorizados en la tabla Activos
	 */
	public int RevalorizaActivos(Movimiento data) {
		Query query = em.createNativeQuery("EXEC Reva_RevalorizacionActivos_I " + "@No_resolucion=?1, " + "@Fecha=?2 ");
		query.setParameter(1, data.getNro_documento_referencia());
		query.setParameter(2, data.getFecha_nro_referencia());
		try {
			int result = (Integer) query.getSingleResult();
			return result;
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * Realizara copias del activo n veces
	 */
	public int addCopiaActivo(BigDecimal codigo, int no_copias) {
		Query query = em.createNativeQuery("EXEC Mvac_ActivoCopia_I " + "@Codigo_Activo=?1, " + "@No_Copias=?2 ");
		query.setParameter(1, codigo);
		query.setParameter(2, no_copias);
		try {
			int result = (Integer) query.getSingleResult();
			return result;
		} catch (Exception e) {
			return 0;
		}
	}
	
	/**
	 * Retorna los Datos Generales de un Activo Dependiendo el Codigo de Activo
	 * 
	 * @param idActivo
	 * @return
	 */
	public DatosGeneralesActivos getDatosGenerales(long idActivo) {
		
		Query query = this.em.createNativeQuery("EXEC Mvac_GetDatosGenerales @Id_Activo=?1", "datos-generales")
				.setHint(QueryHints.REFRESH, HintValues.TRUE).setParameter(1, idActivo);
		DatosGeneralesActivos result = (DatosGeneralesActivos) query.getSingleResult();
		return result;
	}
	
	/**
	 * Retorna las caracteristicas de un Activo dependiendo del Codigo de Activo
	 * 
	 * @param idActivo
	 * @return
	 */
	public CaracteristicasActivo getCaracteristicas(long idActivo) {
		Query query = this.em.createNativeQuery("EXEC Mvac_GetCaracteristicasActivos @Id_Activo=?1", "caracteristicas")
				.setHint(QueryHints.REFRESH, HintValues.TRUE).setParameter(1, idActivo);
		CaracteristicasActivo result = (CaracteristicasActivo) query.getSingleResult();
		return result;
	}
	
	public boolean esModificable(long idActivo) {
		Query query = this.em.createNativeQuery("EXEC Mvac_EsModificable @Id_Activo=?1").setParameter(1, idActivo);
		int result = (Integer) query.getSingleResult();
		return result > 0 ? true : false;
	}
}
