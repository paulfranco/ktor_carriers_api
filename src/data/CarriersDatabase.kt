package co.paulfran.data

import co.paulfran.data.collections.Carrier
import co.paulfran.data.collections.User
import org.litote.kmongo.contains
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

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