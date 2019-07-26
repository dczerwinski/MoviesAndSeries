package com.example.recycleview.DataBase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import java.util.ArrayList;
import java.util.List;

@Database(entities = {DataBase.class}, version =  1)
public abstract class AppDatabase  extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public abstract DataBaseDao dataBaseDao();

    public static AppDatabase getAppDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, "movies-database").
                    addCallback(new RoomDatabase.Callback(){
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            final List<DataBase> dataBases = new ArrayList<>();
                            dataBases.add(new DataBase("Back to the future", "Robert Zemeckis", "bttf", "movie", 1, "Sci-fi"));
                            dataBases.add(new DataBase("Stranger Things", " Matt i Ross Dufferowie", "st", "series", 8, "Sci-fi"));
                            dataBases.add(new DataBase("Game of thrones", "Tim Van Patten Brian Kirk Daniel Minahan", "got", "series", 60, "Fantasy"));
                            dataBases.add(new DataBase("Pirates of the Caribbean", " Gore Verbinski", "potc", "movie", 10, "Pirates"));
                            dataBases.add(new DataBase("Dr House", "Bryan Singer", "dh", "series", 177, "Medicine"));
                            dataBases.add(new DataBase("Pirates of the Caribbean At World's End", " Gore Verbinski", "potcawe", "movie", 10, "Pirates"));
                            dataBases.add(new DataBase("Grey's Anatomy", "Shonda Rhimes", "ga", "series", 338, "Medicine"));

                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getAppDatabase(context).dataBaseDao().insertAll(dataBases);
                                }
                            }).start();
                        }
                    }).allowMainThreadQueries().build();

        }
        return INSTANCE;
    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}