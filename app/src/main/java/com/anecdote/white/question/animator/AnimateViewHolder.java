package com.anecdote.white.question.animator;

/**
 * Created by 44260 on 2015/11/26.
 */

import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class AnimateViewHolder extends RecyclerView.ViewHolder {

    public AnimateViewHolder(View itemView) {
        super(itemView);
    }

    public void preAnimateAddImpl() {
    }

    public void preAnimateRemoveImpl() {
    }

    public abstract void animateAddImpl(ViewPropertyAnimatorListener listener);

    public abstract void animateRemoveImpl(ViewPropertyAnimatorListener listener);
}