package com.spbsu.xml;

import com.spbsu.xml.impl.lexer.XmlLexer;
import com.spbsu.commons.text.CharArrayCharSequence;
import com.spbsu.commons.text.CharSequenceBase;
import com.spbsu.commons.text.CompositeCharSequence;


public class XmlDocument{
  public static final int MAX_FRAGMENTS_COUNT = 200;

  private XmlLexer lexer = new XmlLexer(this);
  int operationsCount = 0;
  private CharSequence text;
  public static long totalCount = 0;

  public XmlDocument(CharSequence text) {
    this.text = CharSequenceBase.createArrayBasedSequence(text);
  }

  public XmlLexer getLexer() {
    return lexer;
  }

  public void insert(int offset, CharSequence text) {
    this.text = new CompositeCharSequence(new CharSequence[]{
        this.text.subSequence(0, offset),
        CharSequenceBase.createArrayBasedSequence(text),
        this.text.subSequence(offset, length())});
    compact();
  }

  public void replace(int start, int end, CharSequence text) {
    this.text = new CompositeCharSequence(new CharSequence[]{
        this.text.subSequence(0, start),
        CharSequenceBase.createArrayBasedSequence(text),
        this.text.subSequence(end, length())});
    compact();
  }

  public void delete(int start, int end) {
    this.text = new CompositeCharSequence(new CharSequence[]{text.subSequence(0, start), text.subSequence(end, length())});
    compact();
  }

  private void compact() {
    if(text instanceof CompositeCharSequence && ((CompositeCharSequence) text).getFragmentsCount() > MAX_FRAGMENTS_COUNT){
      final char[] array = ((CompositeCharSequence) text).toCharArray();
      text = new CharArrayCharSequence(array, 0, array.length);
      operationsCount = 0;
    }
  }

  public CharSequence subSequence(int start) {
    return text.subSequence(start, length());
  }

  public CharSequence subSequence(int start, int end) {
    return text.subSequence(start, end);
  }

  public int length() {
    return text.length();
  }

  public char charAt(int offset) {
    return text.charAt(offset);
  }

  public String toString() {
    return text.toString();
  }

  public CharSequence getText() {
    return text;
  }
}
