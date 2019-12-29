package de.rohnert.smarteatingsystem.backend.databases.daily_database



import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.collections.ArrayList


@Entity(tableName = "dailies")
data class Daily(@PrimaryKey @ColumnInfo(name ="date") var date:String,
                 @ColumnInfo(name = "breakfast") var breakfastEntry:ArrayList<MealEntry>?,
                 @ColumnInfo(name = "lunch") var lunchEntry:ArrayList<MealEntry>?,
                 @ColumnInfo(name = "dinner") var dinnerEntry:ArrayList<MealEntry>?,
                 @ColumnInfo(name = "snacks") var snackEntry:ArrayList<MealEntry>?,
                 @ColumnInfo(name = "maxKcal") var maxKcal:Float,
                 @ColumnInfo(name = "maxCarb") var maxCarb:Float,
                 @ColumnInfo(name = "maxProtein") var maxProtein:Float,
                 @ColumnInfo(name = "maxFett") var maxFett:Float)