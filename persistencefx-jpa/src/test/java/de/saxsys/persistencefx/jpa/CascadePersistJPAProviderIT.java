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
  final CascadePersistJPAProvider<Manufacturer> cutToSave = new CascadePersistJPAProvider<>("test", Manufacturer.class);
  final PersistenceFX<Manufacturer> persistenceFXToSave = PersistenceFX
    .withPersistenceProvider(cutToSave)
    .autoCommit()
    .build();
  final ObservableList<Manufacturer> modelRootsToSave = persistenceFXToSave.getModelRoots();
  final Manufacturer manufacturerToSave1 = new Manufacturer("Audi");
  manufacturerToSave1.getCarsObservable().add(new Car("A3"));
  modelRootsToSave.add(manufacturerToSave1);
  manufacturerToSave1.getCarsObservable().add(new Car("A4"));

  final CascadePersistJPAProvider<Manufacturer> cutToLoad = new CascadePersistJPAProvider<>("test", Manufacturer.class);
  final PersistenceFX<Manufacturer> persistenceFXToLoad = PersistenceFX
    .withPersistenceProvider(cutToLoad)
    .autoCommit()
    .build();
  final ObservableList<Manufacturer> loadedModelRoots = persistenceFXToLoad.getModelRoots();
  assertThat(loadedModelRoots).hasSize(1);
  final Manufacturer loadedManufacturer = loadedModelRoots.get(0);
  assertThat(loadedManufacturer.getName()).isEqualTo(manufacturerToSave1.getName());

  loadedManufacturer.setName("new name");

  final CascadePersistJPAProvider<Manufacturer> cutToLoad2 = new CascadePersistJPAProvider<>("test",
    Manufacturer.class);
  final PersistenceFX<Manufacturer> persistenceFXToLoad2 = PersistenceFX
    .withPersistenceProvider(cutToLoad2)
    .autoCommit()
    .build();
  final ObservableList<Manufacturer> loadedModelRoots2 = persistenceFXToLoad2.getModelRoots();
  assertThat(loadedModelRoots2).hasSize(1);
  final Manufacturer loadedManufacturer2 = loadedModelRoots2.get(0);
  assertThat(loadedManufacturer2.getName()).isEqualTo("new name");
 }

}
