package com.anecdote.white.question.bean;

import org.json.JSONObject;

/**
 * Created by 44260 on 2015/11/24.
 */
public class TextProfile extends Profile {

    public TextProfile() {
        showType = ShowType.TYPE_TEXT;
    }

    @Override
    public void parse(JSONObject jo) {
        super.parse(jo);
    }
}
