# Pruebas end2end - Orbit QA Automation Framework

Framework de automatización de pruebas End-to-End (E2E) para el aplicativo **Orbit** (Módulo de Conciliación), diseñado bajo el patrón **Page Object Model (POM)** y **BDD**.

Este proyecto implementa estándares de calidad de nivel empresarial, incluyendo ejecución paralela, gestión segura de credenciales y reportes visuales avanzados.

## Tech Stack

* **Lenguaje:** Java 17 (OpenJDK)
* **Build Tool:** Gradle 8.x
* **Core:** Selenium WebDriver 4.x
* **BDD:** Cucumber 7.x + JUnit 5 Platform
* **Reportes:** Allure Reports
* **CI/CD:** GitHub Actions

## Estructura del Proyecto

```text
orbit-qa-end2end/
├── .github/
│   └── workflows/
│       └── ejecucion-pruebas.yml   # Pipeline de CI/CD (GitHub Actions)
├── src/test/
│   ├── java/org/example/
│   │   ├── base/
│   │   │   └── BaseTest.java       # Configuración ThreadSafe del WebDriver
│   │   ├── pages/
│   │   │   ├── LoginPage.java      # Login Híbrido (Local vs CI Secrets)
│   │   │   └── ConciliacionPage.java # Lógica de interacción (Scroll, Waits)
│   │   ├── runners/
│   │   │   └── RunnerTest.java     # Motor de ejecución JUnit 5
│   │   ├── steps/
│   │   │   └── ConciliacionSteps.java # Glue Code (Gherkin -> Java)
│   │   └── utils/                  # Utilidades (ConfigReader, Hooks)
│   └── resources/
│       ├── features/               # Archivos .feature (Escenarios de Prueba)
│       ├── cucumber.properties     # Configuración de logs y plugins
│       └── junit-platform.properties # Configuración de Paralelismo
├── build.gradle                    # Dependencias, Plugins (Retry) y Tareas
└── README.md                       # Documentación del proyecto
```
##  Requisitos Previos
* **Java JDK 17** instalado y configurado en el PATH.

* **Google Chrome** instalado (para ejecución local).

* Acceso a la red/VPN donde reside el ambiente de QA de Orbit.

## Instalación
1. Clona este repositorio:
   ```bash
   git clone <repository-url>
   cd orbit-qa-end2end
    ```
2. Descarga las dependencias del proyecto usando Gradle:
   ```bash
   ./gradlew clean build -x test 
   ```
## Ejecución de Pruebas
### 1. Ejecución Local(paralela)
El framework detecta automáticamente los núcleos de tu CPU y ejecuta múltiples escenarios a la vez.
```bash
./gradlew clean test
```

### 2. Ejecución por Tags
Para correr un grupo específico de pruebas
```bash
./gradlew clean test -Dcucumber.filter.tags="@tagname"
```
## Configuración de Seguridad (CI/CD)
El proyecto utiliza una Lógica Híbrida de Autenticación en **LoginPage.java**:
- **Local:** Usa credenciales de respaldo/configuración.
- **CI/CD:** Extrae credenciales seguras de los Secrets de GitHub Actions.

Para que el Pipeline funcione correctamente, asegúrate de configurar los siguientes Secrets en tu repositorio de GitHub:
- `APP_USERNAME : Usuario de prueba`
- `APP_PASSWORD : Contraseña de prueba`

## Reportes de Pruebas (Allure)
### En Github Actions
1. Ir a la pestaña **Actions** → Seleccionar la ejecución. 
2. Bajar a la sección **Artifacts**.
3. Descargar el archivo `allure-report.zip`.
4. Descomprimir y abrir `index.html` en un navegador.
### Localmente
Para levantar un servidor web temporal con el reporte:
```bash
allure serve build/allure-results
```
## Características Avanzadas
1. **Ejecución Paralela Dinámica:**
 - Implementada mediante `JUnit 5 Platform Engine`.
 - Usa `ThreadLocal<WebDriver>` en `BaseTest.java` para aislar cada navegador y evitar choques de sesión.

2. **Reintentos Automáticos (Self-Healing):**
 - Configurado en `build.gradle` (plugin `org.gradle.test-retry`).
 - Si una prueba falla por intermitencia, se reintenta hasta 2 veces antes de marcarse como error.

3. **Scroll Inteligente:**
 - Solución al error ElementClickInterceptedException. Se usa `JavascriptExecutor` para hacer scroll hasta el elemento antes de interactuar.

## Troubleshooting
### ERROR: "NoTestsDiscoveredException"
 - **Causa probable:** JUnit no encuentra los **.feature**.
 - **Solución:** Verifica que los archivos `.feature` estén en `src/test/resources/features/` y que el `RunnerTest.java` tenga la ruta correcta.

### ERROR: "TimeoutException"
 - **Causa probable:** Elementos no cargan a tiempo.
 - **Solución:** Aumenta los tiempos de espera explícitos en `ConciliacionPage.java` o revisa la estabilidad del ambiente de pruebas.

### ERROR: "ElementNotInteractable"
 - **Causa probable:** El elemento está oculto o cubierto.
 - **Solución:** Asegúrate de que el elemento sea visible. Si es necesario, usa el scroll inteligente implementado.


##  Archivos Clave
| Archivo                     | Propósito |
|-----------------------------|-----------|
| `BaseTest.java`             | Inicializa el WebDriver usando ThreadLocal para soportar ejecución paralela segura. |
| `ConciliacionPage.java`     | Contiene los selectores (Locators) y la lógica de interacción (Scroll, Clics JS). |
| `RunnerTest.java `          | Punto de entrada para JUnit 5. Configura el plugin de Cucumber. |
| `ejecucion-pruebas.yml`     | Define los pasos del Pipeline de Integración Continua en GitHub Actions. |
| `junit-platform.properties` | Activa la estrategia de ejecución paralela dinámica. |