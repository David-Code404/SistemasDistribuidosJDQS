
package com.mycompany.consumoenapi;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class ConsumoEnAPI {

    private static final String API_URL_REST = "http://127.0.0.1:8000/api";
    private static final String API_URL_GRAPHQL = "http://127.0.0.1:8000/graphql";
    
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    
    private static final Gson gson = new Gson();

public static void main(String[] args) {
        System.out.println("--- Iniciando Cliente Java ---");

        int idParaActualizar = 22; 
        int idParaEliminar = 21;   
        
        try {
            // 1. Login
            String token = login("admin@sis258.com", "password");
            
            if (token != null) {
                System.out.println("Login exitoso.");
                
                System.out.println("\n--- Lista Original ---");
                getPersonas(token);

                updatePersona(token, idParaActualizar);
                
                deletePersona(token, idParaEliminar);

                System.out.println("\n--- Lista actualizada después de U/D ---");
                getPersonas(token);

            } else {
                System.out.println("error el login.");
            }
            
        } catch (Exception e) {
            System.err.println("Ha ocurrido un error: " + e.getMessage());
        }
    }

    private static String login(String email, String password) throws Exception {
        System.out.println("Intentando login (REST)");

        String jsonBody = String.format(
            "{\"email\": \"%s\", \"password\": \"%s\"}",
            email, password
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL_REST + "/login"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            return jsonResponse.get("token").getAsString(); // Devuelve el token
        } else {
            System.out.println("Error en Login. Código: " + response.statusCode());
            System.out.println("Respuesta: " + response.body());
            return null;
        }
    }

    private static void getPersonas(String token) throws Exception {
        System.out.println("\nIntentando obtener personas (GraphQL)...");

        String graphQLQuery = "{ \"query\": \"query { personas { id nombres apellidos email } }\" }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL_GRAPHQL))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + token) // Usamos el token
                .POST(HttpRequest.BodyPublishers.ofString(graphQLQuery))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Personas obtenidas:");
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            System.out.println(gson.toJson(jsonResponse));
        } else {
            System.out.println("Error en GraphQL. Codigo: " + response.statusCode());
            System.out.println("Respuesta: " + response.body());
        }
    }
    
private static void createPersona(String token) throws Exception {
        System.out.println("\nIntentando crear una nueva persona (GraphQL)...");

        // 1. Datos de la nueva persona (¡Usa un CI y Email NUEVOS!)
        String nombres = "Java";
        String apellidos = "Client";
        String ci = "999888777";
        String email = "java@cliente.com";
        String direccion = "Av. Java 123"; 
        String telefono = "777666555";
        
        String graphQLMutation = String.format(
            "{ \"query\": \"mutation CreatePersona(" +
            "    $nombres: String!, $apellidos: String!, $ci: String!, " +
            "    $email: String, $direccion: String, $telefono: String" +
            "  ) { " +
            "  createPersona(nombres: $nombres, apellidos: $apellidos, ci: $ci, " +
            "    email: $email, direccion: $direccion, telefono: $telefono" +
            "  ) { " +
            "    id nombres apellidos ci email direccion telefono " +
            "  } " +
            "}\", " +
            "\"variables\": { " +
            "  \"nombres\": \"%s\", " +
            "  \"apellidos\": \"%s\", " +
            "  \"ci\": \"%s\", " +
            "  \"email\": \"%s\", " +
            "  \"direccion\": \"%s\", " +
            "  \"telefono\": \"%s\" " +
            "} }",
            nombres, apellidos, ci, email, direccion, telefono 
        );
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL_GRAPHQL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(graphQLMutation))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 && !response.body().contains("errors")) {
            System.out.println("Persona Creada:");
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            System.out.println(gson.toJson(jsonResponse));
        } else {
            System.out.println("Error al crear." + response.statusCode());
            System.out.println("Respuesta: " + response.body());
        }
    }
    
    private static void deletePersona(String token, int idParaEliminar) throws Exception {
        System.out.println("\nIntentando eliminar la persona (GraphQL) con ID: " + idParaEliminar);

        String graphQLMutation = String.format(
            "{ \"query\": \"mutation DeletePersona($id: Int!) { " +
            "  deletePersona(id: $id) { " +
            "    id nombres " +
            "  } " +
            "}\", " +
            "\"variables\": { \"id\": %d } }",
            idParaEliminar
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL_GRAPHQL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(graphQLMutation))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Persona Eliminada:");
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            System.out.println(gson.toJson(jsonResponse));
        } else {
            System.out.println("Error al eliminar. " + response.statusCode());
            System.out.println("Respuesta: " + response.body());
        }
    }
    
    private static void updatePersona(String token, int idParaActualizar) throws Exception {
        System.out.println("\nIntentando actualizar la persona (GraphQL) con ID: " + idParaActualizar);
        String nuevosNombres = "Persona Actualizada (Java)";
        String nuevoEmail = "update-java@cliente.com";
        
        String graphQLMutation = String.format(
            "{ \"query\": \"mutation UpdatePersona($id: Int!, $nombres: String, $email: String) { " +
            "  updatePersona(id: $id, nombres: $nombres, email: $email) { " +
            "    id nombres apellidos email " + 
            "  } " +
            "}\", " +
            "\"variables\": { \"id\": %d, \"nombres\": \"%s\", \"email\": \"%s\" } }",
            idParaActualizar, nuevosNombres, nuevoEmail
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL_GRAPHQL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(graphQLMutation))
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200 && !response.body().contains("errors")) {
            System.out.println("Persona Actualizada:");
            JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
            System.out.println(gson.toJson(jsonResponse));
        } else {
            System.out.println("Error al actualizar." + response.statusCode());
            System.out.println("Respuesta: " + response.body());
        }
    }
    
}