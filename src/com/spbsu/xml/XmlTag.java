package com.spbsu.xml;

import org.jetbrains.annotations.Nullable;

/**
 * User: Igor Kuralenok
 * Date: 10.05.2006
 */
public interface XmlTag extends XmlTagChild {
  String ALL_NS = "http://www.mobile-news.org/namespaces/all";

  CharSequence getName();
  CharSequence getNamespace();

  CharSequence getNamespaceByPrefix(CharSequence prefix);

  CharSequence getAttribute(CharSequence name);
  String getAttributeValue(CharSequence name);

  XmlTagChild getFirstChild();
  XmlTag getChild(CharSequence name);

  XmlTag setAttribute(CharSequence name, CharSequence value);
  XmlTag addContent(CharSequence childText);
  XmlTag addContent(XmlTagChild childText);
  XmlTag deleteChild(XmlTagChild child);

  void processTagsWithName(XmlVisitor visitor, @Nullable CharSequence name, @Nullable CharSequence namespace);
  void processTexts(XmlVisitor visitor);
  void processAttributes(XmlVisitor visitor);

  XmlTagChild getLastChild();

  CharSequence getChildText();
  String getTextTrim();
}
