package com.projektzaliczeniowy.projektzaliczeniowy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Reprezentuje wypożyczenie w bazie danych.
 */
@Entity
@Table(name = "wypozyczenia_table")
public class Wypozyczenia implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long idWypozyczenia;

    @Column(name = "idKsiazki")
    private Long idKsiazki;

    @Column(name = "idKlienta")
    private Long idKlienta;

    @Column(name = "dataWypozyczenia")
    private LocalDate dataWypozyczenia;

    @Column(name = "dataZwrotu")
    private LocalDate dataZwrotu;

    /**
     * Pusty konstruktor do klasy Wypozczyenia.
     */
    public Wypozyczenia() {

    }

    /**
     * Konstruktor do klasy Wypozczyenia nadającym wszytskim polom wartosci.
     *
     * @param idWypozyczenia   Automatycznie generowanie Id wypozyczenia.
     * @param idKlienta        Id klienta wypozyczającego.
     * @param idKsiazki        id ksiazki wypożyczanej.
     * @param dataWypozyczenia Data wypożyczenia w formacie LocalDate.
     * @param dataZwrotu       Data zwrotu w formacie LocalDate.
     */
    public Wypozyczenia(Long idWypozyczenia, Long idKsiazki, Long idKlienta, LocalDate dataWypozyczenia, LocalDate dataZwrotu) {
        this.idWypozyczenia = idWypozyczenia;
        this.idKsiazki = idKsiazki;
        this.idKlienta = idKlienta;
        this.dataWypozyczenia = dataWypozyczenia;
        this.dataZwrotu = dataZwrotu;
    }

    public Long getIdWypozyczenia() {
        return idWypozyczenia;
    }

    public void setIdWypozyczenia(Long idWypozyczenia) {
        this.idWypozyczenia = idWypozyczenia;
    }

    public Long getIdKsiazki() {
        return idKsiazki;
    }

    public void setIdKsiazki(Long idKsiazki) {
        this.idKsiazki = idKsiazki;
    }

    public Long getIdKlienta() {
        return idKlienta;
    }

    public void setIdKlienta(Long idKlienta) {
        this.idKlienta = idKlienta;
    }

    public LocalDate getDataWypozyczenia() {
        return dataWypozyczenia;
    }

    public void setDataWypozyczenia(LocalDate dataWypozyczenia) {
        this.dataWypozyczenia = dataWypozyczenia;
    }

    public LocalDate getDataZwrotu() {
        return dataZwrotu;
    }

    public void setDataZwrotu(LocalDate dataZwrotu) {
        this.dataZwrotu = dataZwrotu;
    }

    /**
     * Wyświetla wszystkie dane wypożyczeń w formie tekstu
     */
    @Override
    public String toString() {
        return "Wypozyczenia{" +
                "idWypozyczenia=" + idWypozyczenia +
                ", idKsiazki=" + idKsiazki +
                ", idKlienta=" + idKlienta +
                ", dataWypozyczenia=" + dataWypozyczenia +
                ", dataZwrotu=" + dataZwrotu +
                '}';
    }

}

