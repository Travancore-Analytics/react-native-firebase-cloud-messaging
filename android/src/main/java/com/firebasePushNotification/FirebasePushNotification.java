package com.firebasePushNotification;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;

import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import javax.annotation.Nonnull;

public class FirebasePushNotification extends ReactContextBaseJavaModule {
    public static final String REACT_CLASS = "FirebasePushNotification";

    public FirebasePushNotification(ReactApplicationContext context) {
        super(context);
    }

    @ReactMethod
    public void registerRemoteNotification(Promise promise){
        promise.resolve(null);
    }

    @ReactMethod
    public void subscribeToTopic(String topic, final Promise promise) {
        FirebaseMessaging
                .getInstance()
                .subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@Nonnull Task<Void> task) {
                        if (task.isSuccessful()) {
                            promise.resolve(null);
                        } else {
                            Exception exception = task.getException();
                            promise.reject(exception);
                        }
                    }
                });
    }

    @ReactMethod
    public void unsubscribeFromTopic(String topic, final Promise promise) {
        FirebaseMessaging
                .getInstance()
                .unsubscribeFromTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@Nonnull Task<Void> task) {
                        if (task.isSuccessful()) {
                            promise.resolve(null);
                        } else {
                            Exception exception = task.getException();
                            promise.reject(exception);
                        }
                    }
                });
    }

    @ReactMethod
    public void checkPermissionForPushNotification(Callback callback) {
        callback.invoke(null,NotificationManagerCompat.from(getCurrentActivity()).areNotificationsEnabled());
    }


    @ReactMethod
    public void getToken(final Callback callback) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                              callback.invoke(null,null);
                        } else {
                                callback.invoke(task.getResult());
                        }
                    }
                });
    }

    @Override
    public String getName() {
        return REACT_CLASS;
    }
}