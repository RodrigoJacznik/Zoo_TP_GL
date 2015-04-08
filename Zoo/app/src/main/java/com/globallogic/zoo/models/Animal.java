package com.globallogic.zoo.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by GL on 19/03/2015.
 */
public class Animal implements Serializable {
    private static final long serialVersionUID = 4851874892403066514L;
    private static int nextId = 0;

    private int id;
    private String name;
    private String specie;
    private String description;
    private String url;
    private boolean favorite;
    private int specieCode;

    private List<Schudle> schudle;
    public static List<Animal> animals;

    public Animal(String name, String specie, String description, int specieCode) {
        this.id = nextId++;
        this.name = name;
        this.specie = specie;
        this.description = description;
        this.specieCode = specieCode;
        this.url = "http://es.wikipedia.org/wiki/" + this.name;
        this.schudle = generateHorarios();
        this.favorite = false;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", specie='" + specie + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    static private List<Schudle> generateHorarios() {
        List<Schudle> schudles = new ArrayList<>();

        Date now = new Date();

        schudles.add(new Schudle(now, Schudle.addOneHoursToDate(now, 2)));
        schudles.add(new Schudle(Schudle.addOneHoursToDate(now, 26),
                Schudle.addOneHoursToDate(now, 28)));
        schudles.add(new Schudle(Schudle.addOneHoursToDate(now, 50),
                Schudle.addOneHoursToDate(now, 55)));
        schudles.add(new Schudle(Schudle.addOneHoursToDate(now, 76),
                Schudle.addOneHoursToDate(now, 80)));


        return schudles;
    }

    static public List<Animal> getAnimalList() {
        if (animals == null) {
            animals = generateAnimals();
        }
        return animals;
    }

    static private List<Animal> generateAnimals() {
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

    static public Animal getById(int id) {
        if (id == -1) {
            return null;
        }
        for (Animal animal: animals) {
            if (animal.getId() == id) {
                return animal;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecie() {
        return specie;
    }

    public void setSpecie(String specie) {
        this.specie = specie;
    }

    public String getDescripcion() {
        return description;
    }

    public void setDescripcion(String description) {
        this.description = description;
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

    public List<Schudle> getSchudle() {
        return schudle;
    }

    public void setSchudle(List<Schudle> schudle) {
        this.schudle = schudle;
    }

    public int getSpecieCode() {
        return specieCode;
    }

    public void setSpecieCode(int specieCode) {
        this.specieCode = specieCode;
    }

    static public boolean deleteAnimal(int animalID) {
        Animal animal = animals.remove(animalID);
        return animal != null;
    }

    static public boolean deleteAnimal(Animal animal) {
        return animals.remove(animal);
    }

    public int getId() {
        return id;
    }
}
