package de.saxsys.persistencefx.examples.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlRootElement(name = "manufacturer")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Manufacturer {

  private final StringProperty name = new SimpleStringProperty();
  private final ObservableList<Car> observableCars = FXCollections.observableArrayList();

  public Manufacturer() {
  }

  public Manufacturer(final String name) {
    this.name.set(name);
  }

  public StringProperty nameProperty() {
    return this.name;
  }

  @XmlAttribute
  public String getName() {
    return this.nameProperty().get();
  }

  public final void setName(final String name) {
    this.nameProperty().set(name);
  }

  @XmlElement(name = "car")
  public ObservableList<Car> getCars() {
    return observableCars;
  }

}
