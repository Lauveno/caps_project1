package com.example.caps_project1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BDAdapter extends RecyclerView.Adapter<BDAdapter.ViewHolder> {
    private ArrayList<Board> mDataset;
    private Context mContext;

    @NonNull
    @Override
    public BDAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.boarditem, parent, false);
        return new BDAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.board_title.setText(String.valueOf(mDataset.get(position).getTitle()));
        holder.board_content.setText(String.valueOf(mDataset.get(position).getContent()));
        holder.board_username.setText(String.valueOf(mDataset.get(position).getUsername()));
    }

    public BDAdapter(ArrayList<Board> mDataset){
        this.mDataset = mDataset;
    }
    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return mDataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView board_title;
        private TextView board_content;
        private TextView board_username;

        public ViewHolder(View itemView) {
            super(itemView);
            board_title = itemView.findViewById(R.id.board_title);
            board_content = itemView.findViewById(R.id.board_content);
            board_username = itemView.findViewById(R.id.board_user);
        }

        void onBind(Board board) {
            board_title.setText(board.getTitle());
            board_content.setText(board.getContent());
            board_username.setText(board.getUsername());
        }
    }
}

