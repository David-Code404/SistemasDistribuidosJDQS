using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace ServidorMonitoreo
{
    class Program
    {
        // VARIABLES GLOBALES DE ESTADO (Compartidas)
        // Aquí almacenamos los IDs de los sensores según su estado
        static List<string> sensoresAlto = new List<string>();
        static List<string> sensoresMedio = new List<string>();
        static List<string> sensoresBajo = new List<string>();

        // Objeto para bloqueo (Thread-Safety) para que los hilos no choquen
        static readonly object candado = new object();

        static void Main(string[] args)
        {
            TcpListener server = null;
            try
            {
                // Escuchar en cualquier IP local, puerto 8888
                Int32 port = 8888;
                IPAddress localAddr = IPAddress.Any;
                server = new TcpListener(localAddr, port);

                server.Start();
                Console.WriteLine("--- SERVIDOR DE MONITOREO INICIADO ---");
                Console.WriteLine("Esperando sensores...");

                while (true)
                {
                    // Esperar conexión (bloqueante hasta que alguien entre)
                    TcpClient client = server.AcceptTcpClient();
                    Console.WriteLine("Nuevo cliente conectado.");

                    // CREAR UN HILO NUEVO PARA ESTE CLIENTE
                    Thread clientThread = new Thread(new ParameterizedThreadStart(HandleClient));
                    clientThread.Start(client);
                }
            }
            catch (Exception e)
            {
                Console.WriteLine("Error: " + e.Message);
            }
        }

        // Lógica de cada hilo (Estado interno del cliente)
        public static void HandleClient(object obj)
        {
            TcpClient client = (TcpClient)obj;
            NetworkStream stream = client.GetStream();

            // Buffer interno del cliente (su propio estado de memoria)
            byte[] bytes = new byte[256];
            string data = null;

            try
            {
                int i;
                // Bucle de lectura mientras el cliente esté conectado
                while ((i = stream.Read(bytes, 0, bytes.Length)) != 0)
                {
                    data = Encoding.ASCII.GetString(bytes, 0, i).Trim(); // Limpiar espacios
                    Console.WriteLine($"Recibido: {data}");

                    string respuesta = "";

                    // CASO 1: El cliente pide REPORTE
                    if (data.ToLower() == "reporte")
                    {
                        lock (candado) // Bloqueamos para leer seguro
                        {
                            // Prioridad: Alto > Medio > Bajo
                            if (sensoresAlto.Count > 0)
                                respuesta = $"CRITICO: Sensor {sensoresAlto[0]} está en ALTO.";
                            else if (sensoresMedio.Count > 0)
                                respuesta = $"ALERTA: Sensor {sensoresMedio[0]} está en MEDIO.";
                            else if (sensoresBajo.Count > 0)
                                respuesta = $"NORMAL: Sensor {sensoresBajo[0]} está en BAJO.";
                            else
                                respuesta = "Sin datos de sensores.";
                        }
                    }
                    // CASO 2: El cliente envía datos (Formato: "sensor01,alto")
                    else if (data.Contains(","))
                    {
                        string[] partes = data.Split(',');
                        if (partes.Length == 2)
                        {
                            string idSensor = partes[0].Trim();
                            string estado = partes[1].Trim().ToLower();

                            // Actualizamos las listas globales (Thread-Safe)
                            lock (candado)
                            {
                                // Primero quitamos el sensor de todas las listas para no duplicarlo
                                sensoresAlto.Remove(idSensor);
                                sensoresMedio.Remove(idSensor);
                                sensoresBajo.Remove(idSensor);

                                // Lo agregamos a la lista correspondiente
                                switch (estado)
                                {
                                    case "alto":
                                        sensoresAlto.Add(idSensor);
                                        break;
                                    case "medio":
                                        sensoresMedio.Add(idSensor);
                                        break;
                                    case "bajo":
                                        sensoresBajo.Add(idSensor);
                                        break;
                                }
                            }
                            // Respuesta requerida por el ejercicio
                            respuesta = $"{estado.ToUpper()} OK";
                        }
                        else
                        {
                            respuesta = "Formato incorrecto. Use: ID,ESTADO";
                        }
                    }
                    else
                    {
                        respuesta = "Comando no reconocido.";
                    }

                    // Enviar respuesta al cliente
                    byte[] msg = Encoding.ASCII.GetBytes(respuesta);
                    stream.Write(msg, 0, msg.Length);
                }
            }
            catch (Exception ex)
            {
                Console.WriteLine("Cliente desconectado: " + ex.Message);
            }
            finally
            {
                client.Close();
            }
        }
    }
}