package com.example.administrator.discussapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
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


public class Staff2Activity extends ActionBarActivity {

    private static   String getURLServer = "http://192.168.1.113:8080/DiscussWeB2/";
    public ImageLoader imageLoader;
    private GridView gridV;
    private ImageAdapter imageAdap;
    ///value Spinner

    /////get Staff
    private static final String TAG_CAT_ID_STAFF = "cat_id";
    private static final String TAG_CAT_TOPPIC= "cat_topic";
    private static final String TAG_USERNAME_STAFF = "username";
    private static final String TAG_TIME_CATE = "date";
    private static final String TAG_Count = "num_reply";
    //JSON Node Names Gridviwe

    private static final String TAG_TOPIC_ID = "topic_id";
    private static final String TAG_CAT_ID = "cat_id";
    private static final String TAG_TOPIC = "topic";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_IMG = "img";
    private static final String TAG_DATA = "data";
    private static final String TAG_TIME = "dateTime";
    private static final String URLImg = getURLServer+"images/";
    JSONArray Data = null;
    ArrayList<HashMap<String, Object>> cateList = new ArrayList<>();
    //ArrayList<HashMap<String, Object>> cateList = new ArrayList<HashMap<String, Object>>();
    JSONParser jParser = new JSONParser();

    private String username ,topicID,catID,roleID;
    private String TopicId,CatId,Username;
    String nameCatStaff,catIDStaff,usernameStaff,countComment,timeStaffCat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_staff2);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ImageButton BtnPost = (ImageButton) this.findViewById(R.id.PostIcon);
        ImageButton BtnUpdate = (ImageButton) this.findViewById(R.id.UpdateIcon);
        ImageButton BtnSreach = (ImageButton) this.findViewById(R.id.btnSearh);

        ImageView ImgUser = (ImageView) this.findViewById(R.id.imgUser_Landing);
        ImgUser.setImageResource(R.drawable.bt_id);
        BtnPost.setImageResource(R.drawable.post);
        BtnUpdate.setImageResource(R.drawable.icon);
        BtnSreach.setImageResource(R.drawable.searh);


        //Get Parameter From Login Actvity
        Bundle intent = getIntent().getExtras();

        if (intent != null) {
            this.username = intent.getString("username");
            this.roleID = intent.getString("role_id");

            // and get whatever type user account id is
        }

        // Get Username//
        TextView NameUser = (TextView) this.findViewById(R.id.NameUser);
        NameUser.setText(username);

        // String urlStaff = getURLServer + "jsonCatStaff?username="+username+"";
       // String urlStaff = getURLServer + "jsonCatStaff?username="+username+"";\
        String urlStaff = getURLServer + "jsonCatStaff?username=admin3";
        JSONObject jsonCate = jParser.getJSONFromUrl(urlStaff);



        try {

// Getting JSON Array
            Data = jsonCate.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                catIDStaff = c.getString(TAG_CAT_ID_STAFF);
                nameCatStaff = c.getString(TAG_CAT_TOPPIC);
                usernameStaff = c.getString(TAG_USERNAME_STAFF);
                timeStaffCat = c.getString(TAG_TIME_CATE);
                countComment = c.getString(TAG_Count);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        TextView cateName = (TextView)this.findViewById(R.id.cateName);
        cateName.setText(nameCatStaff);
        TextView countCate= (TextView)this.findViewById(R.id.countCate);
        countCate.setText(countComment);
        /////////
        String url = getURLServer+"jsonPost_reply2?cat_id="+catIDStaff+"";
        Bitmap newBitmap;
        // Getting JSON from URL
        JSONObject json = jParser.getJSONFromUrl(url);
        imageLoader = new ImageLoader(this);
        // GridView and imageAdapter

        try {

// Getting JSON Array
            Data = json.getJSONArray(TAG_DATA);

            for (int i = 0; i < Data.length(); i++) {
                JSONObject c = Data.getJSONObject(i);
                String topicID = c.getString(TAG_TOPIC_ID);
                String catID = c.getString(TAG_CAT_ID);
                String topic = c.getString(TAG_TOPIC);
                String owner = c.getString(TAG_OWNER);
                String img = c.getString(TAG_IMG);
                String dateTime = c.getString(TAG_TIME);

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put(TAG_TOPIC_ID, topicID);
                map.put(TAG_CAT_ID, catID);
                map.put(TAG_TOPIC, topic);
                map.put(TAG_OWNER, owner);
                map.put(TAG_TIME, dateTime);
                // Thumbnail Get ImageBitmap To Object
                String urlBitMap = URLImg+img;
                newBitmap = imageLoader.getBitmap(urlBitMap);


                map.put("ImagePathBitmap", newBitmap);
                cateList.add(map);


            }


            /// Start Grid//
            gridV = (GridView) findViewById(R.id.gridViewStaff2);
            // Next
            gridV.setClipToPadding(false);
            imageAdap = new ImageAdapter(getApplicationContext());
            gridV.setAdapter(imageAdap);

            gridV.setOnScrollListener(new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    // Triggered only when new data needs to be appended to the list
                    // Add whatever code is needed to append new items to your AdapterView
                    customLoadMoreDataFromApi(page);
                    // or customLoadMoreDataFromApi(totalItemsCount);
                }
            });









        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext()
                    , "เชื่อมต่อระบบล้มเหลว ", Toast.LENGTH_LONG).show();
        }



        final AlertDialog.Builder viewDetail = new AlertDialog.Builder(this);
        // OnClick Item
        gridV.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> myAdapter, View myView,
                                    int position, long mylng) {

                topicID =  cateList.get(position).get("topic_id").toString();
                SetTopicId(topicID);
                catID =  cateList.get(position).get("cat_id").toString();
                SetCatId(catID);
                Intent it = new Intent(getApplicationContext(), CommentActivity.class);

                it.putExtra("topic_id", topicID);
                it.putExtra("username",username);
                it.putExtra("cat_id",catIDStaff);
                it.putExtra("role_id",roleID);

                startActivity(it);
            }

        });
        BtnSreach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent it = new Intent(getApplicationContext(), SearchActivity.class);

                it.putExtra("username",username);
                it.putExtra("topic_id", topicID);
                it.putExtra("cat_id",catIDStaff);
                it.putExtra("role_id",roleID);
                Toast.makeText(getApplicationContext()
                        ,"ค้นหาข้อมูล",Toast.LENGTH_LONG).show();
                System.out.println("");
                startActivity(it);
            }
        });


        ///// button Post Activity
        BtnPost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), PostActivity.class);

                it.putExtra("username",username);
                it.putExtra("topic_id", topicID);
                it.putExtra("cat_id",catIDStaff);
                it.putExtra("role_id",roleID);
                Toast.makeText(getApplicationContext()
                        ,"เพิ่มกระทู้",Toast.LENGTH_LONG).show();
                System.out.println("");
                startActivity(it);

            }
        });
        BtnUpdate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent it = new Intent(getApplicationContext(), EditPostActivity.class);
                it.putExtra("topic_id", topicID);
                it.putExtra("username",username);
                it.putExtra("cat_id",catIDStaff);
                it.putExtra("role_id",roleID);
                Toast.makeText(getApplicationContext()
                        ,"แก้ไขข้อมูลส่วนตัว"+roleID,Toast.LENGTH_LONG).show();
                System.out.println("");
                startActivity(it);

            }
        });
    }



    public void customLoadMoreDataFromApi(int offset) {
        // This method probably sends out a network request and appends new data items to your adapter.
        // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
        // Deserialize API response and then construct new objects to append to the adapter
    }

    public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 5;
        // The current offset index of data you have loaded
        private int currentPage = 0;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;
        // Sets the starting page index
        private int startingPageIndex = 0;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        public EndlessScrollListener(int visibleThreshold, int startPage) {
            this.visibleThreshold = visibleThreshold;
            this.startingPageIndex = startPage;
            this.currentPage = startPage;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount)
        {
            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) { this.loading = true; }
            }
            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
                currentPage++;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
                onLoadMore(currentPage + 1, totalItemCount);
                loading = true;
            }
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount);

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Don't take any action on changed
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
    public String GetUsername(){
        return Username;
    }
    public void SetUsername(String  Username){
        this. Username=Username;
    }
    /////ImageAdapter/////
    class ImageAdapter extends BaseAdapter {

        private Context mContext;

        public ImageAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return cateList.size();
        }

        public Object getItem(int position) {
            return cateList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        class ViewHolderItem {
            ImageView imageView;
            TextView txtImageID;
            TextView txtItemID;
            TextView txtTimeID;
            int position = -1;
            Handler handler;
            ImageButton btnUpdate, btnDelete, btnPoint, btnGO;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub

            ViewHolderItem viewHolder = null;
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            if (convertView == null) {


                viewHolder = new ViewHolderItem();
                convertView = inflater.inflate(R.layout.activity_multibtn_staff, null);
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.ColImgPath);
                convertView.setTag(viewHolder);

                // ColPhoto

                //imageView.getLayoutParams().height = 60;
                // imageView.getLayoutParams().width = 60;
                viewHolder.imageView.setPadding(5, 5, 5, 5);
                viewHolder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                try {
                    Log.d("ERROR", cateList.get(position).get("ImagePathBitmap").toString());
                    viewHolder.imageView.setImageBitmap((Bitmap) cateList.get(position).get("ImagePathBitmap"));
                    //  AsyncTask<String, Integer, Bitmap> exec = new Bitmapload(imageView);
                    // exec.execute(cateList.get(position).get("ImagePathBitmap").toString());


                } catch (Exception e) {
                    // When Error
                    viewHolder.imageView.setImageResource(R.drawable.add_image);
                }

                // ColImageID
                viewHolder.txtImageID = (TextView) convertView.findViewById(R.id.ColTopic);
                viewHolder.txtImageID.setPadding(5, 0, 0, 0);
                viewHolder.txtImageID.setText(cateList.get(position).get("topic").toString());

                // ColItemID
                viewHolder.txtItemID = (TextView) convertView.findViewById(R.id.Colowner);
                viewHolder.txtItemID.setPadding(5, 0, 0, 0);
                viewHolder.txtItemID.setText(cateList.get(position).get("owner").toString());
//ColItemTime
                viewHolder.txtTimeID = (TextView) convertView.findViewById(R.id.Coltime);
                viewHolder.txtTimeID.setPadding(5, 0, 0, 0);
                viewHolder.txtTimeID.setText(cateList.get(position).get("dateTime").toString());

                //Update
                viewHolder.btnUpdate = (ImageButton) convertView.findViewById(R.id.imgCmdupdate);
                viewHolder.btnUpdate.setImageResource(R.drawable.edit2);


                viewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Toast.makeText(Staff2Activity.this, "Your Shared (ImageID = " + cateList.get(position).get("topic_id") + catIDStaff + "///" + roleID, Toast.LENGTH_LONG).show();

                        Intent it = new Intent(getApplicationContext(), EditCommentActivity.class);
                        String topPicId = cateList.get(position).get("topic_id").toString();
                        it.putExtra("topic_id", topPicId);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catIDStaff);
                        it.putExtra("role_id", roleID);


                        Toast.makeText(getApplicationContext()
                                , "แก้ไขข้อมูล" + roleID, Toast.LENGTH_LONG).show();
                        System.out.println("");
                        startActivity(it);
                    }
                });
                //Delete
                viewHolder.btnDelete = (ImageButton) convertView.findViewById(R.id.imgCmdelete);
                viewHolder.btnDelete.setImageResource(R.drawable.delete);
                // imgCmdDelete

                //cmdDelete.setBackgroundColor(Color.TRANSPARENT);
                final AlertDialog.Builder adb = new AlertDialog.Builder(Staff2Activity.this);
                viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        adb.setTitle("Delete?");
                        adb.setMessage("Are you sure delete [" + cateList.get(position).get("topic_id") + "]");
                        adb.setNegativeButton("Cancel", null);
                        adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Staff2Activity.this, "Your Delete (ImageID = " + cateList.get(position).get("topic_id") + ")", Toast.LENGTH_LONG).show();
                                topicID = cateList.get(position).get("topic_id").toString();

                                try {
                                    URL url = new URL(getURLServer + "DeleteTopic?topic_id=" + topicID + "&cat_id=" + catIDStaff + "");
                                    Scanner sc = new Scanner(url.openStream());
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                /**
                                 * Command for Delete
                                 * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                                 */
                                Intent it = new Intent(getApplicationContext(), Staff2Activity.class);

                                it.putExtra("topic_id", topicID);
                                it.putExtra("username", username);
                                it.putExtra("cat_id", catIDStaff);
                                it.putExtra("role_id", roleID);

                                startActivity(it);
                            }
                        });
                        adb.show();
                    }
                });
                ///Top
                viewHolder.btnPoint = (ImageButton) convertView.findViewById(R.id.imgCmdpoint);
                viewHolder.btnPoint.setImageResource(R.drawable.point);

                final AlertDialog.Builder dialogP = new AlertDialog.Builder(Staff2Activity.this);
                viewHolder.btnPoint.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        dialogP.setTitle("TOP Comment?");
                        dialogP.setMessage("Are you sure delete [" + cateList.get(position).get("topic_id") + "]");


                        dialogP.setNegativeButton("DELETE TOP", new AlertDialog.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(Staff2Activity.this, "DeleteTOP(ImageID = " + cateList.get(position).get("topic_id") + ")", Toast.LENGTH_LONG).show();
                                topicID = cateList.get(position).get("topic_id").toString();
                                try {
                                    URL url = new URL(getURLServer + "UpdateTopComment?topic_id=" + topicID + "&status=0");
                                    Scanner sc = new Scanner(url.openStream());
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                /**
                                 * Command for Delete
                                 * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                                 */
                                Intent it = new Intent(getApplicationContext(), Staff2Activity.class);

                                it.putExtra("topic_id", topicID);
                                it.putExtra("username", username);
                                it.putExtra("cat_id", catIDStaff);
                                it.putExtra("role_id", roleID);

                                startActivity(it);

                            }
                        });


