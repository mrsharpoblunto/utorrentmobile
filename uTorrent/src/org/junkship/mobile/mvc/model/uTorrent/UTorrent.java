package org.junkship.mobile.mvc.model.uTorrent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.file.FileConnection;

import org.junkship.mobile.json.JSONArray;
import org.junkship.mobile.json.JSONException;
import org.junkship.mobile.json.JSONObject;
import org.junkship.mobile.json.JSONTokener;
import org.junkship.mobile.mvc.helpers.BasicAuth;
import org.junkship.mobile.mvc.helpers.URLEncoder;
import org.junkship.mobile.mvc.helpers.UTF8EncoderDecoder;

/**
 * this class controls all interactions with the utorrent webUI
 * @author glenn
 *
 */
public class UTorrent {
	private static UTorrent _instance = new UTorrent();
	private static int BUFF_SIZE = 512;
	
	public static UTorrent getInstance() {
		return _instance;
	}
	
	private String _serverAddress;
	private String _userCredentials;
	private String _cacheId;
	private Hashtable _torrents;
	private Hashtable _labels;
	private Hashtable _settings;
	private String _token;
	
	private InputStream _uploadFileStream;
	private String _uploadFile;
	
	private UTorrent() {
		_serverAddress = "";
		_userCredentials = "";
		_cacheId = "";
		_torrents = new Hashtable();
		_labels = new Hashtable();	
		_settings = new Hashtable();
	}
	
	/**
	 * sets the location of the webUI server
	 * @param serverAddress
	 * @param serverPort
	 */
	public void setServerSettings(boolean useSSL,String serverAddress,int serverPort){
		_serverAddress = (useSSL?"https":"http")+"://"+serverAddress+":"+Integer.toString(serverPort)+"/gui/";
	}
	
	/**
	 * sets the user credentials for the application to use when connecting to the utorrent webUI server
	 * @param userName
	 * @param password
	 */
	public void setUserCredentials(String userName,String password) {
		_userCredentials = "Basic "+BasicAuth.encode(userName,password);
	}
	
	/**
	 * @return get the list of cached torrents on the client
	 */
	public Vector getTorrents() {
		Vector result = new Vector();
		Enumeration torrents = _torrents.elements();
		while (torrents.hasMoreElements()) {
			result.addElement(torrents.nextElement());
		}
		return result;
	}
	
	/**
	 * @param torrentHash the hash of the torrent to get
	 * @return the torrent with the given hash (or null if not found)
	 */
	public Torrent getTorrent(String torrentHash) {
		if (_torrents.containsKey(torrentHash)) {
			return (Torrent)_torrents.get(torrentHash);
		}
		else {
			return null;
		}
	}
	
	/**
	 * @return the list of cached labels on the client
	 */
	public Vector getLabels() {
		Vector result = new Vector();
		Enumeration labels = _labels.elements();
		while (labels.hasMoreElements()) {
			result.addElement(labels.nextElement());
		}
		return result;		
	}
	
	/**
	 * @param torrentLabel the torrent label
	 * @return the torrentLabel requested (or null if not found)
	 */
	public TorrentLabel getLabel(String torrentLabel) {
		if (_labels.containsKey(torrentLabel)) {
			return (TorrentLabel)_labels.get(torrentLabel);
		}
		else {
			return null;
		}
	}
	
