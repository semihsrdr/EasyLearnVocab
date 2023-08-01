package com.semihserdarsahin.easylearnv2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Word.class},version = 1)
public abstract class AppDataBase extends RoomDatabase {
    public abstract WordDao wordDao();
}
