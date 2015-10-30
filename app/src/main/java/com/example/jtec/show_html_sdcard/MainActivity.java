package com.example.jtec.show_html_sdcard;

import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // do your handling codes here, which url is the requested url
                // probably you need to open that url rather than redirect:
                view.loadUrl(url);
                return true; // then it is not handled by default action
            }
        });
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }
    //back for webview
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //load trang menu day tien
    public void btn_load(View view){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.d("Error", "No SDCARD");
        } else {
            EditText editText = (EditText)findViewById(R.id.editText);
            //default file:///sdcard/a1/menu.html
            String value = editText.getText().toString();
            //nut_download_menu("http://192.168.0.15/a/sach_data/menu.html");
            nut_download_menu("http://developer.j-tec.com.vn/projects/android/sach_data/menu.html");
            webView.loadUrl(value);
        }
    }



    public void nut_download_menu(String str_link) {
        String filedow = str_link;
        String file = null;
        try {
            file = downloadFile(filedow,"menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
        TextView textView=(TextView)findViewById(R.id.textView);
        textView.setText(file);
    }


    private String saveDir = "/sdcard/";
    private String directory_parrent = "a1";
    private String directory = "";
    private String type_file = "";
    private String fileName = "";
    String saveFilePath = "";
    public String downloadFile(String dwnload_file_path, String type1) throws IOException {
        //check thu muc ext hay khong
        File f1 = new File(saveDir + directory_parrent);
        if (!f1.exists()) {
            f1.mkdir();
        }

        //get name
        //http://192.168.0.15/a/sach/bai_1/bai1.html
        String[] parts = dwnload_file_path.split("/");
        String part0 = parts[parts.length-1]; // bai_1.html
        String part1 = parts[parts.length-2]; // bai_1
        String part2 = parts[parts.length-3]; // sach
        //Toast.makeText(MainActivity.this, part0, Toast.LENGTH_SHORT).show();

        address_part=part1;

        String type=part1;
        if (part0.contains("menu.html")){
            type="menu";
            Toast.makeText(getBaseContext(), "La menu", Toast.LENGTH_SHORT).show();
        }

        if (type!="menu"){
            //check folder chuong
            directory = directory_parrent+"/"+part1;
            if (type=="img"){ //neu la thu muc img cua bai thi them folder bai_1 truoc sau do moi them thu muc . img
                directory = directory_parrent+"/"+part2;
            }
            File f = new File(saveDir + directory);
            if (!f.exists()) {
                f.mkdir();
            }
        }
        else{
            directory=directory_parrent;
        }
        //gan file
        fileName = dwnload_file_path.substring(dwnload_file_path.lastIndexOf("/") + 1, dwnload_file_path.length());
        File saveFilePath1 = new File(saveDir + directory + File.separator + fileName);

        saveFilePath = saveDir + directory + File.separator + fileName;
        //check folder la img hay khong
        if (type.contains("img")){
            type_file="/"+part1+"/";
            //"http://192.168.0.15/a/sach/bai_1/img/1.jpg";
            directory = directory_parrent+"/"+part2;
            File f_img = new File(saveDir + directory + type_file);
            if (!f_img.exists()) {
                f_img.mkdir();
            }
            saveFilePath1 = new File(saveDir + directory + type_file + fileName);
            saveFilePath = saveDir + directory +type_file+ fileName;
        }
        if (type.contains("hinhanh")){
            type_file="/hinhanh/";
            File f_img = new File(saveDir + directory_parrent + type_file);
            if (!f_img.exists()) {
                f_img.mkdir();
            }
            saveFilePath1 = new File(saveDir + directory_parrent + type_file + fileName);
            saveFilePath = saveDir + directory_parrent +type_file+ fileName;
        }
        if (type.contains("menu")){
            type_file="/";

            File f_img = new File(saveDir + directory_parrent + type_file);
            if (!f_img.exists()) {
                f_img.mkdir();
            }
            saveFilePath1 = new File(saveDir + directory_parrent + type_file + fileName);
            saveFilePath = saveDir + directory_parrent +type_file+ fileName;
        }
        if (type.contains("css")){
            type_file="/css/";
            File f_img = new File(saveDir + directory_parrent + type_file);
            if (!f_img.exists()) {
                f_img.mkdir();
            }
            saveFilePath1 = new File(saveDir + directory_parrent + type_file + fileName);
            saveFilePath = saveDir + directory_parrent +type_file+ fileName;
        }
        if (type.contains("js")){
            type_file="/js/";
            File f_img = new File(saveDir  + directory_parrent + type_file);
            if (!f_img.exists()) {
                f_img.mkdir();
            }
            saveFilePath1 = new File(saveDir   + directory_parrent +type_file + fileName);
            saveFilePath = saveDir   + directory_parrent + type_file+ fileName;
        }

        if (saveFilePath1.exists()) {
            saveFilePath = saveFilePath1.toString();
            //Toast.makeText(getBaseContext(), "File Ext", Toast.LENGTH_SHORT).show();
            return saveFilePath1.toString();
        } else {

            try {
                int BUFFER_SIZE = 4096;
                URL url = new URL(dwnload_file_path);
                HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                int responseCode = httpConn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {

                    String disposition = httpConn.getHeaderField("Content-Disposition");
                    if (disposition != null) {
                        // extracts file name from header field
                        int index = disposition.indexOf("filename=");
                        if (index > 0) {
                            fileName = disposition.substring(index + 10, disposition.length() - 1);
                        }
                    } else {
                        // extracts file name from URL
                        fileName = dwnload_file_path.substring(dwnload_file_path.lastIndexOf("/") + 1, dwnload_file_path.length());
                    }
                    // opens input stream from the HTTP connection
                    InputStream inputStream = httpConn.getInputStream();


                    // opens an output stream to save into file
                    FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                    int bytesRead = -1;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    outputStream.close();
                    inputStream.close();
                    //Toast.makeText(getBaseContext(), "File downloaded", Toast.LENGTH_SHORT).show();
                    //textView.setText("File xml downloaded");
                } else {
                    Toast.makeText(getBaseContext(), "No file to download. Server replied HTTP code" + responseCode, Toast.LENGTH_SHORT).show();
                    // textView.setText("No file .xml downloaded");
                }
                httpConn.disconnect();

            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Error:" + e, Toast.LENGTH_SHORT).show();
            }
            return saveFilePath;
        }

    }

    public void nut_delete_allfile(View v){
        File dir = new File(Environment.getExternalStorageDirectory()+"/"+directory_parrent);
        Toast.makeText(getApplicationContext(), "bat dau xoa "+dir, Toast.LENGTH_SHORT).show();
        DeleteRecursive(dir);
        Toast.makeText(getApplicationContext(), "Da xoa het ", Toast.LENGTH_SHORT).show();
    }
    void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);
        fileOrDirectory.delete();
    }

    //load file bai1.html
    public void and_load(String url_load){
        reload=true;
        String file = null;
        try {
                file = downloadFile(url_load,"html");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //nut_download_menu("http://192.168.0.15/a/sach_data/menu.html");
        webView.loadUrl("file://"+file);

    }
    String address_local="file:///sdcard/a1/";
    String address_part="";

    //load file hinhanh cho menu
    boolean reload =true;
    public void and_onload_first(String data_first[]){
        Toast.makeText(getApplicationContext(), "and_onload_first ", Toast.LENGTH_SHORT).show();
        String filedow = data_first[1];
        String file = null;
        //boolean reload =true;
        try {
            for (int i=1; i<data_first.length;i++){
                file = downloadFile(data_first[i],"hinhanh");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(),String.valueOf(reload) , Toast.LENGTH_SHORT).show();
        if (reload){
            webView.loadUrl("javascript:reload()");
        }
        //boolean reload =true;
    }
    public void and_reload_ok(){
        reload =false;
    }
}

class WebAppInterface {
    private final MainActivity activity;

    /**
     * Creates the API.
     *
     * @param activity
     *            the activity used by this API.
     */
    public WebAppInterface(final MainActivity activity) {
        this.activity = activity;
    }


    public static String str_link = "null";
    public String linksmp3[]=null; //all mp3 from javascript


    public void load(String url_load){
        this.activity.and_load(url_load);
    }
    public void onload_first(String data_first[]){
        this.activity.and_onload_first(data_first);
    }
    public void reload_ok(){
        this.activity.and_reload_ok();
    }

}
