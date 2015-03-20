package com.globallogic.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GL on 19/03/2015.
 */
public class Animal implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    private String nombre;
    private String especie;
    private String descripcion;
    private int especieCode;

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

    @Override
    public String toString() {
        return "Animal{" +
                "nombre='" + nombre + '\'' +
                ", especie='" + especie + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
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
