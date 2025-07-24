# 📚 Documentación Técnica Completa - Juego de Ajedrez

## 🏗️ Arquitectura del Sistema

Este proyecto implementa un juego de ajedrez completo usando una arquitectura cliente-servidor:

- **Backend:** Servidor HTTP en Java con lógica completa del ajedrez
- **Frontend:** Aplicación web con HTML5, CSS3 y JavaScript vanilla
- **Comunicación:** API REST con formato JSON y soporte CORS

## � Backend - ServidorAjedrez.java

### Funcionalidades Principales

#### 1. Servidor HTTP Básico
```java
ServerSocket servidor = new ServerSocket(9090);
// Acepta conexiones y crea hilos para cada cliente
```

#### 2. Estado del Juego
```java
private static String[][] tablero = {
    // Tablero 8x8 con piezas Unicode
    {"♜","♞","♝","♛","♚","♝","♞","♜"},
    // ... resto del tablero
};
private static String turnoActual = "blancas";
private static String modoJuego = "amigo"; // o "bot"
private static String nivelBot = "principiante"; // o "intermedio"
```

#### 3. APIs REST Implementadas

##### GET /api/estado
Retorna el estado actual del juego:
```json
{
    "tablero": [["♜","♞",...], ...],
    "turno": "blancas",
    "movimientos": 5,
    "terminado": false,
    "ganador": ""
}
```

##### POST /api/mover
Procesa un movimiento de ajedrez:
```json
// Request
{
    "desde": {"fila": 6, "columna": 4},
    "hacia": {"fila": 4, "columna": 4}
}

// Response
{
    "exito": true,
    "mensaje": "Movimiento realizado: Peón a e4"
}
```

##### POST /api/reiniciar
Reinicia el juego al estado inicial.

##### GET /api/configurar
Configura el modo de juego y dificultad del bot.

### Lógica del Ajedrez Implementada

#### 1. Validación de Movimientos por Pieza

```java
// Ejemplo para el Peón
if (pieza.equals("♙") || pieza.equals("♟")) {
    // Lógica específica del peón
    // - Movimiento inicial de 2 casillas
    // - Captura diagonal
    // - Promoción
    // - En passant
}
```

#### 2. Detección de Jaque
```java
private static boolean estaEnJaque(String color) {
    // Encuentra la posición del rey
    // Verifica si alguna pieza enemiga puede atacarlo
}
```

#### 3. Detección de Jaque Mate
```java
private static boolean esJaqueMate(String color) {
    if (!estaEnJaque(color)) return false;
    // Prueba todos los movimientos posibles
    // Si ninguno saca del jaque, es mate
}
```

#### 4. Inteligencia Artificial del Bot

##### Nivel Principiante
```java
private static void hacerMovimientoBot() {
    // Obtiene todos los movimientos válidos
    // Selecciona uno aleatoriamente
}
## 🎨 Frontend - Estructura y Funcionalidad

### Archivos Principales

#### 1. modoJuego.html
Página de selección de modo de juego:
- Botón "Jugar con un amigo"
- Botón "Jugar contra un bot" con selector de dificultad
- Redirección a tablero.html con parámetros URL

#### 2. tablero.html
Interfaz principal del juego:
- Tablero de ajedrez visual 8x8
- Panel de información de jugadores
- Controles de juego (nuevo juego, configuración)
- Modal para fin de partida

#### 3. tablero.js - Lógica Principal

### Funciones Clave del Frontend

#### 1. Inicialización del Juego
```javascript
function initializeGame() {
    // Lee parámetros de URL (modo, nivel)
    // Configura la interfaz
    // Establece conexión con servidor
    // Obtiene estado inicial
}
```

#### 2. Comunicación con Servidor
```javascript
async function getGameState() {
    const response = await fetch(`${SERVER_URL}/api/estado`);
    const data = await response.json();
    // Actualiza el tablero visual
}

async function makeMove(fromRow, fromCol, toRow, toCol) {
    const moveData = {
        desde: { fila: fromRow, columna: fromCol },
        hacia: { fila: toRow, columna: toCol }
    };
    
    const response = await fetch(`${SERVER_URL}/api/mover`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(moveData)
    });
    
    // Procesa la respuesta
}
```

#### 3. Interfaz de Usuario
```javascript
function handleSquareClick(row, col) {
    if (!selectedSquare) {
        // Seleccionar pieza
        if (gameBoard[row][col] !== '') {
            selectedSquare = { row, col };
            updateBoardDisplay();
        }
    } else {
        // Intentar movimiento
        makeMove(selectedSquare.row, selectedSquare.col, row, col);
    }
}
```

#### 4. Actualización Visual del Tablero
```javascript
function updateBoardDisplay() {
    const squares = boardElement.children;
    
    for (let row = 0; row < 8; row++) {
        for (let col = 0; col < 8; col++) {
            const square = squares[row * 8 + col];
            const piece = gameBoard[row][col];
            
            square.textContent = piece || '';
            
            // Aplicar estilos de selección
            if (selectedSquare && selectedSquare.row === row && selectedSquare.col === col) {
                square.classList.add('selected');
            }
        }
    }
}
```

## 🎯 Reglas de Ajedrez Implementadas

### 1. Movimientos de Piezas

#### Peón (♙♟)
- Movimiento inicial: 1 o 2 casillas hacia adelante
- Movimiento normal: 1 casilla hacia adelante
- Captura: Diagonal hacia adelante
- Promoción: Al llegar al final del tablero
- En passant: Captura especial de peón

#### Torre (♖♜)
- Movimiento: Horizontal y vertical sin límite
- Participación en enroque

#### Alfil (♗♝)
- Movimiento: Diagonal sin límite

#### Caballo (♘♞)
- Movimiento: En "L" (2+1 casillas)
- Única pieza que puede saltar sobre otras

#### Dama (♕♛)
- Movimiento: Combinación de torre y alfil

#### Rey (♔♚)
- Movimiento: 1 casilla en cualquier dirección
- Enroque: Movimiento especial con torre
- No puede moverse a casilla atacada

### 2. Reglas Especiales

#### Enroque
```java
private static boolean puedeEnrocar(String color, boolean esCortoPLargo) {
    // Verifica condiciones:
    // - Rey y torre no se han movido
    // - No hay piezas entre rey y torre
    // - Rey no está en jaque
    // - Rey no pasa por casilla atacada
}
```

#### Promoción de Peón
```java
if (esPromocion(fromRow, toRow, pieza)) {
    // Promociona automáticamente a Dama
    tablero[toRow][toCol] = color.equals("blancas") ? "♕" : "♛";
}
```

#### En Passant
```java
if (esEnPassant(fromRow, fromCol, toRow, toCol)) {
    // Captura el peón enemigo
    tablero[fromRow][toCol] = "";
}
```

## 🔄 Flujo de Comunicación

### 1. Inicio de Partida
```
Frontend                  Backend
   |                        |
   |--- GET /api/estado --->|
   |<-- Estado inicial ------|
   |                        |
