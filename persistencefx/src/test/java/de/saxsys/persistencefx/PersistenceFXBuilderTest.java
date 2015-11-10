package de.saxsys.persistencefx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.saxsys.persistencefx.model.TestModel;
import de.saxsys.persistencefx.persistence.PersistenceProvider;

@RunWith(MockitoJUnitRunner.class)
public class PersistenceFXBuilderTest {

 @Mock
 private PersistenceProvider<TestModel> modelPersistenceProvider;

 private final TestModel model = new TestModel();

 @Before
 public void setUp() {
  when(modelPersistenceProvider.load()).thenReturn(model);
 }

 @Test
 public void fluentBuilderShouldNotSetAutoCommit() {
  final PersistenceFX<TestModel> persistenceFX = PersistenceFX
    .<TestModel> withPersistenceProvider(modelPersistenceProvider).build();
  assertThat(persistenceFX.isAutoCommit()).isFalse();
 }

 @Test
 public void fluentBuilderShouldSetAutoCommit() {
  final PersistenceFX<TestModel> persistenceFX = PersistenceFX
    .<TestModel> withPersistenceProvider(modelPersistenceProvider).autoCommit().build();
  assertThat(persistenceFX.isAutoCommit()).isTrue();
 }

 @Test
 public void buildShouldDelegateToModelPersistenceProvider() {
  final PersistenceFX<TestModel> persistenceFX = PersistenceFX
    .<TestModel> withPersistenceProvider(modelPersistenceProvider).build();
  final TestModel loadedModel = persistenceFX.getModel();

  assertThat(loadedModel).isSameAs(model);
 }

}
