package com.andlvovsky.validator;

import com.andlvovsky.util.ResourceHelper;

import org.junit.Test;
import org.xml.sax.SAXException;

import java.io.IOException;

public class XmlXsdValidatorTest {

    @Test
    public void validateDevice() throws IOException, SAXException {
        XmlXsdValidator.validate(
            ResourceHelper.getFilename("xml/valid_device.xml"),
            ResourceHelper.getFilename("xsd/device.xsd"));
    }

    @Test(expected = SAXException.class)
    public void deviceValidationFails() throws IOException, SAXException {
        XmlXsdValidator.validate(
            ResourceHelper.getFilename("xml/invalid_device.xml"),
            ResourceHelper.getFilename("xsd/device.xsd"));
    }

}
