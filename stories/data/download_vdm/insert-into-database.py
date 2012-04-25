#! /usr/bin/python 

from __future__ import with_statement
import os, sys, lxml


dir1 = "dl/last"
dir2 = "dl/top"
dir3 = "dl/random"

ids =[]

cnt_duplicates = 0
cnt_stories = 0

def parse_files_from_dir(directory, csv_file) :
    "loads all content from the xmls in the given directory"

    print("parsing files from dir : " + directory)
    global cnt_duplicates, cnt_stories

    cnt_files = 0
    
    for file in os.listdir(directory) :
        from lxml import etree

        with open(directory + "/" + file, "r") as crtfile :
            cnt_files += 1
            
            doc = etree.parse(crtfile)
            for item in doc.xpath("//vdm") :
                id = item.get("id")
                if id in ids :
                    cnt_duplicates += 1
                    continue;

                ids.append(id)
                cnt_stories += 1
                
                author = item.findtext("auteur")
                cat = item.findtext("categorie")
                jeValide = item.findtext("je_valide")
                bienMerite = item.findtext("bien_merite")
                comments = item.findtext("commentaires")
                txt = item.findtext("texte")
                commentable = item.findtext("commentable")

                vars = (id, author, cat, jeValide, bienMerite, comments, commentable, txt)
                try:
                    vars = map(
                        lambda s : s and s.replace('\n', '').replace(chr(13), "") or "", # we can't use "if" inside lambda
                        vars)


                    line = "%s \t %s \t %s \t %s \t %s \t %s \t %s \t %s \n" % tuple(vars)
                except:
                    print "error on vars", vars
                    raise
                
                csv_file.write(line.encode("utf-8"))

    print "\t ...parsed %d files." % cnt_files



def main():
    with open("all.csv", "w") as f :
        parse_files_from_dir(dir1, f)
        parse_files_from_dir(dir2, f)
        parse_files_from_dir(dir3, f)

    print "done, there were %d stories and %d duplicates" % (cnt_stories, cnt_duplicates)


main()
