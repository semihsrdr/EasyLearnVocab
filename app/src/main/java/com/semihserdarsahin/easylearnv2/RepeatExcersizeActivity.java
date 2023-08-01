package com.semihserdarsahin.easylearnv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.semihserdarsahin.easylearnv2.databinding.ActivityAddingBinding;
import com.semihserdarsahin.easylearnv2.databinding.ActivityRepeatExcersizeBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RepeatExcersizeActivity extends AppCompatActivity {
    private ActivityRepeatExcersizeBinding binding;
    AppDataBase db;
    WordDao wordDao;
    Word word;
    Random random;
    int question;
    int maxSize;
    int score;
    String green="#00fe08";
    ArrayList<Integer> intList;
    ArrayList<Integer> randomList;
    TextToSpeech mTTS;
    MediaPlayer player;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRepeatExcersizeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        score=0;
        binding.textscore.setText(String.valueOf(score));

        random=new Random();

        db= Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"word").build();
        wordDao=db.wordDao();

        Intent intent=getIntent();
        word=(Word)intent.getSerializableExtra("word");

        compositeDisposable.add(wordDao.getall().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(RepeatExcersizeActivity.this::handleResponse));
        intList=new ArrayList<Integer>();


        mTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    int result=mTTS.setLanguage(Locale.US);
                }
            }
        });


    }
    public void wrong(){
        player=null;
        if (player==null){
            player=MediaPlayer.create(getApplicationContext(),R.raw.wrong);
        }
        player.start();
    }
    public void correct(){
        player=null;
        if (player==null){
            player=MediaPlayer.create(getApplicationContext(),R.raw.correct);
        }
        player.start();
    }


    public void handleResponse(List<Word> wordList){
        question++;
        binding.textQuestion.setText("Question "+question);

        maxSize=wordList.size();
        int questionNumber=random.nextInt(maxSize);

        intList=new ArrayList<>();
        randomList=new ArrayList<>();

        binding.textscore.setText(String.valueOf(score));

        //putting procces numbers until size of the wordlist into array
        for (int i=0;i<maxSize-1;i++){
            intList.add(i);
        }

        Collections.shuffle(intList);
        for (int i=0;i<maxSize-1;i++){
            System.out.println(intList);
        }


        binding.choices.setText(wordList.get(questionNumber).meaning);
        int trueAnswer=random.nextInt(4);
        if (trueAnswer==0){
            binding.buttonA.setText(wordList.get(questionNumber).word);
        }
        else if (trueAnswer==1){
            binding.buttonB.setText(wordList.get(questionNumber).word);
        }
        else if (trueAnswer==2){
            binding.buttonC.setText(wordList.get(questionNumber).word);
        }
        else if (trueAnswer==3){
            binding.buttonD.setText(wordList.get(questionNumber).word);
        }

        for (int i=0;i<4;i++){
            if (i!=trueAnswer&&i==0){
                binding.buttonA.setText(wordList.get(intList.get(i)).word);
            }
            else if (i!=trueAnswer&&i==1){
                binding.buttonB.setText(wordList.get(intList.get(i)).word);
            }
            else if (i!=trueAnswer&&i==2){
                binding.buttonC.setText(wordList.get(intList.get(i)).word);
            }
            else if (i!=trueAnswer&&i==3)
                binding.buttonD.setText(wordList.get(intList.get(i)).word);
        }

        binding.buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buttonA.setEnabled(false);
                binding.buttonB.setEnabled(false);
                binding.buttonC.setEnabled(false);
                binding.buttonD.setEnabled(false);
                binding.buttonC.setTextColor(Color.parseColor("#050505"));
                binding.buttonD.setTextColor(Color.parseColor("#050505"));
                binding.buttonB.setTextColor(Color.parseColor("#050505"));
                binding.buttonA.setTextColor(Color.parseColor("#050505"));

                if (binding.buttonA.getText().toString().matches(wordList.get(questionNumber).word)){
                    binding.buttonA.setBackgroundColor(Color.parseColor("#00fe08"));
                    score++;
                    binding.textscore.setText(String.valueOf(score));
                    correct();
                }
                else {
                    binding.buttonA.setBackgroundColor(Color.parseColor("#cc0000"));
                    if (trueAnswer == 1) {
                        binding.buttonB.setBackgroundColor(Color.parseColor("#00fe08"));
                        wrong();
                    } else if (trueAnswer == 2) {
                        binding.buttonC.setBackgroundColor(Color.parseColor("#00fe08"));
                        wrong();
                    } else if (trueAnswer == 3) {
                        binding.buttonD.setBackgroundColor(Color.parseColor("#00fe08"));
                        wrong();
                    }
                }
                Handler handler= new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTTS.speak(wordList.get(questionNumber).word,TextToSpeech.QUEUE_FLUSH,null);
                    }
                },700);            }
        });

        binding.buttonB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buttonA.setEnabled(false);
                binding.buttonB.setEnabled(false);
                binding.buttonC.setEnabled(false);
                binding.buttonD.setEnabled(false);
                binding.buttonC.setTextColor(Color.parseColor("#050505"));
                binding.buttonD.setTextColor(Color.parseColor("#050505"));
                binding.buttonB.setTextColor(Color.parseColor("#050505"));
                binding.buttonA.setTextColor(Color.parseColor("#050505"));
                if (binding.buttonB.getText().toString().matches(wordList.get(questionNumber).word)){
                    binding.buttonB.setBackgroundColor(Color.parseColor("#00fe08"));
                    score++;
                    binding.textscore.setText(String.valueOf(score));
                    correct();
                }
                else {
                    binding.buttonB.setBackgroundColor(Color.parseColor("#cc0000"));
                    if (trueAnswer == 0) {
                        binding.buttonA.setBackgroundColor(Color.parseColor("#00fe08"));
                        wrong();
                    } else if (trueAnswer == 2) {
                        binding.buttonC.setBackgroundColor(Color.parseColor("#00fe08"));
                        wrong();
                    } else if (trueAnswer == 3) {
                        wrong();
                        binding.buttonD.setBackgroundColor(Color.parseColor("#00fe08"));
                    }
                }
                Handler handler= new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTTS.speak(wordList.get(questionNumber).word,TextToSpeech.QUEUE_FLUSH,null);
                    }
                },700);            }
        });

        binding.buttonC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buttonA.setEnabled(false);
                binding.buttonB.setEnabled(false);
                binding.buttonC.setEnabled(false);
                binding.buttonD.setEnabled(false);
                binding.buttonC.setTextColor(Color.parseColor("#050505"));
                binding.buttonD.setTextColor(Color.parseColor("#050505"));
                binding.buttonB.setTextColor(Color.parseColor("#050505"));
                binding.buttonA.setTextColor(Color.parseColor("#050505"));

                if (binding.buttonC.getText().toString().matches(wordList.get(questionNumber).word)){
                    binding.buttonC.setBackgroundColor(Color.parseColor("#00fe08"));
                    score++;
                    binding.textscore.setText(String.valueOf(score));
                    correct();
                }
                else{
                    binding.buttonC.setBackgroundColor(Color.parseColor("#cc0000"));
                    if (trueAnswer==0){
                        binding.buttonA.setBackgroundColor(Color.parseColor("#00fe08"));
                        wrong();
                    } else if (trueAnswer==1) {
                        binding.buttonB.setBackgroundColor(Color.parseColor("#00fe08"));
                        wrong();
                    } else if (trueAnswer==3) {
                        binding.buttonD.setBackgroundColor(Color.parseColor("#00fe08"));
                        wrong();
                    }
                }
                Handler handler= new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTTS.speak(wordList.get(questionNumber).word,TextToSpeech.QUEUE_FLUSH,null);
                    }
                },700);
            }
        });

        binding.buttonD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buttonA.setEnabled(false);
                binding.buttonB.setEnabled(false);
                binding.buttonC.setEnabled(false);
                binding.buttonD.setEnabled(false);
                binding.buttonC.setTextColor(Color.parseColor("#050505"));
                binding.buttonD.setTextColor(Color.parseColor("#050505"));
                binding.buttonB.setTextColor(Color.parseColor("#050505"));
                binding.buttonA.setTextColor(Color.parseColor("#050505"));

                if (binding.buttonD.getText().toString().matches(wordList.get(questionNumber).word)){
                    binding.buttonD.setBackgroundColor(Color.parseColor("#00fe08"));
                    score++;
                    binding.textscore.setText(String.valueOf(score));
                    correct();
                }
                else{
                    binding.buttonD.setBackgroundColor(Color.parseColor("#cc0000"));
                    if (trueAnswer==0){
                        binding.buttonA.setBackgroundColor(Color.parseColor("#00fe08"));
                        wrong();
                    } else if (trueAnswer==1) {
                        binding.buttonB.setBackgroundColor(Color.parseColor("#00fe08"));
                        wrong();
                    } else if (trueAnswer==2) {
                        binding.buttonC.setBackgroundColor(Color.parseColor("#00fe08"));
                        wrong();
                    }
                }
                Handler handler= new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mTTS.speak(wordList.get(questionNumber).word,TextToSpeech.QUEUE_FLUSH,null);
                    }
                },700);
            }
        });

        binding.button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.buttonA.setBackgroundColor(Color.parseColor("#8A2BE2"));
                binding.buttonB.setBackgroundColor(Color.parseColor("#8A2BE2"));
                binding.buttonC.setBackgroundColor(Color.parseColor("#8A2BE2"));
                binding.buttonD.setBackgroundColor(Color.parseColor("#8A2BE2"));

                binding.buttonA.setTextColor(Color.parseColor("#050505"));
                binding.buttonB.setTextColor(Color.parseColor("#050505"));
                binding.buttonC.setTextColor(Color.parseColor("#050505"));
                binding.buttonD.setTextColor(Color.parseColor("#050505"));

                binding.buttonA.setEnabled(true);
                binding.buttonB.setEnabled(true);
                binding.buttonC.setEnabled(true);
                binding.buttonD.setEnabled(true);

                question++;
                binding.textQuestion.setText("Question "+question);

                maxSize=wordList.size();
                int questionNumber=random.nextInt(maxSize);

                intList=new ArrayList<>();
                randomList=new ArrayList<>();


                //putting procces numbers until size of the wordlist into array
                for (int i=0;i<maxSize-1;i++){
                    intList.add(i);
                }

                Collections.shuffle(intList);
                for (int i=0;i<maxSize-1;i++){
                    System.out.println(intList);
                }


                binding.choices.setText(wordList.get(questionNumber).meaning);
                int trueAnswer=random.nextInt(4);
                if (trueAnswer==0){
                    binding.buttonA.setText(wordList.get(questionNumber).word);
                }
                else if (trueAnswer==1){
                    binding.buttonB.setText(wordList.get(questionNumber).word);
                }
                else if (trueAnswer==2){
                    binding.buttonC.setText(wordList.get(questionNumber).word);
                }
                else if (trueAnswer==3){
                    binding.buttonD.setText(wordList.get(questionNumber).word);
                }

                for (int i=0;i<4;i++){
                    if (i!=trueAnswer&&i==0){
                        binding.buttonA.setText(wordList.get(intList.get(i)).word);
                    }
                    else if (i!=trueAnswer&&i==1){
                        binding.buttonB.setText(wordList.get(intList.get(i)).word);
                    }
                    else if (i!=trueAnswer&&i==2){
                        binding.buttonC.setText(wordList.get(intList.get(i)).word);
                    }
                    else if (i!=trueAnswer&&i==3)
                        binding.buttonD.setText(wordList.get(intList.get(i)).word);
                }

                binding.buttonA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.buttonA.setEnabled(false);
                        binding.buttonB.setEnabled(false);
                        binding.buttonC.setEnabled(false);
                        binding.buttonD.setEnabled(false);
                        if (binding.buttonA.getText().toString().matches(wordList.get(questionNumber).word)){
                            binding.buttonA.setBackgroundColor(Color.parseColor("#00fe08"));
                            score++;
                            binding.textscore.setText(String.valueOf(score));
                            correct();
                        }
                        else {
                            binding.buttonA.setBackgroundColor(Color.parseColor("#cc0000"));
                            if (trueAnswer == 1) {
                                binding.buttonB.setBackgroundColor(Color.parseColor("#00fe08"));
                                wrong();
                            } else if (trueAnswer == 2) {
                                binding.buttonC.setBackgroundColor(Color.parseColor("#00fe08"));
                                wrong();
                            } else if (trueAnswer == 3) {
                                binding.buttonD.setBackgroundColor(Color.parseColor("#00fe08"));
                                wrong();
                            }
                        }
                        Handler handler= new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTTS.speak(wordList.get(questionNumber).word,TextToSpeech.QUEUE_FLUSH,null);
                            }
                        },700);
                    }
                });

                binding.buttonB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.buttonA.setEnabled(false);
                        binding.buttonB.setEnabled(false);
                        binding.buttonC.setEnabled(false);
                        binding.buttonD.setEnabled(false);
                        if (binding.buttonB.getText().toString().matches(wordList.get(questionNumber).word)){
                            binding.buttonB.setBackgroundColor(Color.parseColor("#00fe08"));
                            score++;
                            binding.textscore.setText(String.valueOf(score));
                            correct();
                        }
                        else {

                            binding.buttonB.setBackgroundColor(Color.parseColor("#cc0000"));
                            if (trueAnswer == 0) {
                                binding.buttonA.setBackgroundColor(Color.parseColor("#00fe08"));
                                wrong();
                            } else if (trueAnswer == 2) {
                                binding.buttonC.setBackgroundColor(Color.parseColor("#00fe08"));
                                wrong();
                            } else if (trueAnswer == 3) {
                                binding.buttonD.setBackgroundColor(Color.parseColor("#00fe08"));
                                wrong();
                            }
                        }
                        Handler handler= new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTTS.speak(wordList.get(questionNumber).word,TextToSpeech.QUEUE_FLUSH,null);
                            }
                        },700);
                    }
                });

                binding.buttonC.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.buttonA.setEnabled(false);
                        binding.buttonB.setEnabled(false);
                        binding.buttonC.setEnabled(false);
                        binding.buttonD.setEnabled(false);
                        binding.buttonA.setTextColor(Color.parseColor("#050505"));
                        binding.buttonB.setTextColor(Color.parseColor("#050505"));
                        binding.buttonC.setTextColor(Color.parseColor("#050505"));
                        binding.buttonD.setTextColor(Color.parseColor("#050505"));

                        if (binding.buttonC.getText().toString().matches(wordList.get(questionNumber).word)){
                            binding.buttonC.setBackgroundColor(Color.parseColor("#00fe08"));
                            score++;
                            binding.textscore.setText(String.valueOf(score));
                            correct();
                        }
                        else{
                            binding.buttonC.setBackgroundColor(Color.parseColor("#cc0000"));
                            if (trueAnswer==0){
                                binding.buttonA.setBackgroundColor(Color.parseColor("#00fe08"));
                                wrong();
                            } else if (trueAnswer==1) {
                                binding.buttonB.setBackgroundColor(Color.parseColor("#00fe08"));
                                wrong();
                            } else if (trueAnswer==3) {
                                binding.buttonD.setBackgroundColor(Color.parseColor("#00fe08"));
                                wrong();
                            }
                        }
                        Handler handler= new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTTS.speak(wordList.get(questionNumber).word,TextToSpeech.QUEUE_FLUSH,null);
                            }
                        },700);
                    }
                });

                binding.buttonD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        binding.buttonA.setEnabled(false);
                        binding.buttonB.setEnabled(false);
                        binding.buttonC.setEnabled(false);
                        binding.buttonD.setEnabled(false);
                        binding.buttonA.setTextColor(Color.parseColor("#050505"));
                        binding.buttonB.setTextColor(Color.parseColor("#050505"));
                        binding.buttonC.setTextColor(Color.parseColor("#050505"));
                        binding.buttonD.setTextColor(Color.parseColor("#050505"));

                        if (binding.buttonD.getText().toString().matches(wordList.get(questionNumber).word)){
                            binding.buttonD.setBackgroundColor(Color.parseColor("#00fe08"));
                            score++;
                            binding.textscore.setText(String.valueOf(score));
                            correct();
                        }
                        else{
                            wrong();
                            binding.buttonD.setBackgroundColor(Color.parseColor("#cc0000"));
                            if (trueAnswer==0){
                                binding.buttonA.setBackgroundColor(Color.parseColor("#00fe08"));
                            }
                            else if (trueAnswer==1) {
                                binding.buttonB.setBackgroundColor(Color.parseColor("#00fe08"));
                            }
                            else if (trueAnswer==2) {
                                binding.buttonC.setBackgroundColor(Color.parseColor("#00fe08"));
                            }
                        }
                        Handler handler= new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mTTS.speak(wordList.get(questionNumber).word,TextToSpeech.QUEUE_FLUSH,null);
                            }
                        },700);
                    }
                });



            }
        });


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
        startActivity(intent);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}