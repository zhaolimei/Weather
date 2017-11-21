package com.example.a337910542.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bean.City;

public class SelectCity extends Activity implements View.OnClickListener{
	private ImageView mBackBtn;//返回
	private EditText searchEt;//搜索编辑
	private ImageView searchBtn;//搜索确认按钮
	private ListView cityListLv;//listview
	private List<City> mCityList;  //存放城市数据
	private MyApplication myApplication;   //读citydb
	private ArrayList<String> mArryList;//城市名array
	private String updateCityCode;//点击的城市代码
	private ArrayAdapter<String> adapter;
	private TextView currentCity;


	@Override
	protected void onCreate(Bundle savedInstanceState){		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_city);
		mBackBtn=(ImageView)findViewById(R.id.title_back);
		mBackBtn.setOnClickListener(this);
		searchEt=(EditText)findViewById(R.id.selectcity_search);
		searchBtn=(ImageView)findViewById(R.id.selectcity_search_button);
		searchBtn.setOnClickListener(this);
		Log.d("mybug" , "before！");
		myApplication=(MyApplication)getApplication();//读库
		mCityList=myApplication.getCityList();//获取城市列表
		mArryList =new ArrayList<String>();
		currentCity=(TextView)findViewById(R.id.title_name);

		Bundle bundle = getIntent().getExtras();//获取MainActivity天气首页传递的当前城市
		currentCity.setText(bundle.getString("currentCity")); //读出当前城市



		for(int i=0;i<mCityList.size();i++) //获取城市名列表
		{
			String cityName=mCityList.get(i).getCity();
			mArryList.add(cityName);
		}
		cityListLv=(ListView)findViewById(R.id.selectcity_lv);
		//新建listview适配器
		adapter=new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,mArryList);
		cityListLv.setAdapter(adapter);
		//添加ListView项的点击事件的动作
		AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?>parent,View view,int position, long id){
				updateCityCode = mCityList.get(position).getNumber();
				Toast.makeText(SelectCity.this,mCityList.get(position).getCity(),Toast.LENGTH_SHORT).show();
				//Log.d("update city code",Integer.toString(updateCityCode));
			}
		};
		//为组件绑定监听
		cityListLv.setOnItemClickListener(itemClickListener);
	}

	ArrayList<String> mSearchList=new ArrayList<String>();
	 //back点击回传citycode
	@Override
	public void onClick(View v){
		switch (v.getId()){
			case R.id.selectcity_search_button:
				String search_name=searchEt.getText().toString();

				for(int i=0;i<mCityList.size();i++)
				{
					String cityName=mCityList.get(i).getCity();
					if(cityName.equals(search_name)){
						mSearchList.add(search_name);
					}
				}
				ArrayAdapter<String> adapter=new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,mSearchList);
				cityListLv.setAdapter(adapter);
				adapter.notifyDataSetChanged();


				AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?>parent,View view,int position, long id){
						for(int i=0;i<mCityList.size();i++)
						{
							if(mCityList.get(i).getCity().equals(mSearchList.get(position)))
							{
								updateCityCode=mCityList.get(i).getNumber();
							}
						}
						Toast.makeText(SelectCity.this,mSearchList.get(position),Toast.LENGTH_SHORT).show();
						//Log.d("update city code",Integer.toString(updateCityCode));
					}
				};
				//为组件绑定监听
				cityListLv.setOnItemClickListener(itemClickListener);



				break;

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
