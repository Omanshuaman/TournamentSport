package com.omanshuaman.tournamentsports

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import com.google.firebase.storage.FileDownloadTask
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.omanshuaman.tournamentsports.models.Upload
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException


class PosterActivity : AppCompatActivity() {
    var imageView: ImageView? = null
    private var storageReference: StorageReference? = null
    private var button: Button? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poster)
        imageView = findViewById(R.id.secondActivity)
        button = findViewById(R.id.storage)

        val intent = intent
        val extras = intent.extras
        val tournamentName = extras!!.getString("tournamentId")

        val textPaint7 = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint7.textAlign = Paint.Align.CENTER
        textPaint7.typeface = ResourcesCompat.getFont(this, R.font.ralewaythin)
        //  textPaint7.color = Color.rgb(6, 94, 105)
        textPaint7.color = Color.rgb(255, 255, 255)

        textPaint7.textSize = 225f

        //   setTextSizeForWidth(textPaint7, 350f, tournamentName!!)
        val xPos7 = 400
        val yPos7 = 800

        val text =
            "TOURNAMENT NAME"
        val text1 =
            "Date:19/09/2022"
        val myTextPaint = TextPaint(TextPaint.ANTI_ALIAS_FLAG)
        myTextPaint.isAntiAlias = true
        //   myTextPaint.textSize = setTextSizeForWidth(myTextPaint, 800f, "TOURNAMENT NAME TOURNAMENT NAME")
        myTextPaint.textSize = 70f
        myTextPaint.typeface = ResourcesCompat.getFont(this, R.font.robotoc)

        myTextPaint.color = Color.rgb(243, 242, 249)
        myTextPaint.textAlign = Paint.Align.CENTER

        val width = 800
        val spacingMultiplier = 1f
        val spacingAddition = 0f
        val includePadding = false

        val builder = StaticLayout.Builder.obtain(text, 0, text.length, myTextPaint, width)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(spacingAddition, spacingMultiplier)
            .setIncludePad(includePadding)
            .setMaxLines(5)

        val myStaticLayout1 = builder.build()

        val builder1 = StaticLayout.Builder.obtain(text1, 0, text.length, myTextPaint, width)
            .setAlignment(Layout.Alignment.ALIGN_CENTER)
            .setLineSpacing(spacingAddition, spacingMultiplier)
            .setIncludePad(includePadding)
            .setMaxLines(5)

        val myStaticLayout2 = builder1.build()
//        imageView.setImageBitmap(overlay(bitmap2, myLogo));

        storageReference =
            FirebaseStorage.getInstance().getReference("Poster/" + "Running" + ".png")

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
                    val xPos2: Int = canvas1.width / 2
                    val yPos2 = 3750

                    canvas1.drawText(
                        text,
                        xPos2.toFloat(),
                        yPos2.toFloat(),
                        textPaint7
                    )
                    myStaticLayout1.draw(canvas1, 2500f, 4500f)
                    myStaticLayout2.draw(canvas1, 500f, 4500f)

                    imageView!!.setImageBitmap(bitmap)
                    button!!.setOnClickListener {
                        uploadFile(getImageUri(this, bitmap))
                        val intent1 = Intent(this, GroupCreateActivity::class.java)
                        startActivity(intent1)
                    }
                }.addOnFailureListener {
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

    private fun uploadFile(imagUri: Uri?) {
        if (imagUri != null) {

            val fileRef =
                storageReference!!.child(
                    System.currentTimeMillis()
                        .toString() + "." + getFileExtension(imagUri)
                )

            fileRef.putFile(imagUri).addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri1: Uri ->

                    Toast.makeText(
                        this@PosterActivity,
                        "Uploaded Successfully",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

            }
        }
    }

    private fun getFileExtension(mUri: Uri): String? {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(mUri))
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            inContext.contentResolver,
            inImage,
            "Title",
            null
        )
        return Uri.parse(path)
    }

    private fun StaticLayout.draw(canvas: Canvas, x: Float, y: Float) {
        canvas.withTranslation(x, y) {
            draw(this)
        }
    }

}

private fun overlay(bmp1: Bitmap, bmp2: Bitmap): Bitmap {
    val bmOverlay = Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
    val canvas = Canvas(bmOverlay)
    canvas.drawBitmap(bmp1, Matrix(), null)
    canvas.drawBitmap(bmp2, 100f, 200f, null)
    return bmOverlay
}


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


