<?php

use Illuminate\Support\Facades\Route;
use App\Http\Controllers\FacturaController;


Route::apiResource('facturas', FacturaController::class);