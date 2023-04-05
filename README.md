# PL-Project-3

## Part A

### [Part A] Main Loop — 10 points

Most but not all of these steps are completed for you — make sure you understand where things are happening!
1.	Load WebPage map from resources
2.	Rank pages using a method of the PageRank object (defined in the section PageRank)
3.	Normalize ranks to fall in the range [0.0, 1.0] using min-max (Appendix A – completed for you)
4.	While the user enters a query other than “:quit”  . . .
    a.  Accept the user query as a string
    b.	Compute how well each page matches using the methods of the PageSearch object (defined in the section PageSearch)
    c.	Normalize match ratings to fall in the range [0.0, 1.0] using min-max (Appendix A) 
    d.	Compute the overall match as a mean of the pages rank and text-match. For your report you will be comparing and thus must implement each of the following means.
        i.	Arithmetic Mean
        ii.	Geometric Mean
        iii.	Harmonic Mean
    e.	Sort pages based on their overall match using scala.math.Ordering to support multiple options for computing the mean
    f.	Display the name and url of the top 10 results

### [Part A] Ordering[SearchedWebPage] Objects — 15 points

This part of the project will be completed in SortNorm.scala
You are required to sort your SearchedWebPage objects in the main loop using three different subclasses of Ordering[SearchedWebPage]. These subclasses need only implement a single method named compare. The details of how this method should work are essentially identical to the functionality of the compareTo method of Java’s Comparable interface.
https://www.scala-lang.org/api/3.1.0/scala/math/Ordering.html 
https://docs.oracle.com/javase/8/docs/api/java/lang/Comparable.html 
The three comparison options you must implement are Harmonic, Arithmetic, and Geometric means. Each of these will use those means as part of the comparison. In each case you will take the appropriate mean of each pages importance (from PageRank) and textual match (from PageSearch) and compare the averages. The page with the higher average is considered greater than the other.
A simple example sorting pages based on their names has been provided for reference.

### [Parts A & B] Page Ranking — 30 points

One of the most important aspects of modern search is the relative weighting of pages. Certain pages are likely far more authoritative and desirable than others even if they do not match quite as well with the provided search term. For example, should a search query “Harvard” yield “harvard.edu” or “proudharvardgrad.cheaphosting.net”? Clearly the answer is the first result even if the second contains far more occurrences of the search term. That leaves the question of how do we know what pages are important?
One of the greatest benefits of the web for this task is the presence of hyperlinks. Presumably pages that are linked to by more pages are more important. This might even be extended iteratively to say that pages that are linked to by important pages are themselves even more important. We will use these ideas in our page ranking system.
You will implement a PageRank object with methods for the following three ranking methods.
NOTE: All listed methods are required, but you are permitted to write as many others as you need. You should not use any mutable variables anywhere in these methods. After all methods are working, you should attempt to use parallel collections to provide a speed-up where appropriate.

### [Part A] Equal Weighting — 10 points

def equal(pages: Map[String, WebPage]): Map[String: Double]
This method is the simplest naïve approach of unweighted results. This should be implemented both for comparison to other methods and to help make progress possible on other parts of the project. This method should return a Map where each string of the input map of pages is mapped to a value of 1.0.

### [Parts A & B] Page Search — 30 points

Finally, the most familiar aspect of search — determining how closely a query matches a page’s content. There are numerous ways this can be done. We will follow an approach that starts from the simplest solutions and gradually introduce improvements until we have one of the most widely used algorithms. In each case we will divide our query into terms. Terms refer to individual components of the query which for our simple example will be the component words e.g. “Hello World”  List(“Hello”, “World”).
The earliest and simplest approach to matching queries to content is to simply count the total number of occurrences of all of the search terms in the content. As one might imagine, this approach heavily favors longer documents. Our first improvement upon this will be to normalize this value by dividing by the length of the content in characters. This is still not very robust. Suppose our user types “The Bible”, should we return the document that says either “The” or “Bible” the most total times per character? Of course not — this would lead to identifying the pages that most commonly use the word “The”. Our solution will be something called TF-IDF. This technique gives higher weight to terms that appear in only a few documents and lower weight to words that appear everywhere.
You will implement a PageSearch object with methods for the following three search methods.
NOTE: All listed methods are required, but you are permitted to write as many others as you need. You should not use any mutable variables anywhere in these methods. After all methods are working, you should attempt to use parallel collections to provide a speed-up where appropriate.

### [Part A] Simple Count — 10 points

def count(pages: List[WebPage], query: List[String]): List[Double]
This method should compute for each page the number of times any of the terms in the query appear as substrings of the page content. You may implement this to be case-sensitive or case-insensitive as you see fit. Your output list should correspond to your pages list such that with ith value of your output list is the number of term matches in the ith page of pages.

