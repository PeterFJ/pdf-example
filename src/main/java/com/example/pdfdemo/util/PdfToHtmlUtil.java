package com.example.pdfdemo.util;

import com.example.pdfdemo.constans.LoanProtocolConstans;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

/**
 * @Author: fengjian
 * @Date: 2020/12/23 5:34 下午
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PdfToHtmlUtil {

    private final LoanProtocolConstans constans;
    private Template template;
    private FontProvidesr fontProvidesr;

    public void fontsInit(LoanProtocolConstans constans) {
        log.info("============初始字体到内存============");
        fontProvidesr = new FontProvidesr(constans.fontsPath + constans.simsun);
    }

    public void templateInit(LoanProtocolConstans constans) {
        try {
            log.info("============初始模板到内存============");
            Configuration freemarkerCfg = new Configuration();
            File templatesFile = new File(constans.templatesPath);
            freemarkerCfg.setDirectoryForTemplateLoading(templatesFile);
            // 获取模板,并设置编码方式
            template = freemarkerCfg.getTemplate(constans.templateName);
            template.setEncoding("UTF­8");
        } catch (Exception e) {
            throw new RuntimeException("初始模板到内存失败");
        }
    }


    /**
     * 读取html
     *
     * @param data
     * @param htmlTmp
     * @return
     */
    public String freeMarkerRender(Map<String, Object> data, String htmlTmp) {
        Writer out = new StringWriter();
        try {
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

    private void createPdf(String content, String dest) {
        //A4纸大小
        Document document = new Document(PageSize.A4);
        PdfWriter pdfWriter = null;
        try {
            pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();
            XMLWorkerHelper worker = XMLWorkerHelper.getInstance();
            worker.parseXHtml(pdfWriter, document, new ByteArrayInputStream(content.getBytes()), null, fontProvidesr);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    public void writePdf(Map<String, Object> data, String pdfPath) {
        createPdf(freeMarkerRender(data, constans.templateName), pdfPath);
    }

}
