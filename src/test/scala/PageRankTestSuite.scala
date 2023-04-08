import org.scalatest.matchers.must.Matchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

class PageRankTestSuite extends AnyFunSuite with Matchers with BeforeAndAfterAll {
  var pages: Map[String, WebPage] = Map()

  override def beforeAll(): Unit = {
    val testFileName: String = "testPages.csv"

    pages = mapWebPages(loadWebPages(testFileName))
  }

  test("equal should return with all weights set at 1") {
    val rankedPages = PageRank.equal(pages)
    assert(rankedPages.toList.length == this.pages.toList.length)
    assert(rankedPages.forall((x, ranking) => ranking == 1.0))
  }

  test("makeRankedPages should return a list of RankedPages with weights == 1") {
    val rp = PageRank.equal(pages)
    var rankedPages = PageRank.makeRankedPages(pages, rp)
    assert(rankedPages.forall(_.weight == 1.0))
    rankedPages = PageRank.makeRankedPages(pages, PageRank.equal)
    assert(rankedPages.forall(_.weight == 1.0))
  }
}