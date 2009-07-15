package com.spbsu.xml;

/**
 * User: Igor Kuralenok
 * Date: 11.05.2006
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
