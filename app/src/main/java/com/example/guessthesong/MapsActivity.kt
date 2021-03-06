package com.example.guessthesong

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val PERMISSION_ID = 42
    private var mCurrLocationMarker: Marker? = null
    private lateinit var coordinatesNow: LatLng
    private lateinit var lyricCircle: Circle
    private lateinit var lyricsMarker: Marker
    private val southWest = LatLng(51.617860, -3.885071)
    private val northEast = LatLng(51.620361, -3.875546)

    /**
     * Called when the activity is called in the foreground. Sets up the view, the listener
     * for the lyrics button and the Fused Location Client we are using for location.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        lyricsButton.setOnClickListener {
            val intent = Intent(applicationContext, LyricsActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     *
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * Sets map properties and calls for last location and location updates.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = MAP_TYPE_NORMAL
        //  mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, android.R.))
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.setMaxZoomPreference(21f)
        mMap.setMinZoomPreference(16f)
        val bounds = LatLngBounds(southWest, northEast)
        mMap.setLatLngBoundsForCameraTarget(bounds)
        getLastKnownLocation()
        requestNewLocation()
        generateNewMarker()
    }

    /**
     * Stops the location updates when the activity is not in the foreground to save system resources.
     */
//    override fun onPause() {
//        super.onPause()
//        if (mFusedLocationClient != null) {
//            mFusedLocationClient.removeLocationUpdates(mLocationCallBack)
//        }
//    }

    /**
     * Whenever the activity is called back in the foreground the location updates are restarted.
     */
//    override fun onResume() {
//        super.onResume()
//        requestNewLocation()
//    }

    /**
     * Gets the lst location of the user and stores the coordinates. Checks if the necessary permissions
     * are allowed and the necessary sensors are available.
     */
    private fun getLastKnownLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocation()
                    } else {
                        var lat = location.latitude
                        var long = location.longitude
                        val locationCoorditnates = LatLng(lat, long)
                        val zoomLevel = 10f
                        mCurrLocationMarker =
                            mMap.addMarker(
                                MarkerOptions().position(locationCoorditnates).title("Your location")
                            )
                        mMap!!.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                locationCoorditnates,
                                zoomLevel
                            )
                        )

                    }
                }
            } else {
                Toast.makeText(this, "Enable Location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    /**
     * Request a location update. Sets the priority of the request and how often a requeste is made.
     */
    private fun requestNewLocation() {
        var mLocationRequest = LocationRequest()

        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 2000
        mLocationRequest.fastestInterval = 1000

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest,
            mLocationCallBack,
            Looper.myLooper()
        )
    }

    /**
     * Updates the location marker at the set interval. At every marker update it
     * checks whether the user is within the designated radius around a lyric.
     */
    private val mLocationCallBack = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

            val locationList =
                locationResult.locations
            if (locationList.size > 0) {
                val location = locationList[locationList.size - 1]
                var mLastLocation = location
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker!!.remove()
                }

                var lat = mLastLocation.latitude
                var long = mLastLocation.longitude

                val lastCoordinates = LatLng(lat, long)
                coordinatesNow = lastCoordinates
                val markerOptions = MarkerOptions().position(lastCoordinates).title("Your Location")
                    .icon(
                        bitmapDescriptorFromVector(
                            applicationContext,
                            R.drawable.ic_person_pin_black_24dp
                        )
                    )
                mCurrLocationMarker = mMap.addMarker(markerOptions)
                mCurrLocationMarker
                mMap!!.animateCamera(CameraUpdateFactory.newLatLng(lastCoordinates))
                checkInRadius()
            }
        }
    }

    /**
     * Checks if the phones location is on.
     * @return true if it is enabled false otherwise
     */
    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    /**
     * Checks if the necessary permissions have been given. If they have been returns true otherwise
     * requests them.
     * @return true if all permissions are available false otherwise
     */
    private fun checkPermissions(): Boolean {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    /**
     * Requests permissions if some are lacking.
     */
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    /**
     * Called when permissions are granted. Used to check last known location and request a new one.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastKnownLocation()
            }
        }
    }

    /**
     * Used to calculate the distance between the users location and to check if the user is
     * within the radius of the lyric. If they are within the radius the lyric is added to the list
     * of collected lyrics, a message is displayed and a new lyric is generated.
     */
    private fun checkInRadius() {
        val distance = FloatArray(2)
        Location.distanceBetween(
            coordinatesNow.latitude,
            coordinatesNow.longitude,
            lyricCircle.center.latitude,
            lyricCircle.center.longitude,
            distance
        )
        //distance.forEach { println("Distance" + it) }
        //println(distance[0] < lyricCircle.radius)
        if (distance[0] < lyricCircle.radius) {
            //Toast.makeText(this, "You got this one", Toast.LENGTH_LONG).show()
            lyricsMarker.remove()
            lyricCircle.remove()
            val intent = Intent(applicationContext, PopUpActivity::class.java)
            val congratulations = resources.getString(R.string.new_lyric_found)

            if (MainMenuActivity.getMode()) {  // Current
                val nextLineCurrent = FileReaderObject.nextLineCurrent()
                LyricsActivity.addModernLyric(nextLineCurrent)
                intent.putExtra("STRING", congratulations + nextLineCurrent)
                startActivity(intent)
                LyricsActivity.additionalLyricModern()
                if (LyricsActivity.getModernSongPoints() <= 0){
                    LyricsActivity.skipSong(applicationContext, LyricsActivity())
                }
            } else {    // Classic
                val nextLineClassic = FileReaderObject.nextLineClassic()
                LyricsActivity.addClassicLyric(nextLineClassic)
                intent.putExtra("STRING", congratulations + nextLineClassic)
                startActivity(intent)
                LyricsActivity.additionalLyricClassic()
                if (LyricsActivity.getClassicSongPoints() <= 0){
                    LyricsActivity.skipSong(applicationContext, LyricsActivity())
                }
            }
            generateNewMarker()
        }
    }

    /**
     * Generates a new lyric at a random location in the designated area.
     */
    private fun generateNewMarker() {

        val lngSpan = northEast.longitude - southWest.longitude
        val latSpan = northEast.latitude - southWest.latitude
        val randomMarker = LatLng(
            southWest.latitude + latSpan * Math.random(),
            southWest.longitude + lngSpan * Math.random()
        )
        lyricsMarker = mMap.addMarker(
            MarkerOptions().position(randomMarker).title("Lyric").icon(
                BitmapDescriptorFactory.fromResource(
                    R.drawable.ic_audiotrack_light
                )
            )
        )
        lyricCircle =
            mMap.addCircle(CircleOptions().center(randomMarker).radius(15.0).strokeColor(Color.RED))
        println(randomMarker)
    }

    /**
     * Converts a vector image to bitmap.
     * Source: StackOverflow
     */
    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap =
                Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }
}