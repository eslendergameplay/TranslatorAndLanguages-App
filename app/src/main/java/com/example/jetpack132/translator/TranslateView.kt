package com.example.jetpack132.translator

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.jetpack132.R
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TranslateView(viewmodel:TranlateViewModel){
    LaunchedEffect(Unit) {
        viewmodel.state
    }
    val state = viewmodel.state
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val languageOptions = viewmodel.languageOptions
    val itemSelection = viewmodel.itemSelection
    var indexSource by remember { mutableIntStateOf(0) }
    var indexTarget by remember{ mutableIntStateOf(1) }
    var expandSource by remember { mutableStateOf(false)}
    var expandTarget by remember { mutableStateOf(false)}
    var selectedSourceLang by remember { mutableStateOf(languageOptions[0])}
    var selectedTargetLang by remember { mutableStateOf(languageOptions[1])}

    val permissionState = rememberPermissionState(permission = Manifest.permission.RECORD_AUDIO)
    SideEffect {
        permissionState.launchPermissionRequest()
    }
    val speechRecognitionLauncher = rememberLauncherForActivityResult(
        contract = SpeechRecognizerContract(),
        onResult = {viewmodel.onValue(it.toString().replace("[","").replace("]","").trimStart())
        })

    val itemsVoice = viewmodel.itemsVoice
    var selectedTargetVoice by remember { mutableStateOf(itemsVoice[1])}
    Column (modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)
        .padding(top = 20.dp), horizontalAlignment = Alignment.CenterHorizontally){
        Row (verticalAlignment = Alignment.CenterVertically){
            DropdownLang(
                itemsSelection = itemSelection,
                selectedIndex = indexSource,
                expand = expandSource,
                onClickExpanded = { expandSource = true},
                onClickDismiss = { expandSource = false }) {index ->
                indexSource = index
                selectedSourceLang = languageOptions[indexSource]
                expandSource = false
            }
            Icon(Icons.Default.ArrowForward, contentDescription = "",modifier = Modifier.padding(start = 15.dp, end = 15.dp))
            DropdownLang(
                itemsSelection = itemSelection,
                selectedIndex = indexTarget,
                expand = expandTarget,
                onClickExpanded = { expandTarget = true },
                onClickDismiss = { expandTarget = false }) {index ->
                indexTarget = index
                selectedTargetLang = languageOptions[indexTarget]
                selectedTargetVoice = itemsVoice[indexTarget]
                expandTarget = false
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        OutlinedTextField(value = state.textToTranslate, onValueChange = {viewmodel.onValue(it)},label = {Text(
            text = "Escribe para traducir..."
        )}, keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onDone = {
            viewmodel.onTranslate(state.textToTranslate,context,selectedSourceLang,selectedTargetLang)
        }),colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent), modifier = Modifier.fillMaxWidth())

        Row(verticalAlignment = Alignment.CenterVertically){
            MainIconButton(icon = R.drawable.mic) {
                if(permissionState.status.isGranted){
                    speechRecognitionLauncher.launch(Unit)
                }else{
                    permissionState.launchPermissionRequest()
                }
            }
            MainIconButton(icon = R.drawable.traducir) {
                viewmodel.onTranslate(state.textToTranslate,context,selectedSourceLang,selectedTargetLang)
            }
            MainIconButton(icon = R.drawable.speech) {
                viewmodel.textToSpeech(context,selectedTargetVoice)
            }
            MainIconButton(icon = R.drawable.delete) {
                viewmodel.clean()
            }
        }

        if(state.isDownloading){
            CircularProgressIndicator()
            Text(text = "Descargando Modelo...espere un momento")
        }else{
            OutlinedTextField(value = state.translateText, onValueChange = {},label = { Text(text = "Texto Traducido a...")}, readOnly = false, colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent),modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
            )
        }
    }
}