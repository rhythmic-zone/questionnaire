package com.anecdote.white.question.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.anecdote.white.question.bean.Profile;
import com.anecdote.white.question.bean.ProfileRule;


public abstract class AbsProfileViewHolder<T extends Profile> extends RecyclerView.ViewHolder {

    protected ProfileRule.ObtainProfileListener obtainProfileListener;
    public AbsProfileViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void fillView(T t);

    public void setObtainProfileListener(ProfileRule.ObtainProfileListener obtainProfileListener){
        this.obtainProfileListener = obtainProfileListener;
    }


}
