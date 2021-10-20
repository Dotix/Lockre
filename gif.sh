#!/bin/bash

if [ -e $1 ]
then
	echo "Démarrage ---------------------"
	for i in 1 2 3 4 5 
	do
	echo "Copie du fichier N°$i -----------"
		case $i in
			1)
				if [ -d /root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-hdpi ] 
				then
					convert $1 -resize 540x540 $1-new
					mv $1-new /root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-hdpi/$1
				else
					echo "le répertoire '/root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-hdpi' n'est pas accessible"
				fi
			;;
			2) 
				if [ -d /root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-mdpi ] 
				then
					convert $1 -resize 810x810 $1-new
					mv $1-new /root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-mdpi/$1
				else
					echo "le répertoire '/root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-mdpi' n'est pas accessible"
				fi
			;;
			3) 
				if [ -d /root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-xhdpi ] 
				then
					convert $1 -resize 1080x1080 $1-new
					mv $1-new /root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-xhdpi/$1
				else
					echo "le répertoire '/root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-xhdpi' n'est pas accessible"
				fi
			;;
			4) 
				if [ -d /root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-xxhdpi ] 
				then
					convert $1 -resize 1620x1620 $1-new
					mv $1-new /root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-xxhdpi/$1
				else
					echo "le répertoire '/root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-xxhdpi' n'est pas accessible"
				fi
			;;
			5) 
				if [ -d /root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-xxxhdpi ] 
				then
					convert $1 -resize 2160x2160 $1-new
					mv $1-new /root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-xxxhdpi/$1
				else
					echo "le répertoire '/root/AndroidStudioProjects/Lokre/app/src/main/res/drawable-xxxhdpi' n'est pas accessible"
				fi
			;;
		esac
	done
	echo "Fin ---------------------------"
else 
	echo "Veuiller vous assurez que le fichier existe"
fi
	
