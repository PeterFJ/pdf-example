package com.example.pdfdemo.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

/**
 * @Author: fengjian
 * @Date: 2021/1/5 10:41 上午
 * @Description:
 */
@Data
public class UploadExcelDto extends BaseRowModel {

    @ExcelProperty(value = {"Loan Number"}, index = 0)
    private String loanNo;

}
