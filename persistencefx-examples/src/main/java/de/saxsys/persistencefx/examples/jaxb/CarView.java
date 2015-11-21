package de.saxsys.persistencefx.examples.jaxb;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.bind.JAXBException;

import de.saxsys.persistencefx.PersistenceFX;
import de.saxsys.persistencefx.jaxb.JAXBProvider;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;

public class CarView {
  private static final Path XML_PATH = Paths.get("target", "manufacturers.xml");

  @FXML
  private ListView<Manufacturer> lvManufacturer;

  @FXML
  private Button btnAddManufacturer;

  @FXML
  private Button btnRemoveManufacturer;

  @FXML
  private ListView<Car> lvCars;

  @FXML
  private Button btnAddCar;

  @FXML
  private Button btnRemove;

  @FXML
  private TextField txtNewManufacturerName;
  @FXML
  private TextField txtNewCarName;

  private PersistenceFX<ManufacturerList> persistenceFx;

  private final ListProperty<Manufacturer> manufacturers = new SimpleListProperty<>(
      FXCollections.observableArrayList());

  @FXML
  private void initialize() throws JAXBException {
    XML_PATH.getParent().toFile().mkdirs();
    lvCars.setCellFactory(param -> new TextFieldListCell<Car>(new StringConverter<Car>() {

      @Override
      public String toString(final Car object) {
        return object.getName();
      }

      @Override
      public Car fromString(final String string) {
        throw new RuntimeException("should not happen");
      }

    }));
    lvManufacturer.setCellFactory(param -> new TextFieldListCell<Manufacturer>(new StringConverter<Manufacturer>() {

      @Override
      public String toString(final Manufacturer object) {
        return object.getName();
      }

      @Override
      public Manufacturer fromString(final String string) {
        throw new RuntimeException("should not happen");
      }
    }));
    lvManufacturer.setItems(manufacturers);
    lvManufacturer.getSelectionModel().selectedItemProperty()
        .addListener((ChangeListener<Manufacturer>) (observable, oldValue, newValue) -> {
          lvCars.setItems(newValue.getCars());
        });
    final JAXBProvider<ManufacturerList> persistenceProvider = new JAXBProvider<>(XML_PATH, new ManufacturerList());
    persistenceFx = PersistenceFX
        .withPersistenceProvider(persistenceProvider)
        .autoCommit()
        .build();
    manufacturers.set(persistenceFx.getModelRoots().get(0).getManufacturers());
  }

  @FXML
  void addCar(final ActionEvent event) {
    final String newCarName = txtNewCarName.getText();
    if (newCarName != null && !newCarName.isEmpty()) {
      final Manufacturer selectedManufacturer = lvManufacturer.getSelectionModel().getSelectedItem();
      if (selectedManufacturer != null) {
        selectedManufacturer.getCars().add(new Car(newCarName));
      }
    }
  }

  @FXML
  void removeCar(final ActionEvent event) {
    final Manufacturer selectedManufacturer = lvManufacturer.getSelectionModel().getSelectedItem();
    final Car selectedCar = lvCars.getSelectionModel().getSelectedItem();
    if (selectedManufacturer != null && selectedCar != null) {
      selectedManufacturer.getCars().remove(selectedCar);
    }
  }

  @FXML
  void addManufacturer(final ActionEvent event) {
    final String newManufacturerName = txtNewManufacturerName.getText();
    if (newManufacturerName != null && !newManufacturerName.isEmpty()) {
      final Manufacturer newManufacturer = new Manufacturer(newManufacturerName);
      manufacturers.add(newManufacturer);
    }
  }

  @FXML
  void removeManufacturer(final ActionEvent event) {
    final Manufacturer selectedItem = lvManufacturer.getSelectionModel().getSelectedItem();
    if (selectedItem != null) {
      manufacturers.remove(selectedItem);
    }
  }

}
