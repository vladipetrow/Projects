import { memo, useCallback, useState } from "react";
import {
  GoogleMap,
  useJsApiLoader,
  MarkerClusterer,
  Marker,
} from "@react-google-maps/api";
import { useNavigate } from "react-router-dom";

// Map container styles
const containerStyle = {
  width: "60%",
  height: "700px", // Set height explicitly for consistency
  margin: "0", // Remove any unnecessary margin
  padding: "0", // Remove unnecessary padding
};

// Default map center
const center = {
  lat: 42.6977,
  lng: 23.3219,
};

// Marker locations
const locations = [
  { lat: 42.70203125142239, lng: 23.31816268398586 },
  { lat: 42.70370274718432, lng: 23.327604059317043 },
  { lat: 42.69089318703021, lng: 23.31604861114047 },
  { lat: 42.70035470848623, lng: 23.31209536024857 },
  { lat: 42.7230086272865, lng: 23.328928816918094 },
  { lat: 42.71730712693488, lng: 23.288101498880657 },
  { lat: 42.70003416822498, lng: 23.281578125750706 },
  { lat: 42.72940972016858, lng: 23.31886653600338 },
];

// Marker clusterer options
const options = {
  imagePath:
    "https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m",
};

const createKey = (location: { lat: number; lng: number }, index: number) =>
  `${location.lat}-${location.lng}-${index}`;

const ClusteredMap = () => {
  const navigate = useNavigate();

  // Load Google Maps API
  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    googleMapsApiKey: process.env.REACT_APP_API_KEY || "", // Use your valid API key
  });

  const [map, setMap] = useState<google.maps.Map | null>(null);

  // Handle unmounting the map
  const onUnmount = useCallback(() => {
    setMap(null);
  }, []);

  return isLoaded ? (
    <GoogleMap
      mapContainerStyle={containerStyle}
      center={center}
      zoom={12}
      onUnmount={onUnmount}
    >
      {/* Marker Clusterer */}
      <MarkerClusterer options={options}>
        {(clusterer) => (
          <>
            {locations.map((location, index) => (
              <Marker
                key={createKey(location, index)} // Ensure unique keys
                position={location}
                clusterer={clusterer}
                onClick={() => navigate("/example-ad")} // Navigate to example ad
              />
            ))}
          </>
        )}
      </MarkerClusterer>
    </GoogleMap>
  ) : (
    <div>Loading map...</div>
  );
};

export default memo(ClusteredMap);