```

### 2. Realizar Movimiento
```
Frontend                  Backend
   |                        |
   |--- POST /api/mover ---->|
   |    {desde:{}, hacia:{}} |
   |                        |--- Validar movimiento
   |                        |--- Actualizar estado
   |                        |--- Verificar fin de juego
   |<-- Resultado ----------|
   |                        |
   |--- GET /api/estado --->|--- (Si es modo bot)
   |<-- Estado actual ------|--- Hacer movimiento bot
   |                        |
```

### 3. Manejo de Errores
```javascript
// El frontend implementa fallbacks
try {
    const response = await fetch(`${SERVER_URL}/api/mover`, options);
    // Procesar respuesta exitosa
} catch (error) {
    // Mostrar mensaje de error
    // Intentar reconexión
    // Usar modo offline si es necesario
}
```

## 🚀 Optimizaciones Implementadas

### 1. Manejo de Concurrencia
- Cada cliente HTTP maneja en hilo separado
- Estado compartido protegido implícitamente

### 2. Eficiencia de Validación
- Validación temprana de movimientos básicos
- Cache de posiciones válidas cuando sea posible

### 3. Experiencia de Usuario
- Feedback visual inmediato en selección
- Mensajes de error claros
- Reconexión automática en caso de fallos

### 4. Compatibilidad
- Uso de caracteres Unicode estándar para piezas
- Headers CORS para compatibilidad web
- Fallback a modo local si servidor no disponible

## 🐛 Debugging y Logs

### Backend
```java
System.out.println("🎯 Movimiento: " + pieza + " de " + desde + " a " + hacia);
System.out.println("⚠️ Movimiento inválido: " + razon);
```

### Frontend
```javascript
console.log('🔄 Obteniendo estado del juego...');
console.log('📦 Datos recibidos:', data);
console.log('✅ Movimiento exitoso:', result.mensaje);
```

## 📈 Posibles Mejoras Futuras

1. **Base de datos** para persistir partidas
2. **Sistema de usuarios** y ranking
3. **IA más avanzada** con algoritmos minimax
4. **Interfaz más rica** con animaciones
5. **Modo espectador** para observar partidas
6. **Chat integrado** para comunicación entre jugadores
7. **Análisis de partidas** con sugerencias de mejores movimientos

---

Esta documentación cubre todos los aspectos técnicos del proyecto. Para dudas específicas sobre implementación, revisar los comentarios en el código fuente.
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700&display=swap">
</head>
<body>
    <main>
        <section id="inicio">
            <div class="game-mode-container">
                <!-- Título principal -->
                <h1>Selecciona tu modo de juego</h1>
                
                <!-- Botones principales de selección -->
                <div class="game-buttons">
                    <!-- Botón para jugar con amigo - llama a playWithFriend() -->
                    <button class="game-btn" onclick="playWithFriend()">
                        <span class="btn-icon">👥</span>
                        Jugar con un amigo
                    </button>
                    
                    <!-- Botón para mostrar opciones de bot - llama a showBotOptions() -->
                    <button class="game-btn" onclick="showBotOptions()">
                        <span class="btn-icon">🤖</span>
                        Jugar contra un bot
                    </button>
                    
                    <!-- Botón para volver al inicio - llama a goToIndex() -->
                    <button class="home-btn" onclick="goToIndex()">
                        <span class="btn-icon">🏠</span>
                        Volver al inicio
                    </button>
                </div>
                
                <!-- Selector de dificultad del bot (inicialmente oculto) -->
                <div id="bot-difficulty" class="bot-difficulty-container" style="display: none;">
                    <h2>Selecciona el nivel de dificultad</h2>
                    <div class="difficulty-buttons">
                        <!-- Botón para nivel principiante -->
                        <button class="difficulty-btn" onclick="playWithBot('principiante')">
                            <span class="difficulty-icon">🟢</span>
                            Principiante
                            <small>Nivel fácil</small>
                        </button>
                        
                        <!-- Botón para nivel intermedio -->
                        <button class="difficulty-btn" onclick="playWithBot('intermedio')">
                            <span class="difficulty-icon">🟡</span>
                            Intermedio
                            <small>Nivel medio</small>
                        </button>
                    </div>
                    
                    <!-- Botón para volver - llama a hideBotOptions() -->
                    <button class="back-btn" onclick="hideBotOptions()">Volver</button>
                </div>
            </div>
        </section>
    </main>
    
    <!-- Pie de página -->
    <footer>&copy; 2025 Chess.</footer>
    
    <!-- Script con funciones de navegación -->
    <script src="main.js"></script>
</body>
</html>
```

**Funciones JavaScript utilizadas**:
- `playWithFriend()`: Redirige al tablero con modo=amigo
- `showBotOptions()`: Muestra el selector de dificultad
- `hideBotOptions()`: Oculta el selector de dificultad
- `playWithBot(difficulty)`: Redirige al tablero con modo=bot y nivel específico
- `goToIndex()`: Vuelve a la página principal

---

### 🏁 tablero.html
**Propósito**: Página principal del juego con tablero de ajedrez interactivo

