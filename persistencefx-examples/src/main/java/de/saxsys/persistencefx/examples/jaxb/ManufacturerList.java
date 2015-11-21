package de.saxsys.persistencefx.examples.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlRootElement(name = "manufacurers")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class ManufacturerList {

  private final ObservableList<Manufacturer> manufacturers = FXCollections.observableArrayList();

  @XmlElement(name = "manufacturer")
  public ObservableList<Manufacturer> getManufacturers() {
    return manufacturers;
  }

}
