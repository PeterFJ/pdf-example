package com.example.pdfdemo.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.metadata.Table;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * <p> The describe </p>
 *
 * @author zhengLin
 */
@Slf4j
public class ExcelUtil {

    /**
     * 导出excel
     *
     * @param fileName 导出的文件名
     * @param response 响应
     * @param request  请求
     * @param lists    导出的数据
     * @param <T>      导出的实体类泛型
     */
    public static <T> void writerExcel(String fileName, HttpServletResponse response, HttpServletRequest request, List<T> lists, Class<T> clazz) {
        String sheetName = fileName;
        try {
            String userAgent = request.getHeader("User-Agent");

            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                // 针对IE或者以IE为内核的浏览器：
                fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
            } else {
                // 非IE浏览器的处理:
                fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
            }
            response.setHeader("Content-disposition", String.format("attachment; filename=\"%s\"", fileName + ".xlsx"));
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", -1);
        } catch (UnsupportedEncodingException e1) {
            log.error("导出excel未知编码异常", e1);
        }
        try {
            EasyExcel.write(response.getOutputStream(), clazz)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .sheet(sheetName)
                    .doWrite(lists);
        } catch (IOException e) {
            log.error("导出excel文件异常", e);
        }
    }

    /**
     * 输出excel到磁盘
     *
     * @param fileName
     * @param lists
     * @param clazz
     * @param <T>
     */
    public static <T> void writerExcel(String fileName, List<T> lists, Class<T> clazz) {
        try {
            EasyExcel.write(new File(fileName), clazz).sheet().doWrite(lists);
        } catch (Exception e) {
            log.error("导出excel文件异常", e);
        }
    }

    /**
     * @param file        MultipartFile excel文件
     * @param sheetNo     sheet页
     * @param headLineMun 起始行
     * @param t           返回实体类型 需要继承自 com.alibaba.excel.metadata.BaseRowModel
     * @return list集合
     * @author zhengLin
     * @description 读取excel
     * @date 2019/3/14 16:30
     */
    public static <T extends BaseRowModel> List<T> readExcel(MultipartFile file, int sheetNo, int headLineMun, Class<T> t) throws IOException {
        InputStream inputStream = file.getInputStream();
        ExcelListener excelListener = new ExcelListener();
        EasyExcelFactory.readBySax(inputStream, new Sheet(sheetNo, headLineMun, t), excelListener);
        List<T> list = excelListener.getData();
        if (null != inputStream) {
            inputStream.close();
        }
        return list;
    }


    public static <T extends BaseRowModel> void excelExportUtil(List<T> dataList, String sheetName, Class<T> modelClass, HttpServletResponse response) throws IOException {
        String fileName = "list.xlsx";
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream out = response.getOutputStream();
        ExcelWriter writer = EasyExcelFactory.getWriter(out);
        Sheet sheet = new Sheet(1, 0);
        sheet.setSheetName(sheetName);
        Table table = new Table(1);
        table.setClazz(modelClass);
        writer.write(dataList, sheet, table);
        writer.finish();
        out.flush();
    }

    public static List<Map<String, String>> parseExcel(InputStream fis) {
        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        try {
            XSSFWorkbook book = new XSSFWorkbook(fis);
            XSSFSheet sheet = book.getSheetAt(0);
            int firstRow = sheet.getFirstRowNum();
            int lastRow = sheet.getLastRowNum();
            // 除去表头和第一行
            for (int i = firstRow; i < lastRow; i++) {
                Map map = new LinkedHashMap();

                XSSFRow row = sheet.getRow(0);
                int firstCell = row.getFirstCellNum();
                int lastCell = row.getLastCellNum();

                for (int j = firstCell; j < lastCell; j++) {

                    XSSFCell keyCell = row.getCell(j);
                    if (keyCell.getCellTypeEnum() != CellType.NUMERIC) {
                        keyCell.setCellType(CellType.STRING);
                    }
                    String key = keyCell.getStringCellValue();

                    XSSFCell valCell = sheet.getRow(i + 1).getCell(j);
                    if (null != valCell) {
                        if (valCell.getCellTypeEnum() != CellType.STRING) {
                            valCell.setCellType(CellType.STRING);
                        }
                        String value = valCell.getStringCellValue();
                        if (StringUtils.isNotBlank(value)) {
                            map.put(key, value);
                        }
                    }
//					System.out.println(map);
                }
                data.add(map);
            }
//			System.out.println(data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static void main(String[] args) throws IOException {
        List<DateModel> list = new ArrayList<>();
        list.add(new DateModel("name", 1));
        list.add(new DateModel("name1", 2));
        writerExcel("/Users/fengjian/Desktop/test1.xlsx", list, DateModel.class);
    }
}


@Data
class DateModel extends BaseRowModel {
    @ExcelProperty(value = {"name"}, index = 1)
    private String name;

    @ExcelProperty(value = {"表头4"}, index = 2)
    private int age;

    public DateModel(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
