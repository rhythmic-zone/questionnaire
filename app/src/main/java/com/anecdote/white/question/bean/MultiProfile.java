package com.anecdote.white.question.bean;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 44260 on 2015/11/24.
 */
public class MultiProfile extends Profile {

    private String[] choiceItem;
    private String[] choiceItemDescription;
    private String extra;
    private List<String> profileValues;

    public MultiProfile() {
        showType = ShowType.TYPE_MULTI_BOX;
    }


    public List<String> getProfileValues() {
        if (profileValues == null || profileValues.size() == 0)
            parseProfileValues();
        return profileValues;
    }

    @Override
    public void parse(JSONObject jo) {
        super.parse(jo);
        String validatorValue;
        try {
            validatorValue = jo.getString("validator");
            final String split = validatorValue.replace("mubox:", "");
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

    public String getExtra() {
        if (extra == null)
            parseProfileValues();
        if (extra != null) parseDecode(extra);
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public void parseProfileValues() {
        List<String> checkValue = new ArrayList<>();
        if (getProfileValue() != null) {
            String[] arr = getProfileValue().split(",");
            Collections.addAll(checkValue, arr);
        }
        Iterator<String> iterator = checkValue.listIterator();
        while (iterator.hasNext()) {
            String val = iterator.next();
            if (getChoiceItem() == null)
                break;
            if (!Arrays.asList(getChoiceItem()).contains(val)) {
                setExtra(val);
                iterator.remove();
            }
        }
        profileValues = checkValue;
    }
}
