package com.netmi.baselibrary.data.base;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.netmi.baselibrary.data.Constant;
import com.netmi.baselibrary.data.api.LoginApi;
import com.netmi.baselibrary.data.cache.AccessTokenCache;
import com.netmi.baselibrary.data.cache.LoginInfoCache;
import com.netmi.baselibrary.data.entity.AccessToken;
import com.netmi.baselibrary.data.entity.BaseData;
import com.netmi.baselibrary.data.entity.UserInfoEntity;
import com.netmi.baselibrary.utils.DateUtil;
import com.netmi.baselibrary.utils.Logs;
import com.netmi.baselibrary.utils.MD5;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Call;

import static com.netmi.baselibrary.data.base.AesGsonConverterFactory.CONTENT_TYPE_AES;
import static com.netmi.baselibrary.data.base.AesGsonConverterFactory.CONTENT_TYPE_JSON;

/**
 * 类描述：
 * 创建人：Simple
 * 创建时间：2017/9/5 14:08
 * 修改备注：
 */
public class AccessTokenInterceptor implements Interceptor {

    private static final String TAG = "AccessTokenInterceptor";

    private static final String CONTENT_SUB_TYPE_JSON = "json";

    private static final String JSON_ACCESS_TOKEN = "\"token\":" + "\"" + "?" + "\"" + ",\"token_type\":1}";

    /**
     * synchronized 加入同步，避免多线程进入，产生线程同步问题：多次刷新token，token数据出错
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public synchronized Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();
        String postBody = "";

        if (canInjectIntoBody(request)) {
            AccessToken accessT = AccessTokenCache.get();
            String accessToken = accessT.getToken();
            if (AccessTokenCache.isTokenExpired()) {
                Logs.d(TAG, "token 过期");
                accessToken = synchRefreshToken(accessT.getRefresh_token(), accessT.getRefresh_end_time());
            }
            Logs.d(TAG, "accessToken :" + accessT.toString());
            RequestBody requestBody = request.body();

            //JSON 实体提交，加入access_token
            if (TextUtils.equals(requestBody.contentType().subtype(), CONTENT_SUB_TYPE_JSON)) {
                String json = bodyToString(requestBody);
                //access_token 非空，加入token
                if (!TextUtils.isEmpty(accessToken)) {
                    json = (TextUtils.isEmpty(json) ? "{" : json.substring(0, json.length() - 1) + ",") + JSON_ACCESS_TOKEN.replace("?", accessToken);
                }
                postRequest(requestBuilder, MediaType.parse(CONTENT_TYPE_JSON), json);
                postBody = json;
            }

            //POST 表单提交，加入access_token
            else {
                String postBodyString = bodyToString(requestBody);
                //access_token 非空，加入token
                if (!TextUtils.isEmpty(accessToken)) {
                    FormBody.Builder formBodyBuilder = new FormBody.Builder();
                    formBodyBuilder.add("token", accessToken);
                    formBodyBuilder.add("token_type", "1");
                    RequestBody formBody = formBodyBuilder.build();
                    postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
                    postRequest(requestBuilder, requestBody.contentType(), postBodyString);
                }
                postBody = string2JSON(postBodyString).toString();
            }

            request = requestBuilder.header("Content-Type", AesGsonConverterFactory.ENCRYPTION ? CONTENT_TYPE_AES : CONTENT_TYPE_JSON).build();
        }

        Response response = chain.proceed(request);
        String responseBody = response.peekBody(1024 * 1024).string();
        Logs.e(TAG, String.format("%s \n接口url:%s\n请求json:%s\n返回json:%s",
                canInjectIntoBody(request) ? "接口内容" : "没有参数",
                response.request().url(),
                postBody + (AesGsonConverterFactory.ENCRYPTION ? "\n请求密文：" + Aes128CdcUtils.getInstance().encrypt(postBody) : ""),
                (AesGsonConverterFactory.ENCRYPTION ? Aes128CdcUtils.getInstance().decrypt(responseBody) + "\n返回密文：" : "") + responseBody));

        return response;
    }

    private void postRequest(Request.Builder builder, MediaType contentType, String content) {
        if (AesGsonConverterFactory.ENCRYPTION) {
            String encryptContent;
            if (TextUtils.equals(contentType.subtype(), CONTENT_SUB_TYPE_JSON)) {
                encryptContent = Aes128CdcUtils.getInstance().encrypt(content);
            } else {
                encryptContent = Aes128CdcUtils.getInstance().encrypt(string2JSON(content).toString());
            }
            builder.post(RequestBody.create(!TextUtils.isEmpty(encryptContent) ? null : contentType, !TextUtils.isEmpty(encryptContent) ? encryptContent : content));
        } else {
            builder.post(RequestBody.create(contentType, content));
        }
    }

    /**
     * 同步请求方式，获取最新的Token
     */
    private String synchRefreshToken(String refreshToken, String refreshEndTime) throws IOException {

        Logs.d(TAG, "into synchRefreshToken()");

        //没有旧的token，尝试自动登录
        if (TextUtils.isEmpty(refreshToken)
                || TextUtils.isEmpty(refreshEndTime)
                || DateUtil.strToLong(refreshEndTime) < System.currentTimeMillis()) {
            return synchAutoLogin();
        }

        Call<BaseData<UserInfoEntity>> call = synchToken(refreshToken, null, null, null, null);
        retrofit2.Response<BaseData<UserInfoEntity>> baseDataResponse = call.execute();

        if (baseDataResponse.isSuccessful() && baseDataResponse.body() != null) {
            BaseData<UserInfoEntity> baseData = baseDataResponse.body();
            //token刷新成功
            if (baseData.getErrcode() == Constant.SUCCESS_CODE) {
                AccessToken accessToken = baseData.getData().getToken();
                if (accessToken != null) {
                    AccessTokenCache.put(accessToken);
                    return accessToken.getToken();
                }
                //token刷新失败，尝试自动登录
                else {
                    return synchAutoLogin();
                }
            }
            //token刷新失败，尝试自动登录
            else {
                return synchAutoLogin();
            }
        }
        //token刷新失败，尝试自动登录
        else {
            return synchAutoLogin();
        }
    }

