package com.spbsu.xml;

/**
 * User: Igor Kuralenok
 * Date: 10.05.2006
 */
public interface XmlFile extends XmlElement{
  XmlInstruction getXmlDefinition();
  XmlTag getRootTag();
}
