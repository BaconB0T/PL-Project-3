import org.scalatest.matchers.must.Matchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
class PageSearchTest extends AnyFunSuite with Matchers with BeforeAndAfterAll {
    var pages: Map[String, WebPage] = Map()
    var rankedPages: List[RankedWebPage] = List()
    override def beforeAll(): Unit = {
        val testFileName: String = "testPages.csv"

        pages = mapWebPages(loadWebPages(testFileName))
        rankedPages = PageRank.makeRankedPages(pages, PageRank.equal).sortBy(_.id)

        assert(pages.toList.length == rankedPages.length)
    }
    test("Count should return a list of the number of times any of the terms appeared in each page in the same order as given") {
        assert(PageSearch.count(this.rankedPages, List("subroutine")) == List(0.0, 4.0, 5.0, 1.0, 0.0))
        assert(PageSearch.count(this.rankedPages, List("language")) == List(11.0, 6.0, 28.0, 14.0, 14.0))
        assert(PageSearch.count(this.rankedPages, List("asd")) == List(0, 0, 0, 0, 0))
    }
//    test("makeSearchedWebPage should return a list of searchedWebPages") {
//        val textmatch: List[Double] = PageSearch.count(this.rankedPages, List("subroutine"))
//        assert(PageSearch.makeSearchedPages(this.rankedPages, textmatch)
//
//    }
}