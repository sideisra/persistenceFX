package de.saxsys.persistencefx.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import de.saxsys.persistencefx.model.TestModel.TestObsList;
import de.saxsys.persistencefx.model.TestModel.TestObsValue;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class TestModel {
 public static class TestObsValue implements ObservableValue<String> {
 
  private final StringProperty value = new SimpleStringProperty();
 
  public String get() {
   return value.get();
  }
 
  public void set(final String value) {
   this.value.set(value);
  }
 
  @Override
  public String getValue() {
   return value.getValue();
  }
 
  @Override
  public void addListener(final ChangeListener<? super String> listener) {
   value.addListener(listener);
  }
 
  @Override
  public void removeListener(final ChangeListener<? super String> listener) {
   value.removeListener(listener);
  }
 
  @Override
  public void addListener(final InvalidationListener listener) {
   value.addListener(listener);
  }
 
  @Override
  public void removeListener(final InvalidationListener listener) {
   value.removeListener(listener);
  }
 
 }

 public static final class TestObsList implements ObservableList<String> {
 
  private final ObservableList<String> obsList = FXCollections.observableArrayList();
 
  @Override
  public void addListener(final ListChangeListener<? super String> listener) {
   obsList.addListener(listener);
  }
 
  @Override
  public void removeListener(final ListChangeListener<? super String> listener) {
   obsList.removeListener(listener);
  }
 
  @Override
  public void addListener(final InvalidationListener listener) {
   obsList.addListener(listener);
  }
 
  @Override
  public boolean addAll(final String... elements) {
   return obsList.addAll(elements);
  }
 
  @Override
  public boolean setAll(final String... elements) {
   return obsList.setAll(elements);
  }
 
  @Override
  public boolean setAll(final Collection<? extends String> col) {
   return obsList.setAll(col);
  }
 
  @Override
  public boolean removeAll(final String... elements) {
   return obsList.removeAll(elements);
  }
 
  @Override
  public void removeListener(final InvalidationListener listener) {
   obsList.removeListener(listener);
  }
 
  @Override
  public boolean retainAll(final String... elements) {
   return obsList.retainAll(elements);
  }
 
  @Override
  public void remove(final int from, final int to) {
   obsList.remove(from, to);
  }
 
  @Override
  public int size() {
   return obsList.size();
  }
 
  @Override
  public boolean isEmpty() {
   return obsList.isEmpty();
  }
 
  @Override
  public boolean contains(final Object o) {
   return obsList.contains(o);
  }
 
  @Override
  public Iterator<String> iterator() {
   return obsList.iterator();
  }
 
  @Override
  public Object[] toArray() {
   return obsList.toArray();
  }
 
  @Override
  public <T> T[] toArray(final T[] a) {
   return obsList.toArray(a);
  }
 
  @Override
  public boolean add(final String e) {
   return obsList.add(e);
  }
 
  @Override
  public boolean remove(final Object o) {
   return obsList.remove(o);
  }
 
  @Override
  public boolean containsAll(final Collection<?> c) {
   return obsList.containsAll(c);
  }
 
  @Override
  public boolean addAll(final Collection<? extends String> c) {
   return obsList.addAll(c);
  }
 
  @Override
  public boolean addAll(final int index, final Collection<? extends String> c) {
   return obsList.addAll(index, c);
  }
 
  @Override
  public boolean removeAll(final Collection<?> c) {
   return obsList.removeAll(c);
  }
 
  @Override
  public boolean retainAll(final Collection<?> c) {
   return obsList.retainAll(c);
  }
 
  @Override
  public void clear() {
   obsList.clear();
  }
 
  @Override
  public boolean equals(final Object o) {
   return obsList.equals(o);
  }
 
  @Override
  public int hashCode() {
   return obsList.hashCode();
  }
 
  @Override
  public String get(final int index) {
   return obsList.get(index);
  }
 
  @Override
  public String set(final int index, final String element) {
   return obsList.set(index, element);
  }
 
  @Override
  public void add(final int index, final String element) {
   obsList.add(index, element);
  }
 
  @Override
  public String remove(final int index) {
   return obsList.remove(index);
  }
 
  @Override
  public int indexOf(final Object o) {
   return obsList.indexOf(o);
  }
 
  @Override
  public int lastIndexOf(final Object o) {
   return obsList.lastIndexOf(o);
  }
 
  @Override
  public ListIterator<String> listIterator() {
   return obsList.listIterator();
  }
 
  @Override
  public ListIterator<String> listIterator(final int index) {
   return obsList.listIterator(index);
  }
 
  @Override
  public List<String> subList(final int fromIndex, final int toIndex) {
   return obsList.subList(fromIndex, toIndex);
  }
 
 }

 private final TestModel.TestObsValue stringProp = new TestModel.TestObsValue();
 private final TestModel.TestObsList listProp = new TestModel.TestObsList();

 public final TestModel.TestObsValue stringPropProperty() {
  return this.stringProp;
 }

 public final String getStringProp() {
  return stringProp.get();
 }

 public final void setStringProp(final String stringProp) {
  this.stringProp.set(stringProp);
 }

 public TestModel.TestObsList getListProp() {
  return listProp;
 }

}