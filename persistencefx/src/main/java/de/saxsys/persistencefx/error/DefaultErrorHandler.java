package de.saxsys.persistencefx.error;

/**
 * Default implementation of {@link ErrorHandler} that just rethrows all
 * exceptions.
 */
public class DefaultErrorHandler implements ErrorHandler {

  @Override
  public void error(final Object modelEntity, final Exception error) {
    throw new RuntimeException("Error while saving " + modelEntity, error);
  }

}
