package de.saxsys.persistencefx.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import de.saxsys.persistencefx.PersistenceFX;
import javafx.collections.ObservableList;

/**
 * Integration test for {@link CascadePersistJPAProvider}.
 */
public class CascadePersistJPAProviderIT {

 @Test
 public void modelRootsShouldBeSavedAndLoaded() {
  System.setProperty("derby.stream.error.file", "target/derby.log");

  final CascadePersistJPAProvider<Manufacturer> cutToSave = new CascadePersistJPAProvider<>("test", Manufacturer.class);
  final PersistenceFX<Manufacturer> persistenceFXToSave = PersistenceFX
    .withPersistenceProvider(cutToSave)
    .autoCommit()
    .build();
  final ObservableList<Manufacturer> modelRootsToSave = persistenceFXToSave.getModelRoots();
  final Manufacturer manufacturerToSave = new Manufacturer("Audi");
  manufacturerToSave.getCarsObservable().add(new Car("A3"));
  modelRootsToSave.add(manufacturerToSave);
  manufacturerToSave.getCarsObservable().add(new Car("A4"));

  assertThat(manufacturerToSave.getCarsObservable()).hasSize(2);

  final CascadePersistJPAProvider<Manufacturer> cutToLoad = new CascadePersistJPAProvider<>("test", Manufacturer.class);
  final PersistenceFX<Manufacturer> persistenceFXToLoad = PersistenceFX
    .withPersistenceProvider(cutToLoad)
    .autoCommit()
    .build();
  final ObservableList<Manufacturer> loadedModelRoots = persistenceFXToLoad.getModelRoots();
  assertThat(loadedModelRoots).hasSize(1);
  final Manufacturer loadedManufacturer = loadedModelRoots.get(0);
  assertThat(loadedManufacturer.getName()).isEqualTo(manufacturerToSave.getName());
  assertThat(loadedManufacturer.getCarsObservable()).hasSameSizeAs(manufacturerToSave.getCarsObservable());
  assertThat(loadedManufacturer.getCarsObservable().get(0).getName())
    .isEqualTo(manufacturerToSave.getCarsObservable().get(0).getName());
  assertThat(loadedManufacturer.getCarsObservable().get(1).getName())
    .isEqualTo(manufacturerToSave.getCarsObservable().get(1).getName());
 }

}
