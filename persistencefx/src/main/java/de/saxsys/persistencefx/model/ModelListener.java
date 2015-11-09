package de.saxsys.persistencefx.model;

import java.lang.reflect.Field;
import java.util.List;

public interface ModelListener {
	void propertyChanged(Object containingModelEntity);

	void listContentChanged(Object containingModelEntity, Field changedList, List<?> added, List<?> removed);

}
