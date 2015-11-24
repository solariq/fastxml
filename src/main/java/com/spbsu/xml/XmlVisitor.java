package com.spbsu.xml;

/**
 * User: Igor Kuralenok
 * Date: 10.05.2006
 */
public abstract class XmlVisitor {
  public boolean visitFile(XmlFile xmlFile) { return true; }

  public boolean visitInstruction(XmlInstruction xmlInstruction) { return true; }

  public boolean visitTag(XmlTag xmlTag) { return true; }

  public boolean visitAttribute(CharSequence name, CharSequence value, XmlTag tag) { return true; }

  public boolean visitText(XmlText text) { return true; }
}
