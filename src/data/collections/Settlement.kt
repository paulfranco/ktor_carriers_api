package co.paulfran.data.collections

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

data class Settlement(
    val settlementNumber: String,
    val startDate: Int,
    val endDate: Int,
    val stopCount: Int,
    val routeCount: Int,
    val settlementAmount: Double,
    @BsonId
    val id: String = ObjectId().toString()
)