package com.anecdote.white.question.viewholder;

import android.view.View;
import android.widget.TextView;

import com.anecdote.white.question.R;
import com.anecdote.white.question.bean.CheckBoxProfile;
import com.anecdote.white.question.bean.Profile;
import com.anecdote.white.question.widget.AdaptiveRadioGroup;

import java.util.ArrayList;
import java.util.List;

public class CheckBoxViewHolder extends AbsProfileViewHolder<CheckBoxProfile> {

    private TextView tvTitle;
    private AdaptiveRadioGroup mAdaptiveRadioGroup;


    public CheckBoxViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        mAdaptiveRadioGroup = (AdaptiveRadioGroup) itemView.findViewById(R.id.rg_content);
    }

    @Override
    public void fillView(final CheckBoxProfile item) {
        if (item == null)
            return;
        tvTitle.setText(item.getTitle());
        int checkPos = -1;
        List<AdaptiveRadioGroup.Item> list = new ArrayList<>();
        for (int i = 0; i < item.getChoiceItemDescription().length; i++) {
            AdaptiveRadioGroup.Item mItem = mAdaptiveRadioGroup.new Item();
            mItem.setDescription(item.getChoiceItemDescription()[i]);
            mItem.setValue(item.getChoiceItem()[i]);
            mItem.setDisplayContent(item.getChoiceItemDescription()[i]);
            if (item.getProfileValue() != null && item.getChoiceItem()[i] != null && item.getChoiceItem()[i].equals(item.getProfileValue())) {
                checkPos = i;
            }
            if (mItem.getValue().equals(mItem.getDescription())) {
                mItem.setDisplayContent(mItem.getDescription());
            }
            if (item.getChoiceItem()[i].equals(Profile.TYPE_EDIT))
                mItem.setIsEdit(true);
            else mItem.setIsEdit(false);
            list.add(mItem);
        }
        mAdaptiveRadioGroup.setRadioMode(false);
        mAdaptiveRadioGroup.setContent(list, checkPos);
        mAdaptiveRadioGroup.setOnCheckedObtainAllValueListener(new AdaptiveRadioGroup.onCheckedObtainAllValueListener() {
            @Override
            public void onObtainAllValue(List<AdaptiveRadioGroup.Item> items) {
                StringBuilder buffer = new StringBuilder();
                if (items == null)
                    return;
                for (int i = 0; i < items.size(); i++) {
                    buffer.append(items.get(i).getValue());
                    if (i != items.size() - 1)
                        buffer.append(",");
                }
                item.setProfileValue(buffer.toString());
            }
        });
    }
}