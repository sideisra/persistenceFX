package de.saxsys.persistencefx.model;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

public class ModelWalkerTest {

 @Test
 public void shouldRegisterListenerForObservableValues() {
  final ModelWalker cut = new ModelWalker();
  final TestModel testModel = new TestModel();
  final ModelListener testListener = mock(ModelListener.class);
  cut.walkModel(testModel, testListener);

  testModel.setStringProp("new");

  verify(testListener).propertyChanged(same(testModel));
 }

 @Test
 public void shouldRegisterListenerForObservableLists() throws NoSuchFieldException, SecurityException {
  final ModelWalker cut = new ModelWalker();
  final TestModel testModel = new TestModel();
  final String toBeRemoved = "toBeRemoved";
  testModel.getListProp().add(toBeRemoved);
  final ModelListener testListener = mock(ModelListener.class);
  cut.walkModel(testModel, testListener);

  testModel.getListProp().add("new");
  testModel.getListProp().remove(toBeRemoved);

  verify(testListener).listContentChanged(same(testModel), eq(TestModel.class.getDeclaredField("listProp")),
    eq(Arrays.asList("new")), eq(Collections.emptyList()));
  verify(testListener).listContentChanged(same(testModel), eq(TestModel.class.getDeclaredField("listProp")),
    eq(Collections.emptyList()), eq(Arrays.asList(toBeRemoved)));
 }

 @Test
 public void shouldWalkModelRecursively() {
  final ModelWalker cut = new ModelWalker();
  final TestModel testModel = new TestModel();
  final ModelListener testListener = mock(ModelListener.class);
  cut.walkModel(new TestTopLevelModel(testModel), testListener);

  testModel.setStringProp("new");

  verify(testListener).propertyChanged(testModel);
 }

 public static class TestTopLevelModel {
  private final TestModel testModel;

  public TestTopLevelModel(final TestModel testModel) {
   super();
   this.testModel = testModel;
  }

 }

}
