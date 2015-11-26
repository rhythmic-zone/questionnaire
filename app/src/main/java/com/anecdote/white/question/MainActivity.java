package com.anecdote.white.question;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anecdote.white.question.adapter.ProfileAdapter;
import com.anecdote.white.question.bean.Profile;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "QAHealthy";
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private List<Profile> profileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qa_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        adapter = new ProfileAdapter(getBaseContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(adapter);
        refresh();
    }

    public void onSkip(View view) {
//        startActivity(new Intent(getBaseContext(), AnecdoteActivity.class));
        refresh();
    }

    private void refresh() {
        Volley.newRequestQueue(getBaseContext()).add(new JsonArrayRequest("http://192.168.137.1/question", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                profileList = Profile.createQAHealthList(response);
                adapter.setContent(profileList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error == null ? "no error message" : "" + error.getMessage());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                Log.d(TAG, volleyError == null ? "no error message" : "" + volleyError.getMessage());
                tryChangeDomain();
                Snackbar.make(recyclerView, "从本地服务器获取失败，尝试从云服务器获取", Snackbar.LENGTH_SHORT).show();
                return super.parseNetworkError(volleyError);

            }
        });
    }

    private void tryChangeDomain() {
        Volley.newRequestQueue(getBaseContext()).add(new JsonArrayRequest("http://archive.sinaapp.com/question/", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Snackbar.make(recyclerView, "从云服务器获取成功", Snackbar.LENGTH_SHORT).show();
                profileList = Profile.createQAHealthList(response);
                adapter.setContent(profileList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error == null ? "no error message" : "" + error.getMessage());
            }
        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                Log.d(TAG, volleyError == null ? "no error message" : "" + volleyError.getMessage());
                return super.parseNetworkError(volleyError);

            }
        });
    }

    private void reg() {
        Volley.newRequestQueue(getBaseContext()).add(new StringRequest("http://192.168.137.1/question/reg.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Profile profile = new Profile();
                if (response.contains(",")) {
                    String[] arr = response.split(",");
                    if (arr.length > 1) {
                        profile.setProfileValue(arr[0]);
                        Log.d(TAG, "" + profile.valueMatch(arr[1]));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error == null ? "no error message" : "" + error.getMessage());
            }
        }
        ));
    }

    public void obtain(View view) {
        StringBuilder buffer = new StringBuilder();
        if (profileList == null || profileList.size() == 0)
            buffer.append("no data");
        else {
            for (int i = 0; i < profileList.size(); i++) {
                Profile profile = profileList.get(i);
                buffer.append("").append(profile.getProfileKey()).append("--").append(profile.getProfileValue());
            }
        }
        Snackbar.make(view, buffer.toString(), Snackbar.LENGTH_INDEFINITE).setAction("dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        }).show();

    }
}