/////Delete Top Comment
                        dialogP.setPositiveButton("ADD TOP", new AlertDialog.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(Staff2Activity.this, "AddTOP(ImageID = " + cateList.get(position).get("topic_id") + ")", Toast.LENGTH_LONG).show();
                                topicID = cateList.get(position).get("topic_id").toString();

                                try {
                                    URL url = new URL(getURLServer + "UpdateTopComment?topic_id=" + topicID + "&status=1");
                                    Scanner sc = new Scanner(url.openStream());
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                /**
                                 * Command for Delete
                                 * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                                 */
                                Intent it = new Intent(getApplicationContext(), Staff2Activity.class);

                                it.putExtra("topic_id", topicID);
                                it.putExtra("username", username);
                                it.putExtra("cat_id", catIDStaff);
                                it.putExtra("role_id", roleID);

                                startActivity(it);
                            }
                        });
                        dialogP.show();
                    }
                });
                viewHolder.btnGO = (ImageButton) convertView.findViewById(R.id.toComment);
                viewHolder.btnGO.setImageResource(R.drawable.go);


                viewHolder.btnGO.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        /**
                         * Command for Delete
                         * Eg : myDBClass.DeleteData(MyArrList.get(position).get("ImageID"));
                         */
                        Toast.makeText(Staff2Activity.this, "" + cateList.get(position).get("topic_id") + catIDStaff + "//" + roleID + ")", Toast.LENGTH_LONG).show();

                        Intent it = new Intent(getApplicationContext(), CommentActivity.class);
                        topicID = cateList.get(position).get("topic_id").toString();
                        it.putExtra("topic_id", topicID);
                        it.putExtra("username", username);
                        it.putExtra("cat_id", catIDStaff);
                        it.putExtra("role_id", roleID);

                        startActivity(it);
                    }


                });
            } else {
                viewHolder = (ViewHolderItem) convertView.getTag();
            }

            return convertView;

        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_staff2, menu);
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
