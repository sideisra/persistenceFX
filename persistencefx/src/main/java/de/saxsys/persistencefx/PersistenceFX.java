package de.saxsys.persistencefx;

import java.lang.reflect.Field;
import java.util.List;

import de.saxsys.persistencefx.error.BuildException;
import de.saxsys.persistencefx.error.DefaultErrorHandler;
import de.saxsys.persistencefx.error.ErrorHandler;
import de.saxsys.persistencefx.model.ModelListener;
import de.saxsys.persistencefx.model.ModelWalker;
import de.saxsys.persistencefx.persistence.PersistenceProvider;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Central class for handling model persistence.
 */
public class PersistenceFX<ModelRootType> implements ModelListener<ModelRootType> {

  private final PersistenceProvider<ModelRootType> persistenceProvider;
  private final BooleanProperty autoCommit = new SimpleBooleanProperty();
  private final ListProperty<Runnable> events = new SimpleListProperty<>(FXCollections.observableArrayList());
  private final ReadOnlyBooleanWrapper dirty = new ReadOnlyBooleanWrapper();
  private final ObjectProperty<ErrorHandler<ModelRootType>> errorHandler = new SimpleObjectProperty<>(
      new DefaultErrorHandler<>());

  private final ObservableList<ModelRootType> modelRoots = FXCollections.observableArrayList();

  public PersistenceFX(final PersistenceProvider<ModelRootType> persistenceProvider) {
    this.persistenceProvider = persistenceProvider;
    dirty.bind(events.emptyProperty().not());
  }

  public static <ModelType> FluentBuilder<ModelType> withPersistenceProvider(
      final PersistenceProvider<ModelType> persistenceProvider) {
    return new FluentBuilder<ModelType>(persistenceProvider);
  }

  public BooleanProperty autoCommitProperty() {
    return this.autoCommit;
  }

  public boolean isAutoCommit() {
    return this.autoCommitProperty().get();
  }

  public void setAutoCommit(final boolean autoCommit) {
    this.autoCommitProperty().set(autoCommit);
  }

  private void initModel() {
    final List<ModelRootType> loadedModelRoots = persistenceProvider.load();
    if (loadedModelRoots == null) {
      throw new BuildException("given persistence provider has to supply initial model roots on load.");
    }
    modelRoots.setAll(loadedModelRoots);
    new ModelWalker<ModelRootType>().walkModelRoots(modelRoots, this);
    autoCommit.addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
      if (newValue) {
        commit();
      }
    });
  }

  public ObservableList<ModelRootType> getModelRoots() {
    return modelRoots;
  }

  public static class FluentBuilder<ModelRootType> {

    private final PersistenceFX<ModelRootType> persistenceFX;

    public FluentBuilder(final PersistenceProvider<ModelRootType> persistenceProvider) {
      this.persistenceFX = new PersistenceFX<ModelRootType>(persistenceProvider);
    }

    public FluentBuilder<ModelRootType> autoCommit() {
      persistenceFX.setAutoCommit(true);
      return this;
    }

    public PersistenceFX<ModelRootType> build() {
      persistenceFX.initModel();
      return persistenceFX;
    }

    public FluentBuilder<ModelRootType> errorHandler(final ErrorHandler<ModelRootType> errorHandler) {
      persistenceFX.errorHandler.set(errorHandler);
      return this;
    }

  }

  @Override
  public void propertyChanged(final Object containingModelEntity) {
    suspendOrCommit(containingModelEntity,
        () -> persistenceProvider.propertyChanged(containingModelEntity));
  }

  @Override
  public void listContentChanged(final Object containingModelEntity, final Field changedList, final List<?> added,
      final List<?> removed) {
    suspendOrCommit(containingModelEntity,
        () -> persistenceProvider.listContentChanged(containingModelEntity, changedList, added, removed));
  }

  private void suspendOrCommit(final Object containingModelEntity, final Runnable event) {
    if (autoCommit.get()) {
      withErrorHandler(containingModelEntity, event);
    } else {
      events.add(() -> withErrorHandler(containingModelEntity, event));
    }
  }

  private void withErrorHandler(final Object modelEntity, final Runnable event) {
    try {
      event.run();
    } catch (final Exception saveEx) {
      errorHandler.get().error(modelEntity, saveEx);
    }
  }

  public void commit() {
    events.forEach(Runnable::run);
    events.clear();
  }

  public final ObjectProperty<ErrorHandler<ModelRootType>> errorHandlerProperty() {
    return this.errorHandler;
  }

  public final ErrorHandler<ModelRootType> getErrorHandler() {
    return this.errorHandlerProperty().get();
  }

  public final void setErrorHandler(final ErrorHandler<ModelRootType> errorHandler) {
    this.errorHandlerProperty().set(errorHandler);
  }

  @Override
  public void modelRootListChanged(final List<ModelRootType> added, final List<ModelRootType> removed) {
    if (autoCommit.get()) {
      try {
        persistenceProvider.modelRootListChanged(added, removed);
      } catch (final Exception saveEx) {
        errorHandler.get().rootModelListError(added, removed, saveEx);
      }
    } else {
      events.add(() -> {
        try {
          persistenceProvider.modelRootListChanged(added, removed);
        } catch (final Exception saveEx) {
          errorHandler.get().rootModelListError(added, removed, saveEx);
        }
      });
    }
  }

  public ReadOnlyBooleanProperty dirtyProperty() {
    return dirty.getReadOnlyProperty();
  }

  public final boolean isDirty() {
    return dirty.get();
  }

}
