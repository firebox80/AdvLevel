package models;

/**
 * Created by Maxim on 06.04.2016.
 */
public class Categories {
    public String id;
    public String name_ua = "Житлово-комунальне господарство";
    String name_ru;
    String name_en;
    String color = "9b59b6";

    public Categories(String id, String name_ua, String name_ru, String name_en, String color) {
        this.id = id;
        this.name_ua = name_ua;
        this.name_ru = name_ru;
        this.name_en = name_en;
        this.color = color;
    }
}
