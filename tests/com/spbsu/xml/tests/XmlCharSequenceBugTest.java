package com.spbsu.xml.tests;

import com.spbsu.xml.XmlFactory;
import com.spbsu.xml.XmlFile;
import com.spbsu.xml.XmlTag;
import com.spbsu.util.CharSequenceBase;
import junit.framework.TestCase;

/**
 * @author vp
 */
public class XmlCharSequenceBugTest extends TestCase {
  private static final String TAG = "child";
  private static final String TEST_XML =
    "<test>" +
      "<" + TAG + "> some text </" + TAG + ">" +
    "</test>";

  public void testGetChildForString() {
    final CharSequence childName = TAG;
    getChildImpl(childName);
  }

  public void testGetChildForCharSequenceBase() {
    final CharSequence childName = CharSequenceBase.create(TAG.toCharArray());
    getChildImpl(childName);
  }

  private static void getChildImpl(final CharSequence childName) {
    final XmlFile xmlFile = XmlFactory.parseTextFile(TEST_XML);
    final XmlTag rootTag = xmlFile.getRootTag();
    final XmlTag child = rootTag.getChild(childName);
    assertNotNull(child);
  }
}
