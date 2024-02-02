package com.example.watoon.function

fun translate(english: String): String = when (english) {
    "Monday" -> "월"
    "Tuesday" -> "화"
    "Wednesday" -> "수"
    "Thursday" -> "목"
    "Friday" -> "금"
    "Saturday" -> "토"
    "Sunday" -> "일"
    "finished" -> "완결"
    else -> ""
}

fun stringCut(string: String): String {
    return if(string.length>8){
        string.substring(0..7) + " .."
    }
    else string
}
