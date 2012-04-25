import java.io.PrintWriter
import groovy.sql.Sql

groovy.sql.Sql conn = new sqlcxn().sqlprod()
//groovy.sql.Sql conn = new sqlcxn().sqlhome()
storyids = [];

// get story ids
conn.eachRow( 
		"select distinct storyid from InsertionResult order by storyid asc",
		{ storyids << it.storyid }
);

matlabFile = new PrintWriter(new File("/tmp/stories.m"))
rDataFile = new PrintWriter(new File("/tmp/stories-r-datafile.data"))

matlabFile.println ("# generated ${new Date() }; file for matlab to calculate eigenvalues ; for each matrix, the i,j element contains wins(i)/wins(j)")
rDataFile.println ("# generated ${new Date() } ; file for R to calculate p-values, each line is storyid versions-compared votes-for-first votes-for-second")

storyids.removeAll([0, 25]) // these are not proper stories
excludeSomeUsersClause = """
	and not (
	 user in ('bianca', 'bianca_1', 'jld', 'a', 'J. B. Berthelin', 'J. B. Berthelin_1') or
	 user like 'anonymous%'
	)
"""

results = [:]

storyids.each { story_id ->
	query = """
		select * from InsertionResult where storyid = ? and optionPresented1 = ? and optionPresented2 = ? ${excludeSomeUsersClause}
				union
		select * from InsertionResult where storyid = ? and optionPresented1 = ? and optionPresented2 = ? ${excludeSomeUsersClause}
	"""

	crt_matrix = [
	              [1,0,0],
	              [0,1,0],
	              [0,0,1],
	              ];
	[[1,2], [2,3], [1,3]].each { pair ->
	
		firstOpt 	= pair[0]
		scndOpt 	= pair[1]
		
		// start with one for each so we never have division by zero
		votesForFirst 	= 1
		votesForSecond 	= 1
		
		conn.eachRow(query, [story_id, firstOpt, scndOpt, story_id, scndOpt, firstOpt], 
			{
				if (it.chosenOption == firstOpt)
					votesForFirst ++ ;
				if (it.chosenOption == scndOpt)
					votesForSecond ++ ;
			}
		)
		
		//if (votesForFirst == 0) votesForFirst = 0.1;
		//if (votesForSecond == 0) votesForSecond = 0.1;
		
		crt_matrix[firstOpt - 1][scndOpt - 1] = "${votesForFirst}/${votesForSecond}"
		crt_matrix[scndOpt - 1][firstOpt - 1] = "${votesForSecond}/${votesForFirst}"
		
		println "story #${story_id} vers. ${firstOpt} vs ${scndOpt} = ${votesForFirst} / ${votesForSecond} "
	}
	
    // fill the matlab file
	
	matlabFile.println("story_${story_id} = [");
	//matlabFile.println("crt_matrix");
		
	crt_matrix.each { line ->
		//println (line)
		matlabFile.print("\t");
		line.each { matlabFile.print("$it , ") }
		matlabFile.println (";")
	}
	matlabFile.println("];")
	
    
    // fill the r data file
    [[1,2], [2,3], [1,3]].each {
        crt_i = it[0]
        crt_j = it[1]
        values = crt_matrix[crt_i - 1][crt_j - 1].split("/")
        rDataFile.println("story_${story_id} $crt_i-$crt_j ${values[0]} ${values[1]}")
    }
    //rDataFile.println("")
}

PRINT_ENCLOSING_ARRAY: {
	matlabFile.print("all_labels = [");
	storyids.each {
		matlabFile.print("${it}, ");
	}
	matlabFile.println("];");
}

matlabFile.close()
rDataFile.close()
