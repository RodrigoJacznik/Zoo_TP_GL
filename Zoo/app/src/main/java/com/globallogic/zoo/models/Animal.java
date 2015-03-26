package com.globallogic.zoo.models;

import android.text.format.Time;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by GL on 19/03/2015.
 */
public class Animal implements Serializable {
    private static final long serialVersionUID = 4851874892403066514L;
    private String nombre;
    private String especie;
    private String descripcion;
    private String url;
    private boolean favorite;
    private int especieCode;
    private List<Horario> horarios;

    public int getEspecieCode() {
        return especieCode;
    }

    public void setEspecieCode(int especieCode) {
        this.especieCode = especieCode;
    }

    public Animal(String nombre, String especie, String descripcion, int especieCode) {
        this.nombre = nombre;
        this.especie = especie;
        this.descripcion = descripcion;
        this.especieCode = especieCode;
        this.url = "http://es.wikipedia.org/wiki/" + this.nombre;
        horarios = generateHorarios();
        this.favorite = false;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }

    static private List<Horario> generateHorarios() {
        List<Horario> horarios = new ArrayList<>();
        horarios.add(new Horario("Lunes", "10:30", "15:30"));
        horarios.add(new Horario("Martes", "11:30", "15:30"));
        horarios.add(new Horario("Miercoles", "9:30", "15:30"));
        horarios.add(new Horario("Viernes", "10:30", "15:30"));

        return horarios;
    }

    static public List<Animal> getAnimalList() {
        List<Animal> animals = new ArrayList<>();

        String descripcion = "Lorem Ipsum is simply dummy text of the printing and typesetting " +
                "industry. Lorem Ipsum has been the industry's standard dummy text ever since the" +
                " 1500s, when an unknown printer took a galley of type and scrambled it to make a" +
                " type specimen book. It has survived not only five centuries, but also the leap" +
                " into electronic typesetting, remaining essentially unchanged. It was popularised" +
                " in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages," +
                " and more recently with desktop publishing software like Aldus PageMaker including" +
                " versions of Lorem Ipsum.";
        animals.add(new Animal("Leon", "Especie del leon", descripcion, 1));
        animals.add(new Animal("Mono", "Especie del mono", descripcion, 2));
        animals.add(new Animal("Vaca", "Especie del vaca", descripcion, 3));
        animals.add(new Animal("Gorila", "Especie del Gorila", descripcion, 4));
        animals.add(new Animal("Leon", "Especie del leon", descripcion, 1));
        animals.add(new Animal("Mono", "Especie del mono", descripcion, 2));
        animals.add(new Animal("Vaca", "Especie del vaca", descripcion, 3));
        animals.add(new Animal("Gorila", "Especie del Gorila", descripcion, 4));

        return animals;
    }
}
