package com.example.login.configuration

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class DBHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    override fun onCreate(db: SQLiteDatabase) { // TODO Auto-generated method stub
        db.execSQL(
                "create table location " +
                        "(id integer primary key autoincrement, lat text,lan text,vehicle text,timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) { // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS location")
        onCreate(db)
    }

    fun insertLocation(lat: String?, lan: String?,veh: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("lat", lat)
        contentValues.put("lan", lan)
        contentValues.put("vehicle", veh)
        db.insert("location", null, contentValues)
        return true
    }

    fun getData(id: Int): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("select * from location where id=$id", null)
    }

    fun numberOfRows(): Int {
        val db = this.readableDatabase
        return DatabaseUtils.queryNumEntries(db, LOCATION_TABLE_NAME).toInt()
    }

    fun updateLocation(id: Int?, lat: String?, lan: String?, alt: String?): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("lat", lat)
        contentValues.put("lan", lan)
        db.update("location", contentValues, "id = ? ", arrayOf(Integer.toString(id!!)))
        return true
    }

    fun deleteLocation(id: Int?): Int {
        val db = this.writableDatabase
        println("deleting location from db")
        return db.delete("location",
                "id = ? ", arrayOf(Integer.toString(id!!)))
    }

    //hp = new HashMap();
    val allLocation: ArrayList<String>
        get() {
            val array_list = ArrayList<String>()
            //hp = new HashMap();
            val db = this.readableDatabase
            val res = db.rawQuery("select * from location", null)
            res.moveToFirst()
            while (res.isAfterLast == false) {
                array_list.add(res.getString(res.getColumnIndex("id")))
                res.moveToNext()
            }
            return array_list
        }



    companion object {
        const val DATABASE_NAME = "LocalDB.db"
        const val LOCATION_TABLE_NAME = "location"
        const val LOCATION_COLUMN_ID = "id"
        const val LOCATION_COLUMN_LAT = "lat"
        const val LOCATION_COLUMN_LAN = "lan"
        const val CONTACTS_COLUMN_TIMESTAMP = "timestamp"
    }
}