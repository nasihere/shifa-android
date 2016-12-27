package com.shifa.kent;

import java.security.Timestamp;
import java.sql.Date;
import java.util.Random;

import android.graphics.Color;
import android.util.Log;

public class activity_m_medica_data_search_main {
	String data;
	String rem;
	String _id;
	String fullname;
	String DataAllen;
	String DataKent;

	public activity_m_medica_data_search_main(String _id, String data,
			String rem, String SearchKeyWord, int sCount, String allen, String kent, String fullname) {
		super();
		this._id = _id;
		
		SearchKeyWord = SearchKeyWord.replace("*,*", " ");
		SearchKeyWord = SearchKeyWord.replace("*", "");
		 
		String ssk[] = SearchKeyWord.split(" ");
		
		data = data.replaceAll("\\<[^>]*>","").trim();
		allen = allen.replaceAll("\\<[^>]*>","").trim();
		kent = kent.replaceAll("\\<[^>]*>","").trim();
		int i =  -1;
		String SearchDataWord = "";
		for (int iMulC=0;iMulC <= ssk.length - 1; iMulC++)
		{
			SearchDataWord = ssk[iMulC];
			i = data.toLowerCase().indexOf(ssk[iMulC].toLowerCase());
			if (i != -1) break;
		}
		
		
		int iAllen = -1;
		String SearchDataAllen = "";
		for (int iMulC=0;iMulC <= ssk.length - 1; iMulC++)
		{
			SearchDataAllen = ssk[iMulC];
			iAllen = allen.toLowerCase().indexOf(ssk[iMulC].toLowerCase());
			if (iAllen != -1) break;
		}
		
		
		
		int iKent = -1;
		String SearchDataKent = "";
		for (int iMulC=0;iMulC <= ssk.length - 1; iMulC++)
		{
			SearchDataKent = ssk[iMulC];
			iKent = kent.toLowerCase().indexOf(ssk[iMulC].toLowerCase());
			if (iKent != -1) break;
		}
		
		
		int CutLength = 200;
		if (sCount == 1) {
			CutLength = -78888888;
		} else if (sCount >= 10 && sCount <= 2) {
			CutLength = 400;
		}

		int iStart = i - CutLength;
		int iStartAllen = iAllen - CutLength;
		int iStartKent = iKent - CutLength;
		

		int iEnd = i + CutLength;
		int iEndAllen = iAllen + CutLength;
		int iEndKent = iKent + CutLength;
		
		
		data = data.trim();
		allen = allen.trim();
		try {
			data = data.substring(iStart, iEnd);
		} catch (Exception ex) {

		}
		try {
			allen = allen.substring(iStartAllen, iEndAllen);
		} catch (Exception ex) {

		}
		try {
			kent = kent.substring(iStartKent, iEndKent);
		} catch (Exception ex) {

		}
		
		

		int j = data.toLowerCase().indexOf(">");

		int k = data.toLowerCase().indexOf("<");

		Log.e("data - k", String.valueOf(k));
		Log.e("data - j", String.valueOf(j));
		if (k == -1 && j == -1) {

		} else {
			if (k >= j) {
				data = data.substring(k);
				Log.e("data - substring", data);
			}
		}

		Log.e("data", "1");

		data = data.toLowerCase().replaceAll(SearchDataWord.toLowerCase(),
				"<b><strong><sup><font color='#009900' >" + SearchDataWord + "</font></sup></strong></b>");
		allen = allen.toLowerCase().replaceAll(SearchDataAllen.toLowerCase(),
				"<b><strong><sup><font color='#009900' >" + SearchDataAllen + "</font></sup></strong></b>");
		kent = kent.toLowerCase().replaceAll(SearchDataKent.toLowerCase(),
				"<b><strong><sup><font color='#009900' >" + SearchDataKent + "</font></sup></strong></b>");
		
		Log.e("data", "2");
		data = data.replaceAll("<br>", "");

		Log.e("data", "3");
		data = data.trim();
		allen = allen.trim();
		kent = kent.trim();
		
		Log.e("data", "4");

		if (iAllen != -1) {

			allen += "<b><font color='#989898'> - Allen </b></font>";
		} else {
			allen = "";
		}
		if (iKent != -1) {

			kent += "<b><font color='#989898'> - J.K Kent </b></font>";
		} else {
			kent = "";
		}
		
		if (i != -1) {
			data += "<b><font color='#989898'> - Boericke </b></font>";
		} else {
			data = "";
		}

		this.data = data;
		this.DataAllen = allen;
		this.DataKent = kent;
		this.fullname = fullname;
		Log.e("data", this.data);

		this.rem = rem;

	}

}