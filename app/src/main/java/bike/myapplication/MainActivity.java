package bike.myapplication;

        import android.Manifest;
        import android.content.Context;
        import android.content.pm.PackageManager;
        import android.location.Location;
        import android.location.LocationListener;
        import android.location.LocationManager;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.LinearLayout;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.maps.CameraUpdateFactory;
        import com.google.android.gms.maps.GoogleMap;
        import com.google.android.gms.maps.OnMapReadyCallback;
        import com.google.android.gms.maps.SupportMapFragment;
        import com.google.android.gms.maps.model.LatLng;
        import com.google.android.gms.maps.model.MarkerOptions;
        import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Location mLocation;
    private ArrayList<MarkerOptions> locateAcc;
    private LatLng startLatLng;
    private TextView tv_v;

    boolean start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("SABA");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.
                PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        LinearLayout ll_mapView = (LinearLayout) findViewById(R.id.mapView);
        tv_v = (TextView) findViewById(R.id.tv_v);

        mLocation = new Location("service provider");
        locateAcc = new ArrayList<MarkerOptions>();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(findViewById(R.id.map).getId());
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMinZoomPreference(12.0f); //줌 축소 최댓값
        mMap.setMaxZoomPreference(17.0f); //줌 확대 최댓값
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        GPSListener gpsListener = new GPSListener();
        long minTime = 200; //2sec
        float minDistance = 1; //m에 따라서 계속 바뀌게 된다.

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, gpsListener);
        } catch(SecurityException ex) {
            Toast.makeText(getApplicationContext(), "예외처리발생", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
    }

    private class GPSListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            mLocation.setLatitude(location.getLatitude());
            mLocation.setLongitude(location.getLongitude());

            if (!start) { //초기화 false....로 설정
                startLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                start = true;
            }

            try {
                LatLng myLocate = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocate));
                mMap.setMyLocationEnabled(true);

                tv_v.setText("자전거 속도 : " + String.format("%.2f", 36. * location.getSpeed() / 10.) + "km/h");
                // gps를 통한 위치가 변할 때마다 센서에서 가져오는 현재 속도 -라이브러리- 구글에서 제공하는 gps에 따른 api----
                //시속 ?.??킬로미터 즉, 현재 속도라고 해서 한시간에 ?.??킬로미터로 간다고 계산해주는 것이다. km/h
            }
            catch(SecurityException ex) {
                Toast.makeText(getApplicationContext(), "권한설정을 다시해주세요", Toast.LENGTH_SHORT).show();
                finish();
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
