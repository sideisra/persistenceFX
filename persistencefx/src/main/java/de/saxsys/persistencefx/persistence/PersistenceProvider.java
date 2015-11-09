package de.saxsys.persistencefx.persistence;

import java.lang.reflect.Field;
import java.util.List;

public interface PersistenceProvider<ModelType> {

 ModelType load();

 void propertyChanged(Object containingModelEntity);

 void listContentChanged(Object containingModelEntity, Field changedList, List<?> added, List<?> removed);

}
