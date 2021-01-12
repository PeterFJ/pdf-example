package com.example.pdfdemo.util;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> The describe </p>
 *
 * @author zhengLin
 */
public class ExcelListener extends AnalysisEventListener {
    private List<Object>  data = new ArrayList<Object>();

    @Override
    public void invoke(Object object, AnalysisContext context) {
        data.add(object);

    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        doSomething();
    }
    public void doSomething(){

    }
    public List getData() {
        return data;
    }
}
