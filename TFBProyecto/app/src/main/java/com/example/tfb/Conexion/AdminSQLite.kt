package com.example.tfb.Conexion

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQLiteConexion(contexto: Context, nombre: String,
                          factory: SQLiteDatabase.CursorFactory?,
                          version: Int
) : SQLiteOpenHelper(contexto, nombre, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        //esto se quitara al hacer la bbdd online
        val createTable = """
            CREATE TABLE Usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                email VARCHAR NOT NULL,
                provider TEXT NOT NULL,
                score INTEGER NOT NULL
            )
        """
        db.execSQL(createTable)

        val tabla2 = """
            CREATE TABLE UsuarioOffline (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                score INTEGER NOT NULL
            )
        """
        db.execSQL(tabla2)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Usuarios")
        onCreate(db)
    }
}
