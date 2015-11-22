package de.saxsys.persistencefx.jpa;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.saxsys.persistencefx.persistence.PersistenceProvider;

/**
 * Implementation of {@link PersistenceProvider} for persisting the model with
 * JPA. Cascade policy of all One-To-One, One-To-Many and Many-To-Many
 * relationships have to set to {@link CascadeType.PERSIST}.
 */
public class CascadePersistJPAProvider<ModelType> implements PersistenceProvider<ModelType> {

  private static final Logger LOG = LoggerFactory.getLogger(CascadePersistJPAProvider.class);

  private final EntityManagerFactory factory;
  private EntityManager em;
  private final Class<ModelType> modelTypeClass;

  public CascadePersistJPAProvider(final String persistenceUnitName, final Class<ModelType> modelTypeClass) {
    factory = Persistence.createEntityManagerFactory(persistenceUnitName);
    this.modelTypeClass = modelTypeClass;
  }

  @Override
  public List<ModelType> load() {
    em = factory.createEntityManager();
    em.getTransaction().begin();
    try {
      final String query = "select m from " + modelTypeClass.getSimpleName() + " m";
      final Query q = em.createQuery(query);
      final List<ModelType> manufacturer = q.getResultList();
      return manufacturer;
    } finally {
      em.getTransaction().commit();
    }
  }

  public void close() {
    em.close();
  }

  @Override
  public void propertyChanged(final Object containingModelEntity) {
    persist(containingModelEntity);
  }

  @Override
  public void listContentChanged(final Object containingModelEntity, final Field changedList, final List<?> added,
      final List<?> removed) {
    persist(containingModelEntity);
  }

  private void persist(final Object containingModelEntity) {
    inTransaction(em -> {
      if (em.contains(containingModelEntity)) {
        em.merge(containingModelEntity);
      } else {
        em.persist(containingModelEntity);
      }
    });
  }

  private void remove(final Object containingModelEntity) {
    inTransaction(em -> {
      em.remove(em.merge(containingModelEntity));
    });
  }

  private void inTransaction(final Consumer<EntityManager> command) {
    if (em.getTransaction().isActive()) {
      LOG.debug("Ignoring event as transaction is already active.");
    } else {
      em.getTransaction().begin();
      try {
        command.accept(em);
        em.getTransaction().commit();
      } catch (final Exception e) {
        LOG.error("", e);
        em.getTransaction().rollback();
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void modelRootListChanged(final List<?> added, final List<?> removed) {
    added.forEach(this::persist);
    removed.forEach(this::remove);
  }
}
