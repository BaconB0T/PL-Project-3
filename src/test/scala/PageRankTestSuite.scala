import org.scalatest.matchers.must.Matchers
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

import scala.util.chaining.scalaUtilChainingOps

class PageRankTestSuite extends AnyFunSuite with Matchers with BeforeAndAfterAll {
  var pages: Map[String, WebPage] = Map()

  override def beforeAll(): Unit = {
    val testFileName: String = "testPages.csv"

    pages = mapWebPages(loadWebPages(testFileName))
  }

  test("equal should return with all weights set at 1") {
    val rankedPages = PageRank.equal(pages)
    rankedPages must have size this.pages.size
    assert(rankedPages.forall((x, ranking) => ranking == 1.0))
  }

  test("makeRankedPages should return a list of RankedPages with weights == 1") {
    val rp = PageRank.equal(pages)

    var rankedPages = PageRank.makeRankedPages(pages, rp)
    assert(rankedPages.forall(_.weight == 1.0))

    rankedPages = PageRank.makeRankedPages(pages, PageRank.equal)
    assert(rankedPages.forall(_.weight == 1.0))
  }

  test("Indegree should return a map of page id -> the number of other pages that link back to it") {
    val rankedPages = PageRank.indegree(pages)
    val expectedValues: Map[String, Double] = List(
      ("26e3cfd86d89a511", 0.0), ("9316632bd3f17e5f", 0.0), ("dbdfed4f2a476b3d", 0.0),
      ("d9b660ed4dddb0a9", 1.0), ("ef2385f729d7871f", 2.0), ("b4637e99e79eae92", 0.0),
      ("7abb92702b0f74a0", 0.0), ("3653ceefecb816c4", 2.0)
    ).toMap

    assert(rankedPages.toList.length == this.pages.toList.length)
    rankedPages must contain allElementsOf expectedValues
  }

//  test("pagerank should return a map of webpage ids -> their ranking") {
//    // TODO: I have no idea what these values SHOULD be, so this test is useless as is. Figure out expected values.
//    println(PageRank.pagerank(5)(pages))
//    assert(true)
//  }

}