    /**
     * 同步请求方式，获取最新的Token
     */
    private String synchAutoLogin() throws IOException {

        Logs.d(TAG, "into synchAutoLogin()");
        final String login = LoginInfoCache.get().getLogin();
        final String password = LoginInfoCache.get().getPassword();
        final String openid = LoginInfoCache.get().getOpenid();
        final String unionid = LoginInfoCache.get().getUnionId();
        if ((TextUtils.isEmpty(login) || TextUtils.isEmpty(password))
                && TextUtils.isEmpty(openid)) {
            return "";
        }
        Call<BaseData<UserInfoEntity>> call = synchToken(null, login, password, openid, unionid);
        retrofit2.Response<BaseData<UserInfoEntity>> baseDataResponse = call.execute();

        if (baseDataResponse.isSuccessful() && baseDataResponse.body() != null) {
            BaseData<UserInfoEntity> baseData = baseDataResponse.body();
            //token刷新成功
            if (baseData.getErrcode() == Constant.SUCCESS_CODE) {
                AccessToken accessToken = baseData.getData().getToken();
                if (accessToken != null) {
                    AccessTokenCache.put(accessToken);
                    return accessToken.getToken();
                }
                //token刷新失败
                else {
                    return "";
                }
            }
            //token刷新失败
            else {
                //已自动登出，请重新登陆
                if (baseData.getErrcode() == Constant.TOKEN_OUT) {
                    return "" + Constant.TOKEN_OUT;
                } else {
                    return "";
                }
            }
        }
        //token刷新失败
        else {
            return "";
        }
    }

    private Call<BaseData<UserInfoEntity>> synchToken(String refreshToken, String login, String password, String openid, String unionid) {
        AccessTokenCache.clear();
        if (TextUtils.isEmpty(refreshToken)) {
            return RetrofitApiFactory.createApi(LoginApi.class).doLogin(login, MD5.GetMD5Code(password), openid, unionid, TextUtils.isEmpty(password) ? "login_wechat" : "login_phone");
        } else {
            return RetrofitApiFactory.createApi(LoginApi.class).doRefreshToken(refreshToken);
        }
    }

    /**
     * 将请求提转为String
     *
     * @param request
     * @return
     */
    @NonNull
    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "";
        }
    }

    /**
     * 请求是否可以注入参数
     *
     * @param request
     * @return
     */
    private boolean canInjectIntoBody(Request request) {

        if (request == null) {
            return false;
        }

        if (!TextUtils.equals(request.method(), "POST")) {
            return false;
        }

        RequestBody body = request.body();
        if (body == null) {
            return false;
        }

        MediaType mediaType = body.contentType();
        if (mediaType == null) {
            return false;
        }

        //针对 POST 表单 x-www-form-urlencoded 和 POST 实体 json
        if (!TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded") && !TextUtils.equals(mediaType.subtype(), "json")) {
            return false;
        }
        return true;
    }

    /**
     * 将表单提交的内容转为JSON查看
     */
    private JSONObject string2JSON(String str) {
        JSONObject json = new JSONObject();
        try {
            String[] arrStr = str.split("&");
            for (String formString : arrStr) {
                String[] arrKeyValue = formString.split("=");

                if (arrKeyValue.length < 2 || TextUtils.isEmpty(arrKeyValue[0]) || TextUtils.isEmpty(arrKeyValue[1])) {
                    continue;
                }

                String key = getUtf8Text(arrKeyValue[0]).replace("[]", "");
                String value = getUtf8Text(arrKeyValue[1]);
                if (json.isNull(key)) {
                    if (getUtf8Text(arrKeyValue[0]).contains("[]")) {
                        JSONArray jsonArray = new JSONArray();
                        jsonArray.put(value);
                        json.put(key, jsonArray);
                    } else {
                        json.put(key, value);
                    }
                } else {
                    JSONArray jsonArray = null;
                    if (json.get(key) instanceof JSONArray) {
                        jsonArray = json.getJSONArray(key);
                    } else {
                        jsonArray = new JSONArray();
                        jsonArray.put(json.get(key));
                    }
                    jsonArray.put(value);
                    json.put(key, jsonArray);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    private String getUtf8Text(String content) {
        try {
            return URLDecoder.decode(content, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }

}
