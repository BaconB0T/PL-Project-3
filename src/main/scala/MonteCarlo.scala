import scala.annotation.tailrec
import scala.util.Random

enum State:
  case  //Start,      // get a random page, update rankings, set state = HasLinks, DON'T increment step
        RandomPage, // same as State.Start, but DO increment step
        HasLinks,   // determine if we are to follow links or go to a Random Page. Set state accordingly.
        FollowLink, // follow a random link from page, update rankings, set state = HasLinks, inc step
        EndUser     // return rankings

object MonteCarlo {
  def pageVisits(pages: Map[String, WebPage], numUsers: Int, stepsPerUser: Int = 100): Map[String, Double] = {
    val usersWalks: List[String] =
      Range(0, numUsers).par.flatMap(i => MonteCarlo.userWalk(pages, stepsPerUser)).toList

    usersWalks.par.foldLeft(Map[String, Double]().withDefaultValue(0.0))(
      (sofar, pageId) => sofar.updated(pageId, sofar(pageId)+1.0)
    )
  }

  // Start state. rankings may not be updated on this initial state
  def userWalk(pages: Map[String, WebPage], maxSteps: Int = 100): List[String] = {
    val pageId: String = randomKey(pages)
    userWalk(pages, List(pageId), State.HasLinks, 0, maxSteps, pages(pageId))
  }

  @tailrec
  private def userWalk(pages: Map[String, WebPage], linksWalked: List[String],
           currentState: State, stepsTaken: Int, maxSteps: Int, page: WebPage): List[String] = {
    val state = if stepsTaken == maxSteps then State.EndUser else currentState
    state match {
      case State.RandomPage =>
        val pageId: String = randomKey(pages)
        userWalk(pages, linksWalked ++ List(pageId), State.HasLinks, stepsTaken+1, maxSteps, pages(pageId))

      case State.HasLinks => userWalk(pages, linksWalked,
        if hasLinks(page) && followLink then State.FollowLink else State.RandomPage,
        stepsTaken, maxSteps, page)

      case State.FollowLink =>
        val pageId: String = page.links(randomIndex(page.links))
        userWalk(pages, linksWalked ++ List(pageId),
          State.HasLinks, stepsTaken+1, maxSteps, pages(pageId))
      case State.EndUser => linksWalked
      case _ => throw new Error(s"WHAT THE HECK JUST HAPPENED: $state")
    }
  }

  private def randomKey(pages: Map[String, WebPage]): String = pages.keys.toList(randomIndex(pages.keys))
  private def randomIndex(l: Iterable[Any]): Int = Math.floor(Random.nextDouble() * l.size).toInt
  private def followLink: Boolean = Random.nextDouble() < 0.85
  private def hasLinks(page: WebPage): Boolean = page.links.nonEmpty
}