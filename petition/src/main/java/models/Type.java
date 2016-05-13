package models;

/**
 * Created by Maxim on 06.04.2016.
 */
public class Type {
    String id;
    String name_ua = "Всеукраїнське";
    String name_ru;
    String name_en;
    String needs = "25000";

    public Type(String id, String name_ua, String name_ru, String name_en, String needs) {
        this.id = id;
        this.name_ua = name_ua;
        this.name_ru = name_ru;
        this.name_en = name_en;
        this.needs = needs;
    }
}
