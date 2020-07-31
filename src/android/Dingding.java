package com.wfl.corodva.dingtalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.text.TextUtils;

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

/**
 * @author scofieldwenwen
 */
public class Dingding extends CordovaPlugin {
    public static final String TAG = "Cordova.Plugin.Dingding";

    public static final String DINGDING_PROPERTY_KEY = "dingtalk_appid";
    public static final String PREFS_NAME = "Cordova.Plugin.Dingding";
    public static final int ERROR_REQUEST_FAIL = -1;
    public static final int ERROR_DINGDINDG_NOT_INSTALLED = -2;
    public static final String ERROR_INVALID_PARAMETERS = "参数格式错误";
    public static final int ERROR_DINGDINDG_NOT_SUPPORT = -3;

    private static CallbackContext currentCallbackContext;
    private static IDDShareApi iddShareApi;

    private static String appId;
    private static CordovaPreferences ddPreferences;

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();

        String id = getAppId(preferences);
        saveAppId(cordova.getActivity(), id);
        initDdAPI();

        Log.d(TAG, "plugin initialized.");
    }
    /**
     * 注册钉钉API
     */
    private void initDdAPI() {
        IDDShareApi api = getDdAPI(cordova.getActivity());
        if(ddPreferences == null) {
            ddPreferences = preferences;
        }
        if (api != null) {
            api.registerApp(getAppId(preferences));
        }
    }
    /**
     * 获取钉钉API
     */
    public static IDDShareApi getDdAPI(Context ctx) {
        if (iddShareApi == null) {
            String appId = getSavedAppId(ctx);
            Log.d(TAG, "getDdAPI appId = " + appId);
            if (!appId.isEmpty()) {
                iddShareApi = DDShareApiFactory.createDDShareApi(ctx, appId, true);
            }
        }
        return iddShareApi;
    }


    @Override
    public boolean execute(String action, CordovaArgs args, CallbackContext callbackContext) throws JSONException {
        Log.d(TAG, String.format("%s is called. Callback ID: %s.", action, callbackContext.getCallbackId()));
        if (action.equals("isDingTalkInstalled")) {
            return this.isInstalled(callbackContext);
        } else if (action.equals("login")) {
            return this.login(callbackContext);
        } else if(action.equals("shareTextObject")) {
            return this.shareTextObject(args, callbackContext);
        } else if(action.equals("shareImageObject")) {
            return this.shareImageObject(args, callbackContext);
        } else if (action.equals("shareWebObject")) {
            return this.shareWebObject(args, callbackContext);
        }
        return false;
    }
    /**
     * 分享文本
     */
    private boolean shareTextObject(CordovaArgs args, CallbackContext callbackContext) {
        final IDDShareApi api = getDdAPI(cordova.getActivity());

        // check app is installed
        boolean isInstalled = api.isDDAppInstalled();
        Log.d(TAG, "isInstalled=" + isInstalled);
        if (!isInstalled) {
            Log.d(TAG, "未安装钉钉，请先安装");
            callbackContext.error(ERROR_DINGDINDG_NOT_INSTALLED);
            return true;
        }
        //检查参数是否正确
        final JSONObject params;
        try {
            params = args.getJSONObject(0);
        } catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
        //初始化一个DDTextMessage对象
        DDTextMessage textObject = new DDTextMessage();
        //用DDTextMessage对象初始化一个DDMediaMessage对象
        DDMediaMessage mediaMessage = new DDMediaMessage();
        //构造一个Req
        SendMessageToDD.Req req = new SendMessageToDD.Req();
        try {
            textObject.mText = params.has("text") ? params.getString("text") : "text";
            mediaMessage.mMediaObject = textObject;
        }catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
        req.mMediaMessage = mediaMessage;
        if (api.sendReq(req)) {
            Log.i(TAG, "Share request has been sent successfully.");
            sendNoResultPluginResult(callbackContext);
        } else {
            Log.i(TAG, "Share request has been sent unsuccessfully.");
            callbackContext.error(ERROR_REQUEST_FAIL);
        }
        return true;
    }
    /**
     * 分享图片
     */
    private boolean shareImageObject(CordovaArgs args, CallbackContext callbackContext) {
        final IDDShareApi api = getDdAPI(cordova.getActivity());

        // check app is installed
        boolean isInstalled = api.isDDAppInstalled();
        Log.d(TAG, "isInstalled=" + isInstalled);
        if (!isInstalled) {
            Log.d(TAG, "未安装钉钉，请先安装");
            callbackContext.error(ERROR_DINGDINDG_NOT_INSTALLED);
            return true;
        }
        //检查参数是否正确
        final JSONObject params;
        try {
            params = args.getJSONObject(0);
        } catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
        //初始化一个DDImageMessage
        DDImageMessage imageObject = new DDImageMessage();
        //构造一个mMediaObject对象
        DDMediaMessage mediaMessage = new DDMediaMessage();
        //构造一个Req
        SendMessageToDD.Req req = new SendMessageToDD.Req();
        try {
            imageObject.mImageUrl = params.has("imageURL") ? params.getString("imageURL") : "https://oral2.s3.cn-north-1.amazonaws.com.cn/logo/icon-72%402x.jpg";
            mediaMessage.mMediaObject = imageObject;
        }catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
        req.mMediaMessage = mediaMessage;
        if (api.sendReq(req)) {
            Log.i(TAG, "Share request has been sent successfully.");
            sendNoResultPluginResult(callbackContext);
        } else {
            Log.i(TAG, "Share request has been sent unsuccessfully.");
            callbackContext.error(ERROR_REQUEST_FAIL);
        }
        return true;
    }
    /**
     * 分享网页消息
     */
    private boolean shareWebObject(CordovaArgs args, CallbackContext callbackContext) {
        final IDDShareApi api = getDdAPI(cordova.getActivity());

        // check app is installed
        boolean isInstalled = api.isDDAppInstalled();
        Log.d(TAG, "isInstalled=" + isInstalled);
        if (!isInstalled) {
            Log.d(TAG, "未安装钉钉，请先安装");
            callbackContext.error(ERROR_DINGDINDG_NOT_INSTALLED);
            return true;
        }
        //检查参数是否正确
        final JSONObject params;
        try {
            params = args.getJSONObject(0);
        } catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
        //初始化一个DDWebpageMessage并填充网页链接地址
        DDWebpageMessage webPageObject = new DDWebpageMessage();
        //构造一个DDMediaMessage对象
        DDMediaMessage webMessage = new DDMediaMessage();
        //构造一个Req
        SendMessageToDD.Req webReq = new SendMessageToDD.Req();
        try {
            webPageObject.mUrl = params.has("pageURL") ? params.getString("pageURL") : "http://www.baidu.com";
            webMessage.mMediaObject = webPageObject;

            //填充网页分享必需参数，开发者需按照自己的数据进行填充
            webMessage.mTitle = params.has("title") ? params.getString("title") : "title";
            webMessage.mContent = params.has("messageDescription") ? params.getString("messageDescription") : "messageDescription";
            webMessage.mThumbUrl =params.has("thumbURL") ? params.getString("thumbURL") : "https://oral2.s3.cn-north-1.amazonaws.com.cn/logo/icon-72%402x.jpg";
        }catch (JSONException e) {
            callbackContext.error(ERROR_INVALID_PARAMETERS);
            return true;
        }
        webReq.mMediaMessage = webMessage;
        if (api.sendReq(webReq)) {
            Log.i(TAG, "Share request has been sent successfully.");
            sendNoResultPluginResult(callbackContext);
        } else {
            Log.i(TAG, "Share request has been sent unsuccessfully.");
            callbackContext.error(ERROR_REQUEST_FAIL);
        }
        return true;

    }
    /**
     * 
     * DingDing Login
     *
     * @param callbackContext
     * @return
     */
    private boolean login(CallbackContext callbackContext) {
        final IDDShareApi api = getDdAPI(cordova.getActivity());

        // check app is installed
        boolean isInstalled = api.isDDAppInstalled();
        Log.d(TAG, "isInstalled=" + isInstalled);
        if (!isInstalled) {
            Log.d(TAG, "未安装钉钉，请先安装");
            callbackContext.error(ERROR_DINGDINDG_NOT_INSTALLED);
            return true;
        }

        // check app version is support
        SendAuth.Req req = new SendAuth.Req();
        boolean isSupport = req.getSupportVersion() <= api.getDDSupportAPI();
        Log.d(TAG, "isSupport=" + isSupport);
        if (!isSupport) {
            Log.d(TAG, "钉钉版本过低，不支持登录授权");
            callbackContext.error(ERROR_DINGDINDG_NOT_SUPPORT);
            return true;
        }

        req.scope = SendAuth.Req.SNS_LOGIN;
        // req.state = "Auth";
        if (api.sendReq(req)) {
            Log.i(TAG, "Auth request has been sent successfully.");
            // send no result
            sendNoResultPluginResult(callbackContext);
        } else {
            Log.i(TAG, "Auth request has been sent unsuccessfully.");
            // send error
            callbackContext.error(ERROR_REQUEST_FAIL);
        }

        return true;
    }
    private static final String ONLINE_PACKAGE_NAME = "com.wflischool.oralv3";//todo:将值替换为在钉钉开放平台上申请时的packageName
    private static final String ONLINE_APP_ID = "dingoamwt68eyxd4gqslgh";//todo:将值替换为在钉钉开放平台上申请时平台生成的appId
    private static final String ONLINE_SIGNATURE = "37b9e5990e251f434ffd15f1c454b028";//todo:将值替换为在钉钉开放平台上申请时的signature
    /**
     * check app is installed
     *
     * @param callbackContext
     * @return
     */
    private boolean isInstalled(CallbackContext callbackContext) {
        final IDDShareApi api = getDdAPI(cordova.getActivity());
        boolean isInstalled = api.isDDAppInstalled();
        Log.d(TAG, "isInstalled=" + isInstalled);
        if (!isInstalled) {
            callbackContext.success(getCallbackMsg(0,"未安装钉钉"));
        } else {
            //校验分享到钉钉的参数是否有效
            if(!TextUtils.equals(ONLINE_APP_ID, appId)){
                callbackContext.error(getCallbackMsg(2,"APP_ID 与生成的不匹配"+appId));
                return true;
            }
            callbackContext.success(getCallbackMsg(1,"已安装钉钉"+appId));
        }
        return true;
    }

    /**
     * use CallbackContext In  DDShareActivity
     *
     * @return
     */
    public static CallbackContext getCurrentCallbackContext() {
        return currentCallbackContext;
    }

    /**
     * set Callback keep , in DDShareActivity will callBack result
     *
     * @param callbackContext
     */
    private void sendNoResultPluginResult(CallbackContext callbackContext) {
        // save current callback context
        currentCallbackContext = callbackContext;
        // send no result and keep callback
        PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
        result.setKeepCallback(true);
        callbackContext.sendPluginResult(result);
    }

    public static String getAppId(CordovaPreferences f_preferences) {
        if (appId == null) {
            if(f_preferences != null) {
                appId = f_preferences.getString(DINGDING_PROPERTY_KEY, "");
            }else if(ddPreferences != null){
                appId = ddPreferences.getString(DINGDING_PROPERTY_KEY, "");
            }
        }
        Log.d(TAG, "getAppId appId = " + appId);
        return appId;
    }


    /**
     * Get saved app id
     *
     * @param ctx
     * @return
     */
    public static String getSavedAppId(Context ctx) {
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(DINGDING_PROPERTY_KEY, "");
    }

    /**
     * Save app appId into SharedPreferences
     *
     * @param ctx
     * @param appId
     */
    public static void saveAppId(Context ctx, String appId) {
        if (appId != null && appId.isEmpty()) {
            return;
        }
        SharedPreferences settings = ctx.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(DINGDING_PROPERTY_KEY, appId);
        editor.commit();
    }

    /**
     * Callback json
     *
     * @param code
     * @param msg
     * @return Callback json
     */
    public String getCallbackMsg(int code, String msg){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("code", code);
            jsonObject.put("msg", msg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
