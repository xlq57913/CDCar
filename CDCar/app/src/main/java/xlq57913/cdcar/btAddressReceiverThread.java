package xlq57913.cdcar;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.inuker.bluetooth.library.beacon.Beacon;
import com.inuker.bluetooth.library.connect.response.BleReadResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.model.BleGattCharacter;
import com.inuker.bluetooth.library.model.BleGattService;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.utils.BluetoothLog;
import com.inuker.bluetooth.library.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static android.content.ContentValues.TAG;
import static com.inuker.bluetooth.library.Constants.BLE_NOT_SUPPORTED;
import static com.inuker.bluetooth.library.Constants.REQUEST_SUCCESS;
import static com.inuker.bluetooth.library.Constants.STATUS_DEVICE_CONNECTED;
import static com.inuker.bluetooth.library.utils.BluetoothUtils.sendBroadcast;


public class btAddressReceiverThread extends Thread {
    private Handler mHandle;
    private BluetoothClient mClient;
    private Context mContext;

    private BleGattProfile mProfile;

    public btAddressReceiverThread(Handler handle,Context context){
        mHandle = handle;
        mContext = context;
        mClient = new BluetoothClient(mContext);
        if(!mClient.isBluetoothOpened()){
            mClient.openBluetooth();
        }
    }
    public void searchPi() {
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(3000, 3)   // 先扫BLE设备3次，每次3s
                .searchBluetoothClassicDevice(5000) // 再扫经典蓝牙5s
                .searchBluetoothLeDevice(2000)      // 再扫BLE设备2s
                .build();

        mClient.search(request, new SearchResponse() {
            @Override
            public void onSearchStarted() {

            }

            @Override
            public void onDeviceFounded(SearchResult device) {
                Beacon beacon = new Beacon(device.scanRecord);
                BluetoothLog.v(String.format("beacon for %s\n%s", device.getAddress(), beacon.toString()));
                if(device.getAddress().equals("B8:27:EB:DF:67:14")){
                    onSearchStopped();
                    int status = mClient.getConnectStatus(device.getAddress());
                    if (status == STATUS_DEVICE_CONNECTED){
                        BluetoothLog.v("raspberry is connected, sending msg...");
                        mClient.write("B8:27:EB:DF:67:14", mProfile.getServices().get(0).getUUID(), mProfile.getServices().get(0).getCharacters().get(0).getUuid(), "1".getBytes(), new BleWriteResponse() {

                            @Override

                            public void onResponse(int code) {

                                if (code == REQUEST_SUCCESS) {
                                    mClient.read("B8:27:EB:DF:67:14", mProfile.getServices().get(0).getUUID(), mProfile.getServices().get(0).getCharacters().get(0).getUuid(), new BleReadResponse() {
                                        @Override
                                        public void onResponse(int code, byte[] data) {
                                            if (code == REQUEST_SUCCESS) {
                                                BluetoothLog.v("Data received: " + data.toString());
                                            }
                                        }
                                    });
                                }

                            }

                        });
                    }
                    BluetoothLog.v("Find raspberryPi, trying to connect...... ");
                    try {
                        connectToRasp(device.getAddress());
                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onSearchStopped() {

            }

            @Override
            public void onSearchCanceled() {

            }
        });
    }

    @Override
    public void run(){
        super.run();
        searchPi();
    }


    private void connectToRasp(final String macAddress){
        mClient.connect(macAddress, new BleConnectResponse() {
            @Override
            public void onResponse(int code, BleGattProfile profile) { 
                mProfile = profile;
                BluetoothLog.v("onResponse: "+code);
                if (code == REQUEST_SUCCESS) {
                    Log.i(TAG, "onResponse: request_success!");
                    mClient.write("B8:27:EB:DF:67:14", mProfile.getServices().get(0).getUUID(), mProfile.getServices().get(0).getCharacters().get(0).getUuid(), "1".getBytes(), new BleWriteResponse() {

                        @Override

                        public void onResponse(int code) {

                            if (code == REQUEST_SUCCESS) {
                                mClient.read("B8:27:EB:DF:67:14", mProfile.getServices().get(0).getUUID(), mProfile.getServices().get(0).getCharacters().get(0).getUuid(), new BleReadResponse() {
                                    @Override
                                    public void onResponse(int code, byte[] data) {
                                        if (code == REQUEST_SUCCESS) {
                                            BluetoothLog.v("Data received: " + data.toString());
                                        }
                                    }
                                });
                            }
                        }

                    });
                }
            }
        });
    }
}
