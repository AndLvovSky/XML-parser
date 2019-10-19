package com.andlvovsky.validator;

import com.andlvovsky.parser.XmlAttribute;
import com.andlvovsky.parser.XmlParser;
import com.andlvovsky.parser.XmlTag;
import com.andlvovsky.reader.ResourceReader;
import org.junit.Test;

public class XmlXsdValidatorTest {

    private String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    @Test
    public void primitiveStringTag() {
        XmlTag xsd = new XmlTag("xs:schema");
        XmlTag el = new XmlTag("xs:element");
        el.addAttribute(new XmlAttribute("name", "abc"));
        el.addAttribute(new XmlAttribute("type", "xs:string"));
        xsd.addTag(el);
        XmlTag xml = new XmlTag("abc");
        xml.setText("greeting");
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test
    public void primitiveDecimalTag() {
        XmlTag xsd = new XmlTag("xs:schema");
        XmlTag el = new XmlTag("xs:element");
        el.addAttribute(new XmlAttribute("name", "abc"));
        el.addAttribute(new XmlAttribute("type", "xs:decimal"));
        xsd.addTag(el);
        XmlTag xml = new XmlTag("abc");
        xml.setText("12.25");
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void primitiveDecimalTagFail() {
        XmlTag xsd = new XmlTag("xs:schema");
        XmlTag el = new XmlTag("xs:element");
        el.addAttribute(new XmlAttribute("name", "abc"));
        el.addAttribute(new XmlAttribute("type", "xs:decimal"));
        xsd.addTag(el);
        XmlTag xml = new XmlTag("abc");
        xml.setText("t12&2523");
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test
    public void simpleElementWithAttribute() {
        XmlTag xsd = new XmlTag("xs:schema");
        XmlTag el = new XmlTag("xs:element").addAttribute(new XmlAttribute("name", "abc"))
            .addAttribute(new XmlAttribute("type", "xs:string"));
        xsd.addTag(el);
        XmlTag attr = new XmlTag("xs:attribute").addAttribute(new XmlAttribute("name", "name"))
                .addAttribute(new XmlAttribute("type", "xs:string"));
        el.addTag(new XmlTag("xs:simpleType").addTag(attr));
        XmlTag xml = new XmlTag("abc").setText("hi").addAttribute(new XmlAttribute("name", "John"));
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test
    public void simpleElementWithDecimalMinExclusiveRestriction() {
        XmlTag xsd = new XmlTag("xs:schema");
        XmlTag el = new XmlTag("xs:element");
        el.addAttribute(new XmlAttribute("name", "abc"));
        xsd.addTag(el);
        XmlTag restriction = new XmlTag("xs:minExclusive");
        restriction.addAttribute(new XmlAttribute("value", "0.7"));
        el.addTag(new XmlTag("xs:simpleType").addTag(new XmlTag("xs:restriction")
                .addAttribute(new XmlAttribute("base", "xs:decimal")).addTag(restriction)));
        XmlTag xml = new XmlTag("abc");
        xml.setText("0.9");
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void simpleElementWithDecimalMinExclusiveRestrictionFail() {
        XmlTag xsd = new XmlTag("xs:schema");
        XmlTag el = new XmlTag("xs:element");
        el.addAttribute(new XmlAttribute("name", "abc"));
        xsd.addTag(el);
        XmlTag restriction = new XmlTag("xs:minExclusive");
        restriction.addAttribute(new XmlAttribute("value", "0.7"));
        el.addTag(new XmlTag("xs:simpleType").addTag(new XmlTag("xs:restriction")
                .addAttribute(new XmlAttribute("base", "xs:decimal")).addTag(restriction)));
        XmlTag xml = new XmlTag("abc");
        xml.setText("0.4");
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test
    public void simpleElementWithEnum() {
        XmlTag xsd = new XmlTag("xs:schema");
        XmlTag el = new XmlTag("xs:element");
        el.addAttribute(new XmlAttribute("name", "abc"));
        xsd.addTag(el);
        el.addTag(new XmlTag("xs:simpleType").addTag(new XmlTag("xs:restriction")
                .addAttribute(new XmlAttribute("base", "xs:string"))
                .addTag(new XmlTag("xs:enumeration").addAttribute(new XmlAttribute("value", "YES")))
                .addTag(new XmlTag("xs:enumeration").addAttribute(new XmlAttribute("value", "NO")))));
        XmlTag xml = new XmlTag("abc");
        xml.setText("YES");
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void simpleElementWithEnumFails() {
        XmlTag xsd = new XmlTag("xs:schema");
        XmlTag el = new XmlTag("xs:element");
        el.addAttribute(new XmlAttribute("name", "abc"));
        xsd.addTag(el);
        el.addTag(new XmlTag("xs:simpleType").addTag(new XmlTag("xs:restriction")
                .addAttribute(new XmlAttribute("base", "xs:string"))
                .addTag(new XmlTag("xs:enumeration").addAttribute(new XmlAttribute("value", "YES")))
                .addTag(new XmlTag("xs:enumeration").addAttribute(new XmlAttribute("value", "NO")))));
        XmlTag xml = new XmlTag("abc");
        xml.setText("MAYBE");
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test
    public void complexElementAll() {
        XmlTag xsd = new XmlTag("xs:schema");
        XmlTag el = new XmlTag("xs:element");
        XmlTag all = new XmlTag("xs:all");
        all.addTag(new XmlTag("xs:element")
                .addAttribute(new XmlAttribute("name", "abc"))
                .addAttribute(new XmlAttribute("type", "xs:string"))).addTag(new XmlTag("xs:element")
                .addAttribute(new XmlAttribute("name", "def"))
                .addAttribute(new XmlAttribute("type", "xs:string")));
        xsd.addTag(el.addAttribute(new XmlAttribute("name", "xyz"))
                .addTag(new XmlTag("xs:complexType").addTag(all)));
        XmlTag xml = new XmlTag("xyz").addTag(new XmlTag("def").setText("hey"))
                .addTag(new XmlTag("abc").setText("you"));
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test
    public void complexElementSequence() {
        XmlTag xsd = new XmlTag("xs:schema");
        XmlTag el = new XmlTag("xs:element");
        XmlTag all = new XmlTag("xs:sequence");
        all.addTag(new XmlTag("xs:element")
                .addAttribute(new XmlAttribute("name", "abc"))
                .addAttribute(new XmlAttribute("type", "xs:string"))).addTag(new XmlTag("xs:element")
                .addAttribute(new XmlAttribute("name", "def"))
                .addAttribute(new XmlAttribute("type", "xs:string")));
        xsd.addTag(el.addAttribute(new XmlAttribute("name", "xyz"))
                .addTag(new XmlTag("xs:complexType").addTag(all)));
        XmlTag xml = new XmlTag("xyz").addTag(new XmlTag("abc").setText("hey"))
                .addTag(new XmlTag("def").setText("you"));
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test
    public void elementWithTemplate() {
        XmlTag xsd = new XmlTag("xs:schema");
        XmlTag type = new XmlTag("xs:complexType");
        XmlTag all = new XmlTag("xs:all");
        all.addTag(new XmlTag("xs:element")
                .addAttribute(new XmlAttribute("name", "abc"))
                .addAttribute(new XmlAttribute("type", "xs:string"))).addTag(new XmlTag("xs:element")
                .addAttribute(new XmlAttribute("name", "def"))
                .addAttribute(new XmlAttribute("type", "xs:string")));
        type.addTag(all).addAttribute(new XmlAttribute("name", "typeTemp"));
        XmlTag el = new XmlTag("xs:element").addAttribute(new XmlAttribute("name", "xyz"))
                .addAttribute(new XmlAttribute("type", "typeTemp"));
        xsd.addTag(type).addTag(el);
        XmlTag xml = new XmlTag("xyz").addTag(new XmlTag("def").setText("hey"))
                .addTag(new XmlTag("abc").setText("you"));
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test
    public void validateDevice() {
        XmlTag xsd = XmlParser.parseTag("xsd/device.xsd");
        XmlTag xml = XmlParser.parseTag("xml/valid_device.xml");
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deviceValidationFails() {
        XmlTag xsd = XmlParser.parseTag("xsd/device.xsd");
        XmlTag xml = XmlParser.parseTag("xml/invalid_device.xml");
        XmlXsdValidator validator = new XmlXsdValidator(xsd);
        validator.validate(xml);
    }

}
