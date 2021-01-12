package com.example.pdfdemo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Author: fengjian
 * @Date: 2021/1/4 2:13 下午
 * @Description:
 */
@Data
@Builder
public class FailExcelDto {

    @ExcelProperty(value = {"Loan Number"}, index = 0)
    private String loanNo;

    @ExcelProperty(value = {"Reasons for Export Failure"}, index = 1)
    private String failMsg;

}
