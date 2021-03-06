package de.saxsys.persistencefx.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ModelWalker<ModelRootType> {

  public void walkModelRoots(final ObservableList<ModelRootType> modelRoots,
      final ModelListener<ModelRootType> modelListener) {
    modelRoots.forEach(modelRoot -> walkModel(modelRoot, modelListener));
    modelRoots.addListener((ListChangeListener<ModelRootType>) c -> {
      while (c.next()) {
        final List<ModelRootType> added = c.wasAdded() ? new ArrayList<>(c.getAddedSubList()) : Collections.emptyList();
        added.forEach(newModelRoot -> walkModel(newModelRoot, modelListener));
        final List<ModelRootType> removed = c.wasRemoved() ? new ArrayList<>(c.getRemoved()) : Collections.emptyList();
        modelListener.modelRootListChanged(added, removed);
      }
    });
  }

  private void walkModel(final Object model, final ModelListener<ModelRootType> modelListener) {
    final List<Object> nonProps = new LinkedList<>();
    final BooleanProperty propsFound = new SimpleBooleanProperty();
    Arrays.stream(model.getClass().getDeclaredFields()).forEach(field -> {
      try {
        field.setAccessible(true);
        final Object fieldInst = field.get(model);
        if (fieldInst != null) {
          boolean isObservable = false;
          if (fieldInst instanceof ObservableValue) {
            listenToObservableValue(model, modelListener, fieldInst);
            propsFound.set(true);
            isObservable = true;
          }
          if (fieldInst instanceof ObservableList) {
            listenToObservableList(model, modelListener, field, fieldInst);
            propsFound.set(true);
            isObservable = true;
          }
          if (!isObservable) {
            nonProps.add(fieldInst);
          }
        }
      } catch (final Exception escanEx) {
        throw new RuntimeException("Error while scanning model.", escanEx);
      }
    });
    // we assume that a class without properties marks the border of the model
    // so no further decent if no prop was found
    if (propsFound.get()) {
      nonProps.forEach(nonProp -> walkModel(nonProp, modelListener));
    }
  }

  @SuppressWarnings("unchecked")
  private void listenToObservableList(final Object model, final ModelListener<ModelRootType> modelListener,
      final Field field,
      final Object fieldInst) {
    final ObservableList<? extends Object> obsList = (ObservableList<? extends Object>) fieldInst;
    obsList.addListener((ListChangeListener<? super Object>) (c) -> {
      while (c.next()) {
        final List<?> added = c.wasAdded() ? new ArrayList<>(c.getAddedSubList()) : Collections.emptyList();
        added.forEach(addedEntry -> walkModel(addedEntry, modelListener));
        final List<?> removed = c.wasRemoved() ? new ArrayList<>(c.getRemoved()) : Collections.emptyList();
        modelListener.listContentChanged(model, field, added, removed);
      }
    });
    obsList.forEach(entry -> walkModel(entry, modelListener));
  }

  @SuppressWarnings("unchecked")
  private void listenToObservableValue(final Object model, final ModelListener<ModelRootType> modelListener,
      final Object fieldInst) {
    ((ObservableValue<? extends Object>) fieldInst).addListener(
        (ChangeListener<? super Object>) (observable, oldValue, newValue) -> {
          if (oldValue != newValue) {
            // list properties fire a change event when list content is changed
            // but lists are the same to we ignore changes without change
            modelListener.propertyChanged(model);
          }
        });
  }

}
