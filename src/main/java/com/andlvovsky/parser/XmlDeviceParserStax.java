package com.andlvovsky.parser;

import com.andlvovsky.domain.Device;
import com.andlvovsky.exception.XmlParserException;
import com.andlvovsky.meta.DeviceTagsAndAttrs;

import java.io.FileInputStream;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

  public class XmlDeviceParserStax implements XmlDeviceParser {

  private Device device;
  private Device.Type deviceType;

  @Override
  public Device parse(String filename) {
    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    try {
      XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(new FileInputStream(filename));
      while(xmlEventReader.hasNext()){
        XMLEvent xmlEvent = xmlEventReader.nextEvent();
        if (xmlEvent.isStartElement()){
          handleStartElement(xmlEvent, xmlEventReader);
        }
        if(xmlEvent.isEndElement()){
          handleEndElement(xmlEvent);
        }
      }
      return device;
    } catch (Exception e) {
      throw new XmlParserException("Parsing device failed with stax parser", e);
    }
  }

  private void handleStartElement(XMLEvent xmlEvent, XMLEventReader xmlEventReader) throws XMLStreamException {
    StartElement startElement = xmlEvent.asStartElement();
    switch (startElement.getName().getLocalPart()) {
      case DeviceTagsAndAttrs.DEVICE:
        device = new Device();
        device.setId(startElement.getAttributeByName(new QName(DeviceTagsAndAttrs.ID)).getValue());
        device.setCritical(Boolean.parseBoolean(startElement.getAttributeByName(new QName(DeviceTagsAndAttrs.CRITICAL)).getValue()));
        break;
      case DeviceTagsAndAttrs.TYPE:
        deviceType = new Device.Type();
        break;
      case DeviceTagsAndAttrs.NAME:
        xmlEvent = xmlEventReader.nextEvent();
        device.setName(xmlEvent.asCharacters().getData());
        break;
      case DeviceTagsAndAttrs.ORIGIN:
        xmlEvent = xmlEventReader.nextEvent();
        device.setOrigin(xmlEvent.asCharacters().getData());
        break;
      case DeviceTagsAndAttrs.PRICE:
        xmlEvent = xmlEventReader.nextEvent();
        device.setPrice(Double.parseDouble(xmlEvent.asCharacters().getData()));
        break;
      case DeviceTagsAndAttrs.PERIPHERY:
        xmlEvent = xmlEventReader.nextEvent();
        deviceType.setPeriphery(Boolean.parseBoolean(xmlEvent.asCharacters().getData()));
        break;
      case DeviceTagsAndAttrs.ENERGY_CONSUMPTION:
        xmlEvent = xmlEventReader.nextEvent();
        deviceType.setEnergyConsumption(Integer.parseInt(xmlEvent.asCharacters().getData()));
        break;
      case DeviceTagsAndAttrs.WITH_COOLER:
        xmlEvent = xmlEventReader.nextEvent();
        deviceType.setWithCooler(Boolean.parseBoolean(xmlEvent.asCharacters().getData()));
        break;
      case DeviceTagsAndAttrs.COMPONENT_GROUP:
        xmlEvent = xmlEventReader.nextEvent();
        deviceType.setComponentGroup(Device.Type.ComponentGroup.valueOf(xmlEvent.asCharacters().getData()));
        break;
      case DeviceTagsAndAttrs.PORT:
        xmlEvent = xmlEventReader.nextEvent();
        deviceType.setPort(Device.Type.Port.valueOf(xmlEvent.asCharacters().getData()));
        break;
    }
  }

  private void handleEndElement(XMLEvent xmlEvent) {
    EndElement endElement = xmlEvent.asEndElement();
    switch (endElement.getName().getLocalPart()) {
      case DeviceTagsAndAttrs.TYPE:
        device.setType(deviceType);
        break;
    }
  }

}