**Estructura principal**:

#### Header (Información de jugadores)
```html
<header class="game-header">
    <div class="header-content">
        <!-- Jugador 1 (Blancas) -->
        <div class="player-info left-player">
            <div class="player-avatar">
                <span class="player-icon">👤</span>
            </div>
            <div class="player-details">
                <h3 class="player-name" id="player1-name">Jugador 1</h3>
                <p class="player-type" id="player1-type">Blancas</p>
            </div>
        </div>
        
        <!-- Controles centrales -->
        <div class="game-controls">
            <div class="game-status">
                <h2 id="game-status">Turno de las Blancas</h2>
            </div>
            <div class="control-buttons">
                <button class="control-btn" onclick="showSettings()">⚙️</button>
            </div>
        </div>
        
        <!-- Jugador 2 (Negras) -->
        <div class="player-info right-player">
            <div class="player-details">
                <h3 class="player-name" id="player2-name">Jugador 2</h3>
                <p class="player-type" id="player2-type">Negras</p>
            </div>
            <div class="player-avatar">
                <span class="player-icon" id="player2-icon">👤</span>
            </div>
        </div>
    </div>
</header>
```

#### Main (Tablero y panel lateral)
```html
<main class="game-main">
    <div class="game-layout">
        <!-- Tablero de ajedrez -->
        <section class="chess-board-container">
            <div class="board-wrapper">
                <!-- Coordenadas laterales izquierdas (números 8-1) -->
                <div class="coordinates-left">
                    <span>8</span>...<span>1</span>
                </div>
                
                <!-- Tablero principal 8x8 -->
                <div class="chess-board" id="chess-board">
                    <!-- Las 64 casillas se generan dinámicamente con JavaScript -->
                </div>
                
                <!-- Coordenadas laterales derechas (números 8-1) -->
                <div class="coordinates-right">
                    <span>8</span>...<span>1</span>
                </div>
            </div>
            
            <!-- Coordenadas inferiores (letras a-h) -->
            <div class="coordinates-bottom">
                <span>a</span>...<span>h</span>
            </div>
        </section>

        <!-- Panel lateral de información -->
        <aside class="game-info">
            <!-- Piezas capturadas -->
            <div class="captured-pieces">
                <h4>Piezas Capturadas</h4>
                <div class="captured-white">
                    <p>Blancas:</p>
                    <div class="pieces" id="captured-white"></div>
                </div>
                <div class="captured-black">
                    <p>Negras:</p>
                    <div class="pieces" id="captured-black"></div>
                </div>
            </div>
            
            <!-- Botones de acción -->
            <div class="action-buttons">
                <button class="action-btn primary" onclick="offerDraw()">
                    Ofrecer Tablas
                </button>
                <button class="action-btn secondary" onclick="goBack()">
                    Volver al Menú
                </button>
            </div>
        </aside>
    </div>
</main>
```

#### Modales
```html
<!-- Modal de configuración -->
<div id="settings-modal" class="modal" style="display: none;">
    <div class="modal-content">
        <span class="close-modal" onclick="closeSettings()">&times;</span>
        <h3>Configuración</h3>
        <div class="setting-item">
            <label>
                <input type="checkbox" id="show-legal-moves"> 
                Mostrar movimientos legales
            </label>
        </div>
        <div class="setting-item">
            <label>
                <input type="checkbox" id="auto-promote-queen" checked> 
                Auto-promoción a Reina
            </label>
        </div>
        <div class="setting-item">
            <label>Sonidos:</label>
            <select id="sound-setting">
                <option value="on">Activados</option>
                <option value="off">Desactivados</option>
            </select>
        </div>
    </div>
</div>

<!-- Modal de fin de juego -->
<div id="game-end-modal" class="modal" style="display: none;">
    <div class="modal-content">
        <h3 id="game-result">¡Jaque Mate!</h3>
        <p id="winner-text">Las Blancas ganan</p>
        <div class="end-game-buttons">
            <button class="action-btn primary" onclick="newGame()">Nuevo Juego</button>
            <button class="action-btn secondary" onclick="goBack()">Volver al Menú</button>
        </div>
    </div>
</div>
```

**Elementos dinámicos importantes**:
- `#player1-name`, `#player2-name`: Nombres de jugadores (cambian según el modo)
- `#player2-icon`: Icono del jugador 2 (👤 para humano, 🤖 para bot)
- `#game-status`: Estado actual del turno
- `#chess-board`: Contenedor del tablero (se genera con JavaScript)
- `#captured-white`, `#captured-black`: Contenedores de piezas capturadas

---

## 📄 ARCHIVOS JAVASCRIPT

### 🎯 main.js
**Propósito**: Manejo de navegación y selección de modo de juego

```javascript
// FUNCIONES DE NAVEGACIÓN Y SELECCIÓN DE MODO

/**
 * Función para jugar con un amigo
 * Redirige al tablero con parámetros: modo=amigo
 */
function playWithFriend() {
    window.location.href = 'tablero.html?modo=amigo';
}

/**
 * Muestra las opciones de dificultad del bot
 * Oculta los botones principales y muestra el selector de dificultad
 */
function showBotOptions() {
    const gameButtons = document.querySelector('.game-buttons');
    const botDifficulty = document.getElementById('bot-difficulty');
    const title = document.querySelector('.game-mode-container h1');
    
    gameButtons.style.display = 'none';
    botDifficulty.style.display = 'block';
    title.textContent = 'Modo Bot';
}

/**
 * Oculta las opciones de dificultad del bot
 * Muestra nuevamente los botones principales
 */
function hideBotOptions() {
    const gameButtons = document.querySelector('.game-buttons');
    const botDifficulty = document.getElementById('bot-difficulty');
    const title = document.querySelector('.game-mode-container h1');
    
    gameButtons.style.display = 'flex';
    botDifficulty.style.display = 'none';
    title.textContent = 'Selecciona tu modo de juego';
}

/**
 * Función para jugar contra un bot
 * @param {string} difficulty - Nivel de dificultad ('principiante' o 'intermedio')
 * Redirige al tablero con parámetros: modo=bot&nivel=difficulty
 */
function playWithBot(difficulty) {
    window.location.href = `tablero.html?modo=bot&nivel=${difficulty}`;
}

/**
 * Función para volver al índice principal
 * Redirige a la página de inicio
 */
function goToIndex() {
    window.location.href = 'index.html';
}

// Inicialización cuando la página carga
document.addEventListener('DOMContentLoaded', function() {
    console.log('Chess game mode selection loaded');
});
```

