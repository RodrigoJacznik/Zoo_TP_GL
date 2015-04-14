package com.globallogic.zoo.models;

import com.globallogic.zoo.helpers.ZooDatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GL on 19/03/2015.
 */
public class Animal {
    public static List<Animal> animals = new ArrayList<>();

    private long id;
    private String name;
    private String specie;
    private String description;
    private String image;
    private String moreInfo;
    private List<Show> shows;

    private boolean favorite;

    public Animal(long id, String name, String specie, String description, String image,
                  String moreInfo, List<Show> shows) {

        this(id, name, specie, description, image, moreInfo);
        this.shows = shows;
    }

    public Animal(long id, String name, String specie, String description, String image,
                  String moreInfo) {
        this.id = id;
        this.name = name;
        this.specie = specie;
        this.description = description;
        this.image = image;
        this.moreInfo = moreInfo;
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

    static public Animal getById(long id) {
        if (id == -1 || animals.isEmpty()) {
            return null;
        }
        for (Animal animal : animals) {
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

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public List<Show> getShow() {
        return shows;
    }

    public void setShow(List<Show> shows) {
        this.shows = shows;
    }

    public static List<Animal> getAnimals() {
        return animals;
    }

    public static void setAnimals(List<Animal> animals) {
        Animal.animals = animals;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
    }

    static public boolean deleteAnimal(Animal animal) {
        return animals.remove(animal);
    }

    static public boolean deleteAnimal(int animalID) {
        Animal animal = animals.remove(animalID);
        return animal != null;
    }

    public long getId() {
        return id;
    }

    public static Animal fromJson(String str) throws JSONException {
        JSONObject jAnimal = new JSONObject(str);
        long id = jAnimal.getLong("id");

        String name = jAnimal.getString("name");
        String specie = jAnimal.getString("specie");
        String description = jAnimal.getString("description");
        String image = jAnimal.getString("image");

        List<Show> shows = new ArrayList<>();
        JSONArray jShows = jAnimal.getJSONArray("shows");
        for (int i = 0; i < jShows.length(); i++) {
            shows.add(Show.fromJson(jShows.get(i).toString()));
        }
        String moreInfo = jAnimal.getString("moreInfo");

        return new Animal(id, name, specie, description, image, moreInfo, shows);
    }

}
