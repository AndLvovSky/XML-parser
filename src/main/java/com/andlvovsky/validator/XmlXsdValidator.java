package com.andlvovsky.validator;

import com.andlvovsky.parser.XmlAttribute;
import com.andlvovsky.parser.XmlTag;

import java.util.*;

public class XmlXsdValidator {

    private XmlTag xsd;

    private Map<String, XmlTag> types = new HashMap<>();

    public static final List<String> supportedPrimitives =
            Arrays.asList("xs:string", "xs:decimal", "xs:boolean", "xs:positiveInteger", "xs:ID");

    public XmlXsdValidator(XmlTag xsd) {
        this.xsd = xsd;
        findTypes();
    }

    public void validate(XmlTag xml) {
        for (XmlTag tag : xsd.tags) {
            if (tag.name.equals("xs:element")) {
                validateElement(xml, tag);
                return;
            }
        }
        throw new IllegalArgumentException("Invalid xsd");
    }

    private void findTypes() {
        for (XmlTag tag : xsd.tags) {
            if (tag.name.equals("xs:complexType") && tag.hasAttribute("name")) {
                types.put(tag.getAttributeValue("name"), tag);
            }
        }
    }

    private void validateElement(XmlTag xmlTag, XmlTag xsdTag) {
        validateName(xmlTag, xsdTag);
        if (!(xsdTag.tags.size() == 1 && xsdTag.tags.get(0).name.equals("xs:complexType"))) {
            if (!xsdTag.tags.isEmpty() && xsdTag.tags.get(0).tags.get(0).name.equals("xs:restriction")) {
                validateAttributes(xmlTag, xsdTag.tags.get(0));
                validateRestrictions(xmlTag.text, xsdTag.tags.get(0).tags.get(0));
            } else {
                validateSimpleElement(xmlTag, xsdTag);
            }
        } else {
            validateAttributes(xmlTag, xsdTag.tags.get(0));
            validateComplexElement(xmlTag, xsdTag);
        }
    }

    private void validateSimpleElement(XmlTag xmlTag, XmlTag xsdTag) {
        String type = xsdTag.getAttributeValue("type");
        if (supportedPrimitives.contains(type)) {
            validatePrimitiveElement(xmlTag, xsdTag);
        } else {
            XmlTag compElXsd = createComplexElementXsd(xsdTag.name, types.get(type));
            validateComplexElement(xmlTag, compElXsd);
        }
    }

    private void validatePrimitiveElement(XmlTag xmlTag, XmlTag xsdTag) {
        String value = xmlTag.text;
        String type = xsdTag.getAttributeValue("type");
        switch (type) {
            case "xs:decimal" : {
                Double.parseDouble(value);
                break;
            }
            case "xs:boolean" : {
                Boolean.parseBoolean(value);
                break;
            }
            case "xs:positiveInteger" : {
                int parsedValue = Integer.parseInt(value);
                if (parsedValue < 0) {
                    throw new IllegalArgumentException("Number " + xmlTag.name + " must be positive");
                }
                break;
            }
            case "xs:ID" : {
                if (!Character.isLetter(value.charAt(0))) {
                    throw new IllegalArgumentException("Id must begin with latin letter");
                }
            }
        }
    }

    private XmlTag createComplexElementXsd(String name, XmlTag type) {
        XmlTag compEl = new XmlTag(name);
        compEl.addTag(type);
        return compEl;
    }

    private void validateName(XmlTag xmlTag, XmlTag xsdTag) {
        String name = xsdTag.getAttributeValue("name");
        if (!name.equals(xmlTag.name)) {
            throw new IllegalArgumentException("Must be element " + name + " instead of " + xmlTag.name);
        }
    }

