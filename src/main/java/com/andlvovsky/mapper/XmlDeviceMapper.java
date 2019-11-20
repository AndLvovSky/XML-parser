package com.andlvovsky.mapper;

import com.andlvovsky.domain.Device;
import com.andlvovsky.parser.XmlParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlDeviceMapper {

    private XmlParser parser;

    public XmlDeviceMapper(XmlParser parser) {
        this.parser = parser;
    }

    public Device toDevice(String xmlFilename) {
        Document document = parser.parse(xmlFilename);
        Device device = createDevice(document);
        return device;
    }

    private Device createDevice(Document document) {
        Element element = document.getDocumentElement();
        Device device = new Device();
        setMainAttributes(device, element);
        setMainFields(device, element);
        Device.Type deviceType = new Device.Type();
        setTypeFields(deviceType, element);
        device.setType(deviceType);
        return device;
    }

    private void setMainAttributes(Device device, Element element) {
        device.setId(element.getAttribute("id"));
        device.setCritical(Boolean.parseBoolean(element.getAttribute("critical")));
    }

    private void setMainFields(Device device, Element element) {
        device.setName(element.getElementsByTagName("name").item(0).getTextContent());
        device.setOrigin(element.getElementsByTagName("origin").item(0).getTextContent());
        device.setPrice(Double.parseDouble(element.getElementsByTagName("price").item(0).getTextContent()));
    }

    private void setTypeFields(Device.Type deviceType, Element element) {
        deviceType.setPeriphery(Boolean.parseBoolean(element.getElementsByTagName("periphery").item(0).getTextContent()));
        deviceType.setEnergyConsumption(Integer.parseInt(element.getElementsByTagName("energyConsumption").item(0).getTextContent()));
        deviceType.setWithCooler(Boolean.parseBoolean(element.getElementsByTagName("withCooler").item(0).getTextContent()));
        deviceType.setComponentGroup(Device.Type.ComponentGroup.valueOf(element.getElementsByTagName("componentGroup").item(0).getTextContent()));
        deviceType.setPort(Device.Type.Port.valueOf(element.getElementsByTagName("port").item(0).getTextContent()));
    }

}
