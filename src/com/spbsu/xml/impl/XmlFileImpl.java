package com.spbsu.xml.impl;

import com.spbsu.xml.XmlDocument;
import com.spbsu.xml.*;
import com.spbsu.xml.impl.lexer.XmlLexer;
import com.spbsu.xml.impl.lexer.XmlTokenType;
import com.spbsu.xml.impl.lexer.XmlFlexLexer;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 10.05.2006
 * Time: 20:47:41
 * To change this template use File | Settings | File Templates.
 */
public class XmlFileImpl extends XmlElementBase implements XmlFile{
  public static final int[] INSIDE_MASK = XmlTokenType.createMask(XmlTokenType.PI_START, XmlTokenType.TAG_START);

  private final XmlDocument text;
  private XmlInstruction xmlDefinition = null;
  private XmlTag rootTag = null;

  public XmlFileImpl(XmlDocument text) {
    super(null, 0);
    this.text = text;
  }

  public XmlInstruction getXmlDefinition() {
    parseTopLevelElements();
    return xmlDefinition;
  }

  public XmlTag getRootTag() {
    parseTopLevelElements();
    return rootTag;
  }

  public XmlDocument getDocument() {
    return text;
  }

  public XmlElement getParent() {
    return null;
  }

  public int getEndOffset() {
    return text.length();
  }

  public boolean accept(XmlVisitor visitor) {
    return visitor.visitFile(this);
  }

  protected void parseTopLevelElementsInner() {
    final XmlLexer lexer = getDocument().getLexer();
    lexer.reset(0, XmlFlexLexer.YYINITIAL);
    int tokenType;
    do {
      tokenType = XmlTokenType.skipTokensByMask(lexer, INSIDE_MASK);
      if(tokenType == XmlTokenType.TAG_START){
        rootTag = new XmlTagImpl(this, lexer.getTokenStart());
        break;
      }
      else if(tokenType == XmlTokenType.PI_START) {
        xmlDefinition = new XmlInstructionImpl(this, lexer.getTokenStart());
      }
      tokenType = lexer.advance();
    }
    while (tokenType >= 0);
    endOffset = text.length();
  }
}
