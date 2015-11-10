package de.saxsys.persistencefx.error;

import de.saxsys.persistencefx.PersistenceFX;

/**
 * Indicates an error while building {@link PersistenceFX} instance.
 */
public class BuildException extends RuntimeException {

  /**
   * 
   */
  private static final long serialVersionUID = 8276920320899188084L;

  public BuildException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public BuildException(final String message) {
    super(message);
  }

  public BuildException(final Throwable cause) {
    super(cause);
  }

}
