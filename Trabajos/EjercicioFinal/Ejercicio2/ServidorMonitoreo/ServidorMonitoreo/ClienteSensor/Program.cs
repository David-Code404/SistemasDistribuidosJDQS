using System;
using System.Net.Sockets;
using System.Text;

namespace ClienteSensor
{
    class Program
    {
        static void Main(string[] args)
        {
            try
            {
                Int32 port = 8888;
                TcpClient client = new TcpClient("127.0.0.1", port);

                Console.WriteLine("--- CONECTADO AL SISTEMA DE MONITOREO ---");
                Console.WriteLine("Instrucciones:");
                Console.WriteLine("1. Enviar datos: [ID],[ESTADO] (ej: sensor01,alto)");
                Console.WriteLine("2. Pedir reporte: reporte");
                Console.WriteLine("-----------------------------------------");

                NetworkStream stream = client.GetStream();

                while (true)
                {
                    Console.Write("\nIngrese comando: ");
                    string mensaje = Console.ReadLine();

                    // Enviar al servidor
                    byte[] data = Encoding.ASCII.GetBytes(mensaje);
                    stream.Write(data, 0, data.Length);

                    // Recibir respuesta
                    data = new byte[256];
                    String responseData = String.Empty;
                    Int32 bytes = stream.Read(data, 0, data.Length);
                    responseData = Encoding.ASCII.GetString(data, 0, bytes);

                    Console.WriteLine($"Respuesta del Servidor: {responseData}");
                }
            }
            catch (ArgumentNullException e)
            {
                Console.WriteLine("ArgumentNullException: {0}", e);
            }
            catch (SocketException e)
            {
                Console.WriteLine("No se pudo conectar al servidor. ¿Está encendido?");
            }
        }
    }
}