package com.example.androidiscool

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri

@Composable
fun SettingsScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    var darkThemeEnabled by remember { mutableStateOf(false) }

    val shareText = stringResource(R.string.share_text)
    val chooserTitle = stringResource(R.string.share_chooser_title)
    val developerEmail = stringResource(R.string.developer_email)
    val developerSubject = stringResource(R.string.developer_email_subject)
    val developerBody = stringResource(R.string.developer_email_body)
    val agreementUrl = stringResource(R.string.agreement_url)
    val unavailableMessage = stringResource(R.string.no_app_available)

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
            Text(text = stringResource(R.string.settings_title), fontSize = 22.sp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = stringResource(R.string.dark_theme), fontSize = 20.sp)
            Switch(
                checked = darkThemeEnabled,
                onCheckedChange = { darkThemeEnabled = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(0xFF3772E7),
                    checkedTrackColor = Color(0xFF9FBBF3),
                    uncheckedThumbColor = Color(0xFFAEAFB4),
                    uncheckedTrackColor = Color(0xFFE6E8EB),
                    checkedBorderColor = Color.Transparent,
                    uncheckedBorderColor = Color.Transparent
                )
            )
        }

        SettingsItem(
            text = stringResource(R.string.share_app),
            icon = Icons.Default.Share,
            onClick = {
                val sendIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                if (sendIntent.resolveActivity(context.packageManager) != null) {
                    context.startSafely(
                        Intent.createChooser(sendIntent, chooserTitle),
                        unavailableMessage
                    )
                } else {
                    context.showUnavailable(unavailableMessage)
                }
            }
        )

        SettingsItem(
            text = stringResource(R.string.write_to_support),
            icon = Icons.Default.Email,
            onClick = {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = "mailto:".toUri()
                    putExtra(Intent.EXTRA_EMAIL, arrayOf(developerEmail))
                    putExtra(Intent.EXTRA_SUBJECT, developerSubject)
                    putExtra(Intent.EXTRA_TEXT, developerBody)
                }
                context.startSafely(intent, unavailableMessage)
            }
        )

        SettingsItem(
            text = stringResource(R.string.user_agreement),
            icon = Icons.Default.ArrowForward,
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW, agreementUrl.toUri())
                context.startSafely(intent, unavailableMessage)
            }
        )
    }
}

private fun Context.startSafely(intent: Intent, unavailableMessage: String) {
    if (intent.resolveActivity(packageManager) == null) {
        showUnavailable(unavailableMessage)
        return
    }
    try {
        startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        showUnavailable(unavailableMessage)
    }
}

private fun Context.showUnavailable(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@Composable
private fun SettingsItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = text, fontSize = 20.sp)
        Icon(imageVector = icon, contentDescription = null)
    }
}
