package com.example.watoon.function

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoginButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(Color(255,203,49))
    ) {
        Text(
            text = text,
            color = Color.Black
        )
    }
}
@Composable
fun MenuButton(
    text: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(2.dp)
    ) {
        Text(text = text)
    }
}