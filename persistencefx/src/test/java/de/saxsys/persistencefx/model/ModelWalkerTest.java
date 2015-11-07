package de.saxsys.persistencefx.model;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ModelWalkerTest {

 @Test
 public void shouldRegisterListenerForStringProperty() {
  final ModelWalker cut = new ModelWalker();
  final TestModel testModel = new TestModel();
  final ModelListener testListener = mock(ModelListener.class);
  cut.walkModel(testModel, testListener);

  testModel.setStringProp("new");

  verify(testListener).propertyChanged(testModel, testModel.stringProp);
 }

 public static class TestModel {
  private final StringProperty stringProp = new SimpleStringProperty();

  public final StringProperty stringPropProperty() {
   return this.stringProp;
  }

  public final String getStringProp() {
   return stringProp.get();
  }

  public final void setStringProp(final String stringProp) {
   this.stringProp.set(stringProp);
  }

 }

}
