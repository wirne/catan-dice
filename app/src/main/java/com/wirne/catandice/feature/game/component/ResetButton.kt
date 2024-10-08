package com.wirne.catandice.feature.game.component

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResetButton(reset: () -> Unit) {
    val toast = rememberResetToast()

    ProvideTextStyle(
        value = MaterialTheme.typography.bodyMedium,
    ) {
        Text(
            modifier = Modifier
                .border(
                    shape = MaterialTheme.shapes.small,
                    border = ButtonDefaults.outlinedButtonBorder(),
                )
                .clip(MaterialTheme.shapes.small)
                .combinedClickable(
                    onLongClick = reset,
                    onClick = toast::show,
                )
                .padding(ButtonDefaults.ContentPadding),
            text = "Reset game",
        )
    }
}

@Stable
private class ResetToast(
    context: Context,
) {
    private var isShowingToast = false
    private val toast = Toast.makeText(context, "Long press to reset", Toast.LENGTH_SHORT)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            toast.addCallback(
                object : Toast.Callback() {
                    override fun onToastHidden() {
                        super.onToastHidden()
                        isShowingToast = false
                    }

                    override fun onToastShown() {
                        super.onToastShown()
                        isShowingToast = true
                    }
                },
            )
        }
    }

    fun show() {
        if (!isShowingToast) {
            toast.show()
        }
    }
}

@Composable
private fun rememberResetToast(): ResetToast {
    val context = LocalContext.current
    return remember {
        ResetToast(context)
    }
}
