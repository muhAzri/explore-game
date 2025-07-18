package com.zrifapps.exploregame.features

import android.content.Context
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DynamicModuleManager @Inject constructor(
    private val context: Context
) {
    private val splitInstallManager = SplitInstallManagerFactory.create(context)


    sealed class ModuleInstallState {
        object NotInstalled : ModuleInstallState()
        object Installing : ModuleInstallState()
        data class Downloaded(val progress: Int) : ModuleInstallState()
        object Installed : ModuleInstallState()
        data class Failed(val error: String) : ModuleInstallState()
        object Cancelled : ModuleInstallState()
    }

    fun isModuleInstalled(moduleName: String): Boolean {
        val isInstalled = splitInstallManager.installedModules.contains(moduleName)
        return isInstalled
    }

    fun installModule(moduleName: String): Flow<ModuleInstallState> = callbackFlow {

        if (isModuleInstalled(moduleName)) {
            trySend(ModuleInstallState.Installed)
            close()
            return@callbackFlow
        }

        val request = SplitInstallRequest.newBuilder()
            .addModule(moduleName)
            .build()

        val listener = object : SplitInstallStateUpdatedListener {
            override fun onStateUpdate(state: SplitInstallSessionState) {

                when (state.status()) {
                    SplitInstallSessionStatus.PENDING -> {
                        trySend(ModuleInstallState.Installing)
                    }

                    SplitInstallSessionStatus.DOWNLOADING -> {
                        val progress = if (state.totalBytesToDownload() > 0) {
                            (100 * state.bytesDownloaded() / state.totalBytesToDownload()).toInt()
                        } else 0
                        trySend(ModuleInstallState.Downloaded(progress))
                    }

                    SplitInstallSessionStatus.INSTALLING -> {
                        trySend(ModuleInstallState.Installing)
                    }

                    SplitInstallSessionStatus.INSTALLED -> {
                        trySend(ModuleInstallState.Installed)
                        splitInstallManager.unregisterListener(this)
                        close()
                    }

                    SplitInstallSessionStatus.FAILED -> {
                        val errorMessage =
                            "Module installation failed with error code: ${state.errorCode()}"
                        trySend(ModuleInstallState.Failed(errorMessage))
                        splitInstallManager.unregisterListener(this)
                        close()
                    }

                    SplitInstallSessionStatus.CANCELED -> {
                        trySend(ModuleInstallState.Cancelled)
                        splitInstallManager.unregisterListener(this)
                        close()
                    }

                    SplitInstallSessionStatus.CANCELING -> {
                    }

                    SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                        trySend(ModuleInstallState.Failed("User confirmation required"))
                        splitInstallManager.unregisterListener(this)
                        close()
                    }

                    else -> {
                    }
                }
            }
        }

        splitInstallManager.registerListener(listener)

        splitInstallManager.startInstall(request)
            .addOnSuccessListener {
            }
            .addOnFailureListener { exception ->
                trySend(ModuleInstallState.Failed(exception.message ?: "Unknown error"))
                splitInstallManager.unregisterListener(listener)
                close()
            }

        awaitClose {
            splitInstallManager.unregisterListener(listener)
        }
    }
}
