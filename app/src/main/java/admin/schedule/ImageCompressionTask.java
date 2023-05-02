package admin.schedule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;

public class ImageCompressionTask extends AsyncTask<String, Void, byte[]> {

    private ImageCompressionListener mListener;
    private String path;
    public ImageCompressionTask(ImageCompressionListener listener, String path) {
        mListener = listener;
        this.path = path;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected byte[] doInBackground(String... params) {

        Bitmap bitmap = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return byteArray;
    }

    @Override
    protected void onPostExecute(byte[] compressedImage) {
        super.onPostExecute(compressedImage);
        if (mListener != null) {
            mListener.onImageCompressed(compressedImage);
        }
    }


    public interface ImageCompressionListener {
        void onImageCompressed(byte[] compressedImage);
    }
}
