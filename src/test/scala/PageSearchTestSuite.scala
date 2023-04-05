import org.scalatest.matchers.must.Matchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
class PageSearchTest extends AnyFunSuite with Matchers with BeforeAndAfterAll {
    var pages: Map[String, WebPage] = Map()
    var rankedPages: List[RankedWebPage] = List()
    override def beforeAll(): Unit = {
        val testFileName: String = "testPages.csv"

        pages = mapWebPages(loadWebPages(testFileName))
        rankedPages = PageRank.makeRankedPages(pages, PageRank.equal(pages))
    }
    test("Count should return a list of the number of times any of the terms appeared in each page in the same order as given") {
        assert(PageSearch.count(this.rankedPages, List("subroutine")) == List(0, 1, 4, 1, 0))
        assert(PageSearch.count(this.rankedPages, List("language")) == List(12, 6, 13, 29, 14))
        assert(PageSearch.count(this.rankedPages, List("asd")) == List(0, 0, 0, 0, 0))
    }
}