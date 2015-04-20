package com.globallogic.zoo.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GL on 19/03/2015.
 */
public class Animal {

    private long id;
    private String name;
    private String specie;
    private String description;
    private String image;
    private String moreInfo;
    private List<Show> shows;

    private boolean favorite;

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

    public Animal(long id, String name, String specie, String description, String image,
                  String moreInfo, List<Show> shows) {

        this(id, name, specie, description, image, moreInfo);
        this.shows = shows;
    }

    public Animal(long id, String name, String specie, String description, String image,
                         String moreInfo, int isFavorite) {
        this(id, name, specie, description, image, moreInfo);
        this.favorite = isFavorite == 1;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "name='" + name + '\'' +
                ", specie='" + specie + '\'' +
                ", description='" + description + '\'' +
                '}';
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

    public String getDescripcion() {
        return description;
    }

    public String getMoreInfo() {
        return moreInfo;
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

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public List<Show> getShows() {
        return shows;
    }

    public void setShows(List<Show> shows) {
        this.shows = shows;
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
            shows.add(Show.fromAnimalJson(jShows.get(i).toString()));
        }
        String moreInfo = jAnimal.getString("moreInfo");

        return new Animal(id, name, specie, description, image, moreInfo, shows);
    }

}
