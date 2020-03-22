SRV=http://localhost:8001

CUSTOMER=$RANDOM
echo starting session $SRV/$CUSTOMER/session
SESS=$(curl -s $SRV/$CUSTOMER/session)

# place two bets, on bet offer 11 or 12
for j in {1..2}; do
  betoffer=$((10 + j))
  stake=$((100 + j))

  echo "betting $SRV/$betoffer/stake?sessionkey=$SESS $stake"
  curl -X POST $SRV/$betoffer/stake?sessionkey=$SESS -d $stake
done

echo "betoffer 11... $SRV/10/highstakes"
curl $SRV/11/highstakes
echo

echo "betoffer 12... $SRV/10/highstakes"
curl $SRV/12/highstakes
echo
