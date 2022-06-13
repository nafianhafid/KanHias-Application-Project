package com.example.myikanhiascapstone.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.myikanhiascapstone.api.Api
import com.example.myikanhiascapstone.data.Result

class Repository {
    
    companion object {
        private const val TAG = "Repository"
    }
    private lateinit var apiService: Api

    fun setApiService(apiService: Api) {
        this.apiService = apiService
    }

    fun getDataFish(name: String): LiveData<Result<FishResponse>> = liveData{
        emit(com.example.myikanhiascapstone.data.Result.Loading)
        try {
            val response = apiService.getDataFish(name)
            emit(Result.Success(response))
            Log.d(TAG, "getFishList: SUCCESS")
        }catch (e: Exception){
            emit(Result.Error("getFoodList() request error"))
            Log.d(TAG, "getFishList: EXCEPTION ERROR: ${e.cause}")
        }
    }
}