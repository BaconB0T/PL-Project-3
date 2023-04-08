import scala.math.log
import scala.collection.parallel.CollectionConverters.*
import scala.language.postfixOps

object PageSearch {
    /**
     * @param pages  a list of RankedWebPage objects to be searched
     * @param query  a list of search terms to be counted in those pages
     * @return       a list of the number of times any of the terms appeared in each page in the same order as given
     */
    def count(pages: List[RankedWebPage], query: List[String]): List[Double] = for page <- pages yield{
        def countPage(page: RankedWebPage, query: List[String]): Double = {
            val d: Double = if query.length > 1 then {
                "(?i)".concat(query.head).r.findAllIn(page.text).length + countPage(page, query.tail)
            }
            else "(?i)".concat(query.head).r.findAllIn(page.text).length
            d
        }
        countPage(page, query)



    }

    /**
     * @param pages a list of RankedWebPage objects to be searched
     * @param query a list of search terms to be counted in those pages
     * @return      a list of the term-frequency of the occurrences of those terms in each page in the same order given
     */
    def tf(pages: List[RankedWebPage], query: List[String]): List[Double] = {
        List() // TODO: implement this method and remove this stub
    }

    /**
     * @param pages a list of RankedWebPage objects to be searched
     * @param query a list of search terms to be counted in those pages
     * @return      a list of the TF-IDF score for each page in the same order given
     */
    def tfidf(pages: List[RankedWebPage], query: List[String]): List[Double] = {
        List() // TODO: implement this method and remove this stub
    }

    def makeSearchedPages(rankedPages: List[RankedWebPage], textmatch: List[Double]): List[SearchedWebPage] = {
        rankedPages.zip(textmatch).map(x => SearchedWebPage(x._1, x._2))
    }
}