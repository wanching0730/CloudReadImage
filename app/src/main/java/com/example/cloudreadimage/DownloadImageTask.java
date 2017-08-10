package com.example.cloudreadimage;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by James Ooi on 9/8/2017.
 */

//run as separate thread (wont freeze the main activity)
public class DownloadImageTask extends AsyncTask<Void, Void, Bitmap> {
    private static final String IMAGE_URL = "https://labs.jamesooi.com/images/koala5.jpg";

    private Activity activity;

    public DownloadImageTask(Activity activity){
        this.activity = activity;
    }

    @Override
    protected Bitmap doInBackground(Void... v) {
        Bitmap bitmap = null;

        try {
            bitmap = downloadImage();
        }
        catch (IOException ex) {
            Log.e("IO_EXCEPTION", ex.toString());
        }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        ImageView imageView = (ImageView)(activity.findViewById(R.id.image_view));
        imageView.setImageBitmap(bitmap);
    }

    private Bitmap downloadImage() throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(IMAGE_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);

            // Starts the query
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode == 200 || responseCode == 304) {
                is = conn.getInputStream();

                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            }
            else {
                Log.e("HTTP_ERROR", Integer.toString(responseCode));
                return null;
            }
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }
}
