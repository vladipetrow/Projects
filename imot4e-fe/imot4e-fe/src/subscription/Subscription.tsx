import { Box, Typography, Card, CardContent, Button } from "@mui/material";
import { useState, useEffect } from "react";
import AgencySubscription from "./AgencySubscription";
import UserSubscription from "./UserSubscription";
import { getUserRole, isAuthenticated } from "../utils/authUtils";

const Subscription = () => {
  const [userRole, setUserRole] = useState<string>("");
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const checkAuth = async () => {
      const authenticated = await isAuthenticated();
      console.log("Subscription page - Authenticated:", authenticated);
      
      if (authenticated) {
        const role = await getUserRole();
        console.log("User role:", role);
        setUserRole(role);
      } else {
        setUserRole("");
      }
      setIsLoading(false);
    };
    
    checkAuth();
  }, []);

  if (isLoading) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
        <Typography>Loading...</Typography>
      </Box>
    );
  }

  if (!userRole) {
    return (
      <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
        <Card sx={{ maxWidth: 400 }}>
          <CardContent sx={{ textAlign: 'center' }}>
            <Typography variant="h5" gutterBottom>
              Please Log In
            </Typography>
            <Typography variant="body1" color="text.secondary" sx={{ mb: 2 }}>
              You need to be logged in to view subscription options.
            </Typography>
            <Button 
              variant="contained" 
              href="/login"
              sx={{ mt: 2 }}
            >
              Go to Login
            </Button>
          </CardContent>
        </Card>
      </Box>
    );
  }

  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        flexDirection: 'column',
        bgcolor: 'background.paper',
        p: 2
      }}>
      
      {userRole.includes("ROLE_AGENCY") ? (
        <AgencySubscription />
      ) : userRole.includes("ROLE_USER") ? (
        <UserSubscription />
      ) : (
        <Box sx={{ display: 'flex', justifyContent: 'center', p: 4 }}>
          <Typography>Unknown user role: "{userRole}" (length: {userRole.length})</Typography>
        </Box>
      )}
    </Box>
  );
};

export default Subscription;