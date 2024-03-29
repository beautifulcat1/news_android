package com.example.thinkpad.wenews;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class amusementFragment extends Fragment {
    private List<NewItem> newItems=new ArrayList<NewItem>();
    private  RecyclerView recyclerView_amusement;
    private  NewsAdapter adapter;
    private  static int flag3 = 0;
    public amusementFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.layout_amusement, container, false);
        recyclerView_amusement=(RecyclerView) view.findViewById(R.id.recyclerview_amusement) ;
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView_amusement.setLayoutManager(layoutManager);
        adapter=new NewsAdapter(newItems);
        recyclerView_amusement.setAdapter(adapter);
        if(flag3 == 0){
            GetNews();
            flag3 = 1;
        }

        Log.d("amus3", "onCreateView:3 ");

        return view;
    }



    public void GetNews(){
    if(!MainActivity.progressDialog.isShowing()){
        MainActivity.progressDialog.show();
    }
    HttpUtil.sendOkhttpRequest("https://3g.163.com/touch/reconstruct/article/list/BD2AC4LMwangning/0-20.html", new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("error11","获取错误！！！");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.d("成功！","12121212");
            String text=response.body().string();
            Log.d("response",text);
            char test[]=text.toCharArray();
            for(int i=0;i<9;i++)
                test[i]=' ';
            test[test.length-1]=' ';
            Log.d("text",String.valueOf(test));
            text=String.valueOf(test);
            parseJSONWithJSONObject(text);
        }
    });
}
    private  void  parseJSONWithJSONObject(String jsonData)
    {
        try{
            Log.d("hello","hello");
            JSONObject jsonObject=new JSONObject(jsonData);
            Log.d("testtest",jsonObject.toString());

            final JSONArray array=jsonObject.getJSONArray("BD2AC4LMwangning");
            for(int i=0;i<array.length();i++)
            {
                NewItem one=new NewItem();
                JSONObject object=array.getJSONObject(i);
                one.setPictureAddress(object.getString("imgsrc"));
                one.setTitle(object.getString("title"));
                one.setContentAddress(object.getString("url"));
                one.setSource(object.getString("source"));
                boolean check=false;
                for(NewItem c:newItems){
                    if(c.getTitle().equals(one.getTitle())){
                        check=true;
                    break;
                }}
                if(!check)
                newItems.add(one);
            }


           getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(MainActivity.progressDialog.isShowing())
                    MainActivity.progressDialog.dismiss();
                    adapter.notifyDataSetChanged();
                }
            });
        }catch (Exception e)
        {
            Log.d("listsize3","1234333"+e.toString());
            e.printStackTrace();

        }
    }
}
