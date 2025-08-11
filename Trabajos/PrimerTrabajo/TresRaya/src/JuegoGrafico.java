import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*David-Code*/

public class JuegoGrafico extends JFrame {
    private JButton[][] botones = new JButton[3][3];
    private Raya juego;

    public JuegoGrafico() {
        juego = new Raya();
        setTitle("Tres en Raya");
        setSize(300, 300);
        setLayout(new GridLayout(3, 3));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j] = new JButton("");
                botones[i][j].setFont(new Font("Arial", Font.BOLD, 40));
                final int fila = i;
                final int columna = j;

                botones[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (juego.realizarMovimiento(fila, columna)) {
                            botones[fila][columna].setText(String.valueOf(juego.getJugadorActual()));
                            if (juego.hayGanador()) {
                                JOptionPane.showMessageDialog(null, "¡El jugador " + juego.getJugadorActual() + " ha ganado!");
                                reiniciarJuego();
                            } else if (juego.tableroLleno()) {
                                JOptionPane.showMessageDialog(null, "¡Empate!");
                                reiniciarJuego();
                            } else {
                                juego.cambiarTurno();
                            }
                        }
                    }
                });
                add(botones[i][j]);
            }
        }
    }

    private void reiniciarJuego() {
        juego = new Raya();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                botones[i][j].setText("");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JuegoGrafico().setVisible(true);
        });
    }
}
