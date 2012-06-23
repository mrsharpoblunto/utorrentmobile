package org.junkship.mobile.mvc.model.uTorrent;

import org.junkship.mobile.json.JSONArray;

/**
 * a configurable utorrent setting
 * @author glenn
 *
 */
public class UTorrentSetting {
	public static String RATE_LIMIT_LOCAL_PEERS = "rate_limit_local_peers";
	public static String BT_NO_CONNECT_TO_SERVICES = "bt.no_connect_to_services";
	public static String SCORE = "score";
	public static String QUEUE_DONT_COUNT_SLOW_DL = "queue.dont_count_slow_dl";
	public static String PROXY_USERNAME = "proxy.username";
	public static String SYS_PREVENT_STANDBY = "sys.prevent_standby";
	public static String SHOW_TOOLBAR = "show_toolbar";
	public static String PEER_RESOLVE_COUNTRY = "peer.resolve_country";
	public static String PEER_DISCONNECT_INACTIVE_INTERVAL = "peer.disconnect_inactive_interval";
	public static String CONFIRM_WHEN_DELETING = "confirm_when_deleting";
	public static String BT_BAN_THRESHOLD = "bt.ban_threshold";
	public static String RAND_PORT_ON_START = "rand_port_on_start";
	public static String DISKIO_COALESCE_WRITES = "diskio.coalesce_writes";
	public static String SHOW_STATUS = "show_status";
	public static String CACHE_READ_TURNOFF = "cache.read_turnoff";
	public static String EXTRAS = "extras";
	public static String CT_HIST_FLAGS = "ct_hist_flags";
	public static String SEED_TIME = "seed_time";
	public static String MOVE_IF_DEFDIR = "move_if_defdir";
	public static String UPNP = "upnp";
	public static String BT_MULTISCRAPE = "bt.multiscrape";
	public static String CACHE_READ = "cache.read";
	public static String WEBUI_USERNAME = "webui.username";
	public static String DIR_TORRENT_FILES = "dir_torrent_files";
	public static String WEBUI_ENABLE_GUEST = "webui.enable_guest";
	public static String WEBUI_GUEST = "webui.guest";
	public static String WEBUI_RESTRICT = "webui.restrict";
	public static String PREALLOC_SPACE = "prealloc_space";
	public static String DISKIO_USE_PARTFILE = "diskio.use_partfile";
	public static String NET_WSAEVENTS = "net.wsaevents";
	public static String BT_USE_RANGEBLOCK = "bt.use_rangeblock";
	public static String MAX_DL_RATE = "max_dl_rate";
	public static String DIR_TORRENT_FILES_FLAG = "dir_torrent_files_flag";
	public static String TRACKER_IP = "tracker_ip";
	public static String GUI_LAST_PREFERENCE_TAB = "gui.last_preference_tab";
	public static String BIND_PORT = "bind_port";
	public static String BT_ALLOW_SAME_IP = "bt.allow_same_ip";
	public static String GUI_SPEED_IN_TITLE = "gui.speed_in_title";
	public static String BT_AUTO_UL_SAMPLE_AVERAGE = "bt.auto_ul_sample_average";
	public static String WEBUI_ENABLE_LISTEN = "webui.enable_listen";
	public static String DISABLE_FW = "disable_fw";
	public static String APPEND_INCOMPLETE = "append_incomplete";
	public static String NET_MAX_HALFOPEN = "net.max_halfopen";
	public static String BT_USE_BAN_RATIO = "bt.use_ban_ratio";
	public static String RESOLVE_PEERIPS = "resolve_peerips";
	public static String PROXY_TYPE = "proxy.type";
	public static String MINIMIZE_TO_TRAY = "minimize_to_tray";
	public static String DIR_COMPLETED_TORRENTS_FLAG = "dir_completed_torrents_flag";
	public static String ENCRYPTION_MODE = "encryption_mode";
	public static String DHT_RATE = "dht.rate";
	public static String PROXY_PORT = "proxy.port";
	public static String CACHE_WRITEIMM = "cache.writeimm";
	public static String QUEUE_PRIO_NO_SEEDS = "queue.prio_no_seeds";
	public static String UL_SLOTS_PER_TORRENT = "ul_slots_per_torrent";
	public static String DIR_ACTIVE_DOWNLOAD_FLAG = "dir_active_download_flag";
	public static String DHT = "dht";
	public static String CT_HIST_SKIP = "ct_hist_skip";
	public static String CONNS_GLOBALLY = "conns_globally";
	public static String CONFIRM_EXIT = "confirm_exit";
	public static String RSS_UPDATE_INTERVAL = "rss.update_interval";
	public static String IPFILTER_ENABLE = "ipfilter.enable";
	public static String MAX_ACTIVE_DOWNLOADS = "max_active_downloads";
	public static String BT_NO_CONNECT_TO_SERVICES_LIST = "bt.no_connect_to_services_list";
	public static String GUI_UPDATE_RATE = "gui.update_rate";
	public static String DIR_AUTOLOAD_DELETE = "dir_autoload_delete";
	public static String TORRENTS_START_STOPPED = "torrents_start_stopped";
	public static String SEED_PRIO_LIMITUL = "seed_prio_limitul";
	public static String LSD = "lsd";
	public static String DISKIO_SPARSE_FILES = "diskio.sparse_files";
	public static String PEER_LAZY_BITFIELD = "peer.lazy_bitfield";
	public static String TRAY_SINGLE_CLICK = "tray.single_click";
	public static String BT_PRIO_FIRST_LAST_PIECE = "bt.prio_first_last_piece";
	public static String CACHE_WRITE = "cache.write";
	public static String NET_OUTGOING_PORT = "net.outgoing_port";
	public static String BT_SCRAPE_STOPPED = "bt.scrape_stopped";
	public static String SEED_PRIO_LIMITUL_FLAG = "seed_prio_limitul_flag";
	public static String PEX = "pex";
	public static String DIR_ADD_LABEL = "dir_add_label";
	public static String WEBUI_COOKIE = "webui.cookie";
	public static String NET_BIND_IP = "net.bind_ip";
	public static String GUI_SG_MODE = "gui.sg_mode";
	public static String GUI_LIMITS_IN_STATUSBAR = "gui.limits_in_statusbar";
	public static String BT_AUTO_UL_INTERVAL = "bt.auto_ul_interval";
	public static String ANONINFO = "anoninfo";
	public static String CACHE_READ_TRASH = "cache.read_trash";
	public static String BT_AUTO_UL_SAMPLE_WINDOW = "bt.auto_ul_sample_window";
	public static String DIR_COMPLETED_DOWNLOAD_FLAG = "dir_completed_download_flag";
	public static String CT_HIST_COMM = "ct_hist_comm";
	public static String CLOSE_TO_TRAY = "close_to_tray";
	public static String ENCRYPTION_ALLOW_LEGACY = "encryption_allow_legacy";
	public static String DISKIO_SMART_HASH = "diskio.smart_hash";
	public static String ALWAYS_SHOW_ADD_DIALOG = "always_show_add_dialog";
	public static String SEEDS_PRIORITIZED = "seeds_prioritized";
	public static String BT_BAN_RATIO = "bt.ban_ratio";
	public static String SCHED_ENABLE = "sched_enable";
	public static String GUI_DLRATE_MENU = "gui.dlrate_menu";
	public static String CACHE_REDUCE = "cache.reduce";
	public static String NET_LOW_CPU = "net.low_cpu";
	public static String SCHED_UL_RATE = "sched_ul_rate";
	public static String WEBUI_ENABLE = "webui.enable";
	public static String AUTOSTART = "autostart";
	public static String RSS_START_MATCHES = "rss.start_matches";
	public static String CACHE_WRITEOUT = "cache.writeout";
	public static String GUI_BYPASS_SEARCH_REDIRECT = "gui.bypass_search_redirect";
	public static String SHOW_DETAILS = "show_details";
	public static String DIR_COMPLETED_TORRENTS = "dir_completed_torrents";
	public static String NET_OUTGOING_IP = "net.outgoing_ip";
	public static String BT_SEND_HAVE_TO_SEED = "bt.send_have_to_seed";
	public static String DIR_AUTOLOAD_FLAG = "dir_autoload_flag";
	public static String PROXY_PASSWORD = "proxy.password";
	public static String BT_AUTO_UL_FACTOR = "bt.auto_ul_factor";
	public static String QUEUE_DONT_COUNT_SLOW_UL = "queue.dont_count_slow_ul";
	public static String DHT_PER_TORRENT = "dht_per_torrent";
	public static String GUI_ALTERNATE_COLOR = "gui.alternate_color";
	public static String ACTIVATE_ON_FILE = "activate_on_file";
	public static String BT_SET_SOCKBUF = "bt.set_sockbuf";
	public static String SHOW_TABICONS = "show_tabicons";
	public static String PROXY_P2P = "proxy.p2p";
	public static String NOTIFY_COMPLETE = "notify_complete";
	public static String TRAY_ACTIVATE = "tray_activate";
	public static String GUI_DBLCLICK_DL = "gui.dblclick_dl";
	public static String K = "k";
	public static String GUI_LAST_OVERVIEW_TAB = "gui.last_overview_tab";
	public static String ENABLE_SCRAPE = "enable_scrape";
	public static String WEBUI_PASSWORD = "webui.password";
	public static String EXTRA_ULSLOTS = "extra_ulslots";
	public static String CONNS_PER_TORRENT = "conns_per_torrent";
	public static String MAINWND_SPLIT_X = "mainwnd_split_x";
	public static String GUI_DELETE_TO_TRASH = "gui.delete_to_trash";
	public static String GUI_DEFAULT_DEL_ACTION = "gui.default_del_action";
	public static String RELOAD_FREQ = "reload_freq";
	public static String NET_OUTGOING_MAX_PORT = "net.outgoing_max_port";
	public static String CACHE_OVERRIDE_SIZE = "cache.override_size";
	public static String TRAY_SHOW = "tray.show";
	public static String MAX_ACTIVE_TORRENT = "max_active_torrent";
	public static String BT_ENABLE_TRACKER = "bt.enable_tracker";
	public static String CHECK_UPDATE = "check_update";
	public static String MAX_UL_RATE_SEED_FLAG = "max_ul_rate_seed_flag";
	public static String DIR_COMPLETED_DOWNLOAD = "dir_completed_download";
	public static String GUI_PERSISTENT_LABELS = "gui.persistent_labels";
	public static String MAX_UL_RATE = "max_ul_rate";
	public static String BT_AUTO_UL_MIN = "bt.auto_ul_min";
	public static String BOSS_KEY = "boss_key";
	public static String SCHED_DIS_DHT = "sched_dis_dht";
	public static String SHOW_ADD_DIALOG = "show_add_dialog";
	public static String GUI_MANUAL_RATEMENU = "gui.manual_ratemenu";
	public static String MAINWND_SPLIT = "mainwnd_split";
	public static String WEBUI_PORT = "webui.port";
	public static String UL_AUTO_THROTTLE = "ul_auto_throttle";
	public static String CACHE_READ_PRUNE = "cache.read_prune";
	public static String DIR_ACTIVE_DOWNLOAD = "dir_active_download";
	public static String DISKIO_FLUSH_FILES = "diskio.flush_files";
	public static String GUI_ULRATE_MENU = "gui.ulrate_menu";
	public static String CHECK_ASSOC_ON_START = "check_assoc_on_start";
	public static String LOGGER_MASK = "logger_mask";
	public static String CHECK_UPDATE_BETA = "check_update_beta";
	public static String SHOW_CATEGORY = "show_category";
	public static String PROXY_AUTH = "proxy.auth";
	public static String BT_CONNECT_SPEED = "bt.connect_speed";
	public static String GUI_GRAPHIC_PROGRESS = "gui.graphic_progress";
	public static String QUEUE_USE_SEED_PEER_RATIO = "queue.use_seed_peer_ratio";
	public static String SEED_RATIO = "seed_ratio";
	public static String GUI_COMPAT_DIROPEN = "gui.compat_diropen";
	public static String BT_COMPACT_ALLOCATION = "bt.compact_allocation";
	public static String PEER_DISCONNECT_INACTIVE = "peer.disconnect_inactive";
	public static String LANGUAGE = "language";
	public static String CACHE_OVERRIDE = "cache.override";
	public static String PROXY_PROXY = "proxy.proxy";
	public static String MAX_UL_RATE_SEED = "max_ul_rate_seed";
	public static String SCHED_DL_RATE = "sched_dl_rate";
	public static String BT_GRACEFUL_SHUTDOWN = "bt.graceful_shutdown";
	public static String DIR_AUTOLOAD = "dir_autoload";
	public static String GUI_DBLCLICK_SEED = "gui.dblclick_seed";
	public static String NATPMP = "natpmp";
	
