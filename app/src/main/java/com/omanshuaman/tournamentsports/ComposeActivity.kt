package com.omanshuaman.tournamentsports

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import com.google.firebase.auth.FirebaseAuth
import com.omanshuaman.tournamentsports.details.DetailsContent
import com.omanshuaman.tournamentsports.details.DetailsViewModel
import com.omanshuaman.tournamentsports.models.Upload
import com.omanshuaman.tournamentsports.ui.theme.TournamentSportsTheme
import com.omanshuaman.tournamentsports.util.PaletteGenerator.convertImageUrlToBitmap
import com.omanshuaman.tournamentsports.util.PaletteGenerator.extractColorsFromBitmap


@ExperimentalCoilApi
@ExperimentalMaterialApi

class ComposeActivity : ComponentActivity() {


    private var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            firebaseAuth = FirebaseAuth.getInstance()

            TournamentSportsTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    DetailsScreen()

                }

            }
        }
    }

    @Composable
    fun DetailsScreen(
        detailsViewModel: DetailsViewModel = hiltViewModel()
    ) {

        val intent = intent
        val extras = intent.extras
        val id = extras!!.getString("tournamentId")
        val entryFee = extras.getString("entryFee")
        val prizeFee = extras.getString("prizeFee")
        val address = extras.getString("address")
        val backgroundImage = extras.getString("backgroundImage")
        val tournamentName = extras.getString("tournamentName")
        val matchDate = extras.getString("matchDate")


        val colorPalette by detailsViewModel.colorPalette

        if (colorPalette.isNotEmpty()) {

            DetailsContent(
                selectedHero = Upload(
                    Id = id,
                    tournamentName = tournamentName,
                    matchDate = matchDate,
                    registerDate = "dvdvd",
                    requirement = "vdf",
                    sports = "rdf",
                    address = address,
                    imageUrl = backgroundImage,
                    longitude = "String",
                    latitude = "String",
                    SportsType = "String",
                    entryFee = entryFee,
                    prizeMoney = prizeFee,
                    uid = "String"
                ), colors = colorPalette
            )
        } else {
            detailsViewModel.generateColorPalette()

        }

        val context = LocalContext.current

        LaunchedEffect(key1 = true) {

            val bitmap = convertImageUrlToBitmap(
                imageUrl = backgroundImage!!,

                //  imageUrl = "$BASE_URL/images/sasuke.jpg",
                context = context
            )

            if (bitmap != null) {
                detailsViewModel.setColorPalette(
                    colors = extractColorsFromBitmap(bitmap = bitmap)
                )

            }
        }

    }
}


