package com.spbsu.xml;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 11.05.2006
 * Time: 20:26:33
 * To change this template use File | Settings | File Templates.
 */
public class RecursiveXmlVisitor extends XmlVisitor {
  public boolean visitFile(XmlFile xmlFile) {
    final XmlInstruction xmlDefinition = xmlFile.getXmlDefinition();
    if(xmlDefinition != null) xmlDefinition.accept(this);
    final XmlTag rootTag = xmlFile.getRootTag();
    if(rootTag != null) rootTag.accept(this);
    return true;
  }

  public boolean visitTag(XmlTag xmlTag) {
    XmlTagChild child = xmlTag.getFirstChild();
    while(child != null) {
      if(!child.accept(this)) return false;
      child = child.next();
    }
    return true;
  }
}
