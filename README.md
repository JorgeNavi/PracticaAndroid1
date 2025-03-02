# Dragon Ball Android App

¡Bienvenido/a a la **Dragon Ball Android App**! Esta aplicación te permite realizar un **login** de usuario, ver una lista de héroes del universo de Dragon Ball y simular un combate con cualquiera de ellos.


## Descripción General

Esta aplicación está desarrollada en **Android** utilizando **XML** para los diseños de pantalla. Consume servicios de una **API** para:
- Autenticar usuarios (login).
- Obtener la información de los personajes.

Además, incorpora un **simulador de combate** sencillo que muestra la vida del personaje a través de una barra de progreso (**ProgressBar**). También gestiona la persistencia del usuario para mantener su sesión iniciada.

---

## Características Principales

1. **Diseños en XML**  
   - Cada pantalla está diseñada utilizando vistas en XML para un control total sobre la apariencia.

2. **Llamadas de red (API)**  
   - Se realiza una llamada para el login, validando usuario y contraseña.
   - Se obtienen los datos de los personajes (nombres, imágenes, etc.) desde un servicio remoto.

3. **Listado de Héroes**  
   - Muestra los personajes disponibles en una lista con su nombre y barra de vida inicial.

4. **Simulador de Combate**  
   - Al seleccionar un personaje, se accede a una pantalla de detalle donde se simula el combate:
     - Una **ProgressBar** representa la vida del personaje.
     - Dos botones permiten “golpear” (disminuye la vida) y “curar” (incrementa la vida).

5. **Persistencia de Usuario**  
   - Tras iniciar sesión, se almacena la información del usuario para que no sea necesario volver a loguearse cada vez.

---

## Requisitos

- **Android Studio** (o un IDE equivalente para desarrollo Android).
- **Gradle** (generalmente viene incluido con Android Studio).
- **Dispositivo o Emulador** con versión de Android adecuada (API 21 o superior, por ejemplo).

---

## Instalación

1. **Clonar o descargar** este repositorio en tu equipo.
2. **Abrir** la carpeta del proyecto con Android Studio.
3. **Configurar** las credenciales o URL de la API en el archivo que corresponda (por ejemplo, en `build.gradle` o en un archivo de constantes).
4. **Ejecutar** el proyecto en un emulador o dispositivo físico.

---

## Uso de la App

1. **Inicio de sesión**  
   - Al abrir la aplicación, se muestra la pantalla de login.  
   - Introduce tu usuario y contraseña (validación vía API).

2. **Listado de Personajes**  
   - Una vez logueado, se muestra una lista de personajes con sus imágenes y una barra de vida inicial.

3. **Detalle de Personaje**  
   - Selecciona cualquier personaje de la lista para acceder a su pantalla de detalle.  
   - Podrás simular un combate utilizando los botones para “golpear” o “curar” al personaje. La barra de progreso reflejará los cambios en la vida.

4. **Cerrar Sesión (opcional)**  
   - Dependiendo de tu implementación, es posible que exista un botón de “Logout” para cerrar la sesión y eliminar la persistencia.

---

## Capturas de Pantalla

### Pantalla de Login
![Captura de pantalla 2025-03-02 090350](https://github.com/user-attachments/assets/99972116-a413-43f6-a9af-65fee5757cc8)


> *Captura de pantalla mostrando el formulario para ingresar usuario y contraseña, con opción de “remember me” y botón de “login”.*

---

### Listado de Personajes
![Captura de pantalla 2025-03-02 090428](https://github.com/user-attachments/assets/ab24c2d6-144c-4608-bd90-7d44788c2daa)


> *Captura de pantalla mostrando la lista de personajes, con su nombre e imagen, además de una barra de vida inicial.*

---

### Detalle de Personaje
![Captura de pantalla 2025-03-02 090443](https://github.com/user-attachments/assets/e1f529c6-27cb-4bd9-b3ec-502f6a9584cc)


> *En esta pantalla se ve la imagen del personaje, su nombre, su barra de vida y los botones para golpear o curar.*


¡Disfruta explorando el universo de Dragon Ball en esta aplicación de Android y no dudes en aportar mejoras o reportar problemas!
