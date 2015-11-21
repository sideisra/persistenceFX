package de.saxsys.persistencefx.examples.jpa;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Access(AccessType.PROPERTY)
public class Car {

 @Transient
 private long id;
 @Transient
 private final StringProperty name = new SimpleStringProperty();

 public Car() {

 }

 public Car(final String name) {
  this.name.set(name);
 }

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 public long getId() {
  return id;
 }

 public void setId(final long id) {
  this.id = id;
 }

 public final StringProperty nameProperty() {
  return this.name;
 }

 public String getName() {
  return this.nameProperty().get();
 }

 public void setName(final String name) {
  this.nameProperty().set(name);
 }
}
