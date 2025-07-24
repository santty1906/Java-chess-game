import java.io.*;
import java.net.*;

public class TestServidor {
    public static void main(String[] args) throws IOException {
        System.out.println("🚀 Iniciando servidor de prueba en puerto 9090...");
        
        ServerSocket servidor = new ServerSocket(9090);
        System.out.println("✅ Servidor funcionando en http://localhost:9090");
        System.out.println("🌐 Abre el navegador y ve a http://localhost:9090/api/estado");
        
        while (true) {
            Socket cliente = servidor.accept();
            new Thread(() -> {
                try (BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                     PrintWriter salida = new PrintWriter(cliente.getOutputStream())) {
                    
                    String linea = entrada.readLine();
                    if (linea == null) return;
                    
                    System.out.println("📥 Petición: " + linea);
                    
                    // Headers CORS
                    String headers = "HTTP/1.1 200 OK\r\n" +
                                   "Access-Control-Allow-Origin: *\r\n" +
                                   "Access-Control-Allow-Methods: GET, POST, OPTIONS\r\n" +
                                   "Access-Control-Allow-Headers: Content-Type\r\n" +
                                   "Content-Type: application/json\r\n\r\n";
                    
                    String respuesta = "{ \"tablero\": [[\"♜\",\"♞\",\"♝\",\"♛\",\"♚\",\"♝\",\"♞\",\"♜\"],[\"♟\",\"♟\",\"♟\",\"♟\",\"♟\",\"♟\",\"♟\",\"♟\"],[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"],[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"],[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"],[\"\",\"\",\"\",\"\",\"\",\"\",\"\",\"\"],[\"\u2659\",\"\u2659\",\"\u2659\",\"\u2659\",\"\u2659\",\"\u2659\",\"\u2659\",\"\u2659\"],[\"\u2656\",\"\u2658\",\"\u2657\",\"\u2655\",\"\u2654\",\"\u2657\",\"\u2658\",\"\u2656\"]], \"turno\": \"blancas\", \"movimientos\": 0, \"terminado\": false, \"ganador\": \"\" }";
                    
                    salida.print(headers);
                    salida.print(respuesta);
                    salida.flush();
                    
                    System.out.println("📤 Respuesta enviada");
                    
                } catch (Exception e) {
                    System.err.println("❌ Error: " + e.getMessage());
                }
            }).start();
        }
    }
}