## Part B

### [Parts A & B] Page Ranking — 30 points

One of the most important aspects of modern search is the relative weighting of pages. Certain pages are likely far more authoritative and desirable than others even if they do not match quite as well with the provided search term. For example, should a search query “Harvard” yield “harvard.edu” or “proudharvardgrad.cheaphosting.net”? Clearly the answer is the first result even if the second contains far more occurrences of the search term. That leaves the question of how do we know what pages are important?
One of the greatest benefits of the web for this task is the presence of hyperlinks. Presumably pages that are linked to by more pages are more important. This might even be extended iteratively to say that pages that are linked to by important pages are themselves even more important. We will use these ideas in our page ranking system.
You will implement a PageRank object with methods for the following three ranking methods.
NOTE: All listed methods are required, but you are permitted to write as many others as you need. You should not use any mutable variables anywhere in these methods. After all methods are working, you should attempt to use parallel collections to provide a speed-up where appropriate.

### [Part B] Indegree Weighting — 10 points

def indegree(pages: Map[String, WebPage]): Map[String: Double]
This method should map the id of each page in pages to the number of other pages that link to this page. That is, it should count the number of other pages for which this page’s id appears in that page’s list of links. This will need to be converted to a Double for compatibility with the general interface.

### [Part B] PageRank Weighting — 10 points

def pagerank(pages: Map[String, WebPage]): Map[String: Double]
This method is an extension of the indegree method that accounts for differences in importance between the pages that link to a given page. Most simply a link from an important page should provide more weight than a link from a relatively obscure page. This as you might imagine can be difficult to compute and is usually done with linear algebra. For our purposes we will take the random walk approach outlined in Appendix B. In short, we drop a large number of independent “walkers” on random pages. These walkers then repeatedly choose a page the current page links to and go to the new page. The distribution of where these walkers are positioned after a large number of steps is the distribution of the importance of the pages in our system.

### [Parts A & B] Page Search — 30 points

Finally, the most familiar aspect of search — determining how closely a query matches a page’s content. There are numerous ways this can be done. We will follow an approach that starts from the simplest solutions and gradually introduce improvements until we have one of the most widely used algorithms. In each case we will divide our query into terms. Terms refer to individual components of the query which for our simple example will be the component words e.g. “Hello World”  List(“Hello”, “World”).
The earliest and simplest approach to matching queries to content is to simply count the total number of occurrences of all of the search terms in the content. As one might imagine, this approach heavily favors longer documents. Our first improvement upon this will be to normalize this value by dividing by the length of the content in characters. This is still not very robust. Suppose our user types “The Bible”, should we return the document that says either “The” or “Bible” the most total times per character? Of course not — this would lead to identifying the pages that most commonly use the word “The”. Our solution will be something called TF-IDF. This technique gives higher weight to terms that appear in only a few documents and lower weight to words that appear everywhere.
You will implement a PageSearch object with methods for the following three search methods.
NOTE: All listed methods are required, but you are permitted to write as many others as you need. You should not use any mutable variables anywhere in these methods. After all methods are working, you should attempt to use parallel collections to provide a speed-up where appropriate.

### [Part B] Term Frequency — 10 points

def tf(pages: List[WebPage], query: List[String]): List[Double]
This method is an enhanced version of the count method. Each value of the output should be equal to the result of the count method for that page divided by the number of characters in the page text.

### [Part B] TF-IDF — 10 points

def tfidf(pages: List[WebPage], query: List[String]): List[Double]
This method is an enhanced version of the term frequency method. In this case we add an additional metric — inverse-document frequency. In this method the tf result for a single term on a page is multiplied by the inverse frequency with which the term appears in the text of other pages. (Appendix C)

### [Part B] Search Quality Report — 15 points

Demonstrate improvements gained (or not gained) by using certain page ranking or query matching techniques. Be sure to include example queries and results to demonstrate your point. This report should describe the results of several experiments using different combinations of ranking and text matching functions for search. For each experiment you should provide the following information.
1.	A statement of which combinations will be compared. — e.g. “For our first experiment we compared the results using indegree ranking with term frequency and TF-IDF matching.”
2.	A statement of what queries you will test and why you think they will show differences between your chosen configurations.
3.	A table showing the search results for each query on each configuration.
4.	An analysis of the results stating whether the outcome matched your expectations and analyzing why that might be the case — particularly if the results were unexpected.
You should perform at least three such experiments and include an introductory and concluding paragraph summarizing the overall experimental set and the results.
Your reports will be graded according to the following rubric.
•	Writing tone, quality, and formatting are professional (3 points)
•	At least three experiments were performed and described according to the outline (12 points)


# For Appendices, see the instructions.
