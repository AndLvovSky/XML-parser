package com.andlvovsky.parser;

public class XmlAttribute {

	public final String name;

	public final String value;

	public XmlAttribute(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String toString() {
		return name + "=" + "\"" + value + "\"";
	}

}