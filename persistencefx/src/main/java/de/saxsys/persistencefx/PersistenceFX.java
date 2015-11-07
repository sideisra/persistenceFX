package de.saxsys.persistencefx;

import de.saxsys.persistencefx.persistence.ModelPersister;

/**
 * Central class for handling model persistence.
 */
public class PersistenceFX<ModelType> {

 private final ModelPersister<ModelType> persister;
 private boolean autoCommit;

 public PersistenceFX(final ModelPersister<ModelType> persister) {
  super();
  this.persister = persister;
 }

 public static <ModelType> FluentBuilder<ModelType> withPersister(final ModelPersister<ModelType> persister) {
  return new FluentBuilder<ModelType>(persister);
 }

 public boolean isAutoCommit() {
  return autoCommit;
 }

 public void setAutoCommit(final boolean autoCommit) {
  this.autoCommit = autoCommit;
 }

 public ModelType load() {
  return persister.load();
 }

 public static class FluentBuilder<ModelType> {

  private final PersistenceFX<ModelType> persistenceFX;

  public FluentBuilder(final ModelPersister<ModelType> persister) {
   this.persistenceFX = new PersistenceFX<ModelType>(persister);
  }

  public FluentBuilder<ModelType> autoCommit() {
   persistenceFX.setAutoCommit(true);
   return this;
  }

  public PersistenceFX<ModelType> build() {
   return persistenceFX;
  }

 }

}
