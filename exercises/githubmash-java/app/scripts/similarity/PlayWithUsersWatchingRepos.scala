package scripts.similarity

import githubmashup.delegate._
import githubmashup.model._
import githubmashup.delegate.iterators._
import scala.collection.JavaConversions._

object PlayWithUsersWatchingRepos {
    
    val github = new GithubApiDelegateImpl()
    
    def repo_commits(repo: Repository, maxResults:Int) = {
        // v3 api
        val url = Constants.BASE_URL + "repos/" + repo.owner + "/" + repo.name + "/commits"
        new RemotePagedJsonArray(url, maxResults)
    }
    
    def get_repoes_watched_by_user(user: String, maxres:Int) = {
        // v2 api 
        val url = "http://github.com/api/v2/json/repos/watched/" + user
        new RemotePagedJsonArray(url, "repositories", Int.MaxValue)
    }
    
    def get_repo_watchers(repo: Repository) = {
        val url = "https://api.github.com/repos/" + repo.owner +"/" + repo.name + "/watchers"
        new RemotePagedJsonArray(url)
    }
    
       def get_repository(owner:String, name:String) = {
        val url = "https://api.github.com/repos/" + owner + "/" + name
        val json = github.getJsonObject(url)
        new Repository(owner, name, json.get("watchers").getAsInt)
    }
    
    def see_also(owner:String, name:String) = {
	    import scala.collection.mutable._
	    
	    val maxres = 10
	    val repos = new HashSet[Repository]
	    val users = new HashSet[String]

	    // get committers for the root repo
	    val root_repo = get_repository(owner, name)
	    repos += root_repo 
	    val committers:List[String] = repo_commits(root_repo, maxres).map(
	            it => it.getAsJsonObject("committer").get("login").getAsString()
	    ).asInstanceOf[List[String]]
	    
	    // get repos watched by committers
	    committers.foreach { it =>
	        println("*** user " + it + " watches: ")
	        val watched_repoes:List[Repository] = get_repoes_watched_by_user(it, maxres).map(it => 
	            new Repository(it.get("owner").getAsString(), 
	                    it.get("name").getAsString(), 
	                    it.get("watchers").getAsInt())
	        ).asInstanceOf[List[Repository]]
	        println(watched_repoes)
	        watched_repoes.foreach( { it => println("\t" + it) ; repos += it } )
	    }
	    println(repos)
	    
	    val repo_watchers = Map[Repository, List[String]]()
	    // for the resulting collection of repos, see who watches them (perhaps other people too, besides the committers of the root repo)
	    repos.foreach { it =>
	        val watchers = get_repo_watchers(it).map ( it => it.get("login").getAsString()).asInstanceOf[List[String]]
	        repo_watchers += it -> watchers
	        watchers.foreach(users += _)
	    }
	    
	    println(repo_watchers)
	    
	    // assign incremental numerical ids to repos and to watchers
	    def assign_ids() = {
	    	val user_id_map = Map[String, Int]() // ids assigned to users
	    	val repo_id_map = Map[Repository, Int]() // ids assigned to repos
	    	val id_user_map = Map[Int, String]()
	    	val id_repo_map = Map[Int, Repository]()
	    	
	    	users.foldLeft(0) { (counter, user) => 
	    	    user_id_map += user -> counter ;
	    	    id_user_map += counter -> user;
	    	    counter + 1; 
	    	}
	    	
	    	repos.foldLeft(0) { (counter, repo) => 
	    	    repo_id_map += repo -> counter ;
	    	    id_repo_map += counter -> repo;
	    	    counter + 1; 
	    	}
	    	
	    	(user_id_map, repo_id_map, id_user_map, id_repo_map)
	    }
	    
	    val (user_id_map, repo_id_map, id_user_map, id_repo_map) = assign_ids()
	    

	    // build matrix
        import Jama._
        import com.thoughtworks.xstream._
        
	    // fill the matrix with 0 and 1's
	    val matrix = new Matrix(users.size, repos.size)
        
        users.foreach { user =>
	    	val userId = user_id_map(user)
	    	repos.foreach { repo =>
	        	val repoId = repo_id_map(repo)
	        	val crt_repo_watchers = repo_watchers(repo)
	        
	             if (crt_repo_watchers.contains(user))
	                 matrix.set(userId, repoId, 1);
	             else
	                 matrix.set(userId, repoId, 0);
	        }
	    }
	    
	    println("================")
	    println(user_id_map)
	    println(repo_id_map)
	    
	    assert(users.size > repos.size)
	    
//	    for (val i <- 0 to users.size - 1) {
//	        for (val j <- 0 to repos.size - 1)
//	            if (matrix.get(i,j) == 1) print("*") else print(" ");
//	        Console println "|"
//	    }
//	    
	    /*
	    val xstream = new XStream()
	    xstream.toXML(matrix, new java.io.BufferedWriter(new java.io.FileWriter("/home/petru/tmp/matrix.xml")));
	    xstream.toXML(user_id_map, new java.io.BufferedWriter(new java.io.FileWriter("/home/petru/tmp/user_id_map.xml")));
	    xstream.toXML(id_user_map, new java.io.BufferedWriter(new java.io.FileWriter("/home/petru/tmp/id_user_map.xml")));
	    xstream.toXML(repo_id_map, new java.io.BufferedWriter(new java.io.FileWriter("/home/petru/tmp/repo_id_map.xml")));
	    xstream.toXML(id_repo_map, new java.io.BufferedWriter(new java.io.FileWriter("/home/petru/tmp/id_repo_map.xml")));
	    */
	    
	    val result = ApplySvdAndCalculateSimilarity.get_array_of_repos_sorted_by_similarity(matrix, root_repo, repo_id_map, id_repo_map)
	    val javafriendlystructure = new java.util.HashMap[Repository,java.lang.Double]
	    result.foreach(t => javafriendlystructure.put(t._1, t._2))
	    
        javafriendlystructure
    }
       
	def main(args: Array[String]) {
	    val arr = see_also("dpp", "liftweb")
	    arr.foreach { t => println(t) }
	}
	
}