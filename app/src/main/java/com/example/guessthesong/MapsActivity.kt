package com.example.guessthesong

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocatiionClient: FusedLocationProviderClient
    val PERMISSION_ID = 42
    private var mCurrLocationMarker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocatiionClient = LocationServices.getFusedLocationProviderClient(this)
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
        mMap.mapType = MAP_TYPE_HYBRID
        mMap.uiSettings.isZoomControlsEnabled = true


    }

    override fun onPause() {
        super.onPause()

        if (mFusedLocatiionClient != null) {
            mFusedLocatiionClient.removeLocationUpdates(mLocationCallBack)
        }
    }

    private fun getLastKnownLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocatiionClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocation()
                    } else {
                        var lat = location.latitude
                        var long = location.longitude

                        var accuracy = location.accuracy

                        val locationCoorditnates = LatLng(lat, long)
                        val zoomLevel = 10f

                        mCurrLocationMarker = mMap.addMarker(MarkerOptions().position(locationCoorditnates).title("Your location"))
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

        mFusedLocatiionClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocatiionClient!!.requestLocationUpdates(
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
                mMap.setOnMarkerClickListener{marker ->
                    clickOnMarker(marker)
                    true
                }
                val lastCoordinates = LatLng(lat, long)
                val markerOptions = MarkerOptions().position(lastCoordinates).title("Your Location")
              //  markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.cast_ic_expanded_controller_play))
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

    private fun clickOnMarker(marker: Marker){

    }
}
