package com.sky.CatBreeds.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.sky.CatBreeds.R;
import com.sky.CatBreeds.database.DBManager;
import com.sky.CatBreeds.database.Model;
import com.sky.CatBreeds.utils.LogUtils;
import com.sky.CatBreeds.utils.SharedPreferencesUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;


/**
 * Created by Thinkpad on 2018/7/28.
 */

public class MealListAdapter extends BaseAdapter {

        private List<Model> list;
        private ListView listview;
        private LruCache<String, BitmapDrawable> mImageCache;
        private String imageIag="imagetag";
        private DBManager dbManager;
        Context mContext;
    String  checkstatus;
        public MealListAdapter(List<Model> list,Context context) {
            super();

            this.list = list;
            this.mContext=context;


        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (listview == null) {
                listview = (ListView) parent;
            }
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.meal_list_item, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.item_iv_cover);
                holder.item_tv_id = (TextView) convertView.findViewById(R.id.item_tv_id);
                holder.item_tv_title = (TextView) convertView.findViewById(R.id.item_tv_title);
                holder.item_tv_catalog = (TextView) convertView.findViewById(R.id.item_tv_made_from);
                holder.item_tv_type = (TextView) convertView.findViewById(R.id.item_tv_type);
                holder.item_tv_desc = (TextView) convertView.findViewById(R.id.item_tv_desc);
                holder.item_tv_price = (TextView) convertView.findViewById(R.id.item_tv_price);
                holder.item_tv_date = (TextView) convertView.findViewById(R.id.item_tv_date);
                holder.item_tv_status = (TextView) convertView.findViewById(R.id.item_tv_status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Model meal = list.get(position);
            imageIag=meal.getImg();

            holder.item_tv_id.setText(meal.getMID()+"");
            holder.item_tv_title.setText(meal.getName());
            holder.item_tv_type.setText(meal.getType());
            holder.item_tv_desc.setText(meal.getDesc());
            holder.item_tv_date.setText(meal.getDate_of_sale());
            holder.item_tv_price.setText(meal.getRn());

            if (getLibID(meal.getMID()+"")!=null && getLibID(meal.getMID()+"").equals(meal.getMID()+"")) {
                checkstatus ="saved";
            }else{
                checkstatus ="save";
            }
            Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+getLibID(meal.getMID()+"")+"|"+meal.getMID()+"");

            holder.item_tv_status.setText(checkstatus);

            final ViewHolder finalHolder = holder;
            holder.item_tv_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeOrderState(meal, finalHolder);
                }
            });
            //holder.iv.setImageResource(ImageArray[meal.getMID()%10]);
            Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+meal.getImg());

            if (meal.getImg()!=null) {

                holder.iv.setTag( imageIag );
                String path = Environment.getExternalStorageDirectory()+"/listviewImage/";
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
                File imageFile = new File(fileDir, meal.getName()+meal.getMID()+".PNG");
                String filePath = Environment.getExternalStorageDirectory().getPath()+"/"+meal.getName()+meal.getMID()+".PNG";
                Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+filePath);
                final File file=new File(filePath);
                // 如果本地已有缓存，就从本地读取，否则从网络请求数据

                if (file.exists()) {
                    Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+"本地已有缓存");
                    // holder.iv.setImageDrawable( mImageCache.get( player.getIcon().getFileUrl() ) );
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 1;
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath,options);
                    holder.iv.setImageBitmap(bitmap);

                } else {

                    Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+"本地没有缓存，从网络获取");
                    Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+path);
                    //新建线程加载图片信息，发送到消息队列中
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Bitmap bmp = getURLimage(meal.getImg(),file);
                            Message msg = new Message();
                            msg.what = 0;
                            msg.obj = bmp;
                            handle.sendMessage(msg);
                        }
                    }).start();

                }
            } else {
                holder.iv.setImageResource(R.drawable.book1);
            }
            return convertView;
        }
    /**
     * 根据url从网络上下载图片
     *
     * @return
     */

    private Bitmap getURLimage(String imageUrl,File tempFile) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(imageUrl);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            //使用buffer流保存到File中
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();

            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;

    }

    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bmp=(Bitmap)msg.obj;
                    ImageView iv = (ImageView) listview.findViewWithTag(imageIag );
                    iv.setImageBitmap(bmp);
                    break;
            }
        };
    };

    private void changeOrderState(final Model morder,final ViewHolder holder) {

        switch (checkstatus) {
            case "saved":
                new AlertDialog.Builder(mContext).setTitle("do you want to delete save？")
                        .setNegativeButton("no", null)
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DBManager.getInstance(mContext).deleteNoteByName(morder.getName());
                                holder.item_tv_status.setText("save");
                                removeLibID(morder.getMID() + "");
                                checkstatus="save";
                            }
                        })
                        .show();
                break;
                case "save":
                    new AlertDialog.Builder(mContext).setTitle("do you want to save？")
                            .setNegativeButton("no", null)
                            .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dbManager = new DBManager(mContext);
                                    dbManager.addToDB(morder.getMID()+"", morder.getName(), morder.getDesc(), morder.getDate_of_sale());
                                    holder.item_tv_status.setText("saved");
                                    saveLibID(morder.getMID()+"");
                                    checkstatus="saved";
                                }
                            })
                            .show();
                    break;
                default:
                    break;
            }
        }
    /**
     * 保存图书ID
     */
    public void saveLibID(String mid) {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(mContext, "MID");
        helper.putValues(new SharedPreferencesUtils.ContentValue(mid, mid));

    }
    /**
     * 获取图书ID
     */
    public String getLibID(String mid) {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(mContext, "MID");
        return helper.getString(mid);

    }
    /**
     * removeID
     */
    public void removeLibID(String mid) {
        SharedPreferencesUtils helper = new SharedPreferencesUtils(mContext, "MID");
        helper.putValues(new SharedPreferencesUtils.ContentValue(mid, ""));

    }
    class ViewHolder {
            ImageView iv;
            TextView item_tv_id,item_tv_title,item_tv_catalog,item_tv_type,item_tv_desc,item_tv_cover,item_tv_status,item_tv_price,item_tv_date;
        }


}
