package com.semihserdarsahin.easylearnv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.semihserdarsahin.easylearnv2.databinding.ActivityAddingBinding;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AddingActivity extends AppCompatActivity {
    private ActivityAddingBinding binding;
    AppDataBase db;
    WordDao wordDao;



    CompositeDisposable compositeDisposable=new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddingBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        db= Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"word").build();
        wordDao=db.wordDao();



    }
    public void save(View view){
        String wordName=binding.editTextWord.getText().toString();
        String sentence=binding.editTextExample.getText().toString();
        String turkish=binding.editTextTurkish.getText().toString();
        String meaning=binding.editTextMeaning.getText().toString();

        if (wordName.matches("")||meaning.matches("")){
            Toast.makeText(this, "You Have To Fiil 'Word' Blank and 'Meaning' Blank", Toast.LENGTH_LONG).show();
        }
        else{
            Word word=new Word(wordName,sentence,meaning,turkish);

            compositeDisposable.add(wordDao.insert(word).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread()).subscribe(AddingActivity.this::handleResponse));
        }




    }

    private void handleResponse(){
        Intent intent=new Intent(AddingActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.addingmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}