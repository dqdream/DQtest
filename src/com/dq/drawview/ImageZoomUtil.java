package com.dq.drawview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;

/**
 * 图片缩放类
 * 2016.11.25
 * @author DQ
 */
public class ImageZoomUtil {
	public static final String TAG="dq";
	/**
	 * 获取资源缩放图片
	 * 动态缩放，避免OOM
	 * @param context
	 * @param imgId 资源ID
	 * @param minSideLength 最小宽或者高的大小，-1为不设置
	 * @param maxNumOfPixels 最大占用内存 格式 w*h
	 * @return
	 */
    public static Bitmap getResourceZoomBitmap(Context context, int imgId, int minSideLength, int maxNumOfPixels) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imgId, newOpts);
        newOpts.inSampleSize = computeSampleSize(newOpts, minSideLength,
        		maxNumOfPixels);
        newOpts.inJustDecodeBounds = false;
        Bitmap bitmap=  BitmapFactory.decodeResource(context.getResources(), imgId, newOpts);
        return bitmap;

    }
    /**
     * 获取文件缩放图片
     * 动态缩放，避免OOM
     * @param context
     * @param imgId
     * @param minSideLength
     * @param maxNumOfPixels
     * @return
     */
    public static Bitmap getFileZoomBitmap(String path, int minSideLength, int maxNumOfPixels) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, newOpts);
        newOpts.inSampleSize = computeSampleSize(newOpts, minSideLength,
        		maxNumOfPixels);
        newOpts.inJustDecodeBounds = false;
        Bitmap bitmap=  BitmapFactory.decodeFile(path, newOpts);
        return bitmap;

    }
    /**
     * 动态计算采样率/缩放比
     * @param options
     * @param minSideLength 最小宽或者高的大小，-1为不设置
     * @param maxNumOfPixels 最大占用内存 格式 w*h
     * @return
     */
    private static int computeSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8 ) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
            int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 :
                (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 :
                (int) Math.min(Math.floor(w / minSideLength),
                Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) &&
                (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * Bitmap精确压缩
     * @param bm
     * @param reqWidth 缩放宽度
     * @param reqHeight 缩放高度
     * @return
     */
    public static Bitmap compress(final Bitmap bm, int reqWidth, int reqHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        if (height > reqHeight || width > reqWidth) {
            float scaleWidth = (float) reqWidth / width;
            float scaleHeight = (float) reqHeight / height;
            float scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;

            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap result = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
                    bm.getHeight(), matrix, true);
            Log.d(TAG, "compress2 width = "+result.getWidth()+", height = "+result.getHeight()+", zise = "+ result.getWidth()*result.getHeight());
            bm.recycle();
            return result;
        }
        return bm;
    }
}
