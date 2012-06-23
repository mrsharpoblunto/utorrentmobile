package org.junkship.mobile.mvc.model.uTorrent;

import org.junkship.mobile.json.JSONArray;
import org.junkship.mobile.json.JSONException;

/**
 * A file contained within a torrent
 * @author glenn
 *
 */
public class TorrentFile {
	public static int DONT_DOWNLOAD = 0;
	public static int LOW = 1;
	public static int NORMAL = 2;
	public static int HIGH = 3;
	
	private String _fileName;
	private long _fileSize;
	private long _downloaded;
	private int _priority;
	
	public TorrentFile(JSONArray array) {
		try {
			_fileName = array.getString(0);
			_fileSize = Long.parseLong(array.getString(1));
			_downloaded = Long.parseLong(array.getString(2));
			_priority = Integer.parseInt(array.getString(3));
		}
		catch (JSONException ex) {
			ex.printStackTrace();
		}
	}
	
	public final long getDownloaded() {
		return _downloaded;
	}
	
	public final String getFileName() {
		return _fileName;
	}

	public final int getPriority() {
		return _priority;
	}
	
	public final void setPriority(int value) {
		_priority = value;
	}
	
	public final String getPriorityString() {
		switch (_priority) {
		case 0:
			return "Don't Download";
		case 1:
			return "Low Priority";
		case 2:
			return "Normal Priority";
		case 3:
			return "High priority";
		default:
			return "Unknown";
		}
	}

	public final long getFileSize() {
		return _fileSize;
	}
	
	public double getProgress() {
		return (_downloaded / (double)_fileSize) * 100;
	}
}
