import { Box, List, ListItem, ListItemText, ListSubheader, Switch } from "@mui/material";
import { GoogleMap, Marker, useJsApiLoader } from '@react-google-maps/api';
import { useCallback, useState } from "react";

const containerStyle = {
  width: "55em",
  height: "42em"
};

const center = {
  lat: 42.67500315541814,
  lng: 23.33011102986799
};

const ConvenienceMap = () => {
  const [map, setMap] = useState(null);
  const [checked, setChecked] = useState(['transport', 'nujdi', 'sport', 'social-life']);

  const onUnmount = useCallback(function callback(map: any) {
    setMap(null)
  }, []);

  const { isLoaded } = useJsApiLoader({
    id: 'google-map-script',
    googleMapsApiKey: "AIzaSyDiZEjj-eCVBTsLYI-G3f2lLwNrYiOt-jY"
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
        display: 'flex',
        gap: '1em',
        p: 1,
        m: 1,
        bgcolor: 'background.paper',
        borderRadius: 1,
      }}
    >
      {isLoaded ?
        <GoogleMap
          mapTypeId="hybrid"
          mapContainerStyle={containerStyle}
          center={center}
          zoom={18}
          // onLoad={onLoad}
          onUnmount={onUnmount}
        >
          <Marker position={center} />
        </GoogleMap> : <></>}
      {/* <iframe
        title="map"
        width="900"
        height="650"
        style={{ border: 0 }}
        loading="lazy"
        allowFullScreen
        referrerPolicy="no-referrer-when-downgrade"
        src="https://www.google.com/maps/embed/v1/place?key=API_KEY&q=Lozenets, 1421 Sofia">
      </iframe> */}
      <List
        sx={{ width: '100%', maxWidth: 360, bgcolor: 'background.paper' }}
        subheader={<ListSubheader>Удобства в района</ListSubheader>}
      >
        <ListItem>
          <ListItemText id="switch-list-label-transport" primary="Транспорт" />
          <Switch
            edge="end"
            onChange={handleToggle('transport')}
            checked={checked.indexOf('transport') !== -1}
            inputProps={{
              'aria-labelledby': 'switch-list-label-transport',
            }}
          />
        </ListItem>
        <ListItem>
          <ListItemText id="switch-list-label-nujdi" primary="Всекидневни нужди" />
          <Switch
            edge="end"
            onChange={handleToggle('nujdi')}
            checked={checked.indexOf('nujdi') !== -1}
            inputProps={{
              'aria-labelledby': 'switch-list-label-nujdi',
            }}
          />
        </ListItem>
        <ListItem>
          <ListItemText id="switch-list-label-sport" primary="Спорт" />
          <Switch
            edge="end"
            onChange={handleToggle('sport')}
            checked={checked.indexOf('sport') !== -1}
            inputProps={{
              'aria-labelledby': 'switch-list-label-sport',
            }}
          />
        </ListItem>
        <ListItem>
          <ListItemText id="switch-list-label-social-life" primary="Социален живот" />
          <Switch
            edge="end"
            onChange={handleToggle('social-life')}
            checked={checked.indexOf('social-life') !== -1}
            inputProps={{
              'aria-labelledby': 'switch-list-label-social-life',
            }}
          />
        </ListItem>
      </List>
    </Box>
  );
};

export default ConvenienceMap;