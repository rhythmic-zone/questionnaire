package com.anecdote.white.question.viewholder;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.anecdote.white.question.R;
import com.anecdote.white.question.bean.ProfileRule;
import com.anecdote.white.question.bean.TextProfile;

public class TextViewHolder extends AbsProfileViewHolder<TextProfile> {

    private TextView tvTitle;
    private TextView tvUnit;
    private EditText etInput;


    public TextViewHolder(View itemView) {
        super(itemView);
        etInput = (EditText) itemView.findViewById(R.id.et_input);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        tvUnit = (TextView) itemView.findViewById(R.id.tv_unit);
    }

    @Override
    public void fillView(TextProfile item) {
        if (item == null)
            return;
        tvTitle.setText(item.getTitle());
        tvUnit.setText(item.getUnit());
        etInput.setText(item.getProfileValue());
    }
}