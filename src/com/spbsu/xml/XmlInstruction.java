package com.spbsu.xml;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 10.05.2006
 * Time: 17:53:36
 * To change this template use File | Settings | File Templates.
 */
public interface XmlInstruction extends XmlTagChild{
  CharSequence getName();
  CharSequence getAttribute(CharSequence name);

  void setAttribute(CharSequence name, CharSequence value);
}
