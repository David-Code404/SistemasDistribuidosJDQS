<?php

namespace Database\Seeders;
use App\Models\Factura;
use App\Models\User;
use Illuminate\Database\Console\Seeds\WithoutModelEvents;
use Illuminate\Database\Seeder;

class DatabaseSeeder extends Seeder
{
    use WithoutModelEvents;

    /**
     * Seed the application's database.
     */
    public function run(): void
    {
        Factura::create([
            'ci_cliente' => '12345',
            'empresa' => 'CESSA',
            'monto' => 150.50,
            'estado' => 'pendiente'
        ]);

        Factura::create([
            'ci_cliente' => '12345',
            'empresa' => 'ELAPAS',
            'monto' => 80.00,
            'estado' => 'pendiente'
        ]);

        Factura::create([
            'ci_cliente' => '67890',
            'empresa' => 'CESSA',
            'monto' => 200.00,
            'estado' => 'pendiente'
        ]);
    }
}
