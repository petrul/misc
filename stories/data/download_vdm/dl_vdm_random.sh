mkdir -p dl/random/

key=48fa4cc3a2ed8

for i in `seq 101 500`;
do
  str="http://api.viedemerde.fr/1.2/view/random/?key=$key"
  formatted_i=`printf "%05d" $i`
  target="dl/random/vdm-${formatted_i}.xml"
  echo "downloading $str to $target"

  GET $str | xmllint --format - > $target

  # avoid killing the site
  sleep 2

done