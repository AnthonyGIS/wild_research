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
package com.weng.util.mail;

import android.content.Context;

import com.weng.R;
import com.weng.util.mail.MultiMailsender.MultiMailSenderInfo;

public class SendMail {

	public static String SendMail(Context context,String content,String author){
		//这个类主要是设置邮件
	      MultiMailSenderInfo mailInfo = new MultiMailSenderInfo();
	      mailInfo.setMailServerHost(context.getResources().getString(R.string.smtp));
	      mailInfo.setMailServerPort(context.getResources().getString(R.string.port));
	      mailInfo.setValidate(true);
	      mailInfo.setUserName(context.getResources().getString(R.string.account));
	      mailInfo.setPassword(context.getResources().getString(R.string.pwd));//您的邮箱密码
	      mailInfo.setFromAddress(context.getResources().getString(R.string.account));
	      mailInfo.setToAddress(context.getResources().getString(R.string.mailto));
	      mailInfo.setSubject(context.getResources().getString(R.string.mail_subject)+" -["+author+"]");
	      mailInfo.setContent(content + "\n\n-"+author);
//	      设置CC对象
//	      String[] receivers = new String[]{context.getResources().getString(string.mailto)};
//	      String[] ccs = receivers; 
//	      mailInfo.setReceivers(receivers);
//	      mailInfo.setCcs(ccs);
	      //这个类主要来发送邮件
	      MultiMailsender sms = new MultiMailsender();
	      boolean status = sms.sendTextMail(mailInfo);//发送文体格式
//	      MultiMailsender.sendHtmlMail(mailInfo);//发送html格式
//	      MultiMailsender.sendMailtoMultiCC(mailInfo);//发送抄送
	      return status ? context.getResources().getString(R.string.scuess):context.getResources().getString(R.string.fail);
	}
}
