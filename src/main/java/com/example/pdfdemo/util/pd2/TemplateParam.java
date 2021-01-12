package com.example.pdfdemo.util.pd2;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: fengjian
 * @Date: 2020/12/24 12:28 上午
 * @Description: 模板动态参数 初始化为空，放置模板转换报错
 */
@Getter
@Setter
public class TemplateParam {

    // 贷款编号
    private String loanNo = "";

    // 协议签署时间
    // 取值为提现成功，日期格式为：dd/mm/yyyy
    // 取自：根据贷款编号查询fin_rc_dtl表TRANSTER_TYPE=10440002并且BEF_ABLE=10000000的INST_TIME
    private String protocolTime = "";

    // 取表lo_loan_cust_cert_rel该贷款编号对应的CUST_NAME
    private String custName = "";
    private String custNameB = "";
    private String nbfcImagePath = "";

    // 取表lo_loan_cust_cert_rel该贷款编号对应的PAN_NO
    private String panNo = "";

    // 取表pan ocr该贷款编号对应的客户编号的father name
    private String fatherName = "";

    // 取lo_loan_cust_cert_rel表该贷款编号对应的REG_ADDR
    private String regAddr = "";

    // 借款金额
    // 取lo_loan_dtl表LOAN_AMT
    private String loanAmt = "";

    // X days
    // x取lo_loan_prod_rel表STEP_SIZE
    //
    // 借款期限
    // 取lo_loan_prod_rel表STEP_SIZE，months
    // 改为days
    private String stepSize = "";

    // 取值为应还金额:借款金额+固定利息
    private String repayAmt = "";

    // 固定利息：
    // 根据贷款编号查询lo_loan_fee_rel表取SUBJ_NO为12的VAL
    private String interest = "";

    // 信用报告费
    // 根据贷款编号查询lo_loan_fee_rel表取SUBJ_NO为20的VAL
    private String creditRepCost = "";

    // 计算年化利率：固定利息*360/（借款期限*借款金额）
    // 以百分数形式表示，向上取整，百分号内保留整数
    private String yearRate = "";

}
