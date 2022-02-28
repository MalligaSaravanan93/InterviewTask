package com.malliga.interviewtask.model

import com.google.gson.annotations.SerializedName

data class CardData(
    @SerializedName("data")
    val `data`: MutableList<CardItem>,
    @SerializedName("meta")
    val meta: Meta
)