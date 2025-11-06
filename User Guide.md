# User Guide - Java Chess Game

## Quick Start

### First Launch

1. **Run the game**: For detailed instructions on how to run the game, see the README file.
2. **Select mode**: Choose between "Player vs Player" or "Player vs Bot".
3. **Start the match**: The board opens ready to play.

### Basic Controls

* **Left click**: Select a piece / Move to a square
* **Click selected piece again**: Cancel selection
* **Blue border**: Highlights the currently selected piece

## Game Modes

### Player vs Player (1v1)

* **Turns alternate**: White always starts
* **Full control**: Both players control their pieces
* **Ideal for**: Local matches with friends

### Player vs Bot (PvE)

* **You play**: Always white pieces
* **Bot plays**: Automatically with black pieces
* **Level**: Beginner (good for learning)
* **Response time**: ~0.5 seconds per move

## How to Move Pieces

### Basic Move

1. **Click** the piece you want to move

   * It will highlight with a blue border
2. **Click** the destination square

   * Valid move → piece moves
   * Invalid move → error message shown

### Captures

* **Automatic**: Enemy pieces in the destination are captured automatically
* **Visual**: Captured pieces disappear from the board

### Cancel Move

* **Click same piece**: Cancel current selection
* **Click another piece**: Switch selection

## Special Rules

### Castling

Castling is a special move involving the king and a rook.

#### How to Castle

1. **Select your king** (must be on starting square)
2. **Move king two squares** toward the rook you want to castle with

   * **Right side**: Short castle (king's rook)
   * **Left side**: Long castle (queen's rook)

#### Conditions

* King and rook haven't moved
* No pieces between king and rook
* King is not in check
* King does not pass through attacked squares

#### Example:

```
Before short castle:
...R...K  (R=Rook, K=King)

After short castle:
.....RK.  (King and rook swap positions)
```

### Pawn Promotion

#### When it happens

* When a pawn reaches the opposite end of the board (row 1 for white, row 8 for black)

#### How to choose

1. **Move pawn** to last row
2. **Menu pops up** with 4 options:

   * **Queen** (recommended)
   * **Rook**
   * **Bishop**
   * **Knight**
3. **Select piece** and click "OK"
4. **Pawn transforms** into the chosen piece

#### Strategy

* **Queen**: Best choice 95% of the time
* **Knight**: Useful for specific checkmates
* **Rook/Bishop**: Occasionally useful in endgames

## Check Situations

### What is Check?

* Your king is under threat
* Message shows: "Check to player [color]!"
* You must respond in your next move

### How to Get Out of Check

1. **Move king** to a safe square
2. **Block** the attack with another piece
3. **Capture** the threatening piece

### Checkmate

* Cannot escape check in any way
* Result: You lose
* Game ends automatically

## Playing Against the Bot

### Bot Features

* **Level**: Beginner
* **Behavior**: Makes realistic mistakes
* **Speed**: Moves ~0.5 seconds
* **Style**: Varied, slightly unpredictable

### Bot Strengths

* Escapes when king is in check
* Takes obvious captures
* Advances pawns
* Gradually develops pieces

### Bot Weaknesses

* Sometimes ignores threats
* Does not always make the best move
* Can be "distracted" (~20% of the time)
* No long-term planning

### Tips to Win

1. Develop your pieces quickly
2. Protect your king with early castling
3. Look for captures the bot may miss
4. Control the center
5. Be patient – the bot will make mistakes

## Visual Interface

### Board Colors

* **Light squares**: Beige (#F0D9B5)
* **Dark squares**: Brown (#B58863)
* **Selected piece**: Thick blue border

### Pieces

* Clear graphics for each piece
* 60x60 pixels, perfectly scaled
* Easy identification by traditional colors and shapes

## Troubleshooting

### "I can't move my piece"

**Causes:**

* Not your turn
* Invalid move
* King would be in check
* Piece blocking the path

**Solution:** Check piece rules and turn order

### "Castling doesn't work"

**Causes:**

* King or rook moved
* Pieces in the way
* King in check
* King would pass through attacked square

**Solution:** Verify all castling conditions

## Basic Chess Rules

### Objective

* **Win**: Checkmate the enemy king
* **Draw**: Stalemate (not implemented yet)

### Piece Movement

#### Pawn

* Forward: 1 square (2 on first move)
* Capture: 1 square diagonally
* Special: Promotion at the end

#### Rook

* Move: Horizontal/vertical any distance
* Capture: Same as move
* Special: Castling

#### Knight

* Move: L-shape (2+1 or 1+2)
* Unique: Can jump over pieces
* Capture: On destination square

#### Bishop

* Move: Diagonal any distance
* Capture: Same as move

#### Queen

* Move: Combines rook + bishop
* Most versatile
* Capture: Same as move

#### King

* Move: 1 square in any direction
* Special: Castling
* Important: Cannot move into check

## Tips & Strategies

### For Beginners

1. Learn piece values:

   * Pawn = 1 point
   * Knight/Bishop = 3 points
   * Rook = 5 points
   * Queen = 9 points
   * King = invaluable
2. Opening principles:

   * Develop minor pieces first
   * Control the center
   * Castle early to protect king
3. Basic tactics:

   * Look for free captures
   * Protect your pieces
   * Attack undefended pieces

### Against the Bot

1. Be patient – bot makes mistakes
2. Develop quickly – move pieces out
3. Look for tactical opportunities – double attacks, pins, etc.
4. Endgame – learn basic checkmates

Enjoy your matches!

