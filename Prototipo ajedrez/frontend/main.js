// Game mode selection functionality

function playWithFriend() {
    // Redirect to friend game page or show friend game setup
    alert('Redirigiendo a jugar con un amigo...');
    // window.location.href = 'jugarAmigo.html';
}

function showBotOptions() {
    // Hide main game buttons
    const gameButtons = document.querySelector('.game-buttons');
    const botDifficulty = document.getElementById('bot-difficulty');
    const title = document.querySelector('.game-mode-container h1');
    
    gameButtons.style.display = 'none';
    botDifficulty.style.display = 'block';
    title.textContent = 'Modo Bot';
}

function hideBotOptions() {
    // Show main game buttons again
    const gameButtons = document.querySelector('.game-buttons');
    const botDifficulty = document.getElementById('bot-difficulty');
    const title = document.querySelector('.game-mode-container h1');
    
    gameButtons.style.display = 'flex';
    botDifficulty.style.display = 'none';
    title.textContent = 'Selecciona tu modo de juego';
}

function playWithBot(difficulty) {
    // Handle bot game with selected difficulty
    alert(`Iniciando juego contra bot - Nivel: ${difficulty}`);
    // Here you would redirect to the bot game page with the difficulty parameter
    // window.location.href = `jugarBot.html?nivel=${difficulty}`;
}

// Function to go back to index page
function goToIndex() {
    window.location.href = 'index.html';
}

// Add smooth scrolling and additional interactions
document.addEventListener('DOMContentLoaded', function() {
    // Add any additional initialization code here
    console.log('Chess game mode selection loaded');
});
