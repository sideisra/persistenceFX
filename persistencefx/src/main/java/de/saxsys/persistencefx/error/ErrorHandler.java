package de.saxsys.persistencefx.error;

import java.util.Collection;

import de.saxsys.persistencefx.persistence.PersistenceProvider;

/**
 * Handles errors raised from {@link PersistenceProvider} while saving model
 * state.
 * 
 * @param <ModelRootType>
 */
public interface ErrorHandler<ModelRootType> {
  void error(Object modelEntity, Exception error);

  void rootModelListError(Collection<ModelRootType> added, Collection<ModelRootType> removed, Exception error);
}
