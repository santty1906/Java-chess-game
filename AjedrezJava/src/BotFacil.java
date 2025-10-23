import java.util.Random;

public class BotFacil {

    public static int[] obtenerMovimiento(String[][] piezas) {

        //PRIMERA PRIORIDAD: Si el rey del bot está en jaque, salir del jaque
        if (estaEnJaque(piezas, "negro")) {
            int[] escaparJaque = buscarEscapeDeJaque(piezas);
            if (escaparJaque != null) {
                return escaparJaque;
            }
        }

        //Introducir factor random: a veces el bot no ve bien y no busca capturas como debe, mismo noob
        if (Math.random() < 0.2) { // 20% de las veces ignora capturas
            // Saltar directamente a movimientos aleatorios
            int[] movimientoDistraccion = buscarMovimientoAleatorio(piezas);
            if (movimientoDistraccion != null) {
                return movimientoDistraccion;
            }
        }

        // 1. Solo buscar capturas sencillas sin revisar si puede ser castigado por ellas
        int[] captura = buscarCapturaAleatoria(piezas); // Selecciona una captura aleatoria
        if (captura != null) {
            return captura;
        }

        // 2. Mover peones hacia adelante sin algún motivo general
        int[] movimientoPeon = moverPeonAleatorio(piezas); // Cambio a versión aleatoria
        if (movimientoPeon != null) {
            return movimientoPeon;
        }

        // 3. Mover piezas de forma aleatoria sin estrategia
        int[] movimientoAleatorio = buscarMovimientoAleatorio(piezas);
        if (movimientoAleatorio != null) {
            return movimientoAleatorio;
        }

        // 4. Fallback: primer movimiento válido
        return buscarMovimientoBasico(piezas);
    }

    // Buscar capturas de forma aleatoria
    private static int[] buscarCapturaAleatoria(String[][] piezas) {
        int[][] capturasPosibles = new int[64][4]; 
        int contador = 0;

        //Recopilar todas las capturas posibles
        for (int filaOrigen = 0; filaOrigen < 8; filaOrigen++) {
            for (int colOrigen = 0; colOrigen < 8; colOrigen++) {
                String pieza = piezas[filaOrigen][colOrigen];
                if (pieza != null && pieza.contains("negro")) {
                    for (int filaDestino = 0; filaDestino < 8; filaDestino++) {
                        for (int colDestino = 0; colDestino < 8; colDestino++) {
                            String piezaDestino = piezas[filaDestino][colDestino];
                            //Capturar cualquier pieza blanca
                            if (piezaDestino != null && piezaDestino.contains("blanco")) {
                                if (ValidadorMovimiento.esMovimientoValido(piezas, pieza, filaOrigen, colOrigen, filaDestino, colDestino)) {
                                    capturasPosibles[contador][0] = filaOrigen;
                                    capturasPosibles[contador][1] = colOrigen;
                                    capturasPosibles[contador][2] = filaDestino;
                                    capturasPosibles[contador][3] = colDestino;
                                    contador++;
                                }
                            }
                        }
                    }
                }
            }
        }
        
        if (contador > 0) {
            int indiceAleatorio = (int)(Math.random() * contador);
            return new int[]{capturasPosibles[indiceAleatorio][0], capturasPosibles[indiceAleatorio][1],
                    capturasPosibles[indiceAleatorio][2], capturasPosibles[indiceAleatorio][3]};
        }

        return null;
    }

