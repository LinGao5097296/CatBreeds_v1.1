package com.sky.CatBreeds.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sky.CatBreeds.R;
import com.sky.CatBreeds.adapter.DetailListAdapter;
import com.sky.CatBreeds.database.DBManager;
import com.sky.CatBreeds.database.Note;
import com.sky.CatBreeds.utils.LogUtils;
import com.sky.CatBreeds.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 套餐列表
 */
public class KeepFragment extends Fragment implements  AdapterView.OnItemLongClickListener{

    private Handler handler;
    private FloatingActionButton btnAdd;
    private  SwipeRefreshLayout swipe;
    private ListView listView;

    private DBManager dm;
    private List<Note> noteDataList = new ArrayList<>();
    private DetailListAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_keep,container,false);

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

        listView.setOnItemClickListener(new NoteClickListener());
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


    //listView单击事件
    private class NoteClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

            final Note note = (Note) ((DetailListAdapter) adapterView.getAdapter()).getItem(i);
            if (note == null) {

            }
            final int id = note.getId();
            final  String bid=note.getBid();
            new android.app.AlertDialog.Builder(getActivity())
                    .setMessage("是否取消收藏？")
                    .setNegativeButton("取消",null)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DBManager.getInstance(getActivity()).deleteNote(id);
                            adapter.removeItem(i);
                            saveLibID(bid);
                           load();
                        }
                    }).show();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        return false;
    }
    /**
     * 保存图书ID
     */
    public void saveLibID(String mid) {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(getContext(), "MID");
        helper.putValues(new SharedPreferencesUtils.ContentValue(mid, ""));

    }
    // 自定义一个加载数据库中的全部记录到当前页面的无参方法
    public void load() {
        noteDataList.clear();
        Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()));
        dm = new DBManager(getContext());
        dm.readFromDB(noteDataList);

        adapter = new DetailListAdapter(noteDataList);
        listView.setAdapter(adapter);

    }
}
