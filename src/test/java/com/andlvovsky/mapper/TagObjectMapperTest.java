package com.andlvovsky.mapper;

import com.andlvovsky.domain.Device;
import com.andlvovsky.parser.XmlAttribute;
import com.andlvovsky.parser.XmlTag;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TagObjectMapperTest {

    public static class OneString {
        public OneString() {
        }

        private String xyz;

        public void setXyz(String xyz) {
            this.xyz = xyz;
        }
    }

    public static class OneInteger {
        public OneInteger() {
        }

        private int xyz;

        public void setXyz(int xyz) {
            this.xyz = xyz;
        }
    }

    public static class OneEnum {
        public static enum En {
            A_K, B_P, C_R
        }

        public OneEnum() {
        }

        private En xyz;

        public void setXyz(En xyz) {
            this.xyz = xyz;
        }
    }

    public static class WithInnerClass {
        public static class InnerClass {
            private String q;

            public InnerClass() {
            }

            public void setQ(String q) {
                this.q = q;
            }
        }

        public WithInnerClass() {
        }

        private InnerClass xyz;

        public void setXyz(InnerClass xyz) {
            this.xyz = xyz;
        }
    }

    @Test
    public void mapOneStringFieldClass() {
        XmlTag tag = new XmlTag("OneString");
        XmlTag subtag = new XmlTag("xyz");
        subtag.containsText = true;
        subtag.text = "hello";
        tag.addTag(subtag);
        TagObjectMapper mapper = new TagObjectMapper(OneString.class);
        OneString a = (OneString) mapper.map(tag);
        assertEquals("hello", a.xyz);
    }

    @Test
    public void mapOneIntegerFieldClass() {
        XmlTag tag = new XmlTag("OneInteger");
        XmlTag subtag = new XmlTag("xyz");
        subtag.containsText = true;
        subtag.text = "17";
        tag.addTag(subtag);
        TagObjectMapper mapper = new TagObjectMapper(OneInteger.class);
        OneInteger a = (OneInteger) mapper.map(tag);
        assertEquals(17, a.xyz);
    }

    @Test
    public void mapOneEnumFieldClass() {
        XmlTag tag = new XmlTag("OneEnum");
        XmlTag subtag = new XmlTag("xyz");
        subtag.containsText = true;
        subtag.text = "b_p";
        tag.addTag(subtag);
        TagObjectMapper mapper = new TagObjectMapper(OneEnum.class);
        OneEnum a = (OneEnum) mapper.map(tag);
        assertEquals(OneEnum.En.B_P, a.xyz);
    }

    @Test
    public void mapClassWithInnerClass() {
        XmlTag tag = new XmlTag("WithInnerClass");
        XmlTag subtag = new XmlTag("xyz");
        XmlTag subsubtag = new XmlTag("q");
        subsubtag.containsText = true;
        subsubtag.text = "hi";
        subtag.addTag(subsubtag);
        tag.addTag(subtag);
        TagObjectMapper mapper = new TagObjectMapper(WithInnerClass.class);
        WithInnerClass a = (WithInnerClass) mapper.map(tag);
        assertEquals("hi", a.xyz.q);
    }

    @Test
    public void mapDevice() {
        XmlTag deviceTag = new XmlTag("device");
        XmlTag nameTag = new XmlTag("name");
        XmlTag originTag = new XmlTag("origin");
        XmlTag priceTag = new XmlTag("price");
        XmlTag typeTag = new XmlTag("type");
        XmlTag peripheryTag = new XmlTag("periphery");
        XmlTag energyConsumptionTag = new XmlTag("energyConsumption");
        XmlTag withCoolerTag = new XmlTag("withCooler");
        XmlTag componentGroupTag = new XmlTag("componentGroup");
        XmlTag portTag = new XmlTag("port");
        deviceTag.addTag(nameTag);
        deviceTag.addTag(originTag);
        deviceTag.addTag(priceTag);
        deviceTag.addTag(typeTag);
        typeTag.addTag(peripheryTag);
        typeTag.addTag(energyConsumptionTag);
        typeTag.addTag(withCoolerTag);
        typeTag.addTag(componentGroupTag);
        typeTag.addTag(portTag);
        deviceTag.addAttribute(new XmlAttribute("id", "id-1"));
        deviceTag.addAttribute(new XmlAttribute("critical", "true"));
        nameTag.setText("Display AGK-1203, 20 inch, IPS");
        originTag.setText("USA");
        priceTag.setText("200.75");
        peripheryTag.setText("true");
        energyConsumptionTag.setText("50");
        withCoolerTag.setText("false");
        componentGroupTag.setText("output_device");
        portTag.setText("VGA");
        TagObjectMapper mapper = new TagObjectMapper(Device.class);
        Device device = (Device)mapper.map(deviceTag);
        assertEquals("id-1", device.getId());
        assertEquals(true, device.isCritical());
        assertEquals("Display AGK-1203, 20 inch, IPS", device.getName());
        assertEquals("USA", device.getOrigin());
        assertEquals(200.75, device.getPrice(), 0.0000001);
        Device.Type deviceType = device.getType();
        assertEquals(true, deviceType.isPeriphery());
        assertEquals(50, deviceType.getEnergyConsumption());
        assertEquals(false, deviceType.isWithCooler());
        assertEquals(Device.Type.ComponentGroup.OUTPUT_DEVICE, deviceType.getComponentGroup());
        assertEquals(Device.Type.Port.VGA, deviceType.getPort());
    }

}


