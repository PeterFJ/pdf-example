package com.example.pdfdemo.constans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Author: fengjian
 * @Date: 2020/12/23 9:31 下午
 * @Description:
 */
@Component
@PropertySource(value = "classpath:protocol/protocol.properties")
public class LoanProtocolConstans {

    /**
     * 模板名称
     */
    @Value("${loan.protocol.template.name}")
    public String templateName;

    /**
     * 字体名称
     */
    @Value("${loan.protocol.fonts.simsun}")
    public String simsun;

    /**
     * 模板目录
     */
    @Value("${loan.protocol.path.templates}")
    public String templatesPath;

    /**
     * 字体目录
     */
    @Value("${loan.protocol.path.fonts}")
    public String fontsPath;

    /**
     * 字体目录
     */
    @Value("${loan.protocol.path.pdf}")
    public String pdfPath;

    /**
     * 图片目录
     */
    @Value("${loan.protocol.path.images}")
    public String imagesPath;

    /**
     * 图片名称
     */
    @Value("${loan.protocol.images.nbfcsign}")
    public String nbfcSign;

    /**
     * 最大处理数量
     */
    @Value("${loan.protocol.thread.max.count}")
    public int maxCount;

    /**
     * 每条线程处理的数量
     */
    @Value("${loan.protocol.thread.count}")
    public int threadCount;





    /*====================================================================*/
    // 输出文件信息

    public final String FILE_PREFIX = "LOAN_AGREEMENT_";

    public final String ERROR_PREFIX = "EXPORT_FAILURE_RECORD_";

    public final String PDF_SUFFIX = ".pdf";

    public final String ZIP_SUFFIX = ".zip";

    public final String PATH_EXCEL = ".xlsx";

    public final String PATH_SEPARATE = "/";

    /**
     * 输出目录
     *
     * @param nowTime
     * @return
     */
    public String getPackagePath(String nowTime) {
        return this.pdfPath + this.FILE_PREFIX + nowTime + this.PATH_SEPARATE;
    }

    /**
     * 压缩包目录名
     *
     * @param nowTime
     * @return
     */
    public String getZipPathName(String nowTime) {
        return this.FILE_PREFIX + nowTime + this.ZIP_SUFFIX;
    }

    /**
     * 压缩包全路径
     *
     * @param nowTime
     * @return
     */
    public String getZipPath(String nowTime) {
        return this.pdfPath + getZipPathName(nowTime);
    }

    /**
     * pdf文件名
     *
     * @param loanNo
     * @return
     */
    public String getPdfName(String loanNo) {
        return this.FILE_PREFIX + loanNo + this.PDF_SUFFIX;
    }

    /**
     * pdf完整路径
     *
     * @param nowTime
     * @param loanNo
     * @return
     */
    public String getPdfPath(String nowTime, String loanNo) {
        return this.getPackagePath(nowTime) + getPdfName(loanNo);
    }

    /**
     * 导出失败excel路径
     *
     * @return
     */
    public String getExportFailPathName(String nowTime) {
        return this.getPackagePath(nowTime) + this.ERROR_PREFIX + nowTime + this.PATH_EXCEL;
    }

}
