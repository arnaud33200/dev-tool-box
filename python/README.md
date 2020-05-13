# Read Html tags

```python
from urllib.request import urlopen
from urllib.request import urlretrieve
import ssl
from bs4 import BeautifulSoup

def getBytesFromUrl(url):
	context = ssl._create_unverified_context()
	fp = urlopen(url, context=context)
	dataBytes = fp.read()
	# mystr = mybytes.decode("utf8") # string versiob
	fp.close()
	return dataBytes

data = getBytesFromUrl(url)
soup = BeautifulSoup(data, 'html.parser')
for link in soup.find_all('a'): 
	url = link.get('href')
```

# Update Sound Speed

```python
from pydub import AudioSegment
import os

def swiftSoundSpeed(sound, speed=1.0):
	return sound._spawn(sound.raw_data, overrides={"frame_rate": int(sound.frame_rate * speed)})

convertDirPath = '<PATH>'
exportDirPath = '<PATH>'

soundSpeed = 1.1

for filename in os.listdir(convertDirPath):
	soundFile = AudioSegment.from_file(convertDirPath + "/" + filename)    
	updatedSoundFile = swiftSoundSpeed(soundFile, soundSpeed)
	updatedSoundFile.export(os.path.join(exportDirPath, filename), format="mp3")
```
