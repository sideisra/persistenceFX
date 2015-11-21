package de.saxsys.persistencefx;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.saxsys.persistencefx.error.ErrorHandler;
import de.saxsys.persistencefx.model.testdata.TestModel;
import de.saxsys.persistencefx.persistence.PersistenceProvider;

public class PersistenceFXTest {
  final PersistenceProvider<TestModel> persistenceProvider = mock(PersistenceProvider.class);
  final TestModel model = new TestModel();

  @Before
  public void initMocks() {
    when(persistenceProvider.load()).thenReturn(Collections.singletonList(model));
  }

  @Test
  public void propertyChangedEventsShouldBeDelegatedDirectlyToPersistenceProviderWhenAutoCommitIsTrue() {
    PersistenceFX.withPersistenceProvider(persistenceProvider).autoCommit().build();

    model.setStringProp("new");

    verify(persistenceProvider).propertyChanged(model);
  }

  @Test
  public void listChangedEventsShouldBeDelegatedDirectlyToPersistenceProviderWhenAutoCommitIsTrue()
      throws NoSuchFieldException, SecurityException {
    PersistenceFX.withPersistenceProvider(persistenceProvider).autoCommit().build();

    final String newValue = "new";
    model.getObsList().add(newValue);

    verify(persistenceProvider).listContentChanged(same(model), eq(TestModel.class.getDeclaredField("obsList")),
        eq(Collections.singletonList(newValue)), eq(Collections.emptyList()));

  }

  @Test
  public void propertyChangedEventsShouldNotBeDelegatedDirectlyToPersistenceProviderWhenAutoCommitIsFalse() {
    PersistenceFX.withPersistenceProvider(persistenceProvider).build();

    model.setStringProp("new");

    verify(persistenceProvider, times(0)).propertyChanged(model);
  }

  @Test
  public void listChangedEventsShouldNotBeDelegatedDirectlyToPersistenceProviderWhenAutoCommitIsFalse()
      throws NoSuchFieldException, SecurityException {
    PersistenceFX.withPersistenceProvider(persistenceProvider).build();

    final String newValue = "new";
    model.getObsList().add(newValue);

    verify(persistenceProvider, times(0)).listContentChanged(same(model),
        eq(TestModel.class.getDeclaredField("listProp")), eq(Collections.singletonList(newValue)),
        eq(Collections.emptyList()));
  }

  @Test
  public void autoCommitStateShouldBeSwitchable() {
    final PersistenceFX<TestModel> cut = PersistenceFX.withPersistenceProvider(persistenceProvider).autoCommit()
        .build();

    model.setStringProp("new");

    verify(persistenceProvider).propertyChanged(model);

    cut.setAutoCommit(false);

    model.setStringProp("new2");

    verify(persistenceProvider, times(1)).propertyChanged(model);
  }

  @Test
  public void suspendedEventsShouldBeDelegatedToPersistenceProviderOnCommit()
      throws NoSuchFieldException, SecurityException {
    final PersistenceFX<TestModel> cut = PersistenceFX.withPersistenceProvider(persistenceProvider).build();

    final String newValue = "new";
    model.getObsList().add(newValue);
    model.setStringProp("new");

    verify(persistenceProvider, times(0)).propertyChanged(model);
    verify(persistenceProvider, times(0)).listContentChanged(same(model),
        eq(TestModel.class.getDeclaredField("obsList")), eq(Collections.singletonList(newValue)),
        eq(Collections.emptyList()));

    cut.commit();

    verify(persistenceProvider).propertyChanged(model);
    verify(persistenceProvider).listContentChanged(same(model),
        eq(TestModel.class.getDeclaredField("obsList")), eq(Collections.singletonList(newValue)),
        eq(Collections.emptyList()));
  }

  @Test
  public void secondCommitShouldNotFireOldEvents()
      throws NoSuchFieldException, SecurityException {
    final PersistenceFX<TestModel> cut = PersistenceFX.withPersistenceProvider(persistenceProvider).build();

    model.setStringProp("new");

    verify(persistenceProvider, times(0)).propertyChanged(model);

    cut.commit();
    cut.commit();

    verify(persistenceProvider).propertyChanged(model);
  }

