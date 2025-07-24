// TABLERO.JS - Lógica principal del tablero de ajedrez
// Maneja la comunicación con el servidor y la interfaz del juego

// Variables globales del estado del juego
let gameMode = 'amigo'; // 'amigo' or 'bot'
let botDifficulty = 'principiante'; // 'principiante' or 'intermedio'
let currentPlayer = 'white';
let gameBoard = [];
let selectedSquare = null;

// Configuración del servidor
const SERVER_URL = 'http://localhost:9090';

/**
 * initializeGame() - Inicializa el juego basado en parámetros URL
 * Lee modo y dificultad de URL, configura interfaz y obtiene estado inicial
 */
function initializeGame() {
    // Get URL parameters
    const urlParams = new URLSearchParams(window.location.search);
    gameMode = urlParams.get('modo') || 'amigo';
    botDifficulty = urlParams.get('nivel') || 'principiante';
    
    console.log(`Initializing game - Mode: ${gameMode}, Difficulty: ${botDifficulty}`);
    
    // Update UI based on game mode
    updateGameModeUI();
    
    // Initialize the chess board
    setupBoard();
    
    // Configure and get initial state
    setTimeout(() => {
        configureGameMode();
        setTimeout(() => {
            getGameState();
        }, 100);
    }, 100);
}

/**
 * updateGameModeUI() - Actualiza interfaz según modo de juego
 * Cambia nombres de jugadores e iconos según sea modo amigo o bot
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

/**
 * setupBoard() - Crea estructura HTML del tablero 8x8
 * Genera 64 casillas con eventos de click y colores alternados
 */
function setupBoard() {
    console.log('Setting up chess board...');
    
    const boardElement = document.getElementById('chess-board');
    if (!boardElement) return;
    
    // Clear existing board
    boardElement.innerHTML = '';
    
    // Create 64 squares
    for (let row = 0; row < 8; row++) {
        for (let col = 0; col < 8; col++) {
            const square = document.createElement('div');
            square.className = 'square';
            square.dataset.row = row;
            square.dataset.col = col;
            
            // Alternate colors (light/dark)
            if ((row + col) % 2 === 0) {
                square.classList.add('light');
            } else {
                square.classList.add('dark');
            }
            
            // Add click event
            square.addEventListener('click', () => handleSquareClick(row, col));
            
            boardElement.appendChild(square);
        }
    }
}

/**
 * getGameState() - Obtiene estado actual del juego desde servidor
 * Actualiza tablero y turno con datos del servidor via GET /api/estado
 */
async function getGameState() {
    try {
        console.log('🔄 Obteniendo estado del juego...');
        const response = await fetch(`${SERVER_URL}/api/estado`);
        console.log('📡 Respuesta del servidor:', response);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        console.log('📦 Datos recibidos:', data);
        
        if (data.tablero) {
            gameBoard = data.tablero;
            console.log('🎯 Tablero actualizado:', gameBoard);
            updateBoardDisplay();
            updateGameStatus(data.turno, data.movimientos);
        } else {
            console.error('❌ No se recibió tablero del servidor');
            // Usar tablero inicial por defecto
            initializeDefaultBoard();
        }
    } catch (error) {
        console.error('❌ Error getting game state:', error);
        // Usar tablero inicial por defecto si falla la conexión
        initializeDefaultBoard();
        showConnectionError();
    }
}

/**
 * initializeDefaultBoard() - Inicializa tablero por defecto si falla servidor
 */
function initializeDefaultBoard() {
    console.log('🔧 Inicializando tablero por defecto...');
    gameBoard = [
        ["♜","♞","♝","♛","♚","♝","♞","♜"],
        ["♟","♟","♟","♟","♟","♟","♟","♟"],
        ["","","","","","","",""],
        ["","","","","","","",""],
        ["","","","","","","",""],
        ["","","","","","","",""],
        ["♙","♙","♙","♙","♙","♙","♙","♙"],
        ["♖","♘","♗","♕","♔","♗","♘","♖"]
    ];
    updateBoardDisplay();
    updateGameStatus("blancas", 0);
}

