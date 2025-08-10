package com.example.youtubevideosearchapp;

import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class YouTubeFetchTask extends AsyncTask<String, Void, List<VideoItem>> {

    private WeakReference<MainActivity> activityReference;

    public YouTubeFetchTask(MainActivity activity) {
        activityReference = new WeakReference<>(activity);
    }

    @Override
    protected void onPreExecute() {
        MainActivity activity = activityReference.get();
        if (activity != null) {
            activity.showLoading(true);
        }
    }

    @Override
    protected List<VideoItem> doInBackground(String... strings) {
        try {
            return YouTubeApiHelper.searchVideos(strings[0]);
        } catch (Exception e) {
            YouTubeApiHelper.lastException = e;
            return null; // تأكد من إعادة null لتدل على فشل
        }
    }
    @Override
    protected void onPostExecute(List<VideoItem> videoItems) {
        MainActivity activity = activityReference.get();
        if (activity == null) return;

        activity.showLoading(false);

        if (videoItems != null && !videoItems.isEmpty()) {
            activity.updateResults(videoItems);
        } else {
            if (YouTubeApiHelper.lastException != null) {
                // رسالة الخطأ فقط
                activity.showError("فشل الاتصال: " + YouTubeApiHelper.lastException.getMessage());
            } else {
                // لا تظهر رسالة "لا توجد نتائج" إلا لو لم يكن هناك خطأ
                Toast.makeText(activity, "لا توجد نتائج لهذا البحث.", Toast.LENGTH_SHORT).show();
                activity.updateResults(new ArrayList<>()); // عرض قائمة فارغة لتحديث RecyclerView
                //
            }
        }
    }

}