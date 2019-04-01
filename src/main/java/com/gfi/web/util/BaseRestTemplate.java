/**
 * 
 */
package com.gfi.web.util;

import lombok.extern.log4j.Log4j;

/**
 * @author Obi
 *
 */
@Log4j
public class BaseRestTemplate<T> {

	/*private Class<T> clazz;
	public static Client client;
	
	public final void setClazz(Class< T> clazzToSet) {
        this.clazz = clazzToSet;
    }
	
	private static Client getClient() {
        if (client == null) {
            client = ClientBuilder.newClient();
        } else {
            client.close();
            client = ClientBuilder.newClient();
        }
        return client;
    }
	
	public T postJson(String url, Object req, MultivaluedMap<String, Object> header) {
		try {
			log.info(req.toString());
			WebTarget target = getClient().target(url);
            return (T) target.request().headers(header).accept(MediaType.APPLICATION_JSON).post(Entity.entity(req, MediaType.APPLICATION_JSON), clazz);
		} catch(Exception e) {
			log.error("", e);
		} finally {
			getClient().close();
		}
		return null;
	}*/
	
}
