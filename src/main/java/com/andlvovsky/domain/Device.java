package com.andlvovsky.domain;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "device")
public class Device {

	public static class Type {

		public static enum ComponentGroup {
			INPUT_DEVICE,
			OUTPUT_DEVICE
		}

		public static enum Port {
			COM,
			USB,
			LPT,
			VGA
		}

		private boolean periphery;
		private int energyConsumption;
		private boolean withCooler;
		private ComponentGroup componentGroup;
		private Port port;

		@XmlElement
		public boolean isPeriphery() {
			return periphery;
		}

		public void setPeriphery(boolean periphery) {
			this.periphery = periphery;
		}

		@XmlElement
		public int getEnergyConsumption() {
			return energyConsumption;
		}

		public void setEnergyConsumption(int energyConsumption) {
			this.energyConsumption = energyConsumption;
		}

		@XmlElement
		public boolean isWithCooler() {
			return withCooler;
		}

		public void setWithCooler(boolean withCooler) {
			this.withCooler = withCooler;
		}

		@XmlElement
		public ComponentGroup getComponentGroup() {
			return componentGroup;
		}

		public void setComponentGroup(ComponentGroup componentGroup) {
			this.componentGroup = componentGroup;
		}

		@XmlElement
		public Port getPort() {
			return port;
		}

		public void setPort(Port port) {
			this.port = port;
		}


	}

	private String id;
	private boolean critical;
	private String name;
	private String origin;
	private double price;
	private Type type;

	@XmlAttribute
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlAttribute
	public boolean isCritical() {
		return critical;
	}

	public void setCritical(boolean critical) {
		this.critical = critical;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	@XmlElement
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	@XmlElement
	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
