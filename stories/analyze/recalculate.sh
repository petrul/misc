# call this first : 

# retrieve data from database and create two files for use in octave and R
groovy -cp ../compare_stories_test_webapp/src/main/groovy/ ../compare_stories_test_webapp/src/main/groovy/analyze.groovy
cp /tmp/stories.m . ; cp /tmp/stories-r-datafile.data .

# calculate rankings using matrix eigenvalue
cat stories.m calculate-ranks.m | octave -q | tee rankings.csv
R --vanilla < calculate-p-values.r

