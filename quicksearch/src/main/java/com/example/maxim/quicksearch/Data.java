package com.example.maxim.quicksearch;

import java.io.Serializable;

/**
 * Created by Maxim on 10.01.2016.
 */
public class Data implements Serializable{
    String pathFile;
    String nameFile;
    int avatarFile;

    public Data(String pathFile, String nameFile, int avatarFile) {
        this.pathFile = pathFile;
        this.nameFile = nameFile;
        this.avatarFile = avatarFile;
    }
}
