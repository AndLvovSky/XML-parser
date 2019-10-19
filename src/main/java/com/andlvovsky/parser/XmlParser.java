package com.andlvovsky.parser;

import com.andlvovsky.adapter.DomXmlTagAdapter;
import com.andlvovsky.reader.ResourceReader;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class XmlParser {

	public static XmlTag parseTag(String filename) {
		String localFilename = ResourceReader.getFilename(filename);
		InputStream inputStream;
		try {
			inputStream = new FileInputStream(localFilename);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return parseTag(inputStream);
	}

	public static XmlTag parseTagFromString(String xml) {
		InputStream inputStream = new ByteArrayInputStream(xml.getBytes());
		return parseTag(inputStream);
	}

	public static XmlTag parseTag(InputStream inputStream) {
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
		return DomXmlTagAdapter.getXmlTag(document);
	}

}