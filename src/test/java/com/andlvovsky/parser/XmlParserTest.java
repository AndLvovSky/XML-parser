package com.andlvovsky.parser;

import com.andlvovsky.util.ResourceHelper;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import static org.junit.Assert.assertEquals;

public class XmlParserTest {

    @Test
    public void parseDevice() {
        Document document = XmlParser.parseDom(ResourceHelper.getFilename("xml/valid_device.xml"));
        Element rootElement = document.getDocumentElement();
        assertEquals("true",  rootElement.getAttributes().getNamedItem("critical").getNodeValue());
        assertEquals("USA", rootElement.getElementsByTagName("origin").item(0).getTextContent());
        assertEquals("50", rootElement.getElementsByTagName("energyConsumption").item(0).getTextContent());
    }

}