package de.saxsys.persistencefx.examples.jpa;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(final Stage primaryStage) throws IOException {
    final FXMLLoader loader = new FXMLLoader(
        Main.class.getResource("/de/saxsys/persistencefx/examples/jpa/CarView.fxml"));
    loader.load();
    final Scene scene = new Scene(loader.getRoot());
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  public static void main(final String[] args) {
    System.setProperty("derby.stream.error.file", "target/derby.log");
    launch(args);
  }
}
