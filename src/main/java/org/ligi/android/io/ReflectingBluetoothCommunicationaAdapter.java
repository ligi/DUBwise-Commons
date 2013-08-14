package org.ligi.android.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import org.ligi.java.io.CommunicationAdapterInterface;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Looper;
import android.util.Log;



public class ReflectingBluetoothCommunicationaAdapter implements
	CommunicationAdapterInterface {
		
		private InputStream input_stream;
		private OutputStream output_stream;
		private BluetoothSocket bt_connection;
		private String mac;

		public void log(String what) {
		 Log.d("DUBwise Bluetooth Communication Adapter",what);	
		}
		
		public ReflectingBluetoothCommunicationaAdapter(String mac) {
			this.mac=mac;
		}

		@Override
		public void connect() {
			try {
				
				Log.i("BTCONN","BTCONN get Adapter");
				try {
					Looper.prepare();
				} catch (Exception e) {}
				BluetoothAdapter bta = BluetoothAdapter.getDefaultAdapter();
				
				// LocalBluetoothDevice.initLocalDevice(context);

				Log.i("BTCONN","BTCONN connect trigger");
				
				log("getting remote device" +mac );
				BluetoothDevice bd = bta.getRemoteDevice(mac);
				
				log("waiting for bond");

				// Thread.sleep(5000 );
				/*
				 * while (bd.getBondState()!=bd.BOND_BONDED) { log("waiting for bond");
				 * Thread.sleep(200 ); }
				 */
				log("create method");
				log("waiting for bond");
				Method m = bd.getClass().getMethod("createRfcommSocket",
						new Class[] { int.class });
				m.setAccessible(true);
				log("create connection");
				bt_connection = (BluetoothSocket) m.invoke(bd, Integer.valueOf(1));
				// bt_connection.getRemoteDevice().
				// localBluetoothDevice.initLocalDevice(context );

				// localBluetoothDevice.

				// BluetoothDevice bt =
				// bta.getRemoteDevice(mk_url.replaceAll("btspp://","" ));

				// connection = (new BluetoothSocket(mk_url.replaceAll("btssp://",""
				// ).split(":")[0])));
				// bt_connection=LocalBluetoothDevice.getLocalDevice().getRemoteBluetoothDevice(mk_url.replaceAll("btspp://",""
				// )).openSocket(1 );
				// bt.createRfcommSocketToServiceRecord((UUID.fromString("a60f35f0-b93a-11de-8a39-08002009c666")));
				log("connect ");
				// Thread.sleep(5000 );
				
				bt_connection.connect();

				log("getting streams ");
				// Thread.sleep(5000 );
				input_stream = bt_connection.getInputStream();
				output_stream = bt_connection.getOutputStream();
				
				}
				catch(Exception e) { log(""+e); }
		}

		@Override
		public void disconnect() {
			Log.i("BTCONN","BTCONN DISconnect trigger");
			
			try {
				input_stream.close();
		    }
		    catch (IOException e) {
		    	Log.w("BTCONN","problem closing input stream");
		    }
			
			try {
				output_stream.close();
			} catch (IOException e) {
		    	Log.w("BTCONN","problem closing output stream");
		        
		    }
			
			try {
				bt_connection.close();
			} catch (IOException e) {
		    	Log.w("BTCONN","problem closing BT-Connection");
		    }
		}

		private InputStream getInputStream() {
			return input_stream;
		}

		private OutputStream getOutputStream() {
			return output_stream;
		}

		@Override
		public String getName() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getURL() {
			return "btspp://"+mac;
		}

		@Override
		public void write(byte[] buffer, int offset, int count) throws IOException {
			getOutputStream().write(buffer, offset, count);
		}

		@Override
		public void write(byte[] buffer)  throws IOException  {
			getOutputStream().write(buffer);
		}

		@Override
		public void write(int oneByte) throws IOException  {
			getOutputStream().write(oneByte);
		}

		@Override
		public int read(byte[] b, int offset, int length) throws IOException {
			if (length==0)
				return 0;
			
			if (getInputStream().available()>0)
				return getInputStream().read(b,offset,length);
			else
				return 0;
		}

		@Override
		public int read() throws IOException {
			if (getInputStream().available()>0)
				return getInputStream().read();
			
			return -1;
		}
		
		@Override
		public int available() throws IOException{
				return getInputStream().available();
		}

		@Override
		public void flush() throws IOException {
			getOutputStream().flush();
		}

}
