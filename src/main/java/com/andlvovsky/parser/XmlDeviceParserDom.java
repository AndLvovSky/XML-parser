package com.andlvovsky.parser;

import com.andlvovsky.domain.Device;
import com.andlvovsky.exception.XmlParserException;
import com.andlvovsky.mapper.XmlDeviceDomMapper;

import org.w3c.dom.Document;

import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XmlDeviceParserDom implements XmlDeviceParser {

	private XmlDeviceDomMapper mapper;

	public XmlDeviceParserDom(XmlDeviceDomMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public Device parse(String filename) {
		Document document = parseDom(filename);
		return mapper.toDevice(document);
	}

	private Document parseDom(String filename) {
		try {
			InputStream inputStream = new FileInputStream(filename);
			return parse(inputStream);
		} catch (Exception e) {
			throw new XmlParserException("Parsing device failed with dom parser", e);
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