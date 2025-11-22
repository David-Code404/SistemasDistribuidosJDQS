using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ClientePagos
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
            string ci = textBox1.Text;

            if (string.IsNullOrEmpty(ci))
            {
                MessageBox.Show("Por favor escribe un CI.");
                return;
            }

            try
            {
                var deudas = servicio.VerDeudas(ci);

                if (deudas == null || deudas.Length == 0)
                {
                    MessageBox.Show("No se encontraron deudas o el CI no existe.");
                    dataGridView1.DataSource = null;
                }
                else
                {
                    dataGridView1.DataSource = deudas;
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            if (dataGridView1.CurrentRow == null)
            {
                MessageBox.Show("Selecciona una fila.");
                return;
            }

            try
            {
                var fila = dataGridView1.CurrentRow;
                int idFactura = int.Parse(fila.Cells["Id"].Value.ToString());
                string empresa = fila.Cells["Empresa"].Value.ToString();

                string respuesta = servicio.PagarDeuda(idFactura, empresa);

                MessageBox.Show(respuesta);

                button1_Click(sender, e);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }
    }
}