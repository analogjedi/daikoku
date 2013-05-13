#!/bin/sh

cd res/drawable-xhdpi
for i in onebit*.png ic_launcher.png
do
    convert -resize 37.5%  $i ../drawable-ldpi/$i
    convert -resize 50%    $i ../drawable-mdpi/$i
    convert -resize 75%    $i ../drawable-hdpi/$i
done
cd ../..
