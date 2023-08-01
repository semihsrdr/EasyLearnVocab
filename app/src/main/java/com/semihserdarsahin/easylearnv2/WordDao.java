package com.semihserdarsahin.easylearnv2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;

@Dao
public interface WordDao {

    @Query("SELECT * FROM word")
    Flowable<List<Word>> getall();

    @Insert
    Completable insert(Word word);

    @Delete
    Completable delete(Word word);
}
