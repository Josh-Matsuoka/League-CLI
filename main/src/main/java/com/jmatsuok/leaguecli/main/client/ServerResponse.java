package com.jmatsuok.leaguecli.main.client;

import org.apache.http.StatusLine;

/**
 * Wrapper Class for containing responses from the Riot web api
 */
public class ServerResponse {

    private String JSONData;
    private StatusLine statusLine;

    /**
     * Constructs a new instance
     *
     * @param JSONData JSON representation of response to request
     * @param statusLine Status line returned by the server
     */
    public ServerResponse(String JSONData, StatusLine statusLine) {
        this.JSONData = JSONData;
        this.statusLine = statusLine;
    }

    /**
     * Retrieve a JSON representation of the response to the request
     *
     * @return JSON representation of the response
     */
    public String getJSONData() {
        return this.JSONData;
    }

    /**
     * Retrieve the status line
     *
     * @return status line returned by the server
     */
    public StatusLine getStatusLine() {
        return this.statusLine;
    }
}
