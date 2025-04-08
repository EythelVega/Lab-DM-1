package com.bav.labdispositivosmovilesbav.screens


import android.view.SurfaceView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas

@Composable
fun VideoCallScreen(rtcEngine: RtcEngine, channelName: String) {
    val context = LocalContext.current

    // Creamos un SurfaceView para la cámara local
    val localSurfaceView = SurfaceView(context)

    // Configuramos el video local usando el SurfaceView
    rtcEngine.setupLocalVideo(VideoCanvas(localSurfaceView, VideoCanvas.RENDER_MODE_HIDDEN, 0))

    // Iniciamos la vista previa de la cámara
    rtcEngine.startPreview()

    // Añadimos la vista de video al layout de Compose
    AndroidView(
        factory = { localSurfaceView },
        modifier = Modifier.fillMaxSize()
    )

    // Dispose effect para limpiar los recursos cuando se destruya la pantalla
    DisposableEffect(Unit) {
        onDispose {
            rtcEngine.stopPreview()
            rtcEngine.leaveChannel()
        }
    }
}
