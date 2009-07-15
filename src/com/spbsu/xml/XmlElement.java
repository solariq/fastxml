package com.spbsu.xml;

/**
 * User: Igor Kuralenok
 * Date: 10.05.2006
 */
public interface XmlElement {
  XmlElement getParent();

  boolean accept(XmlVisitor visitor);

  XmlDocument getDocument();

  int getStartOffset();
  int getEndOffset();

  CharSequence getText();
}
