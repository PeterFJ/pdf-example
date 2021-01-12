package com.example.pdfdemo.conf;

import com.example.pdfdemo.constans.LoanProtocolConstans;
import com.example.pdfdemo.util.FileUtils;
import com.example.pdfdemo.util.PdfToHtmlUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;

/**
 * @Author: fengjian
 * @Date: 2020/12/24 10:19 上午
 * @Description: 初始化协议配置等文件
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProtocolInitConf implements CommandLineRunner {

    private final LoanProtocolConstans protocolConstans;
    private final PdfToHtmlUtil pdfToHtmlUtil;

    @Override
    public void run(String... args) throws Exception {
        this.fontsInit();
        this.templateInit();
        this.pdfInit();
        this.imagesInit();

//        pdfToHtmlUtil.fontsInit(protocolConstans);
//        pdfToHtmlUtil.templateInit(protocolConstans);
    }

    private void imagesInit() {
        log.info("============初始图片开始============");
        FileUtils.deleteDir(protocolConstans.imagesPath);
        String[] templateNames = {protocolConstans.nbfcSign};
        if (templateNames.length == 0) {
            log.error("============图片文件不存在");
            log.error("============初始图片文件失败============");
        }
        for (int i = 0; i < templateNames.length; i++) {
            try {
                InputStream in = this.getClass().getClassLoader().getResourceAsStream("protocol/images/" + templateNames[i]);
                File file = new File(protocolConstans.imagesPath + templateNames[i]);
                if (file.exists() == false) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                OutputStream out = new FileOutputStream(file);
                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = in.read(buff)) != -1) {
                    out.write(buff, 0, len);
                }
                in.close();
                out.close();
                log.info("============初始图片" + templateNames[i] + "成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("============初始图片结束============");
    }

    private void pdfInit() {
        log.info("============初始化pdf目录开始============");
        File pdfPath = new File(protocolConstans.pdfPath);
        if (!pdfPath.exists()) {
            pdfPath.mkdirs();
        }
        log.info("============初始化pdf目录结束============");
    }

    private void fontsInit() {
        log.info("============初始化字体开始============");
        InputStream fontawesomeWebIn = null;
        InputStream simsunIn = null;
        OutputStream simsunOut = null;
        OutputStream fontawesomeWebOut = null;
        try {
            FileUtils.deleteDir(protocolConstans.fontsPath);
            fontawesomeWebIn = this.getClass().getClassLoader().getResourceAsStream("protocol/fonts/fontawesomeWebfont.ttf");
            simsunIn = this.getClass().getClassLoader().getResourceAsStream("protocol/fonts/simsun.ttf");
            if (fontawesomeWebIn == null || simsunIn == null) {
                log.error("============字体文件不存在!");
                log.error("============初始化字体失败============");
                return;
            }
            //写出字体 -- simsun
            File simsun = new File(protocolConstans.fontsPath + "simsun.ttf");
            if (simsun.exists() == false) {
                simsun.getParentFile().mkdirs();
                simsun.createNewFile();
            }
            simsunOut = new FileOutputStream(simsun);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = simsunIn.read(buff)) != -1) {
                simsunOut.write(buff, 0, len);
            }
            log.info("============初始化字体simsun成功");

            //写出字体 -- fontawesomeWeb
            File fontawesomeWeb = new File(protocolConstans.fontsPath + "fontawesomeWebfont.ttf");
            if (fontawesomeWeb.exists() == false) {
                fontawesomeWeb.getParentFile().mkdirs();
                fontawesomeWeb.createNewFile();
            }
            fontawesomeWebOut = new FileOutputStream(fontawesomeWeb);
            byte[] fontawesomeWebBuff = new byte[1024];
            int fontawesomeWebLen = 0;
            while ((fontawesomeWebLen = fontawesomeWebIn.read(fontawesomeWebBuff)) != -1) {
                fontawesomeWebOut.write(fontawesomeWebBuff, 0, fontawesomeWebLen);
            }
            log.info("============初始化字体fontawesomeWebLen成功");
            log.info("============初始化字体结束============");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("============初始化字体失败============");
        } finally {
            try {
                if (fontawesomeWebIn != null) fontawesomeWebIn.close();
                if (simsunIn != null) simsunIn.close();
                if (simsunOut != null) simsunIn.close();
                if (fontawesomeWebOut != null) simsunIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void templateInit() {
        log.info("============初始模板文件开始============");
        FileUtils.deleteDir(protocolConstans.templatesPath);
        String[] templateNames = {protocolConstans.templateName};
        if (templateNames.length == 0) {
            log.error("============模板文件不存在");
            log.error("============初始模板文件失败============");
        }
        for (int i = 0; i < templateNames.length; i++) {
            try {
                InputStream in = this.getClass().getClassLoader().getResourceAsStream("protocol/templates/" + templateNames[i]);
                File file = new File(protocolConstans.templatesPath + templateNames[i]);
                if (file.exists() == false) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                OutputStream out = new FileOutputStream(file);
                byte[] buff = new byte[1024];
                int len = 0;
                while ((len = in.read(buff)) != -1) {
                    out.write(buff, 0, len);
                }
                in.close();
                out.close();
                log.info("============初始模板" + templateNames[i] + "成功");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("============初始模板文件结束============");
    }
}
