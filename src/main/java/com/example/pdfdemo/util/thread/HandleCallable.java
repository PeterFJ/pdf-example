package com.example.pdfdemo.util.thread;

import com.example.pdfdemo.util.thread.bean.ResultBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @Author: fengjian
 * @Date: 2020/12/23 11:56 下午
 * @Description:
 */
@Slf4j
public class HandleCallable<E> implements Callable<ResultBean> {

    // 线程名称
    private String threadName = "";
    // 需要处理的数据
    private List<E> data;
    // 辅助参数
    private Map<String, Object> params;
    // 具体执行任务
    private ITask<ResultBean<String>, E> task;

    public HandleCallable(String threadName, List<E> data, Map<String, Object> params,
                          ITask<ResultBean<String>, E> task) {
        this.threadName = threadName;
        this.data = data;
        this.params = params;
        this.task = task;
    }

    @Override
    public ResultBean<List<ResultBean<String>>> call() throws Exception {
        Thread.currentThread().setName(this.threadName);
        // 该线程中所有数据处理返回结果
        ResultBean<List<ResultBean<String>>> resultBean = ResultBean.newInstance();
        if (CollectionUtils.isNotEmpty(data)) {
            log.info("线程: {}, 共处理: {}个数据, 开始处理......", threadName, data.size());
            // 返回结果集
            List<ResultBean<String>> resultList = new ArrayList<>();
            // 循环处理每个数据
            for (int i = 0; i < data.size(); i++) {
                // 需要执行的数据
                E e = data.get(i);
                // 将数据执行结果加入到结果集中
                resultList.add(task.execute(e, params));
            }
            log.info("线程：{}, 共处理: {}个数据, 处理完成......", threadName, data.size());
            resultBean.setData(resultList);
        }
        return resultBean;
    }
}
