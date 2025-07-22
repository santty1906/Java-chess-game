import java.io.*;
import java.net.*;

public class ServidorAjedrez {
    private static final int PUERTO = 9090;
    
    // 🎮 Estado del juego - TODA LA LÓGICA EN JAVA
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
    
    private static String turnoActual = "blancas";
    private static int movimientosRealizados = 0;
    private static boolean juegoTerminado = false;
    private static String ganador = "";
    
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
            
            if (ruta.equals("/")) {
                // Servir página principal
                servirArchivo(salida, "demo.html", "text/html", headers);
            } else if (ruta.equals("/style.css")) {
                servirArchivo(salida, "style.css", "text/css", headers);
            } else if (ruta.equals("/script.js")) {
                servirArchivo(salida, "script.js", "application/javascript", headers);
            } else if (ruta.equals("/api/estado")) {
                // API: Estado del juego - LÓGICA EN JAVA
                String tableroJson = tableroAJson();
                String respuesta = String.format(
                    "{ \"tablero\": %s, \"turno\": \"%s\", \"movimientos\": %d, \"terminado\": %s, \"ganador\": \"%s\" }",
                    tableroJson, turnoActual, movimientosRealizados, juegoTerminado, ganador
                );
                enviarRespuesta(salida, respuesta, "application/json", headers);
            } else if (ruta.equals("/api/mover")) {
                // API: Realizar movimiento - LÓGICA EN JAVA
                String cuerpo = leerCuerpoRequest(entrada);
                String respuesta = procesarMovimiento(cuerpo);
                enviarRespuesta(salida, respuesta, "application/json", headers);
            } else if (ruta.equals("/api/reiniciar")) {
                // API: Reiniciar juego - LÓGICA EN JAVA
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
    
    private static void enviarRespuesta(PrintWriter salida, String contenido, String tipo, String headers) {
        salida.println(headers + "Content-Type: " + tipo + "\r\n");
        salida.println(contenido);
    }
    
    // 🎯 MÉTODOS DE LÓGICA DEL JUEGO - TODO EN JAVA
    
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
        
        // ✅ Por ahora, movimiento básico válido
        // Aquí puedes agregar lógica específica de cada pieza
        return validarMovimientoPorPieza(pieza, desdeF, desdeC, haciaF, haciaC);
    }
    
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
    
    private static boolean validarMovimientoTorre(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Torre se mueve en línea recta
        if (desdeF != haciaF && desdeC != haciaC) return false;
        
        // Verificar que no hay piezas en el camino
        return caminoLibre(desdeF, desdeC, haciaF, haciaC);
    }
    
    private static boolean validarMovimientoCaballo(int desdeF, int desdeC, int haciaF, int haciaC) {
        int deltaF = Math.abs(haciaF - desdeF);
        int deltaC = Math.abs(haciaC - desdeC);
        
        // Movimiento en L: 2+1 o 1+2
        return (deltaF == 2 && deltaC == 1) || (deltaF == 1 && deltaC == 2);
    }
    
    private static boolean validarMovimientoAlfil(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Alfil se mueve en diagonal
        if (Math.abs(haciaF - desdeF) != Math.abs(haciaC - desdeC)) return false;
        
        // Verificar que no hay piezas en el camino
        return caminoLibre(desdeF, desdeC, haciaF, haciaC);
    }
    
    private static boolean validarMovimientoReina(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Reina combina torre + alfil
        return validarMovimientoTorre(desdeF, desdeC, haciaF, haciaC) || 
               validarMovimientoAlfil(desdeF, desdeC, haciaF, haciaC);
    }
    
    private static boolean validarMovimientoRey(int desdeF, int desdeC, int haciaF, int haciaC) {
        // Rey se mueve una casilla en cualquier dirección
        return Math.abs(haciaF - desdeF) <= 1 && Math.abs(haciaC - desdeC) <= 1;
    }
    
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
        
        System.out.println("🔄 Juego reiniciado");
    }
}
