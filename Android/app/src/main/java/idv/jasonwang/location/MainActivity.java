package idv.jasonwang.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

/**
 * 需要在 AndroidManifest.xml 加入以下權限
 * 1. <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
 * 2. <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
 */
public class MainActivity extends AppCompatActivity {

    private static final String ACCESS_COARSE_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final String ACCESS_FIND_LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;

    private static final int LOCATION_REQUEST_CODE = 0xF5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLocation();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Log.d("TAG", "需要權限才能定位");
            }
        }
    }


    private void getLocation() {
        // 檢查權限
        if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_PERMISSION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, ACCESS_FIND_LOCATION_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_PERMISSION, ACCESS_FIND_LOCATION_PERMISSION}, LOCATION_REQUEST_CODE);
        } else {
            // 檢查定位功能啟動狀態
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                // 註冊監聽
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            } else {
                Log.d("TAG", "需要啟動定位功能");
            }
        }
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            // 取得經緯度
            String coordinate = String.format("%.6f,%.6f", location.getLatitude(), location.getLongitude());
            Log.d("TAG", coordinate);
            // 取消監聽
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.removeUpdates(this);
            // 結束
            Log.d("TAG", "END");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };

}
