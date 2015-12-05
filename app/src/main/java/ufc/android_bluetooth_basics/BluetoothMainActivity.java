package ufc.android_bluetooth_basics;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.reflect.Method;

public class BluetoothMainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BLUETOOTH = 1;
    private final String TAG = "BLUETOOTH_BASICS";
    private final int DISCOVERABLE_TIMEOUT = 0;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            snakeBar(R.string.not_support_bluetooth);
            Log.d(TAG, "Device does not support Bluetooth");
        } else {
            if (!bluetoothAdapter.isEnabled()) {
                Intent enableBluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH);
            } else {
                setDiscoverable();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK) {
                    setDiscoverable();
                } else {
                    snakeBar(R.string.request_enable_bluetooth_error);
                    Log.d(TAG, "Bluetooth not enabled");
                    this.finish();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        if (bluetoothAdapter.isEnabled()) {
            bluetoothAdapter.disable();
            Log.d(TAG, "Disable bluetooth");
        }
        super.onDestroy();
    }

    private void setDiscoverable() {
        Log.d(TAG, "Bluetooth enabled");
        try {
            Method setScanModeMethod = BluetoothAdapter.class.getDeclaredMethod("setScanMode", int.class, int.class);
            setScanModeMethod.invoke(bluetoothAdapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, DISCOVERABLE_TIMEOUT);
        } catch (Exception e) {
            Log.d(TAG, "Error: this device is not discoverable");
            e.printStackTrace();
        }
        snakeBar(R.string.set_discoverable);
        Log.d(TAG, "This device is discoverable");
    }

    private void snakeBar(int resId) {
        Snackbar.make(findViewById(android.R.id.content), resId, Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }
}
