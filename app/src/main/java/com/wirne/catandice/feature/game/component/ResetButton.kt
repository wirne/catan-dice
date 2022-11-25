package com.wirne.catandice.feature.game.component

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResetButton(
    reset: () -> Unit
) {
    val toast = rememberResetToast()

    ProvideTextStyle(
        value = MaterialTheme.typography.button
    ) {
        Text(
            modifier = Modifier
                .border(
                    shape = MaterialTheme.shapes.small,
                    border = ButtonDefaults.outlinedBorder,
                )
                .combinedClickable(
                    onLongClick = reset,
                    onClick = toast::show
                )
                .padding(ButtonDefaults.ContentPadding),
            text = "Reset game"
        )
    }
}

@Stable
private class ResetToast(
    context: Context
) : Toast(context) {

    var isShowingToast = false

    init {
        setText("Long press to reset")
        duration = LENGTH_SHORT

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            addCallback(
                object : Callback() {
                    override fun onToastHidden() {
                        super.onToastHidden()
                        isShowingToast = false
                    }

                    override fun onToastShown() {
                        super.onToastShown()
                        isShowingToast = true
                    }
                })
        }
    }

    override fun show() {
        if (!isShowingToast) {
            super.show()
        }
    }
}

@Composable
fun rememberResetToast(): Toast {
    val context = LocalContext.current
    return remember {
        ResetToast(context)
    }
}
