/*
 * SERVIDORAJEDREZ.JAVA - Servidor HTTP para el juego de ajedrez
 * Propósito: Manejar la lógica del juego y API REST para el frontend
 * Puerto: 9090
 * APIs: /api/estado, /api/mover, /api/reiniciar
 */

import java.io.*;
import java.net.*;

public class ServidorAjedrez {
    private static final int PUERTO = 9090;
    
    // Estado del juego - Tablero inicial con piezas Unicode
    private static String[][] tablero = {
        {"♜","♞","♝","♛","♚","♝","♞","♜"},
        {"♟","♟","♟","♟","♟","♟","♟","♟"},
        {"","","","","","","",""},
        {"","","","","","","",""},
        {"","","","","","","",""},
        {"","","","","","","",""},
        {"♙","♙","♙","♙","♙","♙","♙","♙"},
        {"♖","♘","♗","♕","♔","♗","♘","♖"}
    };
    
    // Variables de estado del juego
    private static String turnoActual = "blancas";
    private static int movimientosRealizados = 0;
    private static boolean juegoTerminado = false;
    private static String ganador = "";
    private static String modoJuego = "amigo"; // "amigo" o "bot"
    private static String nivelBot = "principiante"; // "principiante" o "intermedio"
    
    /**
     * main() - Inicia el servidor HTTP en puerto 9090
     * Acepta conexiones y crea un hilo por cada cliente
     */
    public static void main(String[] args) throws IOException {
        ServerSocket servidor = new ServerSocket(PUERTO);
        System.out.println("🎯 Servidor de Ajedrez iniciado en puerto " + PUERTO);
        System.out.println("📱 Abre: http://localhost:" + PUERTO);
        System.out.println("⏹️  Presiona Ctrl+C para detener");
        
        while (true) {
            Socket cliente = servidor.accept();
            new Thread(() -> manejarCliente(cliente)).start();
        }
    }
    
