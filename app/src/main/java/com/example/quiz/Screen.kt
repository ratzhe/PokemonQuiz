package com.example.quiz

sealed class Screen(val route:String){
    object MainScreen : Screen("main_screen")
    object Question : Screen("question")
    object Leaderboard : Screen("leaderboard")
    object Intro : Screen("intro")
}