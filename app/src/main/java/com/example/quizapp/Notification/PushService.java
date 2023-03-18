//package com.example.quizapp.Notification;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//
//import androidx.annotation.NonNull;
//
//import com.google.firebase.messaging.FirebaseMessagingService;
//import com.google.firebase.messaging.RemoteMessage;
//
//public class PushService extends FirebaseMessagingService {
//
//    private BroadcastReceiver pushBroadcastReceiver;
//
//    @Override
//    public void onNewToken(@NonNull String token) {
//        super.onNewToken(token);
//
//        pushBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//            }
//        };
//
//        IntentFilter filter = new IntentFilter();
//
//        //registerReceiver(pushBroadcastReceiver);
//    }
//
//    @Override
//    public void onDestroy() {
//        unregisterReceiver(pushBroadcastReceiver);
//        super.onDestroy();
//    }
//
//    @Override
//    public void onMessageReceived(@NonNull RemoteMessage message) {
//        super.onMessageReceived(message);
//    }
//}
