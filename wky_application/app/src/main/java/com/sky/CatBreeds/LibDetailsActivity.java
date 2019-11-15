package com.sky.CatBreeds;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.CatBreeds.database.DBManager;
import com.sky.CatBreeds.database.Note;
import com.sky.CatBreeds.utils.LogUtils;
import com.sky.CatBreeds.utils.SharedPreferencesUtils;

import java.io.File;

/**
 * 新建帖子
 */
public class LibDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_CHOOSE = 23;//定义请求码常量
    private Handler handler;
    private TextView et_new_id;
    private TextView et_new_title;
    private TextView et_new_content;
    private TextView tv_new_time;
    private TextView tv_new_group;
    private TextView title;
    private TextView tvFinish;
    Toolbar toolbar;
    private int flag;//区分是新建笔记还是编辑笔记
    private FloatingActionButton saveBtn;
    ImageView imageView;
    private  Note details;
    private DBManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // 隐藏 ActionBar
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_libdetails);



        initEventAndData();
    }

    protected void initEventAndData() {
        dbManager = new DBManager(this);
        tvFinish=findViewById(R.id.tvFinish);
        title=findViewById(R.id.tvTitle);
        title.setText("detail");
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回消息更新上个Activity数据
                setResult(RESULT_OK, new Intent());
                finish();
            }
        });
        tvFinish.setVisibility(View.VISIBLE);
        tvFinish.setText("save");
        tvFinish.setOnClickListener(this);
        imageView= (ImageView) findViewById(R.id.item_iv_cover);
        et_new_id = (TextView) findViewById(R.id.et_new_id);
        et_new_title = (TextView) findViewById(R.id.et_new_title);
        et_new_content = (TextView) findViewById(R.id.et_new_content);
        et_new_content.setMovementMethod(ScrollingMovementMethod.getInstance());
        tv_new_time = (TextView) findViewById(R.id.tv_new_time);
        tv_new_group = (TextView) findViewById(R.id.tv_new_group);

        Intent intent = getIntent();
        String catalog=intent.getStringExtra("catalog");
        Bundle bundle = intent.getBundleExtra("data");
        details = (Note) bundle.getSerializable("details");
        String imag_path=details.getImg();
        et_new_title.setText(details.getTitle());
        et_new_content.setText(details.getContent());
        tv_new_group.setText(details.getTime());
        tv_new_time.setText(catalog);
        if (imag_path!=null) {
            String path = Environment.getExternalStorageDirectory() + "/listviewImage/";
            //File image = new File(path);
            File fileDir;
            /**
             * 文件目录如果不存在，则创建
             */
            fileDir = new File(path);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            /**
             * 创建图片文件
             */
            File imageFile = new File(fileDir, details.getTitle() + details.getBid() + ".PNG");
            String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + details.getTitle() + details.getBid() + ".PNG";
            Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()) + filePath);
            final File file = new File(filePath);
            // 如果本地已有缓存，就从本地读取，否则从网络请求数据

            if (file.exists()) {
                Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()) + "本地已有缓存");
                // holder.iv.setImageDrawable( mImageCache.get( player.getIcon().getFileUrl() ) );
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
                imageView.setImageBitmap(bitmap);

            }
        }

    }




    /**
     * 打开软键盘
     */
    private void openSoftKeyInput(){
        InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        //boolean isOpen=imm.isActive();//isOpen若返回true，则表示输入法打开
        if (imm != null && !imm.isActive() && et_new_content != null){
            et_new_content.requestFocus();
            //第二个参数可设置为0
            //imm.showSoftInput(et_content, InputMethodManager.SHOW_FORCED);//强制显示
            imm.showSoftInputFromInputMethod(et_new_content.getWindowToken(),
                    InputMethodManager.SHOW_FORCED);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFinish:
                if (getLibID(details.getBid())!=null && getLibID(details.getBid()).equals(details.getBid())) {
                    Toast.makeText(getBaseContext(), "areally saved" ,
                            Toast.LENGTH_SHORT).show();
                } else {
                    dbManager.addToDB(details.getBid(), details.getTitle(), details.getContent(), details.getTime());
                    Toast.makeText(getBaseContext(), "save successfully", Toast.LENGTH_SHORT).show();
                    saveLibID(details.getBid());
                }
                break;
        }
    }
    /**
     * 保存图书ID
     */
    public void saveLibID(String mid) {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "MID");
        helper.putValues(new SharedPreferencesUtils.ContentValue(mid, mid));

    }
    /**
     * 获取图书ID
     */
    public String getLibID(String mid) {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(this, "MID");
        return helper.getString(mid);

    }
}
