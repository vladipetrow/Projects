import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box, CircularProgress } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

const UnifiedNavbar: React.FC = () => {
  const { isAuthenticated, isLoading, userRole, logout } = useAuth();
  const navigate = useNavigate();

  // Show loading spinner while checking authentication
  if (isLoading) {
    return (
      <AppBar position="static" sx={{ backgroundColor: '#1976d2' }}>
        <Toolbar>
          <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
            CryptoMoti
          </Typography>
          <CircularProgress size={24} color="inherit" />
        </Toolbar>
      </AppBar>
    );
  }

  // Show logged-in navbar
  if (isAuthenticated) {
    return (
      <AppBar position="static" sx={{ backgroundColor: '#1976d2' }}>
        <Toolbar>
          <Typography 
            variant="h6" 
            component="div" 
            sx={{ flexGrow: 1, cursor: 'pointer' }}
            onClick={() => navigate('/')}
          >
            CryptoMoti
          </Typography>
          
          <Button
            color="inherit"
            onClick={() => navigate('/ads')}
            sx={{ mr: 2 }}
          >
            ОБЯВИ
          </Button>
          
          <Button
            color="inherit"
            onClick={() => navigate('/create-ad')}
            sx={{ mr: 2 }}
          >
            СЪЗДАЙ ОБЯВА
          </Button>
          
          <Button
            color="inherit"
            onClick={() => navigate('/dashboard')}
            sx={{ mr: 2 }}
          >
            МОИТЕ ОБЯВИ
          </Button>
          
          <Button
            color="inherit"
            onClick={() => navigate('/subscription')}
            sx={{ mr: 2 }}
          >
            АБОНАМЕНТИ
          </Button>
          
          <Button
            color="inherit"
            onClick={() => {
              console.log("Logout button clicked in UnifiedNavbar");
              logout();
            }}
          >
            ИЗХОД
          </Button>
          
          {/* Development only - Force logout button */}
          {process.env.NODE_ENV === 'development' && (
            <Button
              color="inherit"
              onClick={() => {
                console.log("Force logout clicked");
                // Clear everything aggressively
                document.cookie = "Authorization=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                document.cookie = "RefreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                localStorage.clear();
                window.location.reload();
              }}
              sx={{ ml: 1, fontSize: '0.7rem' }}
            >
              FORCE LOGOUT
            </Button>
          )}
        </Toolbar>
      </AppBar>
    );
  }

  // Show logged-out navbar
  return (
    <AppBar position="static" sx={{ backgroundColor: '#1976d2' }}>
      <Toolbar>
        <Typography 
          variant="h6" 
          component="div" 
          sx={{ flexGrow: 1, cursor: 'pointer' }}
          onClick={() => navigate('/')}
        >
          CryptoMoti
        </Typography>
        
        <Button
          color="inherit"
          onClick={() => navigate('/ads')}
          sx={{ mr: 2 }}
        >
          ОБЯВИ
        </Button>
        
        <Button
          color="inherit"
          onClick={() => navigate('/login')}
          sx={{ mr: 2 }}
        >
          ВХОД
        </Button>
        
        <Button
          color="inherit"
          onClick={() => navigate('/register')}
        >
          РЕГИСТРИРАЙ СЕ
        </Button>
      </Toolbar>
    </AppBar>
  );
};

export default UnifiedNavbar;
