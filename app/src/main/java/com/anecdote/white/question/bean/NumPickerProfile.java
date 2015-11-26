package com.anecdote.white.question.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 44260 on 2015/11/24.
 */
public class NumPickerProfile extends Profile {
    private float numMin;
    private float numMax;
    private float step;

    public NumPickerProfile(){showType = ShowType.TYPE_NUMBER_PICKER;}
    @Override
    public void parse(JSONObject jo) {
        super.parse(jo);
        String validatorValue;
        try {
            validatorValue = jo.getString("validator");
            String split = validatorValue.replace("num:", "");
            String[] arr = split.split(",");
            if (arr.length > 1) {
                setStep(Float.valueOf(arr[1]));
                String[] arr1 = arr[0].split("-");
                if (arr1.length > 1) {
                    setNumMin(Float.valueOf(arr1[0]));
                    setNumMax(Float.valueOf(arr1[1]));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public double getNumMin() {
        return numMin;
    }

    public NumPickerProfile setNumMin(float numMin) {
        this.numMin = numMin;
        return this;
    }

    public double getNumMax() {
        return numMax;
    }

    public NumPickerProfile setNumMax(float numMax) {
        this.numMax = numMax;
        return this;
    }

    public double getStep() {
        return step;
    }

    public NumPickerProfile setStep(float step) {
        this.step = step;
        return this;
    }
}
