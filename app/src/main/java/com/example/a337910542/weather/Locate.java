package com.example.a337910542.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.List;

import bean.City;

/**
 * Created by 337910542 on 2017/12/13.
 */

public class Locate extends Activity{
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;

    Button locateBtn;
    private  String mLocCityCode;
    private List<City> mCityList;
    private MyApplication myApplication;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("test", "定位");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location);
        locateBtn = (Button)findViewById(R.id.bdmap_cityname);

        mLocationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener(locateBtn);
        mLocationClient.registerLocationListener(myLocationListener);
        initLocation();
        mLocationClient.start();

        final Intent intent=new Intent(this,MainActivity.class);
        locateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                myApplication=(MyApplication)getApplication();
                mCityList=myApplication.getCityList();
                for(City city:mCityList)
                {
                    String locateCityName =locateBtn.getText().toString();
                    if(city.getCity().equals(locateCityName.substring(0,locateCityName.length()-1))){
                        mLocCityCode=city.getNumber();
                        Log.d("locate",locateCityName.substring(0,locateCityName.length()-1));
                    }
                }
                SharedPreferences sharedPreferences=getSharedPreferences("CityCodePreference",Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("main_city_code",mLocCityCode);
                editor.commit();
                intent.putExtra("main_city_code",mLocCityCode);
                startActivity(intent);
            }
        });
    }
    void initLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }
    public class MyLocationListener implements BDLocationListener {
        Button locBtn;
        MyLocationListener(Button locBtn)
        {
            this.locBtn = locBtn;
        }
        String cityName;
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            cityName = bdLocation.getCity();
            Log.d("Locate",cityName);
            locBtn.setText(cityName);
        }
    }
}

