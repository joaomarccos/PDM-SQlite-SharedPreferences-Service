package joaomarccos.github.io.appwebservice;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import joaomarccos.github.io.appwebservice.bcreceivers.BroadCast;
import joaomarccos.github.io.appwebservice.interfaces.Listenner;
import joaomarccos.github.io.appwebservice.services.DataSyncronizer;
import joaomarccos.github.io.appwebservice.util.DataStorer;

public class MainActivity extends Activity implements Listenner {

    private ListView list;
    private List<String> nomes;
    private BroadCast bc;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DataStorer(this).reset();
        this.nomes = new ArrayList<>();
        list = (ListView) findViewById(R.id.list);
        this.adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nomes);
        list.setAdapter(adapter);
        bdReceiverRegister();
        dataSyncStart();
    }

    private void dataSyncStart() {
        Intent intent = new Intent(this, DataSyncronizer.class);
        startService(intent);
    }

    private void bdReceiverRegister() {
        bc = new BroadCast(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction("NEW_ITEM");
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(bc, filter);
    }

    @Override
    public void loadNewItem(Bundle bundle) {
        String item = bundle.getString("item");
        this.nomes.add(item);
        adapter.notifyDataSetChanged();
        Log.d("BROADCASTRECEIVER", item);
    }

    @Override
    protected void onPause() {
        unregisterReceiver(bc);
        super.onPause();
    }
}
