package de.saxsys.persistencefx;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Test;

import de.saxsys.persistencefx.model.TestModel;
import de.saxsys.persistencefx.persistence.PersistenceProvider;

public class PersistenceFXTest {

 @Test
 public void propertyChangedEventsShouldBeDelegatedDirectlyToPersistenceProviderWhenAutoCommitIsFalse() {
  final PersistenceProvider<TestModel> persistenceProvider = mock(PersistenceProvider.class);
  final TestModel model = new TestModel();
  when(persistenceProvider.load()).thenReturn(model);

  PersistenceFX.withPersistenceProvider(persistenceProvider).build();

  model.setStringProp("new");

  verify(persistenceProvider).propertyChanged(model);
 }

 @Test
 public void listChangedEventsShouldBeDelegatedDirectlyToPersistenceProviderWhenAutoCommitIsFalse()
   throws NoSuchFieldException, SecurityException {
  final PersistenceProvider<TestModel> persistenceProvider = mock(PersistenceProvider.class);
  final TestModel model = new TestModel();
  when(persistenceProvider.load()).thenReturn(model);

  PersistenceFX.withPersistenceProvider(persistenceProvider).build();

  final String newValue = "new";
  model.getListProp().add(newValue);

  verify(persistenceProvider).listContentChanged(same(model), eq(TestModel.class.getDeclaredField("listProp")),
    eq(Collections.singletonList(newValue)), eq(Collections.emptyList()));

 }
}
