# ♔ Chess Game - Juego de Ajedrez Completo

Un juego de ajedrez completamente funcional con servidor Java y frontend HTML/CSS/JavaScript.

## 🎯 Características Principales

- ✅ **Juego completo de ajedrez** con todas las reglas implementadas
- 🤖 **Inteligencia artificial** con dos niveles de dificultad
- � **Modo multijugador** para jugar con amigos
- 🌐 **Arquitectura cliente-servidor** con API REST
- 🎨 **Interfaz moderna** y responsive
- ⚡ **Tiempo real** - movimientos instantáneos

## �🚀 Cómo ejecutar el proyecto

### 1. Iniciar el Servidor Java

1. **Navega a la carpeta del backend:**
   ```bash
   cd "Prototipo ajedrez/backend"
   ```

2. **Compila y ejecuta el servidor:**
   ```bash
   javac ServidorAjedrez.java
   java ServidorAjedrez
   ```

3. **Verifica que el servidor esté funcionando:**
   - Deberías ver: `🎯 Servidor de Ajedrez iniciado en puerto 9090`
   - Servidor disponible en: `http://localhost:9090`

### 2. Abrir el Frontend

1. **Navega a la carpeta del frontend:**
   ```bash
   cd "Prototipo ajedrez/frontend"
   ```

2. **Abre en tu navegador:**
   - Doble clic en `index.html` o `modoJuego.html`
   - O usa Live Server en VS Code para mejor experiencia

### 3. ¡A Jugar! 🎮

1. **Selecciona modo de juego:**
   - **👥 Jugar con un amigo:** Partidas entre dos humanos
   - **🤖 Jugar contra bot:** Con dificultad Principiante o Intermedio

2. **En el tablero:**
   - Click en una pieza para seleccionar
   - Click en destino para mover
   - Todas las reglas del ajedrez están implementadas

## 🔧 Arquitectura Técnica

### Backend (Java) - ServidorAjedrez.java
- **Servidor HTTP** standalone en puerto 9090
- **API REST completa** con endpoints principales:
  - `GET /api/estado` - Estado actual del juego
  - `POST /api/mover` - Realizar movimiento
  - `POST /api/reiniciar` - Reiniciar partida
  - `GET /api/configurar` - Configurar modo y dificultad
- **Lógica completa del ajedrez** con validación de movimientos
- **IA del bot** con dos niveles:
  - Principiante: Movimientos aleatorios válidos
  - Intermedio: Estrategia básica con evaluación de posiciones
- **Detección de jaque, jaque mate y tablas**
- **Soporte CORS** para frontend web

### Frontend (HTML/CSS/JavaScript)
- **Interfaz moderna** con diseño responsive y atractivo
- **Tablero interactivo** de 8x8 con piezas Unicode
- **Comunicación HTTP** asíncrona con el servidor
- **Gestión de estados** en tiempo real
- **Selección visual** de piezas y movimientos válidos
- **Soporte completo** para ambos modos de juego
- **Manejo de errores** y reconexión automática

## 📡 API Endpoints del Servidor

| Método | Endpoint | Descripción | Parámetros |
|--------|----------|-------------|------------|
| GET | `/api/estado` | Obtiene estado actual del juego | Ninguno |
| POST | `/api/mover` | Realiza un movimiento | `{"desde": {"fila": 0, "columna": 0}, "hacia": {"fila": 1, "columna": 1}}` |
| POST | `/api/reiniciar` | Reinicia el juego | Ninguno |
| GET | `/api/configurar` | Configura modo y dificultad | `?modo=amigo&nivel=principiante` |
| OPTIONS | `/*` | Soporte CORS | Headers CORS |

## 🎯 Reglas Implementadas

- ✅ **Movimientos básicos** de todas las piezas
- ✅ **Enroque** (corto y largo)
- ✅ **Captura en pasada** (en passant)
- ✅ **Promoción de peones**
- ✅ **Detección de jaque y jaque mate**
- ✅ **Detección de tablas** (empate)
- ✅ **Validación de movimientos legales**
- ✅ **Prevención de auto-jaque**

## 🤖 Inteligencia Artificial

### Nivel Principiante
- Movimientos completamente aleatorios
- Solo considera movimientos válidos
- Ideal para jugadores nuevos

### Nivel Intermedio  
- Evaluación básica de posiciones
- Considera capturas valiosas
- Evita movimientos peligrosos
- Estrategia defensiva básica

## 📁 Estructura de Archivos

```
chess-game-3/
├── README.md
├── Documentacion.md
└── Prototipo ajedrez/
    ├── backend/
    │   ├── ServidorAjedrez.java    # Servidor principal con lógica completa
    │   └── TestServidor.java       # Servidor de pruebas simple
    └── frontend/
        ├── index.html              # Página de inicio
        ├── index.css              # Estilos de inicio
        ├── modoJuego.html         # Selección de modo de juego
        ├── modoJuego.css          # Estilos de selección
        ├── tablero.html           # Interfaz del tablero
        ├── tablero.css            # Estilos del tablero
        ├── main.js                # Lógica de navegación
        └── tablero.js             # Lógica principal del juego
```

## � Solución de Problemas

### El servidor no inicia
- Verifica que el puerto 9090 esté libre
- Asegúrate de tener Java instalado (versión 8 o superior)

### Las piezas no aparecen
- Verifica que el servidor esté corriendo
- Abre la consola del navegador para ver errores
- Revisa la conexión a `http://localhost:9090`

### Los movimientos no funcionan
- Confirma que el servidor esté respondiendo en `/api/estado`
- Verifica la consola del navegador para errores CORS
- Reinicia el servidor si es necesario

## 🎮 ¡Comienza a Jugar!

1. **Ejecuta el servidor:** `java ServidorAjedrez`
2. **Abre el juego:** Doble clic en `modoJuego.html`
3. **Selecciona modo:** Amigo o Bot con dificultad
4. **¡Disfruta jugando ajedrez!** ♔♕♖♗♘♙

---

**Desarrollado usando Java y JavaScript vanilla**

- `GET /api/estado` - Obtener estado actual del juego
- `POST /api/mover` - Realizar un movimiento
- `POST /api/reiniciar` - Reiniciar el juego

## 🎯 Flujo del Juego

1. El frontend se conecta al servidor Java
2. El servidor mantiene el estado del tablero y la lógica del juego
3. Cada movimiento se envía al servidor para validación
4. El servidor responde con el nuevo estado del juego
5. El frontend actualiza la interfaz automáticamente

## 🛠️ Requisitos

- **Java 8 o superior**
- **Navegador web moderno** (Chrome, Firefox, Safari, Edge)
- **Conexión a internet** no requerida (funciona localmente)

## 🎮 ¡Disfruta jugando ajedrez!
