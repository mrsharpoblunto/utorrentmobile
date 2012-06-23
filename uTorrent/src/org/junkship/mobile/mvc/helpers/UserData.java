package org.junkship.mobile.mvc.helpers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordStore;

public class UserData {
	
	private static String StoreName = "uTorrentUserData";
	private static int RecordId = 1;
	
	private class UserDataRecord {
		public String UserName;
		public String Password;
		public String ServerAddress;
		public int ServerPort;	
		public boolean AutoUpdateList;
		public boolean WrapListText;
	}
	
	private static UserData _instance = new UserData();
	
	private UserDataRecord _record;

	private UserData() {
		load();
	}
	
	private void load() {
		
		//open the record store
		RecordStore store=null;
		try {
			store = RecordStore.openRecordStore(StoreName, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//load data from the record store
		try {
			byte[] loadData = store.getRecord(RecordId);
			DataInputStream is = new DataInputStream(new ByteArrayInputStream(loadData));
			_record = new UserDataRecord();
			_record.UserName = is.readUTF();
			_record.Password = is.readUTF();
			_record.ServerAddress = is.readUTF();
			_record.ServerPort = is.readInt();
			_record.AutoUpdateList = is.readBoolean();
			_record.WrapListText = is.readBoolean();
			is.close();
			System.out.println("Loaded UserData");
		//if theres no record currently in the store, we'll create a default one
		} catch (InvalidRecordIDException ire) {
			createNewRecord(store);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//close the recordstore
		try {
			store.closeRecordStore();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
	}

	private void createNewRecord(RecordStore store) {
		_record = new UserDataRecord();
		_record.UserName = "";
		_record.Password = "";
		_record.ServerAddress = "";
		_record.ServerPort = 6681;
		_record.AutoUpdateList = true;
		_record.WrapListText = true;
		byte[] saveData = getSaveData();
		try {
			store.addRecord(saveData, 0, saveData.length);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		System.out.println("Created new UserData Record");
	}

	private byte[] getSaveData() {	
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream os = new DataOutputStream(baos);
		try {
			os.writeUTF(_record.UserName);
			os.writeUTF(_record.Password);
			os.writeUTF(_record.ServerAddress);
			os.writeInt(_record.ServerPort);
			os.writeBoolean(_record.AutoUpdateList);
			os.writeBoolean(_record.WrapListText);
			os.close();
		}
		catch (IOException ex) {
		}
		
		return baos.toByteArray();
	}
	
	public void save() {
		//open the record store
		RecordStore store=null;
		try {
			store = RecordStore.openRecordStore(StoreName, true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//load data from the record store
		byte[] saveData = getSaveData();
		try {
			store.setRecord(RecordId,saveData, 0, saveData.length);
		} catch (Exception ex) {
			ex.printStackTrace();
		}	
		
		//close the recordstore
		try {
			store.closeRecordStore();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		System.out.println("Saved UserData");
	}

	public static final UserData getInstance() {
		return _instance;
	}
	
	public final String getPassword() {
		return _record.Password;
	}
	
	public final void setPassword(String password) {
		this._record.Password = password;
	}
	
	public final String getUserName() {
		return _record.UserName;
	}
	
	public final void setUserName(String name) {
		_record.UserName = name;
	}
	
	public final String getServerAddress() {
		return _record.ServerAddress;
	}

	public final void setServerAddress(String address) {
		_record.ServerAddress = address;
	}

	public final int getServerPort() {
		return _record.ServerPort;
	}

	public final void setServerPort(int port) {
		_record.ServerPort = port;
	}
	
	public final boolean getAutoUpdateList() {
		return _record.AutoUpdateList;
	}

	public final void setAutoUpdateList(boolean autoUpdateList) {
		_record.AutoUpdateList = autoUpdateList;
	}
	
	public final boolean getWrapListText() {
		return _record.WrapListText;
	}

	public final void setWrapListText(boolean wrapListText) {
		_record.WrapListText = wrapListText;
	}
	
}
