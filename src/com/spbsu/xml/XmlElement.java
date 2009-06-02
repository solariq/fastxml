package com.spbsu.xml;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 10.05.2006
 * Time: 18:10:59
 * To change this template use File | Settings | File Templates.
 */
public interface XmlElement {
  XmlElement getParent();

  boolean accept(XmlVisitor visitor);

  XmlDocument getDocument();

  int getStartOffset();
  int getEndOffset();

  CharSequence getText();
}
