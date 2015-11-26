package com.anecdote.white.question.bean;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 44260 on 2015/11/25.
 * rule list
 */
public class ProfileRule extends HashMap<String, String> {

    public ProfileRule(@NonNull JSONObject jsonObject) {
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                put(key, jsonObject.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean matched(@NonNull ObtainProfileListener listener) {
        Iterator<Entry<String, String>> iterator = entrySet().iterator();
        boolean ret = false;
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            Profile profile = listener.obtain(entry.getKey());
            if (profile != null) {
                ret |= profile.valueMatch(entry.getValue());
            }
        }
        return ret;
    }

    public static List<ProfileRule> createProfileRules(@NonNull JSONArray jsonArray) {
        List<ProfileRule> profileRuleList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                profileRuleList.add(new ProfileRule(jsonArray.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return profileRuleList;
    }

    public interface ObtainProfileListener {
        Profile obtain(String profileKey);
    }
}
