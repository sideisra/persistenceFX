package de.saxsys.persistencefx.jaxb;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlRootElement(name = "jaxbtest")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class JaxbModel {

  private final StringProperty stringProp = new SimpleStringProperty();
  private final ObservableList<String> obslist = FXCollections.observableArrayList();

  public final StringProperty stringPropProperty() {
    return this.stringProp;
  }

  @XmlAttribute
  public final java.lang.String getStringProp() {
    return this.stringPropProperty().get();
  }

  public final void setStringProp(final java.lang.String stringProp) {
    this.stringPropProperty().set(stringProp);
  }

  @XmlElement(name = "obsVal")
  @XmlElementWrapper(name = "obsValues")
  public ObservableList<String> getObslist() {
    return obslist;
  }

}
