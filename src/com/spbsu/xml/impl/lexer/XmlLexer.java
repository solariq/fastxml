package com.spbsu.xml.impl.lexer;

import com.spbsu.xml.XmlDocument;

public class XmlLexer {
  private final XmlFlexLexer lexer = new XmlFlexLexer();
  private final XmlDocument document;
  private int tokenStart = 0;
  private int tokenType = -2;
  private int nextTokenType = -2;
  private boolean isDocumentSet = false;

  public XmlLexer(XmlDocument xmlDocument) {
    document = xmlDocument;
  }

  public int getTokenType() {
    int tokenType = this.tokenType;
    if(tokenType < -1) return advance();
    return tokenType;
  }

  public int advance() {
    final XmlFlexLexer lexer = this.lexer;
    tokenStart = lexer.getTokenStart();
    int tokenType;
    int nextTokenType = this.nextTokenType;
    if(nextTokenType < -1) tokenType = lexer.advance();
    else tokenType = nextTokenType;
    this.tokenType = tokenType;
    if(tokenType == -1) return tokenType;
    //noinspection StatementWithEmptyBody
    while(
      (nextTokenType = lexer.advance()) == tokenType &&
        nextTokenType != XmlTokenType.ENTITY_REF &&
        nextTokenType != XmlTokenType.CHAR_ENTITY
    );
    this.nextTokenType = nextTokenType;
    return tokenType;
  }

  public int getTokenStart() {
    if(tokenType < -1) advance();
    return tokenStart;
  }

  public int getTokenEnd() {
    if(tokenType < -1) advance();
    return lexer.getTokenStart();
  }

  public CharSequence getTokenText() {
    return document.getText().subSequence(getTokenStart(), getTokenEnd());
  }

  public void reset(int startOffset, int state) {
    if(startOffset == tokenStart && isDocumentSet) return;
    tokenType = nextTokenType = -2;
    lexer.reset(document.getText(), startOffset, state);
    isDocumentSet = true;
  }
}
