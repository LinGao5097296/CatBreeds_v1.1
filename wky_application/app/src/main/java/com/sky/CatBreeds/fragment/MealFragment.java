package com.sky.CatBreeds.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.sky.CatBreeds.LibDetailsActivity;
import com.sky.CatBreeds.R;
import com.sky.CatBreeds.adapter.MealListAdapter;
import com.sky.CatBreeds.database.DBManager;
import com.sky.CatBreeds.database.Model;
import com.sky.CatBreeds.database.Note;
import com.sky.CatBreeds.utils.DbUtil;
import com.sky.CatBreeds.utils.LogUtils;
import com.sky.CatBreeds.utils.SharedPreferencesUtils;

import java.util.List;

import static cn.bmob.v3.Bmob.getApplicationContext;


/**
 * 套餐列表
 */
public class MealFragment extends Fragment implements  AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    private Handler handler;
    private List<Model> modelList;
    private FloatingActionButton btnAdd;
    private  SwipeRefreshLayout swipe;
    private ListView listView;
    private DBManager dbManager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WorkThread wt=new WorkThread();
        wt.start();//调动子线程
        View view = inflater.inflate(R.layout.fragment_meal,container,false);

        listView=(ListView) view.findViewById(R.id.list_meal);
        btnAdd =(FloatingActionButton)view.findViewById(R.id.btn_add);
        swipe=(SwipeRefreshLayout) view.findViewById(R.id.swipe);

        initView();
        return view;
    }


    /**
     * 初始化view
     */
    private void initView() {
        dbManager = new DBManager(getActivity());
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        //改变加载显示的颜色
        swipe.setColorSchemeColors(getResources().getColor(R.color.text_red), getResources().getColor(R.color.text_red));
        //设置向下拉多少出现刷新
        swipe.setDistanceToTriggerSync(200);
        //设置刷新出现的位置
        swipe.setProgressViewEndTarget(false, 200);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(false);

                load();
            }
        });

        load();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            TextView tv_note_id = (TextView) view.findViewById(R.id.item_tv_id);
            String item_id = tv_note_id.getText().toString();
            TextView tv_note_name = (TextView) view.findViewById(R.id.item_tv_title);
            String item_name = tv_note_name.getText().toString();
            TextView tv_note_price = (TextView) view.findViewById(R.id.item_tv_price);
            String item_price = tv_note_price.getText().toString();
        TextView tv_note_img = (TextView) view.findViewById(R.id.item_tv_status);
        String item_img = tv_note_img.getText().toString();
            String libcontent = getLibContent(item_id);
           // saveItemData(item_id,item_name,libcontent,item_price);
        Note details=new Note(item_id,item_name,libcontent,item_price,item_img);
            showGenderDialog(details);
    }
    /**
     * 显示选择对话框
     */
    public void showGenderDialog(final Note details) {
        new AlertDialog.Builder(getContext()).setItems(new String[]{"收藏", "详情"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: //
                                Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+getLibID(details.getBid())+details.getBid());

                                if (getLibID(details.getBid())!=null && getLibID(details.getBid()).equals(details.getBid())) {
                                    Toast.makeText(getContext(), "已收藏，请勿重复操作！" ,
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    dbManager.addToDB(details.getBid(), details.getTitle(), details.getContent(), details.getTime());
                                    Toast.makeText(getContext(), "收藏成功", Toast.LENGTH_SHORT).show();
                                    saveLibID(details.getBid());
                                }
                                break;
                            case 1:
                                Intent intent=new Intent(getContext(), LibDetailsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("details", details);
                                intent.putExtra("data", bundle);
                                startActivity(intent);
                                break;
                        }

                    }
                }).show();
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

            TextView tv_note_id = (TextView) view.findViewById(R.id.item_tv_id);
            final String item_id = tv_note_id.getText().toString();
            String libcontent = getLibContent(item_id);
            final EditText editText = new EditText(getContext());
            editText.setMaxLines(10);
            editText.setLines(5);
            editText.setText(libcontent);
            if (libcontent != null) {
                //将光标移至文字末尾
                editText.setSelection(libcontent.length());
            }
            new AlertDialog.Builder(getContext()).setTitle("编辑简介")
                    .setView(editText)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            final String input = editText.getText().toString();
                            if (input.equals("")) {
                                Toast.makeText(getApplicationContext(), "内容不能为空！" + input,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                saveLibContent(item_id, input);
                            }
                        }
                    })
                    .show();
        return true;
    }
    /**
     * 获得保存在本地的图书简介
     */
    public String getLibContent(String MID) {
        //获取SharedPreferences对象，使用自定义类的方法来获取对象
        SharedPreferencesUtils helper = new SharedPreferencesUtils(getContext(), "content");
        return  helper.getString(MID);
    }
    /**
     * 保存图书简介
     */
    public void saveLibContent(String MID,String input) {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(getContext(), "content");
        helper.putValues(new SharedPreferencesUtils.ContentValue(MID, input));

    }
    /**
     * 保存图书ID
     */
    public void saveLibID(String mid) {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(getContext(), "MID");
        helper.putValues(new SharedPreferencesUtils.ContentValue(mid, mid));

    }
    /**
     * 获取图书ID
     */
    public String getLibID(String mid) {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(getContext(), "MID");
       return helper.getString(mid);

    }
    class WorkThread extends Thread {
        @Override
        public  void run(){
            Looper.prepare();
            //添加
            handler=new Handler(){
                @Override
                public  void handleMessage(Message m){
                    switch (m.what) {
                        case 2://加载
                            Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()));
                            DbUtil db_load = new DbUtil();//调用数据库查询类
                            //  List<Model>  modelList=new ArrayList<>();
                            Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()));
                            modelList = db_load.QueryModule();//得到返回值
                            // if(progressDialog.isShowing())   progressDialog.dismiss();
                            mHandler.obtainMessage(0).sendToTarget();
                            break;
                    }
                }
            };
            Looper.loop();//Looper循环，通道中有数据执行，无数据堵塞
        }
    }
    // 自定义一个加载数据库中的全部记录到当前页面的无参方法
    public void load() {
        Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()));
        Message m = handler.obtainMessage();//获取事件
        m.what = 2;
        handler.sendMessage(m);//把信息放到通道中
    }
    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+modelList.size());
                    MealListAdapter adapter = new MealListAdapter(modelList,getContext());
                    listView.setAdapter(adapter);
                    break;
                default:
                    break;
            }
        }

    };
}
