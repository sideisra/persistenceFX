package de.saxsys.persistencefx.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Path;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import de.saxsys.persistencefx.jaxb.JAXBProvider;

public class JAXBProviderTest {

 @Rule
 public final TemporaryFolder temporaryFolder = new TemporaryFolder();

 @Test
 public void whenXmlFileDoesNotExistsReturnInitialModel() throws IOException, JAXBException {
  final Path testFile = temporaryFolder.getRoot().toPath().resolve("testfile.xml");
  final JaxbModel initialModel = new JaxbModel();
  final JAXBProvider<JaxbModel> cut = new JAXBProvider<JaxbModel>(testFile, initialModel);

  assertThat(cut.load().get(0)).isSameAs(initialModel);
 }

 @Test
 public void modelShouldBeLoadedFromXmlFile() throws IOException, JAXBException {
  final Path testFile = temporaryFolder.newFile("testfile.xml").toPath();

  final JaxbModel testModel = new JaxbModel();
  testModel.setStringProp("myStringProp");
  testModel.getObslist().addAll("val1", "val2");
  JAXBContext.newInstance(JaxbModel.class).createMarshaller().marshal(testModel, testFile.toFile());

  final JAXBProvider<JaxbModel> cut = new JAXBProvider<JaxbModel>(testFile, new JaxbModel());

  final JaxbModel loadedModel = cut.load().get(0);
  assertThat(loadedModel.getStringProp()).isEqualTo(testModel.getStringProp());
  assertThat(loadedModel.getObslist()).containsExactlyElementsOf(testModel.getObslist());
 }

 @Test
 public void modelShouldBeSavedToXmlFileOnPropChangeEvent() throws IOException, JAXBException {
  final Path testFile = temporaryFolder.newFile("testfile.xml").toPath();

  final JaxbModel testModel = new JaxbModel();
  testModel.setStringProp("myStringProp");
  testModel.getObslist().addAll("val1", "val2");
  final JAXBProvider<JaxbModel> cut = new JAXBProvider<JaxbModel>(testFile, testModel);

  cut.propertyChanged(null);

  final JaxbModel savedModel = (JaxbModel) JAXBContext.newInstance(JaxbModel.class).createUnmarshaller()
    .unmarshal(testFile.toFile());
  assertThat(savedModel.getStringProp()).isEqualTo(testModel.getStringProp());
  assertThat(savedModel.getObslist()).containsExactlyElementsOf(testModel.getObslist());
 }

 @Test
 public void modelShouldBeSavedToXmlFileOnListChangeEvent() throws IOException, JAXBException {
  final Path testFile = temporaryFolder.newFile("testfile.xml").toPath();

  final JaxbModel testModel = new JaxbModel();
  testModel.setStringProp("myStringProp");
  testModel.getObslist().addAll("val1", "val2");
  final JAXBProvider<JaxbModel> cut = new JAXBProvider<JaxbModel>(testFile, testModel);

  cut.listContentChanged(null, null, null, null);

  final JaxbModel savedModel = (JaxbModel) JAXBContext.newInstance(JaxbModel.class).createUnmarshaller()
    .unmarshal(testFile.toFile());
  assertThat(savedModel.getStringProp()).isEqualTo(testModel.getStringProp());
  assertThat(savedModel.getObslist()).containsExactlyElementsOf(testModel.getObslist());
 }
}
