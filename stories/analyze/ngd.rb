#! /usr/bin/ruby

#
# calculate normalized Google distance (NGD) following Cilibrasi & Vitanyi
#

require 'rubygems'
require 'json'
require 'net/http'
require 'cgi'


INDEX_SIZE = 20 * 1000 * 1000 * 1000
#INDEX_SIZE = 550 * 1000 # site:lemonde.fr

#DEBUG=false
DEBUG=true



GOOGLE_SEARCH_API_KEY = "ABQIAAAAY8AGoDcxvqI7cqol-NomlBS_o4gTiJXlDY7tZtshl-ai_PIK7RSn2vWX4ujun3Fr67p9gv4jxM-4Rw"
KEY_REGISTERED_FOR_SITE = "http://perso.telecom-paristech.fr/~dimulesc/"

#require "getopt/long"
#cliopts = Getopt::Long.getopts(
#    ["--quote-terms", "-q", Getopt::BOOLEAN]
#   )

#puts cliopts['q']
#puts cliopts.properties
#Process.exit


def parse_cli
    require 'getoptlong'
    require 'rdoc/usage'
    
    opts = GetoptLong.new(
       [ '--help', '-h', GetoptLong::NO_ARGUMENT        ],
       [ "--quote-terms", '-q', GetoptLong::NO_ARGUMENT ]
       )
    
end

def print_hash(obj)
  print_hash_rec(obj, "")
end

def print_hash_rec(obj, indent_level) 
  if obj.class == Hash 
    str = "\n#{indent_level}{"
    obj.each { |key, value|
      str += "\n#{indent_level}#{key} => " + print_hash_rec(value, indent_level + " ")
    }
    str += "\n#{indent_level}}"
    return str
  else
    if obj.class == Array 
      str = "\n#{indent_level}["
      obj.each { |value|
        str += "\n#{indent_level}" + print_hash_rec(value, indent_level + " ")
      }
      str += "\n#{indent_level}]"
      return str
    else
      return "#{obj}"
    end
  end
end


def call_google_search(term)  
  options = {
    :rsz    =>  'small',
    :v      => '1.0',
    #:q => CGI.escape(term + " site:nytimes.com"),
    :q      => CGI.escape(term),
    :key    => GOOGLE_SEARCH_API_KEY,
    #:meta => CGI.escape("lr=lang_fr"),
    #:meta => CGI.escape("lr=en"),
  }

  options_to_str = options.map { |key, value|  "#{key}=#{value}" }.join("&")
  url = URI("http://ajax.googleapis.com/ajax/services/search/web?" + options_to_str)
  #puts "will GET url #{url}"
  puts "url : http://www.google.com/search?" + options_to_str if DEBUG
  Net::HTTP.get(url)
end


def parse_json(jsondata)
  JSON.parse(jsondata)
end


def get_estimated_result_count(jsonobj) 
  val = jsonobj['responseData']['cursor']['estimatedResultCount']
  raise "no results" if val == nil
  val
end


def main(argv)

  arg1 = argv[0]
  arg2 = argv[1]

  throw "please provide two terms to compare" if (arg1 == nil || arg2 == nil)

  puts "calculating NGD between '#{arg1}' and '#{arg2}' ..." if DEBUG
  
  hits1 = get_estimated_result_count(parse_json(call_google_search("\"#{arg1}\"")))
  #puts "response for #{arg1} : #{print_hash(parse_json(call_google_search(arg1)))}"
  hits2 = get_estimated_result_count(parse_json(call_google_search("\"#{arg2}\"")))

  the_two_args = "\"#{arg1}\" \"#{arg2}\""
  hits_together = get_estimated_result_count(parse_json(call_google_search(the_two_args)))

  log_hits1 = log_base(2, hits1)
  log_hits2 = log_base(2, hits2)
  log_hits_together = log_base(2, hits_together)

  print_hits_info(arg1, hits1)
  print_hits_info(arg2, hits2)
  print_hits_info(the_two_args, hits_together)

  max_hits = max(log_hits1, log_hits2)
  min_hits = min(log_hits1, log_hits2)

  log_N = log_base(2, INDEX_SIZE)

  puts("(#{max_hits} - #{log_hits_together} ) / (#{log_N} - #{min_hits})") if DEBUG;

  ngd = (max_hits - log_hits_together ) / (log_N - min_hits)

  puts "NGD('#{arg1}', '#{arg2}') = #{ngd}"
end

def print_hits_info(term, hits)
  g = hits.to_f() / INDEX_SIZE
  bigG = log_base(2, 1/g)
  log_hits = log_base(2,hits)

  puts "log(f('#{term}')) = log2(#{format_decimal(hits)}) = #{log_hits}; " +
    "g = f/N = #{g}; G = log(1/g) = #{bigG}" if DEBUG;
end

def log_base(n, x)
  return (Math.log(x) / Math.log(n))
end 

def max(x, y) 
  if x > y then x else y end 
end

def min(x, y) 
  if x < y then x else y end 
end

def format_decimal(st)
  st.to_s.gsub(/(\d)(?=(\d\d\d)+(?!\d))/, "\\1,")
end


main(ARGV)
