package de.saxsys.persistencefx.error;

import java.util.Collections;

import org.junit.Test;

/**
 * Test for {@link DefaultErrorHandler}.
 */
public class DefaultErrorHandlerTest {

  @Test(expected = RuntimeException.class)
  public void shouldRethrowExceptionOnError() {
    new DefaultErrorHandler<Object>().error("error", new Exception("error"));
  }

  @Test(expected = RuntimeException.class)
  public void shouldRethrowExceptionOnRootModelListError() {
    new DefaultErrorHandler<Object>().rootModelListError(Collections.emptyList(), Collections.emptyList(),
        new Exception("error"));
  }
}
