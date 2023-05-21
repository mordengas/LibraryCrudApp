package com.projektzaliczeniowy.projektzaliczeniowy;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.projektzaliczeniowy.projektzaliczeniowy.mainApp.sessionFactory;

public class SceneController {

    /**
     * Przechowuje id ksiazki ktora zostala wybrna do wypozyczenia.
     */
    public Long ksiazka_wypozyczona_id;

    /**
     * Przechowuje dane ksiazki ktora zostala wybrna do wypozyczenia.
     */
    public Ksiazki ksiazka_wypozyczona;

    /**
     * Przechowuje dane ksiazki ktora zostala wybrna do zwrocenia.
     */
    public Ksiazki ksiazka_zwrocona;

    /**
     * Przechowuje id ksiazki ktora zostala wybrna do zwrocenia.
     */
    public Long ksiazka_zwrocona_id;

    /**
     * Główny stage na ktorym wszytsko jest wyswietlane.
     */
    private Stage stage;

    /**
     * Główna scena na ktorej wszytsko jest wyswietlane.
     */
    private Scene scene;

    /**
     * Zmienne używane w pętli w zwrocie i wypozyczeniu.
     */
    public int k, l;

    /**
     * Obsługuje przycisk służący do wyjścia z programu.
     */
    public void exitProgram(ActionEvent event) throws IOException {
        Platform.exit();
    }

