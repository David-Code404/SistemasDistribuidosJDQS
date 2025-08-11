import java.util.Scanner;

/*David-Code*/

public class JuegoConsola {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Raya juego = new Raya();
        boolean juegoTerminado = false;

        while (!juegoTerminado) {
            juego.mostrarTablero();
            System.out.println("Turno del jugador " + juego.getJugadorActual());
            System.out.print("Introduce fila (0-2): ");
            int fila = sc.nextInt();
            System.out.print("Introduce columna (0-2): ");
            int columna = sc.nextInt();

            if (juego.realizarMovimiento(fila, columna)) {
                if (juego.hayGanador()) {
                    juego.mostrarTablero();
                    System.out.println("¡El jugador " + juego.getJugadorActual() + " ha ganado!");
                    juegoTerminado = true;
                } else if (juego.tableroLleno()) {
                    juego.mostrarTablero();
                    System.out.println("¡Empate!");
                    juegoTerminado = true;
                } else {
                    juego.cambiarTurno();
                }
            } else {
                System.out.println("Movimiento inválido. Intenta de nuevo.");
            }
        }
        sc.close();
    }
}
