package com.example.guessthesong

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    val PERMISSION_ID = 42
    private var mCurrLocationMarker: Marker? = null
    private lateinit var coordinatesNow: LatLng
    val testingCircle = CircleOptions().center(
        LatLng(
            51.618381,
            -3.878355
        )
    ).radius(15.0).strokeColor(Color.RED)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastKnownLocation()
        requestNewLocation()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.mapType = MAP_TYPE_NORMAL
        mMap.uiSettings.isZoomControlsEnabled = false
        mMap.setMaxZoomPreference(18f)
        mMap.setMinZoomPreference(15f)
        mMap.addCircle(testingCircle)
        mMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    51.618381, -3.878355
                )
            ).title("Great Hall marker").draggable(false).icon(
                BitmapDescriptorFactory.fromResource(
                    R.drawable.ic_audiotrack_light
                )
            )
        )
        lyricsButton.setOnClickListener { view ->
            val intent = Intent(applicationContext, LyricsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onPause() {
        super.onPause()

        if (mFusedLocationClient != null) {
            mFusedLocationClient.removeLocationUpdates(mLocationCallBack)
        }
    }

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

    private val mLocationCallBack = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

            val locationList =
                locationResult.locations
            if (locationList.size > 0) {
                val location = locationList[locationList.size - 1]
                Log.i(
                    "MapsActivity",
                    "Location: " + location.latitude + " " + location.longitude
                )
                var mLastLocation = location
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker!!.remove()
                }

                var lat = mLastLocation.latitude
                var long = mLastLocation.longitude

                val lastCoordinates = LatLng(lat, long)
                coordinatesNow = lastCoordinates
                val markerOptions = MarkerOptions().position(lastCoordinates).title("Your Location")
                mCurrLocationMarker = mMap.addMarker(markerOptions)
                mCurrLocationMarker
                mMap!!.animateCamera(CameraUpdateFactory.newLatLng(lastCoordinates))
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

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

    private fun checkInRadius(): Boolean {
        val distance = FloatArray(2)
        Location.distanceBetween(
            coordinatesNow.latitude,
            coordinatesNow.longitude,
            testingCircle.center.latitude,
            testingCircle.center.longitude,
            distance
        )
        distance.forEach { println("Distance" + it) }

        return distance[0] < testingCircle.radius
    }
}