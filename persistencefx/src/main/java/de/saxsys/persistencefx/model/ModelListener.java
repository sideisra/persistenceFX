package de.saxsys.persistencefx.model;

import java.lang.reflect.Field;
import java.util.List;

public interface ModelListener<ModelRootType> {
  void propertyChanged(Object containingModelEntity);

  void listContentChanged(Object containingModelEntity, Field changedList, List<?> added, List<?> removed);

  void modelRootListChanged(List<ModelRootType> added, List<ModelRootType> removed);
}
