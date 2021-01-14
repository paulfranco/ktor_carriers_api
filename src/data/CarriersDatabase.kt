package co.paulfran.data

import co.paulfran.data.collections.Carrier
import co.paulfran.data.collections.User
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

private val client = KMongo.createClient().coroutine
private val database = client.getDatabase("CarriersDatabase")
private val users = database.getCollection<User>()
private val carriers = database.getCollection<Carrier>()

suspend fun registerUser(user: User): Boolean {
    return users.insertOne(user).wasAcknowledged()
}

suspend fun checkIfUserExists(email: String): Boolean {
    return users.findOne(User::email eq email) != null
}

suspend fun checkPasswordForEmail(email: String, passwordToCheck: String): Boolean {
    val actualPassword = users.findOne(User::email eq email)?.password ?: return false
    return actualPassword == passwordToCheck
}

suspend fun getCarriersForUser(email: String): List<Carrier> {
    return carriers.find(Carrier::owners contains email).toList()
}

suspend fun saveCarrier(carrier: Carrier): Boolean {
    val carrierExists = carriers.findOneById(carrier.id) != null
    return if (carrierExists) {
        carriers.updateOneById(carrier.id, carrier).wasAcknowledged()
    } else {
        carriers.insertOne(carrier).wasAcknowledged()
    }
}

suspend fun deleteCarrierForUser(email: String, carrierId: String): Boolean {
    val carrier = carriers.findOne(Carrier::id eq carrierId, Carrier::owners contains email)
    carrier?.let { carrier ->
        if (carrier.owners.size > 1) {
            val newOwners = carrier.owners - email
            val updateResult = carriers.updateOne(Carrier::id eq carrier.id, setValue(Carrier::owners, newOwners))
            return updateResult.wasAcknowledged()
        }
        return carriers.deleteOneById(carrier.id).wasAcknowledged()
    } ?: return false
}

suspend fun addOwnerToCarrier(carrierId: String, owner: String): Boolean {
    val owners = carriers.findOneById(carrierId)?.owners ?: return false
    return carriers.updateOneById(carrierId, setValue(Carrier::owners, owners + owner)).wasAcknowledged()
}

suspend fun isOwnerOfCarrier(carrierId: String, owner: String): Boolean {
    val carrier = carriers.findOneById(carrierId) ?: return false
    return owner in carrier.owners
}

