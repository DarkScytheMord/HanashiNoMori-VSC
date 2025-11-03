# 📱 Integración Android Studio - HanashiNoMori Backend

Esta guía te ayudará a conectar tu aplicación Android con el backend Spring Boot para usar las APIs de **Registro** y **Login**.

---

## 🎯 Requisitos Previos

- ✅ Android Studio instalado (versión Arctic Fox o superior)
- ✅ Backend corriendo en `http://localhost:8080`
- ✅ Dispositivo Android o Emulador configurado
- ✅ Conocimientos básicos de Kotlin/Java

---

## 📦 Paso 1: Configurar Dependencias en Android

### 1.1. Abrir `build.gradle.kts` (Module: app)

Agrega estas dependencias:

```kotlin
dependencies {
    // Retrofit - Cliente HTTP
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // OkHttp - Logging interceptor (para debug)
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    
    // Coroutines - Para llamadas asíncronas
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    
    // ViewModel y LiveData
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    
    // Opcionales pero recomendados
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.fragment:fragment-ktx:1.6.1")
}
```

### 1.2. Sincronizar Gradle

Click en **"Sync Now"** o **File → Sync Project with Gradle Files**

---

## 🌐 Paso 2: Configurar Permisos de Internet

### 2.1. Editar `AndroidManifest.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.tuempresa.hanashinomori">

    <!-- Permiso de Internet -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

    <!-- Permitir tráfico HTTP en texto plano (solo para desarrollo) -->
    <application
        android:usesCleartextTraffic="true"
        android:networkSecurityConfig="@xml/network_security_config"
        ...>
        
        <activity ...>
        </activity>
    </application>
</manifest>
```

### 2.2. Crear archivo `res/xml/network_security_config.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <domain-config cleartextTrafficPermitted="true">
        <!-- Para conectar con localhost desde emulador -->
        <domain includeSubdomains="true">10.0.2.2</domain>
        <!-- Para conectar desde dispositivo físico (usa tu IP local) -->
        <domain includeSubdomains="true">192.168.1.XXX</domain>
    </domain-config>
</network-security-config>
```

⚠️ **IMPORTANTE:** Cambia `192.168.1.XXX` por tu IP local (usa `ipconfig` en Windows)

---

## 📝 Paso 3: Crear Modelos de Datos (Data Classes)

### 3.1. Crear `models/RegisterRequest.kt`

```kotlin
package com.tuempresa.hanashinomori.models

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val fullName: String
)
```

### 3.2. Crear `models/LoginRequest.kt`

```kotlin
package com.tuempresa.hanashinomori.models

data class LoginRequest(
    val username: String,
    val password: String
)
```

### 3.3. Crear `models/AuthResponse.kt`

```kotlin
package com.tuempresa.hanashinomori.models

data class AuthResponse(
    val success: Boolean,
    val message: String,
    val data: AuthData?
)

data class AuthData(
    val token: String,
    val refreshToken: String,
    val tokenType: String,
    val id: Int,
    val username: String,
    val email: String,
    val roles: List<String>,
    val createdAt: String
)
```

---

## 🔌 Paso 4: Crear la API Interface (Retrofit)

### 4.1. Crear `api/AuthApiService.kt`

```kotlin
package com.tuempresa.hanashinomori.api

import com.tuempresa.hanashinomori.models.AuthResponse
import com.tuempresa.hanashinomori.models.LoginRequest
import com.tuempresa.hanashinomori.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>
    
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>
    
    @POST("api/auth/logout")
    suspend fun logout(): Response<AuthResponse>
}
```

---

## 🏗️ Paso 5: Configurar Retrofit Client

### 5.1. Crear `api/RetrofitClient.kt`

```kotlin
package com.tuempresa.hanashinomori.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    
    // IMPORTANTE: Cambia esta URL según tu configuración
    private const val BASE_URL = "http://10.0.2.2:8080/" // Para emulador Android
    // private const val BASE_URL = "http://192.168.1.XXX:8080/" // Para dispositivo físico
    
    // Logging interceptor para debug
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    
    // OkHttp Client con configuraciones
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    // Instancia de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    // API Service
    val authApiService: AuthApiService by lazy {
        retrofit.create(AuthApiService::class.java)
    }
}
```

### 5.2. URLs según tu entorno

| Entorno | URL Base | Cuándo usar |
|---------|----------|-------------|
| `http://10.0.2.2:8080/` | Emulador Android | Más común para desarrollo |
| `http://192.168.1.XXX:8080/` | Dispositivo físico | Usa tu IP local (ejecuta `ipconfig`) |
| `http://localhost:8080/` | ❌ NO funciona | Android no puede acceder a localhost |

