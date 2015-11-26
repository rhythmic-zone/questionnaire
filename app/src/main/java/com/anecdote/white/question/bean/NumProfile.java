package com.anecdote.white.question.bean;

import org.json.JSONObject;

/**
 * Created by 44260 on 2015/11/24.
 */
public class NumProfile extends Profile {

    public NumProfile() {
        showType = ShowType.TYPE_NUM;
    }

    @Override
    public void parse(JSONObject jo) {
        super.parse(jo);
    }
}
