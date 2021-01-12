package com.example.pdfdemo.service;


import com.example.pdfdemo.dto.TemplateParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author: fengjian
 * @Date: 2020/12/23 6:20 下午
 * @Description: 贷款协议相关
 */
public interface LoanProtocolService {

    /**
     * 批量下载
     *
     * @param request
     * @param response
     * @param list
     */
    void downBatch(HttpServletRequest request, HttpServletResponse response, List<String> list);



    /**
     * 获取协议动态参数
     *
     * @param loanNo
     * @return
     */
    TemplateParam getProtocolParam(String loanNo);
}
