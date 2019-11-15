package com.sky.CatBreeds.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sky.CatBreeds.LibDetailsActivity;
import com.sky.CatBreeds.R;
import com.sky.CatBreeds.adapter.MealListAdapter;
import com.sky.CatBreeds.database.DBManager;
import com.sky.CatBreeds.database.Model;
import com.sky.CatBreeds.database.Note;
import com.sky.CatBreeds.utils.DbUtil;
import com.sky.CatBreeds.utils.LogUtils;

import java.util.List;


/**
 * 首页
 */
public class HomepageFragment extends Fragment {

    private Handler handler;
    private List<Model> modelList;
    private  SwipeRefreshLayout swipe;
    private DBManager dbManager;
    private ListView listView;
    private String catalog="全部";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WorkThread wt=new WorkThread();
        wt.start();//调动子线程

        View view = inflater.inflate(R.layout.fragment_homepage,container,false);
        listView=(ListView) view.findViewById(R.id.list_meal);
        swipe=(SwipeRefreshLayout) view.findViewById(R.id.swipe);
        //listView.setOnItemClickListener(listViewItemClickListener);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                TextView tv_note_id = (TextView) view.findViewById(R.id.item_tv_id);
                String item_id = tv_note_id.getText().toString();
                TextView tv_note_name = (TextView) view.findViewById(R.id.item_tv_title);
                String item_name = tv_note_name.getText().toString();
                TextView tv_note_price = (TextView) view.findViewById(R.id.item_tv_price);
                String item_price = tv_note_price.getText().toString();
                TextView tv_note_desc = (TextView) view.findViewById(R.id.item_tv_desc);
                String item_desc = tv_note_desc.getText().toString();
                TextView tv_note_img = (TextView) view.findViewById(R.id.item_tv_status);
                String item_img = tv_note_img.getText().toString();
                TextView tv_note_date = (TextView) view.findViewById(R.id.item_tv_date);
                String item_date = tv_note_date.getText().toString();

                Note details=new Note(item_id,item_name,item_desc,item_price,item_img);
                Intent intent=new Intent(getContext(), LibDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("details", details);
                intent.putExtra("data", bundle);
                intent.putExtra("catalog",item_date);
                startActivity(intent);
            }
        });
        initView();
        return view;
    }


    /**
     * 初始化view
     */
    private void initView() {



        dbManager = new DBManager(getActivity());

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

                loadDetails();
            }
        });

        loadDetails();
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
                        case 1:
                            Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()));
                            DbUtil db_load2 = new DbUtil();//调用数据库查询类
                            //  List<Model>  modelList=new ArrayList<>();
                            Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()));
                            modelList = db_load2.QueryContent(catalog);//得到返回值
                            // if(progressDialog.isShowing())   progressDialog.dismiss();
                            mHandler.obtainMessage(11).sendToTarget();
                            break;
                    }
                }
            };
            Looper.loop();//Looper循环，通道中有数据执行，无数据堵塞
        }
    }

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 11:
                    Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+modelList.size());
                    MealListAdapter adapter2 = new MealListAdapter(modelList,getActivity());
                    listView.setAdapter(adapter2);
                    break;
                default:
                    break;
            }
        }

    };
    // 自定义一个加载数据库中的全部记录到当前页面的无参方法
    public void loadDetails() {
        Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()));
        Message m = handler.obtainMessage();//获取事件
        m.what = 1;
        handler.sendMessage(m);//把信息放到通道中
    }
}
