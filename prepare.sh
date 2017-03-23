#!/bin/bash -e
find ./src/ -name '*.jar' | while read file; do
	if [ ! -e "${file}.url.txt" ]; then
		echo Deleting dangling file `basename $file`...
		rm $file
	fi
done
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
if [ ! -d ".gradle" ]; then
	echo Preparing to build the Fondue mod...
	# setupDevWorkspace and setupCiWorkspace do not include
	# generic type information, and are pretty broken as a
	# result. Don't change this.
	./gradlew setupDecompWorkspace
fi
echo Building the Fondue mod...
./gradlew clean build
rm -f ../src/mods/1.11.2/Fondue-*.jar
rm build/libs/*-slim.jar
rm build/libs/*-sources.jar
cp build/libs/* ../src/mods/1.11.2/
