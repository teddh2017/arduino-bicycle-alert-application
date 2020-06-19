package bike.myapplication;
// 안드로이드와 아두이노 블루투스를 연결하기 위한 기기 선택 페이지

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class DeviceListActivity extends Activity {
    public static final String TAG = DeviceListActivity.class.getSimpleName();
    public static String EXTRA_DEVICE_ADDRESS = "device_address";

    private BluetoothAdapter mBTAdapter;
    //"BluetoothAdapter.getDefaultAdapter()"는 블루투스 모듈이 없으면 null을 리턴한다.
    // 폰의 블루투스 모듈을 사용하기 위한 오브젝트.

    private Button btn_scan;
    private LinearLayout mBackground;
    private ListView mListDevices;
    private ListAdapter mNewDeviceAdapter;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                mNewDeviceAdapter.addItem(device.getName(), device.getAddress());
                // 처음에 기기 검색 버튼 터치후 기기 검출

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarVisibility(false);
                if (mNewDeviceAdapter.getCount() == 0) {
                    mNewDeviceAdapter.addItem("블루투스 기기가 없습니다");
                }
            }
            mListDevices.setAdapter(mNewDeviceAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_list);
        android.view.WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.gravity = Gravity.TOP;
        layoutParams.y = 200;
        setTitle("SABA");

        mBTAdapter = BluetoothAdapter.getDefaultAdapter();
        //"BluetoothAdapter.getDefaultAdapter()"는 블루투스 기능이 꺼져 있으면 null을 리턴한다.
        if (mBTAdapter == null) {
            Toast.makeText(getApplicationContext(), "블루투스가 지원되지 않습니다", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mBackground = (LinearLayout) findViewById(R.id.ll_background);
        mBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBTAdapter != null) {
                    mBTAdapter.cancelDiscovery();
                }
                finish();
            }
        });

        mNewDeviceAdapter = new ListAdapter();
        mListDevices = (ListView) findViewById(R.id.lv_devices);
        mListDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (((TextView) view.findViewById(R.id.name)).getText().toString().equals("블루투스 기기가 없습니다")) {
                    return;
                }// 블루투스 ... 없습니다. 이게 터치가 안되게 하기 위해서......
                mBTAdapter.cancelDiscovery();
                // 기기 스캔을 멈추게 하고

                String address = ((TextView) view.findViewById(R.id.address)).getText().toString();
                // 주소를 가져와서 인덴트를 가져와서 다시 메인 페이지로

                Intent intent = new Intent();
                intent.putExtra(EXTRA_DEVICE_ADDRESS, address);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        btn_scan = (Button) findViewById(R.id.btn_scan);
        btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doDiscovery();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mBTAdapter != null) {
            mBTAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }

    private void doDiscovery() {
        setProgressBarIndeterminateVisibility(true);
        setTitle("디바이스 스캔 중");
        if (mBTAdapter.isDiscovering()) {
            mBTAdapter.cancelDiscovery();
            btn_scan.setText("START SCANNING");

        } else {
            mNewDeviceAdapter.Clear();
            mBTAdapter.startDiscovery();
            btn_scan.setText("CANCEL SCANNING");
        }
    }

    private class ListData { // 리스트 구조 자체 ------ 목록 ------
        private String mName;
        private String mAddress;

        public void setName(String name) {
            mName = name;
        }
        public void setAddress(String address) {
            mAddress = address;
        }
        public String getName() {
            return mName;
        }
        public String getAddress() {
            return mAddress;
        }
    }

    private class ListAdapter extends BaseAdapter {
        private ArrayList<ListData> mListData = new ArrayList<ListData>();

        @Override
        public int getCount() {
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //inflate 를 사용하기 위해서는 우선 inflater 를 얻어오고 있습니다.
                //안드로이드에서 inflate 를 사용하면 xml 에 씌여져 있는 view 의 정의를 실제 view 객체로 만드는 역할을 합니다.

                convertView = inflater.inflate(R.layout.device_elements, parent, false);
            }

            TextView tv_name = (TextView) convertView.findViewById(R.id.name);
            TextView tv_address = (TextView) convertView.findViewById(R.id.address);

            ListData listData = mListData.get(position);

            tv_name.setText(listData.getName());
            tv_address.setText(listData.getAddress());

            return convertView;
        }

        public void addItem(String none) {
            ListData data = new ListData();
            data.setName(none);

            mListData.add(data);
        }
        public void addItem(String name, String address) {
            ListData data = new ListData();

            data.setName(name);
            data.setAddress(address);

            mListData.add(data);
        }

        public void Clear() {
            mListData.clear();
        }
    }
}