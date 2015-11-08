package de.saxsys.persistencefx.model;

import java.util.List;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

public interface ModelListener {
 void propertyChanged(Object containingModelEntity, ObservableValue<?> changedProperty);

 void listContentChanged(Object containingModelEntity, ObservableList<?> changedList, List<?> added, List<?> removed);

}
