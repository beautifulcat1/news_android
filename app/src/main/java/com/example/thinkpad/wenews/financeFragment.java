package com.example.thinkpad.wenews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class financeFragment extends Fragment {
    private List<NewItem> newItems = new ArrayList<NewItem>();
    private RecyclerView recyclerView_finance;
    private NewsAdapter adapter;
    private static int flag1 = 0;

    public financeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_finance, container, false);
        recyclerView_finance = (RecyclerView) view.findViewById(R.id.recyclerview_finace);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView_finance.setLayoutManager(layoutManager);
        adapter = new NewsAdapter(newItems);
        recyclerView_finance.setAdapter(adapter);
        if (flag1 == 0) {
            GetNews();
            flag1 = 1;
        }
        Log.d("fina1", "onCreateView:1 ");
        return view;
    }

    public void GetNews() {
        if (!MainActivity.progressDialog.isShowing()) {
            MainActivity.progressDialog.show();
        }
        HttpUtil.sendOkhttpRequest("https://3g.163.com/touch/reconstruct/article/list/BA8EE5GMwangning/0-20.html", new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("error11", "获取错误！！！");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("成功！", "12121212");
                String text = response.body().string();
                Log.d("response1", text);
                char test[] = text.toCharArray();//由于api的json数据不是标准格式要去掉前边的artist
                for (int i = 0; i < 9; i++)
                    test[i] = ' ';
                test[test.length - 1] = ' ';
                text = String.valueOf(test);
                parseJSONWithJSONObject(text);
            }
        });
    }

    private void parseJSONWithJSONObject(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            final JSONArray array = jsonObject.getJSONArray("BA8EE5GMwangning");
            for (int i = 0; i < array.length(); i++) {
                NewItem one = new NewItem();
                JSONObject object = array.getJSONObject(i);
                one.setPictureAddress(object.getString("imgsrc"));
                Log.d("address", "parseJSONWithJSONObject: " + one.getPictureAddress());
                one.setTitle(object.getString("title"));
                Log.d("title", "parseJSONWithJSONObject: " + one.getTitle());
                one.setContentAddress(object.getString("url"));
                Log.d("finececontentadress", one.getContentAddress());
                one.setSource(object.getString("source"));
                Log.d("source", "parseJSONWithJSONObject: "+one.getSource());

                boolean check = false;
                for (NewItem c : newItems) {
                    Log.d("for_test", "parseJSONWithJSONObject: " + c.getTitle());
                    if (c.getTitle().equals(one.getTitle())) {
                        check = true;
                        break;
                    }
                }
                if (!check)
                    newItems.add(one);
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (MainActivity.progressDialog.isShowing())
                        MainActivity.progressDialog.dismiss();
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