---

## 🗄️ Paso 6: Crear Repository

### 6.1. Crear `repository/AuthRepository.kt`

```kotlin
package com.tuempresa.hanashinomori.repository

import com.tuempresa.hanashinomori.api.RetrofitClient
import com.tuempresa.hanashinomori.models.AuthResponse
import com.tuempresa.hanashinomori.models.LoginRequest
import com.tuempresa.hanashinomori.models.RegisterRequest
import retrofit2.Response

class AuthRepository {
    
    private val apiService = RetrofitClient.authApiService
    
    suspend fun register(
        username: String,
        email: String,
        password: String,
        fullName: String
    ): Response<AuthResponse> {
        val request = RegisterRequest(username, email, password, fullName)
        return apiService.register(request)
    }
    
    suspend fun login(
        username: String,
        password: String
    ): Response<AuthResponse> {
        val request = LoginRequest(username, password)
        return apiService.login(request)
    }
    
    suspend fun logout(): Response<AuthResponse> {
        return apiService.logout()
    }
}
```

---

## 🎭 Paso 7: Crear ViewModel

### 7.1. Crear `viewmodel/AuthViewModel.kt`

```kotlin
package com.tuempresa.hanashinomori.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tuempresa.hanashinomori.models.AuthData
import com.tuempresa.hanashinomori.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    
    private val repository = AuthRepository()
    
    // LiveData para observar el estado de autenticación
    private val _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult> = _authResult
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun register(username: String, email: String, password: String, fullName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.register(username, email, password, fullName)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val authData = response.body()?.data
                    _authResult.value = AuthResult.Success(authData)
                } else {
                    val errorMsg = response.body()?.message ?: "Error en el registro"
                    _authResult.value = AuthResult.Error(errorMsg)
                }
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error("Error de conexión: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun login(username: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = repository.login(username, password)
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val authData = response.body()?.data
                    _authResult.value = AuthResult.Success(authData)
                } else {
                    val errorMsg = response.body()?.message ?: "Usuario o contraseña incorrectos"
                    _authResult.value = AuthResult.Error(errorMsg)
                }
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error("Error de conexión: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}

// Sealed class para manejar resultados
sealed class AuthResult {
    data class Success(val data: AuthData?) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
```

---

## 🎨 Paso 8: Crear UI - Activity de Login

### 8.1. Crear `activity_login.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:padding="24dp">

    <TextView
        android:id="@+id/tvTitle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="HanashiNoMori"
        android:textSize="32sp"
        android:textStyle="bold"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="60dp"/>

    <EditText
        android:id="@+id/etUsername"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:hint="Username"
        android:inputType="text"
        app:layout_constraintTop_toBottomOf="@id/tvTitle"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="60dp"/>

    <EditText
        android:id="@+id/etPassword"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:hint="Password"
        android:inputType="textPassword"
        app:layout_constraintTop_toBottomOf="@id/etUsername"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="16dp"/>

    <Button
        android:id="@+id/btnLogin"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="LOGIN"
        app:layout_constraintTop_toBottomOf="@id/etPassword"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="24dp"/>

    <Button
        android:id="@+id/btnRegister"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:text="REGISTRARSE"
        android:backgroundTint="@android:color/darker_gray"
        app:layout_constraintTop_toBottomOf="@id/btnLogin"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="16dp"/>

    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:visibility="gone"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"/>

    <TextView
        android:id="@+id/tvError"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:textColor="@android:color/holo_red_dark"
        android:visibility="gone"
        app:layout_constraintTop_toBottomOf="@id/btnRegister"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        android:layout_marginTop="16dp"/>

