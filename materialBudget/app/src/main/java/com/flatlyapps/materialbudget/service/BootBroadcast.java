package com.flatlyapps.materialBudget.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootBroadcast extends BroadcastReceiver {
	
    @Override
    public void onReceive(Context context, Intent intent) {    
    	
    	if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            RecurringService.schedule(context);
        } 
    }
}