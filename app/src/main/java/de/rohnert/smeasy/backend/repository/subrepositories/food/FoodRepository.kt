package com.example.roomdatabaseexample.backend.repository.subrepositories.food

import android.app.Application
import android.os.AsyncTask
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.roomdatabaseexample.backend.databases.food_database.Food
import com.example.roomdatabaseexample.backend.databases.food_database.appfood_database.AppFoodDao
import com.example.roomdatabaseexample.backend.databases.food_database.appfood_database.AppFoodDataBaseProvider
import com.example.roomdatabaseexample.backend.databases.food_database.userfood_database.UserFoodDao
import com.example.roomdatabaseexample.backend.databases.food_database.userfood_database.UserFoodDataBaseProvider


class FoodRepository(var application: Application)
{
    private var appFoodDao:AppFoodDao
    private var appFoodList:List<Food>

    private var userFoodDao:UserFoodDao
    private lateinit var userFoodList:List<Food>

    private var foodProcessor:FoodProcessor



    init {
        var appDB = AppFoodDataBaseProvider.getDatabase(application)
        appFoodDao = appDB.appFoodDao()
        appFoodList = appFoodDao.getAppFoodData()

        var userDB = UserFoodDataBaseProvider.getDatabase(application)
        userFoodDao = userDB.userFoodDao()
        Thread(Runnable {
            userFoodList = userFoodDao.getUserFoodData()
        }).start()



        foodProcessor = FoodProcessor(application)

    }


    /////////////////////////////////////////////////////
    // Methoden für komplette FoodList:
    fun getFoodList():ArrayList<Food>
    {

        var export:ArrayList<Food> = ArrayList(appFoodDao.getAllData())
        /*var t = Thread(Runnable {

        })
        t.start()
        t.join()*/

        export.addAll(ArrayList(userFoodList))

        return export
    }

    fun getLiveFoodList():LiveData<ArrayList<Food>>
    {
        var export:MutableLiveData<ArrayList<Food>> = MutableLiveData()
        export.value = (getFoodList())
        return export
    }

    // Nach Categorie gefilterte FoodList
    fun getCategoryFilteredFoodList(filterItems:ArrayList<String>):LiveData<ArrayList<Food>>
    {
        var export:MutableLiveData<ArrayList<Food>> = MutableLiveData()
        var foods:ArrayList<Food> = getFoodList()
        var values:ArrayList<Food> = ArrayList()
        var usedIndex:ArrayList<Int> = ArrayList()
        Log.d("Smeasy","FoodRepository - getCategoryFilteredFoodList - started...")
        var t = Thread(Runnable {
            for(i in filterItems)
            {
                for((index,j) in foods.withIndex())
                {
                    if(!usedIndex.contains(index) && j.category == i)
                    {
                        usedIndex.add(index)
                        values.add(j)
                    }
                }
            }
       })

        t.start()
        t.join()



        export.value = values
        Log.d("Smeasy","FoodRepository - getCategoryFilteredFoodList - finished...")




        return export


    }

    private fun filterListWithFilterItems(mFoods:ArrayList<Food>,filterItems: ArrayList<String>):ArrayList<Food>
    {
        var export:ArrayList<Food> = ArrayList()
        for(i in mFoods)
        {
            for(j in filterItems)
            {
                if(j ==  i.category)
                {
                    export.add(i)
                }
            }

        }
        return export
    }

    // Liste mit Suchbegriffen....
    fun getSearchFilterFoodList(item:String,filterItems:ArrayList<String>):ArrayList<Food>
    {
        //var export:MutableLiveData<ArrayList<Food>> = MutableLiveData()
        var foods:ArrayList<Food> = getFoodList()
        var values:ArrayList<Food> = ArrayList()
        /*var t = Thread(Runnable {

        })
        t.start()
        t.join()*/

        if(item != "")
        {
            Log.d("Smeasy","FoodRepository - getSearchFilterFoodList() - index == notempty")
            var usedIndex:ArrayList<Int> = ArrayList()
            for((index,i) in foods.withIndex())
            {
                // if(!usedIndex.contains(index) && i.name.toLowerCase().contains(item.toLowerCase()) || i.category.toLowerCase().contains(item.toLowerCase()))
                if(i.name.toLowerCase().contains(item.toLowerCase()) || i.category.toLowerCase().contains(item.toLowerCase()))
                {
                    usedIndex.add(index)
                    values.add(i)
                }
            }
            values = filterListWithFilterItems(values,filterItems)
        }
        else
        {
            Log.d("Smeasy","FoodRepository - getSearchFilterFoodList() - index == empty")
            values = filterListWithFilterItems(foods,filterItems)

        }






        return values
    }

    // Food Kategorien:
    fun getFoodCateogries():ArrayList<String>
    {
        var foods:ArrayList<Food> = ArrayList(getAppFoodList()!!)
        foods.addAll(ArrayList(getUserFoodList()))
        var export:ArrayList<String> = ArrayList()
        for(i in foods)
        {
            if(!export.contains(i.category))
            {
                export.add(i.category)
            }

        }

        return export
    }



    /////////////////////////////////////////////////////
    // Methoden für die AppFoodList()
    fun getAppFoodList():List<Food>
    {
        return appFoodList
    }
    fun getAppFoodById(id:String):Food?
    {
        return appFoodDao.getDataById(id)
    }




    /////////////////////////////////////////////////////
    // Methoden für die UserFoodList()
    fun getUserFoodList():List<Food>
    {
        return userFoodList
    }
    fun getUserFoodById(id:String):Food?
    {
        return userFoodDao.getDataById(id)
    }

    fun insertNewUserFood(food:Food)
    {
        InsertUserFoodAsyncTask(
            userFoodDao
        ).execute(food)
    }

    fun updateUserFood(food:Food)
    {

        userFoodDao.update(food)
    }

    /////////////////////////////////////////////////////
    // Methode für das erstmalige aufrufen der App...
    fun insertCSVFoodList(list:List<Food>)
    {
        InsertFoodListAsyncTask(
            appFoodDao
        ).execute(list)
    }

    /////////////////////////////////////////////////////
    // AsyncTasks...
    class InsertFoodListAsyncTask(private val appFoodDao: AppFoodDao) : AsyncTask<List<Food>, Void, Void>()
    {

        override fun doInBackground(vararg food: List<Food>): Void? {
            appFoodDao.insertAll(food[0])
            return null
        }
    }
    class InsertUserFoodAsyncTask(private val userFoodDao: UserFoodDao) : AsyncTask<Food, Void, Void>()
    {

        override fun doInBackground(vararg food: Food): Void? {
            userFoodDao.insert(food[0])
            return null
        }
    }




    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Testing Methods..
    fun getNumberOfAppFoods():Int
    {
        return appFoodDao.getNumberOfEntries()
    }

}