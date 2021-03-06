package com.spbsu.xml;

import com.spbsu.xml.impl.XmlFileImpl;

/**
 * User: Igor Kuralenok
 * Date: 10.05.2006
 */
public class XmlFactory {
  public static XmlFile parseText(CharSequence text) {
    return new XmlFileImpl(new XmlDocument(text));
  }

  public static XmlTag createTag(final String name) {
    return parseText("<" + name + "/>").getRootTag();
  }

  //comm
  public static XmlTag createTag(String name, CharSequence text) {
    return parseText("<" + name + ">" + escape(text) + "</" + name + ">").getRootTag();
  }

  public static CharSequence escape(CharSequence text) {
    if(text == null) return null;
    StringBuilder buffer = null;
    int index = 0;
    int length = text.length();
    int lengthInBuffer = 0;
    while (index < length) {
      char escaped = text.charAt(index);
      if (needToBeEscaped(escaped)) {
        if (buffer == null) {
          buffer = new StringBuilder();
        }
        buffer.append(text.subSequence(lengthInBuffer, index));
        lengthInBuffer = index + 1;
        buffer.append(escape(escaped));
      }
      index++;
    }
    if (buffer != null && lengthInBuffer < length)
      buffer.append(text.subSequence(lengthInBuffer, index));
    return buffer != null ? buffer : text;
  }

  private static boolean needToBeEscaped(char escaped) {
    return escaped == '<' || escaped == '&' || escaped == '"';
  }

  private static CharSequence escape(char escaped) {
    if (escaped == '<') return "&lt;";
    if (escaped == '&') return "&amp;";
    if (escaped == '"') return "&quot;";
    return "" + escaped; // must not be returned ever
  }
}
