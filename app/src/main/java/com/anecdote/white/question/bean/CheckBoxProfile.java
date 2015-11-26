package com.anecdote.white.question.bean;

import android.text.TextUtils;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by 44260 on 2015/11/24.
 */
public class CheckBoxProfile extends Profile {

    private String[] choiceItem;
    private String[] choiceItemDescription;


    public CheckBoxProfile() {
        showType = ShowType.TYPE_MULTI_BOX;
    }

    @Override
    public void parse(JSONObject jo) {
        super.parse(jo);
        String[] choiceItem = new String[]{"Y", "N"};
        String[] choiceItemDescription;
        if (TextUtils.equals(Locale.getDefault().toString(), Locale.CHINA.toString()) || TextUtils.equals(Locale.getDefault().toString(), Locale.CHINESE.toString())) {
            choiceItemDescription = new String[]{"是", "否"};
        } else {
            choiceItemDescription = new String[]{"Yes", "No"};
        }

        setChoiceItem(choiceItem);
        setChoiceItemDescription(choiceItemDescription);
    }

    public String[] getChoiceItem() {
        return choiceItem;
    }

    public CheckBoxProfile setChoiceItem(String[] choiceItem) {
        this.choiceItem = choiceItem;
        return this;
    }

    public String[] getChoiceItemDescription() {
        return choiceItemDescription;
    }

    public CheckBoxProfile setChoiceItemDescription(String[] choiceItemDescription) {
        this.choiceItemDescription = choiceItemDescription;
        return this;
    }

    @Override
    public String toString() {
        return Arrays.toString(choiceItemDescription) + "\n" + Arrays.toString(choiceItem) + "\n" + super.toString();
    }
}
