package de.saxsys.persistencefx.examples.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@XmlRootElement(name = "car")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Car {

  private final StringProperty name = new SimpleStringProperty();

  public Car() {
  }

  public Car(final String name) {
    this.name.set(name);
  }

  public final StringProperty nameProperty() {
    return this.name;
  }

  @XmlAttribute
  public String getName() {
    return this.nameProperty().get();
  }

  public void setName(final String name) {
    this.nameProperty().set(name);
  }
}
