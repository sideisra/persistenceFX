package de.saxsys.persistencefx.persistence;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * Implementation of {@link PersistenceProvider} for persisting the model with
 * JAXB.
 */
public class JAXBProvider<ModelType> implements PersistenceProvider<ModelType> {

 private final JAXBContext context;
 private final Path xmlPath;
 private ModelType model;

 public JAXBProvider(final Path xmlPath, final ModelType initialModel) throws JAXBException {
  super();
  this.xmlPath = xmlPath;
  context = JAXBContext.newInstance(initialModel.getClass());
  model = initialModel;
 }

 @Override
 public List<ModelType> load() {
  try {
   if (Files.exists(xmlPath)) {
    model = (ModelType) context.createUnmarshaller().unmarshal(xmlPath.toFile());
   }
   return Collections.singletonList(model);
  } catch (final JAXBException loadEx) {
   throw new RuntimeException(loadEx);
  }
 }

 @Override
 public void propertyChanged(final Object containingModelEntity) {
  save();
 }

 @Override
 public void listContentChanged(final Object containingModelEntity, final Field changedList, final List<?> added,
   final List<?> removed) {
  save();
 }

 private void save() {
  try {
   context.createMarshaller().marshal(model, xmlPath.toFile());
  } catch (final JAXBException loadEx) {
   throw new RuntimeException(loadEx);
  }
 }
}
