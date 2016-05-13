package models;

/**
 * Created by Maxim on 07.04.2016.
 */
public class Petition {

    String id = "0";
    public String votes = "0";
    String petition_type = "1";
    public String petition_category = "7";
    public String title =  "Врятуймо науку України!";
    public String created = "2016-02-24 11:32:47";
    String finished = "2016-05-24 11:32:58";
    String name_ua = "Президент України";

    public Petition(String id, String votes, String petition_type, String petition_category, String title, String created, String finished, String name_ua) {
        this.id = id;
        this.votes = votes;
        this.petition_type = petition_type;
        this.petition_category = petition_category;
        this.title = title;
        this.created = created;
        this.finished = finished;
        this.name_ua = name_ua;
    }
}
