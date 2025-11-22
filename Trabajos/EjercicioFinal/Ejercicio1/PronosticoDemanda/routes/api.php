<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;


use App\Http\Controllers\PronosticoController;

Route::apiResource('pronosticos', PronosticoController::class);