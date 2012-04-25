import groovy.sql.Sql

def sqldev() {
	return Sql.newInstance(  
		"jdbc:mysql://localhost/story_comparison_dev", "dadi", "dadi", "com.mysql.jdbc.Driver")
}

def sqlprod()  {
	return Sql.newInstance(
			"jdbc:mysql://infres5.enst.fr/dimulesc", 
			"dimulesc", 
			"cselumid", 
			"com.mysql.jdbc.Driver")  
}
		

def sqlhome()  {
	return Sql.newInstance(
			"jdbc:mysql://localhost/story_comparison_dev", 
			"dadi", 
			"dadi", 
			"com.mysql.jdbc.Driver")  
}

// cache_filename = "/tmp/responses-cache.xml"
// results = new TreeSet()