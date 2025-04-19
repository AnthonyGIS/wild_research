package com.weng.Login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.weng.R;

public class login_f1 extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private EditText uname = null;
	private EditText upswd = null;
	private CheckBox auto = null;
	private Button login = null;
	SharedPreferences sp = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sp = this.getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        init();
    }
    public void init()
	{
		uname = (EditText) findViewById(R.id.uname);
		upswd = (EditText) findViewById(R.id.upswd);
	    auto = (CheckBox) findViewById(R.id.auto);
	    login = (Button) findViewById(R.id.login);
	    if (sp.getBoolean("auto", false))
	  		{
	  	    	uname.setText(sp.getString("uname", null));
	  	    	upswd.setText(sp.getString("upswd", null));
	  	    	auto.setChecked(true);

	  		}
	    login.setOnClickListener(this);
	}


	public void onClick(View v) {

		if (v == login){
			String name = uname.getText().toString();
		 	String pswd = upswd.getText().toString();
		 	if(name.trim().equals("")){
    			Toast.makeText(this, "user name is empty", Toast.LENGTH_SHORT).show();
				return;
    		}
    		if(pswd.trim().equals("")){
    			Toast.makeText(this, "password not input", Toast.LENGTH_SHORT).show();
				return;
    		}
			boolean autoLogin = auto.isChecked();
			if (autoLogin)
			{
					Editor editor = sp.edit();
					editor.putString("uname", name);
					editor.putString("upswd", pswd);
					editor.putBoolean("auto", true);
					editor.commit();
			}
			else
			{
				Editor editor = sp.edit();
				editor.putString("uname", null);
				editor.putString("upswd", null);
				editor.putBoolean("auto", false);
				editor.commit();}

		}}

		// TODO Auto-generated method stub

}