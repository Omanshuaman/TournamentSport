package com.omanshuaman.tournamentsports

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import com.adevinta.leku.locale.SearchZoneRect
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.omanshuaman.tournamentsports.models.LocationModel
import com.omanshuaman.tournamentsports.models.Upload
import java.util.*

@Suppress("DEPRECATION")


class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var mAuth: FirebaseAuth? = null
    private var uploadBtn: FloatingActionButton? = null
    private var imageView: ImageView? = null
    private var progressBar: ProgressBar? = null
    private var mtournamentsportsname: EditText? = null
    private var mPrizeMoney: EditText? = null
    private var mEntryFee: EditText? = null
    private var progressDialog: ProgressDialog? = null
    private var mbtnPicklocation: Button? = null
    private var tvMylocation: TextView? = null
    private val PLACE_PICKER_REQUEST2 = 999
    private var spinner: Spinner? = null
    private var item: String? = null
    private var planets = arrayOf("Choose Sports", "Football", "Cricket", "Athletics", "Badminton")
    private var textView: TextView? = null
    private var latitude: String? = null
    private var longitude: String? = null
    private var geocoder: Geocoder? = null
    private var addresses: List<Address>? = null
    private val userid = FirebaseAuth.getInstance().currentUser!!.uid
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Tournament")
    private val storageReference =
        FirebaseStorage.getInstance().reference.child("Photos").child(userid)
    private var imageUri: Uri? = null
    val gTimestamp = "" + System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        uploadBtn = findViewById(R.id.upload_btn)
        progressBar = findViewById(R.id.progressBar)
        imageView = findViewById(R.id.imageView)
        mtournamentsportsname = findViewById(R.id.tournament_name)
        mbtnPicklocation = findViewById(R.id.BtnPickLocation)
        textView = findViewById(R.id.select_sports)
        spinner = findViewById(R.id.spinner)
        mEntryFee = findViewById(R.id.entry_fee)
        mPrizeMoney = findViewById(R.id.prize_money)
        spinner!!.onItemSelectedListener = this

        val spinnerArrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            planets
        )
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner!!.adapter = spinnerArrayAdapter
        geocoder = Geocoder(this, Locale.getDefault())

        progressBar!!.visibility = View.INVISIBLE
        mAuth = FirebaseAuth.getInstance()

        imageView!!.setOnClickListener {
            val galleryIntent = Intent()
            galleryIntent.action = Intent.ACTION_GET_CONTENT
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, 2)
        }

        mbtnPicklocation!!.setOnClickListener {
            openPlacePicker()
        }

        uploadBtn!!.setOnClickListener {
            if (imageUri != null) {
                uploadToFirebase(imageUri!!, item!!)
                val intent = Intent(this, GroupCreateActivity::class.java)
                intent.putExtra("Id", gTimestamp)
                startActivity(intent)
            } else {
                Toast.makeText(this@MainActivity, "Please Select Image", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun openPlacePicker() {

        val ai: ApplicationInfo = applicationContext.packageManager
            .getApplicationInfo(applicationContext.packageName, PackageManager.GET_META_DATA)
        val value = ai.metaData["com.google.android.geo.API_KEY"]
        val key = value.toString()

        val locationPickerIntent = LocationPickerActivity.Builder()
            .withLocation(41.4036299, 2.1743558)
            .withGeolocApiKey(key)
            .withSearchZone("es_ES")
            .withSearchZone(
                SearchZoneRect(
                    LatLng(26.525467, -18.910366),
                    LatLng(43.906271, 5.394197)
                )
            )
            .withDefaultLocaleSearchZone()
            .shouldReturnOkOnBackPressed()
            .withStreetHidden()
            .withCityHidden()
            .withZipCodeHidden()
            .withSatelliteViewHidden()
            //.withGooglePlacesEnabled()
            .withGooglePlacesApiKey(key)
            .withGoogleTimeZoneEnabled()
            .withVoiceSearchHidden()
            .withUnnamedRoadHidden()
            //   .withMapStyle()
            // .withSearchBarHidden()
            .build(applicationContext)

        startActivityForResult(locationPickerIntent, PLACE_PICKER_REQUEST2)

//        val intent = PlacePicker.IntentBuilder()
//            .setLatLong(
//                40.748672,
//                -73.985628
//            )  // Initial Latitude and Longitude the Map will load into
//            .showLatLong(true)  // Show Coordinates in the Activity
//            .setMapZoom(12.0f)  // Map Zoom Level. Default: 14.0
//            .setAddressRequired(true) // Set If return only Coordinates if cannot fetch Address for the coordinates. Default: True
//            .hideMarkerShadow(true) // Hides the shadow under the map marker. Default: False
//            .setMarkerDrawable(R.drawable.ic_profile_black) // Change the default Marker Image
//            .setMarkerImageImageColor(R.color.colorPrimary)
//            .setFabColor(R.color.black)
//            .setPrimaryTextColor(R.color.colorPrimary) // Change text color of Shortened Address
//            .setSecondaryTextColor(R.color.colorPrimary) // Change text color of full Address
//            .setBottomViewColor(R.color.colorBlack) // Change Address View Background Color (Default: White)
//            .setMapRawResourceStyle(R.raw.map_style)  //Set Map Style (https://mapstyle.withgoogle.com/)
//            .setMapType(MapType.NORMAL)
//            .setPlaceSearchBar(
//                false,
//                MAPS_API_KEY
//            ) //Activate GooglePlace Search Bar. Default is false/not activated. SearchBar is a chargeable feature by Google
//            .onlyCoordinates(false)  //Get only Coordinates from Place Picker
//            .hideLocationButton(false)   //Hide Location Button (Default: false)
//            .disableMarkerAnimation(false) //Disable Marker Animation (Default: false)
//            .build(this)
//        startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            imageUri = data.data
            imageView!!.setImageURI(imageUri)
        } else
            if (resultCode == Activity.RESULT_OK && data != null) {
                Log.d("RESULT****", "OK")
                if (requestCode == PLACE_PICKER_REQUEST2) {

                    latitude = data.getDoubleExtra(LATITUDE, 0.0).toString()
                    Log.d("LATITUDE****", latitude.toString())
                    longitude = data.getDoubleExtra(LONGITUDE, 0.0).toString()
                    Log.d("LONGITUDE****", longitude.toString())

                    addresses =
                        geocoder?.getFromLocation(
                            latitude!!.toDouble(),
                            longitude!!.toDouble(),
                            1
                        ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    Log.d("address****", addresses.toString())

                    val sb = StringBuilder()
                    sb.append("LATITUDE:").append(latitude).append("\n").append("LONGITUDE: ")
                        .append(longitude)
                    tvMylocation?.text = sb.toString()
                }
            }
//            if (resultCode == Activity.RESULT_OK && data != null) {
//                Log.d("RESULT****", "OK")
//                if (requestCode == Constants.PLACE_PICKER_REQUEST) {
//                    val addressData =
//                        data.getParcelableExtra<AddressData>(Constants.ADDRESS_INTENT)
//                    Log.d("TAGm", "onActivityResult: "+addressData)
//                } else {
//                    super.onActivityResult(requestCode, resultCode, data)
//                }
//
//            }
    }

    private fun uploadToFirebase(uri: Uri, item: String) {
        progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Uploading")

        val fileRef =
            storageReference.child(
                System.currentTimeMillis()
                    .toString() + "." + getFileExtension(uri)
            )
        progressDialog!!.show()

        fileRef.putFile(uri).addOnSuccessListener {
            fileRef.downloadUrl.addOnSuccessListener { uri1: Uri ->
                if (item == "Choose planets") {
                    Toast.makeText(this, "pla", Toast.LENGTH_SHORT).show()
                } else {
                    val model = Upload(
                        gTimestamp,
                        mtournamentsportsname!!.text.toString(),
                        uri1.toString(),
                        longitude,
                        latitude, item, mEntryFee?.text.toString(), mPrizeMoney?.text.toString(),userid
                    )
                    val locationModel = LocationModel(longitude, latitude)

                    databaseReference.child("Just Photos").child(gTimestamp).setValue(model)
                    databaseReference.child("Location").child(gTimestamp).child("LatLng")
                        .setValue(locationModel)

                    // progressBar!!.visibility = View.INVISIBLE
                    Toast.makeText(this@MainActivity, "Uploaded Successfully", Toast.LENGTH_SHORT)
                        .show()
                    progressDialog!!.dismiss()

                }
            }

        }.addOnProgressListener { progressBar!!.visibility = View.VISIBLE }.addOnFailureListener {
            progressBar!!.visibility = View.INVISIBLE
            Toast.makeText(this@MainActivity, "Uploading Failed !!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFileExtension(mUri: Uri): String? {
        val cr = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cr.getType(mUri))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        item = spinner?.selectedItem.toString()
        textView?.text = item
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }
}