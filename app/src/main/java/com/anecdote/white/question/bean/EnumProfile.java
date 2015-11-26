package com.anecdote.white.question.bean;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by 44260 on 2015/11/24.
 */
public class EnumProfile extends Profile {

    private String[] choiceItem;
    private String[] choiceItemDescription;


    public EnumProfile() {
        showType = ShowType.TYPE_ENUM;
    }

    @Override
    public void parse(JSONObject jo) {
        super.parse(jo);
        String validatorValue;
        try {
            validatorValue = jo.getString("validator");
            final String split = validatorValue.replace("enum:", "");
            final String regex = ",";
            String[] arr = split.split(regex);

            if (arr.length > 0) {
                final int choiceItemSize = arr.length;
                String[] choiceItem = new String[choiceItemSize];
                String[] choiceItemDescription = new String[choiceItemSize];
                for (int i = 0; i < choiceItemSize; i++) {
                    String[] descArr = arr[i].split("=");
                    choiceItem[i] = descArr[0];
                    if (descArr.length > 1)
                        choiceItemDescription[i] = parseEncode(descArr[1]);
                }
                setChoiceItem(choiceItem);
                setChoiceItemDescription(choiceItemDescription);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String parseEncode(String desc) {
        return desc.replaceAll("%2C", ",");
    }

    public String[] getChoiceItem() {
        return choiceItem;
    }

    public void setChoiceItem(String[] choiceItem) {
        this.choiceItem = choiceItem;
    }

    public String[] getChoiceItemDescription() {
        return choiceItemDescription;
    }

    public void setChoiceItemDescription(String[] choiceItemDescription) {
        this.choiceItemDescription = choiceItemDescription;
    }

    @Override
    public String toString() {
        return Arrays.toString(choiceItemDescription) + "\n" + Arrays.toString(choiceItem) + "\n" + super.toString();
    }
}
