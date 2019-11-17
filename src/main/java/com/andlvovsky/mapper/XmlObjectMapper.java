package com.andlvovsky.mapper;

import com.andlvovsky.domain.Device;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class XmlObjectMapper {

    private XmlObjectMapper() {}

    public static Object toObject(Class<?> clazz, String xmlFilename) throws JAXBException {
        File file = new File(xmlFilename);
        JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Device device = (Device) jaxbUnmarshaller.unmarshal(file);
        return device;
    }

}
