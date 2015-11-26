package com.anecdote.white.question.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by 44260 on 2015/11/24.
 * profile bean
 */
public class Profile implements NetParser {
    private String profileKey;
    private String name;
    private String title;
    private String profileValue;
    private String unit;
    protected int showType;
    private String originRule;
    private List<ProfileRule> ruleList;
    public static final String TYPE_EDIT = "___";

    public int getShowType() {
        return showType;
    }

    public boolean isShown(@NonNull ProfileRule.ObtainProfileListener listener) {
        if (ruleList == null || ruleList.size() == 0)
            return true;
        boolean ret = true;
        for (int i = 0; i < ruleList.size(); i++) {
            ret &= ruleList.get(i).matched(listener);
        }
        return ret;
    }

    public String getUnit() {
        return unit;
    }

    public Profile setUnit(String unit) {
        this.unit = unit;
        return this;
    }

    public String getProfileValue() {
        return profileValue;
    }

    public Profile setProfileValue(String profileValue) {
        this.profileValue = profileValue;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Profile setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getName() {
        return name;
    }

    public Profile setName(String name) {
        this.name = name;
        return this;
    }

    public String getProfileKey() {
        return profileKey;
    }

    public Profile setProfileKey(String profileKey) {
        this.profileKey = profileKey;
        return this;
    }

    public void setOriginRule(String originRule) {
        this.originRule = originRule;
        parseRuleList();
    }

    private static Profile createByValidator(String validator) {
        Profile profile;
        if (validator.matches("num")) {
            profile = new NumProfile();
        } else if (validator.matches("num:(.*)")) {
            profile = new NumPickerProfile();
        } else if (validator.matches("date")) {
            profile = new DateProfile();
        } else if (validator.matches("checkbox")) {
            profile = new CheckBoxProfile();
        } else if (validator.matches("enum:(.*)")) {
            profile = new EnumProfile();
        } else if (validator.matches("mubox:(.*)")) {
            profile = new MultiProfile();
        } else if (validator.matches("text")) {
            profile = new TextProfile();
        } else {
            //default type
            profile = new TextProfile();
        }
        return profile;
    }

    public static Profile createProfile(JSONObject jsonObject) {
        if (jsonObject == null)
            return null;

        if (jsonObject.has("validator") && !jsonObject.isNull("validator")) {
            try {
                String validatorValue = jsonObject.getString("validator");
                Profile profile = createByValidator(validatorValue);
                profile.parse(jsonObject);
                return profile;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static List<Profile> createProfile(JSONArray ja) {

        List<Profile> profileList = null;
        try {
            profileList = new ArrayList<>();
            for (int i = 0; i < ja.length(); i++) {
                Profile profile = createProfile((JSONObject) ja.get(i));
                if (profile != null) {
                    profileList.add(profile);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return profileList;
    }

    @Override
    public void parse(JSONObject jo) {
        try {
            if (jo.has("profileid") && !jo.isNull("profileid")) {
                setProfileKey(jo.getString("profileid"));
            }
            if (jo.has("name") && !jo.isNull("name")) {
                setName(jo.getString("name"));
            }
            if (jo.has("title") && !jo.isNull("title")) {
                setTitle(jo.getString("title"));
            }
            if (jo.has("unit") && !jo.isNull("unit")) {
                setUnit(jo.getString("unit"));
            }
            if (jo.has("value") && !jo.isNull("value")) {
                setProfileValue(jo.getString("value"));
            }
            if (jo.has("showon") && !jo.isNull("showon")) {
                setOriginRule(jo.getString("showon"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "title:" + getTitle() + "\nname:" + getName() + "\nkey:" + getProfileKey() + "\nvalue:" + getProfileValue() + "\nunit:" + getUnit() + "\nrule:" + originRule + "\n===================";
    }

    public boolean valueMatch(@NonNull String expect) {
        String actual = getProfileValue();
        if (TextUtils.isEmpty(getProfileValue()))
            return false;
        if (strCmp(expect, "~/", 2)) {
            return TextUtils.equals(expect.substring(2), actual);
        } else if (strCmp(expect, "<>", 2)) {
            return !TextUtils.equals(actual, expect.substring(2));
        } else if (strCmp(expect, ">=", 2)) {
            String expectVal = expect.substring(2);
            if (isIllegal(actual) && isIllegal(expectVal)) {
                return Float.valueOf(actual) >= Float.valueOf(expectVal);
            }
        } else if (strCmp(expect, "<=", 2)) {
            String expectVal = expect.substring(2);
            if (isIllegal(actual) && isIllegal(expectVal)) {
                return Float.valueOf(actual) <= Float.valueOf(expectVal);
            }
        } else if (strCmp(expect, ">", 1)) {
            String expectVal = expect.substring(1);
            if (isIllegal(actual) && isIllegal(expectVal)) {
                return Float.valueOf(actual) > Float.valueOf(expectVal);
            }
        } else if (strCmp(expect, "<", 1)) {
            String expectVal = expect.substring(1);
            if (isIllegal(actual) && isIllegal(expectVal)) {
                return Float.valueOf(actual) < Float.valueOf(expectVal);
            }
        } else if (strCmp(expect, "=", 1)) {
            String expectVal = expect.substring(1);
            return TextUtils.equals(expectVal, actual);
        }
        return TextUtils.equals(expect, actual);
    }


    private boolean isIllegal(@NonNull String dest) {
        return !TextUtils.isEmpty(dest) && Pattern.compile("^(-?\\d+)(\\.\\d+)?").matcher(dest).matches();
    }

    private boolean strCmp(@NonNull String origin, @NonNull String destReg, int digit) {
        return !(TextUtils.isEmpty(origin) || TextUtils.isEmpty(destReg) || !origin.contains(destReg) || digit < 0) && (TextUtils.equals(destReg, origin.substring(0, digit)));
    }


    public List<ProfileRule> getRuleList() {
        if (ruleList == null) {
            parseRuleList();
        }
        return ruleList;
    }

    private void parseRuleList() {
        if (originRule == null)
            return;
        try {
            JSONArray jsonArray = new JSONArray(originRule);
            ruleList = ProfileRule.createProfileRules(jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected String parseEncode(String desc) {
        return desc.replaceAll("，", ",");
    }

    protected String parseDecode(String desc) {
        return desc.replaceAll(",", "，");
    }
}
