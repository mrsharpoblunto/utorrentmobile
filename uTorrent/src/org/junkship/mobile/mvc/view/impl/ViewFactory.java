package org.junkship.mobile.mvc.view.impl;

import org.junkship.mobile.mvc.controller.IApplicationController;
import org.junkship.mobile.mvc.model.uTorrent.ServerNotFoundException;
import org.junkship.mobile.mvc.model.uTorrent.Torrent;
import org.junkship.mobile.mvc.model.uTorrent.UnauthorizedException;
import org.junkship.mobile.mvc.view.IView;

/**
 * create the vrious UI views in the application
 * @author glenn
 *
 */
public class ViewFactory {
	public static IView createSettingsView(IApplicationController controller) {
		return new SettingsView(controller);
	}
	
	public static IView createTorrentListView(IApplicationController controller) {
		return new TorrentListView(controller);
	}
	
	public static IView createTorrentDetailsView(IApplicationController controller,Torrent torrent) {
		return new TorrentDetailsView(controller,torrent);
	}
	
	public static IView createTorrentFilesView(IApplicationController controller,Torrent torrent) {
		return new TorrentFilesView(controller,torrent);
	}

	public static IView createAddTorrentView(IApplicationController controller) {
		return new AddTorrentView(controller);
	}
	
	public static IView createAddTorrentFileView(IApplicationController controller) {
		return new AddTorrentFileView(controller);
	}
	
	public static IView createServerNotFoundExceptionView(IApplicationController controller,ServerNotFoundException exceptionDetail) {
		return new ServerNotFoundExceptionView(controller,exceptionDetail);
	}
	
	public static IView createUnauthorizedExceptionView(IApplicationController controller,UnauthorizedException exceptionDetail) {
		return new UnauthorizedExceptionView(controller,exceptionDetail);
	}
}
