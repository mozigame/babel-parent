/**
 *
 */
package com.babel.common.core.util;


import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class RequestLogRecord {

    static final String pattern = "yyyy-MM-dd HH:mm:ss,S";
    static final String SPLIT = "\t";

    private String requestId;

    private Date date = new Date();

    /**
     * http method
     */
    private String method;

    /**
     * http response status
     */
    private int responseStatus;

    private Map<String, String[]> parameters = Collections.emptyMap();

    private String parameterString;

    private String response;

    private String userAgent;
    /*请求路径*/
    private String reqUri;


    // TODO
    // /**
    // * 调用方ip，如果是 内网服务端调用，该ip是服务器ip， 否则该ip与用户ip一致
    // */
    // private String clientIp;

    /**
     * 用户ip,如果是内网服务器端调用，该ip是调用方通过 Api-RemoteIP机制传递的用户ip
     */
    private String ip;

    /**
     * 接口响应使用时间
     */
    private long useTime;

    /**
     * 响应大小，单位：字节
     */
    private long responseSize;

    public RequestLogRecord() {
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Map<String, String[]> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String[]> parameters) {
        this.parameters = parameters;
    }

    public long getUseTime() {
        return useTime;
    }

    public void setUseTime(long useTime) {
        this.useTime = useTime;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


    public long getResponseSize() {
        return responseSize;
    }

    public void setResponseSize(long responseSize) {
        this.responseSize = responseSize;
    }

    public String getReqUri() {
        return reqUri;
    }

    public void setReqUri(String reqUri) {
        this.reqUri = reqUri;
    }


    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }


}
