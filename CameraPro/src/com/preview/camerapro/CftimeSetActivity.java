package com.preview.camerapro;

import com.preview.utils.ConfigUtils;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class CftimeSetActivity extends Activity {

	private EditText zstart,zend,wstart,wend,start,end;
	private Button save,quxiao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);// 保持屏幕亮
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.activity_set_cftime);
		zstart=(EditText)super.findViewById(R.id.zstart);
		zend=(EditText)super.findViewById(R.id.zend);
		wstart=(EditText)super.findViewById(R.id.wstart);
		wend=(EditText)super.findViewById(R.id.wend);
		start=(EditText)super.findViewById(R.id.start);
		end=(EditText)super.findViewById(R.id.end);
		zstart.setOnClickListener(new TimeOnClickListener());
		zend.setOnClickListener(new TimeOnClickListener());
		wstart.setOnClickListener(new TimeOnClickListener());
		wend.setOnClickListener(new TimeOnClickListener());
		start.setOnClickListener(new TimeOnClickListener());
		end.setOnClickListener(new TimeOnClickListener());
		
		zstart.setText(ConfigUtils.GetString(this, "zstart", ""));
		zend.setText(ConfigUtils.GetString(this, "zend", ""));
		wstart.setText(ConfigUtils.GetString(this, "wstart", ""));
		wend.setText(ConfigUtils.GetString(this, "wend", ""));
		start.setText(ConfigUtils.GetString(this, "start", ""));
		end.setText(ConfigUtils.GetString(this, "end", ""));
		
		save=(Button)super.findViewById(R.id.save);
		quxiao=(Button)super.findViewById(R.id.quxiao);
		save.setOnClickListener(new saveOnClickListener());
		quxiao.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CftimeSetActivity.this.finish();
			}
		});
	}
	class TimeOnClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v instanceof EditText){
				EditText timetext=(EditText)v;
				int hour=12;
				int minute=0;
				String time=timetext.getText().toString();
				if(time!=null&&!"".equals(time)){
					String[] p=time.split(":");
					hour=Integer.parseInt(p[0]);
					minute=Integer.parseInt(p[1]);
				}
				TimePickerDialog tpd=new TimePickerDialog(CftimeSetActivity.this, new OnTimeSetListenerImpl(timetext), hour, minute, true);
				tpd.show();
			}
			
		}
	}
	class OnTimeSetListenerImpl implements OnTimeSetListener{

		private EditText timetext;
		public OnTimeSetListenerImpl(EditText timetext){
			this.timetext=timetext;
		}
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// TODO Auto-generated method stub
			
			timetext.setText((hourOfDay<10?"0"+hourOfDay:hourOfDay)+":"+(minute<10?"0"+minute:minute));
		}
		
	}
	class saveOnClickListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			ConfigUtils.savaDate(CftimeSetActivity.this, "zstart", zstart.getText().toString());
			ConfigUtils.savaDate(CftimeSetActivity.this, "zend", zend.getText().toString());
			ConfigUtils.savaDate(CftimeSetActivity.this, "wstart", wstart.getText().toString());
			ConfigUtils.savaDate(CftimeSetActivity.this, "wend", wend.getText().toString());
			ConfigUtils.savaDate(CftimeSetActivity.this, "start", start.getText().toString());
			ConfigUtils.savaDate(CftimeSetActivity.this, "end", end.getText().toString());
			Toast.makeText(CftimeSetActivity.this, "保存成功", Toast.LENGTH_LONG).show();
			CftimeSetActivity.this.finish();
		}
		
	}
}
