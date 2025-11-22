<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Factura extends Model
{
    use HasFactory;
    
    // Habilitar asignación masiva para estos campos
    protected $fillable = [
        'ci_cliente', 
        'empresa', 
        'monto', 
        'estado'
    ];
}