package com.cyllide.app.beta.forum.questionlist.questionPage;

import android.view.View;
import android.widget.TextView;

import com.cyllide.app.beta.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class QuestionTagViewHolder extends RecyclerView.ViewHolder {

    TextView tagText;

    public QuestionTagViewHolder(@NonNull View itemView) {
        super(itemView);
        tagText = itemView.findViewById(R.id.tag_text);
    }

    public void populate(final QuestionTagModel questionTagModel){
        tagText.setText(questionTagModel.getTag());

    }

}
