package com.andlvovsky.parser;

import java.util.ArrayList;
import java.util.List;

public class XmlTag {

	public final String name;

	public List<XmlAttribute> attrs = new ArrayList<>();

	public List<XmlTag> tags = new ArrayList<>();

	public boolean isSingleton;

	public boolean containsText;

	public String text;

	public XmlTag(String name) {
		this.name = name;
	}

	public void addTag(XmlTag tag) {
		tags.add(tag);
	}

	public void addAttribute(XmlAttribute attr) {
		attrs.add(attr);
	}

	public void setText(String text) {
		containsText = true;
		this.text = text;
	}

	public String getAttributeValue(String name) {
		XmlAttribute attr = getAttribute(name);
		return attr == null ? null : attr.value;
	}

	public boolean hasAttribute(String name) {
		XmlAttribute attr = getAttribute(name);
		return attr != null;
	}

	public String toString() {
		return toString(0);
	}

	private XmlAttribute getAttribute(String name) {
		for (XmlAttribute attr : attrs) {
			if (attr.name.equals(name)) {
				return attr;
			}
		}
		return null;
	}

	private String toString(int depth) {
		String res = new String(new char[depth * 4]).replace("\0", " ") + 
			"<" + name;
		for (XmlAttribute attr : attrs) {
			res += " " + attr.toString();
		}
		if (isSingleton) {
			res += "/>\n";
			return res;
		}
		res += ">\n";
		if (containsText) {
		    res += text + "\n";
        } else {
            for (XmlTag tag : tags) {
                res += tag.toString(depth + 1);
            }
        }
		res += new String(new char[depth * 4]).replace("\0", " ") +
			"</" + name + ">\n";
		return res;
	}

}