    private void validateAttributes(XmlTag xmlTag, XmlTag xsdTag) {
        for (XmlTag tag : xsdTag.tags) {
            if (tag.name.equals("xs:attribute")) {
                String attrName = tag.getAttributeValue("name");
                if (!xmlTag.hasAttribute(attrName)) {
                    throw new IllegalArgumentException("No attribute " + attrName);
                }
                validatePrimitiveElement(createPrimitiveElement(xmlTag.getAttribute(attrName)), tag);
            }
        }
    }

    private XmlTag createPrimitiveElement(XmlAttribute attr) {
        XmlTag simpEl = new XmlTag(attr.name);
        simpEl.setText(attr.value);
        return simpEl;
    }

    private void validateRestrictions(String value, XmlTag restrictions) {
        String base = restrictions.getAttributeValue("base");
        if (base == null) {
            throw new IllegalArgumentException("No base for restriction");
        }
        if (base.equals("xs:string") && restrictions.tags.get(0).name.equals("xs:enumeration")) {
            validateEnumeration(value, restrictions);
        } else {
            for (XmlTag restriction : restrictions.tags) {
                validateRestriction(value, base, restriction);
            }
        }
    }

    private void validateRestriction(String value, String base, XmlTag restriction) {
        switch (base) {
            case "xs:decimal" : {
                double parsedValue = Double.parseDouble(value);
                String restrictionName = restriction.name;
                switch (restrictionName) {
                    case "xs:minExclusive" : {
                        double min = Double.parseDouble(restriction.getAttributeValue("value"));
                        if (parsedValue <= min) {
                            throw new IllegalArgumentException("Number must be bigger than " + min);
                        }
                        break;
                    }
                    case "xs:maxInclusive" : {
                        double max = Double.parseDouble(restriction.getAttributeValue("value"));
                        if (parsedValue > max) {
                            throw new IllegalArgumentException("Number must not be greater than " + max);
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    private void validateEnumeration(String value, XmlTag restriction) {
        for (XmlTag tag : restriction.tags) {
            if (tag.getAttributeValue("value").equals(value)) {
                return;
            }
        }
        throw new IllegalArgumentException("No value " + value + " in the enumeration");
    }

    private void validateComplexElement(XmlTag xmlTag, XmlTag xsdTag) {
        XmlTag innerTag = xsdTag.tags.get(0).tags.get(0);
        if (innerTag.name.equals("xs:all")) {
            validateAllElements(xmlTag, innerTag);
        } else {
            validateSequenceElements(xmlTag, innerTag);
        }
    }

    private void validateAllElements(XmlTag xmlTag, XmlTag xsdTag) {
        int count = 0;
        for (XmlTag tag : xsdTag.tags) {
            if (tag.name.equals("xs:element")) {
                String tagName = tag.getAttributeValue("name");
                XmlTag innerTag = findTag(tagName, xmlTag);
                if (innerTag == null) {
                    throw new IllegalArgumentException("No element with name " + tagName);
                }
                validateElement(innerTag, tag);
                count++;
            }
        }
        if (count != xmlTag.tags.size()) {
            throw new IllegalArgumentException("Too many inner elements");
        }
    }

    private XmlTag findTag(String name, XmlTag tag) {
        for (XmlTag subtag : tag.tags) {
            if (subtag.name.equals(name)) {
                return subtag;
            }
        }
        return null;
    }

    private void validateSequenceElements(XmlTag xmlTag, XmlTag xsdTag) {
        int count = 0;
        for (XmlTag tag : xsdTag.tags) {
            if (tag.name.equals("xs:element")) {
                XmlTag innerTag;
                try {
                    innerTag = xmlTag.tags.get(count);
                } catch(IndexOutOfBoundsException e) {
                    throw new IllegalArgumentException("Too few inner elements");
                }
                String tagName = tag.getAttributeValue("name");
                if (!innerTag.name.equals(tagName)) {
                    throw new IllegalArgumentException("Wrong element in position " + count);
                }
                validateElement(innerTag, tag);
                count++;
            }
        }
        if (count != xmlTag.tags.size()) {
            throw new IllegalArgumentException("Too many of inner elements");
        }
    }

}
