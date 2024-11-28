package edu.isil.novpush01

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {
    // Método que se llama cuando se crea la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el archivo de diseño para esta actividad.
        setContentView(R.layout.activity_main)

        // Llama al método para solicitar permisos de notificación.
        askNotificationPermission()
        // Llama al método para obtener el nuevo token FCM.
        tokenNew()

        // Establece el listener para aplicar insets de ventanas (barras de sistema).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            // Obtiene los insets de las barras de sistema.
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Aplica padding a la vista con los insets obtenidos.
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            // Retorna los insets.
            insets
        }
    }

    // Método para solicitar permisos de notificación.
    private fun askNotificationPermission() {
        // Comprueba si la versión de Android es TIRAMISU (API 33) o superior.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Comprueba si el permiso de notificación ya está concedido.
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED) {
                // El permiso ya está concedido, no hace nada.
            } else {
                // Solicita el permiso de notificación.
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Crea un lanzador para manejar la solicitud de permisos.
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        // Se ejecuta cuando el usuario responde a la solicitud de permiso.
        if (isGranted) {
            // El permiso fue concedido.
        } else {
            // El permiso fue denegado.
        }
    }

    // Método para obtener el nuevo token FCM.
    private fun tokenNew() {
        // Obtiene el token FCM de manera asíncrona.
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            // Comprueba si la tarea fue exitosa.
            if (!task.isSuccessful) {
                // Imprime un mensaje de error si no se pudo obtener el token.
                Log.w("FCM TOKEN", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Obtiene el nuevo token FCM.
            val token = task.result
            // Imprime el nuevo token en los registros.
            Log.d("FCM TOKEN", token.toString())
        })
    }

}