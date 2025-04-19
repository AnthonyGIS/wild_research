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

/**
 * CustomMenuItem class
 * 
 * This is class basically a container for you menu items.
 * 
 * @category Helper
 * @author William J Francis (w.j.francis@tx.rr.com)
 * @copyright Enjoy!
 * @version 1.0
 */
public class CustomMenuItem {

	/**
	 * Some global variables.
	 */
	private String mCaption = null;
	private int mImageResourceId = -1;
	private int mId = -1;

	/**
	 * Use this method to set the caption displayed under the icon for a menu
	 * item.
	 * 
	 * @param String
	 *            caption
	 * @return void
	 */
	public void setCaption(String caption) {
		mCaption = caption;
	}

	/**
	 * Use this method to get the caption displayed under the icon for a menu
	 * item.
	 * 
	 * @return String caption
	 */
	public String getCaption() {
		return mCaption;
	}

	/**
	 * Use this method to set the resource ID for the drawable displayed for a
	 * menu item.
	 * 
	 * @param int imageResourceId
	 * @return void
	 */
	public void setImageResourceId(int imageResourceId) {
		mImageResourceId = imageResourceId;
	}

	/**
	 * Use this method to get the resource ID for the drawable displayed for a
	 * menu item.
	 * 
	 * @return int imageResourceId
	 */
	public int getImageResourceId() {
		return mImageResourceId;
	}

	/**
	 * Use this method to set an ID to be returned when this menu item is
	 * clicked. This is really for convenience only and optional.
	 * 
	 * @param int id
	 * @return void
	 */
	public void setId(int id) {
		mId = id;
	}

	/**
	 * Use this method to get an ID assigned to a menu item. This is really for
	 * convenience only and optional.
	 * 
	 * @return int id
	 */
	public int getId() {
		return mId;
	}

}
