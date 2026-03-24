package com.example.playscore.viewmodel

import androidx.lifecycle.ViewModel
import com.example.playscore.data.model.Game
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.collections.listOf

class GameViewModel : ViewModel() {
    private val _gamesList = MutableStateFlow(
        listOf(
            Game(1, "Football", "Zeljeznicar - Sarajevo", "3 : 0", "Finished"),
            Game(2, "Basketball", "Dubai - Real Madrid", "In progress", "Playing"),
            Game(3, "Football", "Wales - BiH", "0 - 0", "Upcoming")
        )
    )
    val gamesList : StateFlow<List<Game>> = _gamesList
}