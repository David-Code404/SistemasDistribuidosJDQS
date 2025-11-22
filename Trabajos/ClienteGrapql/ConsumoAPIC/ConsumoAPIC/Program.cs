using System;
using System.Net.Http;
using System.Net.Http.Headers; 
using System.Text;
using System.Threading.Tasks;

using Newtonsoft.Json;
using Newtonsoft.Json.Linq;

const string API_URL_REST = "http://127.0.0.1:8000/api";
const string API_URL_GRAPHQL = "http://127.0.0.1:8000/graphql";

var client = new HttpClient();

Console.WriteLine("Iniciando Cliente C#");

int idParaActualizar = 19; 
int idParaEliminar = 5;   

try
{
    // 1. Hacer Login
    string? token = await Login("admin@sis258.com", "password");

    if (!string.IsNullOrEmpty(token))
    {
        Console.WriteLine("Login exitoso.");

        client.DefaultRequestHeaders.Authorization =
            new AuthenticationHeaderValue("Bearer", token);
        client.DefaultRequestHeaders.Accept.Add(
            new MediaTypeWithQualityHeaderValue("application/json"));

        Console.WriteLine("\n--- Lista Original ---");
        await GetPersonas();

        await CreatePersona();

        await UpdatePersona(idParaActualizar, "Actualizado (C#)");

        await DeletePersona(idParaEliminar);

        Console.WriteLine("\nLista actualizada");
        await GetPersonas();
    }
    else
    {
        Console.WriteLine("error el login.");
    }
}
catch (Exception e)
{
    Console.Error.WriteLine($"Ha ocurrido un error: {e.Message}");
}

async Task<string?> Login(string email, string password)
{
    Console.WriteLine("Intentando login (REST)");
    var loginData = new { email, password };
    string jsonBody = JsonConvert.SerializeObject(loginData);
    var content = new StringContent(jsonBody, Encoding.UTF8, "application/json");

    try
    {
        HttpResponseMessage response = await client.PostAsync($"{API_URL_REST}/login", content);
        string responseBody = await response.Content.ReadAsStringAsync();

        if (response.IsSuccessStatusCode)
        {
            var jsonResponse = JObject.Parse(responseBody);
            return jsonResponse["token"]?.ToString();
        }
        else
        {
            Console.WriteLine($"Error en Login. Código: {response.StatusCode}");
            Console.WriteLine($"Respuesta: {responseBody}");
            return null;
        }
    }
    catch (HttpRequestException e)
    {
        Console.WriteLine($"Error de conexión: {e.Message}");
        return null;
    }
}

async Task<string> SendGraphQLQuery(object query)
{
    string jsonBody = JsonConvert.SerializeObject(query);
    var content = new StringContent(jsonBody, Encoding.UTF8, "application/json");

    HttpResponseMessage response = await client.PostAsync(API_URL_GRAPHQL, content);
    string responseBody = await response.Content.ReadAsStringAsync();

    if (!response.IsSuccessStatusCode || responseBody.Contains("errors"))
    {
        throw new Exception($"Error en GraphQL. Código: {response.StatusCode}\nRespuesta: {responseBody}");
    }

    return responseBody;
}

async Task GetPersonas()
{
    Console.WriteLine("\nIntentando obtener personas (GraphQL)");
    var graphqlQuery = new
    {
        query = "query { personas { id nombres apellidos email } }"
    };

    try
    {
        string responseBody = await SendGraphQLQuery(graphqlQuery);
        Console.WriteLine(" Personas obtenidas:");
        var jsonResponse = JObject.Parse(responseBody);
        Console.WriteLine(jsonResponse.ToString(Formatting.Indented));
    }
    catch (Exception e)
    {
        Console.WriteLine(e.Message);
    }
}

async Task CreatePersona()
{
    Console.WriteLine("\nIntentando crear persona");
    string ticksStr = DateTime.Now.Ticks.ToString();
    string ciUnico = ticksStr.Substring(ticksStr.Length - 10);
    string emailUnico = $"cs-{ticksStr}@test.com";

    var variables = new
    {
        nombres = "C# Client",
        apellidos = "Newtonsoft",
        ci = ciUnico,
        email = emailUnico,
        direccion = "Av. DotNet 456",
        telefono = "777888999"
    };

    var mutation = new
    {
        query = @"
            mutation CreatePersona(
                $nombres: String!, $apellidos: String!, $ci: String!, 
                $email: String, $direccion: String, $telefono: String
            ) {
              createPersona(
                nombres: $nombres, apellidos: $apellidos, ci: $ci, 
                email: $email, direccion: $direccion, telefono: $telefono
              ) {
                id nombres ci email
              }
            }",
        variables = variables
    };

    try
    {
        string responseBody = await SendGraphQLQuery(mutation);
        Console.WriteLine("Persona Creada:");
        var jsonResponse = JObject.Parse(responseBody);
        Console.WriteLine(jsonResponse.ToString(Formatting.Indented));
    }
    catch (Exception e)
    {
        Console.WriteLine(e.Message);
    }
}

async Task UpdatePersona(int id, string nuevosNombres)
{
    Console.WriteLine($"\nIntentando actualizar persona ID: {id} (GraphQL)");

    var variables = new
    {
        id = id,
        nombres = nuevosNombres
    };

    var mutation = new
    {
        query = @"
            mutation UpdatePersona($id: Int!, $nombres: String) {
              updatePersona(id: $id, nombres: $nombres) {
                id nombres apellidos
              }
            }",
        variables = variables
    };

    try
    {
        string responseBody = await SendGraphQLQuery(mutation);
        Console.WriteLine("Persona Actualizada:");
        var jsonResponse = JObject.Parse(responseBody);
        Console.WriteLine(jsonResponse.ToString(Formatting.Indented));
    }
    catch (Exception e)
    {
        Console.WriteLine(e.Message);
    }
}
async Task DeletePersona(int id)
{
    Console.WriteLine($"\nIntentando eliminar persona ID: {id} (GraphQL)...");

    var variables = new { id = id };

    var mutation = new
    {
        query = @"
            mutation DeletePersona($id: Int!) {
              deletePersona(id: $id) {
                id nombres
              }
            }",
        variables = variables
    };

    try
    {
        string responseBody = await SendGraphQLQuery(mutation);
        Console.WriteLine("Persona Eliminada:");
        var jsonResponse = JObject.Parse(responseBody);
        Console.WriteLine(jsonResponse.ToString(Formatting.Indented));
    }
    catch (Exception e)
    {
        Console.WriteLine(e.Message);
    }
}