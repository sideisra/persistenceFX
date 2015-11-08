package de.saxsys.persistencefx.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class ModelWalker {

 @SuppressWarnings("unchecked")
 public void walkModel(final Object model, final ModelListener modelListener) {
  Arrays.stream(model.getClass().getDeclaredFields()).forEach(field -> {
   try {
    field.setAccessible(true);
    final Object fieldInst = field.get(model);
    if (fieldInst instanceof ObservableValue) {
     ((ObservableValue<? extends Object>) fieldInst).addListener(
       (ChangeListener<? super Object>) (observable, oldValue, newValue) -> modelListener.propertyChanged(model,
         (ObservableValue<? extends Object>) fieldInst));
    } else if (fieldInst instanceof ObservableList) {
     ((ObservableList<? extends Object>) fieldInst).addListener(
       (ListChangeListener<? super Object>) (c) -> {
      while (c.next()) {
       final List<?> added = c.wasAdded() ? new ArrayList<>(c.getAddedSubList()) : Collections.emptyList();
       final List<?> removed = c.wasRemoved() ? new ArrayList<>(c.getRemoved()) : Collections.emptyList();
       modelListener.listContentChanged(model, (ObservableList<? extends Object>) fieldInst, added, removed);
      }
     });
    } else {
     walkModel(fieldInst, modelListener);
    }
   } catch (final Exception escanEx) {
    throw new RuntimeException("Error while scanning model.", escanEx);
   }
  });
 }

}
