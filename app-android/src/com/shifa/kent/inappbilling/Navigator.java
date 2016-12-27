package com.shifa.kent.inappbilling;

import android.app.Activity;
import android.content.Intent;

import com.shifa.kent.PurchaseItemActivity;
import com.shifa.kent.login;

/**
 * A wrapper around the Android Intent mechanism
 * 
 * @author Blundell
 * 
 */
public class Navigator {

    public static final int REQUEST_PASSPORT_PURCHASE = 2012;

    private final Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    public void toMainActivity() {
        Intent intent = new Intent(activity, login.class);
        activity.startActivity(intent);
    }

    public void toPurchasePassportActivityForResult() {
        Intent intent = new Intent(activity, PurchaseItemActivity.class);
        activity.startActivityForResult(intent, REQUEST_PASSPORT_PURCHASE);
    }

}
