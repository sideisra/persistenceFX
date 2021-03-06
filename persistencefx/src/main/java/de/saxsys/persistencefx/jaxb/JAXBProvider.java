package de.saxsys.persistencefx.jaxb;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import de.saxsys.persistencefx.persistence.PersistenceProvider;

/**
 * Implementation of {@link PersistenceProvider} for persisting the model with
 * JAXB.
 */
public class JAXBProvider<ModelRootType> implements PersistenceProvider<ModelRootType> {

  private final JAXBContext context;
  private final Path xmlPath;
  private ModelRootType model;

  public JAXBProvider(final Path xmlPath, final ModelRootType initialModel) throws JAXBException {
    this.xmlPath = xmlPath;
    context = JAXBContext.newInstance(initialModel.getClass());
    model = initialModel;
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<ModelRootType> load() {
    try {
      if (Files.exists(xmlPath)) {
        model = (ModelRootType) context.createUnmarshaller().unmarshal(xmlPath.toFile());
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

  @Override
  public void modelRootListChanged(final List<?> added, final List<?> removed) {
    throw new UnsupportedOperationException("Management of multiple root model object is not supported at the moment.");
  }
}
