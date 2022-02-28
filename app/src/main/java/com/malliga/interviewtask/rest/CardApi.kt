package com.malliga.interviewtask.rest

import com.malliga.interviewtask.model.CardData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CardApi {

    @GET(CARD_LIST)
    suspend fun getCardList(
        @Query("offset")offset:Int,
        @Query("num")number: Int=10
    ): Response<CardData>

    companion object {
        const val BASE_URL = "https://db.ygoprodeck.com/api/v7/"
        private const val CARD_LIST = "cardinfo.php"
    }
}