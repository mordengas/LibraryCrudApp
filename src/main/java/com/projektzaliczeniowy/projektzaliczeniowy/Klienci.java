package com.projektzaliczeniowy.projektzaliczeniowy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Reprezentuje klienta w bazie danych.
 */
@Entity
@Table(name = "klienci_table")
public class Klienci implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idKlienta;
    @Column(name = "imie")
    private String imie;
    @Column(name = "nazwisko")
    private String nazwisko;
    @Column(name = "numerTel", length = 12)
    private int numerTel;


    /**
     * Pusty konstruktor do klasy Klienci.
     */
    public Klienci() {

    }

    /**
     * Konstruktor do klasy Wypozczyenia nadającym wszytskim polom wartosci.
     *
     * @param idKlienta Automatycznie generowanie Id klienca.
     * @param imie      Imie klienta.
     * @param nazwisko  Nazwisko klienta.
     * @param numerTel  Numer telefonu klienta w formacie int.
     */
    public Klienci(Long idKlienta, String imie, String nazwisko, int numerTel) {
        this.idKlienta = idKlienta;
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.numerTel = numerTel;
    }

    public Long getIdKlienta() {
        return idKlienta;
    }

    public void setIdKlienta(Long idKlienta) {
        this.idKlienta = idKlienta;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public int getNumerTel() {
        return numerTel;
    }

    public void setNumerTel(int numerTel) {
        this.numerTel = numerTel;
    }


    /**
     * Wyświetla wszystkie dane klienta w formie tekstu
     */
    @Override
    public String toString() {
        return "Klienci{" +
                "idKlienta=" + idKlienta +
                ", imie='" + imie + '\'' +
                ", nazwisko='" + nazwisko + '\'' +
                ", numerTel=" + numerTel +
                '}';
    }
}
