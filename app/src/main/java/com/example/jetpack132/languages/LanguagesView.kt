package com.example.jetpack132.languages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.dataStore
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun LanguagesView(){
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = LanguagesStore(context)
    val indexLang by dataStore.getIndexLang.collectAsState(initial = 0)
    val items = listOf("English","Spanish")
    val languages = getStrings()
    var currentLanguage by remember { mutableStateOf(languages[0])}
    var expand by remember { mutableStateOf(false)}
    var selectedIndex by remember { mutableStateOf(indexLang)}/*o 0*/
    LaunchedEffect(Unit) {
        currentLanguage = languages[indexLang]
    }
    Column (modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        Row (verticalAlignment = Alignment.CenterVertically){
            Text(text = items[selectedIndex])
            IconButton(onClick = {expand = true}) {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
            }
            DropdownMenu(expanded = expand, onDismissRequest = {expand = false}) {
                items.forEachIndexed{
                    index,item->
                    DropdownMenuItem(text = { Text(text = item)}, onClick = {
                        selectedIndex = index
                        expand = false
                        currentLanguage = languages[selectedIndex]
                        scope.launch {
                            dataStore.saveIndexLang(index)
                        }
                    })
                }
            }
        }
        Text(text = currentLanguage["title"].toString(),fontWeight = FontWeight.Bold)
        Text(text = currentLanguage["subtitle"].toString())
    }
}