/**
 * updateBoardDisplay() - Actualiza visualización del tablero con piezas
 * Coloca piezas Unicode en casillas y aplica estilo de selección
 */
function updateBoardDisplay() {
    console.log('🎨 Actualizando display del tablero...');
    const boardElement = document.getElementById('chess-board');
    if (!boardElement) {
        console.error('❌ No se encontró elemento chess-board');
        return;
    }
    
    const squares = boardElement.children;
    console.log(`📋 Tablero tiene ${squares.length} casillas`);
    console.log('🎯 Estado del gameBoard:', gameBoard);
    
    for (let row = 0; row < 8; row++) {
        for (let col = 0; col < 8; col++) {
            const squareIndex = row * 8 + col;
            const square = squares[squareIndex];
            
            if (!square) {
                console.error(`❌ No se encontró casilla en índice ${squareIndex}`);
                continue;
            }
            
            const piece = gameBoard[row] ? gameBoard[row][col] : '';
            console.log(`Casilla [${row}][${col}]: "${piece}"`);
            
            // Clear existing piece
            square.textContent = piece || '';
            
            // Update square appearance
            if (selectedSquare && selectedSquare.row === row && selectedSquare.col === col) {
                square.classList.add('selected');
            } else {
                square.classList.remove('selected');
            }
        }
    }
    console.log('✅ Display del tablero actualizado');
}

/**
 * handleSquareClick(row, col) - Maneja clicks en casillas del tablero
 * Selecciona piezas o ejecuta movimientos según el estado actual
 */
function handleSquareClick(row, col) {
    if (!selectedSquare) {
        // Select a square with a piece
        if (gameBoard[row] && gameBoard[row][col] && gameBoard[row][col] !== '') {
            selectedSquare = { row, col };
            updateBoardDisplay();
        }
    } else {
        // Try to move to this square
        if (selectedSquare.row === row && selectedSquare.col === col) {
            // Deselect
            selectedSquare = null;
            updateBoardDisplay();
        } else {
            // Attempt move
            makeMove(selectedSquare.row, selectedSquare.col, row, col);
        }
    }
}

/**
 * makeMove(fromRow, fromCol, toRow, toCol) - Envía movimiento al servidor
 * Valida el movimiento con el servidor via POST /api/mover
 */
