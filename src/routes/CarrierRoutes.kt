package co.paulfran.routes

import co.paulfran.data.*
import co.paulfran.data.collections.Carrier
import co.paulfran.data.requests.AddOwnerRequest
import co.paulfran.data.requests.DeleteCarrierRequest
import co.paulfran.data.responses.SimpleResponse
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

    route("/deleteCarrier") {
        authenticate {
            post {
                val email = call.principal<UserIdPrincipal>()!!.name
                val request = try {
                    call.receive<DeleteCarrierRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (deleteCarrierForUser(email, request.id)) {
                    call.respond(OK)
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }


    route("/addOwnerToCarrier") {
        authenticate {
            post {
                val request = try {
                    call.receive<AddOwnerRequest>()
                } catch (e: ContentTransformationException) {
                    call.respond(BadRequest)
                    return@post
                }
                if (!checkIfUserExists(request.owner)) {
                    call.respond(
                        OK,
                        SimpleResponse(false, "No user with this email exists")
                    )
                    return@post
                }
                if (isOwnerOfCarrier(request.carrierId, request.owner)) {
                    call.respond(
                        OK,
                        SimpleResponse(false, "This user is already an owner of this carrier")
                    )
                    return@post
                }
                if (addOwnerToCarrier(request.carrierId, request.owner)) {
                    call.respond(
                        OK,
                        SimpleResponse(true, "${request.owner} has been added as an owner of this carrier")
                    )
                } else {
                    call.respond(Conflict)
                }
            }
        }
    }
}

