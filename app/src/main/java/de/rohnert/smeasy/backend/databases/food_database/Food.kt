package com.example.roomdatabaseexample.backend.databases.food_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "food")
data class Food(@PrimaryKey @ColumnInfo(name ="id") var id: String,
@ColumnInfo(name = "category") var category:String,
@ColumnInfo(name = "name") var name:String,
@ColumnInfo(name = "unit") var unit:String,
@ColumnInfo(name = "kcal") var kcal:Float,
@ColumnInfo(name = "carb") var carb:Float,
@ColumnInfo(name = "protein") var protein:Float,
@ColumnInfo(name = "fett") var fett:Float,
@ColumnInfo(name = "ean") var ean:String
)