async function makeMove(fromRow, fromCol, toRow, toCol) {
    try {
        console.log(`🎯 Enviando movimiento: (${fromRow},${fromCol}) -> (${toRow},${toCol})`);
        
        const moveData = {
            desde: { fila: fromRow, columna: fromCol },
            hacia: { fila: toRow, columna: toCol }
        };
        
        const response = await fetch(`${SERVER_URL}/api/mover`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(moveData)
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = await response.json();
        console.log('📨 Respuesta del servidor:', result);
        
        if (result.exito) {
            console.log('✅ Movimiento exitoso:', result.mensaje);
            selectedSquare = null;
            
            // Update game state
            await getGameState();
            
        } else {
            console.log('❌ Movimiento fallido:', result.mensaje);
            selectedSquare = null;
            updateBoardDisplay();
        }
        
    } catch (error) {
        console.error('❌ Error making move:', error);
        // En caso de error, mostrar al menos el movimiento local
        if (gameBoard[fromRow] && gameBoard[fromRow][fromCol]) {
            gameBoard[toRow][toCol] = gameBoard[fromRow][fromCol];
            gameBoard[fromRow][fromCol] = '';
            selectedSquare = null;
            updateBoardDisplay();
        }
        showConnectionError();
    }
}

/**
 * updateGameStatus(turn, moves) - Actualiza display del estado del juego
 * Muestra el turno actual en la interfaz
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
 * showConnectionError() - Muestra error de conexión al servidor
 * Cambia texto del estado a mensaje de error en rojo
 */
function showConnectionError() {
    const statusElement = document.getElementById('game-status');
    if (statusElement) {
        statusElement.textContent = 'Error de conexión con el servidor';
        statusElement.style.color = 'red';
    }
}

/**
 * newGame() - Inicia nuevo juego reiniciando servidor
 * Envía petición POST /api/reiniciar al servidor
 */
function newGame() {
    console.log('Starting new game...');
    
    // Call server to restart game
    fetch(`${SERVER_URL}/api/reiniciar`, {
        method: 'POST'
    })
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
    
    // Hide end game modal if visible
    const modal = document.getElementById('game-end-modal');
    if (modal) {
        modal.style.display = 'none';
    }
}

/**
 * goBack() - Vuelve al menú de selección de modo de juego
 * Redirige a modoJuego.html
 */
function goBack() {
    window.location.href = 'modoJuego.html';
}

/**
 * Inicialización - Configura el juego cuando carga la página
 * Verifica conexión con servidor e inicializa el juego
 */
document.addEventListener('DOMContentLoaded', function() {
    console.log('Chess board page loaded');
    checkServerConnection();
    initializeGame();
});

/**
 * makeBotMove() - Simula movimiento del bot
 * Actualmente manejado por la lógica del servidor
 */
function makeBotMove() {
    if (gameMode === 'bot') {
        console.log(`Bot making move with difficulty: ${botDifficulty}`);
        
        // For now, we'll use the server's game logic
        // In the future, you could implement bot AI here
        // or create a separate bot endpoint on the server
        
        // Simulate bot thinking time
        setTimeout(() => {
            console.log('Bot move completed (handled by server logic)');
            // The server will handle the game state
            getGameState();
        }, 1500);
    }
}

/**
 * makePlayerMove(move) - Función legacy para movimientos
 * Ahora manejada por handleSquareClick
 */
function makePlayerMove(move) {
    console.log(`Player move: ${move}`);
    // This function is now handled by the click events and makeMove function
}

/**
 * showSettings() - Muestra modal de configuración
 */
function showSettings() {
    const modal = document.getElementById('settings-modal');
    if (modal) {
        modal.style.display = 'flex';
    }
}

/**
 * closeSettings() - Cierra modal de configuración
 */
function closeSettings() {
    const modal = document.getElementById('settings-modal');
    if (modal) {
        modal.style.display = 'none';
    }
}

/**
 * offerDraw() - Ofrece tablas al oponente
 */
function offerDraw() {
    if (confirm('¿Estás seguro de que quieres ofrecer tablas?')) {
        console.log('Draw offered');
        // You can implement draw logic here
    }
}

/**
 * checkServerConnection() - Verifica conexión con servidor al cargar
 * Muestra estado de conexión en consola
 */
function checkServerConnection() {
    fetch(`${SERVER_URL}/api/estado`)
        .then(response => {
            if (response.ok) {
                console.log('✅ Connected to chess server');
                const statusElement = document.getElementById('game-status');
                if (statusElement) {
                    statusElement.style.color = '#0b4413';
                }
            }
        })
        .catch(error => {
            console.error('❌ Cannot connect to chess server:', error);
            showConnectionError();
        });
}

/**
 * configureGameMode() - Configura el modo de juego en el servidor
 * Envía configuración al servidor antes de iniciar el juego
 */
async function configureGameMode() {
    try {
        const url = `${SERVER_URL}/api/configurar?modo=${gameMode}&nivel=${botDifficulty}`;
        const response = await fetch(url);
        const result = await response.json();
        
        if (result.exito) {
            console.log('🎮 Modo de juego configurado:', result.mensaje);
        } else {
            console.error('❌ Error configurando modo:', result.mensaje);
        }
    } catch (error) {
        console.error('❌ Error enviando configuración:', error);
    }
}
