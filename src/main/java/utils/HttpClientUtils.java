package utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * HTTP请求工具类
 */
public class HttpClientUtils {
    //参数定义
    private static HttpGet httpGet = null;
    private static HttpPost httpPost = null;
    private static HttpEntity httpEntity = null;

    private static HttpHost proxy = new HttpHost("127.0.0.1",8888,"http");
    //配置信息
    private static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(20000)
//            .setProxy(proxy)
            .build();
    //连接池最大连接数
    private static PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
    static {
        manager.setMaxTotal(1000);
    }

    // 实例化CloseableHttpClient对象
    private static CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(manager)
            .setDefaultRequestConfig(requestConfig).build();


    /**
     * POST请求，传参为FORM表单形式（MAP）
     * @param url：请求地址
     * @param params：请求参数
     * @param header：请求头
     * @return
     */
    public static String doPost(String url, Map<String, Object> params, Map<String, Object> header){
        try {
            httpPost = new HttpPost();
            httpPost.setURI(new URI(url));

            // 设置头部
            if (header != null) {
                Set<String> headerSet = header.keySet();
                for (String key : headerSet) {
                    httpPost.addHeader(key, header.get(key).toString());
                }
            }
            // 设置参数
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<String> keys = params.keySet();
            for (String key : keys) {
                Object value = params.get(key);
                nvps.add(new BasicNameValuePair(key, value.toString()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if(code == HttpStatus.SC_OK){// 请求成功
                httpEntity = response.getEntity();
                String result = EntityUtils.toString(httpEntity, "utf-8");
                return result;
            }else {
                System.out.println("状态码：" + code);
                return null;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                EntityUtils.consume(httpEntity);
                //另外一种关闭连接的方式
//                httpPost.releaseConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //重载
    public static String doPost(String url, Map<String, Object> params){
        return doPost(url,params,null);
    }

    /**
     * POST请求，传参为JSON格式
     * @param url：请求地址
     * @param params：请求参数
     * @param header：请求头
     * @return
     */
    public static String doPostJson(String url, String params, Map<String, Object> header){
        httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        StringEntity entity = new StringEntity(params,"UTF-8");
        httpPost.setEntity(entity);

        try {
            // 设置头部
            if (header != null) {
                Set<String> headerSet = header.keySet();
                for (String key : headerSet) {
                    httpPost.addHeader(key, header.get(key).toString());
                }
            }
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int code = response.getStatusLine().getStatusCode();
            if(code == HttpStatus.SC_OK){
                httpEntity = response.getEntity();
                String jsonStr = EntityUtils.toString(httpEntity);
                return jsonStr;
            }else {
                System.out.println("状态码：" + code);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                EntityUtils.consume(httpEntity);
                //另外一种关闭连接的方式
//                httpPost.releaseConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //重载
    public static String doPostJson(String url, String params){
        return doPostJson(url,params,null);
    }

    /**
     * GET请求
     * @param url：请求地址
     * @param header：请求头
     * @return：请求结果返回
     */
    public static String doGet(String url, Map<String,Object> header){
        // 构造请求
        httpGet = new HttpGet(url);
        try {
            if(header != null){
                Set<String> headerSet = header.keySet();
                for(String key : headerSet){
                    httpGet.addHeader(key,header.get(key).toString());
                }
            }
            // 丢到池子里面
            CloseableHttpResponse response = httpClient.execute(httpGet);
            httpEntity = response.getEntity();
            //处理返回结果
            String result = EntityUtils.toString(httpEntity,"UTF-8");
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //关闭连接
                EntityUtils.consume(httpEntity);
                //另外一种关闭连接的方式
//                httpGet.releaseConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //重载
    public static String doGet(String url){
        return doGet(url,null);
    }

    public static void main(String[] args) {
        String str="method=loginMobile&loginname=test1&loginpass=test1";
        Map<String, Object> params = DataUtils.covertStringToMp(str);

		String result = doGet("http://www.baidu.com", params);
		System.out.println(result);
		String result01 = doPost("https://www.baidu.com", params);
        System.out.println(result01);
    }
}
