package de.saxsys.persistencefx.error;

import java.util.Collection;

/**
 * Default implementation of {@link ErrorHandler} that just rethrows all
 * exceptions.
 */
public class DefaultErrorHandler<ModelRootType> implements ErrorHandler<ModelRootType> {

  @Override
  public void error(final Object modelEntity, final Exception error) {
    throw new RuntimeException("Error while saving " + modelEntity, error);
  }

  @Override
  public void rootModelListError(final Collection<ModelRootType> added, final Collection<ModelRootType> removed,
      final Exception error) {
    throw new RuntimeException("Error while saving root model changes. added: " + added + " - removed: " + removed,
        error);
  }

}