	public static int INTEGER_TYPE = 0;
	public static int BOOLEAN_TYPE = 1;
	public static int STRING_TYPE = 2;
	
	private String _name;
	private int _type;
	private String _value;
	
	public UTorrentSetting(JSONArray array) {
		try {
			_name = array.getString(0);
			_type = Integer.parseInt(array.getString(1));
			_value = array.getString(2);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public final String getName() {
		return _name;
	}

	public final int getType() {
		return _type;
	}

	public final String getValue() {
		return _value;
	}
	
	public final int getIntValue() {
		return Integer.parseInt(_value);
	}
	
	public final boolean getBooleanValue() {
		return _value.equals("true");
	}
	
	public void setValue(int value) throws InvalidSettingTypeException {
		if (_type == INTEGER_TYPE) {
			_value = Integer.toString(value);
		}
		else {
			throw new InvalidSettingTypeException();
		}
	}
	
	public void setValue(boolean value) throws InvalidSettingTypeException {
		if (_type == BOOLEAN_TYPE) {
			_value = value?"true":"false";
		}
		else {
			throw new InvalidSettingTypeException();
		}
	}
	
	public void setValue(String value) throws InvalidSettingTypeException {
		if (_type == STRING_TYPE) {
			_value = value;
		}
		else {
			throw new InvalidSettingTypeException();
		}
	}
	
}
