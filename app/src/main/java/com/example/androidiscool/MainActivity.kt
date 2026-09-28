package com.example.androidiscool

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    modifier = Modifier.background(Color.Gray)
                ) {

                    Button(
                        onClick = {
                            val displayIntent =
                                Intent(this@MainActivity, MessageActivity::class.java)

                            startActivity(displayIntent)
                        }
                    ) {
                        Text(stringResource(R.string.display))
                    }

                    Button(
                        onClick = {}
                    ) {
                        Text(stringResource(R.string.share))
                    }
                    Button(onClick = {
                        val message = "Привет, Android разработка — это круто!"
                        val shareIntent = Intent(Intent.ACTION_SENDTO)
                        shareIntent.data = Uri.parse("mailto:")
                        shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("yourEmail@ya.ru"))
                        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
                        startActivity(shareIntent)
                    }, content = {
                        Text(stringResource(R.string.share))
                    })
                }
            }
        }
    }
}