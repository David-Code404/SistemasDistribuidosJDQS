<?php

namespace App\Http\Controllers;
use App\Models\Pronostico;
use Illuminate\Http\Request;

class PronosticoController extends Controller
{
  
    public function index()
    {
        return Pronostico::all();
    }

 
    public function store(Request $request)
    {
        $validated = $request->validate([
            'fecha' => 'required|date',
            'cantidad_estimada' => 'required|integer',
        ]);

        return Pronostico::create($validated);
    }

    
    public function show(string $id)
    {
        return Pronostico::findOrFail($id);
    }

  
    public function update(Request $request, string $id)
    {
        $pronostico = Pronostico::findOrFail($id);
        $pronostico->update($request->all());
        return $pronostico;
    }

   
    public function destroy(string $id)
    {
        Pronostico::destroy($id);
        return response()->json(['mensaje' => 'Eliminado']);
    }
}