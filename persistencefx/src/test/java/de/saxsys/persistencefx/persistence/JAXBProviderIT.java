package de.saxsys.persistencefx.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.bind.JAXBException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.saxsys.persistencefx.PersistenceFX;

public class JAXBProviderIT {

  @Rule
  public final TemporaryFolder temporaryFolder = new TemporaryFolder();

  @Test
  public void jaxbModelShouldBeSavedAndLoadedAutomaticallyOnAutocommit() throws IOException, JAXBException {
    final Path testFile = temporaryFolder.getRoot().toPath().resolve("testfile.xml");

    final PersistenceFX<JaxbModel> persistenceFx = PersistenceFX
        .withPersistenceProvider(new JAXBProvider<>(testFile, new JaxbModel())).autoCommit().build();
    final JaxbModel initialModel = persistenceFx.getModel();

    initialModel.setStringProp("new");
    initialModel.getObslist().add("val1");
    initialModel.getObslist().add("val2");

    // check
    PersistenceFX.withPersistenceProvider(new JAXBProvider<>(testFile, new JaxbModel())).autoCommit().build();

    final JaxbModel loadedModel = persistenceFx.getModel();

    assertThat(loadedModel.getStringProp()).isEqualTo("new");
    assertThat(loadedModel.getObslist()).containsExactly("val1", "val2");
  }

  @Test
  public void jaxbModelShouldNotBeSavedAndLoadedAutomaticallyWhenAutocommitIsOff() throws IOException, JAXBException {
    final Path testFile = temporaryFolder.getRoot().toPath().resolve("testfile.xml");

    final PersistenceFX<JaxbModel> persistenceFx = PersistenceFX
        .withPersistenceProvider(new JAXBProvider<>(testFile, new JaxbModel())).build();
    final JaxbModel initialModel = persistenceFx.getModel();

    initialModel.setStringProp("new");
    initialModel.getObslist().add("val1");
    initialModel.getObslist().add("val2");

    assertThat(testFile).doesNotExist();

    persistenceFx.commit();

    assertThat(testFile).exists();

    PersistenceFX.withPersistenceProvider(new JAXBProvider<>(testFile, new JaxbModel())).autoCommit().build();

    final JaxbModel loadedModel = persistenceFx.getModel();

    assertThat(loadedModel.getStringProp()).isEqualTo("new");
    assertThat(loadedModel.getObslist()).containsExactly("val1", "val2");
  }
}
