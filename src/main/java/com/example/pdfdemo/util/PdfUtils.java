package com.example.pdfdemo.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.*;

/**
 * pdf工具类
 *
 * @author yiqr
 * @date 2019-03-20 15:43
 * @Description: TODO
 */
public class PdfUtils {

    /**
     * 将HTML转化成pdf文件
     *
     * @param htmlName HTML全路径（文件路径+文件名+后缀）
     * @param pdfName  被生成的PDF全路径
     * @param ttf      字体文件路径
     */
    public static void htmlToPdfA4(String htmlName, String pdfName, String ttf) {
        Document document = new Document(PageSize.A4); //A4纸大小
        BufferedReader br = null;
        FileReader reader = null;
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfName));
            document.open();
            reader = new FileReader(htmlName);
            br = new BufferedReader(reader);
            String temStr = null;
            String inputStr = "";
            while ((temStr = br.readLine()) != null) {
                inputStr += temStr;
            }
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            worker.parseXHtml(pdfWriter, document, new ByteArrayInputStream(inputStr.getBytes()), null, new FontProvidesr(ttf));
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
                if (reader != null) reader.close();
                if (document != null) document.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class FontProvidesr extends XMLWorkerFontProvider {
    private String ttf; //字体文件路径

    /**
     * 字体路径
     *
     * @param ttf
     */
    public FontProvidesr(String ttf) {
        super();
        this.ttf = ttf;
    }

    public Font getFont(final String fontname, final String encoding,
                        final boolean embedded, final float size, final int style,
                        final BaseColor color) {
        BaseFont bf = null;
        try {
            bf = BaseFont.createFont(ttf, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Font font = new Font(bf, size, style, color);
        font.setColor(color);
        return font;
    }
}
