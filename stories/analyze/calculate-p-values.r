# R script that parses the stories-r-datafile.data file and spits out p-values 
# for binomial test for comparisons

data <- read.table("stories-r-datafile.data", header=FALSE, sep="", na.strings="NA", dec=".", strip.white=TRUE)

pvalue<-c()
pvalueOK<-c()
for(i in 1:length(data[,1])) {

    test<-binom.test(c(data[i,3],data[i,4]),p=0.5, ,alternative="greater")$p.value

    pvalue <- c(pvalue, test)

    if (test<=0.05) {
        pvalueOK<-c(pvalueOK,"OK")
    } else{
        pvalueOK<-c(pvalueOK,"KO")
    } 
}
new_data<-matrix(c(as.character(data[,1]),as.character(data[,2]),pvalue,pvalueOK),ncol=4)
write.table(new_data, file="stories-p-values.csv", quote=F, append=F, col.name=F, row.names=F,sep=";")

