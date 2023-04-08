import org.scalatest.matchers.must.Matchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

class SortNormSuite extends AnyFunSuite with Matchers with BeforeAndAfterAll {
  var pages: Map[String, WebPage] = Map()
  var rankedPages: List[RankedWebPage] = List()

  override def beforeAll(): Unit = {
    val testFileName: String = "testPages.csv"

    pages = mapWebPages(loadWebPages(testFileName))
    rankedPages = PageRank.makeRankedPages(pages, PageRank.equal)
  }

  test("makeSearchedPages returns correctly") {
    val textmatch: List[Double] = PageSearch.count(rankedPages, List("subroutine"))
    val searchedPages: List[SearchedWebPage] = PageSearch.makeSearchedPages(rankedPages, textmatch)
    assert(searchedPages.forall(_.weight==1))
  }

//  test("GeometricOrdering correctly weights pages") {
//    val textmatch: List[Double] = PageSearch.count(rankedPages, List("subroutine"))
//    val searchedPages: List[SearchedWebPage] = PageSearch.makeSearchedPages(rankedPages, textmatch)
//    val pageArray = SearchedWebPageNormalize.normalize(searchedPages).toArray
//    Sorting.quicksort(pageArray)(GeometricOrdering)
//    assert(pageArray.head.id == "<id here>")
//  }

}