	/**
	 * apply a label to a torrent
	 * @param label
	 * @param torrent
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public void applyLabelToTorrent(String label,Torrent torrent) throws ServerNotFoundException, UnauthorizedException {
		getServerResponse("action=setprops&hash="+torrent.getHash()+"&s=label&v="+label);
	}
	
	/**
	 * check the server for the latest torrent statuses and update the client cache
	 * @return a list of torrents and related information
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	public Vector checkTorrents() throws ServerNotFoundException, UnauthorizedException {
		if (_cacheId.equals("")) {
			String response =  getServerResponse("list=1");
			parseFullResponse(response);
		}
		else {
			String response =  getServerResponse("list=1&cid="+_cacheId);
			parseDiffResponse(response);
		}
		return getTorrents();
	}

	/**
	 * parse the json from a full torrent list response
	 * @param response
	 */
	private void parseFullResponse(String response) {
		try {
			JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);		
			parseCommonResponse(jo);
			
			JSONArray torrents = jo.getJSONArray("torrents");
			_torrents.clear();
			for (int i=0;i<torrents.length();++i) {
				Torrent newTorrent = new Torrent(torrents.getJSONArray(i));
				_torrents.put(newTorrent.getHash(), newTorrent);
			}
			
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * update the labels and cacheid collection
	 * @param jo
	 * @throws JSONException
	 */
	private void parseCommonResponse(JSONObject jo) throws JSONException {
		_cacheId = jo.getString("torrentc");
		
		JSONArray labels = jo.getJSONArray("label");
		_labels.clear();
		for (int i=0;i<labels.length();++i) {
			TorrentLabel torrentLabel = new TorrentLabel(labels.getJSONArray(i));
			_labels.put(torrentLabel.getLabel(), torrentLabel);
		}
	}

	/**
	 * parse the json from a diff torrent list response
	 * @param response
	 */
	private void parseDiffResponse(String response) {
		try {
			JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);
			
			JSONArray changedTorrents = jo.getJSONArray("torrentp");
			for (int i=0;i<changedTorrents.length();++i) {
				Torrent existingTorrent = new Torrent(changedTorrents.getJSONArray(i));
				_torrents.put(existingTorrent.getHash(), existingTorrent);
			}
			
			JSONArray removedTorrents = jo.getJSONArray("torrentm");
			for (int i=0;i<changedTorrents.length();++i) {
				_torrents.remove(removedTorrents.getString(i));
			}
			
			parseCommonResponse(jo);
		//if it won't parse as a diff, then parse the full response	
		} catch (JSONException ex) {
			parseFullResponse(response);
		}
	}
	
	private String getServerResponse(String queryString) throws ServerNotFoundException, UnauthorizedException 
	{
		return getServerResponse(queryString,true);
	}
	
	/**
	 * call the webUI server and return a response string
	 * @param queryString the url querystring to pass to the server
	 * @return
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
	private String getServerResponse(String queryString,boolean retryIfTokenAuthFails) throws ServerNotFoundException, UnauthorizedException {
		HttpConnection c;
		
		try{
			String requestUrl = _serverAddress;
			//only add token auth if its required, this ensures backwards compatibility
			requestUrl += (_token != null && _token!="") ? "?token="+_token+"&" : "?";
			requestUrl += queryString;
			
			c = (HttpConnection)Connector.open(requestUrl);
			c.setRequestProperty("Authorization",_userCredentials);
			//hack as on some cell networks the authorization header gets stripped. This is a backup
			//so you can set up a proxy which alters this header to the Authorization header when it reaches
			//its destination
			c.setRequestProperty("X-J2me-Auth",_userCredentials);
			
			if (_uploadFile!=null) {
				System.out.println("File upload: "+_uploadFile);
				TorrentUploader.Upload(c, _uploadFileStream, _uploadFile);				
			}
			
			System.out.println("URI: "+requestUrl);
			System.out.println("Authorization: "+_userCredentials);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServerNotFoundException(e.toString()+" "+e.getMessage());
		}
		
		try {
			return getServerResponse(c);
		}
		catch (TokenExpiredException e) {
			//refresh the token and retry if the first call fails
			if (retryIfTokenAuthFails) {
				refreshToken();
				return getServerResponse(queryString,false);
			}
			// if the token is still not valid after the second call, bail out
			else {
				throw new UnauthorizedException("Invalid Authentication Token: "+e.getMessage());
			}
		}
	}

	private String getServerResponse(HttpConnection c) throws ServerNotFoundException, UnauthorizedException,TokenExpiredException {
		StringBuffer s = new StringBuffer();
		try {
			//got response OK
			if (c.getResponseCode()==HttpConnection.HTTP_OK) {
				InputStream is = c.openInputStream();
			
				byte[] buff = new byte[BUFF_SIZE];
				int bytesRead = is.read(buff,0,BUFF_SIZE);
				while (bytesRead>0) {
				    s.append(UTF8EncoderDecoder.UTF8Decode(buff,0,bytesRead));
				    bytesRead = is.read(buff,0,BUFF_SIZE);
				}
				is.close();
				c.close();
			}
			//incorrect authorization
			else if (c.getResponseCode()==HttpConnection.HTTP_UNAUTHORIZED) {
				throw new UnauthorizedException("Response code: "+Integer.toString(c.getResponseCode()));
			}
			//failed token authentication
			else if (c.getResponseCode()==HttpConnection.HTTP_MULT_CHOICE || c.getResponseCode()==HttpConnection.HTTP_BAD_REQUEST) {
				throw new TokenExpiredException();
			}
			//other invalid http responses
			else {
				throw new ServerNotFoundException("Response code: "+Integer.toString(c.getResponseCode()));
			}
		} catch (UnauthorizedException e) {
			e.printStackTrace();
			throw e;
		} catch (ServerNotFoundException e) {
			e.printStackTrace();
			throw e;
		} catch (TokenExpiredException e) {
			e.printStackTrace();
			throw e;
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new ServerNotFoundException(e.toString()+" "+e.getMessage());
		}
		
		System.out.println(s.toString());
		return s.toString();
	}
	
	/**
	 * start a torrent
	 * @param torrent
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
    public void start(Torrent torrent) throws ServerNotFoundException, UnauthorizedException
    {
    	getServerResponse("action=start&hash=" + torrent.getHash());
    }
    
	/**
	 * force start a torrent
	 * @param torrent
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
    public void forceStart(Torrent torrent) throws ServerNotFoundException, UnauthorizedException
    {
    	getServerResponse("action=forcestart&hash=" + torrent.getHash());
    }
    
    /**
     * stop a torrent
     * @param torrent
     * @throws ServerNotFoundException
     * @throws UnauthorizedException
     */
    public void stop(Torrent torrent) throws ServerNotFoundException, UnauthorizedException
    {
    	getServerResponse("action=stop&hash=" + torrent.getHash());
    }
    