  @Test
  public void eventsShouldBeFiredWhenActivatingAutoCommit()
      throws NoSuchFieldException, SecurityException {
    final PersistenceFX<TestModel> cut = PersistenceFX.withPersistenceProvider(persistenceProvider).build();

    model.setStringProp("new");

    verify(persistenceProvider, times(0)).propertyChanged(model);

    cut.setAutoCommit(true);

    verify(persistenceProvider).propertyChanged(model);
  }

  @Test
  public void errorsShouldBeDelegatedToErrorHandlerWhileCommiting()
      throws NoSuchFieldException, SecurityException {
    final ErrorHandler errorHandler = mock(ErrorHandler.class);
    final PersistenceFX<TestModel> cut = PersistenceFX.withPersistenceProvider(persistenceProvider)
        .errorHandler(errorHandler).build();

    final RuntimeException runEx = new RuntimeException("testEx");
    doThrow(runEx).when(persistenceProvider).propertyChanged(same(model));

    model.setStringProp("new");

    cut.commit();

    verify(errorHandler).error(same(model), same(runEx));
  }

  @Test
  public void errorsShouldBeDelegatedToErrorHandlerOnAutoCommit()
      throws NoSuchFieldException, SecurityException {
    final ErrorHandler errorHandler = mock(ErrorHandler.class);
    PersistenceFX.withPersistenceProvider(persistenceProvider)
        .errorHandler(errorHandler).autoCommit().build();

    final RuntimeException runEx = new RuntimeException("testEx");
    doThrow(runEx).when(persistenceProvider).propertyChanged(same(model));

    model.setStringProp("new");

    verify(errorHandler).error(same(model), same(runEx));
  }

  @Test
  public void errorsOnListChangeEventsShouldBeDelegatedToErrorHandlerWhileCommiting()
      throws NoSuchFieldException, SecurityException {
    final ErrorHandler errorHandler = mock(ErrorHandler.class);
    final PersistenceFX<TestModel> cut = PersistenceFX.withPersistenceProvider(persistenceProvider)
        .errorHandler(errorHandler).build();

    final RuntimeException runEx = new RuntimeException("testEx");
    doThrow(runEx).when(persistenceProvider).listContentChanged(any(), any(), any(), any());

    model.getObsList().add("new");

    cut.commit();

    verify(errorHandler).error(same(model), same(runEx));
  }

  @Test
  public void errorsOnListChangeEventsShouldBeDelegatedToErrorHandlerOnAutoCommit()
      throws NoSuchFieldException, SecurityException {
    final ErrorHandler errorHandler = mock(ErrorHandler.class);
    PersistenceFX.withPersistenceProvider(persistenceProvider)
        .errorHandler(errorHandler).autoCommit().build();

    final RuntimeException runEx = new RuntimeException("testEx");
    doThrow(runEx).when(persistenceProvider).listContentChanged(any(), any(), any(), any());

    model.getObsList().add("new");

    verify(errorHandler).error(same(model), same(runEx));
  }

  @Test
  public void changesOfRootListShouldFireEvent() {
    final PersistenceFX<TestModel> cut = PersistenceFX.withPersistenceProvider(persistenceProvider).autoCommit()
        .build();

    final TestModel newModelRoot = new TestModel();
    cut.getModelRoots().add(newModelRoot);

    verify(persistenceProvider).modelRootListChanged(eq(Collections.singletonList(newModelRoot)),
        eq(Collections.emptyList()));
  }

  @Test
  public void newRootElementsAreObservedAsWell() {
    final PersistenceFX<TestModel> cut = PersistenceFX.withPersistenceProvider(persistenceProvider).autoCommit()
        .build();

    final TestModel newModelRoot = new TestModel();
    cut.getModelRoots().add(newModelRoot);
    newModelRoot.setStringProp("new");

    verify(persistenceProvider).propertyChanged(newModelRoot);
  }
}
