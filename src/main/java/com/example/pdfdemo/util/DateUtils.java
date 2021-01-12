package com.example.pdfdemo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author yiqr
 * @date 2019-02-27 14:41
 * @Description: TODO 时间类型工具类
 */
public class DateUtils {

    public static Date getHour(int hours) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        return calendar.getTime();
    }

    public static String format(Date date, String pattern){
        if(date==null) return null;
        if (pattern == null || pattern.trim().length()==0) pattern = "yyyy-MM-dd";
        SimpleDateFormat tempDate = new SimpleDateFormat(pattern);
        tempDate.setLenient(false);
        return (tempDate.format(date));
    }

    public static Date format(String date, String pattern){
        if(date==null) return null;
        if (pattern == null || pattern.trim().length()==0) pattern = "yyyy-MM-dd";
        SimpleDateFormat tempDate = new SimpleDateFormat(pattern);
        Date reData = null;
        try {
            reData = tempDate.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("时间转化异常");
        }
        return reData;
    }

    public static String getAfterDay(int number){
        Calendar c =Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, number);
        String upDay = format(c.getTime(), "yyyy-MM-dd");
        return upDay;
    }

    public static String getAfterDay(Date date,int number){
        Calendar c =Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, number);
        String upDay = format(c.getTime(), "yyyy-MM-dd");
        return upDay;
    }

    public static Date getAfterDays(Date date,int number){
        Calendar c =Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, number);
        return c.getTime();
    }


    public static Date getAfterMonth(Date date,int number){
        Calendar c =Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH,number);
        return c.getTime();
    }


    /**
     * 获取两个时间的间隔天数
     * @param bf
     * @param af
     * @return
     */
    public static long differentDays(Date bf,Date af) {
        bf = format(format(bf,"yyyy-MM-dd"),"yyyy-MM-dd");
        af = format(format(af,"yyyy-MM-dd"),"yyyy-MM-dd");
        return (af.getTime() - bf.getTime()) / (1000 * 3600 * 24);
    }

    /**
     * 获取两个时间的间隔天数
     * @param bf
     * @param af
     * @return
     */
    public static int differentYears(Date bf,Date af) {
        Integer differentDays = new Long(differentDays(bf, af)).intValue();
        return differentDays%365==0?differentDays/365:differentDays/365+1;
    }

    /**
     * 获取时间的零时零点
     * @param date
     * @return
     */
    public static Date getZeroOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static void main(String[] args) {
        Date endDate = DateUtils.getAfterDays(DateUtils.getZeroOfDay(new Date()),1);
        Date startDate = DateUtils.getAfterDays(DateUtils.getZeroOfDay(endDate),-30);
        System.out.println(format(startDate,"yyyy-MM-dd HH:mm:ss"));
        System.out.println(format(endDate,"yyyy-MM-dd HH:mm:ss"));
    }
}