    /**
     * pause a torrent
     * @param torrent
     * @throws ServerNotFoundException
     * @throws UnauthorizedException
     */
    public void pause(Torrent torrent) throws ServerNotFoundException, UnauthorizedException
    {
    	getServerResponse("action=pause&hash=" + torrent.getHash());
    }
    
    /**
     * remove a torrent and optionally remove the data as well
     * @param torrent
     * @param removeFiles
     * @throws ServerNotFoundException
     * @throws UnauthorizedException
     */
    public void remove(Torrent torrent,boolean removeFiles) throws ServerNotFoundException, UnauthorizedException
    {
        String action = "action=remove";
        if (removeFiles)
            action += "data";

        getServerResponse(action + "&hash=" + torrent.getHash());
    }
    
    /**
     * get the list of files in a torrent
     * @param torrent
     * @return a list of TorrentFiles contained within a given torrent
     * @throws ServerNotFoundException
     * @throws UnauthorizedException
     */
    public Vector getFilesInTorrent(Torrent torrent) throws ServerNotFoundException, UnauthorizedException {
    	String response = getServerResponse("action=getfiles&hash=" + torrent.getHash());
    	Vector result = new Vector();
    	
		try {
			JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);
			

			JSONArray files = jo.getJSONArray("files").getJSONArray(1);
			for (int i=0;i<files.length();++i) {
				result.addElement(new TorrentFile(files.getJSONArray(i)));
			}
		}
		catch (JSONException ex) {
			ex.printStackTrace();
		}
		
