package de.saxsys.persistencefx.jpa;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.Consumer;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import de.saxsys.persistencefx.persistence.PersistenceProvider;

/**
 * Implementation of {@link PersistenceProvider} for persisting the model with
 * JPA. Cascade policy of all One-To-One, One-To-Many and Many-To-Many
 * relationships have to set to {@link CascadeType.PERSIST}.
 */
public class CascadePersistJPAProvider<ModelType> implements PersistenceProvider<ModelType> {

 private final EntityManagerFactory factory;
 private final Class<ModelType> modelTypeClass;

 public CascadePersistJPAProvider(final String persistenceUnitName, final Class<ModelType> modelTypeClass) {
  factory = Persistence.createEntityManagerFactory(persistenceUnitName);
  this.modelTypeClass = modelTypeClass;
 }

 @Override
 public List<ModelType> load() {
  final EntityManager em = factory.createEntityManager();
  try {
   final String query = "select m from " + modelTypeClass.getName() + " m";
   System.out.println(query);
   final Query q = em.createQuery(query);
   final List<ModelType> manufacturer = q.getResultList();
   return manufacturer;
  } finally {
   em.close();
  }
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

 public void persist(final Object containingModelEntity) {
  inTransaction(em -> em.persist(containingModelEntity));
 }

 public void deleteManufacturer(final Object containingModelEntity) {
  inTransaction(em -> em.remove(em.merge(containingModelEntity)));
 }

 private void inTransaction(final Consumer<EntityManager> command) {
  final EntityManager em = factory.createEntityManager();
  try {
   em.getTransaction().begin();
   command.accept(em);
   em.getTransaction().commit();
  } finally {
   em.close();
  }
 }

 @Override
 public void modelRootListChanged(final List<?> added, final List<?> removed) {
  // TODO Auto-generated method stub

 }
}
