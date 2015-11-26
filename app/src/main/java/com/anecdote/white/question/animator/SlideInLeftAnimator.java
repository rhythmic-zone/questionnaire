package com.anecdote.white.question.animator;

/**
 * Created by 44260 on 2015/11/26.
 */

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.animation.Interpolator;

public class SlideInLeftAnimator extends BaseItemAnimator {

    public SlideInLeftAnimator() {

    }

    public SlideInLeftAnimator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @Override
    protected void animateRemoveImpl(final RecyclerView.ViewHolder holder) {
        ViewCompat.animate(holder.itemView)
                .translationX(-holder.itemView.getRootView().getWidth())
                .setDuration(getRemoveDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultRemoveVpaListener(holder))
                .start();
    }

    @Override
    protected void preAnimateAddImpl(RecyclerView.ViewHolder holder) {
        ViewCompat.setTranslationX(holder.itemView, -holder.itemView.getRootView().getWidth());
    }

    @Override
    protected void animateAddImpl(final RecyclerView.ViewHolder holder) {
        ViewCompat.animate(holder.itemView)
                .translationX(0)
                .setDuration(getAddDuration())
                .setInterpolator(mInterpolator)
                .setListener(new DefaultAddVpaListener(holder))
                .start();
    }
}