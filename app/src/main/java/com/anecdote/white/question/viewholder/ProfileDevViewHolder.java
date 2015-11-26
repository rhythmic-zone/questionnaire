package com.anecdote.white.question.viewholder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anecdote.white.question.R;
import com.anecdote.white.question.bean.Profile;
import com.anecdote.white.question.bean.ProfileRule;

public class ProfileDevViewHolder extends AbsProfileViewHolder<Profile> {

    private TextView tvShow;
    private Button btn;


    public ProfileDevViewHolder(View itemView) {
        super(itemView);
        tvShow = (TextView) itemView.findViewById(R.id.textView);
        btn  = (Button) itemView.findViewById(R.id.button3);
    }

    @Override
    public void fillView(Profile profile) {

    }

    @Override
    public void fillView(final Profile item, final ProfileRule.ObtainProfileListener listener) {
        if (item == null)
            return;
        tvShow.setText(item.toString());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item.isShown(listener);
            }
        });
        if(listener!=null)
        tvShow.append("\nisShown:"+item.isShown(listener));
    }
}