package de.saxsys.persistencefx.model;

public class TestModel {
 private final TestObsValue stringProp = new TestObsValue();
 private final TestObsList listProp = new TestObsList();
 private final TestModelWithoutProps withoutProps = new TestModelWithoutProps();

 public final TestObsValue stringPropProperty() {
  return this.stringProp;
 }

 public final String getStringProp() {
  return stringProp.get();
 }

 public final void setStringProp(final String stringProp) {
  this.stringProp.set(stringProp);
 }

 public TestObsList getListProp() {
  return listProp;
 }

 public TestModelWithoutProps getWithoutProps() {
  return withoutProps;
 }

}