import { memo, useCallback, useState } from "react";
import {
  GoogleMap,
  useJsApiLoader,
  MarkerClusterer,
  Marker,
} from "@react-google-maps/api";
import { useNavigate } from "react-router-dom";

const containerStyle = {
  width: "55em",
  height: "42em",
};

const center = {
  lat: 42.6977,
  lng: 23.3219,
};

const locations = [
  { lat: 42.67189151917374, lng: 23.32950036232505 },
  { lat: 42.679999375307354, lng: 23.329567025945828 },
  { lat: 42.70152664008495, lng: 23.33601546643028 },
  { lat: 42.70203125142239, lng: 23.31816268398586 },
  { lat: 42.70370274718432, lng: 23.327604059317043 },
  { lat: 42.69089318703021, lng: 23.31604861114047 },
  { lat: 42.70035470848623, lng: 23.31209536024857 },
  { lat: 42.7230086272865, lng: 23.328928816918094 },
  { lat: 42.71730712693488, lng: 23.288101498880657 },
  { lat: 42.70003416822498, lng: 23.281578125750706 },
  { lat: 42.72940972016858, lng: 23.31886653600338 },
  { lat: 42.647622862029685, lng: 23.36961991106607 },
  { lat: 42.67612724848612, lng: 23.332530906212565 },
  { lat: 42.697855623497425, lng: 23.309385136266393 },
  { lat: 42.696420965190114, lng: 23.29042233679242 },
  { lat: 42.702364333820434, lng: 23.278988884168406 },
  { lat: 42.697855623497425, lng: 23.267834296242544 },
  { lat: 42.68781231929476, lng: 23.263372461072194 },
  { lat: 42.68781231929476, lng: 23.263372461072194 },
  { lat: 42.65849298248785, lng: 23.34145457655326 },
  { lat: 42.67407700946727, lng: 23.256958573014824 },
  { lat: 42.70830713355565, lng: 23.323328371173726 },
  { lat: 42.69150188507325, lng: 23.328347935740364 },
  { lat: 42.714454258750706, lng: 23.338108200175498 },
  { lat: 42.72633697442456, lng: 23.332252041514423 },
  { lat: 42.73166297030782, lng: 23.275363643092504 },
  { lat: 42.689657129579295, lng: 23.30603875988863 },
  { lat: 42.68740235401309, lng: 23.29990373652941 },
  { lat: 42.68248255959021, lng: 23.29014347209427 },
  { lat: 42.66443998005176, lng: 23.28735482511281 },
  { lat: 42.65459636531526, lng: 23.295441901359062 },
  { lat: 42.6619792224636, lng: 23.273690454903623 },
  { lat: 42.69150188507325, lng: 23.354282352668 },
  { lat: 42.67428203641295, lng: 23.37910131080306 },
  { lat: 42.671821668422744, lng: 23.382447687180814 },
  { lat: 42.71220038353679, lng: 23.35958078193279 },
  { lat: 42.69088697266312, lng: 23.35456121736615 },
  { lat: 42.68350754889142, lng: 23.29237438967945 },
  { lat: 42.70482349236455, lng: 23.287912554509102 },
  { lat: 42.69847046691107, lng: 23.309385136266393 },
  { lat: 42.7199861508801, lng: 23.334761823797734 },
  { lat: 42.7099464264701, lng: 23.364879211197575 },
  { lat: 42.71916664242345, lng: 23.356513270253174 },
  { lat: 42.69949519240496, lng: 23.261420408185167 },
  { lat: 42.68125255010712, lng: 23.26086267878888 },
  { lat: 42.63367350990758, lng: 23.325001559362608 },
  { lat: 42.63825278457154, lng: 23.311563714490102 },
];

const options = {
  imagePath:
    "https://developers.google.com/maps/documentation/javascript/examples/markerclusterer/m", // so you must have m1.png, m2.png, m3.png, m4.png, m5.png and m6.png in that folder
};

function createKey(location: any) {
  return location.lat + location.lng;
}

const ClusteredMap = () => {
  const navigate = useNavigate();
  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    googleMapsApiKey: `${process.env.REACT_APP_API_KEY}`,
  });

  const [map, setMap] = useState(null);

  const onUnmount = useCallback(function callback(map: any) {
    setMap(null);
  }, []);

  return isLoaded ? (
    <GoogleMap
      mapContainerStyle={containerStyle}
      center={center}
      zoom={12}
      // onLoad={onLoad}
      onUnmount={onUnmount}
    >
      <MarkerClusterer options={options}>
        {(clusterer) => (
          <>
            {locations.map((location) => (
              <Marker
                key={createKey(location)}
                position={location}
                clusterer={clusterer}
                onClick={() => navigate("/example-ad")}
              />
            ))}
          </>
        )}
      </MarkerClusterer>
      <></>
    </GoogleMap>
  ) : (
    <></>
  );
};

export default memo(ClusteredMap);
