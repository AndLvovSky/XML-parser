package com.andlvovsky.parser;

import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlParser {

	private XmlParser() {}

	public static Document parseDom(String filename) {
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(filename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return parseDom(inputStream);
	}

	public static Document parseTagFromString(String xml) {
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		return parseDom(inputStream);
	}

	public static Document parseDom(InputStream inputStream) {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		Document document;
		try {
			document = documentBuilder.parse(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return document;
	}

}