#!/bin/zsh
groupId=$1
artifactId=$2
version=$3

obf=$(pwd)/obf-classes
zips=$(pwd)/mods

#Script
setopt EXTENDED_GLOB NULL_GLOB

rm -rf $obf $zips
unzip -q $artifactId-$version.jar -d $obf
cd $obf

for mod in core c98/[^$]#(.:t:r); do #ZSH is neat
	name=${mod:s/core/C98Core}
	files=(
		#(|) is just to force stuff to be a glob, so they are removed (NULL_GLOB) if they don't exist
		assets/c98/${mod:l}(|)    #Assets
		c98/$mod(|\$*).class(|)   #Classes (TODO maybe move inside packages)
		c98/(#i)$mod(|)           #Package (TODO make packages lowercase)
	)
	zip=$zips/$groupId/$name/$version/$name-$version.jar
	mkdir -p $(dirname $zip)
	zip -v9r $zip $files
done
