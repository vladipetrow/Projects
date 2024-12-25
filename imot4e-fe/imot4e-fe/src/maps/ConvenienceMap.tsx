import { Box, List, ListItem, ListItemText, ListSubheader, Switch } from "@mui/material";
import { GoogleMap, Marker, useJsApiLoader } from "@react-google-maps/api";
import { useCallback, useState } from "react";

const containerStyle = {
  width: "100%",
  height: "500px",
};

const center = {
  lat: 42.67500315541814,
  lng: 23.33011102986799,
};

// Example marker categories
const markerCategories = {
  transport: [{ lat: 42.67500315541814, lng: 23.33011102986799 }],
  nujdi: [{ lat: 42.678, lng: 23.335 }],
  sport: [{ lat: 42.672, lng: 23.326 }],
  socialLife: [{ lat: 42.673, lng: 23.322 }],
};

const ConvenienceMap = () => {
  const [checked, setChecked] = useState(["transport", "nujdi", "sport", "socialLife"]);

  const { isLoaded } = useJsApiLoader({
    id: "google-map-script",
    googleMapsApiKey: process.env.REACT_APP_API_KEY || "",
  });

  const handleToggle = (value: string) => () => {
    const currentIndex = checked.indexOf(value);
    const newChecked = [...checked];

    if (currentIndex === -1) {
      newChecked.push(value);
    } else {
      newChecked.splice(currentIndex, 1);
    }

    setChecked(newChecked);
  };

  return (
    <Box
      sx={{
        display: "flex",
        gap: "1em",
        p: 1,
        m: 1,
        bgcolor: "background.paper",
        borderRadius: 1,
      }}
    >
      {isLoaded ? (
        <GoogleMap
          mapContainerStyle={containerStyle}
          center={center}
          zoom={14}
        >
          {checked.flatMap((category) =>
            markerCategories[category].map((location, index) => (
              <Marker key={`${category}-${index}`} position={location} />
            ))
          )}
        </GoogleMap>
      ) : (
        <div>Loading map...</div>
      )}
      <List
        sx={{ width: "100%", maxWidth: 360, bgcolor: "background.paper" }}
        subheader={<ListSubheader>Удобства в района</ListSubheader>}
      >
        {Object.keys(markerCategories).map((category) => (
          <ListItem key={category}>
            <ListItemText
              id={`switch-list-label-${category}`}
              primary={category}
            />
            <Switch
              edge="end"
              onChange={handleToggle(category)}
              checked={checked.includes(category)}
              inputProps={{
                "aria-labelledby": `switch-list-label-${category}`,
              }}
            />
          </ListItem>
        ))}
      </List>
    </Box>
  );
};

export default ConvenienceMap;
