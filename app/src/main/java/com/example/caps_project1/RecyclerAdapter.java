package com.example.caps_project1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<Data> mDataset;
    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.textView_title.setText(String.valueOf(mDataset.get(position).getTitle()));
        holder.textView_content.setText(String.valueOf("refer to this link : "+ mDataset.get(position).getContent()));
    }

    public RecyclerAdapter(ArrayList<Data> mDataset){
        this.mDataset = mDataset;
    }
    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return mDataset.size();
    }

//    void addItem(Data data) {
//        // 외부에서 item을 추가시킬 함수입니다.
//        mDataset.add(data);
//    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_title;
        private TextView textView_content;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_content = itemView.findViewById(R.id.textView_content);
        }

        void onBind(Data data) {
            textView_title.setText(data.getTitle());
            textView_content.setText(data.getContent());
        }
    }
}