package com.integro.eggpro;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    public static boolean isFirstTimeNews(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("NEWS_HINT", MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("news", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("news", true);
            editor.commit();
        }
        return !ranBefore;
    }
}
