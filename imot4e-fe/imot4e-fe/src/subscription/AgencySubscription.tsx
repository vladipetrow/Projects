import { Box, Button, Card, CardContent, List, ListItem, ListItemIcon, ListItemText, Typography } from "@mui/material"
import CheckIcon from '@mui/icons-material/Check';
import { useState } from "react";
import {BTCPay} from "./BTCPay.js"
import { useNavigate } from "react-router-dom";

const AgencySubscription = () => {
  const [isVisible, setVisibility] = useState(false);
  const navigate = useNavigate();

 
  const handleClick = () => {
    fetch("http://localhost:8080/subscribe",{
    method: "POST",
    headers: {
      "Content-type" : "application/json" , 
      "Authorization" : localStorage.getItem("Authorization")!
    },
    body: ""
  }); 
}

  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        bgcolor: 'background.paper',
      }}>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          width: '50%',
          maxWidth: '50em',
          gap: '1em',
          p: 2,
          m: 1,
          bgcolor: 'background.paper',
          borderRadius: 1,
        }}
      >
        <Typography align="center" variant="h5" component="div">
         Абонирай се
        </Typography>
        <Box
        sx={{
          display: 'flex',
          flexDirection: 'row',
          justifyContent: 'center',
          gap: '1em',
          p: 1,
          m: 1,
          bgcolor: 'background.paper',
          borderRadius: 1,
        }}
      >
        <Card sx={{ minWidth: 320 }}>
          <CardContent>
            <Typography align="center" variant="h3" color="text.secondary">
              🚀 Про
            </Typography>
            <nav aria-label="secondary mailbox folders">
              <List>
                <ListItem disablePadding>
                  <ListItemIcon>
                    <CheckIcon />
                  </ListItemIcon>
                  <ListItemText primary="До 10 обяви" />
                </ListItem>
                <ListItem disablePadding>
                </ListItem>
              </List>
            </nav>
            {!isVisible &&
              <Button variant="contained" color="success"  onClick={() =>{
                 setVisibility(true); 
                  handleClick();
                }}>Заявете</Button>
            }
            {isVisible && <BTCPay />}
          </CardContent>
        </Card>

      </Box>
      </Box>
    </Box>
  );
};

export default AgencySubscription;