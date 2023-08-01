package com.semihserdarsahin.easylearnv2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Toast;

import com.semihserdarsahin.easylearnv2.databinding.ActivityAddingBinding;
import com.semihserdarsahin.easylearnv2.databinding.ActivityDetailsBinding;

import java.util.Locale;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DetailsActivity extends AppCompatActivity {
    private ActivityDetailsBinding binding;
    AppDataBase db;
    WordDao wordDao;
    Intent intent;
    TextToSpeech mTTS;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
    Word word;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mTTS=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    int result=mTTS.setLanguage(Locale.US);

                    if(result==TextToSpeech.LANG_MISSING_DATA||result==TextToSpeech.LANG_NOT_SUPPORTED){
                        Toast.makeText(DetailsActivity.this, "Error, Please Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(DetailsActivity.this, "Error, Please Check Your Internet Connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        db= Room.databaseBuilder(getApplicationContext(),AppDataBase.class,"word").build();
        wordDao=db.wordDao();

        intent=getIntent();
        word=(Word) intent.getSerializableExtra("word");

        binding.textView8.setText(word.word);
        binding.textView10.setText(word.sentence);
        binding.textView9.setText(word.meaning);
        binding.textView11.setText(word.turkish);

        binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text=binding.textView8.getText().toString(); //the word we will pronounciation
                System.out.println("Clicked");
                mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null);
            }
        });

    }
    public void delete(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(DetailsActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("Are You Sure To Delete?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Toast.makeText(DetailsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                compositeDisposable.add(wordDao.delete(word).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(DetailsActivity.this::handleResponse));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.show();
    }
    private void handleResponse(){
        Intent backIntent=new Intent(DetailsActivity.this,MainActivity.class);
        startActivity(backIntent);

    }
    public void returnback(View view){
        Intent intent1=new Intent(this,MainActivity.class);
        startActivity(intent1);
        finish();

    }
    public void listen(View view){
        String text=binding.textView8.getText().toString(); //the word we will pronounciation
        System.out.println("Clicked");
        mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null);

    }


    @Override
    protected void onDestroy() {
        if (mTTS!=null){
            mTTS.stop();
            mTTS.shutdown();
        }
        super.onDestroy();
        compositeDisposable.clear();
    }
}