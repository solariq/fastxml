package com.spbsu.xml;

/**
 * User: Igor Kuralenok
 * Date: 10.05.2006
 */
public interface XmlInstruction extends XmlTagChild{
  CharSequence getName();
  CharSequence getAttribute(CharSequence name);

  // I was too lazy to split off instruction implementation from tag
  XmlTag setAttribute(CharSequence name, CharSequence value);
}
