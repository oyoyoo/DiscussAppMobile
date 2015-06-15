package com.example.administrator.discussapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;


public class Comment2Activity extends ActionBarActivity {
    private static   String getURLServer = "http://192.168.1.2:8080/DiscussWeb/";
    private static final String TAG_ID = "id";
    private static final String TAG_TOPIC_ID = "topic_id";
    private static final String TAG_DESC = "description";
    private static final String TAG_NAME = "name";
    private static final String TAG_TIME = "date_time";
    private static final String TAG_DATA = "data";
    private ListView ListV2;
    JSONArray Data = null;
    private ListView ListV;
    ArrayList<HashMap<String, Object>> postList = new ArrayList<>();
    ImageView ImgPost;
    String toppicID ,catID,username;
    //getset
    private String CatId,TopicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ImageButton btnBack = (ImageButton) this.findViewById(R.id.imgBtnBack_Comment);
        btnBack.setImageResource(R.drawable.back);
        ImageButton btnPost = (ImageButton) this.findViewById(R.id.comment2);
        btnPost.setImageResource(R.drawable.postcomment);

        Bundle intent = getIntent().getExtras();

        if (intent != null) {
            this.toppicID = intent.getString("topic_id");
            this.catID = intent.getString("cat_id");
            this.username = intent.getString("username");
            // and get whatever type user account id is
        }
        TextView NameUser = (TextView) this.findViewById(R.id.NameUser);
        NameUser.setText(username);

        SetTopicId(toppicID);
        SetCatId(catID);
        ImageButton btnRefresh = (ImageButton) this.findViewById(R.id.btnRefresh);
        //refresh
        btnRefresh.setImageResource(R.drawable.refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), Comment2Activity.class);
                it.putExtra("topic_id", toppicID);
                it.putExtra("cat_id", catID);
                it.putExtra("username", username);
                System.out.println("");
                startActivity(it);

            }
        });
        //backTo CommentActivity

        btnBack.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), CommentActivity.class);

                it.putExtra("topic_id", toppicID);
                it.putExtra("cat_id", catID);
                it.putExtra("username", username);
                Toast.makeText(Comment2Activity.this,GetTopicId()+"cat_id"+GetCatId(),Toast.LENGTH_SHORT).show();
                System.out.println("");
                startActivity(it);

            }
        });
        ListV2 = (ListView) findViewById(R.id.listViewComment2);
        JSONParser jParser = new JSONParser();
        String url = getURLServer+"jsonPost_reply?&topic_id="+toppicID+"";
        JSONObject json = jParser.getJSONFromUrl(url);
        try {
// Comment Text

            Data = json.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                String topicID = c.getString(TAG_TOPIC_ID);

                String ID = c.getString(TAG_ID);

                String desc = c.getString(TAG_DESC);

                String name = c.getString(TAG_NAME);

                String dateTime = c.getString(TAG_TIME);


                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(TAG_TOPIC_ID, topicID);
                map.put(TAG_ID, ID);
                map.put(TAG_DESC, desc);
                map.put(TAG_NAME, name);
                map.put(TAG_TIME, dateTime);
                // Thumbnail Get ImageBitmap To Object

                postList.add(map);

            }

            SimpleAdapter sAdap;
            sAdap = new SimpleAdapter(Comment2Activity.this, postList, R.layout.activity_comment_text,
                    new String[]{"description", "name", "date_time"}, new int[]{R.id.Detail2Comment, R.id.post2Comment, R.id.Time2Comment});
            ListV2.setAdapter(sAdap);





////Post
            final EditText etdcomment =(EditText) this.findViewById(R.id.edtComment2);
            btnPost.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {
                    String desc=etdcomment.getText().toString();
                    if(desc.equals("")){
                        Toast.makeText(Comment2Activity.this,String.valueOf("ไม่มีข้อความ"),Toast.LENGTH_SHORT).show();
                    }
                    else {

                        URL url = null;


                        try {
                            url = new URL(getURLServer+"PostReplyAPI?name="+username+"&desc="+desc+"&top_id="+toppicID+"");

                            Scanner sc = new Scanner(url.openStream());
                            StringBuffer buf = new StringBuffer();
                            //refresh
                            Intent it = new Intent(getApplicationContext(), Comment2Activity.class);
                            it.putExtra("topic_id", toppicID);
                            it.putExtra("cat_id", catID);
                            it.putExtra("username", username);
                            System.out.println("");
                            startActivity(it);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        } catch (JSONException e) {
            Toast.makeText(getApplicationContext()
                    ,"เชื่อมต่อระบบล้มเหลว ",Toast.LENGTH_LONG).show();
            e.printStackTrace();

        }
    }



    public String GetTopicId(){
        return TopicId;
    }
    public void SetTopicId(String TopicId){
        this.TopicId=TopicId;
    }
    public String GetCatId(){
        return CatId;
    }
    public void SetCatId(String CatId){
        this.CatId=CatId;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_comment2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
