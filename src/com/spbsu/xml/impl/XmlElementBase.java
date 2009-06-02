package com.spbsu.xml.impl;

import com.spbsu.xml.XmlDocument;
import com.spbsu.xml.XmlElement;
import com.spbsu.xml.impl.lexer.XmlLexer;
import com.spbsu.xml.impl.lexer.XmlTokenType;

/**
 * Created by IntelliJ IDEA.
 * User: Igor Kuralenok
 * Date: 10.05.2006
 * Time: 22:07:08
 * To change this template use File | Settings | File Templates.
 */
public abstract class XmlElementBase implements XmlElement{
  private final XmlElement parent;
  private XmlDocument document = null;
  private final int startOffset;
  protected int endOffset = -1;

  public XmlElementBase(XmlElement parent, int startOffset) {
    this.startOffset = startOffset;
    this.parent = parent;
  }

  public XmlElement getParent() {
    return parent;
  }

  public XmlDocument getDocument() {
    if(document != null) return document;
    return document = getParent().getDocument();
  }

  protected void dropParsedFlag(){
    endOffset = -1;
  }

  protected final void parseTopLevelElements(){
    if(endOffset < 0) {
      parseTopLevelElementsInner();
    }
  }

  protected abstract void parseTopLevelElementsInner();

  protected void assertTrue(XmlLexer lexer, int tokenType) {
    if (lexer.getTokenType() != tokenType) {
      throw new RuntimeException("Parsing exception at offset " + lexer.getTokenStart() +
                                 "\nUnexpected token: " + XmlTokenType.getTokenName(lexer.getTokenType()) +
                                 " expected " + XmlTokenType.getTokenName(tokenType) + "");
    }
  }

  public int getStartOffset() {
    return startOffset;
  }

  public int getEndOffset(){
    parseTopLevelElements();
    return endOffset;
  }

  public CharSequence getText() {
    return getDocument().getText().subSequence(getStartOffset(), getEndOffset());
  }
}
