import React, { useEffect, useState } from 'react';
import { Button, Box, Typography, CircularProgress } from '@mui/material';
import './style.css';

interface CoinbaseCommerceProps {
  chargeId: string;
  checkoutUrl?: string;
  onSuccess?: () => void;
  onError?: (error: any) => void;
}

declare global {
  interface Window {
    CoinbaseCommerce: any;
  }
}

export const CoinbaseCommerce: React.FC<CoinbaseCommerceProps> = ({ 
  chargeId, 
  checkoutUrl,
  onSuccess, 
  onError 
}) => {
  const [isLoaded, setIsLoaded] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Check if script is already loaded
    if (window.CoinbaseCommerce) {
      console.log('Coinbase Commerce already loaded');
      setIsLoaded(true);
      setIsLoading(false);
      return;
    }

    // Set a timeout to prevent infinite loading
    const timeout = setTimeout(() => {
      console.warn('Coinbase Commerce script loading timeout');
      setIsLoading(false);
      if (onError) onError('Coinbase Commerce script loading timeout');
    }, 10000); // 10 second timeout

    // Load Coinbase Commerce script
    const script = document.createElement('script');
    script.src = 'https://commerce.coinbase.com/v1/checkout.js';
    script.async = true;
    script.onload = () => {
      console.log('Coinbase Commerce script loaded');
      clearTimeout(timeout);
      
      // Try multiple times to find the CoinbaseCommerce object
      let attempts = 0;
      const maxAttempts = 50; // 5 seconds total
      
      const checkForCoinbaseCommerce = () => {
        attempts++;
        console.log(`Checking for CoinbaseCommerce object, attempt ${attempts}`);
        
        if (window.CoinbaseCommerce) {
          console.log('Coinbase Commerce object is available');
          setIsLoaded(true);
          setIsLoading(false);
        } else if (attempts < maxAttempts) {
          setTimeout(checkForCoinbaseCommerce, 100);
        } else {
          console.error('Coinbase Commerce script loaded but object not available after 5 seconds');
          setIsLoading(false);
          // Don't call onError here, just use fallback
          console.log('Using direct redirect fallback');
        }
      };
      
      // Start checking after a short delay
      setTimeout(checkForCoinbaseCommerce, 100);
    };
    script.onerror = (error) => {
      console.error('Failed to load Coinbase Commerce script:', error);
      clearTimeout(timeout);
      setIsLoading(false);
      if (onError) onError('Failed to load Coinbase Commerce script');
    };
    document.head.appendChild(script);

    return () => {
      clearTimeout(timeout);
      // Cleanup script on unmount
      const existingScript = document.querySelector('script[src="https://commerce.coinbase.com/v1/checkout.js"]');
      if (existingScript) {
        document.head.removeChild(existingScript);
      }
    };
  }, [onError]);

  const handlePayment = () => {
    if (window.CoinbaseCommerce) {
      try {
        window.CoinbaseCommerce.init({
          chargeId: chargeId,
          onSuccess: (charge: any) => {
            console.log('Payment successful:', charge);
            if (onSuccess) onSuccess();
          },
          onError: (error: any) => {
            console.error('Payment error:', error);
            if (onError) onError(error);
          }
        });
      } catch (error) {
        console.error('Error initializing Coinbase Commerce:', error);
        // Fallback: redirect to Coinbase checkout
        handleDirectRedirect();
      }
    } else {
      console.log('Coinbase Commerce not available, using direct redirect fallback');
      // Fallback: redirect to Coinbase checkout
      handleDirectRedirect();
    }
  };

  const handleDirectRedirect = () => {
    // Use the checkoutUrl from backend if available, otherwise construct it
    const url = checkoutUrl || `https://commerce.coinbase.com/pay/${chargeId}`;
    console.log('Redirecting to Coinbase checkout:', url);
    
    // Try window.open first, fallback to location.href if blocked
    const newWindow = window.open(url, '_blank');
    if (!newWindow || newWindow.closed || typeof newWindow.closed == 'undefined') {
      // Popup blocked, redirect in same window
      console.log('Popup blocked, redirecting in same window');
      window.location.href = url;
    }
  };

  if (isLoading) {
    return (
      <Box className="coinbase-commerce-container" sx={{ textAlign: 'center', p: 2 }}>
        <Typography variant="h6" gutterBottom>
          Loading Payment...
        </Typography>
        <CircularProgress sx={{ mb: 2 }} />
        <Typography variant="body2" color="text.secondary">
          Charge ID: {chargeId}
        </Typography>
      </Box>
    );
  }

  return (
    <Box className="coinbase-commerce-container" sx={{ textAlign: 'center', p: 2 }}>
      <Typography variant="h6" gutterBottom>
        Complete Your Payment
      </Typography>
      <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
        Charge ID: {chargeId}
      </Typography>
      {!isLoaded && !isLoading && (
        <Typography variant="body2" color="warning.main" sx={{ mb: 2 }}>
          Using direct payment method (Coinbase Commerce widget unavailable)
        </Typography>
      )}
             <Button
               variant="contained"
               color="primary"
               size="large"
               onClick={handlePayment}
               disabled={isLoading}
               sx={{
                 backgroundColor: isLoaded ? '#0052FF' : '#0052FF',
                 '&:hover': {
                   backgroundColor: isLoaded ? '#0039CC' : '#0039CC',
                 },
                 px: 4,
                 py: 1.5,
                 fontSize: '1.1rem',
                 fontWeight: 'bold'
               }}
             >
               {isLoading ? 'Loading Payment...' : (isLoaded ? 'Pay with Coinbase' : 'Open Payment Page')}
             </Button>
    </Box>
  );
};

export default CoinbaseCommerce;
