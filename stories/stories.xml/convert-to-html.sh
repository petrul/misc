TMPDIR=/tmp/convert-to-html-tmp-dir
mkdir -p $TMPDIR 

for FILE in *.xml; do
    BN=`basename $FILE .xml`
    echo $BN
    xsltproc story2html.xsl $FILE > $TMPDIR/$BN.html
done

