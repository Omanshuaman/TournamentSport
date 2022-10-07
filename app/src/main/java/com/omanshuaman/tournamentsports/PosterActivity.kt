package com.omanshuaman.tournamentsports

import android.graphics.*
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException


class PosterActivity : AppCompatActivity() {
    var imageView: ImageView? = null
    private var storageReference: StorageReference? = null
    private var storageReference1: StorageReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster)
        imageView = findViewById(R.id.secondActivity)
        val intent = intent
        val str = intent.getStringExtra("message_key")
        val str2 = intent.getStringExtra("message_key1")
        val resources = this@PosterActivity.resources
        val scale = resources.displayMetrics.density
        var bitmap2: Bitmap = BitmapHelper.getInstance().bitmap
        var bitmapConfig = bitmap2.config
        if (bitmapConfig == null) {
            bitmapConfig = Bitmap.Config.ARGB_8888
        }
        bitmap2 = bitmap2.copy(bitmapConfig, true)

        //   Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();
        val canvas = Canvas(bitmap2)

        val textPaint7 = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint7.textAlign = Paint.Align.CENTER
        textPaint7.typeface = ResourcesCompat.getFont(this, R.font.ralewaythin)
        textPaint7.color = Color.rgb(6, 94, 105)
        setTextSizeForWidth(textPaint7, 350f, "FAITHHHHHHHHHHHH")
        val xPos7 = canvas.width / 2
        val yPos7 = 800

//        imageView.setImageBitmap(overlay(bitmap2, myLogo));
        storageReference = FirebaseStorage.getInstance().getReference("images/" + "Preto" + ".png")
        storageReference1 =
            FirebaseStorage.getInstance().getReference("images/" + "avenger" + ".jpeg")
        try {
            val localFile = File.createTempFile("tempFile", ".png")
            storageReference!!.getFile(localFile)
                .addOnSuccessListener { taskSnapshot: FileDownloadTask.TaskSnapshot? ->
                    var bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    var bitmapConfig1 = bitmap.config
                    if (bitmapConfig1 == null) {
                        bitmapConfig1 = Bitmap.Config.ARGB_8888
                    }
                    bitmap = bitmap.copy(bitmapConfig1, true)
                    val canvas1 = Canvas(bitmap)
                    canvas1.drawText(
                        "FAITHHHHHHHHHHHH",
                        xPos7.toFloat(),
                        yPos7.toFloat(),
                        textPaint7
                    )
                    try {
                        val localFile1 = File.createTempFile("tempFile1", ".png")
                        val finalBitmap = bitmap
                        storageReference1!!.getFile(localFile1)
                            .addOnSuccessListener { taskSnapshot1: FileDownloadTask.TaskSnapshot? ->
                                val myLogo =
                                    BitmapFactory.decodeFile(localFile1.absolutePath)
                                imageView!!.setImageBitmap(overlay(finalBitmap, myLogo))
                            }.addOnFailureListener { e: Exception? ->
                                Toast.makeText(
                                    this@PosterActivity,
                                    "No",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }.addOnFailureListener { e: Exception? ->
                    Toast.makeText(
                        this@PosterActivity,
                        "No",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun overlay(bmp1: Bitmap, bmp2: Bitmap): Bitmap {
        val bmOverlay = Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
        val canvas = Canvas(bmOverlay)
        canvas.drawBitmap(bmp1, Matrix(), null)
        canvas.drawBitmap(bmp2, 100f, 200f, null)
        return bmOverlay
    }

    companion object {
        private fun setTextSizeForWidth(
            paint: Paint, desiredWidth: Float,
            text: String
        ) {

            val testTextSize = 48f

            // Get the bounds of the text, using our testTextSize.
            //  paint.setTextSize(testTextSize);
            val bounds = Rect()
            paint.getTextBounds(text, 0, text.length, bounds)

            // Calculate the desired size as a proportion of our testTextSize.
            val desiredTextSize = testTextSize * desiredWidth / bounds.width()

            // Set the paint for that size.
            paint.textSize = desiredTextSize
        }
    }
}
