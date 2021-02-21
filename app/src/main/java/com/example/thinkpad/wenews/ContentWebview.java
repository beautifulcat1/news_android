package com.example.thinkpad.wenews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static com.example.thinkpad.wenews.MainActivity.progressDialog;

public class ContentWebview extends AppCompatActivity {
      public static Elements element;
//    private TextView text_title,text_info,text_content;
//    String title=new String();
//    String author=new String();
//    String time=new String() ;
//    String imgAdress=new String();
//    ProgressDialog progressDialog ;
//    StringBuffer buffer=new StringBuffer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_webview);
        Intent intent=getIntent();
        final String address=intent.getStringExtra("address");

        WebView webView=(WebView) findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        //支持缩放

        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl(address);
          String summary1 = "<!DOCTYPE html>\n" +
                  "<html lang=\"zh-CN\">\n" +
                  "  <head>\n" +
                  "    <meta charset=\"utf-8\">\n" +
                  "  </head>\n" +
                  "  <body>";
        String summary2 = "  </body>\n" +
                "</html>";
//线程内更新变量但主要变量不会变出错
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Document document = Jsoup.connect(address).get();
                    element = document.getElementsByClass("article-content");

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }//原文出自【易百教程】，商业转载请联系作者获得授权，非商业请保留原文链接：https://www.yiibai.com/jsoup/jsoup-quick-start.html

            }
        }){}.start();
        try {
            Thread.sleep(1000);//主线程等一下 更新线程将数据更新完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        webView.loadData(summary1+element.toString()+summary2, "text/html", "utf-8");
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null)
            actionBar.hide();
        ImageButton button=(ImageButton)findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
