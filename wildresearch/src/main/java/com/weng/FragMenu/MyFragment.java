package com.weng.FragMenu;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.weng.Dialogs.TestDialog;
import com.weng.R;
import com.weng.gpstools.DataListActivity;
import com.weng.gpstools.FeedbackActivity;
import com.weng.gpstools.HelpActivity;
import com.weng.gpstools.SettingActivity;



public class MyFragment extends Fragment implements View.OnClickListener,TestDialog.onTestListener {

    private String mstrName = "";
    private String mstrHigh = "";
    //定义PopupWindow
    private PopupWindow popWindow;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fp_me_frag_layout,container,false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initUI();
    }


    private void initUI() {
        Button buttonTest = (Button) getActivity().findViewById(R.id.buttonTest);
//        buttonTest.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_LONG).show();
//            }
//        });

        buttonTest.setOnClickListener(this);
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        getActivity().getMenuInflater().inflate(R.menu.main, menu);
    }







    // 接口回调的函数
    // ------------------------------------------------------------------------------------

    public void onTestListener(int uniqueIdentifier, String strName, String strHigh) {
        if (uniqueIdentifier == 1) {
            Toast.makeText( getActivity().getApplicationContext(),
                    "姓名:" + strName + ",身高:" + strHigh, Toast.LENGTH_LONG)
                    .show();
            TextView textView1 = (TextView) getActivity().findViewById(R.id.textView1);
            textView1.setText("姓名:" + strName + ",身高:" + strHigh);
        }

        mstrName = strName;
        mstrHigh = strHigh;
    }


    public void onClick(View view) {

		if (view.getId() == R.id.buttonTest) {//                // 实例化TestDialog,可以传参数进去,例如标题,或者其他参数,还有一个唯一码.
//                TestDialog dialog = new TestDialog().newInstance("请输入", 1, mstrName, mstrHigh);
//                dialog.show(this.getActivity().getFragmentManager(), "TestDialog");
			showPopupWindow(getActivity().findViewById(R.id.buttonTest));
		}

    }


    /**
     * 显示PopupWindow弹出菜单
     */
    private void showPopupWindow(View parent){
        if (popWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.popwindow_layout,null);
            // 创建一个PopuWidow对象
            popWindow = new PopupWindow(view, LinearLayout.LayoutParams.FILL_PARENT, 200);

            ImageView menu_export=(ImageView) view.findViewById(R.id.menu_icon_export);
            ImageView menu_set=(ImageView) view.findViewById(R.id.menu_icon_set);
            ImageView menu_help=(ImageView) view.findViewById(R.id.menu_icon_help);
            ImageView menu_feedback=(ImageView) view.findViewById(R.id.menu_icon_feedback);


            menu_export.setOnClickListener(Menu_Linstener);
            menu_set.setOnClickListener(Menu_Linstener);
            menu_help.setOnClickListener(Menu_Linstener);
            menu_feedback.setOnClickListener(Menu_Linstener);

        }

        // 使其聚集 ，要想监听菜单里控件的事件就必须要调用此方法
        popWindow.setFocusable(true);
        // 设置允许在外点击消失
        popWindow.setOutsideTouchable(true);
        // 设置背景，这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置菜单显示的位置
        popWindow.showAsDropDown(parent, Gravity.CENTER, 0);

        //监听菜单的关闭事件
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            public void onDismiss() {
                //改变显示的按钮图片为正常状态
                //

            }
        });

        //监听触屏事件
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent event) {
                //改变显示的按钮图片为正常状态

                return false;
            }
        });
    }


    View.OnClickListener Menu_Linstener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent();
			int id = v.getId();
			if (id == R.id.menu_icon_export) {
				Toast.makeText(getActivity(), "Export Clicked", Toast.LENGTH_SHORT).show();
				intent.setClass(getActivity(), DataListActivity.class);
				startActivity(intent);
			}
			else if (id == R.id.menu_icon_set) {
				Toast.makeText(getActivity(), "Set Clicked", Toast.LENGTH_SHORT).show();
				intent.setClass(getActivity(), SettingActivity.class);
				startActivity(intent);
			}
			else if (id == R.id.menu_icon_help) {
				Toast.makeText(getActivity(), "Help Clicked", Toast.LENGTH_SHORT).show();
				intent.setClass(getActivity(), HelpActivity.class);
				startActivity(intent);
			}
			else if (id == R.id.menu_icon_feedback) {
				Toast.makeText(getActivity(), "Feedback Clicked", Toast.LENGTH_SHORT).show();
				intent.setClass(getActivity(), FeedbackActivity.class);
				startActivity(intent);
			}
        }

    };











}
