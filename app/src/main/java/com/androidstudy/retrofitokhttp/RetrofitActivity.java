package com.androidstudy.retrofitokhttp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.androidstudy.R;
import com.androidstudy.retrofitokhttp.entity.GitResult;
import com.androidstudy.retrofitokhttp.entity.Item;
import com.androidstudy.retrofitokhttp.rest.GitApi;
import com.androidstudy.retrofitokhttp.rest.GitApiRestClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class RetrofitActivity extends Activity {

    private UserAdapter adapter ;
    List<Item> Users ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_retrofit);

        final ListView listView = (ListView) findViewById(R.id.listView);
        Users = new ArrayList<Item>();

        final ProgressDialog dialog = ProgressDialog.show(this, "", "loading...");
        GitApi service = GitApiRestClient.getClient();
        Call<GitResult> call = service.getUsersNamedTom("tom");
        call.enqueue(new Callback<GitResult>() {    //异步
            @Override
            public void onResponse(Response<GitResult> response, Retrofit retrofit) {
                dialog.dismiss();
                Log.d("MainActivity", "Status Code = " + response.code());
                if (response.isSuccess()) {
                    // request successful (status code 200, 201)
                    GitResult result = response.body();
                    Log.d("MainActivity", "response = " + new Gson().toJson(result));
                    Users = result.getItems();
                    Log.d("MainActivity", "Items = " + Users.size());
                    adapter = new UserAdapter(RetrofitActivity.this, Users);
                    listView.setAdapter(adapter);
                } else {
                    // response received but request not successful (like 400,401,403 etc)
                    //Handle errors
                }
            }
            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
            }
        });
    }

}
