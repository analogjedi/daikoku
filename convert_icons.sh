#!/bin/sh

cd res/drawable-xhdpi
for i in onebit*.png
do
    convert -resize 24x $i ../drawable-mdpi/$i
    convert -resize 32x $i ../drawable-hdpi/$i
done
cd ../..
