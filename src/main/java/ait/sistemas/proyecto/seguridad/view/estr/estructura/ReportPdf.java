package ait.sistemas.proyecto.seguridad.view.estr.estructura;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import ait.sistemas.proyecto.common.component.PathValues;
import ait.sistemas.proyecto.common.report.Column;
import ait.sistemas.proyecto.common.report.PDFTableGenerator;
import ait.sistemas.proyecto.common.report.Table;
import ait.sistemas.proyecto.common.report.TableBuilder;

public class ReportPdf {
	 // Page configuration
    private static final PDRectangle PAGE_SIZE = PDRectangle.LETTER;
    /**
     * Escala 30:1 1cm = 30
     */
    private static final float MARGIN = 50;
    /**
     * false Orientacion Vertical true Horizontal
     */
    private static final boolean IS_LANDSCAPE = true;

    /**
     * Fuente el Texto
     */
    private static final PDFont TEXT_FONT = PDType1Font.HELVETICA;
    private static final float FONT_SIZE = 10;
    
    /**
     * Font for footer report
     */
    private static final PDFont FOOTER_FONT = PDType1Font.HELVETICA;
    private static final float FOOTER_FONT_SIZE = 9;
    
    /**
     * Font for header report
     */
    private static final PDFont HEADER_FONT = PDType1Font.HELVETICA;
    private static final float HEADER_FONT_SIZE = 8;
    
    /**
     * Font for title report
     */
    private static final PDFont TITLE_FONT = PDType1Font.HELVETICA_BOLD;
    private static final float TITLE_FONT_SIZE = 16;
    
    /**
     * Font for subtitle report
     */
    private static final PDFont SUBTITLE_FONT = PDType1Font.HELVETICA;
    private static final float SUBTITLE_FONT_SIZE = 12;
    
    /**
     * Distancia en entre las lineas de texto
     */
    private static final float ROW_HEIGHT = 15;
    /**
     * Distancia entre las lineas
     */
    private static final float CELL_MARGIN = 4;
    /**
     * Tamaño del header expesado en alto de las filas de la tabla
     */
    private static final int HEADER_SIZE = 5;
    
    /**
     * Ruta donde se guardara el reporte
     */
    public static String SAVE_PATH = PathValues.PATH_REPORTS + String.valueOf(new Date().getTime())+".pdf";
    
    
    public boolean getPdf(String[][] data, String subsistema) throws IOException{
    	
        return new PDFTableGenerator().generatePDF(createContent(data, subsistema), SAVE_PATH);
        
    }

    private static Table createContent(String[][] data, String subsistema) {
        /**
         * La suma del ancho de las colmunas debe rondar 400
         */
    	List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("ID", 45));	
        columns.add(new Column("COD", 50));
        columns.add(new Column("NOMBRE DEL MENU", 260));
        columns.add(new Column("NVL", 30));
        columns.add(new Column("PROGRAMA", 280));
        columns.add(new Column("PADRE", 50));
 
        String[][] content = data;

        float tableHeight = IS_LANDSCAPE ? PAGE_SIZE.getWidth() - (2 * MARGIN) : PAGE_SIZE.getHeight() - (2 * MARGIN);

        Table table = new TableBuilder()
            .setCellMargin(CELL_MARGIN)
            .setColumns(columns)
            .setContent(content)
            .setHeight(tableHeight)
            .setNumberOfRows(content.length)
            .setRowHeight(ROW_HEIGHT)
            .setMargin(MARGIN)
            .setPageSize(PAGE_SIZE)
            .setLandscape(IS_LANDSCAPE)
            .setTextFont(TEXT_FONT)
            .setFontSize(FONT_SIZE)
            .setHeaderFont(HEADER_FONT)
            .setFontSizeHeader(HEADER_FONT_SIZE)
            .setFooterFont(FOOTER_FONT)
            .setFontSizeFooter(FOOTER_FONT_SIZE)
            .setTitleFont(TITLE_FONT)
            .setFontSizeTitle(TITLE_FONT_SIZE)
            .setSubTitleFont(SUBTITLE_FONT)
            .setFontSizeSubTitle(SUBTITLE_FONT_SIZE)
            .setHeaderSize(HEADER_SIZE)
            .setUnidad("XXXXXX")
            .setDependencia("XXXXX")
            .setUsuario("XXXXXX")
            .setTitle("REPORTE DE ESTRUCTURA DEL SISTEMA")
            .setSubTitle("SUBSISTEMA : " + subsistema)
            .build();
        return table;
    }   
}
