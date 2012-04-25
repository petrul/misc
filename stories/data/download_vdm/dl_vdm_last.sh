mkdir -p dl/last/

key=48fa4cc3a2ed8

for i in `seq 347 400`;
do
  str="http://api.viedemerde.fr/1.2/view/last/$i?key=$key"
  formatted_i=`printf "%05d" $i`
  target="dl/last/vdm-last-${formatted_i}.xml"
  echo "downloading $str to $target"

  GET $str | xmllint --format - > $target

  # avoid killing the site
  sleep 2

done