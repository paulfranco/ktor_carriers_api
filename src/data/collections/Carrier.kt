package co.paulfran.data.collections


import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Carrier(
    @BsonId
    val id: String = ObjectId().toString(),
    val carrierName: String,
    val dotNumber: String,
    val ccPermit: String,
    val mcNumber: String,
    val ein: String,
    val ubi: String,
    val units: Int,
    val contactEmail: String,
    val contactPhoneNumber: String,
    val owners: List<String>
)