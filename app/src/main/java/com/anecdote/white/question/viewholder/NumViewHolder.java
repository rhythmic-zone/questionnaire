package com.anecdote.white.question.viewholder;

import android.view.View;
import android.widget.TextView;

import com.anecdote.white.question.R;
import com.anecdote.white.question.bean.NumProfile;
import com.anecdote.white.question.bean.ProfileRule;
import com.anecdote.white.question.bean.TextProfile;

public class NumViewHolder extends AbsProfileViewHolder<NumProfile> {

    private TextView tvTitle;
    private TextView tvInput;


    public NumViewHolder(View itemView) {
        super(itemView);
        tvInput = (TextView) itemView.findViewById(R.id.tv_input);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
    }

    @Override
    public void fillView(NumProfile item) {
        if (item == null)
            return;
        tvTitle.setText(item.getTitle());
        tvInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }
}