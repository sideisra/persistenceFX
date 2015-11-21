package de.saxsys.persistencefx.model.testdata;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class TestModel {
  private final TestObsValue stringProp = new TestObsValue();
  private final TestObsList obsList = new TestObsList();
  private final ListProperty<String> listProp = new SimpleListProperty<>(FXCollections.observableArrayList());
  private final ObservableList<TestModelWithProps> obsListWithProps = FXCollections.observableArrayList();
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

  public TestObsList getObsList() {
    return obsList;
  }

  public TestModelWithoutProps getWithoutProps() {
    return withoutProps;
  }

  public ListProperty<String> getListProp() {
    return listProp;
  }

  public ObservableList<TestModelWithProps> getObsListWithProps() {
    return obsListWithProps;
  }

}