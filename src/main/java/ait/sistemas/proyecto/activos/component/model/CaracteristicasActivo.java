package ait.sistemas.proyecto.activos.component.model;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;

@SqlResultSetMapping(name = "caracteristicas", entities = { @EntityResult(entityClass = CaracteristicasActivo.class, fields = {
	@FieldResult(name = "codigo", column = "ACT_Codigo_Activo"),
	@FieldResult(name = "depedencia", column = "ACT_Dependencia"),
	@FieldResult(name = "nit_proveedor", column = "ACT_NIT_Proveedor"),
	@FieldResult(name = "marca", column = "ACT_Marca"),
	@FieldResult(name = "numero_serie", column = "ACT_No_Serie"),
	@FieldResult(name = "numero_garantia", column = "ACT_No_Garantia"),
	@FieldResult(name = "numero_ruat", column = "ACT_No_RUAT"),
	@FieldResult(name = "numero_folio_real", column = "ACT_No_Folio_Real"),
	@FieldResult(name = "numero_poliza_seguro", column = "ACT_No_Poliza_Seguro"),
	@FieldResult(name = "numero_contrato_mantenimiento", column = "ACT_No_Contrato_Mantenimiento"),
	@FieldResult(name = "vencimiento_garantia", column = "ACT_Fecha_Vencimiento_Garantia"),
	@FieldResult(name = "vencimiento_seguro", column = "ACT_Fecha_Vencimiento_Seguro"),
	@FieldResult(name = "vencimiento_contrato_mantenumiento", column = "ACT_Fecha_Vencimiento_Mantenimiento"),
	@FieldResult(name = "ubicacion_imagen", column = "ACT_Ubicacion_Imagen"),
	@FieldResult(name = "numeroLicencia", column = "ACT_No_Licencia"),
	@FieldResult(name = "vencimientoLicencia", column = "ACT_Vencimiento_Licencia")
	})})
@Entity
public class CaracteristicasActivo {
	
	@Id
	private String codigo;
	private short dependencia;
	private String nit_proveedor;
	private String marca;
	private String numero_serie;
	private String numero_garantia;
	
	private String numero_ruat;
	private String numero_folio_real;
	private String numero_poliza_seguro;
	
	private String numero_contrato_mantenimiento;
	
	private Date vencimiento_garantia;
	private Date vencimiento_seguro;
	private Date vencimiento_contrato_mantenumiento;
	private String ubicacion_imagen;
	
	private String numeroLicencia;
	private Date vencimientoLicencia;
	
	public CaracteristicasActivo() {
	}

	public CaracteristicasActivo(String codigo, String nit_proveedor, String marca, String numero_serie,
			String numero_garantia, String numero_ruat, String numero_folio_real,
			String numero_poliza_seguro, String numero_contrato_mantenimiento, Date vencimiento_garantia,
			Date vencimiento_seguro, Date vencimiento_contrato_mantenumiento) {
		this.codigo = codigo;
		this.nit_proveedor = nit_proveedor;
		this.marca = marca;
		this.numero_serie = numero_serie;
		this.numero_garantia = numero_garantia;
		this.numero_ruat = numero_ruat;
		this.numero_folio_real = numero_folio_real;
		this.numero_poliza_seguro = numero_poliza_seguro;
		this.numero_contrato_mantenimiento = numero_contrato_mantenimiento;
		this.vencimiento_garantia = vencimiento_garantia;
		this.vencimiento_seguro = vencimiento_seguro;
		this.vencimiento_contrato_mantenumiento = vencimiento_contrato_mantenumiento;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getNit_proveedor() {
		return nit_proveedor;
	}

	public void setNit_proveedor(String nit_proveedor) {
		this.nit_proveedor = nit_proveedor;
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getNumero_serie() {
		return numero_serie;
	}

	public void setNumero_serie(String numero_serie) {
		this.numero_serie = numero_serie;
	}

	public String getNumero_garantia() {
		return numero_garantia;
	}

	public void setNumero_garantia(String numero_garantia) {
		this.numero_garantia = numero_garantia;
	}

	public String getNumero_ruat() {
		return numero_ruat;
	}

	public void setNumero_ruat(String numero_ruat) {
		this.numero_ruat = numero_ruat;
	}

	public String getNumero_folio_real() {
		return numero_folio_real;
	}

	public void setNumero_folio_real(String numero_folio_real) {
		this.numero_folio_real = numero_folio_real;
	}

	public String getNumero_poliza_seguro() {
		return numero_poliza_seguro;
	}

	public void setNumero_poliza_seguro(String numero_poliza_seguro) {
		this.numero_poliza_seguro = numero_poliza_seguro;
	}

	public String getNumero_cgetontrato_mantenimiento() {
		return numero_contrato_mantenimiento;
	}

	public void setNumero_contrato_mantenimiento(String numero_contrato_mantenimiento) {
		this.numero_contrato_mantenimiento = numero_contrato_mantenimiento;
	}

	public Date getVencimiento_garantia() {
		return vencimiento_garantia;
	}

	public void setVencimiento_garantia(Date vencimiento_garantia) {
		this.vencimiento_garantia = vencimiento_garantia;
	}

	public Date getVencimiento_seguro() {
		return vencimiento_seguro;
	}

	public void setVencimiento_seguro(Date vencimiento_seguro) {
		this.vencimiento_seguro = vencimiento_seguro;
	}

	public Date getVengetcimiento_contrato_mantenumiento() {
		return vencimiento_contrato_mantenumiento;
	}

	public void setVencimiento_contrato_mantenumiento(Date vencimiento_contrato_mantenumiento) {
		this.vencimiento_contrato_mantenumiento = vencimiento_contrato_mantenumiento;
	}

	public String getUbicacion_imagen() {
		return ubicacion_imagen;
	}

	public void setUbicacion_imagen(String ubicacion_imagen) {
		this.ubicacion_imagen = ubicacion_imagen;
	}

	public short getDependencia() {
		return dependencia;
	}

	public void setDependencia(short dependencia) {
		this.dependencia = dependencia;
	}

	public String getNumero_contrato_mantenimiento() {
		return numero_contrato_mantenimiento;
	}

	public Date getVencimiento_contrato_mantenumiento() {
		return vencimiento_contrato_mantenumiento;
	}

	public String getNumeroLicencia() {
		return numeroLicencia;
	}

	public void setNumeroLicencia(String numeroLicencia) {
		this.numeroLicencia = numeroLicencia;
	}

	public Date getVencimientoLicencia() {
		return vencimientoLicencia;
	}

	public void setVencimientoLicencia(Date vencimientoLicencia) {
		this.vencimientoLicencia = vencimientoLicencia;
	}
	
	
	
}
