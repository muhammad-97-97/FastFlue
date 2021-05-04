package com.example.fastfuel;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Payment extends AppCompatActivity {
    Button mButton;

    private RequestQueue mRequestQue;
    private final String URL = "https://fcm.googleapis.com/fcm/send";

    private static final String CHANNEL_ID = "simplified_coding";
    private static final String CHANNEL_NAME = "Simplified Coding";
    private static final String CHANNEL_DESC = "Simplified Coding Notification";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mRequestQue = Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");

       if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }




        mButton = findViewById(R.id.order_btn);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String message = "This is a notification example.";
                NotificationCompat.Builder builder = new NotificationCompat.Builder(Payment.this
                )
                        .setSmallIcon(R.drawable.ic_message)
                        .setContentTitle("New Notification")
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                Intent intent = new Intent(Payment.this, Delivery.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("message",message);

                PendingIntent pendingIntent = PendingIntent.getActivity(Payment.this,0,
                        intent,PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager)getSystemService(
                        Context.NOTIFICATION_SERVICE
                );
                notificationManager.notify(0, builder.build());


                 */


                displayNotification();
                sendNotification();
                openDialog();
            }
        });
    }




    private void sendNotification(){
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to","/topics/" + "news");
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title","Example of Notification");
            notificationObj.put("body", "It's working...");
            mainObj.put("notification",notificationObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,
                    mainObj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                        }
                    }
                    ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization", "key=AAAAZM3heNA:APA91bGh_ViBGc8vlK6I2Ow_Z7kmucVKCTDW-jZn6PtF0-WBFRbgNoUwDjfPTP6NngHi_OFhtkLWLUbZW0RDIOejoPtIgZJLIDZCfjcveX89_rkCciMJNjZ6dUo95v9Ul1pzwdPDPFpm");
                    return header;
                }
            };

            mRequestQue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






    private void displayNotification(){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_message)
                    .setContentTitle("It's working...")
                    .setContentText("Example of Notification")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat mNotificationMgr = NotificationManagerCompat.from(this);
        mNotificationMgr.notify(1,mBuilder.build());
    }




    public void openDialog(){
        DialogPayment dialogPayment = new DialogPayment();
        dialogPayment.show(getSupportFragmentManager(),"dialog payment");
    }
}