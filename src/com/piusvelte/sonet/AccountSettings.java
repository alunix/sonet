/*
 * Sonet - Android Social Networking Widget
 * Copyright (C) 2009 Bryan Emmanuel
 * 
 * This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *  
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Bryan Emmanuel piusvelte@gmail.com
 */
package com.piusvelte.sonet;

import static com.piusvelte.sonet.Sonet.ACTION_REFRESH;

import com.piusvelte.sonet.Sonet.Widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class AccountSettings extends Activity implements View.OnClickListener {
	private int mMessages_color_value,
	mMessages_textsize_value,
	mFriend_color_value,
	mFriend_textsize_value,
	mCreated_color_value,
	mCreated_textsize_value;
	private Button mMessages_color;
	private Button mMessages_textsize;
	private Button mFriend_color;
	private Button mFriend_textsize;
	private Button mCreated_color;
	private Button mCreated_textsize;
	private CheckBox mTime24hr;
	private boolean mUpdateWidget = false;
	private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	private long mAccountId = Sonet.INVALID_ACCOUNT_ID;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.account_preferences);
		Intent i = getIntent();
		if (i.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_ID)) mAppWidgetId = i.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		if (i.hasExtra(Sonet.EXTRA_ACCOUNT_ID)) mAccountId = i.getLongExtra(Sonet.EXTRA_ACCOUNT_ID, Sonet.INVALID_ACCOUNT_ID);
		
		mMessages_color = (Button) findViewById(R.id.messages_color);
		mMessages_textsize = (Button) findViewById(R.id.messages_textsize);
		mFriend_color = (Button) findViewById(R.id.friend_color);
		mFriend_textsize = (Button) findViewById(R.id.friend_textsize);
		mCreated_color = (Button) findViewById(R.id.created_color);
		mCreated_textsize = (Button) findViewById(R.id.created_textsize);
		mTime24hr = (CheckBox) findViewById(R.id.time24hr);

		// get this account/widgets settings, falling back on the defaults...
		Cursor c = this.getContentResolver().query(Widgets.CONTENT_URI, new String[]{Widgets._ID, Widgets.MESSAGES_COLOR, Widgets.MESSAGES_TEXTSIZE, Widgets.FRIEND_COLOR, Widgets.FRIEND_TEXTSIZE, Widgets.CREATED_COLOR, Widgets.CREATED_TEXTSIZE, Widgets.TIME24HR}, Widgets.WIDGET + "=" + mAppWidgetId + " and " + Widgets.ACCOUNT + "=" + mAccountId, null, null);
		if (c.moveToFirst()) {
			mMessages_color_value = c.getInt(c.getColumnIndex(Widgets.MESSAGES_COLOR));
			mMessages_textsize_value = c.getInt(c.getColumnIndex(Widgets.MESSAGES_TEXTSIZE));
			mFriend_color_value = c.getInt(c.getColumnIndex(Widgets.FRIEND_COLOR));
			mFriend_textsize_value = c.getInt(c.getColumnIndex(Widgets.FRIEND_TEXTSIZE));
			mCreated_color_value = c.getInt(c.getColumnIndex(Widgets.CREATED_COLOR));
			mCreated_textsize_value = c.getInt(c.getColumnIndex(Widgets.CREATED_TEXTSIZE));
			mTime24hr.setChecked(c.getInt(c.getColumnIndex(Widgets.TIME24HR)) == 1);			
		} else {
			// fall back on widget settings
			c = this.getContentResolver().query(Widgets.CONTENT_URI, new String[]{Widgets._ID, Widgets.MESSAGES_COLOR, Widgets.MESSAGES_TEXTSIZE, Widgets.FRIEND_COLOR, Widgets.FRIEND_TEXTSIZE, Widgets.CREATED_COLOR, Widgets.CREATED_TEXTSIZE, Widgets.TIME24HR}, Widgets.WIDGET + "=" + mAppWidgetId + " and " + Widgets.ACCOUNT + "=" + Sonet.INVALID_ACCOUNT_ID, null, null);
			if (c.moveToFirst()) {
				mMessages_color_value = c.getInt(c.getColumnIndex(Widgets.MESSAGES_COLOR));
				mMessages_textsize_value = c.getInt(c.getColumnIndex(Widgets.MESSAGES_TEXTSIZE));
				mFriend_color_value = c.getInt(c.getColumnIndex(Widgets.FRIEND_COLOR));
				mFriend_textsize_value = c.getInt(c.getColumnIndex(Widgets.FRIEND_TEXTSIZE));
				mCreated_color_value = c.getInt(c.getColumnIndex(Widgets.CREATED_COLOR));
				mCreated_textsize_value = c.getInt(c.getColumnIndex(Widgets.CREATED_TEXTSIZE));
				mTime24hr.setChecked(c.getInt(c.getColumnIndex(Widgets.TIME24HR)) == 1);
			} else {
				// fall back on user defaults
				c = this.getContentResolver().query(Widgets.CONTENT_URI, new String[]{Widgets._ID, Widgets.MESSAGES_COLOR, Widgets.MESSAGES_TEXTSIZE, Widgets.FRIEND_COLOR, Widgets.FRIEND_TEXTSIZE, Widgets.CREATED_COLOR, Widgets.CREATED_TEXTSIZE, Widgets.TIME24HR}, Widgets.WIDGET + "=" + AppWidgetManager.INVALID_APPWIDGET_ID, null, null);
				if (c.moveToFirst()) {
					mMessages_color_value = c.getInt(c.getColumnIndex(Widgets.MESSAGES_COLOR));
					mMessages_textsize_value = c.getInt(c.getColumnIndex(Widgets.MESSAGES_TEXTSIZE));
					mFriend_color_value = c.getInt(c.getColumnIndex(Widgets.FRIEND_COLOR));
					mFriend_textsize_value = c.getInt(c.getColumnIndex(Widgets.FRIEND_TEXTSIZE));
					mCreated_color_value = c.getInt(c.getColumnIndex(Widgets.CREATED_COLOR));
					mCreated_textsize_value = c.getInt(c.getColumnIndex(Widgets.CREATED_TEXTSIZE));
					mTime24hr.setChecked(c.getInt(c.getColumnIndex(Widgets.TIME24HR)) == 1);
				} else {
					// ultimately fall back on the app defaults
					mMessages_color_value = Integer.parseInt(getString(R.string.message_color));
					mMessages_textsize_value = Integer.parseInt(getString(R.string.messages_textsize));
					mFriend_color_value = Integer.parseInt(getString(R.string.friend_color));
					mFriend_textsize_value = Integer.parseInt(getString(R.string.friend_textsize));
					mCreated_color_value = Integer.parseInt(getString(R.string.created_color));
					mCreated_textsize_value = Integer.parseInt(getString(R.string.created_textsize));
					// initialize default settings
					ContentValues values = new ContentValues();
					values.put(Widgets.MESSAGES_COLOR, mMessages_color_value);
					values.put(Widgets.MESSAGES_TEXTSIZE, mMessages_textsize_value);
					values.put(Widgets.FRIEND_COLOR, mFriend_color_value);
					values.put(Widgets.FRIEND_TEXTSIZE, mFriend_textsize_value);
					values.put(Widgets.CREATED_COLOR, mCreated_color_value);
					values.put(Widgets.CREATED_TEXTSIZE, mCreated_textsize_value);
					values.put(Widgets.TIME24HR, false);
					this.getContentResolver().update(Widgets.CONTENT_URI, values, Widgets.WIDGET + "=" + AppWidgetManager.INVALID_APPWIDGET_ID, null);
				}
				// initialize widget settings
				ContentValues values = new ContentValues();
				values.put(Widgets.MESSAGES_COLOR, mMessages_color_value);
				values.put(Widgets.MESSAGES_TEXTSIZE, mMessages_textsize_value);
				values.put(Widgets.FRIEND_COLOR, mFriend_color_value);
				values.put(Widgets.FRIEND_TEXTSIZE, mFriend_textsize_value);
				values.put(Widgets.CREATED_COLOR, mCreated_color_value);
				values.put(Widgets.CREATED_TEXTSIZE, mCreated_textsize_value);
				values.put(Widgets.TIME24HR, false);
				this.getContentResolver().update(Widgets.CONTENT_URI, values, Widgets.WIDGET + "=" + mAppWidgetId + " and " + Widgets.ACCOUNT + "=" + Sonet.INVALID_ACCOUNT_ID, null);
			}
			// initialize account settings
			ContentValues values = new ContentValues();
			values.put(Widgets.MESSAGES_COLOR, mMessages_color_value);
			values.put(Widgets.MESSAGES_TEXTSIZE, mMessages_textsize_value);
			values.put(Widgets.FRIEND_COLOR, mFriend_color_value);
			values.put(Widgets.FRIEND_TEXTSIZE, mFriend_textsize_value);
			values.put(Widgets.CREATED_COLOR, mCreated_color_value);
			values.put(Widgets.CREATED_TEXTSIZE, mCreated_textsize_value);
			values.put(Widgets.TIME24HR, false);
			this.getContentResolver().update(Widgets.CONTENT_URI, values, Widgets.WIDGET + "=" + mAppWidgetId + " and " + Widgets.ACCOUNT + "=" + mAccountId, null);
		}
		c.close();

		mMessages_color.setOnClickListener(AccountSettings.this);
		mMessages_textsize.setOnClickListener(AccountSettings.this);
		mFriend_color.setOnClickListener(AccountSettings.this);
		mFriend_textsize.setOnClickListener(AccountSettings.this);
		mCreated_color.setOnClickListener(AccountSettings.this);
		mCreated_textsize.setOnClickListener(AccountSettings.this);
		mTime24hr.setOnCheckedChangeListener(mTime24hrListener);

	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mUpdateWidget) startService(new Intent(this, SonetService.class).setAction(ACTION_REFRESH).putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{mAppWidgetId}));
	}

	private void updateDatabase(String column, int value) {
		ContentValues values = new ContentValues();
		values.put(column, value);
		this.getContentResolver().update(Widgets.CONTENT_URI, values, Widgets.WIDGET + "=" + mAppWidgetId, null);
		mUpdateWidget = true;
	}

	ColorPickerDialog.OnColorChangedListener mHeadBackgroundColorListener =
		new ColorPickerDialog.OnColorChangedListener() {

		public void colorChanged(int color) {
			updateDatabase(Widgets.BUTTONS_BG_COLOR, color);
		}

		public void colorUpdate(int color) {}
	};

	ColorPickerDialog.OnColorChangedListener mHeadTextColorListener =
		new ColorPickerDialog.OnColorChangedListener() {

		public void colorChanged(int color) {
			updateDatabase(Widgets.BUTTONS_COLOR, color);
		}

		public void colorUpdate(int color) {}
	};

	ColorPickerDialog.OnColorChangedListener mBodyBackgroundColorListener =
		new ColorPickerDialog.OnColorChangedListener() {

		public void colorChanged(int color) {
			updateDatabase(Widgets.MESSAGES_BG_COLOR, color);
		}

		public void colorUpdate(int color) {}
	};

	ColorPickerDialog.OnColorChangedListener mBodyTextColorListener =
		new ColorPickerDialog.OnColorChangedListener() {

		public void colorChanged(int color) {
			updateDatabase(Widgets.MESSAGES_COLOR, color);
		}

		public void colorUpdate(int color) {}
	};

	ColorPickerDialog.OnColorChangedListener mFriendTextColorListener =
		new ColorPickerDialog.OnColorChangedListener() {

		public void colorChanged(int color) {
			updateDatabase(Widgets.FRIEND_COLOR, color);
		}

		public void colorUpdate(int color) {}
	};

	ColorPickerDialog.OnColorChangedListener mCreatedTextColorListener =
		new ColorPickerDialog.OnColorChangedListener() {

		public void colorChanged(int color) {
			updateDatabase(Widgets.CREATED_COLOR, color);
		}

		public void colorUpdate(int color) {}
	};

	CompoundButton.OnCheckedChangeListener mHasButtonsListener =
		new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			updateDatabase(Widgets.HASBUTTONS, isChecked ? 1 : 0);
		}
	};

	CompoundButton.OnCheckedChangeListener mTime24hrListener =
		new CompoundButton.OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			updateDatabase(Widgets.TIME24HR, isChecked ? 1 : 0);
		}
	};

	@Override
	public void onClick(View v) {
		if (v == mMessages_color) {
			ColorPickerDialog cp = new ColorPickerDialog(this, mBodyTextColorListener, this.mMessages_color_value);
			cp.show();
		} else if (v == mMessages_textsize) {
			int index = 0;
			String[] values = getResources().getStringArray(R.array.textsize_values);
			for (int i = 0; i < values.length; i++) {
				if (Integer.parseInt(values[i]) == this.mMessages_textsize_value) {
					index = i;
					break;
				}
			}
			(new AlertDialog.Builder(this))
			.setSingleChoiceItems(R.array.textsize_entries, index, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					updateDatabase(Widgets.MESSAGES_TEXTSIZE, Integer.parseInt(getResources().getStringArray(R.array.textsize_values)[which]));
					dialog.cancel();
				}
			})
			.setCancelable(true)
			.show();			
		} else if (v == mFriend_color) {
			ColorPickerDialog cp = new ColorPickerDialog(this, mFriendTextColorListener, this.mFriend_color_value);
			cp.show();
		} else if (v == mFriend_textsize) {
			int index = 0;
			String[] values = getResources().getStringArray(R.array.textsize_values);
			for (int i = 0; i < values.length; i++) {
				if (Integer.parseInt(values[i]) == this.mFriend_textsize_value) {
					index = i;
					break;
				}
			}
			(new AlertDialog.Builder(this))
			.setSingleChoiceItems(R.array.textsize_entries, index, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					updateDatabase(Widgets.FRIEND_TEXTSIZE, Integer.parseInt(getResources().getStringArray(R.array.textsize_values)[which]));
					dialog.cancel();
				}
			})
			.setCancelable(true)
			.show();			
		} else if (v == mCreated_color) {
			ColorPickerDialog cp = new ColorPickerDialog(this, mCreatedTextColorListener, this.mCreated_color_value);
			cp.show();
		} else if (v == mCreated_textsize) {
			int index = 0;
			String[] values = getResources().getStringArray(R.array.textsize_values);
			for (int i = 0; i < values.length; i++) {
				if (Integer.parseInt(values[i]) == this.mCreated_textsize_value) {
					index = i;
					break;
				}
			}
			(new AlertDialog.Builder(this))
			.setSingleChoiceItems(R.array.textsize_entries, index, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					updateDatabase(Widgets.CREATED_TEXTSIZE, Integer.parseInt(getResources().getStringArray(R.array.textsize_values)[which]));
					dialog.cancel();
				}
			})
			.setCancelable(true)
			.show();			
		}
	}
}