Imports System.Net
Imports System.Collections.Generic

Public Class Form1

    Private Sub Form1_Load(sender As Object, e As EventArgs) Handles MyBase.Load
        ' Parche para evitar errores de conexión roja
        ServicePointManager.ServerCertificateValidationCallback = Function() True
    End Sub

    ' ==========================================
    ' BOTÓN 1: BUSCAR POR CI
    ' ==========================================
    Private Sub Button1_Click(sender As Object, e As EventArgs) Handles Button1.Click
        Try
            ' CAMBIO 1: Usamos ServiceReference1 en vez de RefSegip
            Dim cliente As New ServiceReference1.WebService1SoapClient()

            Dim ci As String = TextBox1.Text
            Dim persona = cliente.BuscarPersonaCI(ci)

            If persona IsNot Nothing Then
                ' CAMBIO 2: Usamos ServiceReference1.Persona
                Dim lista As New List(Of ServiceReference1.Persona)
                lista.Add(persona)
                DataGridView1.DataSource = lista
            Else
                DataGridView1.DataSource = Nothing
                MsgBox("No se encontró el CI")
            End If
        Catch ex As Exception
            MsgBox("Error: " & ex.Message)
        End Try
    End Sub

    ' ==========================================
    ' BOTÓN 2: BUSCAR POR NOMBRES
    ' ==========================================
    Private Sub Button2_Click(sender As Object, e As EventArgs) Handles Button2.Click
        Try
            ' CAMBIO 3: Usamos ServiceReference1
            Dim cliente As New ServiceReference1.WebService1SoapClient()

            Dim primerApe As String = TextBox2.Text
            Dim segundoApe As String = TextBox3.Text
            Dim nombres As String = TextBox4.Text

            Dim resultados = cliente.BuscarPersonas(primerApe, segundoApe, nombres)

            If resultados IsNot Nothing AndAlso resultados.Length > 0 Then
                DataGridView1.DataSource = resultados
            Else
                DataGridView1.DataSource = Nothing
                MsgBox("No se encontraron personas")
            End If

        Catch ex As Exception
            MsgBox("Error: " & ex.Message)
        End Try
    End Sub

End Class