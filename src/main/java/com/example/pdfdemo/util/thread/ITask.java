package com.example.pdfdemo.util.thread;

import java.util.Map;

/**
 * @Author: fengjian
 * @Date: 2020/12/23 11:53 下午
 * @Description: 任务处理接口
 *
 *  T 返回值类型
 *  E 传入值类型
 */
public interface ITask<T, E> {

    /**
     *
     * 任务执行方法接口
     * 方法名：execute
     */
    T execute(E e, Map<String, Object> params);
}
