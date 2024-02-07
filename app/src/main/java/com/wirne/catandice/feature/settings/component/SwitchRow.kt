package com.wirne.catandice.feature.settings.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.wirne.catandice.ui.theme.CDColor
import com.wirne.catandice.ui.theme.CDTheme

@Composable
fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label)

        Spacer(modifier = Modifier.weight(1f))

        Switch(
            checked = checked,
            onCheckedChange = {
                onCheckedChange()
            },
            colors =
                SwitchDefaults.colors(
                    checkedTrackColor = CDColor.Red,
                    checkedThumbColor = CDColor.Yellow,
                    uncheckedThumbColor = CDColor.Grey,
                ),
        )
    }
}

@Composable
@Preview
private fun Preview() {
    CDTheme {
        Column {
            SwitchRow(
                label = "Label",
                checked = true,
                onCheckedChange = { },
            )
            SwitchRow(
                label = "Label",
                checked = false,
                onCheckedChange = { },
            )
        }
    }
}
