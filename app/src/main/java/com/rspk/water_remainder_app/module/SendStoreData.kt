package com.rspk.water_remainder_app.module

data class SendStoreData(
    val startTime:Long,
    val endTime:Long,
    val timeInterval: Int,
    val waterAmount:Int,
)

