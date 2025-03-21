package dev.mingaliev.wtf

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.jetbrains.compose.resources.stringResource
import wtf.composeapp.generated.resources.Res
import wtf.composeapp.generated.resources.app_name

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
        state = rememberWindowState(
            width = 410.dp,
            height = 920.dp,
        ),
        resizable = true,
    ) {
        App()
    }
}
