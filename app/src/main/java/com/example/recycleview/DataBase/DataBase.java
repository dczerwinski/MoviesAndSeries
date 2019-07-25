package com.example.recycleview.DataBase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "movies")
public class DataBase{
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "director")
    public String director;

    @ColumnInfo(name = "img_file")
    public String img_file;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "number")
    public int number;

    @ColumnInfo(name = "category")
    public String category;

    public DataBase(String title, String director, String img_file, String type, int number, String category) {
        this.title = title;
        this.director = director;
        this.img_file = img_file;
        this.type = type;
        this.number = number;
        this.category = category;
        if(type == "movie")this.number = 1;
    }
}
