package com.ljwm.bootbase.config;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;

/**
 * JKhaled created by yunqisong@foxmail.com 2018-3-18
 * FOR : FastJsonMoney自定义转化
 */
public class MoneySerializer implements ObjectSerializer {

  @Override
  public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
    SerializeWriter out = serializer.getWriter();
    if (object == null) {
      serializer.getWriter().writeNull();
      return;
    }
    if (object instanceof BigDecimal) {
      out.write(String.format("%.2f", ((Number) object).doubleValue()));
    }
  }
}
