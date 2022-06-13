package com.example.myikanhiascapstone.detail

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myikanhiascapstone.R
import com.example.myikanhiascapstone.api.ApiConfig
import com.example.myikanhiascapstone.data.Repository
import com.example.myikanhiascapstone.databinding.ActivityDetailResultBinding
import com.example.myikanhiascapstone.databinding.ActivityMainBinding
import com.example.myikanhiascapstone.data.Result

class DetailResultActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "DetailResultActivity"
    }

    private lateinit var binding: ActivityDetailResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val x = intent.getStringExtra("result")
        val y = intent.getStringExtra("capture")
        val uri = Uri.parse(y)
        binding.ivCapture.setImageURI(uri)
        binding.tvName.setText(x)

        val repository = Repository()
        repository.setApiService(ApiConfig.getApiService())
        repository.getDataFish("${x}").observe(this){
            when(it){
                is Result.Error -> {
                    Log.d(TAG, "onCreate: error")
                }
                is Result.Loading -> {
                    Log.d(TAG, "onCreate: loading")
                }
                is Result.Success -> {
                    Log.d(TAG, "onCreate: succes ${it.data.data?.size}")
                    val data = it?.data?.data?.get(0)?.attributes
                    Log.d(TAG, "onCreate: ${data?.name}")
                    Log.d(TAG, "onCreate: ${data?.createdAt}")
                    Log.d(TAG, "onCreate: ${data?.desciption}")
                    Log.d(TAG, "onCreate: ${data?.publishedAt}")
                    binding.tvName.text = data?.name.toString()
                    binding.tvDescription.text = data?.desciption.toString()

                }
            }
        }
    }
}