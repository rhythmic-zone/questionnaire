package com.anecdote.white.question.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.anecdote.white.question.R;
import com.anecdote.white.question.bean.EventProfile;
import com.anecdote.white.question.bean.MultiProfile;
import com.anecdote.white.question.bean.Profile;
import com.anecdote.white.question.dev.Dev;
import com.anecdote.white.question.widget.AdaptiveRadioGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MultiBoxViewHolder extends AbsProfileViewHolder<MultiProfile> {

    private TextView tvTitle;
    private AdaptiveRadioGroup mAdaptiveRadioGroup;


    public MultiBoxViewHolder(View itemView) {
        super(itemView);
        tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        mAdaptiveRadioGroup = (AdaptiveRadioGroup) itemView.findViewById(R.id.rg_content);
    }

    @Override
    public void fillView(final MultiProfile item) {
        if (item == null)
            return;
        tvTitle.setText(item.getTitle());
        tvTitle.append("extra=>" + item.getExtra() + ",");
        if (item.getRuleList() != null && item.getRuleList().size() > 0) {
            tvTitle.append("isShown:" + item.isShown(obtainProfileListener) + ",rule:" + Arrays.toString(item.getRuleList().toArray()));
        }
        List<Integer> checkPos = new ArrayList<>();
        List<AdaptiveRadioGroup.Item> list = new ArrayList<>();
        for (int i = 0; i < item.getChoiceItemDescription().length; i++) {
            AdaptiveRadioGroup.Item mItem = mAdaptiveRadioGroup.new Item();
            mItem.setDescription(item.getChoiceItemDescription()[i]);
            mItem.setValue(item.getChoiceItem()[i]);
            mItem.setDisplayContent(item.getChoiceItem()[i] + (item.getChoiceItemDescription()[i] == null ? "" : "、" + item.getChoiceItemDescription()[i]));
            if (item.getProfileValue() != null && item.getChoiceItem()[i] != null && item.getProfileValues().contains(item.getChoiceItem()[i]) && !item.getChoiceItem()[i].equals(Profile.TYPE_EDIT)) {
                checkPos.add(i);
            }
            if (mItem.getValue().equals(mItem.getDescription())) {
                mItem.setDisplayContent(mItem.getDescription());
            }
            if (item.getChoiceItem()[i].equals(Profile.TYPE_EDIT)) {
                mItem.setEditText(item.getExtra());
                mItem.setIsEdit(true);
            } else mItem.setIsEdit(false);
            list.add(mItem);
        }
        mAdaptiveRadioGroup.setRadioMode(false);
        mAdaptiveRadioGroup.setContent(list, checkPos);
        mAdaptiveRadioGroup.setOnItemClickListener(new AdaptiveRadioGroup.OnItemClickListener() {
            @Override
            public void onItemClick() {
                EventBus.getDefault().post(new EventProfile());
            }
        });
        mAdaptiveRadioGroup.setOnCheckedObtainAllValueListener(new AdaptiveRadioGroup.OnCheckedObtainAllValueListener() {
            @Override
            public void onObtainAllValue(List<AdaptiveRadioGroup.Item> items) {
                if (items == null)
                    return;
                StringBuilder buffer = new StringBuilder();
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).isEdit()) {
                        item.setExtra(items.get(i).getEditText());
                    } else {
                        buffer.append(items.get(i).getValue());
                        if (i != items.size() - 1)
                            buffer.append(",");
                    }
                }
                if (!TextUtils.isEmpty(item.getExtra())) {
                    buffer.append(item.getExtra());
                }
                item.setProfileValue(buffer.toString());
                item.parseProfileValues();
            }
        });
    }
}