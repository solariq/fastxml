package com.spbsu.xml;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 10.05.2006
 * Time: 17:52:41
 * To change this template use File | Settings | File Templates.
 */
public interface XmlFile extends XmlElement{
  XmlInstruction getXmlDefinition();
  XmlTag getRootTag();
}
