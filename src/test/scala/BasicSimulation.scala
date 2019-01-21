import io.gatling.core.Predef._
import io.gatling.http.Predef._


class BasicSimulation extends Simulation {

	// Configuracion para peticiones HTTP
	val httpProtocol = http
		.baseUrl("http://computer-database.gatling.io")
		// Cabeceras HTTP que se enviarán con todas las peticiones
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
		.doNotTrackHeader("1")
		.acceptLanguageHeader("en-US,en;q=0.5")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

	// Definición del escenario
	val scn = scenario("BasicSimulation")
		// Nombre de la petición HTTP, la cual se mostrará en el reporte final
		.exec(http("request_0")
			.get("/")) // Método GET
		.pause(5) // Tiempo de pause 5 segundos

	// Definición de la simulación
	// Se define la carga a injectar en el servidor
	setUp(
		scn.inject(atOnceUsers(1)) // 1 usuario en el escenario scn
	).protocols(httpProtocol) // Configuracion de la petición

}