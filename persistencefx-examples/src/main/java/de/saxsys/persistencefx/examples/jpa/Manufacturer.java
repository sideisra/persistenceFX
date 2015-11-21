package de.saxsys.persistencefx.examples.jpa;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

@Entity
@Access(AccessType.PROPERTY)
public class Manufacturer {

  @Transient
  private Long id;
  @Transient
  private final StringProperty name = new SimpleStringProperty();
  @Transient
  private List<Car> cars;
  @Transient
  private final ListProperty<Car> observableCars = new SimpleListProperty<>(FXCollections.observableArrayList());

  public Manufacturer() {
  }

  public Manufacturer(final String name) {
    this.name.set(name);
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  public void setId(final Long id) {
    this.id = id;
  }

  public StringProperty nameProperty() {
    return this.name;
  }

  public String getName() {
    return this.nameProperty().get();
  }

  public final void setName(final String name) {
    this.nameProperty().set(name);
  }

  @Transient
  public ListProperty<Car> getCarsObservable() {
    return observableCars;
  }

  @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private List<Car> getCars() {
    return cars;
  }

  private void setCars(final List<Car> cars) {
    this.cars = cars;
    this.observableCars.set(FXCollections.observableList(cars));
  }
}
