package com.example.tfb.Querys

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import com.example.tfb.*
import com.example.tfb.Conexion.AdminSQLiteConexion

object Conexion {
    private var DATABASE_NAME = "encuestas.db"
    private const val DATABASE_VERSION = 1

    fun cambiarBD(nombreBD: String) {
        this.DATABASE_NAME = nombreBD
    }

    fun init(contexto: Context) {
        val admin = AdminSQLiteConexion(contexto, DATABASE_NAME, null, DATABASE_VERSION)
        admin.writableDatabase // Crea o actualiza la base de datos si es necesario
        admin.close()
    }

    fun maxScoreLocal(contexto: AppCompatActivity): Int {
        var maxLocal = 0
        val admin = AdminSQLiteConexion(contexto, DATABASE_NAME, null, DATABASE_VERSION)
        val db = admin.readableDatabase
        val cursor = db.rawQuery("SELECT MAX(maxscore) AS maxscore FROM usuarioOffline", null)

        if (cursor.moveToFirst()) {
            maxLocal = cursor.getInt(cursor.getColumnIndexOrThrow("maxscore"))
        }

        cursor.close() // Importante cerrar el cursor
        db.close()     // Importante cerrar la base de datos
        return maxLocal
    }


    // este metodo se borrara en cuanto la base de datos sea online
    fun addUsuario(contexto: AppCompatActivity, usuario: Usuario): Long {
        val admin = AdminSQLiteConexion(contexto, DATABASE_NAME, null, DATABASE_VERSION)
        val db = admin.writableDatabase
        val registro = ContentValues().apply {
            put("nombre", usuario.nombre)
            put("correo", usuario.email)
            put("provider", usuario.provider.toString())
            put("score", usuario.maxscore)
        }
        val codigo = db.insert("Usuarios", null, registro)
        db.close()
        return codigo
    }


    fun addPuntuacionOffline(contexto: AppCompatActivity): Long {
        val usuarioActual = Usuario.currentUsuario ?: throw IllegalStateException("Usuario.currentUsuario no está inicializado")
        val admin = AdminSQLiteConexion(contexto, DATABASE_NAME, null, DATABASE_VERSION)
        val db = admin.writableDatabase
        val registro = ContentValues().apply {
            put("score", usuarioActual.maxscore)
        }
        val codigo = db.insert("UsuarioOffline", null, registro)
        db.close()
        return codigo
    }


}
