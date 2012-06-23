package org.junkship.mobile.mvc.model.uTorrent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.HttpConnection;

public class TorrentUploader {
	private static final String MULTI_PART_BOUNDARY = "=*=*=*=*=*=";
	
	/**
	 * writes a torrent file to an http post request ready for uploading to utorrent
	 * @param c
	 * @param fileStream
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static void Upload(HttpConnection c,InputStream fileStream,String filename) throws IOException  {
		c.setRequestMethod(HttpConnection.POST);
		c.setRequestProperty("Content-Type", "multipart/form-data; boundary="+MULTI_PART_BOUNDARY);
		
		OutputStream os = c.openOutputStream();
		
		String pre = "--" + MULTI_PART_BOUNDARY + "\r\n" +
        "Content-Disposition: form-data; name=\"torrent_file\"; filename=\"" + filename + "\"\r\n" +
        "Content-Type: application/x-bittorrent\r\n" +
        "\r\n";
		os.write(pre.getBytes());
		
		for(int i = fileStream.read(); i != -1; i = fileStream.read())
        { 
			os.write((char) i);
		}
		fileStream.close();
		
		String post = "\r\n--" + MULTI_PART_BOUNDARY + "--\r\n";
		os.write(post.getBytes());
		os.close();
	}
}
