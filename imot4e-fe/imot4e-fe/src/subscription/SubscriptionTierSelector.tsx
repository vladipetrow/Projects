import React, { useState, useEffect } from 'react';
import {
  Box,
  Card,
  CardContent,
  Typography,
  Button,
  Grid,
  Chip,
  Paper,
  CircularProgress,
  Alert
} from '@mui/material';
import { CheckCircle, Star, SupportAgent } from '@mui/icons-material';

interface SubscriptionTier {
  id: number;
  tierName: string;
  tierType: string;
  price: number;
  maxPosts: number;
  has24_7Support: boolean;
  description: string;
}

interface SubscriptionTierSelectorProps {
  userType: 'USER' | 'AGENCY';
  onTierSelect: (tierName: string) => void;
  selectedTier?: string;
}

const SubscriptionTierSelector: React.FC<SubscriptionTierSelectorProps> = ({
  userType,
  onTierSelect,
  selectedTier
}) => {
  const [tiers, setTiers] = useState<SubscriptionTier[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchTiers();
  }, [userType]);

  const fetchTiers = async () => {
    try {
      setLoading(true);
      console.log(`Fetching subscription tiers for: ${userType.toLowerCase()}`);
      const response = await fetch(`http://localhost:8080/subscription-tiers/${userType.toLowerCase()}`, {
        method: 'GET',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
        }
      });
      
      console.log('Subscription tiers response status:', response.status);
      
      if (!response.ok) {
        const errorText = await response.text();
        console.error('Subscription tiers error response:', errorText);
        throw new Error(`Failed to fetch subscription tiers: ${response.status} ${response.statusText}`);
      }

      const data = await response.json();
      console.log('Subscription tiers data:', data);
      setTiers(data);
    } catch (err) {
      setError('Failed to load subscription options');
      console.error('Error fetching tiers:', err);
    } finally {
      setLoading(false);
    }
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
      minimumFractionDigits: 2
    }).format(price);
  };

  const getTierDisplayName = (tierName: string) => {
    const nameMap: { [key: string]: string } = {
      'USER_PREMIUM': 'User Premium',
      'USER_PREMIUM_ULTRA': 'User Premium Ultra',
      'AGENCY_PREMIUM': 'Agency Premium',
      'AGENCY_PREMIUM_ULTRA': 'Agency Premium Ultra'
    };
    return nameMap[tierName] || tierName;
  };


  if (loading) {
    return (
      <Box display="flex" justifyContent="center" p={4}>
        <CircularProgress />
      </Box>
    );
  }

  if (error) {
    return (
      <Alert severity="error" sx={{ mb: 2 }}>
        {error}
      </Alert>
    );
  }

  return (
    <Box sx={{ mb: 4 }}>
      <Typography variant="h5" gutterBottom align="center" sx={{ mb: 3 }}>
        Изберете вашия абонаментен план
      </Typography>
      
      <Grid container spacing={3} justifyContent="center">
        {tiers.map((tier) => (
          <Grid item xs={12} sm={6} md={4} key={tier.id}>
            <Card
              sx={{
                height: '100%',
                display: 'flex',
                flexDirection: 'column',
                position: 'relative',
                border: selectedTier === tier.tierName ? '2px solid' : '1px solid',
                borderColor: selectedTier === tier.tierName ? 'primary.main' : 'divider',
                cursor: 'pointer',
                transition: 'all 0.3s ease',
                '&:hover': {
                  transform: 'translateY(-4px)',
                  boxShadow: 4
                }
              }}
              onClick={() => onTierSelect(tier.tierName)}
            >
              {tier.has24_7Support && (
                <Chip
                  icon={<Star />}
                  label="24/7 Support"
                  color="primary"
                  size="small"
                  sx={{
                    position: 'absolute',
                    top: 16,
                    right: 16,
                    zIndex: 1
                  }}
                />
              )}
              
              <CardContent sx={{ flexGrow: 1, p: 3 }}>
                <Typography variant="h6" component="h3" gutterBottom align="center">
                  {getTierDisplayName(tier.tierName)}
                </Typography>
                
                <Box textAlign="center" sx={{ mb: 3 }}>
                  <Typography variant="h3" component="span" color="primary">
                    {formatPrice(tier.price)}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    /месец
                  </Typography>
                </Box>

                <Typography variant="body2" color="text.secondary" align="center" sx={{ mb: 3 }}>
                  {tier.description}
                </Typography>

                <Box sx={{ mb: 3 }}>
                  <Box display="flex" alignItems="center" sx={{ mb: 1 }}>
                    <CheckCircle color="success" sx={{ mr: 1, fontSize: 20 }} />
                    <Typography variant="body2">
                      До {tier.maxPosts} обяви
                    </Typography>
                  </Box>
                  
                  <Box display="flex" alignItems="center" sx={{ mb: 1 }}>
                    <CheckCircle color="success" sx={{ mr: 1, fontSize: 20 }} />
                    <Typography variant="body2">
                      PROMO етикети
                    </Typography>
                  </Box>
                  
                  <Box display="flex" alignItems="center" sx={{ mb: 1 }}>
                    <CheckCircle color="success" sx={{ mr: 1, fontSize: 20 }} />
                    <Typography variant="body2">
                      Приоритет в търсенето
                    </Typography>
                  </Box>
                  
                  {tier.has24_7Support && (
                    <Box display="flex" alignItems="center" sx={{ mb: 1 }}>
                      <SupportAgent color="primary" sx={{ mr: 1, fontSize: 20 }} />
                      <Typography variant="body2" color="primary">
                        24/7 лична поддръжка
                      </Typography>
                    </Box>
                  )}
                </Box>

                <Button
                  variant={selectedTier === tier.tierName ? 'contained' : 'outlined'}
                  fullWidth
                  size="large"
                  onClick={(e) => {
                    e.stopPropagation();
                    onTierSelect(tier.tierName);
                  }}
                  sx={{
                    backgroundColor: selectedTier === tier.tierName 
                      ? (tier.has24_7Support ? 'primary.main' : 'primary.main')
                      : 'transparent',
                    color: selectedTier === tier.tierName 
                      ? 'white' 
                      : (tier.has24_7Support ? 'primary.main' : 'text.primary'),
                    borderColor: tier.has24_7Support ? 'primary.main' : 'grey.300',
                    '&:hover': {
                      backgroundColor: selectedTier === tier.tierName 
                        ? 'primary.dark' 
                        : (tier.has24_7Support ? 'primary.light' : 'grey.100')
                    }
                  }}
                >
                  {selectedTier === tier.tierName ? 'Избран' : 'Избери план'}
                </Button>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default SubscriptionTierSelector;