</androidx.constraintlayout.widget.ConstraintLayout>
```

### 8.2. Crear `LoginActivity.kt`

```kotlin
package com.tuempresa.hanashinomori

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tuempresa.hanashinomori.databinding.ActivityLoginBinding
import com.tuempresa.hanashinomori.viewmodel.AuthResult
import com.tuempresa.hanashinomori.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupObservers()
        setupListeners()
    }
    
    private fun setupObservers() {
        // Observar estado de carga
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !isLoading
            binding.btnRegister.isEnabled = !isLoading
        }
        
        // Observar resultado de autenticación
        viewModel.authResult.observe(this) { result ->
            when (result) {
                is AuthResult.Success -> {
                    val authData = result.data
                    Toast.makeText(this, "✅ Login exitoso: ${authData?.username}", Toast.LENGTH_SHORT).show()
                    
                    // Guardar token en SharedPreferences
                    saveToken(authData?.token ?: "")
                    
                    // Ir a MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is AuthResult.Error -> {
                    binding.tvError.text = "❌ ${result.message}"
                    binding.tvError.visibility = View.VISIBLE
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            binding.tvError.visibility = View.GONE
            viewModel.login(username, password)
        }
        
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    
    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences("HanashiPrefs", MODE_PRIVATE)
        sharedPreferences.edit().putString("auth_token", token).apply()
    }
}
```

---

## 📝 Paso 9: Crear Activity de Registro

### 9.1. Crear `activity_register.xml`

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fillViewport="true">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="24dp">

        <TextView
            android:id="@+id/tvTitle"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Registro"
            android:textSize="32sp"
            android:textStyle="bold"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginTop="40dp"/>

        <EditText
            android:id="@+id/etUsername"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:hint="Username"
            android:inputType="text"
            app:layout_constraintTop_toBottomOf="@id/tvTitle"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginTop="40dp"/>

        <EditText
            android:id="@+id/etEmail"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:hint="Email"
            android:inputType="textEmailAddress"
            app:layout_constraintTop_toBottomOf="@id/etUsername"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginTop="16dp"/>

        <EditText
            android:id="@+id/etFullName"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:hint="Nombre Completo"
            android:inputType="textPersonName"
            app:layout_constraintTop_toBottomOf="@id/etEmail"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginTop="16dp"/>

        <EditText
            android:id="@+id/etPassword"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:hint="Password"
            android:inputType="textPassword"
            app:layout_constraintTop_toBottomOf="@id/etFullName"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginTop="16dp"/>

        <Button
            android:id="@+id/btnRegister"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="CREAR CUENTA"
            app:layout_constraintTop_toBottomOf="@id/etPassword"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginTop="24dp"/>

        <Button
            android:id="@+id/btnBackToLogin"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="Volver a Login"
            android:backgroundTint="@android:color/darker_gray"
            app:layout_constraintTop_toBottomOf="@id/btnRegister"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginTop="16dp"/>

        <ProgressBar
            android:id="@+id/progressBar"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:visibility="gone"
            app:layout_constraintTop_toTopOf="parent"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"/>

        <TextView
            android:id="@+id/tvError"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:textColor="@android:color/holo_red_dark"
            android:visibility="gone"
            app:layout_constraintTop_toBottomOf="@id/btnBackToLogin"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            android:layout_marginTop="16dp"/>

    </androidx.constraintlayout.widget.ConstraintLayout>
</ScrollView>
```

### 9.2. Crear `RegisterActivity.kt`

```kotlin
package com.tuempresa.hanashinomori

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.tuempresa.hanashinomori.databinding.ActivityRegisterBinding
import com.tuempresa.hanashinomori.viewmodel.AuthResult
import com.tuempresa.hanashinomori.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: AuthViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupObservers()
        setupListeners()
    }
    
    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.btnRegister.isEnabled = !isLoading
            binding.btnBackToLogin.isEnabled = !isLoading
        }
        
        viewModel.authResult.observe(this) { result ->
            when (result) {
                is AuthResult.Success -> {
                    Toast.makeText(this, "✅ Registro exitoso!", Toast.LENGTH_SHORT).show()
                    finish() // Volver a LoginActivity
                }
                is AuthResult.Error -> {
                    binding.tvError.text = "❌ ${result.message}"
                    binding.tvError.visibility = View.VISIBLE
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    
    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val fullName = binding.etFullName.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            // Validaciones
            when {
                username.isEmpty() -> {
                    binding.etUsername.error = "Username requerido"
                    return@setOnClickListener
                }
                email.isEmpty() -> {
                    binding.etEmail.error = "Email requerido"
                    return@setOnClickListener
                }
                !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.etEmail.error = "Email inválido"
                    return@setOnClickListener
                }
                fullName.isEmpty() -> {
                    binding.etFullName.error = "Nombre completo requerido"
                    return@setOnClickListener
                }
                password.isEmpty() -> {
                    binding.etPassword.error = "Password requerido"
                    return@setOnClickListener
                }
                password.length < 6 -> {
                    binding.etPassword.error = "Password debe tener al menos 6 caracteres"
                    return@setOnClickListener
                }
            }
            
            binding.tvError.visibility = View.GONE
            viewModel.register(username, email, password, fullName)
        }
        
        binding.btnBackToLogin.setOnClickListener {
            finish()
        }
    }
}
```

---

## ✅ Paso 10: Habilitar ViewBinding (Opcional pero Recomendado)

En `build.gradle.kts` (Module: app), agrega:

```kotlin
android {
    ...
    buildFeatures {
        viewBinding = true
    }
}
```

