package com.spbsu.xml.impl.lexer;


import com.spbsu.commons.text.CharArrayCharSequence;

import java.util.Vector;

public class XmlTokenType {
  public static final int TAG_START = 0;
//  nextToken("TAG_START");
  public static final int EMPTY_TAG_END = 1;
//  nextToken("EMPTY_TAG_END");
  public static final int END_TAG_START = 2;
//  nextToken("END_TAG_START");
  public static final int TAG_END = 3;
//  nextToken("TAG_END");

  public static final int ATTR_VAL = 4;
//  nextToken("ATTR_VAL");
  public static final int EQ = 5;
//  nextToken("EQ");
  public static final int ATTR_VAL_START = 6;
//  nextToken("ATTR_VAL_START");
  public static final int ATTR_VAL_END = 7;
//  nextToken("ATTR_VAL_END");

  public static final int PI_START = 8;
//  nextToken("PI_START");
  public static final int PI_END = 9;
//  nextToken("PI_END");

  public static final int ENTITY_REF = 10;
//  nextToken("ENTITY_REF");
  public static final int CHAR_ENTITY = 11;
//  nextToken("CHAR_ENTITY");

  public static final int CDATA_START = 12;
//  nextToken("CDATA_START");
  public static final int CDATA_END = 13;
//  nextToken("CDATA_END");

  public static final int DOCTYPE_START = 14;
//  nextToken("DOCTYPE_START");
  public static final int DOCTYPE_END = 15;
//  nextToken("DOCTYPE_END");
  public static final int MARKUP = 16;
//  nextToken("MARKUP");

  public static final int BAD_CHAR = 17;
//  nextToken("BAD_CHAR");
  public static final int WS = 18;
//  nextToken("WS");

  public static final int NAME = 19;
//  nextToken("NAME");
  public static final int DATA = 20;
//  nextToken("DATA");
  public static final int COMMENT = 21;
//  nextToken("COMMENT");

  public static final int LAST_TOKEN = 22;
//  nextToken("LAST");

  private static Vector tokenNames;

  static{
    nextToken("TAG_START");
    nextToken("EMPTY_TAG_END");
    nextToken("END_TAG_START");
    nextToken("TAG_END");
    nextToken("ATTR_VAL");
    nextToken("EQ");
    nextToken("ATTR_VAL_START");
    nextToken("ATTR_VAL_END");
    nextToken("PI_START");
    nextToken("PI_END");
    nextToken("ENTITY_REF");
    nextToken("CHAR_ENTITY");
    nextToken("CDATA_START");
    nextToken("CDATA_END");
    nextToken("DOCTYPE_START");
    nextToken("DOCTYPE_END");
    nextToken("MARKUP");
    nextToken("BAD_CHAR");
    nextToken("WS");
    nextToken("NAME");
    nextToken("DATA");
    nextToken("COMMENT");
    nextToken("LAST");
  }

  private static int nextToken(String name) {
    if(tokenNames == null) tokenNames = new Vector();
    tokenNames.addElement(name);
    return tokenNames.size() - 1;
  }

  public static int[] createMask(int token1) {
    final int[] result = new int[LAST_TOKEN];
    result[token1] = 1;
    return result;
  }

  public static int[] createMask(int token1, int token2) {
    final int[] result = new int[LAST_TOKEN];
    result[token1] = 1;
    result[token2] = 1;
    return result;
  }

  public static int[] createMask(int token1, int token2, int token3) {
    final int[] result = new int[LAST_TOKEN];
    result[token1] = 1;
    result[token2] = 1;
    result[token3] = 1;
    return result;
  }

  public static int[] createMask(int[] tokens) {
    final int[] result = new int[LAST_TOKEN];
    for (int i = 0; i < tokens.length; i++) {
      result[tokens[i]] = 1;
    }
    return result;
  }

  public static int skipTokensByMask(XmlLexer lexer, int[] mask) {
    //noinspection StatementWithEmptyBody
    int tokenType = lexer.getTokenType();
    while(tokenType >= 0 && mask[tokenType] == 0) tokenType = lexer.advance();
    return tokenType;
  }

  public static CharSequence decodeEntity(CharSequence tokenText) {
    if(tokenText.equals("&amp;")) return "&";
    if(tokenText.equals("&lt;")) return "<";
    if(tokenText.equals("&gt;")) return ">";
    if(tokenText.equals("&nbsp;")) return " ";
    if(tokenText.equals("&quot;")) return "\"";
    if(tokenText.subSequence(0,2).equals("&#")) {
      final CharSequence code = tokenText.subSequence(2, tokenText.length() - 1);
      if(Character.toLowerCase(code.charAt(0)) == 'x') { // hex
        return new CharArrayCharSequence(new char[]{
                (char)Integer.parseInt(code.subSequence(1, code.length()).toString(), 16)
        }, 0, 1);
      }
      else { // dec
        return new CharArrayCharSequence(new char[]{(char)Integer.parseInt(code.toString())}, 0, 1);
      }
    }
    return tokenText;
  }

  public static String getTokenName(int tokenType) {
    if (tokenType < 0 || tokenType >= tokenNames.size())
      return "<invalid token>";
    return (String) tokenNames.elementAt(tokenType);
  }
}
