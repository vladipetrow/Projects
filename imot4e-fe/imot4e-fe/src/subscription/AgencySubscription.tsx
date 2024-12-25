import { Box, Button, Card, CardContent, List, ListItem, ListItemIcon, ListItemText, Typography } from "@mui/material";
import CheckIcon from "@mui/icons-material/Check";
import { useState } from "react";
import { BTCPay } from "./BTCPay";

const AgencySubscription = () => {
    const [isVisible, setVisibility] = useState(false);

    const handleSubscribe = () => {
        setVisibility(true);
        fetch("http://localhost:8080/subscribe", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                Authorization: localStorage.getItem("Authorization") || "",
            },
        });
    };

    return (
        <Box
            sx={{
                display: "flex",
                justifyContent: "center",
                bgcolor: "background.paper",
            }}
        >
            <Card sx={{ minWidth: 320 }}>
                <CardContent>
                    <Typography align="center" variant="h3" color="text.secondary">
                        🚀 Pro
                    </Typography>
                    <List>
                        <ListItem disablePadding>
                            <ListItemIcon>
                                <CheckIcon />
                            </ListItemIcon>
                            <ListItemText primary="Up to 10 posts" />
                        </ListItem>
                    </List>
                    {!isVisible ? (
                        <Button variant="contained" color="success" onClick={handleSubscribe}>
                            Subscribe
                        </Button>
                    ) : (
                        <BTCPay storeId="7Fo9zM7sTVxvm9hhvoHTSeH7cGjoQjhSV97vqEmR5buP" price="2" currency="USD" />
                    )}
                </CardContent>
            </Card>
        </Box>
    );
};

export default AgencySubscription;
