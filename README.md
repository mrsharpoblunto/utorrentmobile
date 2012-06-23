utorrentmobile
==============
An application that allows you to remotely control uTorrent using your mobile device. This application has been tested on a motorola V3x, and Nokia 6120 classic. It has been confirmed to work on most Nokias, and Blackberries, and should work on anything else that supports j2me applications.

Instructions
------------
In order to be able to use this application, you first need to ensure that your WebUI has a publicly accessible URL, this usually involves setting up your router port forwarding rules so the webUI server is accessible. Once this is done, start the application and enter the url of your webui (dont include the /gui at the end of the url, this is appended automatically) and your username/password, from there you should be able to view and edit all your active torrents. 

This project is composed of two applications, the first is a java EclipseME in the uTorrentfolder, this consists of the actual utorrent remote client application. The other component is an optional authentication proxy written in c# which helps get around some mobile carriers which strip authentication headers.