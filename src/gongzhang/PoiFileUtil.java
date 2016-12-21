package gongzhang;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
 
public class PoiFileUtil {

	/**
	 * 
	 * @param file 要读取的EXCEL文件
	 * @param startRow 数据的起始行（从0开始）
	 * @param cols 列对应的字段
	 * @return 返回list<map>
	 * @throws IOException
	 */
	public static List<Map> readExcel(File file, int startRow,List<String> cols) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		List<Map> lm = new ArrayList<Map>();
		Map map = new HashMap();
		int c = 0;
		int r = 0;
		Workbook wb = null;
		if(file.getName().endsWith("xlsx")){
			wb = new XSSFWorkbook(fis);
		} else {
			wb = new HSSFWorkbook(fis);
		}
		int sheets = wb.getNumberOfSheets();
		Sheet sheet ;
		for(int i = 0; i < sheets; i++) {
			sheet = wb.getSheetAt(i);
			for (Row row : sheet) {
				map = new HashMap();
				if(row.getRowNum()<startRow){
					continue;
				}
				for (Cell cell : row) {
					c=cell.getColumnIndex();
					switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							map.put(cols.get(c), cell.getRichStringCellValue().getString());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) {
								map.put(cols.get(c), new Date(cell.getDateCellValue().getTime()));
							} else {
								map.put(cols.get(c), cell.getNumericCellValue());
							}
							break;
						case Cell.CELL_TYPE_BOOLEAN:
							map.put(cols.get(c), cell.getBooleanCellValue());
							System.out.println(cell.getBooleanCellValue());
							break;
						case Cell.CELL_TYPE_FORMULA:
							map.put(cols.get(c), cell.getCellFormula());
							break;
					}
				}
				lm.add(map);
			}
		}
		return lm;
	}
	
	public static void main(String[] args) {
		File excelFile = new File("C:\\Users\\zuobin\\Desktop\\yinzhang.xlsx");
		List cols = new ArrayList<String>();
		cols.add("name");
		try {
			List<Map> resultList = PoiFileUtil.readExcel(excelFile, 0, cols);
			for(Map map : resultList){
				System.out.println(map.get("name"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
