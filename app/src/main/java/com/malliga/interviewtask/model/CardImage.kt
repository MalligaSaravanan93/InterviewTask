package com.malliga.interviewtask.model

import com.google.gson.annotations.SerializedName

data class CardImage(
    @SerializedName("id")
    val id: Int,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("image_url_small")
    val imageUrlSmall: String
)