    // Mover peones de forma más aleatoria
    private static int[] moverPeonAleatorio(String[][] piezas) {
        int[][] movimientosPeones = new int[32][4]; 
        int contador = 0;

        for (int fila = 0; fila < 8; fila++) { 
            for (int col = 0; col < 8; col++) {
                String pieza = piezas[fila][col];
                if (pieza != null && pieza.contains("negro") && pieza.contains("peon")) {

                    // 1. Movimiento hacia adelante 
                    int nuevaFila = fila + 1;
                    if (nuevaFila < 8 && piezas[nuevaFila][col] == null) {
                        if (ValidadorMovimiento.esMovimientoValido(piezas, pieza, fila, col, nuevaFila, col)) {
                            movimientosPeones[contador][0] = fila;
                            movimientosPeones[contador][1] = col;
                            movimientosPeones[contador][2] = nuevaFila;
                            movimientosPeones[contador][3] = col;
                            contador++;
                        }
                    }

                    // 2. Movimiento de 2 casillas
                    if (fila == 1) { 
                        int nuevaFila2 = fila + 2;
                        if (nuevaFila2 < 8 && piezas[nuevaFila2][col] == null) {
                            if (ValidadorMovimiento.esMovimientoValido(piezas, pieza, fila, col, nuevaFila2, col)) {
                                movimientosPeones[contador][0] = fila;
                                movimientosPeones[contador][1] = col;
                                movimientosPeones[contador][2] = nuevaFila2;
                                movimientosPeones[contador][3] = col;
                                contador++;
                            }
                        }
                    }

                    // 3. Capturas en diagonal 
                    int[] columnasCaptura = {col - 1, col + 1}; 
                    for (int colCaptura : columnasCaptura) {
                        if (colCaptura >= 0 && colCaptura < 8 && nuevaFila < 8) {
                            String piezaObjetivo = piezas[nuevaFila][colCaptura];
                            //Si hay una pieza blanca para capturar
                            if (piezaObjetivo != null && piezaObjetivo.contains("blanco")) {
                                if (ValidadorMovimiento.esMovimientoValido(piezas, pieza, fila, col, nuevaFila, colCaptura)) {
                                    movimientosPeones[contador][0] = fila;
                                    movimientosPeones[contador][1] = col;
                                    movimientosPeones[contador][2] = nuevaFila;
                                    movimientosPeones[contador][3] = colCaptura;
                                    contador++;
                                }
                            }
                        }
                    }
                }
            }
        }

        //Elegir un movimiento de peón aleatorio
        if (contador > 0) {
            int indiceAleatorio = (int)(Math.random() * contador);
            return new int[]{movimientosPeones[indiceAleatorio][0], movimientosPeones[indiceAleatorio][1],
                    movimientosPeones[indiceAleatorio][2], movimientosPeones[indiceAleatorio][3]};
        }

        return null;
    }