---

## 🧪 Paso 11: Probar la Integración

### 11.1. Verificar Backend

```powershell
# Asegúrate que el backend esté corriendo
.\mvnw.cmd spring-boot:run

# Verifica que responda
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/login" -Method POST -Headers @{"Content-Type"="application/json"} -Body (@{username="testuser"; password="123456"} | ConvertTo-Json)
```

### 11.2. Obtener tu IP Local

```powershell
# En Windows PowerShell
ipconfig

# Busca "IPv4 Address" de tu adaptador principal
# Ejemplo: 192.168.1.105
```

### 11.3. Configurar URL en Android

Edita `RetrofitClient.kt`:

```kotlin
// Si usas EMULADOR:
private const val BASE_URL = "http://10.0.2.2:8080/"

// Si usas DISPOSITIVO FÍSICO:
private const val BASE_URL = "http://192.168.1.105:8080/" // ⬅️ Usa tu IP
```

### 11.4. Ejecutar la App

1. **Run** tu app de Android Studio
2. Intenta **registrar** un usuario:
   - Username: `androiduser`
   - Email: `android@test.com`
   - Full Name: `Android User`
   - Password: `123456`

3. Verifica en **Logcat** los logs de Retrofit:
   ```
   --> POST http://10.0.2.2:8080/api/auth/register
   {"username":"androiduser","email":"android@test.com","password":"123456","fullName":"Android User"}
   <-- 201 Created
   ```

4. Intenta hacer **login**

---

## 🐛 Troubleshooting Android

### ❌ Error: "Unable to resolve host"

**Causa:** Android no puede conectarse al backend

**Soluciones:**
1. ✅ Verifica que el backend esté corriendo (`http://localhost:8080`)
2. ✅ Usa `10.0.2.2` en emulador (NO `localhost`)
3. ✅ Usa tu IP local en dispositivo físico
4. ✅ Desactiva firewall temporalmente
5. ✅ Verifica `usesCleartextTraffic="true"` en AndroidManifest

### ❌ Error: "CLEARTEXT communication not permitted"

**Solución:** Agrega en `AndroidManifest.xml`:
```xml
android:usesCleartextTraffic="true"
```

### ❌ Error: "Connection refused"

**Solución:** Backend no está corriendo o usa puerto incorrecto

### ❌ Error: "java.net.SocketTimeoutException"

**Solución:** Aumenta timeout en `RetrofitClient.kt`:
```kotlin
.connectTimeout(60, TimeUnit.SECONDS)
```

---

## 📊 Diagrama de Flujo

```
┌─────────────────┐
│  LoginActivity  │
│   (Android)     │
└────────┬────────┘
         │
         │ Username + Password
         ▼
┌─────────────────┐
│  AuthViewModel  │
└────────┬────────┘
         │
         │ Coroutine
         ▼
┌─────────────────┐
│ AuthRepository  │
└────────┬────────┘
         │
         │ Retrofit HTTP POST
         ▼
┌─────────────────┐
│ RetrofitClient  │  ───────►  http://10.0.2.2:8080/api/auth/login
└─────────────────┘
         │
         │ JSON Response
         ▼
┌─────────────────────────┐
│  Spring Boot Backend    │
│  SimpleAuthService      │
│  (password.equals())    │
└─────────────────────────┘
         │
         │ AuthResponse (Token + User Data)
         ▼
┌─────────────────┐
│  LoginActivity  │  ───►  MainActivity
│  (Success!)     │
└─────────────────┘
```

---

## 🎯 Checklist de Integración

- [ ] Dependencias de Retrofit agregadas
- [ ] Permisos de Internet en AndroidManifest
- [ ] Network Security Config creado
- [ ] IP local obtenida (si usas dispositivo físico)
- [ ] Models creados (RegisterRequest, LoginRequest, AuthResponse)
- [ ] AuthApiService creada
- [ ] RetrofitClient configurado con URL correcta
- [ ] AuthRepository creado
- [ ] AuthViewModel creado
- [ ] LoginActivity implementada
- [ ] RegisterActivity implementada
- [ ] Backend corriendo en http://localhost:8080
- [ ] Primera prueba exitosa de registro
- [ ] Primera prueba exitosa de login

---

## 🚀 Próximos Pasos

Una vez que Login y Registro funcionen:

1. **Guardar token en SharedPreferences**
2. **Agregar interceptor para enviar token en headers**
3. **Implementar auto-login si existe token**
4. **Crear MediaApiService para CRUD de medios**
5. **Implementar pantallas de biblioteca**

---

**¡Tu app Android ya puede conectarse al backend! 🎉**
