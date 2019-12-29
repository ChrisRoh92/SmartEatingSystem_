package de.rohnert.smarteatingsystem.backend.databases.food_database.normal_database.favourite_foods

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favFood")
data class FavFood(@PrimaryKey @ColumnInfo(name ="foodID") var id: String,
                   @ColumnInfo(name = "name") var name:String)
