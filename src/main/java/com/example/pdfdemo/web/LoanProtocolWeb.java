package com.example.pdfdemo.web;

import com.example.pdfdemo.constans.LoanProtocolConstans;
import com.example.pdfdemo.dto.UploadExcelDto;
import com.example.pdfdemo.service.LoanProtocolService;
import com.example.pdfdemo.util.ExcelUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: fengjian
 * @Date: 2021/1/9 3:10 下午
 * @Description:
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LoanProtocolWeb {

    private final LoanProtocolConstans constans;
    private final LoanProtocolService loanProtocolService;

    /**
     * 贷款协议 批量下载
     *
     * @param file
     * @param request
     * @param response
     */
    @PostMapping(value = "/down/batch")
    public String downBatch(@RequestParam("file") MultipartFile file,
                                   HttpServletRequest request,
                                   HttpServletResponse response) {
        List<UploadExcelDto> excelDtos;
        try {
            excelDtos = ExcelUtil.readExcel(file, 1, 1, UploadExcelDto.class);
        } catch (Exception e) {
            log.error("Read excel error, msg: {}", e.getMessage());
            throw new RuntimeException( "Read excel error.");
        }

        if (CollectionUtils.isEmpty(excelDtos)) {
            throw new RuntimeException(
                    "Excel data is null.");
        }
        if (excelDtos.size() > constans.maxCount) {
            throw new RuntimeException(
                    "The number of uploads is over 1000, please upload again.");
        }
        loanProtocolService.downBatch(request, response,
                excelDtos.stream().map(UploadExcelDto::getLoanNo).collect(Collectors.toList()));
        return "secusses";
    }

}
