package com.shifa.kent;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class activity_m_medica_data_search_adapter extends
		ArrayAdapter<activity_m_medica_data_search_main> {
	Context context;

	int layoutResourceId;
	UserHolder holder = null;
	String SessionID = "";
	ArrayList<activity_m_medica_data_search_main> data = new ArrayList<activity_m_medica_data_search_main>();
	private static final int TAG_IMAGE_BTN_1 = 0;
	private static final int TAG_IMAGE_BTN_2 = 1;

	public activity_m_medica_data_search_adapter(Context context,
			int layoutResourceId,
			ArrayList<activity_m_medica_data_search_main> data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;

		this.context = context;

		this.data = data;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;

		if (row == null) {

			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);
			holder = new UserHolder();
			holder.txtData = (TextView) row
					.findViewById(R.id.txtm_m_search_item);
			holder.txtHeading = (TextView) row
					.findViewById(R.id.tvm_medica_search_heading);
			holder.txtDataAllen = (TextView) row
					.findViewById(R.id.txtm_m_search_item_allen);
			holder.txtDataKent = (TextView) row
					.findViewById(R.id.txtm_m_search_item_kent);
			
			holder.txtFullName= (TextView) row
					.findViewById(R.id.txtm_m_search_item_fullname);
			
			holder.img_mm_kent= (ImageView) row
					.findViewById(R.id.img_mm_kent);
			
			holder.img_mm_allen= (ImageView) row
					.findViewById(R.id.img_mm_allen);
			
			holder.img_mm_bor= (ImageView) row
					.findViewById(R.id.img_mm_bor);
			
			

		} else {
			holder = (UserHolder) row.getTag();
		}

		row.setTag(holder);
		activity_m_medica_data_search_main user = data.get(position);
		String sData = "";
		String sDataAllen = "";
		String sRem = "";
		String sId = "";
		String sDataKent  = "";
		try {

			sData = user.data;
			sRem = user.rem;
			sId = user._id;
			sDataAllen = user.DataAllen;
			sDataKent  = user.DataKent;
		} catch (Exception e) {
			sData = "---";
			sRem = "";
			Log.e("Error listview", e.toString());

		}

		holder.txtHeading.setText(sRem);
		holder.txtFullName.setText(user.fullname);
		holder.txtHeading.setTag(sId);
		if (sDataAllen.equals("")) {
			holder.txtDataAllen.setVisibility(View.GONE);
			holder.img_mm_allen.setVisibility(View.GONE);
		} else {
			holder.img_mm_allen.setVisibility(View.VISIBLE);
			holder.txtDataAllen.setVisibility(View.VISIBLE);
			holder.txtDataAllen.setText(Html.fromHtml(sDataAllen),
					TextView.BufferType.SPANNABLE);
		}

		if (sDataKent.equals("")) {
			holder.txtDataKent.setVisibility(View.GONE);
			holder.img_mm_kent.setVisibility(View.GONE);
		} else {
			holder.img_mm_kent.setVisibility(View.VISIBLE);
			holder.txtDataKent.setVisibility(View.VISIBLE);
			holder.txtDataKent.setText(Html.fromHtml(sDataKent),
					TextView.BufferType.SPANNABLE);
		}
		
		if (sData.equals("")) {
			holder.txtData.setVisibility(View.GONE);
			holder.img_mm_bor.setVisibility(View.GONE);
		} else {
			holder.img_mm_bor.setVisibility(View.VISIBLE);
			holder.txtData.setVisibility(View.VISIBLE);
			holder.txtData.setText(Html.fromHtml(sData),
					TextView.BufferType.SPANNABLE);
		}

		return row;

	}

	static class UserHolder {
		TextView txtHeading;
		TextView txtFullName;
		TextView txtData;
		TextView txtDataAllen;
		TextView txtDataKent;
		ImageView img_mm_kent;
		ImageView img_mm_allen;
		ImageView img_mm_bor;
	}

}
