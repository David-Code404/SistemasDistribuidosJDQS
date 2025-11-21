// OJO: Necesitas esta librería instalada via NuGet
using MySql.Data.MySqlClient;
using System;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Web.Services;

namespace ServicioCotizaciones
{
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    public class WebService2 : System.Web.Services.WebService
    {
        
        private MySqlConnection ObtenerConexion()
        {
            string cadena = ConfigurationManager.ConnectionStrings["MiConexion"].ConnectionString;
            return new MySqlConnection(cadena);
        }

        
        [WebMethod]
        public DataSet obtenerCotizacion(DateTime fecha)
        {
            using (MySqlConnection cn = ObtenerConexion())
            {
               
                string query = "SELECT * FROM cotizacion WHERE fecha = @fecha";

                using (MySqlCommand cmd = new MySqlCommand(query, cn))
                {
                    
                    cmd.Parameters.AddWithValue("@fecha", fecha.ToString("yyyy-MM-dd"));

                    MySqlDataAdapter da = new MySqlDataAdapter(cmd);
                    DataSet ds = new DataSet();

                    try
                    {
                        da.Fill(ds, "Cotizaciones");
                        return ds;
                    }
                    catch (Exception ex)
                    {
                        
                        return null;
                    }
                }
            }
        }

        
        [WebMethod]
        public string registrarCotizacion(DateTime fecha, decimal monto)
        {
            using (MySqlConnection cn = ObtenerConexion())
            {
                
                string query = "INSERT INTO cotizacion (fecha, cotizacion, cotizacion_oficial) VALUES (@fecha, @monto, 6.97)";

                using (MySqlCommand cmd = new MySqlCommand(query, cn))
                {
                    cmd.Parameters.AddWithValue("@fecha", fecha.ToString("yyyy-MM-dd"));
                    cmd.Parameters.AddWithValue("@monto", monto);

                    try
                    {
                        cn.Open();
                        int filas = cmd.ExecuteNonQuery();
                        if (filas > 0)
                            return "Cotización registrada exitosamente en MySQL.";
                        else
                            return "No se guardó. Verifique los datos.";
                    }
                    catch (MySqlException ex)
                    {
                        
                        if (ex.Number == 1062)
                            return "Error: Ya existe una cotización para esa fecha.";

                        return "Error MySQL: " + ex.Message;
                    }
                    catch (Exception ex)
                    {
                        return "Error general: " + ex.Message;
                    }
                }
            }
        }
    }
}