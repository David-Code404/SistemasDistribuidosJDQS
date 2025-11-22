<?php

define('API_URL_REST', 'http://127.0.0.1:8000/api');
define('API_URL_GRAPHQL', 'http://127.0.0.1:8000/graphql');

echo "Iniciando Cliente PHP\n";

$idParaActualizar = 16; 
$idParaEliminar = 10;   


function callApi($url, $data, $token = null) {
    $ch = curl_init($url);
    
    $payload = json_encode($data);
    
    $headers = [
        'Content-Type: application/json',
        'Accept: application/json',
    ];
    
    if ($token) {
        $headers[] = 'Authorization: Bearer ' . $token;
    }
    
    curl_setopt($ch, CURLOPT_POSTFIELDS, $payload);
    curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
    curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
    
    $response = curl_exec($ch);
    $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
    curl_close($ch);
    
    if ($httpCode >= 200 && $httpCode < 300) {
        return json_decode($response, true); 
    } else {
        echo "\nError en la API. Código: $httpCode\n";
        echo "Respuesta: $response\n";
        return null;
    }
}

function login($email, $password) {
    echo "Intentando login (REST)...\n";
    $data = ['email' => $email, 'password' => $password];
    $response = callApi(API_URL_REST . '/login', $data);
    
    if (isset($response['token'])) {
        return $response['token'];
    }
    return null;
}

function getPersonas($token) {
    echo "\nIntentando obtener personas (GraphQL)...\n";
    
    $graphqlQuery = [
        'query' => 'query { personas { id nombres apellidos email } }'
    ];
    
    $response = callApi(API_URL_GRAPHQL, $graphqlQuery, $token);
    
    if (isset($response['data'])) {
        echo "Personas obtenidas:\n";
        echo json_encode($response['data']['personas'], JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES);
        echo "\n"; 
    } else {
        print_r($response); 
    }
}

function createPersona($token) {
    echo "\nIntentando crear persona (GraphQL)...\n";
    
    $ciUnico = substr(time(), -10); 
    $emailUnico = "php-" . $ciUnico . "@test.com";

    $variables = [
        'nombres' => 'PHP Client',
        'apellidos' => 'cURL',
        'ci' => $ciUnico,
        'email' => $emailUnico,
        'direccion' => 'Av. PHP 7.4',
        'telefono' => '111222333'
    ];

    $mutation = [
        'query' => '
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
            }',
        'variables' => $variables
    ];
    
    $response = callApi(API_URL_GRAPHQL, $mutation, $token);
    
    if (isset($response['data']['createPersona'])) {
        echo " Persona Creada:\n";
        echo json_encode($response['data']['createPersona'], JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES);
        echo "\n";
    } else {
        print_r($response); 
    }
}

function updatePersona($token, $id, $nuevosNombres) {
    echo "\nIntentando actualizar persona ID: $id (GraphQL)...\n";

    $variables = [
        'id' => (int)$id, 
        'nombres' => $nuevosNombres
    ];

    $mutation = [
        'query' => '
            mutation UpdatePersona($id: Int!, $nombres: String) {
              updatePersona(id: $id, nombres: $nombres) {
                id nombres apellidos
              }
            }',
        'variables' => $variables
    ];
    
    $response = callApi(API_URL_GRAPHQL, $mutation, $token);
    
    if (isset($response['data']['updatePersona'])) {
        echo "Persona Actualizada:\n";
        echo json_encode($response['data']['updatePersona'], JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES);
        echo "\n"; 
    } else {
        print_r($response); 
    }
}

function deletePersona($token, $id) {
    echo "\nIntentando eliminar persona ID: $id (GraphQL)...\n";

    $variables = ['id' => (int)$id];

    $mutation = [
        'query' => '
            mutation DeletePersona($id: Int!) {
              deletePersona(id: $id) {
                id nombres
              }
            }',
        'variables' => $variables
    ];
    
    $response = callApi(API_URL_GRAPHQL, $mutation, $token);
    
    if (isset($response['data']['deletePersona'])) {
        echo " Persona Eliminada:\n";
        echo json_encode($response['data']['deletePersona'], JSON_PRETTY_PRINT | JSON_UNESCAPED_SLASHES);
        echo "\n"; 
    } else {
        print_r($response); 
    }
}

try {
    $token = login("admin@sis258.com", "password");
    
    if ($token) {
        echo "Login exitoso.\n";
        
        echo "\n--- Lista Original ---";
        getPersonas($token);

        createPersona($token);
        
        updatePersona($token, $idParaActualizar, "Actualizado (PHP)");
        
        deletePersona($token, $idParaEliminar);

        echo "\n--- Lista actualizada ---";
        getPersonas($token);

    } else {
        echo "error el login.\n";
    }

} catch (Exception $e) {
    echo "\nHa ocurrido un error fatal: " . $e->getMessage() . "\n";
}

?>