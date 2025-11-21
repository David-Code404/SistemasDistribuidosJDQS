using MySql.Data.MySqlClient;
using System;
using System.Collections.Generic;
using System.Web.Services;

namespace ServicioSegip
{
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    public class WebService1 : System.Web.Services.WebService
    {
        public class Persona
        {
            public int Id { get; set; }
            public string Ci { get; set; }
            public string Nombres { get; set; }
            public string PrimerApellido { get; set; }
            public string SegundoApellido { get; set; }
        }

        private MySqlConnection ObtenerConexion()
        {
            return new MySqlConnection("Server=127.0.0.1;Database=Personas;Uid=root;Pwd=;");
        }

        [WebMethod]
        public Persona BuscarPersonaCI(string NumeroDocumento)
        {
            Persona personaEncontrada = null;

            using (MySqlConnection cn = ObtenerConexion())
            {
                try
                {
                    string query = "SELECT * FROM persona WHERE ci = @ci";

                    using (MySqlCommand cmd = new MySqlCommand(query, cn))
                    {
                        cmd.Parameters.AddWithValue("@ci", NumeroDocumento);
                        cn.Open();

                        using (MySqlDataReader reader = cmd.ExecuteReader())
                        {
                            if (reader.Read())
                            {
                                personaEncontrada = new Persona();
                                personaEncontrada.Id = Convert.ToInt32(reader["id"]);
                                personaEncontrada.Ci = reader["ci"].ToString();
                                personaEncontrada.Nombres = reader["nombres"].ToString();
                                personaEncontrada.PrimerApellido = reader["primer_apellido"].ToString();
                                personaEncontrada.SegundoApellido = reader["segundo_apellido"].ToString();
                            }
                        }
                    }
                }
                catch
                {
                    return null;
                }
            }
            return personaEncontrada;
        }

        [WebMethod]
        public Persona[] BuscarPersonas(string PrimerApellido, string SegundoApellido, string Nombres)
        {
            List<Persona> listaResultados = new List<Persona>();

            using (MySqlConnection cn = ObtenerConexion())
            {
                try
                {
                    string query = "SELECT * FROM persona WHERE primer_apellido = @pa AND segundo_apellido = @sa AND nombres = @nom";

                    using (MySqlCommand cmd = new MySqlCommand(query, cn))
                    {
                        cmd.Parameters.AddWithValue("@pa", PrimerApellido);
                        cmd.Parameters.AddWithValue("@sa", SegundoApellido);
                        cmd.Parameters.AddWithValue("@nom", Nombres);

                        cn.Open();
                        using (MySqlDataReader reader = cmd.ExecuteReader())
                        {
                            while (reader.Read())
                            {
                                Persona p = new Persona();
                                p.Id = Convert.ToInt32(reader["id"]);
                                p.Ci = reader["ci"].ToString();
                                p.Nombres = reader["nombres"].ToString();
                                p.PrimerApellido = reader["primer_apellido"].ToString();
                                p.SegundoApellido = reader["segundo_apellido"].ToString();

                                listaResultados.Add(p);
                            }
                        }
                    }
                }
                catch
                {
                    return null;
                }
            }
            return listaResultados.ToArray();
        }
    }
}