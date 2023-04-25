package com.open_activity.open_activity.withturnbyturn.java.com.example.navigationapidemo;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.Process;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.google.android.libraries.mapsplatform.turnbyturn.TurnByTurnManager;
import com.google.android.libraries.mapsplatform.turnbyturn.model.NavInfo;

/*
 * NOTE: TO USE NAV INFO FORWARDING, PLEASE INCLUDE THE TURNBYTURN API JAR FILE IN THE libs/
 * DIRECTORY OF THIS APP, IN ADDITION TO THE GOOGLE NAVIGATION API AAR FILE.
 */

/**
 * Receives turn-by-turn navigation information forwarded from NavSDK and posts each update to live
 * data, which is then displayed on a separate header in {@code NavInfoDisplayFragment}. This
 * service may be part of a different process aside from the main process, depending on how you
 * want to structure your app. The service binding will be able to handle interprocess
 * communication to receive nav info messages from the main process.
 */
public class NavInfoReceivingService extends Service {
    /** The messenger used by the service to receive nav step updates. */
    private Messenger mIncomingMessenger;

    /** Used to read incoming messages. */
    private TurnByTurnManager mTurnByTurnManager;

    private static final MutableLiveData<NavInfo> mNavInfoMutableLiveData = new MutableLiveData<>();

    private final class IncomingNavStepHandler extends Handler {
        public IncomingNavStepHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (TurnByTurnManager.MSG_NAV_INFO == msg.what) {
                // Read the nav info from the message data.
                NavInfo navInfo = mTurnByTurnManager.readNavInfoFromBundle(msg.getData());
                // Post the value to LiveData to be displayed in the nav info header.
                mNavInfoMutableLiveData.postValue(navInfo);
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIncomingMessenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mNavInfoMutableLiveData.postValue(null);
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        mTurnByTurnManager = TurnByTurnManager.createInstance();
        HandlerThread thread =
                new HandlerThread(
                        "NavInfoReceivingService",
                        Process.THREAD_PRIORITY_DEFAULT);
        thread.start();
        mIncomingMessenger = new Messenger(new IncomingNavStepHandler(thread.getLooper()));
    }

    public static LiveData<NavInfo> getNavInfoLiveData() {
        return mNavInfoMutableLiveData;
    }
}
