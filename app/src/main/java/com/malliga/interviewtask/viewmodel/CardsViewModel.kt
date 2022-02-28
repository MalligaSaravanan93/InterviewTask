package com.malliga.interviewtask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.malliga.interviewtask.rest.CardsRepository
import com.malliga.interviewtask.utils.UIState

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class CardsViewModel(
    private val cardsRepository: CardsRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val coroutineScope: CoroutineScope = CoroutineScope(SupervisorJob() + ioDispatcher)
) : CoroutineScope by coroutineScope, ViewModel() {

    private val _cardsLiveData: MutableLiveData<UIState> = MutableLiveData(UIState.LOADING())
    val cardsLivaData: LiveData<UIState> get() = _cardsLiveData

    fun subscribeToCardsInfo(offset:Int) {
        _cardsLiveData.postValue(UIState.LOADING())

        collectCardInfo()

        launch {
            cardsRepository.getCardsList(offset)
        }
    }

    private fun collectCardInfo() {
        launch {
            cardsRepository.cardList.collect { uiState ->
                when(uiState) {
                    is UIState.LOADING -> { _cardsLiveData.postValue(uiState) }
                    is UIState.SUCCESS -> { _cardsLiveData.postValue(uiState) }
                    is UIState.ERROR -> { _cardsLiveData.postValue(uiState) }
                }
            }
        }
    }
}