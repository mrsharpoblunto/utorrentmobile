package org.junkship.mobile.mvc.model.uTorrent;

import org.junkship.mobile.json.JSONArray;
import org.junkship.mobile.json.JSONException;

/**
 * represents a torrent and related information as provided by the utorrent API
 * @author glenn
 *
 */
public class Torrent {
	
	public static String PAUSED = "Paused";
	public static String SEEDING = "Seeding";
	public static String DOWNLOADING = "Downloading";
	public static String FORCEDSEEDING = "Seeding (Forced)";
	public static String FORCEDDOWNLOADING = "Downloading (Forced)";
	public static String CHECKING = "Checking";
	public static String ERROR = "Error";
	public static String QUEUED = "Queued";
	public static String FINISHED = "Finished";
	public static String STOPPED = "Stopped";
	
	
	private String _hash;
	private int _status;
	private String _name;
	private long _size;
	private int _progress;
	private long _downloaded;
	private long _uploaded;
	private int _ratio;
	private long _uploadSpeed;
	private long _downloadSpeed;
	private long _eta;
	private String _label;
	private int _peersConnected;
	private int _peersInSwarm;
	private int _seedsConnected;
	private int _seedsInSwarm;
	private long _availability;
	private int _queueOrder;
	private long _remaining;

	public Torrent(JSONArray array) {
		try {
			_hash = array.getString(0);
			_status = Integer.parseInt(array.getString(1));
			_name= array.getString(2);
			_size = Long.parseLong(array.getString(3));
			_progress = Integer.parseInt(array.getString(4));
			_downloaded = Long.parseLong(array.getString(5));
			_uploaded = Long.parseLong(array.getString(6));
			_ratio = Integer.parseInt(array.getString(7));
			_uploadSpeed = Long.parseLong(array.getString(8));
			_downloadSpeed = Long.parseLong(array.getString(9));
			_eta = Long.parseLong(array.getString(10));
			_label = array.getString(11);
			_peersConnected = Integer.parseInt(array.getString(12));
			_peersInSwarm = Integer.parseInt(array.getString(13));
			_seedsConnected = Integer.parseInt(array.getString(14));
			_seedsInSwarm = Integer.parseInt(array.getString(15));
			_availability = Long.parseLong(array.getString(16));
			_queueOrder = Integer.parseInt(array.getString(17));
			_remaining = Long.parseLong(array.getString(18));
		}
		catch (JSONException ex) {
			ex.printStackTrace();
		}
	}

	public final long getAvailability() {
		return _availability;
	}

	public final long getDownloaded() {
		return _downloaded;
	}

	public final long getDownloadSpeed() {
		return _downloadSpeed;
	}

	public final long getEta() {
		return _eta;
	}

	public final String getHash() {
		return _hash;
	}

	public final String getLabel() {
		return _label;
	}

	public final String getName() {
		return _name;
	}

	public final int getPeersConnected() {
		return _peersConnected;
	}

	public final int getPeersInSwarm() {
		return _peersInSwarm;
	}

	public final int getProgress() {
		return _progress;
	}

	public final int getQueueOrder() {
		return _queueOrder;
	}
	
	public final int getRatio() {
		return _ratio;
	}

	public final long  getRemaining() {
		return _remaining;
	}

	public final int getSeedsConnected() {
		return _seedsConnected;
	}

	public final int getSeedsInSwarm() {
		return _seedsInSwarm;
	}
	
	public final long getSize() {
		return _size;
	}

	public final int getStatus() {
		return _status;
	}
	
	public final String getStatusString() {
		boolean flag = false;
		String ret = "";
		
	    if ((_status & 1) == 1){ //Started
	        if ((_status & 32) == 32){ //paused
	            ret=PAUSED;
	            flag=true;
	        } else { //seeding or leeching
	        	if ((_status & 64) == 64) {
	        		ret= (_progress == 1000) ? SEEDING : DOWNLOADING;
		            flag=true;
	        	}
	        	else {
	        		ret= (_progress == 1000) ? FORCEDSEEDING : FORCEDDOWNLOADING;
		            flag=true;
	        	}
	        }
	    } else if ((_status & 2) == 2){ //checking
	    	ret= CHECKING;
            flag=true;
	    } else if ((_status & 16) == 16){ //error
	    	ret= ERROR;
            flag=true;
	    } else if ((_status & 64) == 64){ //queued
	    	ret= QUEUED;
            flag=true;
	    }
	    
	    if (_progress == 1000 && !flag) {
	    	ret= FINISHED;
	    }
	    else if (_progress < 1000 && !flag) {
	    	ret= STOPPED;
	    }
	    
	    return ret;
	}
	
	public final long getUploaded() {
		return _uploaded;
	}
	public final long getUploadSpeed() {
		return _uploadSpeed;
	}

}
