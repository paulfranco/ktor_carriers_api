package co.paulfran.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Route(
    val routeNumber: String,
    val stopCount: String,
    val mileage: Double,
    val vehicle: Vehicle,
    val routeRevenue: Double,
    val carrier: Carrier,
    @BsonId
    val id: String = ObjectId().toString()
)