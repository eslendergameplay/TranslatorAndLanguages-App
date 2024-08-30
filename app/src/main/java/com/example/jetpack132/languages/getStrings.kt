package com.example.jetpack132.languages

import androidx.compose.runtime.Composable

@Composable
fun getStrings():List<Map<String,String>>{
    val en = mapOf("title" to "Hello World","subtitle" to "The World is Yours.")
    val es = mapOf("title" to "Hola Mundo","subtitle" to "El Mundo es Tuyo.")

    return listOf(en,es)
}