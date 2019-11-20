package com.andlvovsky.handler;

import com.andlvovsky.domain.Device;
import com.andlvovsky.meta.DeviceTagsAndAttrs;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

public class DeviceHandler extends DefaultHandler {

  private Device device;
  private Device.Type deviceType;
  private String elementValue;

  @Override
  public void characters(char[] ch, int start, int length) {
    elementValue = new String(ch, start, length);
  }

  @Override
  public void startElement(String uri, String localName, String qualifiedName, Attributes attrs) {
    switch (qualifiedName) {
      case DeviceTagsAndAttrs.DEVICE:
        device = new Device();
        device.setId(attrs.getValue(DeviceTagsAndAttrs.ID));
        device.setCritical(Boolean.parseBoolean(attrs.getValue(DeviceTagsAndAttrs.CRITICAL)));
        break;
      case DeviceTagsAndAttrs.TYPE:
        deviceType = new Device.Type();
        break;
    }
  }

  @Override
  public void endElement(String uri, String localName, String qualifiedName) {
    switch (qualifiedName) {
      case DeviceTagsAndAttrs.NAME:
        device.setName(elementValue);
        break;
      case DeviceTagsAndAttrs.ORIGIN:
        device.setOrigin(elementValue);
        break;
      case DeviceTagsAndAttrs.PRICE:
        device.setPrice(Double.parseDouble(elementValue));
        break;
      case DeviceTagsAndAttrs.TYPE:
        device.setType(deviceType);
        break;
      case DeviceTagsAndAttrs.PERIPHERY:
        deviceType.setPeriphery(Boolean.parseBoolean(elementValue));
        break;
      case DeviceTagsAndAttrs.ENERGY_CONSUMPTION:
        deviceType.setEnergyConsumption(Integer.parseInt(elementValue));
        break;
      case DeviceTagsAndAttrs.WITH_COOLER:
        deviceType.setWithCooler(Boolean.parseBoolean(elementValue));
        break;
      case DeviceTagsAndAttrs.COMPONENT_GROUP:
        deviceType.setComponentGroup(Device.Type.ComponentGroup.valueOf(elementValue));
        break;
      case DeviceTagsAndAttrs.PORT:
        deviceType.setPort(Device.Type.Port.valueOf(elementValue));
        break;
    }
  }

  public Device getDevice() {
    return device;
  }

}
