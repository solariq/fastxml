package com.spbsu.xml.impl;

import com.spbsu.xml.XmlElement;
import com.spbsu.xml.XmlTag;
import com.spbsu.xml.XmlTagChild;
import com.spbsu.xml.impl.lexer.XmlLexer;
import com.spbsu.xml.impl.lexer.XmlTokenType;
import com.spbsu.xml.impl.lexer.XmlFlexLexer;

/**
 * User: Igor Kuralenok
 * Date: 11.05.2006
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
    assertFalse(lexer, -1);
    parent.setLastKnownChildStart(lexer.getTokenStart());
    return XmlTagImpl.createChild((XmlTagImpl) getParent(), lexer);
  }
}