    private static int[] buscarMovimientoAleatorio(String[][] piezas) {
        int[][] movimientosPosibles = new int[200][4];
        int contador = 0;

        //Orden aleatorio de piezas
        String[] tiposPiezas = {"peon", "caballo", "alfil", "torre", "reina"};
        for (int i = 0; i < tiposPiezas.length; i++) {
            int j = (int)(Math.random() * tiposPiezas.length);
            String temp = tiposPiezas[i];
            tiposPiezas[i] = tiposPiezas[j];
            tiposPiezas[j] = temp;
        }

        for (String tipoPieza : tiposPiezas) {
            for (int filaOrigen = 0; filaOrigen < 8; filaOrigen++) {
                for (int colOrigen = 0; colOrigen < 8; colOrigen++) {
                    String pieza = piezas[filaOrigen][colOrigen];
                    if (pieza != null && pieza.contains("negro") && pieza.contains(tipoPieza)) {

                        //Rango aleatorio de movimiento (a veces muy corto, a veces más largo)
                        int rangoMax = (Math.random() < 0.7) ? 2 : 4; // 70% movimientos cortos, 30% más largos

                        //Buscar movimientos en rango aleatorio
                        for (int deltaFila = -rangoMax; deltaFila <= rangoMax; deltaFila++) {
                            for (int deltaCol = -rangoMax; deltaCol <= rangoMax; deltaCol++) {
                                if (deltaFila == 0 && deltaCol == 0) continue;

                                int filaDestino = filaOrigen + deltaFila;
                                int colDestino = colOrigen + deltaCol;

                                if (filaDestino >= 0 && filaDestino < 8 &&
                                        colDestino >= 0 && colDestino < 8) {

                                    if (ValidadorMovimiento.esMovimientoValido(piezas, pieza, filaOrigen, colOrigen, filaDestino, colDestino)) {
                                        if (contador < 200) { //Evitar desbordamiento
                                            movimientosPosibles[contador][0] = filaOrigen;
                                            movimientosPosibles[contador][1] = colOrigen;
                                            movimientosPosibles[contador][2] = filaDestino;
                                            movimientosPosibles[contador][3] = colDestino;
                                            contador++;
                                        }

                                        //A veces elegir el primer movimiento encontrado (impulsivo)
                                        if (Math.random() < 0.3) { // 30% de ser impulsivo
                                            return new int[]{filaOrigen, colOrigen, filaDestino, colDestino};
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (contador > 0 && Math.random() < 0.4) { // 40% de no seguir buscando otras opciones
                int indiceAleatorio = (int)(Math.random() * contador);
                return new int[]{movimientosPosibles[indiceAleatorio][0], movimientosPosibles[indiceAleatorio][1],
                        movimientosPosibles[indiceAleatorio][2], movimientosPosibles[indiceAleatorio][3]};
            }
        }

        //Si llegó hasta aquí, elegir aleatoriamente entre todos los movimientos recopilados
        if (contador > 0) {
            int indiceAleatorio = (int)(Math.random() * contador);
            return new int[]{movimientosPosibles[indiceAleatorio][0], movimientosPosibles[indiceAleatorio][1],
                    movimientosPosibles[indiceAleatorio][2], movimientosPosibles[indiceAleatorio][3]};
        }

        return null;
    }

    //Método básico como último recurso
    private static int[] buscarMovimientoBasico(String[][] piezas) {
        for (int filaOrigen = 0; filaOrigen < 8; filaOrigen++) {
            for (int colOrigen = 0; colOrigen < 8; colOrigen++) {
                String pieza = piezas[filaOrigen][colOrigen];
                if (pieza != null && pieza.contains("negro")) {
                    for (int filaDestino = 0; filaDestino < 8; filaDestino++) {
                        for (int colDestino = 0; colDestino < 8; colDestino++) {
                            if (ValidadorMovimiento.esMovimientoValido(piezas, pieza, filaOrigen, colOrigen, filaDestino, colDestino)) {
                                return new int[]{filaOrigen, colOrigen, filaDestino, colDestino};
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    //Función para verificar si un rey está en jaque
    private static boolean estaEnJaque(String[][] piezas, String colorRey) {
        //Encontrar la posición del rey
        int filaRey = -1, colRey = -1;
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                String pieza = piezas[f][c];
                if (pieza != null && pieza.contains("rey") && pieza.contains(colorRey)) {
                    filaRey = f;
                    colRey = c;
                    break;
                }
            }
        }

        if (filaRey == -1) return false; //Rey no encontrado

        //Verificar si alguna pieza contraria puede atacar al rey
        String colorEnemigo = colorRey.equals("blanco") ? "negro" : "blanco";
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                String pieza = piezas[f][c];
                if (pieza != null && pieza.contains(colorEnemigo)) {
                    if (ValidadorMovimiento.esMovimientoValido(piezas, pieza, f, c, filaRey, colRey)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Función para buscar escape cuando el rey está en jaque
    private static int[] buscarEscapeDeJaque(String[][] piezas) {
        //Primero busca mover al rey a una casilla segura

        //Primer intento con mover el rey
        for (int fila = 0; fila < 8; fila++) {
            for (int col = 0; col < 8; col++) {
                String pieza = piezas[fila][col];
                if (pieza != null && pieza.contains("rey") && pieza.contains("negro")) {
                    //Probar todas las casillas adyacentes al rey
                    for (int deltaF = -1; deltaF <= 1; deltaF++) {
                        for (int deltaC = -1; deltaC <= 1; deltaC++) {
                            if (deltaF == 0 && deltaC == 0) continue; //No moverse a la misma casilla

                            int nuevaFila = fila + deltaF;
                            int nuevaCol = col + deltaC;

                            if (nuevaFila >= 0 && nuevaFila < 8 && nuevaCol >= 0 && nuevaCol < 8) {
                                if (ValidadorMovimiento.esMovimientoValido(piezas, pieza, fila, col, nuevaFila, nuevaCol)) {
                                    //Simular el movimiento para ver si escapa del jaque
                                    String[][] tableroTemporal = copiarTablero(piezas);
                                    tableroTemporal[nuevaFila][nuevaCol] = pieza;
                                    tableroTemporal[fila][col] = null;

                                    if (!estaEnJaque(tableroTemporal, "negro")) {
                                        return new int[]{fila, col, nuevaFila, nuevaCol};
                                    }
                                }
                            }
                        }
                    }
                    break; // Solo hay un rey
                }
            }
        }

        // Si mover el rey no funciona, intentar bloquear o capturar la pieza que ataca
        for (int filaOrigen = 0; filaOrigen < 8; filaOrigen++) {
            for (int colOrigen = 0; colOrigen < 8; colOrigen++) {
                String pieza = piezas[filaOrigen][colOrigen];
                if (pieza != null && pieza.contains("negro") && !pieza.contains("rey")) {
                    for (int filaDestino = 0; filaDestino < 8; filaDestino++) {
                        for (int colDestino = 0; colDestino < 8; colDestino++) {
                            if (ValidadorMovimiento.esMovimientoValido(piezas, pieza, filaOrigen, colOrigen, filaDestino, colDestino)) {
                                // Simular el movimiento
                                String[][] tableroTemporal = copiarTablero(piezas);
                                tableroTemporal[filaDestino][colDestino] = pieza;
                                tableroTemporal[filaOrigen][colOrigen] = null;

                                if (!estaEnJaque(tableroTemporal, "negro")) {
                                    return new int[]{filaOrigen, colOrigen, filaDestino, colDestino};
                                }
                            }
                        }
                    }
                }
            }
        }

        return null; //jaque mate
    }

    // Función para copiar el tablero
    private static String[][] copiarTablero(String[][] original) {
        String[][] copia = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                copia[i][j] = original[i][j];
            }
        }
        return copia;
}
}
