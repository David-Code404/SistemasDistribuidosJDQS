Imports System.Net
Imports System.Data

Public Class Form1
    Private Sub Form1_Load(sender As Object, e As EventArgs) Handles MyBase.Load
        ServicePointManager.ServerCertificateValidationCallback = Function() True
    End Sub

    Private Sub Button1_Click(sender As Object, e As EventArgs) Handles Button1.Click
        Try
            '
            Dim cliente As New ServiceReference1.WebService2SoapClient()
            Dim ds As DataSet = cliente.obtenerCotizacion(DateTimePicker1.Value)

            If ds IsNot Nothing AndAlso ds.Tables.Count > 0 AndAlso ds.Tables(0).Rows.Count > 0 Then
                DataGridView1.DataSource = ds.Tables(0)
            Else
                DataGridView1.DataSource = Nothing
                MsgBox("No hay datos.")
            End If
        Catch ex As Exception
            MsgBox(ex.Message)
        End Try
    End Sub

    Private Sub Button2_Click(sender As Object, e As EventArgs) Handles Button2.Click
        Try
            Dim cliente As New ServiceReference1.WebService2SoapClient()
            Dim monto As Decimal = Convert.ToDecimal(TextBox1.Text)
            Dim respuesta As String = cliente.registrarCotizacion(DateTimePicker1.Value, monto)
            MsgBox(respuesta)
            Button1_Click(Nothing, Nothing)
        Catch ex As Exception
            MsgBox(ex.Message)
        End Try
    End Sub
End Class