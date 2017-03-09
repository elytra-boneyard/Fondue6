#!/bin/bash
find ./ -name '*.url.txt' | while read file; do
	basename=${file%.url.txt}
	if [ ! -e "$basename" ]; then
		url=`cat $file`
		echo Downloading $url...
		curl -s "$url" -o "$basename"
	else
		echo Skipping download of existing file `basename $basename`
	fi
done
