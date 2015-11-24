package com.spbsu.xml.tests.lexing;

import com.spbsu.xml.impl.lexer.XmlTokenType;

import java.lang.reflect.Field;

//import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 10.05.2006
 * Time: 20:03:57
 * To change this template use File | Settings | File Templates.
 */
public class DebugUtil {
  public static String getNameForTokenIndex(int i) {
    if(i < 0) return "EOF";
    final Class tokenTypes = XmlTokenType.class;
    final Field[] fields = tokenTypes.getFields();
    for (int j = 0; j < fields.length; j++) {
      final Field field = fields[j];
      try {
        if(field.getInt(null) == i) return field.getName();
      }
      catch (IllegalAccessException ignore) {}
    }
    return null;
  }
}
