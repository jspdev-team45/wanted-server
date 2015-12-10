/**
 * @author Xi Lin
 */

package com.wanted.ws.remote;

public interface SocketClientInterface {
	boolean openConnection();
    void handleSession();
    void closeSession();
}
