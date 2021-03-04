#!/usr/bin/python

from urllib.request import urlopen
from urllib.request import urlretrieve
import ssl

from bs4 import BeautifulSoup

def getBytesFromUrl(url):
	context = ssl._create_unverified_context()
	fp = urlopen(url, context=context)
	dataBytes = fp.read()
	fp.close()
	return dataBytes

# ----------------------------------------------------------------
# Giving a web page with a list of song to download
# each song link goes to a different page for the file link
# This script go through each of those song and download the file

# each link would need this base to have the full ULR
baseUrl = "<BASE URL>"

url = baseUrl + "<PAGE URL WITHOUT BASE>"

mainPageData = getBytesFromUrl(url)

soup = BeautifulSoup(mainPageData, 'html.parser')

for link in soup.find_all('a'): 
	filePageUrl = link.get('href')
	if filePageUrl is None or filePageUrl.find(".mp3") == -1: continue

	mp3PageUrl = baseUrl + filePageUrl
	mp3PageData = getBytesFromUrl(mp3PageUrl)
	mp3Soup = BeautifulSoup(mp3PageData, 'html.parser')

	for subLink in mp3Soup.find_all('a'): 
		mp3Link = subLink.get('href')
		if mp3Link is None or mp3Link.find(".mp3") == -1: continue

		mp3Name = mp3Link.rsplit('/', 1)[-1]
		mp3Name = mp3Name.replace("%20", " ")

		mp3Bytes = getBytesFromUrl(mp3Link)
		with open("mp3/" + mp3Name, 'wb') as f:
			f.write(mp3Bytes)	
			f.close()
			print(mp3Name + " - URL = " + mp3Link)
		break
