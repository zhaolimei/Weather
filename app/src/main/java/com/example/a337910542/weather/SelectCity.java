package com.example.a337910542.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import bean.City;

public class SelectCity extends Activity implements View.OnClickListener{
	private ImageView mBackBtn;
	private ListView cityListLv;

	private List<City> mCityList;  //存放城市数据
	private MyApplication myApplication;   //读db
	private ArrayList<String> mArryList;
	private int updateCityCode;

	@Override
	protected void onCreate(Bundle savedInstanceState){		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_city);
		
		mBackBtn=(ImageView)findViewById(R.id.title_back);
		mBackBtn.setOnClickListener(this);
		Log.d("mybug" , "before！");
		myApplication=(MyApplication)getApplication();//读库
		Log.d("mybug" , "挂了！");

		mCityList=myApplication.getCityList();
		mArryList =new ArrayList<String>();

		for(int i=0;i<mCityList.size();i++)
		{
			String cityName=mCityList.get(i).getCity();
			mArryList.add(cityName);
		}

		cityListLv=(ListView)findViewById(R.id.selectcity_lv);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,mArryList);
		cityListLv.setAdapter(adapter);

		//添加ListView项的点击事件的动作
		AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?>parent,View view,int position, long id){
				updateCityCode = Integer.parseInt(mCityList.get(position).getNumber());
				Log.d("update city code",Integer.toString(updateCityCode));
			}
		};
         //为组件绑定监听
		cityListLv.setOnItemClickListener(itemClickListener);

/*
		String[] listDate={"1","2","3"};
		cityListLv=(ListView)findViewById(R.id.selectcity_lv);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,listDate);
		cityListLv.setAdapter(adapter);
*/

	}
	 
	@Override
	public void onClick(View v){
		switch (v.getId()){
			case R.id.title_back:
				Intent i=new Intent();
				i.putExtra("cityCode",updateCityCode);
				setResult(RESULT_OK,i);
				finish();
				break;
			default:
				break;
		}
	}
	

}
