package com.synapse.gofer.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoaderNoProgress {
    //the simplest in-memory cache implementation. This should be replaced with something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
    private HashMap<String, Bitmap> mCache = new HashMap<String, Bitmap>();
    private File mCacheDir;
    private Context mContext;
    private PhotosQueue mPhotosQueue = new PhotosQueue();
    private PhotosLoader mPhotoLoaderThread = new PhotosLoader();
    
    public static ImageLoaderNoProgress imageLoaderNoProgress = null;
    public static ImageLoaderNoProgress getInstance(Context context)
    {
    	if(imageLoaderNoProgress == null)
    		imageLoaderNoProgress = new ImageLoaderNoProgress(context);
    	
    	return imageLoaderNoProgress;
    }
    
    private ImageLoaderNoProgress(Context context) {
        //Make the background thread low priority. This way it will not affect the UI performance
        mContext = context;
        mPhotoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

        //Find the Dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            mCacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "LazyList");
        }
        else {
            mCacheDir = context.getCacheDir();
        }

        if (!mCacheDir.exists()) {
            mCacheDir.mkdirs();
        }
    }

    public void displayImage(String url, ImageView imageView, int resId) {
        if (url == null || url.length() < 1) {
            if (resId > 0) {
                imageView.setImageResource(resId);
            }
            else {
                imageView.setImageBitmap(null);
            }
        }

        imageView.setTag(url);
        if (mCache.containsKey(url)) {
            imageView.setImageBitmap(mCache.get(url));
        } else {
            queuePhoto(url, imageView, resId);
            if (resId > 0) {
                imageView.setImageResource(resId);
            }
        }
    }

    public void displayImage(String url, ImageView imageView) {
        displayImage(url, imageView, 0);
    }

    public void stopThread() {
        mPhotoLoaderThread.interrupt();
    }

    public void clearCache() {
        //clear memory cache
        mCache.clear();

        //clear SD cache
        File[] files = mCacheDir.listFiles();
        for (File f : files) {
            f.delete();
        }
    }

    private void queuePhoto(String url, ImageView imageView, int resourceId) {
        //This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them. 
        mPhotosQueue.clean(imageView);
        PhotoToLoad p = new PhotoToLoad(url, imageView, resourceId);
        synchronized (mPhotosQueue.photosToLoad) {
            mPhotosQueue.photosToLoad.push(p);
            mPhotosQueue.photosToLoad.notifyAll();
        }

        //start thread if it's not started yet
        if (mPhotoLoaderThread.getState() == Thread.State.NEW) {
            mPhotoLoaderThread.start();
        }
    }

    //stores list of photos to download
    private class PhotosQueue {
        private Stack<PhotoToLoad> photosToLoad = new Stack<PhotoToLoad>();

        //removes all instances of this ImageView
        public void clean(ImageView view) {
            for (int i = 0; i < photosToLoad.size(); ) {
                if (photosToLoad.get(i).imageView == view) {
                    photosToLoad.remove(i);
                }
                else {
                    ++i;
                }
            }
        }
    }

    private Bitmap getBitmap(String url) {
        Bitmap bitmap = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            bitmap = BitmapFactory.decodeStream(bis, null, options);
            bis.close();
            is.close();
        } catch (Throwable e) {
        	if(e instanceof OutOfMemoryError)
        		mCache.clear();
            Log.e("Hub", "Error getting the image from server : " + e.getMessage().toString());
        }
        return bitmap;
    }

    //Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;
        public int resourceId;

        public PhotoToLoad(String u, ImageView i, int r) {
            url = u;
            imageView = i;
            resourceId = r;
        }
    }

    private class PhotosLoader extends Thread {
        public void run() {
            try {
                while (true) {
                    //thread waits until there are any images to load in the queue
                    if (mPhotosQueue.photosToLoad.size() == 0) {
                        synchronized (mPhotosQueue.photosToLoad) {
                            mPhotosQueue.photosToLoad.wait();
                        }
                        continue;
                    }

                    PhotoToLoad photoToLoad;
                    synchronized (mPhotosQueue.photosToLoad) {
                        photoToLoad = mPhotosQueue.photosToLoad.pop();
                    }

                    ImageView imageView = photoToLoad.imageView;
                    Bitmap bitmap = getBitmap(photoToLoad.url);

                    mCache.put(photoToLoad.url, bitmap);
                    if (imageView.getTag() != null && imageView.getTag().equals(photoToLoad.url)) {
                        BitmapDisplayer bd = new BitmapDisplayer(bitmap, imageView);
                        Activity a = (Activity) imageView.getContext();
                        a.runOnUiThread(bd);
                    }

                    if (Thread.interrupted())
                        break;
                }
            } catch (InterruptedException e) {
                //allow thread to exit
                e.printStackTrace();
            }
        }
    }

    //Used to display bitmap in the UI thread
    private class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        ImageView imageView;

        public BitmapDisplayer(Bitmap b, ImageView i) {
            bitmap = b;
            imageView = i;
        }

        public void run() {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }
}
