package joaomarccos.github.io.appwebservice.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.security.NoSuchAlgorithmException;

import joaomarccos.github.io.appwebservice.RequestManager;
import joaomarccos.github.io.appwebservice.util.DataStorer;

/**
 * Created by joaomarcos on 26/02/16.
 */
public class DataSyncronizer extends Service {

    private boolean isActive;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isActive) {
            new Worker(new DataStorer(getApplicationContext())).start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        isActive = false;
        super.onDestroy();
    }

    private class Worker extends Thread {
        private RequestManager rm;

        public Worker(DataStorer ds) {
            this.rm = new RequestManager(ds);
        }

        @Override
        public void run() {
            isActive = true;
            String data;
            while (isActive) {
                try {
                    if (!rm.isSync()) {
                        while ((data = rm.getNextValue()) != null) {
                            Intent it = new Intent("NEW_ITEM");
                            Bundle bundle = new Bundle();
                            bundle.putString("item", data);
                            it.putExtra("new_item", bundle);
                            sendBroadcast(it);
                            Log.d("BROADCASTRECEIVER", "NOVO ITEM ENVIADO");
                        }
                        Thread.sleep(5000);
                    }
                } catch (NoSuchAlgorithmException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}