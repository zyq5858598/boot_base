package com.ljwm.bootbase.kit;

import cn.hutool.core.bean.BeanPath;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

/**
 * JKhaled created by yunqisong@foxmail.com 2018-3-19
 * FOR : 常用工具类
 */
@SuppressWarnings({"unchecked"})
@Slf4j
public class UtilKit {

  /**
   * 创建Salt
   *
   * @return
   */
  public static String salt() {
    return RandomUtil.randomInt(1000, 9999) + "";
  }

  /**
   * 创建单号
   *
   * @param pre
   * @return
   */
  public static String createNum(String pre) {
    Date now = DateUtil.date();
    return pre + DateUtil.format(now, "yyyyMMddHHmmssSSS") + +RandomUtil.randomLong(10000, 99999);
  }

  /**
   * 表达式取值
   *
   * @param expression
   * @param source
   * @param <T>
   * @return
   */
  public static <T> T getTargetByExpression(String expression, Object source) {

    return (T) BeanPath.create(expression).get(source);
  }

  /**
   * Json字符串中表达式取值
   *
   * @param expression
   * @param jsonStr
   * @param <T>
   * @return
   */
  public static <T> T getTargetByExpression(String expression, String jsonStr) {

    return (T) BeanPath.create(expression).get(JSONObject.parseObject(jsonStr));
  }


  /**
   * 打印数组
   *
   * @param ts
   * @param <T>
   */
  public static <T> void printArray(T[] ts) {
    if (ts != null && ts.length > 0) {
      for (int i = 0; i < ts.length; i++) {
        log.info("target[" + i + "]: {}", ts[i]);
      }
    }
  }

  /**
   * 构建值数组
   *
   * @param values
   * @return
   */
  public static BigDecimal[] createdByString(String values) {
    if (StrUtil.isNotBlank(values))
      return Arrays.stream(values.split(",")).map(BigDecimal::new).toArray(BigDecimal[]::new);
    return null;
  }

  /**
   * 获取当前IP
   *
   * @return
   */
  public static String currentIp() {
    try {
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
      return HttpUtil.getClientIP(request);
    } catch (Exception e) {
      return "127.0.0.1";
    }
  }
  public static Date getDate(Integer month){
    return getDate(month,null);
  }
  public static Date getDate(Integer month,Date date){
    return getDate(month,date, Calendar.MONTH);
  }
  public static Date getDate(Integer month,Date date,int type){
    Calendar calendar = Calendar.getInstance();
    if(date!=null) {
      calendar.setTime(date);
    }
    calendar.add(type, month);    //得到前一个月
    date = calendar.getTime();
    return date;
  }
}
