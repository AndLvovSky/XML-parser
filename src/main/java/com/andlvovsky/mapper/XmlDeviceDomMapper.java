package com.andlvovsky.mapper;

import com.andlvovsky.domain.Device;

import org.w3c.dom.Document;

public interface XmlDeviceDomMapper {

  Device toDevice(Document document);

}
