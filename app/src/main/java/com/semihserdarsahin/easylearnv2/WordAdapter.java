package com.semihserdarsahin.easylearnv2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.semihserdarsahin.easylearnv2.databinding.RecyclerRowBinding;

import java.util.List;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordHolder> {
    List<Word> wordList;

    public WordAdapter(List<Word> wordList) {
        this.wordList = wordList;
    }

    @NonNull
    @Override
    public WordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);

        return new WordHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull WordHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.recyclerRowBinding.recyclerRowTextWord.setText(wordList.get(position).word);
        holder.recyclerRowBinding.recyclerRowTextMeaning.setText(wordList.get(position).meaning);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(holder.itemView.getContext(),DetailsActivity.class);
                intent.putExtra("word",wordList.get(position));
                holder.itemView.getContext().startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public class WordHolder extends RecyclerView.ViewHolder{
        RecyclerRowBinding recyclerRowBinding;
        public WordHolder(@NonNull RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding=recyclerRowBinding;
        }
    }
}
