import React from 'react';
import { AppBar, Toolbar, Typography, Button } from '@mui/material';
import AddIcon from '@mui/icons-material/Add';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

const SimpleNavbar: React.FC = () => {
  const { isAuthenticated, userRole, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
  };

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
        
        <Button color="inherit" onClick={() => navigate('/ads')}>
          ОБЯВИ
        </Button>
        
        {isAuthenticated ? (
          <>
            <Button
              startIcon={<AddIcon />}
              variant="outlined"
              color="inherit"
              sx={{
                textTransform: "none",
                borderWidth: 1,
                "&:hover": {
                  borderWidth: 2,
                },
                ml: 2
              }}
              onClick={() => navigate('/create-ad')}
            >
              Създай обява
            </Button>
            <Button color="inherit" onClick={() => navigate('/dashboard')} sx={{ ml: 2 }}>
              МОИТЕ ОБЯВИ
            </Button>
            <Button color="inherit" onClick={() => navigate('/subscription')} sx={{ ml: 2 }}>
              АБОНАМЕНТИ
            </Button>
            <Button color="inherit" onClick={handleLogout} sx={{ ml: 2 }}>
              ИЗХОД
            </Button>
          </>
        ) : (
          <>
            <Button color="inherit" onClick={() => navigate('/login')} sx={{ ml: 2 }}>
              ВХОД
            </Button>
            <Button color="inherit" onClick={() => navigate('/register')} sx={{ ml: 2 }}>
              РЕГИСТРИРАЙ СЕ
            </Button>
          </>
        )}
      </Toolbar>
    </AppBar>
  );
};

export default SimpleNavbar;
