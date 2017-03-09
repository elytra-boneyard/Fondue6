#!/bin/bash
find ./ -name '*.url.txt' | while read file; do
	basename=${file%.url.txt}
	url=`cat $file`
	echo Downloading $url...
	curl -s "$url" -o "$basename"
done
