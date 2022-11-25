package com.wirne.catandice.feature.settings.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.wirne.catandice.ui.theme.*

@Composable
fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = checked,
            onCheckedChange = {
                onCheckedChange()
            },
            colors = SwitchDefaults.colors(
                checkedTrackColor = CDColor.Red,
                checkedThumbColor = CDColor.Yellow,
                uncheckedThumbColor = CDColor.Grey
            )
        )
    }
}
