public class ValidadorMovimiento {

    public static boolean esMovimientoValido(String[][] piezas, String pieza,
                                             int filaInicio, int colInicio, int filaFin, int colFin) {

        if (pieza == null) {
            return false;
        }

        String tipo = obtenerTipoPieza(pieza);
        String color = obtenerColor(pieza);

        int df = filaFin - filaInicio;
        int dc = colFin - colInicio;

        String destino = piezas[filaFin][colFin];
        boolean destinoEsAliado = false;
        if (destino != null) {
            String colorDestino = obtenerColor(destino);
            if (colorDestino.equals(color)) {
                destinoEsAliado = true; // No puede capturar piezas propias
            }
        }
        if (destinoEsAliado) {
            return false;
        }

        switch (tipo) {
            case "peon":
                return validarPeon(piezas, color, df, dc, filaInicio, filaFin, colFin);
            case "torre":
                if (df == 0 || dc == 0) {
                    return caminoLibre(piezas, filaInicio, colInicio, filaFin, colFin);
                }
                break;
            case "alfil":
                if (Math.abs(df) == Math.abs(dc)) {
                    return caminoLibre(piezas, filaInicio, colInicio, filaFin, colFin);
                }
                break;
            case "reina":
                if (df == 0 || dc == 0 || Math.abs(df) == Math.abs(dc)) {
                    return caminoLibre(piezas, filaInicio, colInicio, filaFin, colFin);
                }
                break;
            case "caballo":
                // Movimiento en L: 2 y 1 o 1 y 2
                if ((Math.abs(df) == 2 && Math.abs(dc) == 1) || (Math.abs(df) == 1 && Math.abs(dc) == 2)) {
                    return true; // Puede saltar piezas, no revisa camino
                }
                break;
            case "rey":
                // Una casilla en cualquier dirección
                if (Math.abs(df) <= 1 && Math.abs(dc) <= 1) {
                    return true;
                }
                // Permitir movimiento de enroque (2 casillas horizontalmente)
                if (df == 0 && Math.abs(dc) == 2) {
                    // La validación específica del enroque se hace en TableroAjedrez
                    return true;
                }
                break;
            default:
                return false;
        }

        return false;
    }

    // Verifica que el camino esté libre para piezas que recorren varias casillas
    private static boolean caminoLibre(String[][] piezas, int filaInicio, int colInicio, int filaFin, int colFin) {
        int df = Integer.compare(filaFin, filaInicio); // Dirección fila: -1, 0, 1
        int dc = Integer.compare(colFin, colInicio);   // Dirección columna: -1, 0, 1

        int fila = filaInicio + df;
        int col = colInicio + dc;

        while (fila != filaFin || col != colFin) {
            if (piezas[fila][col] != null) {
                return false; // Hay pieza bloqueando el camino
            }
            fila += df;
            col += dc;
        }
        return true;
    }

    private static String obtenerTipoPieza(String nombre) {
        nombre = nombre.toLowerCase();
        if (nombre.contains("peon")) return "peon";
        if (nombre.contains("torre")) return "torre";
        if (nombre.contains("alfil")) return "alfil";
        if (nombre.contains("reina")) return "reina";
        if (nombre.contains("rey")) return "rey";
        if (nombre.contains("caballo")) return "caballo";
        return "";
    }

    private static String obtenerColor(String nombre) {
        if (nombre.toLowerCase().contains("blanco")) {
            return "blanco";
        } else {
            return "negro";
        }
    }

    // Validación específica para peón, considerando movimiento normal y captura
    private static boolean validarPeon(String[][] piezas, String color, int df, int dc, int filaInicio, int filaFin, int colFin) {
        String destino = piezas[filaFin][colFin];

        if (color.equals("blanco")) {
            // Movimiento hacia arriba (fila disminuye)
            if (dc == 0 && df == -1 && destino == null) return true; // Mover 1 casilla
            if (dc == 0 && df == -2 && filaInicio == 6 && destino == null && piezas[5][colFin] == null) return true; // Mover 2 casillas inicial
            if (Math.abs(dc) == 1 && df == -1 && destino != null && obtenerColor(destino).equals("negro")) return true; // Captura diagonal
        } else {
            // Movimiento hacia abajo (fila aumenta)
            if (dc == 0 && df == 1 && destino == null) return true;
            if (dc == 0 && df == 2 && filaInicio == 1 && destino == null && piezas[2][colFin] == null) return true;
            if (Math.abs(dc) == 1 && df == 1 && destino != null && obtenerColor(destino).equals("blanco")) return true;
        }

        return false;
    }
}
