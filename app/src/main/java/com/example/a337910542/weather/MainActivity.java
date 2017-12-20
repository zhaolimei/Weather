package com.example.a337910542.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import bean.TodayWeather;

public class MainActivity extends Activity implements View.OnClickListener,ViewPager.OnPageChangeListener{
	private List<View> views;
	private ViewPagerAdapter viewPagerAdapter;
	private ViewPager viewPager; //多页显示
	private ImageView[] dots;
	private int[] id={R.id.main_pointImg1,R.id.main_pointImg2};

	private static final int UPDATE_TODAY_WEATHER = 1;
	private ImageView mUpdateBtn;//更新
	private ImageView mCitySelect;//城市选择

	private TextView cityTv, timeTv, humidityTv, weekTv, pmDataTv,pmQualityTv,temperatureTv, climateTv, windTv, city_name_Tv,current_tempTv;
	private ImageView weatherImg, pmImg;

	private TextView week1T,week2T,week3T,week4T,week5T,week6T,temperature1T,temperature2T,temperature3T,temperature4T,temperature5T,temperature6T,
	wind1T,wind2T,wind3T,wind4T,wind5T,wind6T,climate1T,climate2T,climate3T,climate4T,climate5T,climate6T;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				//子线程更新数据
				case UPDATE_TODAY_WEATHER:
					updateTodayWeather((TodayWeather) msg.obj);
					break;
				default:
					break;
			}
		}
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guide_activity_main);

		initViews();//viewPager初始化
		initDots();//页面选中点
		//为更新按钮添加点击事件
		mUpdateBtn=(ImageView)views.get(0).findViewById(R.id.title_update_btn);
		mUpdateBtn.setOnClickListener(this);
        /*
        //检测网络连接
        if (NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
        	Log.d("myweather", "网络OK！");
        	Toast.makeText(MainActivity.this, "网络OK！", Toast.LENGTH_LONG).show();
        }else{
        	Log.d("myweather", "网络挂了！");
    		Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
        }
        */
		mCitySelect=(ImageView)views.get(0).findViewById(R.id.title_city_manager);
		mCitySelect.setOnClickListener(this);

		initView();//控件初始化
	}

	void initView(){
		city_name_Tv = (TextView) views.get(0).findViewById(R.id.title_city_name);
		cityTv = (TextView) views.get(0).findViewById(R.id.city);
		timeTv = (TextView) views.get(0).findViewById(R.id.time);
		humidityTv = (TextView) views.get(0).findViewById(R.id.humidity);
		weekTv = (TextView) views.get(0).findViewById(R.id.week_today);
		pmDataTv = (TextView) views.get(0).findViewById(R.id.pm_data);
		pmQualityTv = (TextView) views.get(0).findViewById(R.id.pm2_5_quality);
		pmImg = (ImageView) views.get(0).findViewById(R.id.pm2_5_img);
		temperatureTv = (TextView) views.get(0).findViewById(R.id.temperature);
		climateTv = (TextView) views.get(0).findViewById(R.id.climate);
		windTv = (TextView) views.get(0).findViewById(R.id.wind);
		weatherImg = (ImageView) views.get(0).findViewById(R.id.weather_img);
		current_tempTv=(TextView)views.get(0).findViewById(R.id.current_temp);
		week1T=(TextView)views.get(0).findViewById(R.id.future6_no1_week);
		week2T=(TextView)views.get(0).findViewById(R.id.future6_no2_week);
		week3T=(TextView)views.get(0).findViewById(R.id.future6_no3_week);
		week4T=(TextView)views.get(1).findViewById(R.id.future6_no4_week);
		week5T=(TextView)views.get(1).findViewById(R.id.future6_no5_week);
		week6T=(TextView)views.get(1).findViewById(R.id.future6_no6_week);
		temperature1T=(TextView)views.get(0).findViewById(R.id.future6_no1_temperature);
		temperature2T=(TextView)views.get(0).findViewById(R.id.future6_no2_temperature);
		temperature3T=(TextView)views.get(0).findViewById(R.id.future6_no3_temperature);
		temperature4T=(TextView)views.get(1).findViewById(R.id.future6_no4_temperature);
		temperature5T=(TextView)views.get(1).findViewById(R.id.future6_no5_temperature);
		temperature6T=(TextView)views.get(1).findViewById(R.id.future6_no6_temperature);
		wind1T=(TextView)views.get(0).findViewById(R.id.future6_no1_wind);
		wind2T=(TextView)views.get(0).findViewById(R.id.future6_no2_wind);
		wind3T=(TextView)views.get(0).findViewById(R.id.future6_no3_wind);
		wind4T=(TextView)views.get(1).findViewById(R.id.future6_no4_wind);
		wind5T=(TextView)views.get(1).findViewById(R.id.future6_no5_wind);
		wind6T=(TextView)views.get(1).findViewById(R.id.future6_no6_wind);
		climate1T=(TextView)views.get(0).findViewById(R.id.future6_no1_climate);
		climate2T=(TextView)views.get(0).findViewById(R.id.future6_no2_climate);
		climate3T=(TextView)views.get(0).findViewById(R.id.future6_no3_climate);
		climate4T=(TextView)views.get(1).findViewById(R.id.future6_no4_climate);
		climate5T=(TextView)views.get(1).findViewById(R.id.future6_no5_climate);
		climate6T=(TextView)views.get(1).findViewById(R.id.future6_no6_climate);

		city_name_Tv.setText("N/A");
		cityTv.setText("N/A");
		timeTv.setText("N/A");
		humidityTv.setText("N/A");
		pmDataTv.setText("N/A");
		pmQualityTv.setText("N/A");
		weekTv.setText("N/A");
		temperatureTv.setText("N/A");
		climateTv.setText("N/A");
		windTv.setText("N/A");
		current_tempTv.setText("N/A");
	}

	private void initViews()
	{
		LayoutInflater lf = LayoutInflater.from(this);
		views = new ArrayList<View>();
		views.add(lf.inflate(R.layout.activity_main,null));
		views.add(lf.inflate(R.layout.activity_main_2,null));

		viewPagerAdapter = new ViewPagerAdapter(views,this);
		viewPager = (ViewPager)findViewById(R.id.viewpager);
		viewPager.setAdapter(viewPagerAdapter);
		viewPager.setOnPageChangeListener(this);
	}

	private void initDots()
	{
		dots = new ImageView[views.size()];
		for(int i=0;i< views.size();i++)
		{
			dots[i] = (ImageView)findViewById(id[i]);
		}
	}

	@Override
	//更新、城市选择按钮点击事件
	public void onClick(View view){
		if(view.getId()==R.id.title_city_manager){
			Intent i=new Intent(this,SelectCity.class);
			Bundle bundle = new Bundle();
			bundle.putString("currentCity", city_name_Tv.getText().toString());//传递当前城市到selectcity
			i.putExtras(bundle);
			//startActivity(i);
			//跳转到selectcity并获得回传数据
			startActivityForResult(i,1);
		}

		if(view.getId()==R.id.title_update_btn){
			//通过SharedPreferences读取城市id，如果没有定义则缺省为101010100北京
			SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
			String cityCode=sharedPreferences.getString("main_city_code","");
            if(cityCode.equals(""))
            {
                cityCode="101010100"; //为空则默认北京
            }
            Log.d("myWeather",cityCode);
            queryWeatherCode(cityCode);
			//通过citycode获取网络数据

    		/*
    		if(NetUtil.getNetworkState(this)!=NetUtil.NETWORN_NONE){
    			Log.d("myWeather", "网络OK");
    			queryWeatherCode(cityCode);
    		}else
    		{
    			Log.d("myWeather", "网络挂了");
    			Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
    		}
    		*/
		}
	}
	//接收调用返回数据
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//判断为selectcity
		if (requestCode == 1 && resultCode == RESULT_OK) {
			if(data.getIntExtra("selectFlag",0)==1)//判断是否有选择更新城市
			{
				String newCityCode= data.getStringExtra("cityCode");
				Log.d("myWeather", "选择的城市代码为"+newCityCode);
				SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
				String cityCode=sharedPreferences.getString("main_city_code",newCityCode);
				Log.d("myWeather",cityCode);

				queryWeatherCode(cityCode);
			}
    		/*
    		if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
    			Log.d("myWeather", "网络OK");
    			queryWeatherCode(newCityCode);
    		} else {
    			Log.d("myWeather", "网络挂了");
    			Toast.makeText(MainActivity.this, "网络挂了！", Toast.LENGTH_LONG).show();
    		}
    		*/
		}
	}

	// @param cityCode
	private void queryWeatherCode(String cityCode){
		final String address="http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;
		Log.d("myWeather",address);
		//子线程获取数据
		new Thread(new Runnable(){
			@Override
			public void run(){
				HttpURLConnection con=null;
				TodayWeather todayWeather =null;
				try{
					URL url = new URL(address);
					con=(HttpURLConnection)url.openConnection();
					con.setRequestMethod("GET");
					con.setConnectTimeout(8000);
					con.setReadTimeout(8000);
					InputStream in=con.getInputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();//页面返回数据存放
					String str;
					while((str=reader.readLine())!=null){
						response.append(str);
						Log.d("myWeather", str);
					}
					String responseStr=response.toString();
					Log.d("myWeather", responseStr);
					//xlm数据解析
					todayWeather=parseXML(responseStr);
					if(todayWeather !=null){
						Log.d("myWeather",todayWeather.toString() );

						Message msg =new Message();
						msg.what = UPDATE_TODAY_WEATHER;
						msg.obj=todayWeather;
						mHandler.sendMessage(msg);
					}
				}catch (Exception e){
					e.printStackTrace();
				}finally{
					if(con!=null){
						con.disconnect();
					}
				}
			}
		}).start();
	}


  //xml数据解析
	private TodayWeather parseXML(String xmldata){
		TodayWeather todayWeather = null;
		int fengxiangCount=0;
		int fengliCount =0;
		int dateCount=0;
		int highCount =0;
		int lowCount=0;
		int typeCount =0;
		int fengliCount1 =0;
		int dateCount1=0;
		int highCount1 =0;
		int lowCount1=0;
		int typeCount1 =0;
		int fengliCount3 =0;
		int dateCount3=0;
		int highCount3 =0;
		int lowCount3=0;
		int typeCount3 =0;
		int fengliCount4 =0;
		int dateCount4=0;
		int highCount4 =0;
		int lowCount4=0;
		int typeCount4 =0;
		int fengliCount5 =0;
		int dateCount5=0;
		int highCount5 =0;
		int lowCount5=0;
		int typeCount5 =0;
		int fengliCount6 =0;
		int dateCount6=0;
		int highCount6 =0;
		int lowCount6=0;
		int typeCount6 =0;
		try {
			XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
			XmlPullParser xmlPullParser = fac.newPullParser();
			xmlPullParser.setInput(new StringReader(xmldata));
			int eventType = xmlPullParser.getEventType();
			Log.d("myWeather", "parseXML");
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
					// 判断当前事件是否为文档开始事件
					case XmlPullParser.START_DOCUMENT:
						break;
					// 判断当前事件是否为标签元素开始事件
					case XmlPullParser.START_TAG:
						if(xmlPullParser.getName().equals("resp"
						)){
							todayWeather= new TodayWeather();
						}
						if (todayWeather != null) {
							if (xmlPullParser.getName().equals("city")) {
								eventType = xmlPullParser.next()	;
								todayWeather.setCity(xmlPullParser.getText());
							} else if (xmlPullParser.getName().equals("updatetime")) {
								eventType = xmlPullParser.next();
								todayWeather.setUpdatetime(xmlPullParser.getText());
							} else if (xmlPullParser.getName().equals("shidu")) {
								eventType = xmlPullParser.next();
								todayWeather.setShidu(xmlPullParser.getText());
							} else if (xmlPullParser.getName().equals("wendu")) {
								eventType = xmlPullParser.next();
								todayWeather.setWendu(xmlPullParser.getText());
							} else if (xmlPullParser.getName().equals("pm25")) {
								eventType = xmlPullParser.next();
								todayWeather.setPm25(xmlPullParser.getText());
							} else if (xmlPullParser.getName().equals("quality")) {
								eventType = xmlPullParser.next();
								todayWeather.setQuality(xmlPullParser.getText());
							} else if (xmlPullParser.getName().equals("fengxiang") ) {
								eventType = xmlPullParser.next();
								todayWeather.setFengxiang(xmlPullParser.getText());
								fengxiangCount++;
							} else if (xmlPullParser.getName().equals("fl_1")&&fengliCount1==0) {
								eventType = xmlPullParser.next();
								todayWeather.setFengli1(xmlPullParser.getText());
								fengliCount1++;
							} else if (xmlPullParser.getName().equals("date_1") &&dateCount1==0){
								eventType = xmlPullParser.next();
								todayWeather.setDate1(xmlPullParser.getText());
								dateCount1++;
							} else if (xmlPullParser.getName().equals("high_1") &&highCount1==0) {
								eventType = xmlPullParser.next();
								todayWeather.setHigh1(xmlPullParser.getText().substring(2).trim());
								highCount1++;
							} else if (xmlPullParser.getName().equals("low_1") &&lowCount1==0) {
								eventType = xmlPullParser.next();
								todayWeather.setLow1(xmlPullParser.getText().substring(2).trim());
								lowCount1++;
							} else if (xmlPullParser.getName().equals("type_1")&&typeCount1==0 ) {
								eventType = xmlPullParser.next();
								todayWeather.setType1(xmlPullParser.getText());
								typeCount1++;
							} else if (xmlPullParser.getName().equals("fengli") &&fengliCount==0) {
								eventType = xmlPullParser.next();
								todayWeather.setFengli(xmlPullParser.getText());
								fengliCount++;
							} else if (xmlPullParser.getName().equals("date") &&dateCount==0) {
								eventType = xmlPullParser.next();
								todayWeather.setDate(xmlPullParser.getText());
								dateCount++;
							} else if (xmlPullParser.getName().equals("high")&&highCount==0 ) {
								eventType = xmlPullParser.next();
								todayWeather.setHigh(xmlPullParser.getText().substring(2).trim());
								highCount++;
							} else if (xmlPullParser.getName().equals("low") &&lowCount==0) {
								eventType = xmlPullParser.next();
								todayWeather.setLow(xmlPullParser.getText().substring(2).trim());
								lowCount++;
							} else if (xmlPullParser.getName().equals("type")&&typeCount==0 ) {
								eventType = xmlPullParser.next();
								todayWeather.setType(xmlPullParser.getText());
								typeCount++;
							}else if (xmlPullParser.getName().equals("fengli") &&fengliCount3==0) {
								eventType = xmlPullParser.next();
								todayWeather.setFengli3(xmlPullParser.getText());
								fengliCount3++;
							} else if (xmlPullParser.getName().equals("date") &&dateCount3==0) {
								eventType = xmlPullParser.next();
								todayWeather.setDate3(xmlPullParser.getText());
								dateCount3++;
							} else if (xmlPullParser.getName().equals("high")&&highCount3==0 ) {
								eventType = xmlPullParser.next();
								todayWeather.setHigh3(xmlPullParser.getText().substring(2).trim());
								highCount3++;
							} else if (xmlPullParser.getName().equals("low") &&lowCount3==0) {
								eventType = xmlPullParser.next();
								todayWeather.setLow3(xmlPullParser.getText().substring(2).trim());
								lowCount3++;
							} else if (xmlPullParser.getName().equals("type")&&typeCount3==0 ) {
								eventType = xmlPullParser.next();
								todayWeather.setType3(xmlPullParser.getText());
								typeCount3++;
							}else if (xmlPullParser.getName().equals("fengli") &&fengliCount4==0) {
								eventType = xmlPullParser.next();
								todayWeather.setFengli4(xmlPullParser.getText());
								fengliCount4++;
							} else if (xmlPullParser.getName().equals("date")&&dateCount4==0 ) {
								eventType = xmlPullParser.next();
								todayWeather.setDate4(xmlPullParser.getText());
								dateCount4++;
							} else if (xmlPullParser.getName().equals("high")&&highCount4==0 ) {
								eventType = xmlPullParser.next();
								todayWeather.setHigh4(xmlPullParser.getText().substring(2).trim());
								highCount4++;
							} else if (xmlPullParser.getName().equals("low")&&lowCount4==0) {
								eventType = xmlPullParser.next();
								todayWeather.setLow4(xmlPullParser.getText().substring(2).trim());
								lowCount4++;
							} else if (xmlPullParser.getName().equals("type")&&typeCount4==0 ) {
								eventType = xmlPullParser.next();
								todayWeather.setType4(xmlPullParser.getText());
								typeCount4++;
							}else if (xmlPullParser.getName().equals("date")&&dateCount5==0) {
								eventType = xmlPullParser.next();
								todayWeather.setDate5(xmlPullParser.getText());
								dateCount5++;
							} else if (xmlPullParser.getName().equals("high")&&highCount5==0) {
								eventType = xmlPullParser.next();
								todayWeather.setHigh5(xmlPullParser.getText().substring(2).trim());
								highCount5++;
							} else if (xmlPullParser.getName().equals("low")&&lowCount5==0) {
								eventType = xmlPullParser.next();
								todayWeather.setLow5(xmlPullParser.getText().substring(2).trim());
								lowCount5++;
							} else if (xmlPullParser.getName().equals("type")&&typeCount5==0 ) {
								eventType = xmlPullParser.next();
								todayWeather.setType5(xmlPullParser.getText());
								typeCount5++;
							} else if (xmlPullParser.getName().equals("fengli") &&fengliCount5==0) {
								eventType = xmlPullParser.next();
								todayWeather.setFengli5(xmlPullParser.getText());
								fengliCount5++;
							}else if (xmlPullParser.getName().equals("fengli")&&fengliCount6==0 ) {
								eventType = xmlPullParser.next();
								todayWeather.setFengli6(xmlPullParser.getText());
								fengliCount6++;
							} else if (xmlPullParser.getName().equals("date") &&dateCount6==0) {
								eventType = xmlPullParser.next();
								todayWeather.setDate6(xmlPullParser.getText());
								dateCount6++;
							} else if (xmlPullParser.getName().equals("high")&&highCount6==0) {
								eventType = xmlPullParser.next();
								todayWeather.setHigh6(xmlPullParser.getText().substring(2).trim());
								highCount6++;
							} else if (xmlPullParser.getName().equals("low") &&lowCount6==0) {
								eventType = xmlPullParser.next();
								todayWeather.setLow6(xmlPullParser.getText().substring(2).trim());
								lowCount6++;
							} else if (xmlPullParser.getName().equals("type")&&typeCount6==0 ) {
								eventType = xmlPullParser.next();
								todayWeather.setType6(xmlPullParser.getText());
								typeCount6++;
							}
						}
						break;
					// 判断当前事件是否为标签元素结束事件
					case XmlPullParser.END_TAG:
						break;
				}
				// 进入下一个元素并触发相应事件
				eventType = xmlPullParser.next();
			}
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return todayWeather;
	}

   //更新数据到页面
	void updateTodayWeather(TodayWeather todayWeather){
		city_name_Tv.setText(todayWeather.getCity()+"天气");
		cityTv.setText(todayWeather.getCity());
		timeTv.setText(todayWeather.getUpdatetime()+ "发布");
		humidityTv.setText("湿度："+todayWeather.getShidu());
		current_tempTv.setText("温度："+todayWeather.getWendu());
		pmDataTv.setText(todayWeather.getPm25());
		pmQualityTv.setText(todayWeather.getQuality());
		weekTv.setText(todayWeather.getDate());
		temperatureTv.setText(todayWeather.getLow()+"~"+todayWeather.getHigh());
		climateTv.setText(todayWeather.getType());
		windTv.setText("风力:"+todayWeather.getFengli());

		week1T.setText(todayWeather.getDate1());
		temperature1T.setText(todayWeather.getLow1()+"~"+todayWeather.getHigh1());
		climate1T.setText(todayWeather.getType1());
		wind1T.setText("风力:"+todayWeather.getFengli1());
		week2T.setText(todayWeather.getDate());
		temperature2T.setText(todayWeather.getLow()+"~"+todayWeather.getHigh());
		climate2T.setText(todayWeather.getType());
		wind2T.setText("风力:"+todayWeather.getFengli());
		week3T.setText(todayWeather.getDate3());
		temperature3T.setText(todayWeather.getLow3()+"~"+todayWeather.getHigh3());
		climate3T.setText(todayWeather.getType3());
		wind3T.setText("风力:"+todayWeather.getFengli3());
		week4T.setText(todayWeather.getDate4());
		temperature4T.setText(todayWeather.getLow4()+"~"+todayWeather.getHigh4());
		climate4T.setText(todayWeather.getType4());
		wind4T.setText("风力:"+todayWeather.getFengli4());
		week5T.setText(todayWeather.getDate5());
		temperature5T.setText(todayWeather.getLow5()+"~"+todayWeather.getHigh5());
		climate5T.setText(todayWeather.getType5());
		wind5T.setText("风力:"+todayWeather.getFengli5());
		week6T.setText(todayWeather.getDate6());
		temperature6T.setText(todayWeather.getLow6()+"~"+todayWeather.getHigh6());
		climate6T.setText(todayWeather.getType6());
		wind6T.setText("风力:"+todayWeather.getFengli6());

		Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
		if(todayWeather.getPm25()!=null) {
			int pm25 = Integer.parseInt(todayWeather.getPm25());
			if (pm25 <= 50) {
				pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
			} else if (pm25 >= 51 && pm25 <= 100) {
				pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
			} else if (pm25 >= 101 && pm25 <= 150) {
				pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
			} else if (pm25 >= 151 && pm25 <= 200) {
				pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
			} else if (pm25 >= 201 && pm25 <= 300) {
				pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
			}
		}
		if(todayWeather.getType()!=null)
		{
			switch (todayWeather.getType()) {
				case "晴":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
					break;
				case "阴":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
					break;
				case "雾":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
					break;
				case "多云":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
					break;
				case "小雨":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
					break;
				case "中雨":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
					break;
				case "大雨":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
					break;
				case "阵雨":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
					break;
				case "雷阵雨":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
					break;
				case "雷阵雨加暴":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
					break;
				case "暴雨":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
					break;
				case "大暴雨":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
					break;
				case "特大暴雨":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
					break;
				case "阵雪":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
					break;
				case "暴雪":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
					break;
				case "大雪":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
					break;
				case "小雪":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
					break;
				case "雨夹雪":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
					break;
				case "中雪":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
					break;
				case "沙尘暴":
					weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
					break;
				default:
					break;
			}

		}

	}


	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		for(int i=0;i<id.length;i++)
		{
			Log.d("page select id",Integer.toString(i));
			if(i==position)
			{
				dots[i].setImageResource(R.drawable.page_indicator_focused);
			}else{
				dots[i].setImageResource(R.drawable.page_indicator_unfocused);
			}
		}

	}

	@Override
	public void onPageScrollStateChanged(int state) {

	}

}
