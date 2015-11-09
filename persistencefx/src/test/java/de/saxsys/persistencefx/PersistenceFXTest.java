package de.saxsys.persistencefx;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;

import de.saxsys.persistencefx.model.TestModel;
import de.saxsys.persistencefx.persistence.PersistenceProvider;

public class PersistenceFXTest {

 @Test
 public void propertyChangedEventsShouldBeDelegatedDirectlyToPersistenceProviderWhenAutoCommitIsTrue() {
  final PersistenceProvider<TestModel> persistenceProvider = mock(PersistenceProvider.class);
  final TestModel model = new TestModel();
  when(persistenceProvider.load()).thenReturn(model);

  PersistenceFX.withPersistenceProvider(persistenceProvider).autoCommit().build();

  model.setStringProp("new");

  verify(persistenceProvider).propertyChanged(model);
 }

 @Test
 public void listChangedEventsShouldBeDelegatedDirectlyToPersistenceProviderWhenAutoCommitIsTrue()
   throws NoSuchFieldException, SecurityException {
  final PersistenceProvider<TestModel> persistenceProvider = mock(PersistenceProvider.class);
  final TestModel model = new TestModel();
  when(persistenceProvider.load()).thenReturn(model);

  PersistenceFX.withPersistenceProvider(persistenceProvider).autoCommit().build();

  final String newValue = "new";
  model.getListProp().add(newValue);

  verify(persistenceProvider).listContentChanged(same(model), eq(TestModel.class.getDeclaredField("listProp")),
    eq(Collections.singletonList(newValue)), eq(Collections.emptyList()));

 }

 @Test
 public void propertyChangedEventsShouldNotBeDelegatedDirectlyToPersistenceProviderWhenAutoCommitIsFalse() {
  final PersistenceProvider<TestModel> persistenceProvider = mock(PersistenceProvider.class);
  final TestModel model = new TestModel();
  when(persistenceProvider.load()).thenReturn(model);

  PersistenceFX.withPersistenceProvider(persistenceProvider).build();

  model.setStringProp("new");

  verify(persistenceProvider, times(0)).propertyChanged(model);
 }

 @Test
 public void listChangedEventsShouldNotBeDelegatedDirectlyToPersistenceProviderWhenAutoCommitIsFalse()
   throws NoSuchFieldException, SecurityException {
  final PersistenceProvider<TestModel> persistenceProvider = mock(PersistenceProvider.class);
  final TestModel model = new TestModel();
  when(persistenceProvider.load()).thenReturn(model);

  PersistenceFX.withPersistenceProvider(persistenceProvider).build();

  final String newValue = "new";
  model.getListProp().add(newValue);

  verify(persistenceProvider, times(0)).listContentChanged(same(model),
    eq(TestModel.class.getDeclaredField("listProp")), eq(Collections.singletonList(newValue)),
    eq(Collections.emptyList()));

 }

 @Test
 public void autoCommitStateShouldBeSwitchable() {
  final PersistenceProvider<TestModel> persistenceProvider = mock(PersistenceProvider.class);
  final TestModel model = new TestModel();
  when(persistenceProvider.load()).thenReturn(model);

  final PersistenceFX<TestModel> cut = PersistenceFX.withPersistenceProvider(persistenceProvider).autoCommit().build();

  model.setStringProp("new");

  verify(persistenceProvider).propertyChanged(model);

  cut.setAutoCommit(false);

  model.setStringProp("new2");

  verify(persistenceProvider, times(1)).propertyChanged(model);
 }
}
