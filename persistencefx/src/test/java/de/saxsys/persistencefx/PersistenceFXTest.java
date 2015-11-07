package de.saxsys.persistencefx;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import de.saxsys.persistencefx.persistence.ModelPersister;

public class PersistenceFXTest {

 @Test
 public void fluentBuilderShouldNotSetAutoCommit() {
  final PersistenceFX<String> persistenceFX = PersistenceFX.<String> withPersister(null).build();
  assertThat(persistenceFX.isAutoCommit()).isFalse();
 }

 @Test
 public void fluentBuilderShouldSetAutoCommit() {
  final PersistenceFX<String> persistenceFX = PersistenceFX.<String> withPersister(null).autoCommit().build();
  assertThat(persistenceFX.isAutoCommit()).isTrue();
 }

 @Test
 public void loadShouldDelegateToModelPersister() {
  final String model = "model";
  final ModelPersister<String> modelPersister = mock(ModelPersister.class);
  when(modelPersister.load()).thenReturn(model);

  final PersistenceFX<String> persistenceFX = PersistenceFX.<String> withPersister(modelPersister).build();
  final String loadedModel = persistenceFX.load();

  assertThat(loadedModel).isSameAs(model);
 }
}
