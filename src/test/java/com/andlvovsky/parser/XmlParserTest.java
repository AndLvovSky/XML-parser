package com.andlvovsky.parser;

import org.junit.Test;

import static org.junit.Assert.*;

public class XmlParserTest {

    private String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    @Test
    public void parseEmptyElement() {
        String xml = "<abc></abc>";
        XmlTag tag = XmlParser.parseTagFromString(xmlHeader + xml);
        assertEquals("abc", tag.name);
    }

    @Test
    public void parseSimpleElementWithoutAttributes() {
        String xml = "<abc>hello</abc>";
        XmlTag tag = XmlParser.parseTagFromString(xmlHeader + xml);
        assertEquals("abc", tag.name);
        assertEquals("hello", tag.text);
    }

    @Test
    public void parseSingletonElementWithoutAttributes() {
        String xml = "<singleton/>";
        XmlTag tag = XmlParser.parseTagFromString(xmlHeader + xml);
        assertEquals("singleton", tag.name);
        assertEquals(true, tag.isSingleton);
    }

    @Test
    public void parseSimpleElement() {
        String xml = "<animal type=\"cat\" weight=\"1000\">Lord</animal>";
        XmlTag tag = XmlParser.parseTagFromString(xmlHeader + xml);
        assertEquals("animal", tag.name);
        assertEquals("Lord", tag.text);
        assertEquals("weight", tag.attrs.get(1).name);
        assertEquals("1000", tag.attrs.get(1).value);
    }

    @Test
    public void parseSmallComplexElement() {
        String xml = "<abc><def>yes</def><ghi>no</ghi></abc>";
        XmlTag tag = XmlParser.parseTagFromString(xmlHeader + xml);
        assertEquals("yes", tag.tags.get(0).text);
    }

    @Test
    public void parseBigComplexElement() {
        XmlTag tag = XmlParser.parseTag("xml/valid_device.xml");
        assertEquals("true",  tag.getAttributeValue("critical"));
        assertEquals("USA", tag.tags.get(1).text);
        assertEquals("50", tag.tags.get(3).tags.get(1).text);
    }
    
    @Test
    public void parseNestedElement() {
        String xml = "<abc><abc>yes</abc><abc>no</abc></abc>";
        XmlTag tag = XmlParser.parseTagFromString(xmlHeader + xml);
        System.out.println(tag);
        assertEquals("yes", tag.tags.get(0).text);
    }

}