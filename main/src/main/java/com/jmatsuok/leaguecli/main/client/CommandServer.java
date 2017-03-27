package com.jmatsuok.leaguecli.main.client;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Class for sending requests over HTTP to the League REST API.
 */
public class CommandServer {

    private static final Logger logger = Logger.getLogger(CommandServer.class.getName());
    private static CloseableHttpClient client;

    /**
     * Constructs a new instance
     */
    //TODO: Consider making this a singleton. We should only have one client in existence.
    public CommandServer() {
        client = HttpClients.createDefault();
    }

    /**
     * Send an HTTP GET request to the given URL
     *
     * @param url URL to send an HTTP GET request to
     * @return Server response to the request.
     * @return null if the connection fails.
     */
    public ServerResponse getRequest(String url) {
        // TODO: Error handling
        try {
            HttpGet request = new HttpGet(url);
            CloseableHttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            String entity_data = EntityUtils.toString(entity);
            logger.fine("Request returned: " + response.getStatusLine());
            logger.fine("Entity data: " + entity_data);
            ServerResponse resp = new ServerResponse(entity_data, response.getStatusLine());
            EntityUtils.consume(entity);
            return resp;
        } catch (IOException e) {
            logger.warning(e.toString());
        }
        return null;
    }

    /**
     * Performs the necessary teardown operations.
     */
    public void tearDown() {
        try {
            client.close();
        } catch (IOException e) {
            logger.warning(e.toString());
        }
    }
}
