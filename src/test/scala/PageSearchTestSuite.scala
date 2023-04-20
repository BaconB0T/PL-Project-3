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

        pages must have size rankedPages.length
    }
    test("Count should return a list of the number of times any of the terms appeared in each page in the same order as given") {
        val expectedValues: Map[String, Map[String, Double]] =
            List(
                ("subroutine", List(
                    ("26e3cfd86d89a511", 0.0), ("9316632bd3f17e5f", 4.0), ("dbdfed4f2a476b3d", 1.0),
                    ("d9b660ed4dddb0a9", 5.0), ("ef2385f729d7871f", 0.0), ("b4637e99e79eae92", 0.0),
                    ("7abb92702b0f74a0", 0.0), ("3653ceefecb816c4", 0.0)
                ).toMap),
                ("asd", List(
                    ("26e3cfd86d89a511", 0.0), ("9316632bd3f17e5f", 0.0), ("dbdfed4f2a476b3d", 0.0),
                    ("d9b660ed4dddb0a9", 0.0), ("ef2385f729d7871f", 0.0), ("b4637e99e79eae92", 0.0),
                    ("7abb92702b0f74a0", 0.0), ("3653ceefecb816c4", 0.0)
                ).toMap),
                ("language", List(
                    ("26e3cfd86d89a511", 11.0), ("9316632bd3f17e5f", 6.0), ("dbdfed4f2a476b3d", 14.0),
                    ("d9b660ed4dddb0a9", 28.0), ("ef2385f729d7871f", 14.0), ("b4637e99e79eae92", 21.0),
                    ("7abb92702b0f74a0", 33.0), ("3653ceefecb816c4", 88.0)
                ).toMap)
            ).toMap

        // Iterate through the 3 terms to search and generate the weights using `PageSearch.count`.
        // Then zip the weights with the pageIds so we can compare each page's expected value with
        // the value we created.
        for (searchTerm, expectedWeights) <- expectedValues do {
            val weightedSearch: List[Double] = PageSearch.count(this.rankedPages, List(searchTerm))
            val searchMap: Map[String, Double] = this.rankedPages.map(_.id).zip(weightedSearch).toMap
            searchMap must contain allElementsOf expectedWeights
        }
    }
    test("TF should return a list of the number of times any of the terms appeared in each page in the same order as given") {
        val expectedValues: Map[String, Map[String, Double]] =
            List(
                ("subroutine", List(
                    ("26e3cfd86d89a511", 0.0/pages("26e3cfd86d89a511").text.length), ("9316632bd3f17e5f", 4.0/pages("9316632bd3f17e5f").text.length), ("dbdfed4f2a476b3d", 1.0/pages("dbdfed4f2a476b3d").text.length),
                    ("d9b660ed4dddb0a9", 5.0/pages("d9b660ed4dddb0a9").text.length), ("ef2385f729d7871f", 0.0/pages("ef2385f729d7871f").text.length), ("b4637e99e79eae92", 0.0/pages("b4637e99e79eae92").text.length),
                    ("7abb92702b0f74a0", 0.0/pages("7abb92702b0f74a0").text.length), ("3653ceefecb816c4", 0.0/pages("3653ceefecb816c4").text.length)
                ).toMap),
                ("asd", List(
                    ("26e3cfd86d89a511", 0.0/pages("26e3cfd86d89a511").text.length), ("9316632bd3f17e5f", 0.0/pages("9316632bd3f17e5f").text.length), ("dbdfed4f2a476b3d", 0.0/pages("dbdfed4f2a476b3d").text.length),
                    ("d9b660ed4dddb0a9", 0.0/pages("d9b660ed4dddb0a9").text.length), ("ef2385f729d7871f", 0.0/pages("ef2385f729d7871f").text.length), ("b4637e99e79eae92", 0.0/pages("b4637e99e79eae92").text.length),
                    ("7abb92702b0f74a0", 0.0/pages("7abb92702b0f74a0").text.length), ("3653ceefecb816c4", 0.0/pages("3653ceefecb816c4").text.length)
                ).toMap),
                ("language", List(
                    ("26e3cfd86d89a511", 11.0/pages("26e3cfd86d89a511").text.length), ("9316632bd3f17e5f", 6.0/pages("9316632bd3f17e5f").text.length), ("dbdfed4f2a476b3d", 14.0/pages("dbdfed4f2a476b3d").text.length),
                    ("d9b660ed4dddb0a9", 28.0/pages("d9b660ed4dddb0a9").text.length), ("ef2385f729d7871f", 14.0/pages("ef2385f729d7871f").text.length), ("b4637e99e79eae92", 21.0/pages("b4637e99e79eae92").text.length),
                    ("7abb92702b0f74a0", 33.0/pages("7abb92702b0f74a0").text.length), ("3653ceefecb816c4", 88.0/pages("3653ceefecb816c4").text.length)
                ).toMap)
            ).toMap
        for (searchTerm, expectedWeights) <- expectedValues do {
            val weightedSearch: List[Double] = PageSearch.tf(this.rankedPages, List(searchTerm))
            val searchMap: Map[String, Double] = this.rankedPages.map(_.id).zip(weightedSearch).toMap
            searchMap must contain allElementsOf expectedWeights
        }
    }
//    test("makeSearchedWebPage should return a list of searchedWebPages") {
//        val textmatch: List[Double] = PageSearch.count(this.rankedPages, List("subroutine"))
//        assert(PageSearch.makeSearchedPages(this.rankedPages, textmatch)
//
//    }
}