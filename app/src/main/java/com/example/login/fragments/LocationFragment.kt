package com.example.login.fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.login.BuildConfig
import com.example.login.R
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.infowindow.BasicInfoWindow
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polygon;
import org.osmdroid.util.BoundingBox;


class LocationFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_location, container, false)

       /* val ctx = activity!!.applicationContext
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);
        val map = view.findViewById<MapView>(R.id.mapview)

        map.setUseDataConnection(true)
        //val map = view.findViewById(R.id.map) as MapView
        map.setTileSource(TileSourceFactory.MAPNIK)
        //map.setBuiltInZoomControls(true) //Map ZoomIn/ZoomOut Button Visibility
        map.setMultiTouchControls(true)
        val mapController: IMapController
        mapController = map.getController()

        //mapController.zoomTo(14, 1)
        mapController.setZoom(14)

        val mGpsMyLocationProvider = GpsMyLocationProvider(activity)
        val mLocationOverlay = MyLocationNewOverlay(mGpsMyLocationProvider, map)
        mLocationOverlay.enableMyLocation()
        mLocationOverlay.enableFollowLocation()

        val icon = BitmapFactory.decodeResource(resources, R.drawable.ic_menu_compass)
        mLocationOverlay.setPersonIcon(icon)
        map.getOverlays().add(mLocationOverlay)

        mLocationOverlay.runOnFirstFix {
            map.getOverlays().clear()
            map.getOverlays().add(mLocationOverlay)
            mapController.animateTo(mLocationOverlay.myLocation)
        }

       /* val line = Polyline(activity)
        line.setTitle("Central Park, NYC")
        line.setSubDescription(Polyline::class.java.getCanonicalName())
        line.setWidth(20f)
        val pts: MutableList<GeoPoint> = ArrayList()
        //here, we create a polygon, note that you need 5 points in order to make a closed polygon (rectangle)

        //here, we create a polygon, note that you need 5 points in order to make a closed polygon (rectangle)
        pts.add(GeoPoint(40.796788, -73.949232))
        pts.add(GeoPoint(40.796788, -73.981762))
        pts.add(GeoPoint(40.768094, -73.981762))
        pts.add(GeoPoint(40.768094, -73.949232))
        pts.add(GeoPoint(40.796788, -73.949232))
        line.setPoints(pts)
        line.setGeodesic(true)

        });*/map.getOverlayManager().add(line)*/
        return view
    }
}