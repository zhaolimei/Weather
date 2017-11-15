package util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetUtil {
	public static final int NETWORN_NONE = 0;
	public static final int NETWORN_WIFI = 1;
	public static final int NETWORN_MOBILE = 2;
	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			Log.d("zlm", "NETWORN_NONE");
			return NETWORN_NONE;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			Log.d("zlm", "NETWORN_MOBILE");
			return NETWORN_MOBILE;
		} 
		else if (nType == ConnectivityManager.TYPE_WIFI) {
			Log.d("zlm", "NETWORN_WIFI");
			return NETWORN_WIFI;
			}
		Log.d("zlm", "next NETWORN_NONE");
		return NETWORN_NONE;
	}

}