---

### 🏁 tablero.js
**Propósito**: Lógica principal del tablero de ajedrez y comunicación con el servidor

```javascript
// VARIABLES GLOBALES DEL ESTADO DEL JUEGO

/**
 * Variables de estado del juego
 */
let gameMode = 'amigo';                    // Modo: 'amigo' o 'bot'
let botDifficulty = 'principiante';       // Dificultad: 'principiante' o 'intermedio'
let currentPlayer = 'white';              // Jugador actual
let gameBoard = [];                       // Estado del tablero (matriz 8x8)
let selectedSquare = null;                // Casilla seleccionada {row, col}

// Configuración del servidor
const SERVER_URL = 'http://localhost:9090';

// FUNCIONES DE INICIALIZACIÓN

/**
 * Inicializa el juego basado en parámetros de URL
 * Lee los parámetros modo y nivel de la URL
 * Configura la interfaz según el modo seleccionado
 */
function initializeGame() {
    const urlParams = new URLSearchParams(window.location.search);
    gameMode = urlParams.get('modo') || 'amigo';
    botDifficulty = urlParams.get('nivel') || 'principiante';
    
    console.log(`Initializing game - Mode: ${gameMode}, Difficulty: ${botDifficulty}`);
    
    updateGameModeUI();    // Actualiza interfaz según modo
    setupBoard();          // Crea el tablero HTML
    getGameState();        // Obtiene estado inicial del servidor
}

/**
 * Actualiza la interfaz de usuario según el modo de juego
 * Cambia nombres de jugadores e iconos según modo amigo/bot
 */
function updateGameModeUI() {
    const player1Name = document.getElementById('player1-name');
    const player2Name = document.getElementById('player2-name');
    const player2Icon = document.getElementById('player2-icon');
    
    if (gameMode === 'bot') {
        if (player1Name) player1Name.textContent = 'Jugador';
        if (player2Name) player2Name.textContent = `Bot (${botDifficulty})`;
        if (player2Icon) player2Icon.textContent = '🤖';
    } else {
        if (player1Name) player1Name.textContent = 'Jugador 1';
        if (player2Name) player2Name.textContent = 'Jugador 2';
        if (player2Icon) player2Icon.textContent = '👤';
    }
}

// FUNCIONES DEL TABLERO

/**
 * Crea el tablero HTML de 8x8 casillas
 * Genera 64 elementos div con clases CSS apropiadas
 * Asigna eventos de clic a cada casilla
 */
function setupBoard() {
    console.log('Setting up chess board...');
    
    const boardElement = document.getElementById('chess-board');
    if (!boardElement) return;
    
    boardElement.innerHTML = '';  // Limpia tablero existente
    
    // Crea 64 casillas
    for (let row = 0; row < 8; row++) {
        for (let col = 0; col < 8; col++) {
            const square = document.createElement('div');
            square.className = 'square';
            square.dataset.row = row;
            square.dataset.col = col;
            
            // Alterna colores (claro/oscuro)
            if ((row + col) % 2 === 0) {
                square.classList.add('light');
            } else {
                square.classList.add('dark');
            }
            
            // Asigna evento de clic
            square.addEventListener('click', () => handleSquareClick(row, col));
            
            boardElement.appendChild(square);
        }
    }
}

// FUNCIONES DE COMUNICACIÓN CON EL SERVIDOR

/**
 * Obtiene el estado actual del juego desde el servidor
 * Realiza petición GET a /api/estado
 * Actualiza el tablero visual con la respuesta
 */
async function getGameState() {
    try {
        const response = await fetch(`${SERVER_URL}/api/estado`);
        const data = await response.json();
        
        if (data.tablero) {
            gameBoard = data.tablero;
            updateBoardDisplay();
            updateGameStatus(data.turno, data.movimientos);
        }
    } catch (error) {
        console.error('Error getting game state:', error);
        showConnectionError();
    }
}

/**
 * Envía un movimiento al servidor para validación
 * @param {number} fromRow - Fila origen (0-7)
 * @param {number} fromCol - Columna origen (0-7)  
 * @param {number} toRow - Fila destino (0-7)
 * @param {number} toCol - Columna destino (0-7)
 */
async function makeMove(fromRow, fromCol, toRow, toCol) {
    try {
        const moveData = {
            desde: { fila: fromRow, columna: fromCol },
            hacia: { fila: toRow, columna: toCol }
        };
        
        const response = await fetch(`${SERVER_URL}/api/mover`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(moveData)
        });
        
        const result = await response.json();
        
        if (result.exito) {
            console.log('Move successful:', result.mensaje);
            selectedSquare = null;
            await getGameState();  // Actualiza estado
            
            // Activa bot si es necesario
            if (gameMode === 'bot' && result.turno === 'negras') {
                setTimeout(() => makeBotMove(), 1000);
            }
        } else {
            console.log('Move failed:', result.mensaje);
            selectedSquare = null;
            updateBoardDisplay();
        }
        
    } catch (error) {
        console.error('Error making move:', error);
        showConnectionError();
    }
}

// FUNCIONES DE INTERFAZ VISUAL

/**
 * Actualiza la visualización del tablero con las piezas actuales
 * Coloca las piezas Unicode en sus posiciones
 * Resalta la casilla seleccionada
 */
function updateBoardDisplay() {
    const boardElement = document.getElementById('chess-board');
    if (!boardElement) return;
    
    const squares = boardElement.children;
    
    for (let row = 0; row < 8; row++) {
        for (let col = 0; col < 8; col++) {
            const squareIndex = row * 8 + col;
            const square = squares[squareIndex];
            const piece = gameBoard[row][col];
            
            square.textContent = piece || '';  // Muestra pieza o vacío
            
            // Resalta casilla seleccionada
            if (selectedSquare && selectedSquare.row === row && selectedSquare.col === col) {
                square.classList.add('selected');
            } else {
                square.classList.remove('selected');
            }
        }
    }
}

/**
 * Maneja los clics en las casillas del tablero
 * @param {number} row - Fila de la casilla clickeada
 * @param {number} col - Columna de la casilla clickeada
 * 
 * Lógica:
 * 1. Si no hay casilla seleccionada: selecciona si tiene pieza
 * 2. Si hay casilla seleccionada: 
 *    - Si es la misma: deselecciona
 *    - Si es diferente: intenta mover
 */
function handleSquareClick(row, col) {
    if (!selectedSquare) {
        // Seleccionar casilla con pieza
        if (gameBoard[row] && gameBoard[row][col] && gameBoard[row][col] !== '') {
            selectedSquare = { row, col };
            updateBoardDisplay();
        }
    } else {
        // Mover o deseleccionar
        if (selectedSquare.row === row && selectedSquare.col === col) {
            selectedSquare = null;  // Deselecciona
            updateBoardDisplay();
        } else {
            makeMove(selectedSquare.row, selectedSquare.col, row, col);  // Intenta mover
        }
    }
}

// FUNCIONES DE CONTROL DEL JUEGO

/**
 * Inicia un nuevo juego
 * Envía petición POST a /api/reiniciar al servidor
 * Oculta modal de fin de juego si está visible
 */
function newGame() {
    console.log('Starting new game...');
    
    fetch(`${SERVER_URL}/api/reiniciar`, { method: 'POST' })
    .then(response => response.json())
    .then(result => {
        if (result.exito) {
            console.log('Game restarted successfully');
            selectedSquare = null;
            getGameState();
        }
    })
    .catch(error => {
        console.error('Error restarting game:', error);
        showConnectionError();
    });
    
    const modal = document.getElementById('game-end-modal');
    if (modal) modal.style.display = 'none';
}

/**
 * Vuelve al menú de selección de modo
 */
function goBack() {
    window.location.href = 'modoJuego.html';
}

// FUNCIONES DE BOT

/**
 * Maneja movimientos del bot
 * Por ahora simula tiempo de "pensamiento"
 * El servidor maneja la lógica del bot
 */
function makeBotMove() {
    if (gameMode === 'bot') {
        console.log(`Bot making move with difficulty: ${botDifficulty}`);
        
        setTimeout(() => {
            console.log('Bot move completed');
            getGameState();  // Actualiza estado después del movimiento del bot
        }, 1500);
    }
}

// FUNCIONES AUXILIARES

/**
 * Actualiza el display del estado del juego
 * @param {string} turn - Turno actual ('blancas' o 'negras')
 * @param {number} moves - Número de movimientos realizados
 */
function updateGameStatus(turn, moves) {
    const statusElement = document.getElementById('game-status');
    if (statusElement) {
        const turnText = turn === 'blancas' ? 'Turno de las Blancas' : 'Turno de las Negras';
        statusElement.textContent = turnText;
    }
    console.log(`Turn: ${turn}, Moves: ${moves}`);
}

/**
 * Muestra error de conexión al servidor
 */
function showConnectionError() {
    const statusElement = document.getElementById('game-status');
    if (statusElement) {
        statusElement.textContent = 'Error de conexión con el servidor';
        statusElement.style.color = 'red';
    }
}

/**
 * Verifica conexión con el servidor al cargar la página
 */
function checkServerConnection() {
    fetch(`${SERVER_URL}/api/estado`)
        .then(response => {
            if (response.ok) {
                console.log('✅ Connected to chess server');
                const statusElement = document.getElementById('game-status');
                if (statusElement) statusElement.style.color = '#0b4413';
            }
        })
        .catch(error => {
            console.error('❌ Cannot connect to chess server:', error);
            showConnectionError();
        });
}

// FUNCIONES DE MODALES

/**
 * Muestra el modal de configuración
 */
function showSettings() {
    const modal = document.getElementById('settings-modal');
    if (modal) modal.style.display = 'flex';
}

/**
 * Cierra el modal de configuración
 */
function closeSettings() {
    const modal = document.getElementById('settings-modal');
    if (modal) modal.style.display = 'none';
}

/**
 * Ofrece tablas al oponente
 */
function offerDraw() {
    if (confirm('¿Estás seguro de que quieres ofrecer tablas?')) {
        console.log('Draw offered');
        // Implementar lógica de tablas aquí
    }
}

// INICIALIZACIÓN

/**
 * Inicializa la página cuando carga el DOM
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log('Chess board page loaded');
    checkServerConnection();  // Verifica conexión al servidor
    initializeGame();         // Inicializa el juego
});
```

