use WordNet::Similarity::jcn;
use WordNet::Similarity::path;
use WordNet::QueryData;

$term1 = $ARGV[0] ;
$term2 = $ARGV[1] ;

print "$term1 , $term2, \n" ;

my $wn = WordNet::QueryData->new("/usr/local/wordnet/dict");

@term1_senses = $wn->querySense($term1) ;
@term2_senses = $wn->querySense($term2) ;

# print "Synset: ", join(", ", $wn->querySense("cat#n#7", "syns")), "\n";
# print "Hyponyms: ", join(", ", $wn->querySense("cat#n#1", "hypo")), "\n";
# print "Parts of Speech: ", join(", ", $wn->querySense("run")), "\n";
# print "Senses: ", join(", ", $wn->querySense("run#v")), "\n";
# print "Forms: ", join(", ", $wn->validForms("lay down#v")), "\n";
# print "Noun count: ", scalar($wn->listAllWords("noun")), "\n";
# print "Antonyms: ", join(", ", $wn->queryWord("dark#n#1", "ants")), "\n";

my $rel = WordNet::Similarity::jcn->new($wn);
#my $rel = WordNet::Similarity::path->new($wn);

$max_score = 0 ;

foreach my $term1_sense(@term1_senses) {
    foreach my $term2_sense(@term2_senses) {
        my $value = $rel->getRelatedness($term1_sense, $term2_sense);
        print "$term1_sense <-> $term2_sense => $value\n" ;
        $max_score = $value if $value > $max_score;
    }
}

($error, $errorString) = $rel->getError();
die "$errorString\n" if($error);

print "$term1 <-> $term2 = $max_score\n";


