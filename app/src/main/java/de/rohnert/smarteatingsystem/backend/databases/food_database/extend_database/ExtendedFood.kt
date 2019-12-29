package de.rohnert.smarteatingsystem.backend.databases.food_database.extend_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "extend_food")
class ExtendedFood(
    @PrimaryKey @ColumnInfo(name ="id") var id: String,
    @ColumnInfo(name = "category") var category:String,
    @ColumnInfo(name = "name") var name:String,
    @ColumnInfo(name = "marken") var marken:String,
    @ColumnInfo(name = "menge") var menge:String,
    @ColumnInfo(name = "unit") var unit:String,
    @ColumnInfo(name = "ean") var ean:String,
    @ColumnInfo(name = "kcal") var kcal:Float,
    @ColumnInfo(name = "carb") var carb:Float,
    @ColumnInfo(name = "protein") var protein:Float,
    @ColumnInfo(name = "fett") var fett:Float
)
