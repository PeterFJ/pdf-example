package com.example.pdfdemo.util.thread;

import com.example.pdfdemo.util.thread.bean.ResultBean;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: fengjian
 * @Date: 2020/12/23 11:49 下午
 * @Description:
 */
@Slf4j
@Data
public class MultiThreadUtils<T> {
    // 创建线程池
    private ExecutorService threadpool = new ThreadPoolExecutor(10, 20,
            10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(2048));
    // 线程池管理器
    private CompletionService<ResultBean> pool = new ExecutorCompletionService<>(threadpool);

    private MultiThreadUtils() {
    }

    private static final MultiThreadUtils instance = new MultiThreadUtils();

    public static MultiThreadUtils getInstance() {
        return instance;
    }

    /**
     * 多线程分批执行list中的任务<BR>
     * 方法名：execute<BR>
     *
     * @param threadCount   每条线程处理的数量
     * @param data          线程处理的大数据量list
     * @param task          具体执行业务的任务接口
     */
    @SuppressWarnings("rawtypes")
    public ResultBean execute(int threadCount, List<T> data, ITask<ResultBean<String>, T> task) {
        // 开始时间
        Stopwatch start = Stopwatch.createStarted();
        // 数据分割
        List<List<T>> partitionList = Lists.partition(data, threadCount);
        AtomicInteger countAtm = new AtomicInteger(0);
        for (List<T> par : partitionList) {
            // 将数据分配给各个线程
            String threadName = "multi-task-" + countAtm.getAndIncrement();
            HandleCallable execute = new HandleCallable<T>(threadName, par, Maps.newHashMap(), task);
            // 将线程加入到线程池
            pool.submit(execute);
        }

        // 总的返回结果集
        List<ResultBean<String>> result = new ArrayList<>();
        for (int i = 0; i < partitionList.size(); i++) {
            // 每个线程处理结果集
            ResultBean<List<ResultBean<String>>> threadResult;
            try {
                threadResult = pool.take().get();
                result.addAll(threadResult.getData());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

        }
        // 关闭线程池
        //threadpool.shutdownNow();
        log.info("MultiThreadUtils > execute 总耗时: {}s", start.elapsed(TimeUnit.SECONDS));
        return ResultBean.newInstance().setData(result);
    }

}
