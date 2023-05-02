package network.schedule;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;

public class decompressImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    private decompressImageAsyncTask.ImageDeCompressionListener mListener;
    private Bitmap compressedBitmap;
    public decompressImageAsyncTask(decompressImageAsyncTask.ImageDeCompressionListener listener, Bitmap compressedByteArray) {
        mListener = listener;
        this.compressedBitmap = compressedByteArray;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected Bitmap doInBackground(String... params) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
            compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            byte[] byteArray = out.toByteArray();
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


    }

    @Override
    protected void onPostExecute(Bitmap compressedImage) {
        super.onPostExecute(compressedImage);
        if (mListener != null) {
            mListener.onImageCompressed(compressedImage);
        }
    }


    public interface ImageDeCompressionListener {
        void onImageCompressed(Bitmap compressedImage);
    }
}
