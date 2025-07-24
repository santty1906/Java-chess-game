// MAIN.JS - Navegación y selección de modo de juego
// Funciones para manejar la navegación entre páginas y configurar modos de juego

/**
 * playWithFriend() - Inicia juego entre dos jugadores
 * Redirige al tablero con parámetro modo=amigo
 */
function playWithFriend() {
    // Redirect to the game board for friend vs friend
    window.location.href = 'tablero.html?modo=amigo';
}

/**
 * showBotOptions() - Muestra selector de dificultad del bot
 * Oculta botones principales y muestra opciones de dificultad
 */
function showBotOptions() {
    // Hide main game buttons
    const gameButtons = document.querySelector('.game-buttons');
    const botDifficulty = document.getElementById('bot-difficulty');
    const title = document.querySelector('.game-mode-container h1');
    
    gameButtons.style.display = 'none';
    botDifficulty.style.display = 'block';
    title.textContent = 'Modo Bot';
}

/**
 * hideBotOptions() - Oculta selector de dificultad del bot
 * Vuelve a mostrar botones principales de selección de modo
 */
function hideBotOptions() {
    // Show main game buttons again
    const gameButtons = document.querySelector('.game-buttons');
    const botDifficulty = document.getElementById('bot-difficulty');
    const title = document.querySelector('.game-mode-container h1');
    
    gameButtons.style.display = 'flex';
    botDifficulty.style.display = 'none';
    title.textContent = 'Selecciona tu modo de juego';
}

/**
 * FUNCIÓN: playWithBot(difficulty)
 * 
 * Propósito: Iniciar juego contra el bot con dificultad específica
 * 
 * Parámetros:
 * @param {string} difficulty - Nivel de dificultad ('principiante' o 'intermedio')
/**
 * playWithBot(difficulty) - Inicia juego contra bot con dificultad específica
 * Redirige al tablero con parámetros modo=bot y nivel=difficulty
 */
function playWithBot(difficulty) {
    // Redirect to the game board with bot difficulty parameter
    window.location.href = `tablero.html?modo=bot&nivel=${difficulty}`;
}

/**
 * goToIndex() - Vuelve a la página principal
 * Redirige a index.html
 */
function goToIndex() {
    window.location.href = 'index.html';
}

/**
 * Inicialización - Configura la página cuando se carga
 * Registra que la página se cargó correctamente
 */
document.addEventListener('DOMContentLoaded', function() {
    // Add any additional initialization code here
    console.log('Chess game mode selection loaded');
});
