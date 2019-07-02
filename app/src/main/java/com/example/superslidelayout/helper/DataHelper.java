package com.example.superslidelayout.helper;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.superslidelayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author gexinyu
 * 可以控制数据的
 */
public class DataHelper {

    public static int PAGE_SIZE = 10;//每页条数
    public static int PAGE_NUM = 2;//页数


    /**
     * 获取一列数据
     *
     * @param context
     * @return
     */
    public static List<Bitmap> getSingleLineBitmap(Context context) {
        return getSingleLineBitmap(context, PAGE_SIZE);
    }

    /**
     * 获取一列数据
     *
     * @param context
     * @return
     */
    public static List<Bitmap> getSingleLineBitmap(Context context, int size) {
        List<Bitmap> list = new ArrayList();
        List<Bitmap> bitmaps = getBitmaps(context);
        for (int j = 0; j < size; j++) {
            Bitmap bitmap = bitmaps.get(j);
            list.add(bitmap);
        }
        return list;
    }

    /**
     * 获取多列数据
     *
     * @param context
     * @return
     */
    public static List<ListBean> getBitmapList(Context context) {
        List<ListBean> list = new ArrayList();
        List<Bitmap> bitmaps = getBitmaps(context);

        for (int i = 0; i < PAGE_NUM; i++) {
            for (int j = 0; j < PAGE_SIZE; j++) {
                Bitmap bitmap = bitmaps.get(j);
                list.add(new ListBean(bitmap));
            }
        }
        return list;
    }

    /**
     * 注入数据
     *
     * @return
     */
    public static Bitmap getBitmapIndex(Context context, int index) {
        List<Bitmap> bitmaps = getBitmaps(context);
        return bitmaps.get(index);
    }


    /**
     * 获取bitmap的集合
     */
    private static List<Bitmap> getBitmaps(Context context) {
        List<Bitmap> list = new ArrayList();
        Resources resources = context.getResources();
        list.add(BitmapFactory.decodeResource(resources, R.mipmap.image2));
        list.add(BitmapFactory.decodeResource(resources, R.mipmap.image3));
        list.add(BitmapFactory.decodeResource(resources, R.mipmap.image4));
        list.add(BitmapFactory.decodeResource(resources, R.mipmap.image5));
        list.add(BitmapFactory.decodeResource(resources, R.mipmap.image6));
        list.add(BitmapFactory.decodeResource(resources, R.mipmap.image7));
        list.add(BitmapFactory.decodeResource(resources, R.mipmap.image8));
        list.add(BitmapFactory.decodeResource(resources, R.mipmap.image9));
        list.add(BitmapFactory.decodeResource(resources, R.mipmap.image10));
        list.add(BitmapFactory.decodeResource(resources, R.mipmap.image1));
        return list;
    }

}
