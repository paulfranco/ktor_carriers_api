package co.paulfran.routes

import co.paulfran.data.collections.Carrier
import co.paulfran.data.getCarriersForUser
import co.paulfran.data.saveCarrier
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Conflict
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
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

    route("/addCarrier") {
        authenticate {
            post {
                val carrier = try {
                    call.receive<Carrier>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if ( saveCarrier(carrier)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }
}