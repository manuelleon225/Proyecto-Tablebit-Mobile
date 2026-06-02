# TableBit Mobile

Aplicación Android para gestión de restaurantes, mesas y reservas. Desarrollada en Java con arquitectura MVVM + Repository.

## Requisitos del Sistema

- **Android Studio** Hedgehog (2023.1.1) o superior
- **JDK** 11 o superior
- **Android SDK** API 36 (compileSdk)
- **mínSdk**: 26 (Android 8.0 Oreo)
- **Gradle** 9.2.1 (Wrapper incluido)

## Configuración del Entorno

### 1. Clonar el repositorio

```bash
git clone <url-del-repositorio>
cd TableBitMobile
```

### 2. Abrir en Android Studio

- File > Open > Seleccionar carpeta `TableBitMobile`
- Esperar a que Gradle sincronice las dependencias

### 3. Configurar URL del Backend

Editar el archivo `app/src/main/java/com/tablebit/mobile/data/api/NetworkConfig.java`:

```java
public class NetworkConfig {
    public static final String BASE_URL = "http://127.0.0.1:8000/";  // Cambiar para producción
    public static final String STORAGE_URL = BASE_URL + "storage/";
    public static final String API_URL = BASE_URL + "api/";
}
```

### 4. Compilar y Ejecutar

```bash
./gradlew assembleDebug    # Compilar APK de depuración
./gradlew installDebug     # Instalar en dispositivo conectado
```

O desde Android Studio: Run > Run 'app'

## Arquitectura

### MVVM + Repository

```
┌─────────────────────────────────────────────────────────┐
│                   CAPA DE PRESENTACIÓN                   │
│   (Activities, Fragmentos, Adaptadores)                  │
│   Observan ViewModels via LiveData                       │
├─────────────────────────────────────────────────────────┤
│                   CAPA DE VIEWMODEL                      │
│   (AuthViewModel, RestauranteViewModel, MesaViewModel,   │
│    ReservaViewModel, PerfilViewModel,                    │
│    AdminDashboardViewModel)                              │
│   Manejan lógica de negocio y estado de UI               │
├─────────────────────────────────────────────────────────┤
│                   CAPA DE REPOSITORIO                    │
│   (AuthRepository, RestauranteRepository,                │
│    MesaRepository, ReservaRepository,                    │
│    DashboardRepository)                                  │
│   Abstracción entre API y ViewModels                     │
├─────────────────────────────────────────────────────────┤
│                   CAPA DE DATOS                          │
│   RetrofitClient, ApiService, AuthInterceptor            │
│   NetworkConfig, ErrorUtils, ConnectivityChecker         │
│   TokenManager, SessionManager                           │
├─────────────────────────────────────────────────────────┤
│              SERVIDOR API REST (Laravel)                 │
└─────────────────────────────────────────────────────────┘
```

### Capas del Proyecto

| Capa | Paquete | Responsabilidad |
|------|---------|-----------------|
| **Modelo** | `data.model` | POJOs con Gson para serialización |
| **API** | `data.api` | Retrofit, interceptors, config |
| **Repositorio** | `data.repository` | Abstracción de llamadas API |
| **Local** | `data.local` | Utilidades de conectividad |
| **Sesión** | `session` | Token y sesión de usuario |
| **ViewModel** | `ui.viewmodel` | Lógica de negocio con LiveData |
| **UI** | `ui.*` | Activities, Fragmentos, Adaptadores |

## Estructura de Paquetes

```
com.tablebit.mobile
├── MainActivity.java              - Router principal (redirige según rol)
├── data/
│   ├── api/                        - Retrofit, ApiService, interceptors
│   ├── local/                      - ConnectivityChecker
│   ├── model/                      - 13 clases de modelo
│   └── repository/                 - 5 repositorios
├── session/                        - TokenManager, SessionManager
└── ui/
    ├── admin/                      - 13 clases (dashboard admin)
    ├── auth/                       - Login, Register
    ├── cliente/                    - 18 clases (home cliente)
    ├── mesas/                      - Vista de mesas
    ├── onboarding/                 - Tutorial inicial
    ├── perfil/                     - Perfil de usuario
    ├── reservas/                   - Gestión de reservas
    ├── restaurantes/               - Listado y detalle
    ├── splash/                     - Pantalla de carga
    └── viewmodel/                  - 7 ViewModels
```

## Pruebas

### Pruebas Unitarias (JVM)

```bash
./gradlew testDebugUnitTest
```

**Tests incluidos:**

| Test | Archivo | Descripción |
|------|---------|-------------|
| ModelTest | `model/ModelTest.java` | Pruebas de POJOs (constructores, getters) |
| DashboardDataTest | `model/DashboardDataTest.java` | Pruebas de lógica de DashboardData |
| CalendarioResponseTest | `model/CalendarioResponseTest.java` | Pruebas de CalendarioResponse/Evento |
| NetworkConfigTest | `api/NetworkConfigTest.java` | Verifica URLs de configuración |
| ErrorUtilsTest | `api/ErrorUtilsTest.java` | Pruebas de manejo de errores |
| TokenManagerTest | `session/TokenManagerTest.java` | Pruebas de sesión con Mockito |
| AuthViewModelTest | `viewmodel/AuthViewModelTest.java` | Pruebas de estado de ViewModel |
| BaseViewModelTest | `viewmodel/BaseViewModelTest.java` | Pruebas de BaseViewModel |

### Pruebas Instrumentadas (Android)

```bash
./gradlew connectedAndroidTest
```

## Dependencias Principales

| Librería | Versión | Propósito |
|----------|---------|-----------|
| Retrofit 2 | 2.9.0 | Cliente HTTP REST |
| OkHttp 3 | 4.12.0 | Transporte HTTP |
| Gson | 2.10.1 | Serialización JSON |
| Glide | 4.16.0 | Carga de imágenes |
| Material Design | 1.10.0 | Componentes UI |
| ViewModel + LiveData | 2.6.2 | Arquitectura MVVM |
| SDP + SSP | 1.1.1 | Diseño responsive |
| Mockito | 5.12.0 | Mock para pruebas |

## Generar APK

```bash
./gradlew assembleRelease    # APK firmado para producción
./gradlew assembleDebug      # APK de depuración
```

El APK se genera en: `app/build/outputs/apk/debug/app-debug.apk`

## Generar Diagramas (PlantUML)

Los diagramas están en `docs/diagrams/` en formato PlantUML:
- `diagrama-clases.puml` - Diagrama de clases UML
- `diagrama-paquetes.puml` - Diagrama de paquetes
- `diagrama-componentes.puml` - Diagrama de componentes
- `mapa-navegacion.puml` - Mapa de navegación

Para generar imágenes, usar:
- Plugin PlantUML en VSCode
- PlantUML online: https://www.plantuml.com/plantuml/
- Cliente PlantUML: `java -jar plantuml.jar diagrama-clases.puml`

## Metodología de Desarrollo

- **Arquitectura**: MVVM (Model-View-ViewModel) con Repository Pattern
- **Patrones**: Singleton (RetrofitClient), Observer (LiveData), Repository
- **Lenguaje**: Java 11
- **Control de versiones**: Git
- **API**: RESTful con autenticación Bearer Token
