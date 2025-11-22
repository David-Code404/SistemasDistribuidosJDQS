<?php

namespace App\Http\Controllers;

use App\Models\Factura;
use Illuminate\Http\Request;

class FacturaController extends Controller
{
    // 1. BUSCAR FACTURAS (GET /api/facturas?ci=12345)
    public function index(Request $request)
    {
        // Validamos que envíen el CI
        if (!$request->has('ci')) {
            return response()->json(['mensaje' => 'Se requiere el parametro CI'], 400);
        }

        // Buscamos facturas de ese CI que estén pendientes
        $facturas = Factura::where('ci_cliente', $request->ci)
                           ->where('estado', 'pendiente')
                           ->get();

        return response()->json($facturas);
    }

    // 2. PAGAR FACTURA (PUT /api/facturas/{id})
    public function update(Request $request, string $id)
    {
        $factura = Factura::find($id);

        if (!$factura) {
            return response()->json(['mensaje' => 'Factura no encontrada'], 404);
        }

        // Cambiamos el estado a pagado
        $factura->estado = 'pagado';
        $factura->save();

        return response()->json([
            'mensaje' => 'Pago procesado correctamente', 
            'factura' => $factura
        ]);
    }
    
    // 3. CREAR FACTURA (POST /api/facturas) - Útil para rellenar datos
    public function store(Request $request) {
        return Factura::create($request->all());
    }
}