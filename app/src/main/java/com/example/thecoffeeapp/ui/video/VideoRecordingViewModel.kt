package com.example.thecoffeeapp.ui.video

import android.app.Application
import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

class VideoRecordingViewModel(application: Application) : AndroidViewModel(application) {
    private val _isRecording = MutableLiveData(false)
    val isRecording: LiveData<Boolean> = _isRecording

    private val _isPaused = MutableLiveData(false)
    val isPaused: LiveData<Boolean> = _isPaused

    private val _hasRecording = MutableLiveData(false)
    val hasRecording: LiveData<Boolean> = _hasRecording

    private val _recordingTime = MutableLiveData("00:00")
    val recordingTime: LiveData<String> = _recordingTime

    private val _permissionsGranted = MutableLiveData(false)
    val permissionsGranted: LiveData<Boolean> = _permissionsGranted

    private var videoCapture: VideoCapture<Recorder>? = null
    private var recording: Recording? = null
    private var recordingStartTime: Long = 0
    private var recordingPauseTime: Long = 0
    private var totalPausedTime: Long = 0

    private val mainExecutor: Executor by lazy { ContextCompat.getMainExecutor(getApplication()) }

    fun setPermissionsGranted(granted: Boolean) {
        _permissionsGranted.value = granted
    }

    fun startCamera(
        previewView: androidx.camera.view.PreviewView,
        lifecycleOwner: androidx.lifecycle.LifecycleOwner
    ) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(getApplication())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(previewView.surfaceProvider)

            val recorder = Recorder.Builder()
                .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
                .build()
            videoCapture = VideoCapture.withOutput(recorder)

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    videoCapture
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, mainExecutor)
    }

    fun toggleRecording() {
        if (_isRecording.value == true) {
            stopRecording()
        } else {
            startRecording()
        }
    }

    private fun startRecording() {
        val videoCapture = this.videoCapture ?: return

        val name = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.getDefault())
            .format(System.currentTimeMillis())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CoffeeApp")
            }
        }

        val mediaStoreOutputOptions = MediaStoreOutputOptions
            .Builder(getApplication<Application>().contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            .setContentValues(contentValues)
            .build()

        recording = videoCapture.output
            .prepareRecording(getApplication(), mediaStoreOutputOptions)
            .apply { withAudioEnabled() }
            .start(mainExecutor) { event ->
                when(event) {
                    is VideoRecordEvent.Start -> {
                        _isRecording.postValue(true)
                        recordingStartTime = System.currentTimeMillis()
                        startTimer()
                    }
                    is VideoRecordEvent.Finalize -> {
                        if (event.hasError()) {
                            recording?.close()
                            recording = null
                        } else {
                            _hasRecording.postValue(true)
                        }
                        _isRecording.postValue(false)
                        _isPaused.postValue(false)
                    }
                }
            }
    }

    private fun stopRecording() {
        recording?.stop()
        recording = null
    }

    fun togglePauseResume() {
        if (_isPaused.value == true) {
            resumeRecording()
        } else {
            pauseRecording()
        }
    }

    private fun pauseRecording() {
        recording?.pause()
        recordingPauseTime = System.currentTimeMillis()
        _isPaused.postValue(true)
    }

    private fun resumeRecording() {
        recording?.resume()
        totalPausedTime += System.currentTimeMillis() - recordingPauseTime
        _isPaused.postValue(false)
    }

    private fun startTimer() {
        viewModelScope.launch(Dispatchers.IO) {
            while (_isRecording.value == true) {
                if (_isPaused.value != true) {
                    val currentTime = System.currentTimeMillis()
                    val elapsedTime = currentTime - recordingStartTime - totalPausedTime
                    val seconds = (elapsedTime / 1000) % 60
                    val minutes = (elapsedTime / (1000 * 60)) % 60
                    withContext(Dispatchers.Main) {
                        _recordingTime.value = String.format("%02d:%02d", minutes, seconds)
                    }
                }
                kotlinx.coroutines.delay(1000)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopRecording()
    }
}
