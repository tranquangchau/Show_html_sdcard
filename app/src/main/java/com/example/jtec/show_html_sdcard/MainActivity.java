package com.example.jtec.show_html_sdcard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.PowerManager;
import android.os.StrictMode;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
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

    public MainActivity() {
        super();
    }


    //WebView webView;
    WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);



        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }



        webView = this.getUi();
        //webView = (WebView)findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
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
        btn_load();
    }
    /**
     * Returns the configured {@link WebView} to display the user interface.
     *
     * @return the view.
     */
    private WebView getUi() {
        return (WebView) this.findViewById(R.id.webView);
    }

    //back for webview
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            killMediaPlay();
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //load trang menu dau tien
//    public void btn_load(View view){
    public void btn_load(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Log.d("Error", "No SDCARD");
        } else {
            //nut_download_menu("http://192.168.0.15/a/sach_data/menu.html");
            nut_download_menu("http://developer.j-tec.com.vn/projects/android/sach_data/menu.html");
            //webView.loadUrl(value);
            //webView.loadUrl("file://"+saveFilePath);
            run_url("file://"+saveFilePath);
            //createFolder();
           // test_create_folder();
        }
    }
    public void test_create_folder(){
        File folder = new File(Environment.getExternalStorageDirectory() + "/map");
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        if (success) {
            //Toast.makeText(MainActivity.this,"Make OK"+folder,Toast.LENGTH_LONG).show();
            // Do something on success
        } else {
            // Do something else on failure
            //Toast.makeText(MainActivity.this,"False create"+folder,Toast.LENGTH_LONG).show();
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
        //TextView textView=(TextView)findViewById(R.id.textView);
        //textView.setText(file);
    }
    //bien neu la file cu thi khong autoload=false, neu chua co file thi true
    private boolean is_new_html=true;

    //String saveDir = Environment.getExternalStorageState() +File.separator+"sdcard/";
    //File saveDir = new File(Environment.getExternalStorageDirectory() + File.separator);
    String saveDir = (Environment.getExternalStorageDirectory().toString() + File.separator);
    //private String saveDir = (Environment.getDataDirectory()+File.separator);

    //private String saveDir = "/sdcard/";
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
            //Toast.makeText(MainActivity.this,"Make folder"+f1,Toast.LENGTH_LONG).show();
        }else{
            //Toast.makeText(MainActivity.this,"Exit folder"+f1,Toast.LENGTH_LONG).show();
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
            //Toast.makeText(getBaseContext(), "La menu", Toast.LENGTH_SHORT).show();
        }

        if (type!="menu"){
            //check folder chuong
            directory = directory_parrent+"/"+part1;
            if (type.contains("img")){ //neu la thu muc img cua bai thi them folder bai_1 truoc sau do moi them thu muc . img
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

        //check file la  ton tai.
        if (saveFilePath1.exists()) {
            saveFilePath = saveFilePath1.toString();
            //Toast.makeText(getBaseContext(), "File Ext", Toast.LENGTH_SHORT).show();
//            if (type.contains("html") || type.contains("menu")){
//                reload=false;
//            }
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
                Toast.makeText(getBaseContext(), "Cần kết nối internet!", Toast.LENGTH_SHORT).show();
            }
            return saveFilePath;
        }

    }
    private String createFolder()
    {
        String test = Environment.getExternalStorageDirectory().toString();
        String extStorageDirectory = Environment
                .getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, "/Android/data/" + getPackageName());
        if(!folder.exists())
        {
            folder.mkdir();
            Toast.makeText(MainActivity.this, "Folder Created At :" + folder.getPath().toString(), Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(MainActivity.this, "Folder Already At :" + folder.getPath().toString(), Toast.LENGTH_LONG).show();
        }
        return folder.getPath().toString();
    }


    public void nut_delete_allfile(View v){
        File dir = new File(Environment.getExternalStorageDirectory()+"/"+directory_parrent);
        Toast.makeText(getApplicationContext(), "Start delete files."+dir, Toast.LENGTH_SHORT).show();
        DeleteRecursive(dir);
        Toast.makeText(getApplicationContext(), "End delete.", Toast.LENGTH_SHORT).show();
    }
    void DeleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                DeleteRecursive(child);
        fileOrDirectory.delete();
    }

    public void andd_delete_files(){
        File dir = new File(Environment.getExternalStorageDirectory()+"/"+directory_parrent);
        //Toast.makeText(getApplicationContext(), "Start delete files."+dir, Toast.LENGTH_SHORT).show();
        DeleteRecursive(dir);
        //Toast.makeText(getApplicationContext(), "End delete.", Toast.LENGTH_SHORT).show();
        run("delete_complement()");
    }


    //load file bai1.html
    public void and_load(String url_load){
        //Toast.makeText(getApplicationContext(), "Load Url ", Toast.LENGTH_SHORT).show();
        reload=true;
        String file = null;
        try {
                file = downloadFile(url_load,"html");

        } catch (IOException e) {
            e.printStackTrace();
        }
        //webView.clearView();
        //nut_download_menu("http://192.168.0.15/a/sach_data/menu.html");
        //webView.reload();
        //webView.loadUrl("file://"+file);

        //webView.clearCache(true);
        //webView.loadUrl("file://"+saveFilePath);
        String url_link="file://"+saveFilePath;
        run_url(url_link);
        //Toast.makeText(getApplicationContext(), "file://"+saveFilePath, Toast.LENGTH_SHORT).show();

    }
    String address_local="file:///sdcard/a1/";
    String address_part="";

    //load file hinhanh cho menu
    boolean reload =true;
    public void and_onload_first(String data_first[]){
        //Toast.makeText(getApplicationContext(), "and_onload_first ", Toast.LENGTH_SHORT).show();
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

        if (reload){
            //Toast.makeText(getApplicationContext(),"Reload is "+String.valueOf(reload) , Toast.LENGTH_SHORT).show();
            //MainActivity.this.getUi().loadUrl(String.format(MainActivity.LOG_TO_WEB_UI_URL, DateFormat.getTimeInstance().format(new Date())));
            //MainActivity.this.getUi().loadUrl("javascript:reload()");
            //String js="alert('alert from java')";
            String js="reload()";

            //webView.loadUrl("JavaScript:" +js);
            //webView.loadUrl("javascript:reload();");
            run(js);
            //Toast.makeText(getApplicationContext(),"Toats JavaScript "+String.valueOf(reload) , Toast.LENGTH_SHORT).show();
        }
        //boolean reload =true;
    }
    public void run(final String scriptSrc) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl("javascript:" + scriptSrc);
            }
        });
    }
    public void run_url(final String url) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                webView.loadUrl(url);
            }
        });
    }

    //reload for menu
    public void and_reload_ok(){
        reload =false;
    }
    //reload for bai1+ truyen danh sach mp3
    public void and_reload_ok_listmp3(String data[]){
        reload =false;
        //code read list mp3
        daad=data;
        leng_mp3=daad.length;
    }

    public String daad[]=null;
    public int leng_mp3=1;
    //public void setValue_link(String data[]){

        //showGui("test");
    //}
        public void and_play1mp3(final int songid) throws IOException {

//        this.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
            run("is_checkbook()");
            is_play_list=false;
            try {
                baihientai=songid;
                //Toast.makeText(getApplicationContext(),"Start play > " +daad[songid] , Toast.LENGTH_SHORT).show();
                music_play(daad[songid]);

            } catch (IOException e) {
                e.printStackTrace();
            }
//            }
//        });
            ///return a;
        }
    public int baihientai=1;
    private int list_play_baihientai=0;
    MediaPlayer mediaPlayer = new MediaPlayer();;
    private void music_play(final String url) throws IOException {

        killMediaPlay();

        //mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        // mediaPlayer
        //mediaPlayer.reset();
        //mediaPlayer = new MediaPlayer();
        //String url = "http://192.168.0.15/a/music/animals__martin.mp3"; // your URL here
        //mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mediaPlayer.setDataSource(url);

        String fileplay = downloadFile(url, "mp3");


        Uri myUri = Uri.parse(fileplay);
        mediaPlayer = MediaPlayer.create(MainActivity.this, myUri);
        /* mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Toast.makeText(MainActivity.this, "Dang Play:"+fileplay, Toast.LENGTH_SHORT).show();
        mediaPlayer.setDataSource(getApplicationContext(), myUri);
        mediaPlayer.prepare(); // might take long! (for buffering, etc)*/
        mediaPlayer.start();
        // mediaPlayer.release();
        //WebAppInterface infte = new WebAppInterface();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                if (is_play_list) {
                    list_play_baihientai++;
                    if (list_play_baihientai < i_last + 1) {
                        try {
                            music_play(daad[list_play_baihientai]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //Toast.makeText(MainActivity.this, "Play_list:"+daad[list_play_baihientai], Toast.LENGTH_LONG).show();
                       // webView.loadUrl("javascript:showAndroidToast11(" + (list_play_baihientai - 1) + ")");
                        run("javascript:showAndroidToast11(" + (list_play_baihientai - 1) + ")");
                    } else {
                        //showGui("Hết đoạn");
                        list_play_baihientai = i_first;
                        run("javascript:showAndroidToast11(" + (list_play_baihientai - 1) + ")");
                        //webView.loadUrl("javascript:showAndroidToast11(" + (list_play_baihientai - 1) + ")");
                        try {
                            music_play(daad[i_first]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //
                        //music_play();
                        //is_play_list = false;
                    }
                    return;
                }
                run("is_checkbook()");

                if (is_a_playall) {
                    try {
                        baihientai = baihientai + 1;
                        //showGui();
                        if ((baihientai < (leng_mp3)) && baihientai > 0) {

                            music_play(daad[baihientai]);

                            //Toast.makeText(MainActivity.this, "Play:"+daad[baihientai], Toast.LENGTH_LONG).show();
                            //webView.loadUrl("javascript:showAndroidToast0(" + (baihientai - 1) + ")");
                            run("javascript:showAndroidToast0(" + (baihientai - 1) + ")");

                        } else {
                            //showGui("Hết all bài");
                            baihientai = 0;
                            killMediaPlay();
                            //showHetBai();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
            //Toast.makeText(MainActivity.this,infte.linksmp3[1],Toast.LENGTH_LONG).show();
        });
        //return  "Het";
    }

    public void killMediaPlay() {
        try {
            mediaPlayer.pause();
            mediaPlayer.release();
            mediaPlayer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private int i_first=0;
    private int i_last=0;
    private boolean is_play_list=false;

    public void play_part_listmp3(int i1,int i2){
        i_first=i1;
        i_last=i2;
        list_play_baihientai=i1;
        is_play_list=true;
        try {

            music_play(daad[i_first]);



        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void btn_sTop(View v) {
        run("stopPlay()"); //javascript
        //webView.loadUrl("javascript:stopPlay()");
    }
    public void sTop(){
        killMediaPlay();
        is_play_list=false;
//        i_first=0;
//        i_last=0;
        //baihientai=0;
        list_play_baihientai=0;
    }
    boolean is_a_playall=false;
    public void and_is_playyall(boolean is_playall){
        is_a_playall=is_playall;
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
//    public WebAppInterface(final MainActivity activity) {
//        this.activity = activity;
//    }


    /** Instantiate the interface and set the context */
    public WebAppInterface(final MainActivity activity) {
        //mContext = c;
        this.activity = activity;
    }



    @JavascriptInterface
    public void load(String url_load){
        this.activity.and_load(url_load);
    }

    @JavascriptInterface
    public void load_url(String url_load){
        this.activity.and_load(url_load);
    }
    @JavascriptInterface
    public void onload_first(String data_first[]){

        this.activity.and_onload_first(data_first);
    }
    @JavascriptInterface
    public void reload_ok(){
        this.activity.and_reload_ok();
    }
    //setValue

    @JavascriptInterface
    public void reload_ok_listmp3(String links_mp3[]){
        this.activity.and_reload_ok_listmp3(links_mp3);
    }

    @JavascriptInterface
    public void showToast(int songid_in) throws IOException {
        //this.activity.action_btn(songid_in);
        this.activity.and_play1mp3(songid_in);
    }
    @JavascriptInterface
    public void play1mp3(int songid_in) throws IOException {
        this.activity.and_play1mp3(songid_in);
    }
    @JavascriptInterface
    public void stopPlay() {
        this.activity.sTop();
    }
    @JavascriptInterface
    public void play_part(int i1,int i2){
        this.activity.play_part_listmp3(i1,i2);
    }
    @JavascriptInterface
    public void is_playyall(boolean isplay){
        this.activity.and_is_playyall(isplay);
    }
    @JavascriptInterface
    public void delete_files(){
        this.activity.andd_delete_files();
    }

}
