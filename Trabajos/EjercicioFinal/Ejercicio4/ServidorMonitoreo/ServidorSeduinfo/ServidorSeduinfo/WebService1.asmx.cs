using System;
using System.Web.Services;

namespace ServidorSeduinfo 
{
    
    public class Estudiante
    {
        public string CI;
        public string Nombres;
        public string Apellidos;
        public string Carrera;
        public string Semestre;
        public double Promedio;

        public string TutorAsignado;
        public string CorreoTutor;
        public string TelefonoTutor;
    }

    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    
    public class WebService1 : System.Web.Services.WebService
    {
        [WebMethod]
        public Estudiante ObtenerDatosAcademicos(string CI)
        {
            Estudiante est = new Estudiante();
            est.CI = CI;

            if (CI == "12345")
            {
                est.Nombres = "Juan";
                est.Apellidos = "Perez";
                est.Carrera = "Sistemas";
                est.Semestre = "5to";
                est.Promedio = 85.5;
            }
            else
            {
                est.Nombres = "Maria";
                est.Apellidos = "Lopez";
                est.Carrera = "Derecho";
                est.Semestre = "3ro";
                est.Promedio = 90.0;
            }
            return est;
        }

        [WebMethod]
        public Estudiante ObtenerDatosTutor(string CI)
        {
            Estudiante est = new Estudiante();

            if (CI == "12345")
            {
                est.Nombres = "Juan";
                est.TutorAsignado = "Ing. Roberto Mendez";
                est.CorreoTutor = "roberto@univ.edu";
                est.TelefonoTutor = "77712345";
            }
            else
            {
                est.Nombres = "Maria";
                est.TutorAsignado = "Lic. Ana Tapia";
                est.CorreoTutor = "ana@univ.edu";
                est.TelefonoTutor = "60098765";
            }
            return est;
        }
    }
}