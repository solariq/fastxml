package com.spbsu.xml.impl;

import com.spbsu.xml.XmlElement;
import com.spbsu.xml.XmlTag;
import com.spbsu.xml.XmlTagChild;
import com.spbsu.xml.impl.lexer.XmlLexer;
import com.spbsu.xml.impl.lexer.XmlTokenType;
import com.spbsu.xml.impl.lexer.XmlFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 11.05.2006
 * Time: 16:54:41
 * To change this template use File | Settings | File Templates.
 */
public abstract class XmlTagChildBase extends XmlElementBase implements XmlTagChild {
  public XmlTagChildBase(XmlElement parent,  int startOffset) {
    super(parent, startOffset);
  }

  public XmlTagChild next() {
    if(!(getParent() instanceof XmlTag)) return null;
    final XmlTagImpl parent = (XmlTagImpl) getParent();
    final XmlLexer lexer = parent.getDocument().getLexer();
    lexer.reset(getEndOffset(), XmlFlexLexer.YYINITIAL);
    int tokenType = lexer.getTokenType();
    while(tokenType == XmlTokenType.COMMENT) tokenType = lexer.advance();
    parent.setLastKnownChildStart(lexer.getTokenStart());
    return XmlTagImpl.createChild((XmlTagImpl) getParent(), lexer);
  }
}
