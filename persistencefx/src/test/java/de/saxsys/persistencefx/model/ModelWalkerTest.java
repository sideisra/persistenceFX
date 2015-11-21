package de.saxsys.persistencefx.model;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import de.saxsys.persistencefx.model.testdata.TestModel;
import de.saxsys.persistencefx.model.testdata.TestModelWithNullValues;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class ModelWalkerTest {

  @Test
  public void shouldRegisterListenerForObservableValues() {
    final ModelWalker cut = new ModelWalker();
    final TestModel testModel = new TestModel();
    final ModelListener testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    testModel.setStringProp("new");

    verify(testListener).propertyChanged(same(testModel));
  }

  @Test
  public void shouldRegisterListenerForObservableLists() throws NoSuchFieldException, SecurityException {
    final ModelWalker cut = new ModelWalker();
    final TestModel testModel = new TestModel();
    final String toBeRemoved = "toBeRemoved";
    testModel.getObsList().add(toBeRemoved);
    final ModelListener testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    testModel.getObsList().add("new");
    testModel.getObsList().remove(toBeRemoved);

    verify(testListener).listContentChanged(same(testModel), eq(TestModel.class.getDeclaredField("obsList")),
        eq(Arrays.asList("new")), eq(Collections.emptyList()));
    verify(testListener).listContentChanged(same(testModel), eq(TestModel.class.getDeclaredField("obsList")),
        eq(Collections.emptyList()), eq(Arrays.asList(toBeRemoved)));
  }

  @Test
  public void shouldWalkModelRecursively() {
    final ModelWalker cut = new ModelWalker();
    final TestModel testModel = new TestModel();
    final ModelListener testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    testModel.setStringProp("new");

    verify(testListener).propertyChanged(testModel);
  }

  @Test
  public void shouldStopRecursionWhenClassContainsNoProperties() {
    final ModelWalker cut = new ModelWalker();
    final TestModel testModel = new TestModel();
    final ModelListener testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    testModel.getWithoutProps().getTestModel().setStringProp("new");

    verifyZeroInteractions(testListener);
  }

  @Test
  public void nullValuesAreIgnored() {
    final ModelWalker cut = new ModelWalker();
    final TestModelWithNullValues testModel = new TestModelWithNullValues();
    final ModelListener testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    verifyZeroInteractions(testListener);
  }

  @Test
  public void shouldListenToPropAndListChangesOfListProperties() throws NoSuchFieldException, SecurityException {
    final ModelWalker cut = new ModelWalker();
    final TestModel testModel = new TestModel();
    final ModelListener testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    testModel.getListProp().add("new");

    verify(testListener).listContentChanged(same(testModel), eq(TestModel.class.getDeclaredField("listProp")),
        eq(Arrays.asList("new")), eq(Collections.emptyList()));

    testModel.getListProp().set(FXCollections.observableArrayList());

    verify(testListener).propertyChanged(same(testModel));
  }

  public static class TestTopLevelModel {
    private final StringProperty stringProp = new SimpleStringProperty("");
    private final TestModel testModel;

    public TestTopLevelModel(final TestModel testModel) {
      super();
      this.testModel = testModel;
    }

    public StringProperty getStringProp() {
      return stringProp;
    }

    public TestModel getTestModel() {
      return testModel;
    }

  }

}
