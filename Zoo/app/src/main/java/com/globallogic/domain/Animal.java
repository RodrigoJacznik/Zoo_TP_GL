package com.globallogic.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GL on 19/03/2015.
 */
public class Animal {
    private String nombre;
    private String especie;
    private String descripcion;

    public Animal(String nombre, String especie, String descripcion) {
        this.nombre = nombre;
        this.especie = especie;
        this.descripcion = descripcion;
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
        animals.add(new Animal("Leon", "Especie del leon", "descripcion del leon"));
        animals.add(new Animal("Mono", "Especie del mono", "descripcion del mono"));
        animals.add(new Animal("Vaca", "Especie del vaca", "descripcion del vaca"));
        animals.add(new Animal("Gorila", "Especie del Gorila", "descripcion del Gorila"));
        animals.add(new Animal("Leon", "Especie del leon", "descripcion del leon"));
        animals.add(new Animal("Mono", "Especie del mono", "descripcion del mono"));
        animals.add(new Animal("Vaca", "Especie del vaca", "descripcion del vaca"));
        animals.add(new Animal("Gorila", "Especie del Gorila", "descripcion del Gorila"));

        return animals;
    }
}
