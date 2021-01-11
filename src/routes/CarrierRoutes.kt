package co.paulfran.routes

import co.paulfran.data.getCarriersForUser
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.response.*
import io.ktor.routing.*

fun Route.carrierRoutes() {
    route("/getCarriers") {
        authenticate {
            get {
                val email = call.principal<UserIdPrincipal>()!!.name
                val carriers = getCarriersForUser(email)
                call.respond(OK, carriers)
            }
        }
    }
}