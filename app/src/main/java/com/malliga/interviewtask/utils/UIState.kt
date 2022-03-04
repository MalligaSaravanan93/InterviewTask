package com.malliga.interviewtask.utils

import com.malliga.interviewtask.model.CardData

sealed class UIState {
    class SUCCESS(val success: CardData): UIState()
    class LOADING(val isLoading: Boolean = true) : UIState()
    class ERROR(val error: Throwable): UIState()
}