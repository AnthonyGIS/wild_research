/*
Copyright 2012 fangqing.fan@hotmail.com

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package com.weng.gpstools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weng.R;
import com.weng.util.mail.SendMail;

public class FeedbackActivity extends Activity implements OnClickListener {

    private Button back;
    private Button send;
    private EditText author;
    private EditText content;

    public static String str_anthor = "";
    public static String str_content = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback);

        author = (EditText) findViewById(R.id.author);
        content = (EditText) findViewById(R.id.content);
        back = (Button) findViewById(R.id.back);
        send = (Button) findViewById(R.id.send);
        back.setOnClickListener(this);
        send.setOnClickListener(this);

    }

    public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.back) {
			this.finish(); // 返回的话直接退出
		}
		else if (id == R.id.send) {
			this.str_content = content.getText().toString();
			this.str_anthor = author.getText().toString();
			new SendMailAsyncTask().execute(); // 执行发送任务
		}
    }


    private class SendMailAsyncTask extends AsyncTask<Object, Void, String> {

        private ProgressDialog pd;
        protected void onPostExecute(String string) {
            pd.cancel();
            Toast.makeText(FeedbackActivity.this, string, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(FeedbackActivity.this);
            pd.setMessage("正在发送反馈信息...");
            pd.setCancelable(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... params) {
            return SendMail.SendMail(FeedbackActivity.this, str_content , str_anthor);
        }
    }
}
