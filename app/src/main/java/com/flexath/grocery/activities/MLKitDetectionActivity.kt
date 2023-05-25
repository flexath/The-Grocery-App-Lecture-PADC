package com.flexath.grocery.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import com.flexath.grocery.R
import com.flexath.grocery.databinding.ActivityMlkitDetectionBinding
import com.flexath.grocery.utils.loadBitMapFromUri
import com.flexath.grocery.utils.scaleToRatio
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.text.TextRecognition
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.StringBuilder

class MLKitDetectionActivity : BaseActivity() {

    private lateinit var binding: ActivityMlkitDetectionBinding
    private var mChosenImageBitmap: Bitmap? = null

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MLKitDetectionActivity::class.java)
        }
    }

    private val paintForTextBlock = Paint().apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 2.0f
    }

    private val paintForFaces = Paint().apply {
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 9.0f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMlkitDetectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpListeners()
    }

    private fun setUpListeners() {
        binding.btnTakePhoto.setOnClickListener {
            selectImageFromGallery()
        }

        binding.btnFindFace.setOnClickListener {
            detectFaceAndDrawRectangle()
        }

        binding.btnFindText.setOnClickListener {
            detectTextAndUpdateUI()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == INTENT_REQUEST_CODE_SELECT_IMAGE_FROM_GALLERY) {

            val imageUri = data?.data

            imageUri?.let { image ->
                Observable.just(image)
                    .map {
                        it.loadBitMapFromUri(applicationContext)
                    }
                    .map {
                        it.scaleToRatio(0.35)
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        mChosenImageBitmap = it
                        binding.ivImage.setImageBitmap(mChosenImageBitmap)
                    }
            }
        }
    }

    private fun detectTextAndUpdateUI() {
        mChosenImageBitmap?.let {
            val inputImage = InputImage.fromBitmap(it, 0)
            val textRecognizer = TextRecognition.getClient()

            textRecognizer.process(inputImage)
                .addOnSuccessListener { visibleText ->

                    val detectedTexts = StringBuilder("")
                    visibleText.textBlocks.forEach { block ->
                        detectedTexts.append("${block.text}\n")
                    }

                    binding.tvDetectedTexts.text = ""
                    binding.tvDetectedTexts.text = detectedTexts.toString()

                    visibleText.textBlocks.forEach { block ->
                        val canvas = Canvas(it)
                        block.boundingBox?.let { boundingBox ->
                            canvas.drawRect(boundingBox, paintForTextBlock)
                        }
                    }
                }
                .addOnFailureListener { error ->
                    showSnackbar(
                        error.localizedMessage
                            ?: getString(R.string.error_message_cannot_detect_text)
                    )
                }
        }
    }

    private fun detectFaceAndDrawRectangle() {
        mChosenImageBitmap?.let {
            val inputImage = InputImage.fromBitmap(it, 0)
            val detector = FaceDetection.getClient()

            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    drawRectangleOnFace(it, faces)
                    binding.ivImage.setImageBitmap(mChosenImageBitmap)
                }
                .addOnFailureListener { error ->
                    showSnackbar(
                        error.localizedMessage
                            ?: getString(R.string.error_message_cannot_detect_face)
                    )
                }
        }
    }

    private fun drawRectangleOnFace(bitmap: Bitmap, faces: MutableList<Face>) {
        faces.firstOrNull()?.boundingBox?.let { boundingBox ->
            val canvas = Canvas(bitmap)
            canvas.drawRect(boundingBox, paintForFaces)
        }
    }

    override fun showError(error: String) {

    }
}