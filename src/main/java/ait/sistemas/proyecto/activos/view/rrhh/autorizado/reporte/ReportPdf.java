package ait.sistemas.proyecto.activos.view.rrhh.autorizado.reporte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import ait.sistemas.proyecto.activos.data.service.Impl.ActivoImpl;
import ait.sistemas.proyecto.common.component.PathValues;
import ait.sistemas.proyecto.common.report.Column;
import ait.sistemas.proyecto.common.report.PageSize;
import ait.sistemas.proyecto.common.report.Table;
import ait.sistemas.proyecto.common.report.TableBuilder;

public class ReportPdf {
	// Page configuration
	
	private static final PDRectangle PAGE_SIZE = PageSize.OFICIO;
	private static final float MARGIN = 30;
	private static final boolean IS_LANDSCAPE = true;
	
	// Font for textFont
	private static final PDFont TEXT_FONT = PDType1Font.HELVETICA;
	private static final float FONT_SIZE = 8;
	
	/**
	 * Font for footer report
	 */
	private static final PDFont FOOTER_FONT = PDType1Font.HELVETICA;
	private static final float FOOTER_FONT_SIZE = 7;
	
	/**
	 * Font for footer report
	 */
	private static final PDFont HEADER_FONT = PDType1Font.HELVETICA;
	private static final float HEADER_FONT_SIZE = 8;
	
	/**
	 * Font for footer report
	 */
	private static final PDFont TITLE_FONT = PDType1Font.HELVETICA_BOLD;
	private static final float TITLE_FONT_SIZE = 14;
	
	/**
	 * Font for footer report
	 */
	private static final PDFont SUBTITLE_FONT = PDType1Font.HELVETICA;
	private static final float SUBTITLE_FONT_SIZE = 12;
	
	private static final float ROW_HEIGHT = 15;
	private static final float CELL_MARGIN = 2;
	
	private static final int HEADER_SIZE = 3;
	
	public String SAVE_PATH = PathValues.PATH_REPORTS + "personal_autorizaciones" + String.valueOf(new java.util.Date().getTime())
			+ ".pdf";
	
	final ActivoImpl activoimpl = new ActivoImpl();
	
	String[][] contables;
	String[][] activos;
	
	public boolean getPdf(String[][] data, String strDependencia, String fecha) throws IOException {
		
		this.SAVE_PATH = PathValues.PATH_REPORTS + "Reporte-Inventario-" + String.valueOf(new java.util.Date().getTime())
				+ ".pdf";
		return new ReportGenerator().generatePDF(createContent(data, strDependencia, fecha), SAVE_PATH);
		
	}
	
	private Table createContent(String[][] content, String strDependencia, String fecha) {
		
		List<Column> columns = new ArrayList<Column>();
		columns.add(new Column("Tipo Movimiento",  180));
		columns.add(new Column("Unidad Organizacional", 250));
		columns.add(new Column("Dependencia Transferencia", 100));
		columns.add(new Column("Orden", 75));
		columns.add(new Column("Servidor Publico", 180));
		columns.add(new Column("Nivel Autorizacion", 100));
		
		float tableHeight = IS_LANDSCAPE ? PAGE_SIZE.getWidth() - (2 * MARGIN) : PAGE_SIZE.getHeight() - (2 * MARGIN);
		Table Inventario = new TableBuilder().setCellMargin(CELL_MARGIN).setContent(content)
				.setColumns(columns).setContent(content).setHeight(tableHeight).setNumberOfRows(content.length)
				.setRowHeight(ROW_HEIGHT).setMargin(MARGIN).setPageSize(PAGE_SIZE).setLandscape(IS_LANDSCAPE)
				.setTextFont(TEXT_FONT).setFontSize(FONT_SIZE).setHeaderFont(HEADER_FONT).setFontSizeHeader(HEADER_FONT_SIZE)
				.setFooterFont(FOOTER_FONT).setFontSizeFooter(FOOTER_FONT_SIZE).setTitleFont(TITLE_FONT)
				.setFontSizeTitle(TITLE_FONT_SIZE).setSubTitleFont(SUBTITLE_FONT).setFontSizeSubTitle(SUBTITLE_FONT_SIZE)
				.setHeaderSize(HEADER_SIZE).setUnidad("XXXXXX").setDependencia("XXXXX").setUsuario("XXXXXX")
				.setTitle("PERSONAL CON AUTORIZACIONES").setSubTitle("").build();
		return Inventario;
		
	}
	
}
