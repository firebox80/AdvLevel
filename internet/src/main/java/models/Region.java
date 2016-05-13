package models;

/**
 * Created by Maxim on 06.04.2016.
 */
public class Region {
    String id;
    String name_ua;
    String name_ru;
    String name_en;

    public Region(String id, String name_ua, String name_ru, String name_en) {
        this.id = id;
        this.name_ua = name_ua;
        this.name_ru = name_ru;
        this.name_en = name_en;
    }
}
