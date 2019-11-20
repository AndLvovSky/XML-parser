package com.andlvovsky.parser;

import com.andlvovsky.domain.Device;
import com.andlvovsky.exception.XmlParserException;
import com.andlvovsky.handler.DeviceHandler;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XmlDeviceParserSax implements XmlDeviceParser {

  @Override
  public Device parse(String filename) {
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      DeviceHandler deviceHandler = new DeviceHandler();
      saxParser.parse(new File(filename), deviceHandler);
      return deviceHandler.getDevice();
    } catch (Exception e) {
      throw new XmlParserException("Parsing device failed with sax parser", e);
    }
  }

}
