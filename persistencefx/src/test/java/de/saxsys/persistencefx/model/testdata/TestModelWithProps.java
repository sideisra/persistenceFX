package de.saxsys.persistencefx.model.testdata;

public class TestModelWithProps {
  private final TestObsValue stringProp = new TestObsValue();
  private final TestObsList listProp = new TestObsList();

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

}