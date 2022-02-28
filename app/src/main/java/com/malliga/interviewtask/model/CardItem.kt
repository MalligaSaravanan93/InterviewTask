package com.malliga.interviewtask.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CardItem(
    @SerializedName("archetype")
    val archetype: String="",
    @SerializedName("card_images")
    val cardImages: List<CardImage>,
    @SerializedName("card_prices")
    val cardPrices: List<CardPrice>,
    @SerializedName("card_sets")
    val cardSets: List<CardSet>,
    @SerializedName("desc")
    val desc: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("race")
    val race: String,
    @SerializedName("type")
    val type: String
):Serializable