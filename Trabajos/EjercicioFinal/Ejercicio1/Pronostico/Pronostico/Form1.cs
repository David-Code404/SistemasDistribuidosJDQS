using Newtonsoft.Json; // Necesario para leer JSON
using Newtonsoft.Json.Linq; // Necesario para leer JSON avanzado
using System;
using System.Collections.Generic;
using System.Net.Http; // Necesario para conectar con Laravel
using System.Text;
using System.Windows.Forms;

namespace Pronostico
{
    public partial class Form1 : Form
    {
        // 1. Configuración de conexión
        private static readonly HttpClient client = new HttpClient();
        private const string BASE_URL = "http://127.0.0.1:8000"; // Asegúrate que tu Laravel corre aquí

        public Form1()
        {
            InitializeComponent();

            // 2. Llenar el ComboBox al iniciar
            comboBox1.Items.Clear();
            comboBox1.Items.Add("REST");
            comboBox1.Items.Add("GraphQL");
            comboBox1.SelectedIndex = 0; // Seleccionar REST por defecto
        }

        // Clase para mapear los datos que vienen de Laravel
        public class DatoPronostico
        {
            public int id { get; set; }
            public string fecha { get; set; }
            public int cantidad_estimada { get; set; }
        }

        // BOTÓN 1: CARGAR DATOS (GET / Query)
        private async void button1_Click(object sender, EventArgs e)
        {
            string modo = comboBox1.SelectedItem.ToString();
            List<DatoPronostico> lista = new List<DatoPronostico>();

            try
            {
                if (modo == "REST")
                {
                    // Lógica REST
                    var response = await client.GetStringAsync($"{BASE_URL}/api/pronosticos");
                    lista = JsonConvert.DeserializeObject<List<DatoPronostico>>(response);
                }
                else
                {
                    // Lógica GraphQL
                    var query = new { query = "{ pronosticos { id fecha cantidad_estimada } }" };
                    var content = new StringContent(JsonConvert.SerializeObject(query), Encoding.UTF8, "application/json");

                    var response = await client.PostAsync($"{BASE_URL}/graphql", content);
                    var jsonString = await response.Content.ReadAsStringAsync();

                    // Extraer datos del nodo "data" -> "pronosticos"
                    var json = JObject.Parse(jsonString);
                    lista = json["data"]["pronosticos"].ToObject<List<DatoPronostico>>();
                }

                // Mostrar en la tabla
                dataGridView1.DataSource = lista;
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al cargar: " + ex.Message);
            }
        }

        // BOTÓN 2: GUARDAR DATOS (POST / Mutation)
        private async void button2_Click(object sender, EventArgs e)
        {
            // Validaciones simples
            if (string.IsNullOrEmpty(textBox1.Text) || string.IsNullOrEmpty(textBox2.Text)) // OJO: Asegúrate de tener textBox2
            {
                MessageBox.Show("Por favor llena Fecha (textBox1) y Cantidad (textBox2)");
                return;
            }

            string modo = comboBox1.SelectedItem.ToString();
            string fecha = textBox1.Text; // Ej: 2025-06-10

            if (!int.TryParse(textBox2.Text, out int cantidad))
            {
                MessageBox.Show("La cantidad debe ser un número entero.");
                return;
            }

            try
            {
                if (modo == "REST")
                {
                    var data = new { fecha = fecha, cantidad_estimada = cantidad };
                    var content = new StringContent(JsonConvert.SerializeObject(data), Encoding.UTF8, "application/json");

                    var response = await client.PostAsync($"{BASE_URL}/api/pronosticos", content);

                    if (response.IsSuccessStatusCode)
                        MessageBox.Show("¡Guardado con REST!");
                    else
                        MessageBox.Show("Error REST: " + response.StatusCode);
                }
                else
                {
                    // Lógica GraphQL Mutation
                    string mutation = $@"
                    mutation {{
                        createPronostico(fecha: ""{fecha}"", cantidad_estimada: {cantidad}) {{
                            id
                        }}
                    }}";

                    var requestObj = new { query = mutation };
                    var content = new StringContent(JsonConvert.SerializeObject(requestObj), Encoding.UTF8, "application/json");

                    var response = await client.PostAsync($"{BASE_URL}/graphql", content);
                    var respuestaString = await response.Content.ReadAsStringAsync();

                    if (respuestaString.Contains("errors"))
                        MessageBox.Show("Error GraphQL: " + respuestaString);
                    else
                        MessageBox.Show("¡Guardado con GraphQL!");
                }

                // Recargar la tabla automáticamente
                button1_Click(sender, e);
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al guardar: " + ex.Message);
            }
        }

        // Eventos vacíos (No los borres si están vinculados en el diseño, o dará error)
        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e) { }
        private void textBox1_TextChanged(object sender, EventArgs e) { }
        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e) { }
    }
}