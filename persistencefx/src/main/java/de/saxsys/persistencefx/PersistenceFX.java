package de.saxsys.persistencefx;

import java.lang.reflect.Field;
import java.util.List;

import de.saxsys.persistencefx.model.ModelListener;
import de.saxsys.persistencefx.model.ModelWalker;
import de.saxsys.persistencefx.persistence.PersistenceProvider;

/**
 * Central class for handling model persistence.
 */
public class PersistenceFX<ModelType> implements ModelListener {

 private final PersistenceProvider<ModelType> persistenceProvider;
 private boolean autoCommit;

 private ModelType model;

 public PersistenceFX(final PersistenceProvider<ModelType> persistenceProvider) {
  super();
  this.persistenceProvider = persistenceProvider;
 }

 public static <ModelType> FluentBuilder<ModelType> withPersistenceProvider(final PersistenceProvider<ModelType> persistenceProvider) {
  return new FluentBuilder<ModelType>(persistenceProvider);
 }

 public boolean isAutoCommit() {
  return autoCommit;
 }

 public void setAutoCommit(final boolean autoCommit) {
  this.autoCommit = autoCommit;
 }

 private void initModel() {
  model = persistenceProvider.load();
  new ModelWalker().walkModel(model, this);
 }

 public ModelType getModel() {
  return model;
 }

 public static class FluentBuilder<ModelType> {

  private final PersistenceFX<ModelType> persistenceFX;

  public FluentBuilder(final PersistenceProvider<ModelType> persistenceProvider) {
   this.persistenceFX = new PersistenceFX<ModelType>(persistenceProvider);
  }

  public FluentBuilder<ModelType> autoCommit() {
   persistenceFX.setAutoCommit(true);
   return this;
  }

  public PersistenceFX<ModelType> build() {
   persistenceFX.initModel();
   return persistenceFX;
  }

 }

 @Override
 public void propertyChanged(final Object containingModelEntity) {
  persistenceProvider.propertyChanged(containingModelEntity);
 }

 @Override
 public void listContentChanged(final Object containingModelEntity, final Field changedList, final List<?> added,
   final List<?> removed) {
  persistenceProvider.listContentChanged(containingModelEntity, changedList, added, removed);
 }

}
