package de.saxsys.persistencefx.examples.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

@XmlRootElement(name = "manufacurers")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ManufacturerList {

  private final ListProperty<Manufacturer> manufacturers = new SimpleListProperty<>(
      FXCollections.observableArrayList());

  public final ListProperty<Manufacturer> manufacturersProperty() {
    return this.manufacturers;
  }

  @XmlElement(name = "manufacturer")
  public ListProperty<Manufacturer> getManufacturers() {
    return manufacturers;
  }

}
