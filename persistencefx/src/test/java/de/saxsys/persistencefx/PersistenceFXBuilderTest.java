package de.saxsys.persistencefx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import de.saxsys.persistencefx.persistence.PersistenceProvider;

@RunWith(MockitoJUnitRunner.class)
public class PersistenceFXBuilderTest {

 @Mock
 private PersistenceProvider<String> modelPersistenceProvider;

 @Test
 public void fluentBuilderShouldNotSetAutoCommit() {
  final PersistenceFX<String> persistenceFX = PersistenceFX.<String> withPersistenceProvider(modelPersistenceProvider)
    .build();
  assertThat(persistenceFX.isAutoCommit()).isFalse();
 }

 @Test
 public void fluentBuilderShouldSetAutoCommit() {
  final PersistenceFX<String> persistenceFX = PersistenceFX.<String> withPersistenceProvider(modelPersistenceProvider)
    .autoCommit().build();
  assertThat(persistenceFX.isAutoCommit()).isTrue();
 }

 @Test
 public void buildShouldDelegateToModelPersistenceProvider() {
  final String model = "model";
  when(modelPersistenceProvider.load()).thenReturn(model);

  final PersistenceFX<String> persistenceFX = PersistenceFX.<String> withPersistenceProvider(modelPersistenceProvider)
    .build();
  final String loadedModel = persistenceFX.getModel();

  assertThat(loadedModel).isSameAs(model);
 }
}
