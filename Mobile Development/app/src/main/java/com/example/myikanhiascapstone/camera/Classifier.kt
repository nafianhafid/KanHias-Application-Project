package com.example.myikanhiascapstone.camera

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.example.myikanhiascapstone.ml.ModelSmallWithMetadata
import org.tensorflow.lite.support.image.TensorImage


object Classifier {

    fun classifyImage(context: Context, image: Bitmap): String {
        val model = ModelSmallWithMetadata.newInstance(context)
        val tensorImage = TensorImage.fromBitmap(image)

        // Runs model inference and gets result.
        val outputs = model.process(tensorImage)
        val detectionResult = outputs.probabilityAsCategoryList
        detectionResult.sortWith(compareByDescending { it.score })
        for(i in detectionResult){
            Log.d("DETECT", "classifyImage: DETECT: ${i.label} Score: ${i.score} Display Name: ${i.displayName}")
        }


        // Gets result from DetectionResult.
        val kind = detectionResult[0].label
        Log.d("classifier:", " classifyImage: JENIS: " + kind)
        model.close()
        return kind
    }
}