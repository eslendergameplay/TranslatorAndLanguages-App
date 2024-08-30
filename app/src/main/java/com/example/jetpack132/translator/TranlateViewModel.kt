package com.example.jetpack132.translator

import android.content.Context
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.Locale

class TranlateViewModel : ViewModel() {
    var state by mutableStateOf(TranslateState())
        private set

    val languageOptions = listOf(TranslateLanguage.SPANISH,TranslateLanguage.ENGLISH,TranslateLanguage.ITALIAN,TranslateLanguage.FRENCH)
    val itemSelection = listOf("SPANISH","ENGLISH","ITALIAN","FRENCH")

    val itemsVoice = listOf(Locale.ROOT,Locale.ENGLISH,Locale.ITALIAN,Locale.FRENCH)
    fun onValue(text:String){
        state = state.copy(textToTranslate = text)
    }

    fun onTranslate(text:String,context:Context,sourceLang:String,targetLang:String){
        val options = TranslatorOptions.Builder().setSourceLanguage(sourceLang).setTargetLanguage(targetLang).build()
        val languageTranslator = Translation.getClient(options)
        languageTranslator.translate(text).addOnSuccessListener {translateText->
            state = state.copy(translateText = translateText)
        }.addOnFailureListener {
            Toast.makeText(context,"Descargando Modelo...",Toast.LENGTH_SHORT).show()
            downloadingModel(languageTranslator,context)
        }
    }

    private fun downloadingModel(languageTranslator:Translator,context: Context){
        state = state.copy(isDownloading = true)
        val conditions = DownloadConditions.Builder().requireWifi().build()
        languageTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener {
            Toast.makeText(context,"Modelo Descargado Correctamente.",Toast.LENGTH_SHORT).show()
            state = state.copy(isDownloading = false)
        }.addOnFailureListener {
            Toast.makeText(context,"Modelo Fallido al descargarse o subirse en cuenato a el modelo",Toast.LENGTH_SHORT).show()
        }
    }

    private var  textToSpeech:TextToSpeech? = null

    fun textToSpeech(context:Context,voice:Locale){
        textToSpeech = TextToSpeech(context){
            if(it == TextToSpeech.SUCCESS){
                textToSpeech?.let {
                    textToSpeak->
                    textToSpeak.language = voice
                    textToSpeak.setSpeechRate(1.0f)
                    textToSpeak.speak(state.translateText,TextToSpeech.QUEUE_ADD,null,null)
                }
            }
        }
    }

    fun clean(){
        state = state.copy(
            textToTranslate = "",
            translateText = ""
        )
    }
}