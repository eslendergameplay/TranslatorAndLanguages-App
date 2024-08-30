package com.example.jetpack132

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.jetpack132.languages.LanguagesView
import com.example.jetpack132.translator.TranlateViewModel
import com.example.jetpack132.translator.TranslateView
import com.example.jetpack132.ui.theme.JetPack132Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel:TranlateViewModel by viewModels()
            JetPack132Theme {
                Surface (modifier = Modifier.fillMaxSize(),color = MaterialTheme.colorScheme.background) {
                    TranslateView(viewmodel = viewModel)
                    //LanguagesView()
                    //MyView()
                }
            }
        }
    }
}


@Composable
fun MyView(){
    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        //Desde el strings de res
        Text(text = stringResource(id = R.string.title))
        Text(text = stringResource(id = R.string.subtitle))
    }
}

