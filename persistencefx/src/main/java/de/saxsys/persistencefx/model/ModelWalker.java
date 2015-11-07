package de.saxsys.persistencefx.model;

import java.util.Arrays;

import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;

public class ModelWalker {

 public void walkModel(final Object model, final ModelListener modelListener) {
  Arrays.stream(model.getClass().getDeclaredFields()).forEach(field -> {
   try {
    field.setAccessible(true);
    final Object fieldInst = field.get(model);
    if (fieldInst instanceof StringProperty) {
     ((StringProperty) fieldInst).addListener(
       (ChangeListener<String>) (observable, oldValue, newValue) -> modelListener.propertyChanged(model, fieldInst));
    }
   } catch (final Exception e) {
    e.printStackTrace();
   }
  });
 }

}
