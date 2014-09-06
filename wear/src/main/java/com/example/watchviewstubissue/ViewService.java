package com.example.watchviewstubissue;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.IBinder;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by nicolas on 04/08/14.
 */
public class ViewService extends Service {
    private WindowManager windowManager;
    private View mainView;
    private String formFactor;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {

        Log.d("ViewService", "Starting service");

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mainView = layoutInflater.inflate(R.layout.activity_main, null);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN, PixelFormat.TRANSPARENT);

//        params.gravity = Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
        windowManager.addView(mainView, params);

        final WatchViewStub stub = (WatchViewStub) mainView.findViewById(R.id.watch_view_stub);

        mainView.setOnApplyWindowInsetsListener(new View.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsets onApplyWindowInsets(View v, WindowInsets insets) {
                if (insets.isRound()) {
                    Log.d("ViewService", "Round");
                } else {
                    Log.d("ViewService", "Square");
                }
                return insets;
            }
        });

        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub watchViewStub) {
                TextView text = (TextView) watchViewStub.findViewById(R.id.color_square);
                Point point = new Point();
                windowManager.getDefaultDisplay().getSize(point);
                text.setText(point.x+"x"+point.y);
            }
        });

        stub.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

    }

    @Override
    public void onDestroy() {
        windowManager.removeView(mainView);
        super.onDestroy();
    }
}
