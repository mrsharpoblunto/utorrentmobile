package org.junkship.mobile.mvc.model.uTorrent;

import org.junkship.mobile.json.JSONArray;

/**
 * a label that can be applied to torrents
 * @author glenn
 *
 */
public class TorrentLabel {
	private String _label;
	private int _torrentCount;
	
	public TorrentLabel(JSONArray array) {
		try{
			_label = array.getString(0);
			_torrentCount = Integer.parseInt(array.getString(1));
		}catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public final String getLabel() {
		return _label;
	}

	public final int getTorrentCount() {
		return _torrentCount;
	}
}
