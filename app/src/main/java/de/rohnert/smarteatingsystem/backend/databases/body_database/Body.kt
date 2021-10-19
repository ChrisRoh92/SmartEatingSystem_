package de.rohnert.smarteatingsystem.backend.databases.body_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bodies")
data class Body(@PrimaryKey @ColumnInfo(name ="date") var date:Long,
                @ColumnInfo(name = "weight") var weight:Float,
                @ColumnInfo(name = "kfa") var kfa:Float,
                @ColumnInfo(name = "bauch") var bauch:Float,
                @ColumnInfo(name = "brust") var brust:Float,
                @ColumnInfo(name = "hals") var hals:Float,
                @ColumnInfo(name = "huefte") var huefte:Float,
                @ColumnInfo(name = "fotoDir") var fotoDir:String)