#! /usr/bin/ruby

# script to get users that were closest or furthest from the final answers
# (in order to see if somebody answered randomly)


def run_sql_query(sql_query)
  require "mysql"

  dbh = Mysql.real_connect("infres5.enst.fr", "dimulesc", "cselumid", "dimulesc")
  #dbh = Mysql.real_connect("localhost", "dadi", "dadi", "story_comparison_dev")

  res = dbh.query(sql_query)
  result = []
  while row = res.fetch_hash do
    result << row
  end

  return result
rescue Mysql::Error => e
  puts "Error code: #{e.errno}"
  puts "Error message: #{e.error}"
  puts "Error SQLSTATE: #{e.sqlstate}" if e.respond_to?("sqlstate")
ensure
  # disconnect from server
  dbh.close if dbh

end

def get_all_story_ids 
  run_sql_query("select distinct storyid from InsertionResult order by storyid") #.collect {|i| i['storyid'] }
end

def get_all_users
  run_sql_query("select * from User") #.collect {|i| i['login'] }
end

# parse the text file that contains summed comparisons so that we don't
# have to compute again from the database
# returns a hash<storyid, hash<array, array>> (for each story, for each version comparison as 2-element array, the versions as 2-element array)
def parse_r_datafile
  filename = "stories-r-datafile.data"
  stories_summed_results = {}
  counter = 0
  File.open(filename) do |f|
    f.each_line do |line|
      if (line =~ /story_(\d+) (\d)-(\d) (\d+) (\d+)/) then
        raise "expected something like 'story_9 1-2 23 13'" unless $1 && $2 && $3 && $4 && $5
        crt_story_hash = stories_summed_results[$1] || {}
        key = [$2, $3]
        value = [$4, $5]
        crt_story_hash[key] = value
        stories_summed_results[$1] = crt_story_hash
#        puts " #$1 , #$2, #$3, #$4 #$5"
        counter += 1
      end
    end
  end
  puts "parsed #{counter} lines from file #{filename}"
  stories_summed_results
end

def main(args)
  average_comparisons = parse_r_datafile()

  storyids = get_all_story_ids()
  raise "expected 20 stories" if storyids.length != 20
  users = get_all_users()

  users_who_didnot_complete = []
  user_who_completed = []
  users.each { |u|
    escaped_u = u['login'].sub("\'", "\'\'") # for d'alessandro
    comparisons = run_sql_query("select * from InsertionResult where user = '#{escaped_u}'")

    if (comparisons.length != 20) 
      users_who_didnot_complete << u
      puts "WARNING : user #{u['login']} only did #{comparisons.length} comparisons" 
    else
      user_who_completed << u

      score = 0
      comparisons.each { |comp|
        crtstoryid = comp['storyid']
        chosenOption = comp['chosenOption']
        _tmp = [comp['optionPresented1'],comp['optionPresented2']].sort()
        opt1 = _tmp[0]; opt2 = _tmp[1];
        avg_opt1 = average_comparisons[crtstoryid][[opt1,opt2]][0]
        avg_opt2 = average_comparisons[crtstoryid][[opt1,opt2]][1]
        good_option = opt1
        if (avg_opt2 > avg_opt2)
          good_option = opt2
        end
        if chosenOption == good_option
          score += 1
#          puts "good"
        else
          puts "user #{u['login']} : incorrect choice for story #{crtstoryid}, #{opt1} vs #{opt2} = #{avg_opt1}/#{avg_opt2}, user chose #{chosenOption}"
        end
      }
      
      u['score'] = score
    end
  }
  puts "nr of users who completed the test is #{user_who_completed.size()}"

  user_who_completed.sort {|a, b| b['score'] <=> a['score'] }.each { |user|
    puts "#{user['login']}(#{user['email']})  => #{user['score']}"
  }
end

main(ARGV)

