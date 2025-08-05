/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package primero;

import java.util.Scanner;

/**
 *
 * @author David
 */
public class Primero {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        int op = 0;
        int n = 0;
        Scanner sc = new Scanner(System.in);
        
        while(op!=5){
            System.out.println("1. Introducir n Numero");
            System.out.println("2. Fibonacci ");
            System.out.println("3. Factorial");
            System.out.println("4. Sumatoria");
            System.out.println("5. Salir");
            System.out.println("Introduzca opcion");
            op = sc.nextInt();
            switch(op){
                case 1:
                    System.out.println("Introduzca el valor de n");
                    n = sc.nextInt();
                    break;
                case 2:
                    System.out.println("El fibonacci es.."+fibonacci(n));
                    break;
                case 3:
                    System.out.println("El factorial es.."+factorial(n));
                    break;
                case 4:
                    System.out.println("La sumatoria es.."+sumatoria(n));
                    
            }
        }
    }

    private static int fibonacci(int n) {
        if(n == 0){
            return 0;
        }
        if(n == 1){
            return 1;
        }
        return fibonacci(n-1)+fibonacci(n-2);
    }

    private static int factorial(int n) {
        int res = 1;
        for(int i = 1; i <= n; i++){
            res *= i;
        }
        return res;
    }

    private static int sumatoria(int n) {
        int res = 0;
        for(int i = 1; i<= n; i++){
            res += i;
        }
        return res;
    }
    
}
