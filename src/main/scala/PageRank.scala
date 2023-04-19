import scala.collection.parallel.CollectionConverters._

object PageRank {
    /**
     * @param pages A map of page.id to page for some number of WebPage objects
     * @return      A map of page.id to a weight of 1.0 for those same WebPage objects
     */
    def equal(pages: Map[String, WebPage]): Map[String, Double] = {
        pages.par.map((s, wp) => s -> 1.0).seq
    }

    /**
     * @param pages A map of page.id to page for some number of WebPage objects
     * @return A map of page.id to a weight that is a simple count of the number of pages linking to that page
     */
    def indegree(pages: Map[String, WebPage]): Map[String, Double] = {
        pages.par.map((id, _webPage) =>
            id -> pages.count(
                (pages_id, pages_webPage) =>
                pages_id != id && pages_webPage.links.contains(id)
            ).toDouble
        ).seq
    }
// Is there any particular reason they have to both be defined at the top level of the file/class with the same name?
// Because I'm passing pagerank into makeRankedPages, adding an optional second parameter for numWalks would make its
// Signature different from the other PageRank ranking functions (namely Indegree and Equal). Thus, simulate optional
// params via overloading pagerank. However, currying fixes it. For default numWalks, call pagerank(), then pass pages
// to the returned function: pagerank()(pages).

    /**
     * 
     * @param numUsers The number of users to randomly walk through {@code pages}. Defaults to 10,000.
     * @param pages The Map of {@code (WebPage.ID -> WebPage)} that 'users' will randomly walk through.
     * @return A Curried Function with {@code numUsers} set. Call with {@code pages} to generate weighted
     *         page rankings.
     */
    def pagerank(numUsers: Int = 10_000)(pages: Map[String, WebPage]): Map[String, Double] = {
        val pageVisits: Map[String, Double] = MonteCarlo.pageVisits(pages, numUsers)
        val indegreeRanks = indegree(pages)
        indegreeRanks.par.map(
            (pageId, rank) =>
                pageId -> rank*((pageVisits(pageId)+1.0) / (numUsers+pages.size))
        ).seq.toMap
    }

    def makeRankedPages(pages: Map[String, WebPage], rankWith: Map[String, WebPage] => Map[String, Double]): List[RankedWebPage] = {
        (for pageId -> ranking <- rankWith(pages) yield RankedWebPage(pages(pageId), ranking))
          .toList
    }

    def makeRankedPages(pages: Map[String, WebPage], rankings: Map[String, Double]): List[RankedWebPage] = {
        (for pageId -> ranking <- rankings yield RankedWebPage(pages(pageId), ranking))
          .toList
    }

}