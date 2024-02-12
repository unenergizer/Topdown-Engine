TilesetsDir=Tilesets
mkdir -p $TilesetsDir
OutputDir=../atlas/page1/Tilesets
DumbX=x

#Delete the old folder, and rename existing output as OLD
#rm -r Old-$OutputDir
#mv $OutputDir Old-$OutputDir

rm -r $OutputDir

#Make the output directory if it doesn't exist
mkdir $OutputDir

#Go through the full tileset and split them up. 
files=($TilesetsDir/*.png)
total=${#files[@]}
echo total Tilesets: $total
for FILE in "${files[@]}"; do
    echo "- Processing Tileset: $FILE"
	outputLoc=$OutputDir/$(basename "$FILE" .png).png
	
	#We need to get the width and height of the file, then divide it by 16 to get the tiles
	Width=`magick identify -format '%w' $FILE`
	Height=`magick identify -format '%h' $FILE`
	Row=$(($Width / 16))
	Col=$(($Height / 16))
	#echo - magick convert $FILE -crop $Row$DumbX$Col@ +repage +adjoin $outputLoc
	magick convert $FILE -crop $Row$DumbX$Col@ +repage +adjoin -scene 000 -scene 000 $OutputDir/$(basename "$FILE" .png)-%03d.png
done

#Next we do the animation Tiles
#Cut the animated tiles into individual files, Delete the original tile if it exists so that the file format is correct
Rows=8
Columns=1
InputAnimsDir=Animations
mkdir $InputAnimsDir

#What we wanna do here is, get the filename which should be the tileID. So Tileset-003.png, we can then delete that from the output folder
#And just run the split command for animated tiles with the export into the output folder. 

files=($InputAnimsDir/*.png)
total=${#files[@]}
echo total Animations: $total
for FILE in "${files[@]}"; do
echo "- Processing Animation: $FILE"
	outputLoc=$OutputDir/$(basename "$FILE" .png).png
	rm -r $outputLoc
	#echo - magick convert $FILE -crop $Columns$DumbX$Rows@ +repage +adjoin $outputLoc
	magick convert $FILE -crop $Columns$DumbX$Rows@ +repage +adjoin -scene 000 $OutputDir/$(basename "$FILE" .png)_%d.png
done