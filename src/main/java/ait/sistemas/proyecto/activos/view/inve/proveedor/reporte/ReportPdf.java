package ait.sistemas.proyecto.activos.view.inve.proveedor.reporte;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import ait.sistemas.proyecto.activos.data.model.ProveedoresModel;
import ait.sistemas.proyecto.activos.data.service.Impl.ProveedorImpl;
import ait.sistemas.proyecto.common.component.PathValues;
import ait.sistemas.proyecto.common.report.Column;
import ait.sistemas.proyecto.common.report.PDFMultiTableGenerator;
import ait.sistemas.proyecto.common.report.PDFTableGenerator;
import ait.sistemas.proyecto.common.report.Table;
import ait.sistemas.proyecto.common.report.TableBuilder;

public class ReportPdf {
	 // Page configuration
    private static final PDRectangle PAGE_SIZE = PDRectangle.LETTER;
    private static final float MARGIN = 60;
    private static final boolean IS_LANDSCAPE = false;

    // Font for textFont
    private static final PDFont TEXT_FONT = PDType1Font.HELVETICA;
    private static final float FONT_SIZE = 8;
    
    /**
     * Font for footer report
     */
    private static final PDFont FOOTER_FONT = PDType1Font.HELVETICA;
    private static final float FOOTER_FONT_SIZE = 9;
    
    /**
     * Font for footer report
     */
    private static final PDFont HEADER_FONT = PDType1Font.HELVETICA;
    private static final float HEADER_FONT_SIZE = 10;
    
    /**
     * Font for footer report
     */
    private static final PDFont TITLE_FONT = PDType1Font.HELVETICA_BOLD;
    private static final float TITLE_FONT_SIZE = 14;
    
    /**
     * Font for footer report
     */
    private static final PDFont SUBTITLE_FONT = PDType1Font.HELVETICA;
    private static final float SUBTITLE_FONT_SIZE = 11;
    
    private static final float ROW_HEIGHT = 15;
    private static final float CELL_MARGIN = 2;
    
    private static final int HEADER_SIZE = 5;
    
   
    public static String SAVE_PATH = PathValues.PATH_REPORTS + "Informe-Proveedor"+ String.valueOf(new java.util.Date().getTime()) + ".pdf";
    
    final ProveedorImpl provedorimpl = new ProveedorImpl();
    public boolean getPdf(String[][] data, String strCiudad,String strDependencia) throws IOException{
    	
 
        return new PDFTableGenerator().generatePDF(createContent(data, strCiudad, strDependencia), SAVE_PATH);
        
    }
    
    public boolean getPdfMulti(int[][] map) throws IOException{
    	
    	Table[] tablas = new Table[map.length];
    	int rtable = 0;
    	String strCiudad="",strDependencia="";
    	for (int i = 0; i < map.length; i++) {
    		String[][] str_table = new String[map[i][1]][7];
    		int r = 0;
    		for (ProveedoresModel proveedor : provedorimpl.getByCiudad(map[i][0])) {
    			strCiudad = proveedor.getPRV_Ciudad();
    			strDependencia = proveedor.getPRV_Dependencia();
    			String[] row = { 
    					proveedor.getPRV_NIT(), 
    					proveedor.getPRV_Nombre(),
    					proveedor.getPRV_Ciudad(),
    					proveedor.getPRV_Domicilio(),
    					proveedor.getPRV_Telefono(),
    					proveedor.getPRV_Celular_Contacto(),
    					proveedor.getPRV_Nombre_Contacto()
    			};
    			str_table[r] = row;
    			r++;
			}
    		tablas[rtable] = createContent(str_table, strCiudad, strDependencia);
    		rtable++;
		}
    	
    		
        return new PDFMultiTableGenerator().generatePDF(tablas, SAVE_PATH);
        
    }
    
    public boolean getPdfT(String[][] data) throws IOException{
    	
    	
    	return new PDFTableGenerator().generatePDF(createContentT(data), SAVE_PATH);
    	
    }

    
    
    private static Table createContent(String[][] data, String strCiudad , String strDependencia) {
        List<Column> columns = new ArrayList<Column>();
        columns.add(new Column("NIT", 60));	
        columns.add(new Column("Nombre del Proveedor", 115));
        columns.add(new Column("Ciudad", 60));
        columns.add(new Column("Domicilio", 105));
        columns.add(new Column("Telefono", 50));
        columns.add(new Column("Celular", 50));
        columns.add(new Column("Nombre Contacto", 85));
        
 
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
            .setTitle("PROVEEDORES")
            .setSubTitle("Ciudad: "+ strCiudad +"   "+"Dependencia : " + strDependencia)
            .build();
        return table;
    }   
    private static Table createContentT(String[][] data) {
    	List<Column> columns = new ArrayList<Column>();
    	columns.add(new Column("NIT", 60));	
    	columns.add(new Column("Nombre del Proveedor", 115));
    	columns.add(new Column("Ciudad", 60));
    	columns.add(new Column("Domicilio", 105));
    	columns.add(new Column("Telefono", 50));
    	columns.add(new Column("Celular", 50));
    	columns.add(new Column("Nombre Contacto", 85));
    	
    	
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
    	.setTitle("PROVEEDORES")
    	.setSubTitle(" ")
    	.build();
    	return table;
    }   
}
