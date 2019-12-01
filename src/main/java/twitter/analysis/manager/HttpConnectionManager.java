package twitter.analysis.manager;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import twitter.analysis.exception.HttpConnectionException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

@Component
public class HttpConnectionManager {

    private String url;
    private Request request;
    private Response response;
    private OkHttpClient okHttpClient;

    public HttpConnectionManager(){
        okHttpClient = new OkHttpClient.Builder().build();
    }

    public HttpConnectionManager buildUrl(
            String baseUrl,
            String pathSegment,
            Map<String,String> queryParams){

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromUriString(baseUrl)
                .pathSegment(pathSegment);

        queryParams.forEach((k,v) -> {
            v = UriUtils.encode(v, StandardCharsets.UTF_8);     //Encode params with percent encoding
            uriComponentsBuilder.queryParam(k,v);
        });

        url =  uriComponentsBuilder.build().toString();

        return this;
    }

    public HttpConnectionManager buildRequest(String userToken, String method, RequestBody requestBody){
        request = new Request.Builder()
                .url(url)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                .method(method,requestBody)
                .build();

        return this;
    }

    public JSONObject fetchJSONResponse(ArrayList<Integer> expectedResponseCodes) throws HttpConnectionException, IOException {
        response = okHttpClient.newCall(request).execute();

        if (!expectedResponseCodes.contains(response.code()))
            throw new HttpConnectionException(
                    response.message(),
                    response.code(),
                    url
            );

        return new JSONObject(response.body().string());
    }

}
