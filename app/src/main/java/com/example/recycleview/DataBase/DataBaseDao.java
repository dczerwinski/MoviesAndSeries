package com.example.recycleview.DataBase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DataBaseDao {
    @Query("SELECT * FROM movies ORDER BY title ASC")
    List<DataBase> getAll();

    @Query("SELECT * FROM movies WHERE title like :vtitle limit 1")
    DataBase findByName(String vtitle);

    @Query("SELECT * FROM movies WHERE uid like :id limit 1")
    DataBase findById(int id);

    @Query("select * from movies where director like :dir")
    List<DataBase> findByDirector(String dir);

    @Query("select * from movies where category like :cat")
    List<DataBase> findByCategory(String cat);

    @Query("delete  from movies where title like :vtitle")
    void deleteByTitle(String vtitle);

    @Query("delete from movies where category like :vcat")
    void deleteCatygory(String vcat);

    @Insert
    void insertAll(DataBase... dataBases);

    @Insert
    void insertMovie(DataBase dataBase);

    @Delete
    void delete(DataBase dataBase);

    @Insert
    void insertAll(List<DataBase> dataBases);
}
