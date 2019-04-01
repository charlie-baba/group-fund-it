/**
 * 
 */
package com.gfi.web.util;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
public class RestUtil<T> {

	private Class<T> clazz;
	public static Client client;

	public final void setClazz(Class< T> clazzToSet) {
        this.clazz = clazzToSet;
    }
	
	private static Client getClient() {
		ClientConfig clientConfig = new DefaultClientConfig();
        clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
        clientConfig.getClasses().add(JacksonJsonProvider.class);
        
        if (client == null) {
            client = Client.create(clientConfig);
        } else {
            client.destroy();
            client = Client.create(clientConfig);
        }
        return client;
    }

	public T postJson(String url, Object req, String authorization) {
		try {
			WebResource webResource = getClient().resource(url);
			return (T) webResource.accept(MediaType.APPLICATION_JSON)
					.header("Authorization", authorization)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
					.header("Content-Type", MediaType.APPLICATION_JSON).post(clazz, req);
		} catch(Exception e) {
			log.error("Unable to post json: ", e);
			return null;
		} finally {
			client.destroy();
		}
	}
	
	public T postJson(String url, String authorization) {
		try {
			WebResource webResource = getClient().resource(url);
			return (T) webResource.accept(MediaType.APPLICATION_JSON)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
					.header("Authorization", authorization).post(clazz);
		} catch(Exception e) {
			log.error("Unable to post json: ", e);
			return null;
		} finally {
			client.destroy();
		}
	}
	
	public T getJson(String url, String authorization) {
		try {
			WebResource webResource = getClient().resource(url);
			return (T) webResource.accept(MediaType.APPLICATION_JSON)
					.header("Authorization", authorization)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
					.header("Content-Type", MediaType.APPLICATION_JSON).get(clazz);
		} catch(Exception e) {
			log.error("Unable to get json: ", e);
			return null;
		} finally {
			client.destroy();
		}
	}
	
}