---

## 📄 ARCHIVO JAVA

### ☕ ServidorAjedrez.java
**Propósito**: Servidor HTTP que maneja la lógica del juego de ajedrez

```java
import java.io.*;
import java.net.*;

/**
 * SERVIDOR DE AJEDREZ
 * 
 * Servidor HTTP simple que maneja:
 * 1. Estado del tablero de ajedrez
 * 2. Validación de movimientos según reglas oficiales
 * 3. APIs REST para comunicación con frontend
 * 4. Lógica completa del juego en el backend
 */
public class ServidorAjedrez {
    // CONFIGURACIÓN DEL SERVIDOR
    private static final int PUERTO = 9090;
    
    // ESTADO DEL JUEGO - Toda la lógica en Java
    
    /**
     * Representación del tablero usando caracteres Unicode
     * ♜♞♝♛♚♝♞♜ - Piezas negras (fila 0)
     * ♟♟♟♟♟♟♟♟ - Peones negros (fila 1)
     * Filas 2-5: Vacías
     * ♙♙♙♙♙♙♙♙ - Peones blancos (fila 6)
     * ♖♘♗♕♔♗♘♖ - Piezas blancas (fila 7)
     */
    private static String[][] tablero = {
        {"♜","♞","♝","♛","♚","♝","♞","♜"},  // Fila 0: Piezas negras
        {"♟","♟","♟","♟","♟","♟","♟","♟"},  // Fila 1: Peones negros
        {"","","","","","","",""},            // Filas 2-5: Vacías
        {"","","","","","","",""},
        {"","","","","","","",""},
        {"","","","","","","",""},
        {"♙","♙","♙","♙","♙","♙","♙","♙"},  // Fila 6: Peones blancos
        {"♖","♘","♗","♕","♔","♗","♘","♖"}   // Fila 7: Piezas blancas
    };
    
    private static String turnoActual = "blancas";      // Turno actual
    private static int movimientosRealizados = 0;      // Contador de movimientos
    private static boolean juegoTerminado = false;     // Estado del juego
    private static String ganador = "";                // Ganador del juego

    /**
     * MÉTODO PRINCIPAL
     * Inicia el servidor HTTP en el puerto especificado
     * Crea un hilo para cada cliente que se conecta
     */
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(PUERTO);
        System.out.println("🎯 Servidor de Ajedrez iniciado en puerto " + PUERTO);
        System.out.println("📱 Abre: http://localhost:" + PUERTO);
        System.out.println("⏹️  Presiona Ctrl+C para detener");
        
        // Loop infinito para aceptar conexiones
        while (true) {
            Socket cliente = servidor.accept();
            // Cada cliente en su propio hilo
            new Thread(() -> manejarCliente(cliente)).start();
        }
    }

    /**
     * MANEJO DE CLIENTES
     * Procesa las peticiones HTTP de cada cliente
     * @param cliente Socket del cliente conectado
     */
    private static void manejarCliente(Socket cliente) {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             PrintWriter salida = new PrintWriter(cliente.getOutputStream())) {
            
            // Lee la primera línea de la petición HTTP
            String linea = entrada.readLine();
            if (linea == null) return;
            
            // Parsea método y ruta
            String[] partes = linea.split(" ");
            String metodo = partes[0];  // GET, POST, etc.
            String ruta = partes[1];    // /api/estado, /api/mover, etc.
            
            System.out.println("📥 " + metodo + " " + ruta);
            
            // Headers CORS para permitir conexiones desde frontend
            String headers = "HTTP/1.1 200 OK\r\n" +
                           "Access-Control-Allow-Origin: *\r\n" +
                           "Access-Control-Allow-Methods: GET, POST, OPTIONS\r\n" +
                           "Access-Control-Allow-Headers: Content-Type\r\n";
            
            // ENRUTAMIENTO DE PETICIONES
            if (ruta.equals("/")) {
                // Página principal (no usada en nuestro caso)
                servirArchivo(salida, "demo.html", "text/html", headers);
            } else if (ruta.equals("/style.css")) {
                servirArchivo(salida, "style.css", "text/css", headers);
            } else if (ruta.equals("/script.js")) {
                servirArchivo(salida, "script.js", "application/javascript", headers);
            } else if (ruta.equals("/api/estado")) {
                // API: Obtener estado actual del juego
                String tableroJson = tableroAJson();
                String respuesta = String.format(
                    "{ \"tablero\": %s, \"turno\": \"%s\", \"movimientos\": %d, \"terminado\": %s, \"ganador\": \"%s\" }",
                    tableroJson, turnoActual, movimientosRealizados, juegoTerminado, ganador
                );
                enviarRespuesta(salida, respuesta, "application/json", headers);
            } else if (ruta.equals("/api/mover")) {
                // API: Procesar movimiento
                String cuerpo = leerCuerpoRequest(entrada);
                String respuesta = procesarMovimiento(cuerpo);
                enviarRespuesta(salida, respuesta, "application/json", headers);
            } else if (ruta.equals("/api/reiniciar")) {
                // API: Reiniciar juego
                reiniciarJuego();
                String respuesta = "{ \"exito\": true, \"mensaje\": \"Juego reiniciado\" }";
                enviarRespuesta(salida, respuesta, "application/json", headers);
            } else {
                // 404 - No encontrado
                salida.println("HTTP/1.1 404 Not Found\r\n\r\n<h1>404 - Página no encontrada</h1>");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
        }
    }

    /**
     * CONVERSIÓN DE TABLERO A JSON
     * Convierte la matriz de tablero a formato JSON
     * @return String con el tablero en formato JSON
     */
    private static String tableroAJson() {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < 8; i++) {
            json.append("[");
            for (int j = 0; j < 8; j++) {
                json.append("\"").append(tablero[i][j]).append("\"");
                if (j < 7) json.append(",");
            }
            json.append("]");
            if (i < 7) json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    /**
     * PROCESAMIENTO DE MOVIMIENTOS
     * Valida y ejecuta movimientos de ajedrez
     * @param json Datos del movimiento en formato JSON
     * @return Respuesta JSON con resultado
     */
    private static String procesarMovimiento(String json) {
        try {
            System.out.println("📥 Movimiento recibido: " + json);
            
            // Extrae coordenadas del JSON manualmente
            int desdeF = extraerValor(json, "\"desde\":{\"fila\":");
            int desdeC = extraerValor(json, "\"columna\":", json.indexOf("\"desde\""));
            int haciaF = extraerValor(json, "\"hacia\":{\"fila\":");
            int haciaC = extraerValor(json, "\"columna\":", json.indexOf("\"hacia\""));
            
            System.out.printf("🎯 Movimiento: (%d,%d) -> (%d,%d)%n", desdeF, desdeC, haciaF, haciaC);
            
            // Valida el movimiento
            if (esMovimientoValido(desdeF, desdeC, haciaF, haciaC)) {
                // Ejecuta el movimiento
                String pieza = tablero[desdeF][desdeC];
                tablero[haciaF][haciaC] = pieza;
                tablero[desdeF][desdeC] = "";
                
                // Cambia turno
                turnoActual = turnoActual.equals("blancas") ? "negras" : "blancas";
                movimientosRealizados++;
                
                System.out.println("✅ Movimiento realizado correctamente");
                return "{ \"exito\": true, \"mensaje\": \"Movimiento realizado\", \"turno\": \"" + turnoActual + "\" }";
            } else {
                System.out.println("❌ Movimiento inválido");
                return "{ \"exito\": false, \"mensaje\": \"Movimiento inválido\" }";
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error procesando movimiento: " + e.getMessage());
            return "{ \"exito\": false, \"mensaje\": \"Error en el servidor\" }";
        }
    }

    /**
     * VALIDACIÓN DE MOVIMIENTOS
     * Verifica si un movimiento es válido según las reglas del ajedrez
     * @param desdeF Fila origen
     * @param desdeC Columna origen
     * @param haciaF Fila destino
     * @param haciaC Columna destino
     * @return true si el movimiento es válido
     */
    private static boolean esMovimientoValido(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Validaciones básicas de límites
        if (desdeF < 0 || desdeF >= 8 || desdeC < 0 || desdeC >= 8 ||
            haciaF < 0 || haciaF >= 8 || haciaC < 0 || haciaC >= 8) {
            return false;
        }
        
        // Debe haber pieza en origen
        if (tablero[desdeF][desdeC].isEmpty()) {
            return false;
        }
        
        // No puede mover a la misma casilla
        if (desdeF == haciaF && desdeC == haciaC) {
            return false;
        }
        
        // Validación de turno
        String pieza = tablero[desdeF][desdeC];
        boolean esPiezaBlanca = "♙♖♘♗♕♔".contains(pieza);
        boolean esPiezaNegra = "♟♜♞♝♛♚".contains(pieza);
        
        if (turnoActual.equals("blancas") && !esPiezaBlanca) {
            return false;
        }
        if (turnoActual.equals("negras") && !esPiezaNegra) {
            return false;
        }
        
        // No puede capturar pieza propia
        String piezaDestino = tablero[haciaF][haciaC];
        if (!piezaDestino.isEmpty()) {
            boolean destinoBlanca = "♙♖♘♗♕♔".contains(piezaDestino);
            boolean destinoNegra = "♟♜♞♝♛♚".contains(piezaDestino);
            
            if ((esPiezaBlanca && destinoBlanca) || (esPiezaNegra && destinoNegra)) {
                return false;
            }
        }
        
        // Validación específica por tipo de pieza
        return validarMovimientoPorPieza(pieza, desdeF, desdeC, haciaF, haciaC);
    }

    /**
     * VALIDACIÓN POR TIPO DE PIEZA
     * Verifica movimientos específicos según el tipo de pieza
     * @param pieza Carácter Unicode de la pieza
     * @param desdeF, desdeC, haciaF, haciaC Coordenadas del movimiento
     * @return true si el movimiento es válido para esa pieza
     */
    private static boolean validarMovimientoPorPieza(String pieza, int desdeF, int desdeC, int haciaF, int haciaC) {
        switch (pieza) {
            case "♙": case "♟": // Peón
                return validarMovimientoPeon(pieza, desdeF, desdeC, haciaF, haciaC);
            case "♖": case "♜": // Torre
                return validarMovimientoTorre(desdeF, desdeC, haciaF, haciaC);
            case "♘": case "♞": // Caballo
                return validarMovimientoCaballo(desdeF, desdeC, haciaF, haciaC);
            case "♗": case "♝": // Alfil
                return validarMovimientoAlfil(desdeF, desdeC, haciaF, haciaC);
            case "♕": case "♛": // Reina
                return validarMovimientoReina(desdeF, desdeC, haciaF, haciaC);
            case "♔": case "♚": // Rey
                return validarMovimientoRey(desdeF, desdeC, haciaF, haciaC);
            default:
                return false;
        }
    }

    /**
     * VALIDACIÓN DE MOVIMIENTO DE PEÓN
     * Reglas específicas para peones
     */
    private static boolean validarMovimientoPeon(String pieza, int desdeF, int desdeC, int haciaF, int haciaC) {
        boolean esBlanco = pieza.equals("♙");
        int direccion = esBlanco ? -1 : 1; // Blancas suben (-1), negras bajan (+1)
        
        // Movimiento hacia adelante
        if (desdeC == haciaC) {
            // Un paso adelante
            if (haciaF == desdeF + direccion && tablero[haciaF][haciaC].isEmpty()) {
                return true;
            }
            // Primer movimiento: dos pasos
            if ((esBlanco && desdeF == 6) || (!esBlanco && desdeF == 1)) {
                if (haciaF == desdeF + 2 * direccion && tablero[haciaF][haciaC].isEmpty()) {
                    return true;
                }
            }
        }
        
        // Captura diagonal
        if (Math.abs(desdeC - haciaC) == 1 && haciaF == desdeF + direccion) {
            return !tablero[haciaF][haciaC].isEmpty();
        }
        
        return false;
    }

    /**
     * VALIDACIÓN DE MOVIMIENTO DE TORRE
     * Se mueve en líneas rectas (horizontal/vertical)
     */
    private static boolean validarMovimientoTorre(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Debe moverse en línea recta
        if (desdeF != haciaF && desdeC != haciaC) return false;
        
        // Verifica que no hay piezas en el camino
        return caminoLibre(desdeF, desdeC, haciaF, haciaC);
    }

    /**
     * VALIDACIÓN DE MOVIMIENTO DE CABALLO
     * Movimiento en forma de L
     */
    private static boolean validarMovimientoCaballo(int desdeF, int desdeC, int haciaF, int haciaC) {
        int deltaF = Math.abs(haciaF - desdeF);
        int deltaC = Math.abs(haciaC - desdeC);
        
        // Movimiento en L: (2,1) o (1,2)
        return (deltaF == 2 && deltaC == 1) || (deltaF == 1 && deltaC == 2);
    }

    /**
     * VALIDACIÓN DE MOVIMIENTO DE ALFIL
     * Se mueve en diagonales
     */
    private static boolean validarMovimientoAlfil(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Debe moverse en diagonal
        if (Math.abs(haciaF - desdeF) != Math.abs(haciaC - desdeC)) return false;
        
        // Verifica que no hay piezas en el camino
        return caminoLibre(desdeF, desdeC, haciaF, haciaC);
    }

    /**
     * VALIDACIÓN DE MOVIMIENTO DE REINA
     * Combina movimientos de torre y alfil
     */
    private static boolean validarMovimientoReina(int desdeF, int desdeC, int haciaF, int haciaC) {
        return validarMovimientoTorre(desdeF, desdeC, haciaF, haciaC) || 
               validarMovimientoAlfil(desdeF, desdeC, haciaF, haciaC);
    }

    /**
     * VALIDACIÓN DE MOVIMIENTO DE REY
     * Se mueve una casilla en cualquier dirección
     */
    private static boolean validarMovimientoRey(int desdeF, int desdeC, int haciaF, int haciaC) {
        return Math.abs(haciaF - desdeF) <= 1 && Math.abs(haciaC - desdeC) <= 1;
    }

    /**
     * VERIFICACIÓN DE CAMINO LIBRE
     * Verifica que no hay piezas entre origen y destino
     * @param desdeF, desdeC Coordenadas origen
     * @param haciaF, haciaC Coordenadas destino
     * @return true si el camino está libre
     */
    private static boolean caminoLibre(int desdeF, int desdeC, int haciaF, int haciaC) {
        int deltaF = Integer.signum(haciaF - desdeF);  // -1, 0, o 1
        int deltaC = Integer.signum(haciaC - desdeC);  // -1, 0, o 1
        
        int f = desdeF + deltaF;
        int c = desdeC + deltaC;
        
        // Recorre el camino hasta el destino
        while (f != haciaF || c != haciaC) {
            if (!tablero[f][c].isEmpty()) {
                return false;  // Hay una pieza bloqueando
            }
            f += deltaF;
            c += deltaC;
        }
        
        return true;  // Camino libre
    }

    /**
     * REINICIO DEL JUEGO
     * Restaura el estado inicial del tablero
     */
    private static void reiniciarJuego() {
        // Restaura posición inicial
        tablero = new String[][]{
            {"♜","♞","♝","♛","♚","♝","♞","♜"},
            {"♟","♟","♟","♟","♟","♟","♟","♟"},
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"","","","","","","",""},
            {"♙","♙","♙","♙","♙","♙","♙","♙"},
            {"♖","♘","♗","♕","♔","♗","♘","♖"}
        };
        
        turnoActual = "blancas";
        movimientosRealizados = 0;
        juegoTerminado = false;
        ganador = "";
        
        System.out.println("🔄 Juego reiniciado");
    }

    // MÉTODOS AUXILIARES

    /**
     * Sirve archivos estáticos (HTML, CSS, JS)
     */
    private static void servirArchivo(PrintWriter salida, String archivo, String tipo, String headers) {
        try (BufferedReader lector = new BufferedReader(new FileReader(archivo))) {
            salida.println(headers + "Content-Type: " + tipo + "\r\n");
            
            String linea;
            while ((linea = lector.readLine()) != null) {
                salida.println(linea);
            }
        } catch (IOException e) {
            salida.println("HTTP/1.1 404 Not Found\r\n\r\n<h1>Archivo no encontrado: " + archivo + "</h1>");
        }
    }

    /**
     * Envía respuesta HTTP
     */
    private static void enviarRespuesta(PrintWriter salida, String contenido, String tipo, String headers) {
        salida.println(headers + "Content-Type: " + tipo + "\r\n");
        salida.println(contenido);
    }

    /**
     * Lee el cuerpo de una petición POST
     */
    private static String leerCuerpoRequest(BufferedReader entrada) throws IOException {
        StringBuilder cuerpo = new StringBuilder();
        String linea;
        int longitudContenido = 0;
        
        // Lee headers para encontrar Content-Length
        while ((linea = entrada.readLine()) != null && !linea.isEmpty()) {
            if (linea.startsWith("Content-Length:")) {
                longitudContenido = Integer.parseInt(linea.substring(16).trim());
            }
        }
        
        // Lee el cuerpo
        for (int i = 0; i < longitudContenido; i++) {
            cuerpo.append((char) entrada.read());
        }
        
        return cuerpo.toString();
    }

    /**
     * Extrae valores numéricos del JSON manualmente
     */
    private static int extraerValor(String json, String buscar) {
        return extraerValor(json, buscar, 0);
    }

    private static int extraerValor(String json, String buscar, int desde) {
        int inicio = json.indexOf(buscar, desde) + buscar.length();
        int fin = inicio;
        while (fin < json.length() && Character.isDigit(json.charAt(fin))) {
            fin++;
        }
        return Integer.parseInt(json.substring(inicio, fin));
    }
}
```

---

## 🎯 RESUMEN DE LA ARQUITECTURA

### **Frontend (HTML/CSS/JavaScript)**
- **modoJuego.html**: Selección de modo de juego
- **tablero.html**: Interfaz principal del juego
- **main.js**: Navegación entre páginas
- **tablero.js**: Lógica del tablero y comunicación con servidor

### **Backend (Java)**
- **ServidorAjedrez.java**: Servidor HTTP con lógica completa del ajedrez

### **Comunicación**
- **APIs REST**: `/api/estado`, `/api/mover`, `/api/reiniciar`
- **Formato JSON**: Para intercambio de datos
- **Puerto 9090**: Servidor local

### **Flujo del Juego**
1. Usuario selecciona modo → Frontend navega con parámetros
2. Tablero se conecta al servidor → Obtiene estado inicial
3. Usuario hace clic → Frontend envía movimiento
4. Servidor valida → Responde con nuevo estado
5. Frontend actualiza → Ciclo continúa

Este sistema proporciona un juego de ajedrez completo con validación de reglas en el servidor y una interfaz moderna en el cliente.
