package co.paulfran.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Vehicle(
    val make: String,
    val model: String,
    val year: String,
    val color: String,
    val vin: String,
    val license: String,
    val mileage: Double,
    @BsonId
    val id: String = ObjectId().toString()
)