    /**
     * Obsługuje przycisk służacy do zmiany sceny na scenę początkową.
     */
    public void switchToProjekt(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("projekt.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Obsługuje przycisk służacy do zmiany sceny na scenę admin.
     */
    public void switchToAdmin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("admin.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Obsługuje przycisk służacy do zmiany sceny na scene klient.
     */
    public void switchToKlient(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("klient.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Obsługuje przycisk służacy do zmiany sceny na scene dodaj.
     */
    public void switchToDodaj(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("dodaj.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();


        Button dodaj = (Button) scene.lookup("#dodaj");
        dodaj.setOnAction(new EventHandler<>() {

            /**
             * Pobiera dane z textFielda i dodaje
             * książki do bazy danych.
             */
            @Override
            public void handle(ActionEvent e) {

                TextField tytul = (TextField) scene.lookup("#tytul");
                TextField autor = (TextField) scene.lookup("#autor");
                TextField wydawca = (TextField) scene.lookup("#wydawca");
                TextField data_wydania = (TextField) scene.lookup("#data_wydania");

                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("biblio");
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                Ksiazki ksiazka = new Ksiazki();

                ksiazka.setTytul(tytul.getText());
                ksiazka.setAutor(autor.getText());
                ksiazka.setWydawca(wydawca.getText());
                ksiazka.setDataWydania(data_wydania.getText());
                ksiazka.setStanWypozyczenia(false);

                entityManager.getTransaction().begin();
                entityManager.persist(ksiazka);
                entityManager.getTransaction().commit();

                entityManager.close();
                entityManagerFactory.close();

                System.out.println("dodano ksiazke");

            }
        });
    }


    /**
     * Obsługuje przycisk służacy do zmiany sceny na scene zarządzania klientami.
     */
    public void switchToZarzadzanieKlientami(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("klienci.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        @SuppressWarnings("unchecked")
        TableView<Klienci> klienciTable = (TableView<Klienci>) scene.lookup("#klienci");
        ObservableList<Klienci> observableKlienci = FXCollections.observableArrayList();
        TableColumn<Klienci, Long> idKlienta_c = new TableColumn<>("idKlienta");
        TableColumn<Klienci, String> imie_c = new TableColumn<>("imie");
        TableColumn<Klienci, String> nazwisko_c = new TableColumn<>("nazwisko");
        TableColumn<Klienci, Integer> numerTel_c = new TableColumn<>("numerTel");

        idKlienta_c.setCellValueFactory(new PropertyValueFactory<>("idKlienta"));
        imie_c.setCellValueFactory(new PropertyValueFactory<>("imie"));
        nazwisko_c.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
        numerTel_c.setCellValueFactory(new PropertyValueFactory<>("numerTel"));

        klienciTable.getColumns().addAll(idKlienta_c, imie_c, nazwisko_c, numerTel_c);

        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            Query query = session.createQuery("FROM Klienci");
            List<Klienci> klienci = (List<Klienci>) query.list();

            session.close();

            observableKlienci.addAll(klienci);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Button refresh_kli = (Button) scene.lookup("#odswiez_kli");
        {
            refresh_kli.setOnAction(new EventHandler<ActionEvent>() {


                /**
                 * Odświeża tabelę z wyświetlonymi klientami.
                 */
                @Override
                public void handle(ActionEvent event) {

                    observableKlienci.clear();

                    try {
                        Session session = sessionFactory.openSession();
                        session.beginTransaction();

                        Query query = session.createQuery("FROM Klienci ");
                        List<Klienci> klienci = (List<Klienci>) query.list();

                        session.close();

                        observableKlienci.addAll(klienci);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }
            });

        }


        TableColumn<Klienci, Void> usunEdytuj = new TableColumn<>("Edycja/Usuwanie");

        Callback<TableColumn<Klienci, Void>, TableCell<Klienci, Void>> cellFactory = new Callback<TableColumn<Klienci, Void>, TableCell<Klienci, Void>>() {
            @Override
            public TableCell<Klienci, Void> call(final TableColumn<Klienci, Void> param) {
                final TableCell<Klienci, Void> cell = new TableCell<Klienci, Void>() {

                    private final Button btn = new Button("Edytuj/Usuń");

                    {
                        btn.setOnAction(new EventHandler<ActionEvent>() {

                            /**
                             * Otwiera okienko do edycji danych wybranego klienta.
                             */

                            @Override
                            public void handle(ActionEvent event) {
                                Klienci data = getTableView().getItems().get(getIndex());

                                final Stage editpop = new Stage();
                                editpop.initModality(Modality.APPLICATION_MODAL);
                                editpop.initOwner(stage);
                                editpop.setTitle("Klienci - Edytuj/Usuń");
                                editpop.setResizable(false);

                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("edytujusunklientow.fxml"));
                                    editpop.setScene(new Scene(root));
                                    editpop.show();


                                } catch (Exception e) {

                                    e.printStackTrace();

                                }

                                TextField imie_e = (TextField) editpop.getScene().lookup("#imie_textfield");
                                TextField nazwisko_e = (TextField) editpop.getScene().lookup("#nazwisko_textfield");
                                TextField numerTel_e = (TextField) editpop.getScene().lookup("#numerTel_textfield");
                                Text klientPopup = (Text) editpop.getScene().lookup("#klienciPopup");

                                imie_e.setText(data.getImie());
                                nazwisko_e.setText(data.getNazwisko());
                                numerTel_e.setText(String.valueOf(data.getNumerTel()));


                                Button wsteczbtn = (Button) editpop.getScene().lookup("#wstecz_kli");
                                wsteczbtn.setOnAction(new EventHandler<ActionEvent>() {

                                    /**
                                     * Zamyka okienko do edycji danych wybranego klienta.
                                     */
                                    @Override
                                    public void handle(ActionEvent e) {
                                        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
                                    }
                                });

                                Button edytujbtn = (Button) editpop.getScene().lookup("#zmien_kli");

                                edytujbtn.setOnAction(new EventHandler<ActionEvent>() {


                                    /**
                                     * Zamienia dane wybranego klienta.
                                     */

                                    @Override
                                    public void handle(ActionEvent e) {

                                        Klienci tempklient = new Klienci(getTableView().getItems().get(getIndex()).getIdKlienta(), imie_e.getText(), nazwisko_e.getText(), Integer.parseInt(numerTel_e.getText()));
                                        System.out.println(tempklient.toString());

                                        EntityManagerFactory emf = null;
                                        EntityManager em = null;
                                        EntityTransaction transaction = null;

                                        try {
                                            emf = Persistence.createEntityManagerFactory("biblio");
                                            em = emf.createEntityManager();
                                            transaction = em.getTransaction();
                                            transaction.begin();

                                            Klienci klientEdit = em.find(Klienci.class, tempklient.getIdKlienta());
                                            klientEdit.setImie(tempklient.getImie());
                                            klientEdit.setNazwisko(tempklient.getNazwisko());
                                            klientEdit.setNumerTel(tempklient.getNumerTel());

                                            transaction.commit();

                                        } catch (Exception x) {
                                            transaction.rollback();
                                        } finally {
                                            em.close();
                                            emf.close();
                                        }

                                        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();

                                    }
                                });

                                Button usunbtn = (Button) editpop.getScene().lookup("#usun_kli");
                                usunbtn.setOnAction(new EventHandler<ActionEvent>() {

                                    /**
                                     * Usuwa wybranego klienta.
                                     */

                                    @Override
                                    public void handle(ActionEvent e) {
                                        Session session = sessionFactory.openSession();
                                        session.beginTransaction();
                                        Klienci klientdel = (Klienci) session.load(Klienci.class, getTableView().getItems().get(getIndex()).getIdKlienta());

                                        ObservableList<Wypozyczenia> observableWypozyczenia = FXCollections.observableArrayList();
                                        try {
                                            Session sessions = sessionFactory.openSession();
                                            sessions.beginTransaction();

                                            Query query = sessions.createQuery("FROM Wypozyczenia ");


                                            List<Wypozyczenia> wypozyczeniaList = (List<Wypozyczenia>) query.list();


                                            System.out.println(wypozyczeniaList);

                                            sessions.close();

                                            observableWypozyczenia.addAll(wypozyczeniaList);

                                        } catch (Exception m) {
                                            System.out.println(m.getMessage());
                                        }

                                        for (Wypozyczenia wypUsun : observableWypozyczenia) {
                                            l = 0;

                                            if (wypUsun.getIdKlienta() == klientdel.getIdKlienta()) {

                                                klientPopup.setText("Nie mozna usunąć");

                                                l = 1;
                                                break;
                                            }
                                        }

                                        if (l != 1) {

                                            session.delete(klientdel);
                                            session.getTransaction().commit();
                                            Query query = session.createQuery("FROM Klienci ");
                                            List<Klienci> kliencitemp = (List<Klienci>) query.list();
                                            session.close();

                                        }

                                        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();

                                    }
                                });

                            }
                        });
                    }


                    /**
                     * Przypisuje przycisk Edytuj/Usuń do kazdego zapełnionengo wiersza tabeli.
                     */
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        usunEdytuj.setCellFactory(cellFactory);
        klienciTable.getColumns().add(usunEdytuj);

        TextField wyszukaj = (TextField) scene.lookup("#wyszukaj_kli");

        FilteredList<Klienci> filteredKlienci = new FilteredList<>(observableKlienci, b -> true);

        wyszukaj.textProperty().addListener((observableValue, oldValue, newValue) -> {

            filteredKlienci.setPredicate(klienci -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (klienci.getImie().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (klienci.getNazwisko().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (String.valueOf(klienci.getNumerTel()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else return false;
            });

        });

        SortedList<Klienci> sortedKlienci = new SortedList<>(filteredKlienci);

        sortedKlienci.comparatorProperty().bind(klienciTable.comparatorProperty());

        klienciTable.setItems(sortedKlienci);

        stage.setScene(scene);
        stage.show();

    }


    /**
     * Obsługuje przycisk służacy do zmiany sceny na scene zarządzania ksiazkami.
     */
    public void switchToZarzadzanieKsiazkami(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("ksiazki.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        @SuppressWarnings("unchecked")
        TableView<Ksiazki> ksiazkiTable = (TableView<Ksiazki>) scene.lookup("#ksiazki");
        ObservableList<Ksiazki> observableKsiazki = FXCollections.observableArrayList();
        TableColumn<Ksiazki, Long> idKsiazki_c = new TableColumn<>("idKsiazki");
        idKsiazki_c.setMaxWidth(30);
        TableColumn<Ksiazki, String> tytul_c = new TableColumn<>("tytul");
        TableColumn<Ksiazki, String> autor_c = new TableColumn<>("autor");
        TableColumn<Ksiazki, String> wydawca_c = new TableColumn<>("wydawca");
        TableColumn<Ksiazki, String> rok_c = new TableColumn<>("dataWydania");
        TableColumn<Ksiazki, Boolean> wypozyczona_c = new TableColumn<>("stanWypozyczenia");
        wypozyczona_c.setMaxWidth(60);

        idKsiazki_c.setCellValueFactory(new PropertyValueFactory<>("idKsiazki"));
        tytul_c.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        autor_c.setCellValueFactory(new PropertyValueFactory<>("autor"));
        wydawca_c.setCellValueFactory(new PropertyValueFactory<>("wydawca"));
        rok_c.setCellValueFactory(new PropertyValueFactory<>("dataWydania"));
        wypozyczona_c.setCellValueFactory(new PropertyValueFactory<>("stanWypozyczenia"));

        ksiazkiTable.getColumns().addAll(idKsiazki_c, tytul_c, autor_c, wydawca_c, rok_c, wypozyczona_c);

        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            Query query = session.createQuery("FROM Ksiazki");
            List<Ksiazki> ksiazki = (List<Ksiazki>) query.list();

            session.close();

            observableKsiazki.addAll(ksiazki);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        Button refresh_ksi = (Button) scene.lookup("#refresh_ksi");
        {
            refresh_ksi.setOnAction(new EventHandler<ActionEvent>() {

                /**
                 * Odświeża tabelę z wyświetlonymi ksiazkami.
                 */
                @Override
                public void handle(ActionEvent event) {

                    observableKsiazki.clear();

                    try {
                        Session session = sessionFactory.openSession();
                        session.beginTransaction();

                        Query query = session.createQuery("FROM Ksiazki");
                        List<Ksiazki> ksiazki = (List<Ksiazki>) query.list();

                        session.close();

                        observableKsiazki.addAll(ksiazki);

                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                }
            });

        }


        TableColumn<Ksiazki, Void> usunEdytuj = new TableColumn<>("Edycja/Usuwanie");

        Callback<TableColumn<Ksiazki, Void>, TableCell<Ksiazki, Void>> cellFactory = new Callback<TableColumn<Ksiazki, Void>, TableCell<Ksiazki, Void>>() {
            @Override
            public TableCell<Ksiazki, Void> call(final TableColumn<Ksiazki, Void> param) {
                final TableCell<Ksiazki, Void> cell = new TableCell<Ksiazki, Void>() {

                    private final Button btn = new Button("Edytuj/Usuń");

                    {
                        btn.setOnAction(new EventHandler<ActionEvent>() {


                            /**
                             * Otwiera okienko do edycji danych wybranej ksiazki.
                             */
                            @Override
                            public void handle(ActionEvent event) {
                                Ksiazki data = getTableView().getItems().get(getIndex());

                                final Stage editpop = new Stage();
                                editpop.initModality(Modality.APPLICATION_MODAL);
                                editpop.initOwner(stage);
                                editpop.setTitle("Książki - Edytuj/Usuń");
                                editpop.setResizable(false);

                                try {
                                    Parent root = FXMLLoader.load(getClass().getResource("edytujusunksiazki.fxml"));
                                    editpop.setScene(new Scene(root));
                                    editpop.show();


                                } catch (Exception e) {

                                    e.printStackTrace();

                                }

                                TextField tytul_e = (TextField) editpop.getScene().lookup("#tytul_textfield");
                                TextField autor_e = (TextField) editpop.getScene().lookup("#autor_textfield");
                                TextField wydwaca_e = (TextField) editpop.getScene().lookup("#wydawca_textfield");
                                TextField rokWydania_e = (TextField) editpop.getScene().lookup("#rokWydania_textfield");
                                CheckBox stanWypozyczenia_e = (CheckBox) editpop.getScene().lookup("#select");
                                Text ksiazkiPopup = (Text) editpop.getScene().lookup("#ksiazkiPopup");

                                tytul_e.setText(data.getTytul());
                                autor_e.setText(data.getAutor());
                                wydwaca_e.setText(data.getWydawca());
                                rokWydania_e.setText(data.getDataWydania());
                                stanWypozyczenia_e.selectedProperty().set(data.isStanWypozyczenia());

                                Button wsteczbtn = (Button) editpop.getScene().lookup("#wstecz_ksi");
                                wsteczbtn.setOnAction(new EventHandler<ActionEvent>() {

                                    /**
                                     * Zamyka okienko do edycji danych wybranej ksiazki.
                                     */
                                    @Override
                                    public void handle(ActionEvent e) {
                                        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();
                                    }
                                });

                                Button edytujbtn = (Button) editpop.getScene().lookup("#zmien_ksi");

                                edytujbtn.setOnAction(new EventHandler<ActionEvent>() {

                                    /**
                                     * Zmienia dane wybranej ksiazki.
                                     */
                                    @Override
                                    public void handle(ActionEvent e) {

                                        Ksiazki tempksiazka = new Ksiazki(getTableView().getItems().get(getIndex()).getIdKsiazki(), tytul_e.getText(), autor_e.getText(), wydwaca_e.getText(), rokWydania_e.getText(), stanWypozyczenia_e.isSelected());
                                        System.out.println(tempksiazka.toString());

                                        EntityManagerFactory emf = null;
                                        EntityManager em = null;
                                        EntityTransaction transaction = null;

                                        try {
                                            emf = Persistence.createEntityManagerFactory("biblio");
                                            em = emf.createEntityManager();
                                            transaction = em.getTransaction();
                                            transaction.begin();

                                            Ksiazki ksiazkaEdit = em.find(Ksiazki.class, tempksiazka.getIdKsiazki());
                                            ksiazkaEdit.setTytul(tempksiazka.getTytul());
                                            ksiazkaEdit.setAutor(tempksiazka.getAutor());
                                            ksiazkaEdit.setDataWydania(tempksiazka.getDataWydania());
                                            ksiazkaEdit.setWydawca(tempksiazka.getWydawca());
                                            ksiazkaEdit.setStanWypozyczenia(tempksiazka.isStanWypozyczenia());

                                            transaction.commit();

                                        } catch (Exception x) {
                                            transaction.rollback();
                                        } finally {
                                            em.close();
                                            emf.close();
                                        }

                                        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();

                                    }
                                });

                                Button usunbtn = (Button) editpop.getScene().lookup("#usun_ksi");
                                usunbtn.setOnAction(new EventHandler<ActionEvent>() {

                                    /**
                                     * Usuwa wybraną ksiazke.
                                     */
                                    @Override
                                    public void handle(ActionEvent e) {
                                        Session session = sessionFactory.openSession();
                                        session.beginTransaction();
                                        Ksiazki ksiazkadel = (Ksiazki) session.load(Ksiazki.class, getTableView().getItems().get(getIndex()).getIdKsiazki());

                                        ObservableList<Wypozyczenia> observableWypozyczenia = FXCollections.observableArrayList();
                                        try {
                                            Session sessions = sessionFactory.openSession();
                                            sessions.beginTransaction();

                                            Query query = sessions.createQuery("FROM Wypozyczenia ");


                                            List<Wypozyczenia> wypozyczeniaList = (List<Wypozyczenia>) query.list();


                                            System.out.println(wypozyczeniaList);

                                            sessions.close();

                                            observableWypozyczenia.addAll(wypozyczeniaList);

                                        } catch (Exception m) {
                                            System.out.println(m.getMessage());
                                        }

                                        for (Wypozyczenia wypUsun : observableWypozyczenia) {
                                            l = 0;

                                            if (wypUsun.getIdKsiazki() == ksiazkadel.getIdKsiazki()) {

                                                ksiazkiPopup.setText("Nie mozna usunąć");

                                                l = 1;
                                                break;
                                            }
                                        }

                                        if (l != 1) {

                                            session.delete(ksiazkadel);
                                            session.getTransaction().commit();
                                            Query query = session.createQuery("FROM Ksiazki");
                                            List<Ksiazki> ksiazkitemp = (List<Ksiazki>) query.list();
                                            session.close();


                                        }

                                        ((Stage) (((Button) e.getSource()).getScene().getWindow())).close();

                                    }
                                });

                            }
                        });
                    }


                    /**
                     * Przypisuje przycisk Edytuj/Usuń do kazdego zapełnionengo wiersza tabeli.
                     */
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        usunEdytuj.setCellFactory(cellFactory);
        ksiazkiTable.getColumns().add(usunEdytuj);

        TextField wyszukaj = (TextField) scene.lookup("#wyszukaj_k");

        wyszukajKsiazki(ksiazkiTable, observableKsiazki, wyszukaj);

        stage.setScene(scene);
        stage.show();

    }


    /**
     * Obsługuje przycisk służacy do zmiany sceny na scene zarządzania wypozyczeniami.
     */
    public void switchToZarzadzanieWypozyczeniami(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("wypozyczenia.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        @SuppressWarnings("unchecked")
        TableView<Wypozyczenia> wypozyczeniaTable = (TableView<Wypozyczenia>) scene.lookup("#wypozyczenia");
        ObservableList<Wypozyczenia> observableWypozyczenia = FXCollections.observableArrayList();
        TableColumn<Wypozyczenia, Long> idWypozyczenia_c = new TableColumn<>("idWypozyczenia");
        TableColumn<Wypozyczenia, Long> idKsiazki_cw = new TableColumn<>("idKsiazki");
        TableColumn<Wypozyczenia, Long> idKlienta_cw = new TableColumn<>("idKlienta");
        TableColumn<Wypozyczenia, LocalDate> dataWypozyczenia_c = new TableColumn<>("dataWypozyczenia");
        TableColumn<Wypozyczenia, LocalDate> dataZwrotu_c = new TableColumn<>("dataZwrotu");


        idWypozyczenia_c.setCellValueFactory(new PropertyValueFactory<>("idWypozyczenia"));
        idKsiazki_cw.setCellValueFactory(new PropertyValueFactory<>("idKsiazki"));
        idKlienta_cw.setCellValueFactory(new PropertyValueFactory<>("idKlienta"));
        dataWypozyczenia_c.setCellValueFactory(new PropertyValueFactory<>("dataWypozyczenia"));
        dataZwrotu_c.setCellValueFactory(new PropertyValueFactory<>("dataZwrotu"));

        wypozyczeniaTable.getColumns().addAll(idWypozyczenia_c, idKsiazki_cw, idKlienta_cw, dataWypozyczenia_c, dataZwrotu_c);


        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            Query query = session.createQuery("FROM Wypozyczenia ");


            List<Wypozyczenia> wypozyczeniaList = (List<Wypozyczenia>) query.list();


            System.out.println(wypozyczeniaList);

            session.close();

            observableWypozyczenia.addAll(wypozyczeniaList);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }


        wypozyczeniaTable.setItems(observableWypozyczenia);

        stage.setScene(scene);
        stage.show();

    }

    /**
     * Obsługuje przycisk służacy do zmiany sceny na scene z obsługą wypożyczeń.
     */
    public void switchToWypozyczenie(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("wypozyczenie.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        @SuppressWarnings("unchecked")
        TableView<Ksiazki> ksiazkiTable = (TableView<Ksiazki>) scene.lookup("#ksiazki_w");
        ObservableList<Ksiazki> observableKsiazki = FXCollections.observableArrayList();
        TableColumn<Ksiazki, Long> idKsiazki_c = new TableColumn<>("idKsiazki");
        TableColumn<Ksiazki, String> tytul_c = new TableColumn<>("tytul");
        TableColumn<Ksiazki, String> autor_c = new TableColumn<>("autor");
        TableColumn<Ksiazki, String> wydawca_c = new TableColumn<>("wydawca");
        TableColumn<Ksiazki, String> rok_c = new TableColumn<>("dataWydania");
        TableColumn<Ksiazki, Boolean> wypozyczona_c = new TableColumn<>("stanWypozyczenia");

        idKsiazki_c.setCellValueFactory(new PropertyValueFactory<>("idKsiazki"));
        tytul_c.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        autor_c.setCellValueFactory(new PropertyValueFactory<>("autor"));
        wydawca_c.setCellValueFactory(new PropertyValueFactory<>("wydawca"));
        rok_c.setCellValueFactory(new PropertyValueFactory<>("dataWydania"));
        wypozyczona_c.setCellValueFactory(new PropertyValueFactory<>("stanWypozyczenia"));


        ksiazkiTable.getColumns().addAll(idKsiazki_c, tytul_c, autor_c, wydawca_c, rok_c, wypozyczona_c);

        try {
            Session session = sessionFactory.openSession();
            session.beginTransaction();

            Query query = session.createQuery("FROM Ksiazki");


            List<Ksiazki> ksiazki = (List<Ksiazki>) query.list();

            session.close();

            ksiazki.removeIf(Ksiazki::isStanWypozyczenia);

            observableKsiazki.addAll(ksiazki);


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        TableColumn<Ksiazki, Boolean> wybierz = new TableColumn<>("Wybierz");

        Callback<TableColumn<Ksiazki, Boolean>, TableCell<Ksiazki, Boolean>> cellFactory = new Callback<TableColumn<Ksiazki, Boolean>, TableCell<Ksiazki, Boolean>>() {
            @Override
            public TableCell<Ksiazki, Boolean> call(final TableColumn<Ksiazki, Boolean> param) {
                final TableCell<Ksiazki, Boolean> cell = new TableCell<Ksiazki, Boolean>() {

                    private final CheckBox select = new CheckBox();

                    {

                        select.selectedProperty().addListener(new ChangeListener<Boolean>() {

                            /**
                             * Pobiera z checkBoxa ktora ksiazka zostala wybrana do wypożyczenia.
                             */
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean old_val, Boolean new_val) {

                                Session session = sessionFactory.openSession();
                                session.beginTransaction();
                                if (new_val == true) {
                                    Ksiazki ksiazkawyp = getTableView().getItems().get(getIndex());

                                    ksiazka_wypozyczona_id = ksiazkawyp.getIdKsiazki();

                                    ksiazka_wypozyczona = ksiazkawyp;

                                    System.out.println(ksiazka_wypozyczona);
                                }
                                session.close();

                            }
                        });

                    }


                    /**
                     * Przypisuje chechBoxa do kazdego zapełnionengo wiersza tabeli.
                     */
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(select);
                        }
                    }

                };
                return cell;
            }
        };

        wybierz.setCellFactory(cellFactory);
        ksiazkiTable.getColumns().add(wybierz);


        TextField wyszukaj = (TextField) scene.lookup("#wyszukaj_w");

        wyszukajKsiazki(ksiazkiTable, observableKsiazki, wyszukaj);

        stage.setScene(scene);
        stage.show();


        Button dodaj = (Button) scene.lookup("#wypozycz");

        dodaj.setOnAction(new EventHandler<>() {


            /**
             * Obsługuje wypożyczenie książki przez danego klienta.
             * Gdy klienta nie ma w bazie, to tworzy nowego.
             */
            @Override
            public void handle(ActionEvent e) {

                TextField nazwisko = (TextField) scene.lookup("#nazwisko_w");
                TextField imie = (TextField) scene.lookup("#imie_w");
                TextField telefon = (TextField) scene.lookup("#telefon_w");
                DatePicker data_wyp = (DatePicker) scene.lookup("#data_wyp_w");

                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("biblio");
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                ObservableList<Klienci> observableKlienciW = FXCollections.observableArrayList();

                try {
                    Session session = sessionFactory.openSession();
                    session.beginTransaction();

                    Query query = session.createQuery("FROM Klienci ");
                    List<Klienci> klienciW = (List<Klienci>) query.list();

                    session.close();
                    observableKlienciW.addAll(klienciW);

                } catch (Exception x) {
                    System.out.println(x.getMessage());
                }

                for (Klienci obecniKlienci : observableKlienciW) {

                    k = 0;

                    if (obecniKlienci.getImie().equalsIgnoreCase(imie.getText()) && obecniKlienci.getNazwisko().equalsIgnoreCase(nazwisko.getText()) && obecniKlienci.getNumerTel() == Integer.parseInt(telefon.getText())) {
                        Wypozyczenia wypozyczenie = new Wypozyczenia();

                        wypozyczenie.setDataWypozyczenia(data_wyp.getValue());
                        wypozyczenie.setIdKlienta(obecniKlienci.getIdKlienta());
                        wypozyczenie.setIdKsiazki(ksiazka_wypozyczona.getIdKsiazki());

                        ksiazka_wypozyczona.setStanWypozyczenia(true);

                        entityManager.getTransaction().begin();
                        entityManager.merge(ksiazka_wypozyczona);
                        entityManager.merge(wypozyczenie);
                        entityManager.getTransaction().commit();


                        k = 1;
                        break;
                    }

                }

                if (k != 1) {

                    Klienci klient = new Klienci();
                    klient.setImie(imie.getText());
                    klient.setNazwisko(nazwisko.getText());
                    klient.setNumerTel(Integer.parseInt(telefon.getText()));

                    entityManager.getTransaction().begin();
                    entityManager.persist(klient);
                    entityManager.getTransaction().commit();

                    Wypozyczenia wypozyczenie = new Wypozyczenia();

                    wypozyczenie.setDataWypozyczenia(data_wyp.getValue());
                    wypozyczenie.setIdKlienta(klient.getIdKlienta());
                    wypozyczenie.setIdKsiazki(ksiazka_wypozyczona.getIdKsiazki());

                    ksiazka_wypozyczona.setStanWypozyczenia(true);

                    entityManager.getTransaction().begin();
                    entityManager.merge(ksiazka_wypozyczona);
                    entityManager.merge(wypozyczenie);
                    entityManager.getTransaction().commit();

                }

                entityManager.close();
                entityManagerFactory.close();

            }
        });

    }

    /**
     * Obsługuje przycisk służacy do zmiany sceny na scene z obsługą zwrotów.
     */
    public void switchToZwrot(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("zwrot.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);

        @SuppressWarnings("unchecked")
        TableView<Ksiazki> ksiazkiTable = (TableView<Ksiazki>) scene.lookup("#ksiazki_z");
        ObservableList<Ksiazki> observableKsiazki = FXCollections.observableArrayList();
        TableColumn<Ksiazki, Long> idKsiazki_c = new TableColumn<>("idKsiazki");
        TableColumn<Ksiazki, String> tytul_c = new TableColumn<>("tytul");
        TableColumn<Ksiazki, String> autor_c = new TableColumn<>("autor");
        TableColumn<Ksiazki, String> wydawca_c = new TableColumn<>("wydawca");
        TableColumn<Ksiazki, String> rok_c = new TableColumn<>("dataWydania");
        TableColumn<Ksiazki, Boolean> wypozyczona_c = new TableColumn<>("stanWypozyczenia");

        idKsiazki_c.setCellValueFactory(new PropertyValueFactory<>("idKsiazki"));
        tytul_c.setCellValueFactory(new PropertyValueFactory<>("tytul"));
        autor_c.setCellValueFactory(new PropertyValueFactory<>("autor"));
        wydawca_c.setCellValueFactory(new PropertyValueFactory<>("wydawca"));
        rok_c.setCellValueFactory(new PropertyValueFactory<>("dataWydania"));
        wypozyczona_c.setCellValueFactory(new PropertyValueFactory<>("stanWypozyczenia"));

        ksiazkiTable.getColumns().addAll(idKsiazki_c, tytul_c, autor_c, wydawca_c, rok_c, wypozyczona_c);

        Button pokaz = (Button) scene.lookup("#pokaz_ksiazki");

        pokaz.setOnAction(new EventHandler<ActionEvent>() {

            /**
             * Po wpisaniu danych przez kienta, pokazuje wypożyczone przez niego ksiazki.
             */
            @Override
            public void handle(ActionEvent event) {

                TextField nazwisko = (TextField) scene.lookup("#nazwisko_z");
                TextField imie = (TextField) scene.lookup("#imie_z");
                TextField telefon = (TextField) scene.lookup("#telefon_z");

                ObservableList<Klienci> observableKlienciZW = FXCollections.observableArrayList();

                try {
                    Session session = sessionFactory.openSession();
                    session.beginTransaction();

                    Query query = session.createQuery("FROM Klienci ");
                    List<Klienci> klienciW = (List<Klienci>) query.list();

                    session.close();
                    observableKlienciZW.addAll(klienciW);

                } catch (Exception x) {
                    System.out.println(x.getMessage());
                }

                for (Klienci obecniKlienci : observableKlienciZW) {

                    if (obecniKlienci.getImie().equalsIgnoreCase(imie.getText()) && obecniKlienci.getNazwisko().equalsIgnoreCase(nazwisko.getText()) && obecniKlienci.getNumerTel() == Integer.parseInt(telefon.getText())) {

                        ObservableList<Wypozyczenia> observableWypozyczeniaZ = FXCollections.observableArrayList();
                        try {
                            Session session = sessionFactory.openSession();
                            session.beginTransaction();

                            Query query = session.createQuery("FROM Wypozyczenia ");

                            List<Wypozyczenia> wypozyczeniaList = (List<Wypozyczenia>) query.list();

                            session.close();

                            observableWypozyczeniaZ.addAll(wypozyczeniaList);


                        } catch (Exception p) {
                            System.out.println(p.getMessage());
                        }
                        for (Wypozyczenia obecneWypozyczenia : observableWypozyczeniaZ) {

                            if (obecneWypozyczenia.getIdKlienta() == obecniKlienci.getIdKlienta()) {
                                try {
                                    Session session = sessionFactory.openSession();
                                    session.beginTransaction();

                                    Query query = session.createQuery("FROM Ksiazki");
                                    List<Ksiazki> ksiazki = (List<Ksiazki>) query.list();

                                    session.close();

                                    ksiazki.removeIf(it -> it.getIdKsiazki() != obecneWypozyczenia.getIdKsiazki());

                                    ksiazki.removeIf(it -> !it.isStanWypozyczenia());

                                    observableKsiazki.addAll(ksiazki);

                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }

                            }
                        }

                    }

                }

            }
        });

        TableColumn<Ksiazki, Boolean> wybierz = new TableColumn<>("Wybierz");

        Callback<TableColumn<Ksiazki, Boolean>, TableCell<Ksiazki, Boolean>> cellFactory = new Callback<TableColumn<Ksiazki, Boolean>, TableCell<Ksiazki, Boolean>>() {
            @Override
            public TableCell<Ksiazki, Boolean> call(final TableColumn<Ksiazki, Boolean> param) {
                final TableCell<Ksiazki, Boolean> cell = new TableCell<Ksiazki, Boolean>() {

                    private final CheckBox select = new CheckBox();

                    {

                        select.selectedProperty().addListener(new ChangeListener<Boolean>() {

                            /**
                             * Zczytuje z checkBoxa ktora ksiazka zostala wybrana do zwrotu.
                             */
                            @Override
                            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean old_val, Boolean new_val) {

                                Session session = sessionFactory.openSession();
                                session.beginTransaction();
                                if (new_val == true) {
                                    Ksiazki ksiazkazwr = getTableView().getItems().get(getIndex());

                                    ksiazka_zwrocona_id = ksiazkazwr.getIdKsiazki();

                                    ksiazka_zwrocona = ksiazkazwr;

                                    System.out.println(ksiazka_zwrocona);
                                }
                                session.close();

                            }
                        });

                    }


                    /**
                     * Przypisuje chechBoxa do kazdego zapełnionengo wiersza tabeli.
                     */
                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(select);
                        }
                    }

                };
                return cell;
            }
        };

        wybierz.setCellFactory(cellFactory);
        ksiazkiTable.getColumns().add(wybierz);


        TextField wyszukaj = (TextField) scene.lookup("#wyszukaj_z");

        wyszukajKsiazki(ksiazkiTable, observableKsiazki, wyszukaj);

        stage.setScene(scene);
        stage.show();

        Button dodaj = (Button) scene.lookup("#zwroc");

        dodaj.setOnAction(new EventHandler<>() {


            /**
             * Obsługuje operację zwrotu ksiazki przez danego klienta.
             */
            @Override
            public void handle(ActionEvent e) {

                TextField nazwisko = (TextField) scene.lookup("#nazwisko_z");
                TextField imie = (TextField) scene.lookup("#imie_z");
                TextField telefon = (TextField) scene.lookup("#telefon_z");
                DatePicker data_wyp = (DatePicker) scene.lookup("#data_wyp_z");
                Text textPopup = (Text) scene.lookup("#textPopup");
                EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("biblio");
                EntityManager entityManager = entityManagerFactory.createEntityManager();

                ObservableList<Klienci> observableKlienciW = FXCollections.observableArrayList();

                try {
                    Session session = sessionFactory.openSession();
                    session.beginTransaction();

                    Query query = session.createQuery("FROM Klienci ");
                    List<Klienci> klienciW = (List<Klienci>) query.list();

                    session.close();
                    observableKlienciW.addAll(klienciW);

                } catch (Exception x) {
                    System.out.println(x.getMessage());
                }

                for (Klienci obecniKlienci : observableKlienciW) {

                    k = 0;

                    if (obecniKlienci.getImie().equalsIgnoreCase(imie.getText()) && obecniKlienci.getNazwisko().equalsIgnoreCase(nazwisko.getText()) && obecniKlienci.getNumerTel() == Integer.parseInt(telefon.getText())) {

                        ObservableList<Wypozyczenia> observableWypozyczeniaZ = FXCollections.observableArrayList();
                        try {
                            Session session = sessionFactory.openSession();
                            session.beginTransaction();

                            Query query = session.createQuery("FROM Wypozyczenia ");

                            List<Wypozyczenia> wypozyczeniaList = (List<Wypozyczenia>) query.list();

                            session.close();

                            observableWypozyczeniaZ.addAll(wypozyczeniaList);


                        } catch (Exception p) {
                            System.out.println(p.getMessage());
                        }
                        for (Wypozyczenia obecneWypozyczenia : observableWypozyczeniaZ) {

                            if (obecneWypozyczenia.getIdKlienta() == obecniKlienci.getIdKlienta() && obecneWypozyczenia.getIdKsiazki() == ksiazka_zwrocona_id) {
                                textPopup.setText("Zwrócono Książkę");
                                textPopup.setFill(Color.GREEN);

                                obecneWypozyczenia.setDataZwrotu(data_wyp.getValue());
                                ksiazka_zwrocona.setStanWypozyczenia(false);

                                entityManager.getTransaction().begin();

                                entityManager.merge(ksiazka_zwrocona);
                                entityManager.merge(obecneWypozyczenia);
                                entityManager.getTransaction().commit();

                            }
                        }

                        k = 1;
                        break;
                    }

                }

                if (k != 1) {
                    textPopup.setText("Błędne dane klienta. Prosze podać poprawne!");
                    textPopup.setFill(Color.RED);

                }

                entityManager.close();
                entityManagerFactory.close();

            }
        });
    }

    /**
     * Wyszukuje książki w tabelach wysweitlajacych ksiązki.
     *
     * @param ksiazkiTable      Tabela wyswietlajaca ksiazki.
     * @param observableKsiazki Pozycje wyswietlone w tabeli.
     * @param wyszukaj          Wartość ktora została wpisana w pole wyszukiwania.
     */
    public void wyszukajKsiazki(TableView<Ksiazki> ksiazkiTable, ObservableList<Ksiazki> observableKsiazki, TextField wyszukaj) {
        FilteredList<Ksiazki> filteredKsiazki = new FilteredList<>(observableKsiazki, b -> true);

        wyszukaj.textProperty().addListener((observableValue, oldValue, newValue) -> {

            filteredKsiazki.setPredicate(ksiazki -> {

                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (ksiazki.getTytul().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (ksiazki.getAutor().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (ksiazki.getWydawca().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else if (ksiazki.getDataWydania().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true;
                } else return false;
            });

        });

        SortedList<Ksiazki> sortedKsiazki = new SortedList<>(filteredKsiazki);

        sortedKsiazki.comparatorProperty().bind(ksiazkiTable.comparatorProperty());

        ksiazkiTable.setItems(sortedKsiazki);

    }

    /**
     * Uniemozliwa wpisywanie liczb do textFielda do ktorego funkcja zostala przypisana.
     */
    public void validate(KeyEvent event) {

        TextField field = (TextField) event.getTarget();

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            for (char c : "1234567890".toCharArray()) {
                if (text.indexOf(c) != -1) {
                    return null;
                }
            }
            return change;
        });

        field.setTextFormatter(textFormatter);
    }

    /**
     * Uniemozliwa wpisywanie liter do textFielda do ktorego funkcja zostala przypisana.
     */
    public void validateTel(KeyEvent event) {

        TextField field = (TextField) event.getTarget();

        TextFormatter<String> textFormatter = new TextFormatter<>(change -> {
            if (!change.isContentChange()) {
                return change;
            }

            String text = change.getControlNewText();

            for (char c : text.toCharArray()) {
                if ("1234567890".indexOf(c) == -1) return null;
            }
            return change;
        });

        field.setTextFormatter(textFormatter);
    }

    /**
     * Uniemozliwa wpisywanie ciagu znaku dluzszego niz 9 do przypisanego textFielda.
     */
    public void dlugoscTel(KeyEvent event) {

        final int maxLength = 9;

        TextField field = (TextField) event.getTarget();

        field.setOnKeyTyped(t -> {

            if (field.getText().length() > maxLength) {
                int pos = field.getCaretPosition();
                field.setText(field.getText(0, maxLength));
                field.positionCaret(pos);
            }

        });

    }

    /**
     * Uniemozliwa wpisywanie ciagu znaku dluzszego niz 4 do przypisanego textFielda.
     */
    public void dlugoscRok(KeyEvent event) {

        final int maxLength = 4;

        TextField field = (TextField) event.getTarget();

        field.setOnKeyTyped(t -> {

            if (field.getText().length() > maxLength) {
                int pos = field.getCaretPosition();
                field.setText(field.getText(0, maxLength));
                field.positionCaret(pos);
            }
        });

    }

}
