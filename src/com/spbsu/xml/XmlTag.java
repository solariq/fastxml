package com.spbsu.xml;

import com.sun.istack.internal.Nullable;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 10.05.2006
 * Time: 17:53:18
 * To change this template use File | Settings | File Templates.
 */
public interface XmlTag extends XmlTagChild {
  String ALL_NS = "http://www.mobile-news.org/namespaces/all";

  CharSequence getName();
  CharSequence getNamespace();

  CharSequence getNamespaceByPrefix(CharSequence prefix);

  CharSequence getAttribute(CharSequence name);

  XmlTagChild getFirstChild();

  void setAttribute(CharSequence name, CharSequence value);
  XmlTag addChild(CharSequence childText);
  XmlTag deleteChild(XmlTagChild child);

  void processTagsWithName(XmlVisitor visitor, @Nullable CharSequence name, @Nullable CharSequence namespace);
  void processTexts(XmlVisitor visitor);
  void processAttributes(XmlVisitor visitor);

  XmlTagChild getLastChild();

  CharSequence getChildText();
}
