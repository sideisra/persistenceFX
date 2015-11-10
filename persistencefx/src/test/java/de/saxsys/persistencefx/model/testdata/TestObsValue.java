package de.saxsys.persistencefx.model.testdata;

import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class TestObsValue implements ObservableValue<String> {

 private final StringProperty value = new SimpleStringProperty();

 public String get() {
  return value.get();
 }

 public void set(final String value) {
  this.value.set(value);
 }

 @Override
 public String getValue() {
  return value.getValue();
 }

 @Override
 public void addListener(final ChangeListener<? super String> listener) {
  value.addListener(listener);
 }

 @Override
 public void removeListener(final ChangeListener<? super String> listener) {
  value.removeListener(listener);
 }

 @Override
 public void addListener(final InvalidationListener listener) {
  value.addListener(listener);
 }

 @Override
 public void removeListener(final InvalidationListener listener) {
  value.removeListener(listener);
 }

}