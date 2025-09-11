import { Box, Button, Card, CardContent, List, ListItem, ListItemIcon, ListItemText, Typography, Alert } from "@mui/material";
import CheckIcon from "@mui/icons-material/Check";
import { useState } from "react";
import { getAuthToken } from "../utils/authUtils";
import SubscriptionTierSelector from "./SubscriptionTierSelector";

const UserSubscription = () => {
    const [isLoading, setIsLoading] = useState(false);
    const [selectedTier, setSelectedTier] = useState<string>('USER_PREMIUM');
    const [error, setError] = useState<string | null>(null);

    const handleTierSelect = (tierName: string) => {
        setSelectedTier(tierName);
        setError(null);
    };

    const handleSubscribe = async () => {
        console.log("User subscribe button clicked!");
        setIsLoading(true);
        
        // Authentication is handled via cookies, no need to check token here
        
        try {
            console.log("Making API call to create user subscription...");
            const response = await fetch(`http://localhost:8080/subscriptions/subscribe?tier=${selectedTier}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: 'include', // Include cookies for authentication
            });
            
            console.log("Response status:", response.status);
            
            if (response.ok) {
                const data = await response.json();
                console.log("User subscription created:", data);
                
                // Redirect directly to Coinbase checkout page
                const url = data.checkoutUrl || `https://commerce.coinbase.com/pay/${data.chargeId}`;
                console.log("Redirecting to Coinbase checkout:", url);
                
                // Store the current page URL so we can return after payment
                sessionStorage.setItem('returnUrl', window.location.href);
                
                // Redirect to Coinbase checkout
                window.location.href = url;
            } else {
                const errorData = await response.json();
                console.error("API Error:", errorData);
                setError(`Subscription failed: ${errorData.error || 'Unknown error'}`);
            }
        } catch (error) {
            console.error("Error creating user subscription:", error);
            if (error instanceof TypeError && error.message.includes('fetch')) {
                setError("Cannot connect to server. Please make sure the backend server is running on localhost:8080");
            } else {
                setError("Network error. Please check if the server is running.");
            }
        } finally {
            setIsLoading(false);
        }
    };


    return (
        <Box sx={{ p: 3 }}>
            {error && (
                <Alert severity="error" sx={{ mb: 3 }}>
                    {error}
                </Alert>
            )}
            
            <SubscriptionTierSelector
                userType="USER"
                onTierSelect={handleTierSelect}
                selectedTier={selectedTier}
            />
            
            <Box display="flex" justifyContent="center" mt={3}>
                <Button
                    variant="contained"
                    size="large"
                    onClick={handleSubscribe}
                    disabled={isLoading}
                    sx={{ minWidth: 200 }}
                >
                    {isLoading ? "Opening Payment..." : "Продължи към плащане"}
                </Button>
            </Box>
        </Box>
    );
};

export default UserSubscription;
