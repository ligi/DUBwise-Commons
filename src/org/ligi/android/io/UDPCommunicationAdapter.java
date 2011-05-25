/*                                                                                                                          
 * This software is free software; you can redistribute it and/or modify                                                     
 * it under the terms of the GNU General Public License as published by                                                     
 * the Free Software Foundation; either version 3 of the License, or                                                        
 * (at your option) any later version.                                                                                      
 *                                                                                                                          
 * This program is distributed in the hope that it will be useful, but                                                      
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY                                               
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License                                                  
 * for more details.                                                                                                        
 *                                                                                                                          
 * You should have received a copy of the GNU General Public License along                                                  
 * with this program; if not, write to the Free Software Foundation, Inc.,                                                  
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA                                                                    
 */
package org.ligi.android.io;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.ligi.java.io.CommunicationAdapterInterface;
import org.ligi.tracedroid.logging.Log;

/**
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class UDPCommunicationAdapter implements
		CommunicationAdapterInterface {
	private int port;
	private String host;
	private DatagramSocket ds;
	
	/**
	 * 
	 * @param mac
	 */
	public UDPCommunicationAdapter(String host,int port) {
		this.host=host;
		this.port=port;
	}

	public void connect() {
		try {
		    ds=new DatagramSocket();
		    ds.connect(InetAddress.getByName(host), port);
		    ds.setSoTimeout(100);
		}
		catch(Exception e) { Log.w(""+e); }
	}

	public void disconnect() {
	    ds.close();
	}
	
	public int available() {
	    return 0; // TODO implement me
	}

	public void flush() throws IOException {
	 // TODO implement me
	}

	public int read(byte[] b, int offset, int length) throws IOException {
	    return 0; // TODO implement me
	}

	public void write(byte[] buffer, int offset, int count) throws IOException {
	 // TODO implement me
	}
	
	public void write(byte[] buffer)  throws IOException  {
	 // TODO implement me
	}

	public void write(int oneByte) throws IOException  {
	 // TODO implement me
	}

	public int read() throws IOException {
	 // TODO implement me
	  return -1;
	}

	public String getName() {
		return "no name";	// TODO implement name/url stuff
	}

	public String getURL() {
		return "no url";  // TODO implement name/url stuff
	}
}
