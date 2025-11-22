using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Web.Services;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

namespace ServidorPagos
{
    // Modelo auxiliar
    public class Deuda
    {
        public int Id { get; set; }
        public string Empresa { get; set; }
        public decimal Monto { get; set; }
        public string Estado { get; set; }
    }

    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // CORRECCIÓN AQUÍ: El nombre debe ser WebService1 para que coincida con tu archivo
    public class WebService1 : System.Web.Services.WebService
    {
        private static readonly HttpClient client = new HttpClient();

        [WebMethod]
        public Deuda[] VerDeudas(string CI)
        {
            if (!ValidarSegip(CI))
            {
                return null;
            }

            List<Deuda> listaTotal = new List<Deuda>();

            // CESSA (Puerto 8001)
            listaTotal.AddRange(ObtenerDeudasEmpresa("http://127.0.0.1:8001/api/facturas?ci=" + CI));

            // ELAPAS (Puerto 8002)
            listaTotal.AddRange(ObtenerDeudasEmpresa("http://127.0.0.1:8002/api/facturas?ci=" + CI));

            return listaTotal.ToArray();
        }

        [WebMethod]
        public string PagarDeuda(int idFactura, string empresa)
        {
            string url = "";
            if (empresa == "CESSA") url = $"http://127.0.0.1:8001/api/facturas/{idFactura}";
            if (empresa == "ELAPAS") url = $"http://127.0.0.1:8002/api/facturas/{idFactura}";

            try
            {
                var response = client.PutAsync(url, null).Result;
                if (response.IsSuccessStatusCode) return "Pago Exitoso";
                return "Error en el servidor de la empresa";
            }
            catch (Exception ex)
            {
                return "Error de conexión: " + ex.Message;
            }
        }

        // --- FUNCIONES PRIVADAS ---

        private bool ValidarSegip(string ci)
        {
            try
            {
                var query = new { query = $"{{ persona(ci: \"{ci}\") {{ ci names }} }}" };
                var content = new StringContent(JsonConvert.SerializeObject(query), Encoding.UTF8, "application/json");

                // OJO: Asegúrate que tu Node.js corre en el puerto 4000
                var response = client.PostAsync("http://localhost:4000/graphql", content).Result;
                var jsonString = response.Content.ReadAsStringAsync().Result;

                var json = JObject.Parse(jsonString);
                return json["data"]["persona"].Type != JTokenType.Null;
            }
            catch
            {
                return false;
            }
        }

        private List<Deuda> ObtenerDeudasEmpresa(string url)
        {
            List<Deuda> lista = new List<Deuda>();
            try
            {
                var response = client.GetStringAsync(url).Result;
                var datos = JsonConvert.DeserializeObject<List<dynamic>>(response);

                foreach (var item in datos)
                {
                    lista.Add(new Deuda
                    {
                        Id = (int)item.id,
                        Empresa = (string)item.empresa,
                        Monto = (decimal)item.monto,
                        Estado = (string)item.estado
                    });
                }
            }
            catch { }
            return lista;
        }
    }
}