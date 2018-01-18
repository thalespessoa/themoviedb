package com.pixformance.themovie.data;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

/**
 * Created by thalespessoa on 1/17/18.
 */

public class HttpException {

    public static int ERROR_WRONG_KEY = 7;

    public static int ERROR_EMPTY = 100;
    public static int ERROR_SERVER = 101;
    public static int ERROR_UNKNOWN = 102;

    private int mCode;
    private String mMessage;

    public int getCode() {
        return mCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public HttpException(int code) {
        mCode = code;
    }

    public HttpException(Response response) {

        Map<String, Object> body;

        try {
            String src = response.errorBody().string();
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            body = mapper.readValue(src, HashMap.class);
            mMessage = body.get("status_message").toString();
            mCode = Integer.valueOf(body.get("status_code").toString());
        } catch (IOException e) {
            body = null;
            mCode = ERROR_UNKNOWN;
        } catch (Exception e) {
            mCode = ERROR_UNKNOWN;
        }

    }
}
