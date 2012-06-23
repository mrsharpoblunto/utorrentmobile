package org.junkship.mobile.mvc.model.uTorrent;

import org.junkship.mobile.json.JSONException;
import org.junkship.mobile.json.JSONObject;

/**
 * properties that can be set for a particular torrent
 * @author glenn
 *
 */
public class TorrentProperties {
	private String _hash;
	private String _trackers;
	private int _maximumUploadRate;
	private int _maximumDownloadRate;
	private int _uploadSlots;
	private boolean _dht;
	private boolean _peerExchange;
	private boolean _seedOverride;
	private boolean _superSeed;
	private int _seedRatio;
	private int _seedTime;
	
	public TorrentProperties(JSONObject object) {
		try {
			_hash = object.getString("hash");
			_trackers = object.getString("trackers");
			_maximumUploadRate = object.getInt("ulrate");
			_maximumDownloadRate = object.getInt("dlrate");
			_superSeed = object.getBoolean("superseed");
			_dht = object.getBoolean("dht");
			_peerExchange = object.getBoolean("pex");
			_seedOverride = object.getBoolean("seed_override");
			_seedRatio = object.getInt("seed_ratio");
			_seedTime = object.getInt("seed_time");
			_uploadSlots = object.getInt("ulslots");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the _dht
	 */
	public final boolean is_dht() {
		return _dht;
	}

	/**
	 * @param _dht the _dht to set
	 */
	public final void set_dht(boolean _dht) {
		this._dht = _dht;
	}

	/**
	 * @return the _hash
	 */
	public final String get_hash() {
		return _hash;
	}

	/**
	 * @param _hash the _hash to set
	 */
	public final void set_hash(String _hash) {
		this._hash = _hash;
	}

	/**
	 * @return the _maximumDownloadRate
	 */
	public final int get_maximumDownloadRate() {
		return _maximumDownloadRate;
	}

	/**
	 * @param downloadRate the _maximumDownloadRate to set
	 */
	public final void set_maximumDownloadRate(int downloadRate) {
		_maximumDownloadRate = downloadRate;
	}

	/**
	 * @return the _maximumUploadRate
	 */
	public final int get_maximumUploadRate() {
		return _maximumUploadRate;
	}

	/**
	 * @param uploadRate the _maximumUploadRate to set
	 */
	public final void set_maximumUploadRate(int uploadRate) {
		_maximumUploadRate = uploadRate;
	}

	/**
	 * @return the _peerExchange
	 */
	public final boolean is_peerExchange() {
		return _peerExchange;
	}

	/**
	 * @param exchange the _peerExchange to set
	 */
	public final void set_peerExchange(boolean exchange) {
		_peerExchange = exchange;
	}

	/**
	 * @return the _seedOverride
	 */
	public final boolean is_seedOverride() {
		return _seedOverride;
	}

	/**
	 * @param override the _seedOverride to set
	 */
	public final void set_seedOverride(boolean override) {
		_seedOverride = override;
	}

	/**
	 * @return the _seedRatio
	 */
	public final int get_seedRatio() {
		return _seedRatio;
	}

	/**
	 * @param ratio the _seedRatio to set
	 */
	public final void set_seedRatio(int ratio) {
		_seedRatio = ratio;
	}

	/**
	 * @return the _seedTime
	 */
	public final int get_seedTime() {
		return _seedTime;
	}

	/**
	 * @param time the _seedTime to set
	 */
	public final void set_seedTime(int time) {
		_seedTime = time;
	}

	/**
	 * @return the _superSeed
	 */
	public final boolean is_superSeed() {
		return _superSeed;
	}

	/**
	 * @param seed the _superSeed to set
	 */
	public final void set_superSeed(boolean seed) {
		_superSeed = seed;
	}

	/**
	 * @return the _trackers
	 */
	public final String get_trackers() {
		return _trackers;
	}

	/**
	 * @param _trackers the _trackers to set
	 */
	public final void set_trackers(String _trackers) {
		this._trackers = _trackers;
	}

	/**
	 * @return the _uploadSlots
	 */
	public final int get_uploadSlots() {
		return _uploadSlots;
	}

	/**
	 * @param slots the _uploadSlots to set
	 */
	public final void set_uploadSlots(int slots) {
		_uploadSlots = slots;
	}
	
}
