import java.util.concurrent.ThreadLocalRandom

import io.gatling.core.Predef._
import io.gatling.http.Predef._


class ComputerDatabaseSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://computer-database.gatling.io")
    // Cabeceras HTTP que se enviarán con todas las peticiones
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  val scn = scenario("Scenario with PageObjects")
    .exec(Search.search, Browse.browse, Edit.edit)

  object Search {
    val feeder = csv("search.csv").random
    val search = exec(http("Home")
      .get("/"))
      .pause(1)
      .feed(feeder)
      .exec(http("Search")
        .get("/computers?f=${searchCriterion}")
        .check(css("a:contains('${searchComputerName}')", "href").saveAs("computerUrl")))
      .pause(1)
      .exec(http("Select")
        .get("${computerUrl}"))
      .pause(1)
  }

  object Browse {
    val browse = repeat(5, "n") {
      exec(http("Page ${n}")
        .get("/computers?p=${n}"))
        .pause(1)
    }
  }

  object Edit {
    val edit = exec(http("Form")
      .get("/computers/new"))
      .pause(1)
      .exec(http("Post")
        .post("/computers")
        .check(status.is(session => 200 + ThreadLocalRandom.current.nextInt(2))))

    val tryMaxEdit = tryMax(2) {
      exec(edit)
    }.exitHereIfFailed
  }

  val users = scenario("Users").exec(Search.search, Browse.browse)
  val admins = scenario("Admins").exec(Search.search, Browse.browse, Edit.edit)

  setUp(
    users.inject(rampUsers(10) during(10)),
    admins.inject(rampUsers(2) during(10))
    ).protocols(httpProtocol)
}
