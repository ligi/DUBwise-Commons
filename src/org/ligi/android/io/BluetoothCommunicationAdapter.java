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

import it.gerdavax.easybluetooth.BtSocket;
import it.gerdavax.easybluetooth.LocalDevice;
import it.gerdavax.easybluetooth.RemoteDevice;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.ligi.java.io.CommunicationAdapterInterface;
import org.ligi.tracedroid.logging.Log;

/**
 * Connection Adapter for Android Bluetooth Connections
 * uses lib http://android-bluetooth.googlecode.com to work with 
 * Android <2.0 and have the openSocket method without the need for SDP 
 * But that uses non standard calls - so might not work everywhere ..                                    
 *             
 * 
 * @author ligi ( aka: Marcus Bueschleb | mail: ligi at ligi dot de )
 *
 */
public class BluetoothCommunicationAdapter implements
		CommunicationAdapterInterface {
	
	private BtSocket bt_connection;
	private RemoteDevice remote_device;
	private String mac="";

	/**
	 * the mac addr - bytes seperated by :
	 * 
	 * @param mac
	 */
	public BluetoothCommunicationAdapter(String mac) {
		this.mac=mac;
	}

	public int getRSSI() {
		return remote_device.getRSSI();	 		
	}

	public void connect() {
		try {
			Log.i("getting device: " + mac);
			remote_device = LocalDevice.getInstance().getRemoteForAddr(mac);
			Log.i("ensure Paired");
			remote_device.ensurePaired();
			Log.i("open Socket");
			bt_connection=remote_device.openSocket(1);
			Log.i("done connection sequence");
		}
		catch(Exception e) { Log.w(""+e); }
	}

	public void disconnect() {
		try {
			getInputStream().close();
	    } catch (Exception e) {	    }
	    
	    try {
	    	getOutputStream().close();
	    } catch (Exception e) {	    }
	    
	    try {
	    	bt_connection.close();
	    } catch (Exception e) {	    }
	}

	public InputStream getInputStream() {

		try {
			return bt_connection.getInputStream();
		} catch (Exception e) {
			Log.i("DUBwise","getInputstream problem" + e);
			connect();
			return null;
		}
	}

	public OutputStream getOutputStream() {
		try {
			return bt_connection.getOutputStream();
		} catch (Exception e) {
			return null;
		}
	}
	
	public int available() {
		try {
			return getInputStream().available();
		} catch (Exception e) {
			return 0;
		}
	}

	public void flush() throws IOException {
		getOutputStream().flush();
	}

	public int read(byte[] b, int offset, int length) throws IOException {
		return getInputStream().read(b,offset,length);
	}

	public void write(byte[] buffer, int offset, int count) throws IOException {
		getOutputStream().write(buffer, offset, count);
	}
	
	public void write(byte[] buffer)  throws IOException  {
		getOutputStream().write(buffer);
	}

	public void write(int oneByte) throws IOException  {
		getOutputStream().write(oneByte);
	}

	public int read() throws IOException {
		return getInputStream().read();
	}

    public void getName() {
        // TODO Auto-generated method stub
        
    }

    public void getURL() {
        // TODO Auto-generated method stub
        
    }
}
