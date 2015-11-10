package de.saxsys.persistencefx.error;

import de.saxsys.persistencefx.persistence.PersistenceProvider;

/**
 * Handles errors raised from {@link PersistenceProvider} while saving model
 * state.
 */
public interface ErrorHandler {
  void error(Object modelEntity, Exception error);
}
