using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ClienteEscritorio
{
    public partial class Form1 : Form
    {

        ServiceReference1.WebService1SoapClient servicio;

        public Form1()
        {
            InitializeComponent();

            servicio = new ServiceReference1.WebService1SoapClient();
        }

        
        private void button1_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(textBox1.Text))
            {
                MessageBox.Show("Por favor ingresa un CI.");
                return;
            }

            string ci = textBox1.Text;

            try
            {
                
                var datos = servicio.ObtenerDatosAcademicos(ci);

              
                richTextBox1.Clear();
                richTextBox1.AppendText("=== DATOS ACADÉMICOS ===\n");
                richTextBox1.AppendText("CI: " + datos.CI + "\n");
                richTextBox1.AppendText("Nombre: " + datos.Nombres + " " + datos.Apellidos + "\n");
                richTextBox1.AppendText("Carrera: " + datos.Carrera + "\n");
                richTextBox1.AppendText("Semestre: " + datos.Semestre + "\n");
                richTextBox1.AppendText("Promedio: " + datos.Promedio + "\n");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al conectar con el servicio: " + ex.Message);
            }
        }

       
        private void button2_Click(object sender, EventArgs e)
        {
            if (string.IsNullOrEmpty(textBox1.Text))
            {
                MessageBox.Show("Por favor ingresa un CI.");
                return;
            }

            string ci = textBox1.Text;

            try
            {
                
                var datos = servicio.ObtenerDatosTutor(ci);

                
                richTextBox1.Clear();
                richTextBox1.AppendText("=== DATOS DEL TUTOR ===\n");
                richTextBox1.AppendText("Estudiante: " + datos.Nombres + "\n"); // El servicio devuelve el nombre del alumno aquí también
                richTextBox1.AppendText("Tutor Asignado: " + datos.TutorAsignado + "\n");
                richTextBox1.AppendText("Email Tutor: " + datos.CorreoTutor + "\n");
                richTextBox1.AppendText("Teléfono Tutor: " + datos.TelefonoTutor + "\n");
            }
            catch (Exception ex)
            {
                MessageBox.Show("Error al conectar con el servicio: " + ex.Message);
            }
        }

        
        private void textBox1_TextChanged(object sender, EventArgs e) { }

        private void richTextBox1_TextChanged(object sender, EventArgs e) { }
    }
}