    /**
     * manejarCliente() - Procesa peticiones HTTP del cliente
     * Parsea método y ruta, aplica CORS, y dirige a APIs correspondientes
     */
    private static void manejarCliente(Socket cliente) {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
             PrintWriter salida = new PrintWriter(cliente.getOutputStream())) {
            
            String linea = entrada.readLine();
            if (linea == null) return;
            
            String[] partes = linea.split(" ");
            String metodo = partes[0];
            String ruta = partes[1];
            
            System.out.println("📥 " + metodo + " " + ruta);
            
            // Headers CORS para permitir conexiones desde cualquier origen
            String headers = "HTTP/1.1 200 OK\r\n" +
                           "Access-Control-Allow-Origin: *\r\n" +
                           "Access-Control-Allow-Methods: GET, POST, OPTIONS\r\n" +
                           "Access-Control-Allow-Headers: Content-Type\r\n";
            
            // Manejar peticiones OPTIONS (preflight)
            if (metodo.equals("OPTIONS")) {
                salida.println(headers + "\r\n");
                return;
            }
            
            if (ruta.equals("/")) {
                // Servir página principal
                servirArchivo(salida, "frontend/index.html", "text/html", headers);
            } else if (ruta.equals("/style.css")) {
                servirArchivo(salida, "style.css", "text/css", headers);
            } else if (ruta.equals("/script.js")) {
                servirArchivo(salida, "script.js", "application/javascript", headers);
            } else if (ruta.equals("/api/estado")) {
                // API: Estado del juego - LÓGICA EN JAVA
                String tableroJson = tableroAJson();
                String respuesta = String.format(
                    "{ \"tablero\": %s, \"turno\": \"%s\", \"movimientos\": %d, \"terminado\": %s, \"ganador\": \"%s\", \"modo\": \"%s\", \"nivel\": \"%s\" }",
                    tableroJson, turnoActual, movimientosRealizados, juegoTerminado, ganador, modoJuego, nivelBot
                );
                enviarRespuesta(salida, respuesta, "application/json", headers);
            } else if (ruta.equals("/api/mover")) {
                /**
                 * API /api/mover - Procesa movimiento de pieza
                 * Valida el movimiento y actualiza estado del juego
                 */
                String cuerpo = leerCuerpoRequest(entrada);
                String respuesta = procesarMovimiento(cuerpo);
                enviarRespuesta(salida, respuesta, "application/json", headers);
            } else if (ruta.equals("/api/reiniciar")) {
                /**
                 * API /api/reiniciar - Reinicia el juego
                 * Restaura tablero y variables a estado inicial
                 */
                reiniciarJuego();
                String respuesta = "{ \"exito\": true, \"mensaje\": \"Juego reiniciado\" }";
                enviarRespuesta(salida, respuesta, "application/json", headers);
            } else if (ruta.startsWith("/api/configurar")) {
                /**
                 * API /api/configurar - Configura modo de juego y dificultad del bot
                 * Parámetros: modo=amigo|bot&nivel=principiante|intermedio
                 */
                String respuesta = configurarJuego(ruta);
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
     * servirArchivo() - Sirve archivos estáticos del sistema
     * Lee archivo del disco y lo envía al cliente
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
     * enviarRespuesta() - Envía respuesta HTTP al cliente
     * Incluye headers, content-type y contenido
     */
    private static void enviarRespuesta(PrintWriter salida, String contenido, String tipo, String headers) {
        salida.println(headers + "Content-Type: " + tipo + "\r\n");
        salida.println(contenido);
    }
    
    /**
     * configurarJuego() - Configura el modo de juego y dificultad del bot
     * Parsea parámetros de URL para establecer configuración
     */
    private static String configurarJuego(String ruta) {
        try {
            // Extraer parámetros de la URL
            if (ruta.contains("?")) {
                String parametros = ruta.substring(ruta.indexOf("?") + 1);
                String[] params = parametros.split("&");
                
                for (String param : params) {
                    String[] kv = param.split("=");
                    if (kv.length == 2) {
                        if (kv[0].equals("modo")) {
                            modoJuego = kv[1];
                        } else if (kv[0].equals("nivel")) {
                            nivelBot = kv[1];
                        }
                    }
                }
            }
            
            System.out.println("🎮 Juego configurado - Modo: " + modoJuego + ", Nivel bot: " + nivelBot);
            return String.format("{ \"exito\": true, \"mensaje\": \"Configuración actualizada\", \"modo\": \"%s\", \"nivel\": \"%s\" }", 
                                modoJuego, nivelBot);
            
        } catch (Exception e) {
            System.err.println("❌ Error configurando juego: " + e.getMessage());
            return "{ \"exito\": false, \"mensaje\": \"Error en configuración\" }";
        }
    }
    
    // 🎯 MÉTODOS DE LÓGICA DEL JUEGO - TODO EN JAVA
    
    /**
     * tableroAJson() - Convierte matriz del tablero a formato JSON
     * Genera JSON array 8x8 con las piezas actuales
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
     * leerCuerpoRequest() - Lee el cuerpo de una petición HTTP POST
     * Parsea Content-Length y lee los datos del cuerpo
     */
    private static String leerCuerpoRequest(BufferedReader entrada) throws IOException {
        StringBuilder cuerpo = new StringBuilder();
        String linea;
        int longitudContenido = 0;
        
        // Leer headers para encontrar Content-Length
        while ((linea = entrada.readLine()) != null && !linea.isEmpty()) {
            if (linea.startsWith("Content-Length:")) {
                longitudContenido = Integer.parseInt(linea.substring(16).trim());
            }
        }
        
        // Leer el cuerpo del request
        for (int i = 0; i < longitudContenido; i++) {
            cuerpo.append((char) entrada.read());
        }
        
        return cuerpo.toString();
    }
    
    /**
     * procesarMovimiento() - Valida y ejecuta un movimiento de ajedrez
     * Parsea JSON, valida movimiento y actualiza estado si es válido
     */
    private static String procesarMovimiento(String json) {
        try {
            // Parsear JSON manualmente (sin librerías externas)
            System.out.println("📥 Movimiento recibido: " + json);
            
            // Extraer coordenadas del JSON
            int desdeF = extraerValor(json, "\"desde\":{\"fila\":");
            int desdeC = extraerValor(json, "\"columna\":", json.indexOf("\"desde\""));
            int haciaF = extraerValor(json, "\"hacia\":{\"fila\":");
            int haciaC = extraerValor(json, "\"columna\":", json.indexOf("\"hacia\""));
            
            System.out.printf("🎯 Movimiento: (%d,%d) -> (%d,%d)%n", desdeF, desdeC, haciaF, haciaC);
            
            // Validar movimiento
            if (esMovimientoValido(desdeF, desdeC, haciaF, haciaC)) {
                // Realizar movimiento
                String pieza = tablero[desdeF][desdeC];
                tablero[haciaF][haciaC] = pieza;
                tablero[desdeF][desdeC] = "";
                
                // Cambiar turno
                turnoActual = turnoActual.equals("blancas") ? "negras" : "blancas";
                movimientosRealizados++;
                
                // Si es modo bot y ahora es turno de negras, hacer movimiento del bot
                if (modoJuego.equals("bot") && turnoActual.equals("negras")) {
                    hacerMovimientoBot();
                    turnoActual = "blancas"; // El bot siempre juega con negras
                    movimientosRealizados++;
                }
                
                // Verificar si hay jaque mate o empate
                verificarFinDelJuego();
                
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
     * extraerValor() - Extrae valor numérico del JSON
     * Busca un campo específico y retorna su valor entero
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
    
    /**
     * esMovimientoValido() - Valida si un movimiento es legal según reglas de ajedrez
     * Verifica límites, existencia de pieza, y reglas específicas por tipo de pieza
     */
    private static boolean esMovimientoValido(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Validaciones básicas
        if (desdeF < 0 || desdeF >= 8 || desdeC < 0 || desdeC >= 8 ||
            haciaF < 0 || haciaF >= 8 || haciaC < 0 || haciaC >= 8) {
            return false;
        }
        
        // No hay pieza en la casilla origen
        if (tablero[desdeF][desdeC].isEmpty()) {
            return false;
        }
        
        // No puede mover a la misma casilla
        if (desdeF == haciaF && desdeC == haciaC) {
            return false;
        }
        
        // Validar turno (piezas blancas vs negras)
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
        
        // Validación específica según tipo de pieza
        return validarMovimientoPorPieza(pieza, desdeF, desdeC, haciaF, haciaC);
    }
    
    /**
     * validarMovimientoPorPieza() - Valida movimiento según tipo específico de pieza
     * Dirige a función específica según el carácter Unicode de la pieza
     */
    private static boolean validarMovimientoPorPieza(String pieza, int desdeF, int desdeC, int haciaF, int haciaC) {
        // Lógica básica de movimiento por pieza
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
     * validarMovimientoPeon() - Valida movimiento de peón
     * Permite avanzar 1 o 2 casillas (primera vez) y capturar en diagonal
     */
    private static boolean validarMovimientoPeon(String pieza, int desdeF, int desdeC, int haciaF, int haciaC) {
        boolean esBlanco = pieza.equals("♙");
        int direccion = esBlanco ? -1 : 1; // Blancas suben, negras bajan
        
        // Movimiento hacia adelante
        if (desdeC == haciaC) {
            if (haciaF == desdeF + direccion && tablero[haciaF][haciaC].isEmpty()) {
                return true; // Un paso adelante
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
     * validarMovimientoTorre() - Valida movimiento de torre
     * Torre se mueve en línea recta y camino debe estar libre
     */
    private static boolean validarMovimientoTorre(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Torre se mueve en línea recta
        if (desdeF != haciaF && desdeC != haciaC) return false;
        
        // Verificar que no hay piezas en el camino
        return caminoLibre(desdeF, desdeC, haciaF, haciaC);
    }
    
    /**
     * validarMovimientoCaballo() - Valida movimiento de caballo
     * Caballo se mueve en forma de L (2+1 casillas)
     */
    private static boolean validarMovimientoCaballo(int desdeF, int desdeC, int haciaF, int haciaC) {
        int deltaF = Math.abs(haciaF - desdeF);
        int deltaC = Math.abs(haciaC - desdeC);
        
        // Movimiento en L: 2+1 o 1+2
        return (deltaF == 2 && deltaC == 1) || (deltaF == 1 && deltaC == 2);
    }
    
    /**
     * validarMovimientoAlfil() - Valida movimiento de alfil
     * Alfil se mueve en diagonal y camino debe estar libre
     */
    private static boolean validarMovimientoAlfil(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Alfil se mueve en diagonal
        if (Math.abs(haciaF - desdeF) != Math.abs(haciaC - desdeC)) return false;
        
        // Verificar que no hay piezas en el camino
        return caminoLibre(desdeF, desdeC, haciaF, haciaC);
    }
    
    /**
     * validarMovimientoReina() - Valida movimiento de reina
     * Reina combina movimientos de torre y alfil
     */
    private static boolean validarMovimientoReina(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Reina combina torre + alfil
        return validarMovimientoTorre(desdeF, desdeC, haciaF, haciaC) || 
               validarMovimientoAlfil(desdeF, desdeC, haciaF, haciaC);
    }
    
    /**
     * validarMovimientoRey() - Valida movimiento de rey
     * Rey se mueve una casilla en cualquier dirección
     */
    private static boolean validarMovimientoRey(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Rey se mueve una casilla en cualquier dirección
        return Math.abs(haciaF - desdeF) <= 1 && Math.abs(haciaC - desdeC) <= 1;
    }
    
    /**
     * caminoLibre() - Verifica que no hay piezas en el camino del movimiento
     * Usado por torre, alfil y reina para validar trayectoria
     */
    private static boolean caminoLibre(int desdeF, int desdeC, int haciaF, int haciaC) {
        int deltaF = Integer.signum(haciaF - desdeF);
        int deltaC = Integer.signum(haciaC - desdeC);
        
        int f = desdeF + deltaF;
        int c = desdeC + deltaC;
        
        while (f != haciaF || c != haciaC) {
            if (!tablero[f][c].isEmpty()) {
                return false;
            }
            f += deltaF;
            c += deltaC;
        }
        
        return true;
    }
    
    /**
     * reiniciarJuego() - Restaura tablero y variables a estado inicial
     * Reinicia posición de piezas y contador de movimientos (mantiene configuración de modo)
     */
    private static void reiniciarJuego() {
        // Restaurar estado inicial
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
        // Mantener modoJuego y nivelBot sin cambios
        
        System.out.println("🔄 Juego reiniciado - Modo: " + modoJuego + ", Nivel: " + nivelBot);
    }
    
    /**
     * hacerMovimientoBot() - Realiza un movimiento automático del bot
     * Busca todos los movimientos válidos y elige uno según el nivel de dificultad
     */
    private static void hacerMovimientoBot() {
        System.out.println("🤖 Bot pensando...");
        
        // Obtener todas las piezas negras y sus movimientos posibles
        java.util.List<int[]> movimientosPosibles = new java.util.ArrayList<>();
        
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                String pieza = tablero[f][c];
                if (!pieza.isEmpty() && "♟♜♞♝♛♚".contains(pieza)) {
                    // Es una pieza negra, buscar movimientos válidos
                    for (int destF = 0; destF < 8; destF++) {
                        for (int destC = 0; destC < 8; destC++) {
                            if (esMovimientoValido(f, c, destF, destC)) {
                                movimientosPosibles.add(new int[]{f, c, destF, destC});
                            }
                        }
                    }
                }
            }
        }
        
        if (!movimientosPosibles.isEmpty()) {
            int[] movimiento;
            
            if (nivelBot.equals("principiante")) {
                // Nivel principiante: movimiento aleatorio
                movimiento = movimientosPosibles.get((int)(Math.random() * movimientosPosibles.size()));
            } else {
                // Nivel intermedio: priorizar capturas
                movimiento = elegirMejorMovimiento(movimientosPosibles);
            }
            
            // Realizar el movimiento
            String pieza = tablero[movimiento[0]][movimiento[1]];
            tablero[movimiento[2]][movimiento[3]] = pieza;
            tablero[movimiento[0]][movimiento[1]] = "";
            
            System.out.printf("🤖 Bot movió: (%d,%d) -> (%d,%d)%n", 
                movimiento[0], movimiento[1], movimiento[2], movimiento[3]);
        }
    }
    
    /**
     * elegirMejorMovimiento() - Elige el mejor movimiento para el bot intermedio
     * Prioriza capturas y movimientos estratégicos
     */
    private static int[] elegirMejorMovimiento(java.util.List<int[]> movimientos) {
        int[] mejorMovimiento = null;
        int mejorPuntaje = -1000;
        
        for (int[] mov : movimientos) {
            int puntaje = 0;
            
            // Priorizar capturas
            String piezaCapturada = tablero[mov[2]][mov[3]];
            if (!piezaCapturada.isEmpty()) {
                puntaje += valorPieza(piezaCapturada);
            }
            
            // Priorizar movimientos hacia el centro
            int distanciaCentro = Math.abs(mov[2] - 3) + Math.abs(mov[3] - 3);
            puntaje += (6 - distanciaCentro);
            
            if (puntaje > mejorPuntaje) {
                mejorPuntaje = puntaje;
                mejorMovimiento = mov;
            }
        }
        
        return mejorMovimiento != null ? mejorMovimiento : movimientos.get(0);
    }
    
    /**
     * valorPieza() - Retorna el valor de una pieza para la evaluación del bot
     */
    private static int valorPieza(String pieza) {
        switch (pieza) {
            case "♙": case "♟": return 1;   // Peón
            case "♘": case "♞": return 3;   // Caballo
            case "♗": case "♝": return 3;   // Alfil
            case "♖": case "♜": return 5;   // Torre
            case "♕": case "♛": return 9;   // Reina
            case "♔": case "♚": return 100; // Rey
            default: return 0;
        }
    }
    
    /**
     * verificarFinDelJuego() - Verifica si el juego ha terminado (jaque mate o empate)
     */
    private static void verificarFinDelJuego() {
        // Verificar si el jugador actual tiene movimientos válidos
        boolean tieneMovimientos = false;
        
        String piezasBuscadas = turnoActual.equals("blancas") ? "♙♖♘♗♕♔" : "♟♜♞♝♛♚";
        
        for (int f = 0; f < 8 && !tieneMovimientos; f++) {
            for (int c = 0; c < 8 && !tieneMovimientos; c++) {
                String pieza = tablero[f][c];
                if (!pieza.isEmpty() && piezasBuscadas.contains(pieza)) {
                    for (int destF = 0; destF < 8 && !tieneMovimientos; destF++) {
                        for (int destC = 0; destC < 8 && !tieneMovimientos; destC++) {
                            if (esMovimientoValido(f, c, destF, destC)) {
                                tieneMovimientos = true;
                            }
                        }
                    }
                }
            }
        }
        
        if (!tieneMovimientos) {
            juegoTerminado = true;
            boolean enJaque = estaEnJaque(turnoActual);
            
            if (enJaque) {
                ganador = turnoActual.equals("blancas") ? "negras" : "blancas";
                System.out.println("🏆 Jaque mate! Ganan las " + ganador);
            } else {
                ganador = "empate";
                System.out.println("🤝 Empate por ahogado");
            }
        }
    }
    
    /**
     * estaEnJaque() - Verifica si el rey del color especificado está en jaque
     */
    private static boolean estaEnJaque(String color) {
        // Encontrar la posición del rey
        String reyBuscado = color.equals("blancas") ? "♔" : "♚";
        int reyF = -1, reyC = -1;
        
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                if (tablero[f][c].equals(reyBuscado)) {
                    reyF = f;
                    reyC = c;
                    break;
                }
            }
        }
        
        if (reyF == -1) return false; // Rey no encontrado
        
        // Verificar si alguna pieza enemiga puede capturar el rey
        String piezasEnemigas = color.equals("blancas") ? "♟♜♞♝♛♚" : "♙♖♘♗♕♔";
        
        for (int f = 0; f < 8; f++) {
            for (int c = 0; c < 8; c++) {
                String pieza = tablero[f][c];
                if (!pieza.isEmpty() && piezasEnemigas.contains(pieza)) {
                    // Temporalmente cambiar el turno para validar el movimiento
                    String turnoOriginal = turnoActual;
                    turnoActual = color.equals("blancas") ? "negras" : "blancas";
                    
                    boolean puedeCapturar = esMovimientoValido(f, c, reyF, reyC);
                    
                    turnoActual = turnoOriginal; // Restaurar turno
                    
                    if (puedeCapturar) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
}
