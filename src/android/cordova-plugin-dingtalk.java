package com.wfl.corodva.dingtalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.dingtalk.share.ddsharemodule.DDShareApiFactory;
import com.android.dingtalk.share.ddsharemodule.IDDShareApi;
import com.android.dingtalk.share.ddsharemodule.message.SendAuth;
import com.android.dingtalk.share.ddsharemodule.message.DDImageMessage;
import com.android.dingtalk.share.ddsharemodule.message.DDMediaMessage;
import com.android.dingtalk.share.ddsharemodule.message.DDTextMessage;
import com.android.dingtalk.share.ddsharemodule.message.DDWebpageMessage;
import com.android.dingtalk.share.ddsharemodule.message.DDZhiFuBaoMesseage;
import com.android.dingtalk.share.ddsharemodule.message.SendMessageToDD;
import com.android.dingtalk.share.ddsharemodule.plugin.SignatureCheck;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPreferences;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaArgs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.text.TextUtils;
/**
 * This class echoes a string called from JavaScript.
 */
public class cordova-plugin-dingtalk extends CordovaPlugin {
    public static final String TAG = "Cordova.Plugin.Dingtalk";
    public static final String DINGDING_PROPERTY_KEY = "dingtalk_appid";
    public static final String PREFS_NAME = "Cordova.Plugin.Dingtalk";
    public static final int ERROR_REQUEST_FAIL = -1;
    public static final int ERROR_DINGDINDG_NOT_INSTALLED = -2;
    public static final String ERROR_INVALID_PARAMETERS = "参数格式错误";
    public static final int ERROR_DINGDINDG_NOT_SUPPORT = -3;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("coolMethod")) {
            String message = args.getString(0);
            this.coolMethod(message, callbackContext);
            return true;
        }
        return false;
    }

    private void coolMethod(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            callbackContext.success(message);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
