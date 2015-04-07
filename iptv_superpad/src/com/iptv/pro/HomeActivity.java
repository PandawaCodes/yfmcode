package com.iptv.pro;

import java.io.File;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iptv.pro.UpdateService.DownloadThreadListenerImpl;
import com.iptv.season.R;
import com.iptv.thread.DownLoadThread;
import com.iptv.thread.DownloadThreadListener;
import com.iptv.thread.UserInfoThread;
import com.iptv.utils.HttpUtils;
import com.iptv.utils.Utils;
import com.pro.vidio.MainActivity_;

public class HomeActivity extends Activity {

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		showExit();
	}

	private void showExit() {
		Builder builder = new Builder(this, AlertDialog.THEME_HOLO_DARK);
		builder.setTitle("退出");
		builder.setMessage("确定要退出应用");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				HomeActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == rc && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			Intent video = getVideoFileIntent(uri);
			startActivity(video);
		}
	}

	// android获取一个用于打开视频文件的intent
	public static Intent getVideoFileIntent(Uri uri) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		intent.setDataAndType(uri, "video/*");
		return intent;
	}

	private ImageView livetv, playback, point, market,
			systemsetup, info;
	private HttpUtils hu;
	private SharedPreferences sp;
	private ProgressDialog pd;
	private int rc = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		sp = this.getSharedPreferences("key", Context.MODE_PRIVATE);
		hu = new HttpUtils(this);
		livetv = (ImageView) super.findViewById(R.id.livetv);
		playback = (ImageView) super.findViewById(R.id.playback);
		point = (ImageView) super.findViewById(R.id.point);
		market = (ImageView) super.findViewById(R.id.market);

		systemsetup = (ImageView) super.findViewById(R.id.systemsetup);
		info = (ImageView) super.findViewById(R.id.info);
		playback = (ImageView) super.findViewById(R.id.playback);
		playback = (ImageView) super.findViewById(R.id.playback);

		livetv.setOnClickListener(new OnClickListenerImpl());
		playback.setOnClickListener(new OnClickListenerImpl());
		point.setOnClickListener(new OnClickListenerImpl());
		market.setOnClickListener(new OnClickListenerImpl());
		systemsetup.setOnClickListener(new OnClickListenerImpl());
		info.setOnClickListener(new OnClickListenerImpl());
	}

	class OnClickListenerImpl implements OnClickListener {

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub

			if (but.getId() == R.id.livetv) {
				Intent intent = new Intent(HomeActivity.this,
						PlayActivity.class);
				HomeActivity.this.startActivity(intent);
			}
			if (but.getId() == R.id.playback) {
				Intent intent = new Intent(HomeActivity.this,
						BackActivity.class);
				HomeActivity.this.startActivity(intent);
			}
			if (but.getId() == R.id.point) {
				Intent intent = new Intent(HomeActivity.this,MainActivity_.class);
				HomeActivity.this.startActivity(intent);
				//openCLD("apphi.cv_tv_tvdroid");
			}
			if (but.getId() == R.id.market) {
				Openhome();
			}
			if (but.getId() == R.id.systemsetup) {
				openCLD("com.android.settings");
			}
			if (but.getId() == R.id.info) {
				show("获取", "正在获取用户信息...");
				new UserInfoThread(HomeActivity.this, handler,
						"getUserDate.jsp?active=" + sp.getString("name", ""))
						.start();
			}
			if(but.getId()==R.id.ipoint||but.getId()==R.id.player||but.getId()==R.id.cleaner){
				String url=but.getTag().toString();
				File file=createFile(url);
				if(file!=null){
					DownLoadThread dt = new DownLoadThread(url, file,
							new DownloadThreadListenerImpl());
					dt.start();
					Toast.makeText(HomeActivity.this, "开始下载", Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

	private void show(String title, String msg) {
		pd = new ProgressDialog(this, ProgressDialog.THEME_HOLO_DARK);
		pd.setTitle(title);
		pd.setMessage(msg);
		pd.show();
	}
	private void Openhome() {
		Intent intent = new Intent(this, AllAppActivity.class);
		this.startActivity(intent);
	}


	private void showinfo(String message) {
		Builder builder = new Builder(this, AlertDialog.THEME_HOLO_DARK);
		builder.setTitle("消息");
		View view = LayoutInflater.from(this).inflate(R.layout.userinfo, null);
		TextView versioncode = (TextView) view.findViewById(R.id.versioncode);
		Button ipoint = (Button) view.findViewById(R.id.ipoint);
		Button player = (Button) view.findViewById(R.id.player);
		Button cleaner = (Button) view.findViewById(R.id.cleaner);
		ipoint.setOnClickListener(new OnClickListenerImpl());
		player.setOnClickListener(new OnClickListenerImpl());
		cleaner.setOnClickListener(new OnClickListenerImpl());
		
		
		versioncode.setText(getVersionName());
		TextView msg = (TextView) view.findViewById(R.id.message);
		CheckBox autostart = (CheckBox) view.findViewById(R.id.autostart);
		boolean isauto = sp.getBoolean("autostart", false);
		autostart.setChecked(isauto);
		autostart.setOnCheckedChangeListener(new OnCheckedChangeListenerImpl());
		msg.setText(message);
		builder.setView(view);
		builder.setPositiveButton("关闭", null);
		builder.create().show();
	}

	public void showinstall(final File file) {
		Builder builder = new Builder(this);
		builder.setTitle("安装");
		builder.setMessage("下载完成,是否安装...");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setDataAndType(Uri.fromFile(file),
						"application/vnd.android.package-archive");
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);

			}
		});
		builder.setNegativeButton("取消", null);
		AlertDialog dialog = builder.create();
		dialog.getWindow()
				.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.show();
	}
	public File createFile(String url) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			String filename=url.substring(url.lastIndexOf("/")+1);
			Log.i("tvinfo", url+"  "+filename);
			File file = new File(Environment.getExternalStorageDirectory()
					+ "/"+filename);
			if (file.exists()) {
				Log.i("tvinfo", "文件存在");
				file.delete();
			}
			return file;
		}
		return null;
	}
	class DownloadThreadListenerImpl implements DownloadThreadListener {

		@Override
		public void afterPerDown(String uri, long count, long rcount) {
			// TODO Auto-generated method stub

		}

		@Override
		public void downCompleted(String uri, long count, long rcount,
				boolean isdown, File file) {
			// TODO Auto-generated method stub
			if (file != null&&file.length()>0) {
				Message msg = handler.obtainMessage();
				msg.what = 2;
				msg.obj = file;
				handler.sendMessage(msg);

			}else{
				handler.sendEmptyMessage(3);
			}
		}

		@Override
		public void returncode(int statecode) {
			// TODO Auto-generated method stub
		}

	}
	private Handler handler=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 2 && msg.obj != null) {
				showinstall((File) msg.obj);
			}else if (msg.what == 100) {
				pd.dismiss();
				if (msg.arg1 == 1) {
					showinfo("到期时间：" + msg.obj + "\r\n登录标识："
							+ Utils.getLocalMacAddressFromIp(HomeActivity.this));
				}
			}else{
				Toast.makeText(HomeActivity.this, "下载失败", Toast.LENGTH_SHORT).show();
			}
		}
		
	};
	
	private String getVersionName() {
		String version = "";
		try {
			PackageManager packageManager = getPackageManager();
			PackageInfo packInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}

	class OnCheckedChangeListenerImpl implements OnCheckedChangeListener,OnClickListener {

		@Override
		public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
			// TODO Auto-generated method stub
			Editor editor = sp.edit();
			editor.putBoolean("autostart", arg1);
			editor.commit();
		}

		@Override
		public void onClick(View but) {
			// TODO Auto-generated method stub
			
		}

	}

	public boolean openCLD(String packageName) {
		PackageManager packageManager = this.getPackageManager();
		PackageInfo pi = null;
		try {
			pi = packageManager.getPackageInfo(packageName, 0);
		} catch (NameNotFoundException e) {
			Toast.makeText(this, "应用未安装"+ packageName, Toast.LENGTH_SHORT).show();
			Log.i("tvinfo", "packageName不存在" + packageName);
		}
		if (pi == null) {
			return false;
		}
		Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
		resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		resolveIntent.setPackage(pi.packageName);

		List<ResolveInfo> apps = packageManager.queryIntentActivities(
				resolveIntent, 0);
		ResolveInfo ri = apps.iterator().next();
		if (ri != null) {
			String className = ri.activityInfo.name;

			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);

			ComponentName cn = new ComponentName(packageName, className);

			intent.setComponent(cn);
			this.startActivity(intent);
			return true;
		}
		return false;

	}
}
