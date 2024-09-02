package com.rspk.water_remainder_app

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rspk.water_remainder_app.broadcast.DoneActionBroadcast
import com.rspk.water_remainder_app.compose.AlertBox
import com.rspk.water_remainder_app.navigation.Navigation
import com.rspk.water_remainder_app.ui.theme.Water_Remainder_AppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "timeSettings")
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var permissionCount by mutableIntStateOf(0)
    private var permissionsGranted by mutableStateOf(false)
    private var promptingPermissions by mutableStateOf(false)
    private val launcher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { mapOfPermissions ->
        mapOfPermissions.forEach { (permission, isGranted) ->
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (permission == android.Manifest.permission.POST_NOTIFICATIONS) {
                    permissionsGranted = isGranted
                    promptingPermissions = !isGranted
                    savePermissionCount(permissionCount++)
                }
            }
            if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                if (permission == android.Manifest.permission.READ_EXTERNAL_STORAGE) {
                    permissionsGranted = isGranted
                    promptingPermissions = !isGranted
                }
            }
        }
    }
    private val settingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            permissionsGranted = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            promptingPermissions = ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val filter = IntentFilter("com.rspk.water_remainder_app.DONE_ACTION")
        registerReceiver(DoneActionBroadcast(), filter)
        super.onCreate(savedInstanceState)
        permissionCount = readPermissionCount()
        enableEdgeToEdge()
        setContent {
            Water_Remainder_AppTheme {
                LaunchedEffect(key1 = !permissionsGranted){
                    if(!permissionsGranted){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            launcher.launch(arrayOf(
                                android.Manifest.permission.POST_NOTIFICATIONS,
                            ))
                        }else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                            launcher.launch(arrayOf(
                                android.Manifest.permission.READ_EXTERNAL_STORAGE
                            ))
                        }else{
                            permissionsGranted = true
                        }
                    }else{
                        promptingPermissions = false
                    }
                }
                if(permissionsGranted) {
                    Navigation()
                }else if(promptingPermissions){
                    AlertBox(
                        text = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                            stringResource(id = R.string.notification_permission_alert)
                        else
                            stringResource(id = R.string.storage_permission_alert),
                        cancelClick = {  this.finish() },
                        confirmClick = {
                            promptingPermissions = false
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                if(permissionCount <= 2){
                                    launcher.launch(arrayOf(
                                        android.Manifest.permission.POST_NOTIFICATIONS,
                                    ))
                                }else{
                                    settingsLauncher.launch(
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                            data = Uri.fromParts("package", packageName, null)
                                        }
                                    )
                                }
                            }
                            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                            launcher.launch(
                                arrayOf(
                                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                                )
                            ) }
                        }
                    )
                }
            }
        }
    }

    private fun readPermissionCount(): Int {
        val file = File(filesDir, "permissionCount.txt")
        return if (file.exists()) {
            try {
                FileInputStream(file).use { fis ->
                    val savedCount = BufferedReader(InputStreamReader(fis)).readLine() ?: "0"
                    savedCount.toIntOrNull() ?: 0
                }
            } catch (e: IOException) {
                e.printStackTrace()
                0
            }
        } else {
            0
        }
    }

    private fun savePermissionCount(count: Int) {
        val file = File(filesDir, "permissionCount.txt")
        try {
            FileOutputStream(file).use { fos ->
                fos.write(count.toString().toByteArray())
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
