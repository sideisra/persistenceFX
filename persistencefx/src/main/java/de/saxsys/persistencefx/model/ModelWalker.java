package de.saxsys.persistencefx.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ModelWalker {

 public void walkModel(final Object model, final ModelListener modelListener) {
  Arrays.stream(model.getClass().getDeclaredFields()).forEach(field -> {
   try {
    field.setAccessible(true);
    final Object fieldInst = field.get(model);
    if (fieldInst instanceof ObservableValue) {
     listenToObservableValue(model, modelListener, fieldInst);
    } else if (fieldInst instanceof ObservableList) {
     listenToObservableList(model, modelListener, field, fieldInst);
    } else {
     walkModel(fieldInst, modelListener);
    }
   } catch (final Exception escanEx) {
    throw new RuntimeException("Error while scanning model.", escanEx);
   }
  });
 }

 @SuppressWarnings("unchecked")
 private void listenToObservableList(final Object model, final ModelListener modelListener, final Field field,
   final Object fieldInst) {
  ((ObservableList<? extends Object>) fieldInst).addListener((ListChangeListener<? super Object>) (c) -> {
   while (c.next()) {
    final List<?> added = c.wasAdded() ? new ArrayList<>(c.getAddedSubList()) : Collections.emptyList();
    final List<?> removed = c.wasRemoved() ? new ArrayList<>(c.getRemoved()) : Collections.emptyList();
    modelListener.listContentChanged(model, field, added, removed);
   }
  });
 }

 @SuppressWarnings("unchecked")
 private void listenToObservableValue(final Object model, final ModelListener modelListener, final Object fieldInst) {
  ((ObservableValue<? extends Object>) fieldInst).addListener(
    (ChangeListener<? super Object>) (observable, oldValue, newValue) -> modelListener.propertyChanged(model));
 }

}
