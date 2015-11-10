package de.saxsys.persistencefx.model;

public class TestModelWithNullValues {
  private final TestObsValue stringProp = null;
  private final TestObsList listProp = null;
  private final TestModelWithoutProps withoutProps = null;

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