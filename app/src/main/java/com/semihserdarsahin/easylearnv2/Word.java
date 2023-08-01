package com.semihserdarsahin.easylearnv2;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Word implements Serializable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "word")
    public String word;

    @ColumnInfo(name = "sentence")
    public String sentence;

    @ColumnInfo(name = "meaning")
    public String meaning;

    @ColumnInfo(name = "turkish")
    public String turkish;


    public Word(String word, String sentence, String meaning, String turkish) {
        this.word = word;
        this.sentence = sentence;
        this.meaning = meaning;
        this.turkish = turkish;


    }
}
