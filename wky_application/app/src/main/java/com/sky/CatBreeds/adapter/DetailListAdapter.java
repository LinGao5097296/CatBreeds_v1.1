package com.sky.CatBreeds.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sky.CatBreeds.R;
import com.sky.CatBreeds.database.Note;
import com.sky.CatBreeds.utils.LogUtils;

import java.io.File;
import java.util.List;


/**
 * Created by Thinkpad on 2018/7/28.
 */

public class DetailListAdapter extends BaseAdapter {

        private List<Note> list;
        private ListView listview;
        private LruCache<String, BitmapDrawable> mImageCache;

        public DetailListAdapter(List<Note> list) {
            super();
            this.list = list;


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
                        R.layout.detail_list_item, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.item_iv_cover);
                holder.item_tv_id = (TextView) convertView.findViewById(R.id.item_tv_id);
                holder.item_tv_bid = (TextView) convertView.findViewById(R.id.item_tv_bid);
                holder.item_tv_title = (TextView) convertView.findViewById(R.id.item_tv_title);
                holder.item_tv_type = (TextView) convertView.findViewById(R.id.item_tv_type);
                holder.item_tv_desc = (TextView) convertView.findViewById(R.id.item_tv_desc);
                holder.item_tv_price = (TextView) convertView.findViewById(R.id.item_tv_price);
                holder.item_tv_date = (TextView) convertView.findViewById(R.id.item_tv_date);
                holder.item_tv_status = (TextView) convertView.findViewById(R.id.item_tv_status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final Note meal = list.get(position);
            holder.item_tv_id.setText(meal.getId()+"");
            holder.item_tv_bid.setText(meal.getBid());
            holder.item_tv_title.setText(meal.getTitle());
            holder.item_tv_type .setText(meal.getContent());
            Log.v(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception())+meal.getTitle());
            holder.item_tv_price.setText(meal.getTime());
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
            File imageFile = new File(fileDir, meal.getTitle() + meal.getBid() + ".PNG");
            String filePath = Environment.getExternalStorageDirectory().getPath() + "/" + meal.getTitle() + meal.getBid() + ".PNG";
            Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()) + filePath);
            final File file = new File(filePath);
            // 如果本地已有缓存，就从本地读取，否则从网络请求数据

            if (file.exists()) {
                Log.e(LogUtils.filename(new Exception()), LogUtils.funAndLine(new Exception()) + "本地已有缓存");
                // holder.iv.setImageDrawable( mImageCache.get( player.getIcon().getFileUrl() ) );
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
                holder.iv.setImageBitmap(bitmap);


            }
            return convertView;
        }

    //从List移除对象
    public void removeItem(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }
    class ViewHolder {
            ImageView iv;
            TextView item_tv_id,item_tv_bid,item_tv_title,item_tv_type,item_tv_desc,item_tv_cover,item_tv_status,item_tv_price,item_tv_date;
        }


}
