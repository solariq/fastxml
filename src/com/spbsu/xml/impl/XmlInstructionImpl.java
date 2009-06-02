package com.spbsu.xml.impl;

import com.spbsu.xml.*;
import com.spbsu.xml.impl.lexer.XmlLexer;
import com.spbsu.xml.impl.lexer.XmlTokenType;
import com.spbsu.xml.impl.lexer.XmlFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 10.05.2006
 * Time: 21:41:10
 * To change this template use File | Settings | File Templates.
 */
public class XmlInstructionImpl extends XmlTagImpl implements XmlInstruction {
  public XmlInstructionImpl(XmlElement parent, int startOffset) {
    super(parent, startOffset);
  }

  public boolean accept(XmlVisitor visitor) {
    return visitor.visitInstruction(this);
  }

  public XmlTag addChild(CharSequence childText) {
    throw new RuntimeException("Can't add child to instruction");
  }

  public XmlTagChild getLastChild() {
    return null;
  }

  protected void parseTopLevelElementsInner() {
    final XmlLexer lexer = getDocument().getLexer();
    lexer.reset(getStartOffset(), XmlFlexLexer.YYINITIAL);
    assertTrue(lexer, XmlTokenType.PI_START);

    int tokenType = lexer.getTokenType();
    while(tokenType != XmlTokenType.NAME && tokenType >= 0) tokenType = lexer.advance();
    assertTrue(lexer, XmlTokenType.NAME);
    name = lexer.getTokenText();
    lexer.advance();
    attributes = parseAttributes(lexer, this);
    tokenType = lexer.getTokenType();
    //noinspection StatementWithEmptyBody
    while(tokenType >= 0 && tokenType != XmlTokenType.PI_END) tokenType = lexer.advance();
    assertTrue(lexer, XmlTokenType.PI_END);
    endOffset = lexer.getTokenEnd();
  }
}
