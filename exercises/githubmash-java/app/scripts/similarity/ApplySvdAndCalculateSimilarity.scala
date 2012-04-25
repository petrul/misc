package scripts.similarity

import githubmashup.delegate._
import githubmashup.model._
import githubmashup.delegate.iterators._
import scala.collection.JavaConversions._
import Jama._
import com.thoughtworks.xstream._
import com.thoughtworks.xstream.io.xml._
import scala.collection.mutable._
import org.apache.commons.lang.time._

object ApplySvdAndCalculateSimilarity {

    val github = new GithubApiDelegateImpl()
    
    def print_matrix(m: Matrix) = {
        println(">>> print_matrix of dim(" + m.getRowDimension() + "," + m.getColumnDimension() + ")")
	    for (val i <- 0 to m.getRowDimension() - 1) {
	        for (val j <- 0 to m.getColumnDimension() - 1) {
	            print(m.get(i,j));
	            print (" ")
	        }
	        Console println "|"
	    }        
    }


    /**
     * this applies SVD to the argument, and keeps only the first k eigenvalues; returns S_k * V_k
     * @returns a matrix with columns being repo (document) vectors
     */
    def get_compressed_matrix(bigMatrix: Matrix, k:Int) : Matrix = {
        
        assert(bigMatrix.getRowDimension() > bigMatrix.getColumnDimension()) // precondition for svd
        val true_k = if (k <= bigMatrix.getColumnDimension()) k else bigMatrix.getColumnDimension() 
        
        val watch = new StopWatch
        watch.reset(); watch.start()
        val svd: SingularValueDecomposition = bigMatrix.svd();
		val s = svd.getS();
		
		watch.stop()
		println("svd took " + watch)
//		
		val s_k = svd.getS.getMatrix(0, true_k - 1, 0, true_k - 1)
		val v_k = svd.getV.getMatrix(0, true_k - 1, 0, true_k - 1)
		
		s_k.times(v_k) // see http://en.wikipedia.org/wiki/Latent_semantic_analysis
    }

    def cosine_similarity_between_columns(m : Matrix, c1: Int, c2: Int) = {
        var numerator:Double = 0.0
        var norm_1:Double = 0.0
        var norm_2:Double = 0.0
        for (val i <- 0 to m.getRowDimension() - 1) {
            val v1 = m.get(i, c1);
            val v2 = m.get(i, c2);
            numerator += v1 * v2
            norm_1 += v1 * v1
            norm_2 += v2 * v2
        }
        norm_1 = scala.Math.sqrt(norm_1)
        norm_2 = scala.Math.sqrt(norm_2)
        numerator / (norm_1 * norm_2)
    }

    /**
     * main api point, java-compatible
     */
    def get_array_of_repos_sorted_by_similarity(originalBigMatrix:Matrix, refRepo:Repository, repo_id_map: java.util.Map[Repository,Int], id_repo_map: java.util.Map[Int, Repository]) = {
        val repos_matrix = get_compressed_matrix(originalBigMatrix, 300)
		val root_repo_id = repo_id_map(refRepo)
		val refRepoWatchers = refRepo.watchers.asInstanceOf[Double]
		//print_matrix
		println("" + refRepo + " => " + repo_id_map(refRepo))
		val reposAndSimilarities = new Array[(Repository, Double)](repos_matrix.getColumnDimension())
		for (val i <- 0 to repos_matrix.getColumnDimension() - 1) {
		    val cosine = cosine_similarity_between_columns(repos_matrix, root_repo_id, i)
		    val crtRepo = id_repo_map(i)
		    
		    // apply a penalty for projects that have less watchers than the reference so they don't appear too close
		    val fractionOfWatchers = crtRepo.watchers.asInstanceOf[Double] / refRepoWatchers
		    val cnst = if (fractionOfWatchers > 1.0) 1.0 else fractionOfWatchers;
//		    println ("" + id_repo_map(i) + ": " + cosine)
		    reposAndSimilarities(i) = (id_repo_map(i), cosine * cnst)
		}
		reposAndSimilarities.sortWith( (a, b) => a._2 > b._2)
    }
    
    
    def get_repository(owner:String, name:String) = {
        val url = "https://api.github.com/repos/" + owner + "/" + name
        val json = github.getJsonObject(url)
        new Repository(owner, name, json.get("watchers").getAsInt)
    }
    
	def main(args:Array[String]) {
		val root_repo = get_repository("dpp", "liftweb")
		assert(root_repo.watchers != 0)
		val xstream = new XStream(new StaxDriver())

		val watch = new StopWatch(); watch.start
		val user_id_map:Map[String, Int] = xstream.fromXML(new java.io.BufferedReader(new java.io.FileReader("/home/petru/tmp/good/user_id_map.xml"))).asInstanceOf[Map[String, Int]]
		// this need to be a hashmap for Repository.equals() ity
		val repo_id_map:HashMap[Repository,Int] = xstream.fromXML(new java.io.BufferedReader(new java.io.FileReader("/home/petru/tmp/good/repo_id_map.xml"))).asInstanceOf[HashMap[Repository,Int]]
		val id_user_map:Map[Int, String] = xstream.fromXML(new java.io.BufferedReader(new java.io.FileReader("/home/petru/tmp/good/id_user_map.xml"))).asInstanceOf[Map[Int, String]]
		val id_repo_map:Map[Int, Repository] = xstream.fromXML(new java.io.BufferedReader(new java.io.FileReader("/home/petru/tmp/good/id_repo_map.xml"))).asInstanceOf[Map[Int, Repository]]

		val matrix:Matrix = xstream.fromXML(new java.io.BufferedReader(new java.io.FileReader("/home/petru/tmp/good/matrix.xml"))).asInstanceOf[Matrix]
		//val matrix = Matrix.random(2000, 140)
		
		watch.stop
		println("done loading matrix of dim(" + matrix.getRowDimension() + "," + matrix.getColumnDimension() + "), took " + watch)
		println("" + repo_id_map.size + " repos")
		println("" + user_id_map.size + " users")
		assert(user_id_map.size == id_user_map.size)
		assert(repo_id_map.size == id_repo_map.size)
		
		val result = get_array_of_repos_sorted_by_similarity(matrix, root_repo, repo_id_map, id_repo_map)
		result.foreach { println(_) }
		
		def print_column_for_repo(r: Repository) {
	        for (val i <- 0 to matrix.getRowDimension() - 1) {
			    print(if (matrix.get(i, repo_id_map(r)) == 1.0) '*' else ' ')
			}
	    }
		println("vector for root " + root_repo)
		print_column_for_repo(root_repo)
		
		println
		println(result(0)._1)
		print_column_for_repo(result(0)._1)
		
		println
		println(result(1)._1)
		print_column_for_repo(result(1)._1)
		
		println
		println(result(2)._1)
		print_column_for_repo(result(2)._1)
		
		println
		println(result(3)._1)
		print_column_for_repo(result(3)._1)
		
		println(result(4)._1)
		print_column_for_repo(result(4)._1)
	}
}