package com.spbsu.xml.impl;

import com.spbsu.commons.seq.CharSeqComposite;
import com.spbsu.xml.XmlText;
import com.spbsu.xml.XmlVisitor;
import com.spbsu.xml.impl.lexer.XmlFlexLexer;
import com.spbsu.xml.impl.lexer.XmlLexer;
import com.spbsu.xml.impl.lexer.XmlTokenType;

import java.util.LinkedList;
import java.util.List;

/**
 * User: Igor Kuralenok
 * Date: 11.05.2006
 */
public class XmlTextImpl extends XmlTagChildBase implements XmlText {
  private static final int[] CONTENTS_FILTER = XmlTokenType.createMask(XmlTokenType.WS, XmlTokenType.DATA);
  private CharSequence value;
  public XmlTextImpl(XmlTagImpl parent, int startOffset) {
    super(parent, startOffset);
  }

  public boolean accept(XmlVisitor visitor) {
    return visitor.visitText(this);
  }

  public CharSequence getValue() {
    parseTopLevelElements();
    return value;
  }

  protected void parseTopLevelElementsInner() {
    final XmlLexer lexer = getDocument().getLexer();
    lexer.reset(getStartOffset(), XmlFlexLexer.YYINITIAL);
    final List<CharSequence> buffer = new LinkedList<CharSequence>();
    int currentType = lexer.getTokenType();
    while(currentType >= 0) {
      if(CONTENTS_FILTER[currentType] != 0){
        buffer.add(lexer.getTokenText());
      }
      else if(currentType == XmlTokenType.ENTITY_REF || currentType == XmlTokenType.CHAR_ENTITY) {
        buffer.add(XmlTokenType.decodeEntity(lexer.getTokenText()));
      }
      else if(currentType != XmlTokenType.CDATA_START &&
              currentType != XmlTokenType.CDATA_END &&
              currentType != XmlTokenType.BAD_CHAR
          )
        break; // skip these symbols
      currentType = lexer.advance();
    }
    endOffset = lexer.getTokenStart();
    if(buffer.size() > 1) value = new CharSeqComposite(buffer.toArray(new CharSequence[buffer.size()]));
    else if(buffer.size() == 1) value = buffer.get(0);
    else value = "";
//    if(value.length() == 0)
//      assert false;
  }
}
