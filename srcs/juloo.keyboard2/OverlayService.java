package juloo.keyboard2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class OverlayService extends Service {

    private static final int NOTIFICATION_ID = 1600;
    private static final String NOTIFICATION_CHANNEL_ID = "UnexpectedKeyboardOverlay";
    private WindowManager windowManager;
    private View overlayView;

    public OverlayService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();


        // Create a notification to make the service a foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            createNotificationChannel();

            Notification.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID);
            } else {
                builder = new Notification.Builder(this);
            }
            Notification notification = builder.setContentTitle("Overlay Service")
                    .setContentText("Running")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            startForeground(NOTIFICATION_ID, notification);
        }

        // Create an overlay button
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_button, null);

        // Set up the layout parameters for the overlay button
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                        ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                        : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 10;
        params.y = 110;

        // Add the overlay button to the window
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(overlayView, params);

        // Set up click listener for the overlay button
//        Button button = overlayView.findViewById(R.id.invokeKeyboardButton);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                EditText editText = new EditText(getApplicationContext());
////                editText.setVisibility(View.GONE);
////
////                // Add the EditText to the window to allow focus
////                windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
////                windowManager.addView(editText, new WindowManager.LayoutParams());
//
//                Toast.makeText(OverlayService.this, "Button Clicked!", Toast.LENGTH_SHORT).show();
////
////                             // Show the soft keyboard
////                EditText yourEditText= (EditText) overlayView.findViewById(R.id.dummyEditText);
//////                yourEditText.requestFocus();
//////                yourEditText.setText("hello");
//                InputMethodManager inputMethodManager =
//                        (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//
//
//                // Remove the EditText from the window after use
////                windowManager.removeView(editText);
//            }
//        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Overlay Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (overlayView != null) {
            windowManager.removeView(overlayView);
        }
    }
}