package com.projektzaliczeniowy.projektzaliczeniowy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Reprezentuje książke w bazie danych.
 */
@Entity
@Table(name = "ksiazki_table")
public class Ksiazki implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idKsiazki;
    @Column(name = "tytul")
    private String tytul;
    @Column(name = "autor")
    private String autor;
    @Column(name = "wydawca")
    private String wydawca;
    @Column(name = "dataWydania", length = 4)
    private String dataWydania;
    @Column(name = "stanWypozyczenia")
    private boolean stanWypozyczenia;

    /**
     * Pusty konstruktor do klasy Ksiazki.
     */
    public Ksiazki() {

    }

    /**
     * Konstruktor do klasy Wypozczyenia nadającym wszytskim polom wartosci.
     *
     * @param idKsiazki   Automatycznie generowanie Id ksiazki.
     * @param tytul       Tytuł ksiazki.
     * @param autor       Autor ksiazki.
     * @param wydawca     Wydawca ksiazki.
     * @param dataWydania Rok wydania ksiazki w formacie String.
     */
    public Ksiazki(Long idKsiazki, String tytul, String autor, String wydawca, String dataWydania, boolean stanWypozyczenia) {

        this.idKsiazki = idKsiazki;
        this.tytul = tytul;
        this.autor = autor;
        this.wydawca = wydawca;
        this.dataWydania = dataWydania;
        this.stanWypozyczenia = stanWypozyczenia;

    }

    public Long getIdKsiazki() {
        return idKsiazki;
    }

    public void setIdKsiazki(Long idKsiazki) {
        this.idKsiazki = idKsiazki;
    }

    public String getTytul() {
        return tytul;
    }

    public void setTytul(String tytul) {
        this.tytul = tytul;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getWydawca() {
        return wydawca;
    }

    public void setWydawca(String wydawca) {
        this.wydawca = wydawca;
    }

    public String getDataWydania() {
        return dataWydania;
    }

    public void setDataWydania(String dataWydania) {
        this.dataWydania = dataWydania;
    }

    public boolean isStanWypozyczenia() {
        return stanWypozyczenia;
    }

    public void setStanWypozyczenia(boolean stanWypozyczenia) {
        this.stanWypozyczenia = stanWypozyczenia;
    }

    /**
     * Wyświetla wszystkie dane ksiazki w formie tekstu
     */
    @Override
    public String toString() {
        return "Ksiazki{" +
                "idKsiazki=" + idKsiazki +
                ", tytul='" + tytul + '\'' +
                ", autor='" + autor + '\'' +
                ", wydawca='" + wydawca + '\'' +
                ", dataWydania='" + dataWydania + '\'' +
                ", stanWypozyczenia=" + stanWypozyczenia +
                '}';
    }

}
