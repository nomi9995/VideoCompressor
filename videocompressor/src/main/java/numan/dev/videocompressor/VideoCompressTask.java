package numan.dev.videocompressor;

import android.os.AsyncTask;

import numan.dev.videocompressor.videocompressprogresslistener.VideoCompressProgressListener;

public class VideoCompressTask extends AsyncTask<Object, Float, Boolean> {
    private String errorClass="";
    private String errorMessage="";
    private VideoCompressor.ProgressListener mListener;
    public VideoCompressTask(VideoCompressor.ProgressListener listener) {
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mListener != null) {
            mListener.onStart();
        }
    }

    @Override
    protected Boolean doInBackground(Object... paths) {
        try {
            return VideoController.getInstance().convertVideo((String) paths[0], (String) paths[1], (Integer) paths[2], (Integer) paths[3], (Integer) paths[4], new VideoCompressProgressListener() {
                @Override
                public void onProgress(float percent) {
                    publishProgress(percent);
                }
            });
        }catch (Throwable e) {
            errorClass=e.getClass().toString();
            return false;
        }
    }

    @Override
    protected void onProgressUpdate(Float... percent) {
        super.onProgressUpdate(percent);
        if (mListener != null) {
            mListener.onProgress(percent[0]);
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        if (mListener != null) {
            if (result) {
                mListener.onFinish(true);
            } else {
                mListener.onError(errorClass);
                errorClass="";
            }
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mListener.onError("");
    }
}
