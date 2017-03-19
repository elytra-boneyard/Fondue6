#!/bin/bash -e
find ./ -name '*.url.txt' | while read file; do
	basename=${file%.url.txt}
	if [ ! -e "$basename" ]; then
		url=`cat $file`
		echo Downloading $url...
		curl -L -s "$url" -o "$basename"
	else
		echo Skipping download of existing file `basename $basename`
	fi
done
cd mod
if [ -z "$1" ]; then
	echo Building the Fondue mod...
	gradle clean build > /dev/null 2>&1
	rm -f ../src/mods/1.11.2/Fondue-*.jar
	rm build/libs/*-slim.jar
	rm build/libs/*-sources.jar
	cp build/libs/* ../src/mods/1.11.2/
fi
