package de.saxsys.persistencefx.error;

import org.junit.Test;

/**
 * Test for {@link DefaultErrorHandler}.
 */
public class DefaultErrorHandlerTest {

  @Test(expected = RuntimeException.class)
  public void shouldRethrowException() {
    new DefaultErrorHandler().error("error", new Exception("error"));
  }

}
