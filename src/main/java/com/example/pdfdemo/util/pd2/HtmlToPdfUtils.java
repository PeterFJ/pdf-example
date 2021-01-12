package com.example.pdfdemo.util.pd2;

import com.alibaba.fastjson.JSON;
import com.itextpdf.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Map;

/**
 * @Author: fengjian
 * @Date: 2020/12/23 3:51 下午
 * @Description: 对应模板
 */
public class HtmlToPdfUtils {
    private HtmlToPdfUtils() {
    }

    /**
     * 读取html
     *
     * @param data
     * @return
     */
    public static String freeMarkerRender(Map<String, Object> data) {
        Writer out = new StringWriter();
        try {
            Configuration freemarkerCfg = new Configuration();
            File templatesFile = new File("/Users/fengjian/Desktop/app/server/qt-fpdl-mgt/protocol/templates");
            freemarkerCfg.setDirectoryForTemplateLoading(templatesFile);
            // 获取模板,并设置编码方式
            Template template = freemarkerCfg.getTemplate("loan_protocol_template.html");
            template.setEncoding("UTF­8");
            // 合并数据模型与模板
            // 将合并后的数据和模板写入到流中，这里使用的字符流
            template.process(data, out);
            out.flush();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * html转换成PDF
     *
     * @param pdfPath  pdf路径
     * @throws Exception 异常
     */
    public static void htmlToPdf(String pdfPath) throws Exception {

        OutputStream os = new FileOutputStream(pdfPath);
        ITextRenderer iTextRenderer = new ITextRenderer();
        iTextRenderer.setDocumentFromString(freeMarkerRender(getParams()));

        //解决中文编码
        ITextFontResolver fontResolver = iTextRenderer.getFontResolver();
        fontResolver.addFont("/Users/fengjian/Desktop/app/server/qt-fpdl-mgt/protocol/fonts/simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        iTextRenderer.layout();
        iTextRenderer.createPDF(os);
        os.flush();
        os.close();
    }

    private static Map<String, Object> getParams() {
        TemplateParam param = new TemplateParam();
        param.setCustName("名称");
        param.setRegAddr("dizhi");
        param.setProtocolTime("2020/01/01");
        return JSON.parseObject(JSON.toJSONString(param));
    }

    public static void main(String[] args) {
        String pdfPath = "/Users/fengjian/Downloads/text.pdf";
        try {
            htmlToPdf(pdfPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