		return result;
    }
    
    /**
     * get the properties for a torrent
     * @param torrent
     * @return
     * @throws ServerNotFoundException
     * @throws UnauthorizedException
     */
    public TorrentProperties getPropertiesForTorrent(Torrent torrent) throws ServerNotFoundException, UnauthorizedException
    {
    	String response = getServerResponse("action=getprops&hash=" + torrent.getHash());
    	
		try {
			JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);
			
			JSONObject properties = jo.getJSONObject("props");
			return new TorrentProperties(properties);
		}
		catch (JSONException ex) {
			ex.printStackTrace();
		}
		return null;
    }
	
	private void refreshToken() throws UnauthorizedException,ServerNotFoundException {
		System.out.println("Refreshing token...");
		
		HttpConnection c;
		try {
			c = (HttpConnection)Connector.open(_serverAddress+"token.html");
			c.setRequestProperty("Authorization",_userCredentials);
			c.setRequestProperty("X-J2me-Auth",_userCredentials);
			
			System.out.println("URI: "+_serverAddress+"token.html");
			System.out.println("Authorization: "+_userCredentials);
		} catch (Exception e) {
			_token="";
			e.printStackTrace();
			throw new ServerNotFoundException("Unable to refresh Authentication Token: "+e.getMessage());
		}
		
		try {
			String tokenResponse = getServerResponse(c);
			
			String preToken = "<html><div id='token' style='display:none;'>";
			String postToken = "</div></html>";
			
			_token = tokenResponse.substring(preToken.length(), tokenResponse.length()-postToken.length());
			System.out.println("Extracted token: "+_token);
		}
		catch (Exception e) {
			_token="";
			e.printStackTrace();
			throw new ServerNotFoundException("Unable to refresh Authentication Token: "+e.getMessage());
		}		
	}

	public void setPriority(Torrent torrent,int index,int priority) throws ServerNotFoundException, UnauthorizedException {
    	getServerResponse("action=setprio&hash="+ torrent.getHash()+"&p="+priority+"&f="+index);
    }
    
    /**
     * adds a torrent from the specified URL to utorrents download list
     * must be a publicly accessible url without the use of cookies
     * @param torrentUri
     * @throws ServerNotFoundException
     * @throws UnauthorizedException
     */
    public boolean addTorrent(String torrentUri) throws ServerNotFoundException, UnauthorizedException {
    	try {
    		String response ="";
    		
    		try {
    			response = getServerResponse("action=add-url&s=" + URLEncoder.encode(torrentUri, "UTF-8"));
    		}
    		catch (IOException ex) {
        		ex.printStackTrace();
        	}
        	
    		JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);
			jo.getJSONObject("error");//will throw jsonexception if not found
		}
		catch (JSONException ex) {
			//no error object was found so everything went okay
			return true;
		}
		return false;
    }
    
    public boolean addTorrentFile(String file) throws ServerNotFoundException,UnauthorizedException, IOException {
    	try {			
			FileConnection fc = (FileConnection)Connector.open("file://localhost"+file,Connector.READ);
			_uploadFileStream = fc.openDataInputStream();
			_uploadFile = file;
			String response = getServerResponse("action=add-file");
        	
    		JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);
			jo.getJSONObject("error");//will throw jsonexception if not found
		}
		catch (JSONException ex) {
			//no error object was found so everything went okay
			return true;
		}
		finally
		{
			_uploadFile = null;
			_uploadFileStream = null;
		}
		return false;
    }
    
    /**
     * check with the server and get a list of all exposed utorrent settings
     * @return a list of UtorrentSetting objects
     * @throws ServerNotFoundException
     * @throws UnauthorizedException
     */
    public Vector checkSettings() throws ServerNotFoundException, UnauthorizedException  {
    	String response = getServerResponse("action=getsettings");
    	
		try {
			JSONTokener jt = new JSONTokener(response);
			JSONObject jo = new JSONObject(jt);
			
			_settings.clear();
			JSONArray settings = jo.getJSONArray("settings");
			for (int i=0;i<settings.length();++i) {
				UTorrentSetting setting = new UTorrentSetting(settings.getJSONArray(i));
				_settings.put(setting.getName(),setting);
			}
		}
		catch (JSONException ex) {
			ex.printStackTrace();
		}
		
		return getSettings();	
    }
    
	/**
	 * @return get the list of cached settings on the client
	 */
	public Vector getSettings() {
		Vector result = new Vector();
		Enumeration settings = _settings.elements();
		while (settings.hasMoreElements()) {
			result.addElement(settings.nextElement());
		}
		return result;
	}
	
	/**
	 * get a particular setting from the cached list on the client
	 * @param setting
	 * @return
	 */
	public UTorrentSetting getSetting(String setting) {
		if (_settings.containsKey(setting)) {
			return (UTorrentSetting)_settings.get(setting);
		}
		else {
			return null;
		}
	}
    
	/**
	 * changes the value of a utorrent setting
	 * @param setting the setting to update
	 * @throws ServerNotFoundException
	 * @throws UnauthorizedException
	 */
    public void setSetting(UTorrentSetting setting) throws ServerNotFoundException, UnauthorizedException 
    {
    	try {
    		getServerResponse("action=setsetting&s="+setting.getName()+"&v="+URLEncoder.encode(setting.getValue(),"UTF-8"));
    	}
    	catch (IOException ex) {
    		ex.printStackTrace();
    	}
    }
	
	
}
