package joaomarccos.github.io.appwebservice.bcreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import joaomarccos.github.io.appwebservice.interfaces.Listenner;

/**
 * Created by joaomarcos on 18/03/16.
 */
public class BroadCast extends BroadcastReceiver {

    private Listenner listenner;

    public BroadCast(Listenner listenner) {
        this.listenner = listenner;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("BROADCASTRECEIVER", "NOVO ITEM DETECTADO");
        this.listenner.loadNewItem(intent.getBundleExtra("new_item"));
    }
}
