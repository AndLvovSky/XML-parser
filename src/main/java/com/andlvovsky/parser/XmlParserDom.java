package com.andlvovsky.parser;

import com.andlvovsky.exception.XmlParserException;

import org.w3c.dom.Document;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XmlParserDom implements XmlParser {
	
	public Document parse(String filename) {
		try {
			InputStream inputStream = new FileInputStream(filename);
			return parse(inputStream);
		} catch (Exception e) {
			throw new XmlParserException("Parsing with dom parser failed", e);
		}
	}
	
	private Document parse(InputStream inputStream) throws Exception {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder;
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document;
		document = documentBuilder.parse(inputStream);
		return document;
	}

}