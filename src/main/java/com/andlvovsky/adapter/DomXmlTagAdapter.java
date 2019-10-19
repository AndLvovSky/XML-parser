package com.andlvovsky.adapter;

import com.andlvovsky.parser.XmlAttribute;
import com.andlvovsky.parser.XmlTag;
import org.w3c.dom.*;

public class DomXmlTagAdapter {

    public static XmlTag getXmlTag(Document document) {
        Element rootElement = document.getDocumentElement();
        XmlTag tag = new XmlTag(rootElement.getTagName());
        buildXmlTag(rootElement, tag);
        return tag;
    }

    private static void buildXmlTag(Element element, XmlTag tag) {
        processAttributes(element.getAttributes(), tag);
        processInnerElements(element, tag);
    }

    private static void processAttributes(NamedNodeMap attrs, XmlTag tag) {
        for (int i = 0; i < attrs.getLength(); i++) {
            Attr attr = (Attr)attrs.item(i);
            tag.addAttribute(new XmlAttribute(attr.getName(), attr.getValue()));
        }
    }

    private static void processInnerElements(Element element, XmlTag tag) {
        if (!element.hasChildNodes()) {
            tag.isSingleton = true;
            return;
        }
        NodeList nodes = element.getChildNodes();
        if (isText(nodes)) {
            tag.setText(nodes.item(0).getTextContent());
            return;
        }
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            if (!(node instanceof  Element)) continue;
            Element innerElement = (Element)node;
            XmlTag innerTag = new XmlTag(innerElement.getTagName());
            tag.addTag(innerTag);
            buildXmlTag(innerElement, innerTag);
        }
    }

    private static boolean isText(NodeList nodes) {
        return nodes.getLength() == 1 && nodes.item(0).getTextContent() != null;
    }

}
