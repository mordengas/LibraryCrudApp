package com.projektzaliczeniowy.projektzaliczeniowy;

/**
 * @author Dominik Machnik
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class mainApp extends Application {

    static Configuration configuration = new Configuration().configure();
    static StandardServiceRegistry standardRegistry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
    static Metadata metaData = new MetadataSources(standardRegistry).getMetadataBuilder().build();
    static SessionFactory sessionFactory = metaData.getSessionFactoryBuilder().build();

    public static void main(String[] args) {

        launch(args);

    }

    /**
     * Otwiera pierwszą scenę w projekcie.
     */
    public void start(Stage stage) {
        stage.setTitle("Biblioteka");

        try {
            Parent root = FXMLLoader.load(getClass().getResource("projekt.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

}