package com.semihserdarsahin.easylearnv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.semihserdarsahin.easylearnv2.databinding.ActivityMainBinding;
import com.semihserdarsahin.easylearnv2.databinding.RecyclerRowBinding;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    AppDataBase db;
    WordDao wordDao;
    int max;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        db= Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"word").build();
        wordDao=db.wordDao();
        compositeDisposable.add(wordDao.getall().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()
                ).subscribe(MainActivity.this::handleResponse));

    }
    private void handleResponse(List<Word> wordList){
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        WordAdapter wordAdapter=new WordAdapter(wordList);
        binding.recyclerView.setAdapter(wordAdapter);

        max=wordList.size();

        if (wordList.size()==0){
            binding.textView5.setText("THERE IS NOTHING TO SHOW ANYTHING. PLEASE ADD NEW WORD.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.mymenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.repeat){
            if (max>=0){
                Intent intent=new Intent(MainActivity.this,RepeatExcersizeActivity.class);
                startActivity(intent);
                finish();
            }
            else{
                Toast.makeText(this, "You Need To Have At Least 50 Words To Use This Excersize", Toast.LENGTH_LONG).show();
            }

        }
        else{
            Intent intent=new Intent(MainActivity.this,AddingActivity.class);
            startActivity(intent);
            finish();
        }


        return super.onOptionsItemSelected(item);
    }
}