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
import de.saxsys.persistencefx.model.testdata.TestModelWithProps;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class ModelWalkerTest {

  @SuppressWarnings("unchecked")
  @Test
  public void shouldRegisterListenerForObservableValues() {
    final ModelWalker<TestModel> cut = new ModelWalker<>();
    final TestModel testModel = new TestModel();
    final ModelListener<TestModel> testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    testModel.setStringProp("new");

    verify(testListener).propertyChanged(same(testModel));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void shouldRegisterListenerForObservableLists() throws NoSuchFieldException, SecurityException {
    final ModelWalker<TestModel> cut = new ModelWalker<>();
    final TestModel testModel = new TestModel();
    final String toBeRemoved = "toBeRemoved";
    testModel.getObsList().add(toBeRemoved);
    final ModelListener<TestModel> testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    testModel.getObsList().add("new");
    testModel.getObsList().remove(toBeRemoved);

    verify(testListener).listContentChanged(same(testModel), eq(TestModel.class.getDeclaredField("obsList")),
        eq(Arrays.asList("new")), eq(Collections.emptyList()));
    verify(testListener).listContentChanged(same(testModel), eq(TestModel.class.getDeclaredField("obsList")),
        eq(Collections.emptyList()), eq(Arrays.asList(toBeRemoved)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void shouldWalkModelRecursively() {
    final ModelWalker<TestModel> cut = new ModelWalker<>();
    final TestModel testModel = new TestModel();
    final ModelListener<TestModel> testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    testModel.setStringProp("new");

    verify(testListener).propertyChanged(testModel);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void shouldStopRecursionWhenClassContainsNoProperties() {
    final ModelWalker<TestModel> cut = new ModelWalker<>();
    final TestModel testModel = new TestModel();
    final ModelListener<TestModel> testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    testModel.getWithoutProps().getTestModel().setStringProp("new");

    verifyZeroInteractions(testListener);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void nullValuesAreIgnored() {
    final ModelWalker<TestModelWithNullValues> cut = new ModelWalker<>();
    final TestModelWithNullValues testModel = new TestModelWithNullValues();
    final ModelListener<TestModelWithNullValues> testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    verifyZeroInteractions(testListener);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void shouldListenToPropAndListChangesOfListProperties() throws NoSuchFieldException, SecurityException {
    final ModelWalker<TestModel> cut = new ModelWalker<>();
    final TestModel testModel = new TestModel();
    final ModelListener<TestModel> testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    testModel.getListProp().add("new");

    verify(testListener).listContentChanged(same(testModel), eq(TestModel.class.getDeclaredField("listProp")),
        eq(Arrays.asList("new")), eq(Collections.emptyList()));

    testModel.getListProp().set(FXCollections.observableArrayList());

    verify(testListener).propertyChanged(same(testModel));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void shouldListenToPropsInListEntries() {
    final ModelWalker<TestModel> cut = new ModelWalker<>();
    final TestModel testModel = new TestModel();
    final TestModelWithProps testModelWithProps = new TestModelWithProps();
    testModel.getObsListWithProps().add(testModelWithProps);
    final ModelListener<TestModel> testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    testModelWithProps.setStringProp("new");

    verify(testListener).propertyChanged(same(testModelWithProps));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void shouldListenToPropsInNewlyAddedListEntries() {
    final ModelWalker<TestModel> cut = new ModelWalker<>();
    final TestModel testModel = new TestModel();
    final ModelListener<TestModel> testListener = mock(ModelListener.class);
    cut.walkModelRoots(FXCollections.observableArrayList(testModel), testListener);

    final TestModelWithProps testModelWithProps = new TestModelWithProps();
    testModel.getObsListWithProps().add(testModelWithProps);
    testModelWithProps.setStringProp("new");

    verify(testListener).propertyChanged(same(testModelWithProps));
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
