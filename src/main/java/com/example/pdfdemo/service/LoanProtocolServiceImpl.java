package com.example.pdfdemo.service;


import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.example.pdfdemo.constans.LoanProtocolConstans;
import com.example.pdfdemo.dto.TemplateParam;
import com.example.pdfdemo.util.DownUtils;
import com.example.pdfdemo.util.PdfToHtmlUtil;
import com.example.pdfdemo.util.ZipUtils;
import com.example.pdfdemo.util.thread.ITask;
import com.example.pdfdemo.util.thread.MultiThreadUtils;
import com.example.pdfdemo.util.thread.bean.ResultBean;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: fengjian
 * @Date: 2020/12/23 6:22 下午
 * @Description:
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoanProtocolServiceImpl implements LoanProtocolService {

    private final PdfToHtmlUtil pdfToHtmlUtil;
    private final LoanProtocolConstans constans;


    @Override
    public void downBatch(HttpServletRequest request, HttpServletResponse response, List<String> loans) {
        // 创建pdf目录文件夹
        String now = "2020-01-01";
        FileUtil.mkdir(constans.getPackagePath(now));

        // 开启多线程处理任务
        MultiThreadUtils<String> threadUtils = MultiThreadUtils.getInstance();
        ITask<ResultBean<String>, String> genLoanProTask = (loanNo, params) -> {
            ResultBean<String> resultBean = ResultBean.newInstance();
            resultBean.setData(loanNo);
            String pdfPath = constans.getPdfPath(now, loanNo);
            try {
                pdfToHtmlUtil.writePdf(JSON.parseObject(JSON.toJSONString(this.getProtocolParam(loanNo))), pdfPath);
            } catch (Exception e) {
                resultBean.setCode(ResultBean.FAIL);
                resultBean.setMsg(StringUtils.left(e.getMessage(), 200));
                log.error("downBatch task error, msg: {}", e.getMessage());
            }
            return resultBean;
        };

        ResultBean<List<ResultBean<String>>> resultBean = threadUtils.execute(constans.threadCount, loans, genLoanProTask);
        Optional.ofNullable(resultBean).ifPresent(e -> {
            List<ResultBean<String>> errorData = e.getData().stream()
                    .filter(bean -> bean.getCode() == ResultBean.FAIL)
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(errorData)) {
                // 处理异常数据，输出到excel
//                ExcelUtil.writerExcel(constans.getExportFailPathName(now),
//                        errorData.stream().map(error -> FailExcelDto.builder().loanNo(error.getData())
//                                .failMsg(error.getMsg()).build()).collect(Collectors.toList()),
//                        FailExcelDto.class);
            }
        });

        // 把贷款协议pdf 进行压缩
        ZipUtils.toZip(constans.getPackagePath(now), constans.getZipPath(now), true);

        // 下载压缩包
        this.downloadFile(constans.getZipPath(now), constans.getZipPathName(now), request, response);

        // 删除下载的文件夹
//        FileUtil.del(constans.pdfPath);
    }



    @Override
    public TemplateParam getProtocolParam(String loanNo) {
        TemplateParam param = new TemplateParam();
        param.setCustName("名字");
        param.setLoanNo(loanNo);
        return param;
    }

    private void downloadFile(String src, String fileName, HttpServletRequest request, HttpServletResponse response) {
        try {
            // 下载压缩包
            DownUtils.download(src, fileName, request, response);
        } catch (Exception e) {
            log.error("文件下载错误，{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

}
