import { AppBar, Box, Toolbar, Typography, Button, Menu, MenuItem } from "@mui/material";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import AddIcon from "@mui/icons-material/Add";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

const LoggedInNavbar = () => {
  const { logout } = useAuth();
  const navigate = useNavigate();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const open = Boolean(anchorEl);

  const handleMenuClick = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = (path: string) => {
    setAnchorEl(null);
    navigate(path);
  };

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static" sx={{ bgcolor: "primary.main" }}>
        <Toolbar>
          <Typography
            variant="h6"
            component="div"
            sx={{ flexGrow: 1, cursor: "pointer" }}
            onClick={() => navigate("/")}
          >
            CryptoMoti
          </Typography>
          {/* Обяви Button */}
          <Button
            color="inherit"
            onClick={() => navigate("/ads")}
            sx={{ mr: 2 }}
          >
            Обяви
          </Button>
          {/* Моите обяви Button */}
          <Button
            color="inherit"
            onClick={() => navigate("/dashboard")}
            sx={{ mr: 2 }}
          >
            Моите обяви
          </Button>
          {/* Абонаменти Dropdown */}
          <Button
            aria-controls={open ? "menu-abonamenti" : undefined}
            aria-haspopup="true"
            aria-expanded={open ? "true" : undefined}
            onClick={handleMenuClick}
            color="inherit"
            endIcon={<KeyboardArrowDownIcon />}
          >
            Абонаменти
          </Button>
          <Menu
            id="menu-abonamenti"
            anchorEl={anchorEl}
            open={open}
            onClose={() => setAnchorEl(null)}
          >
            <MenuItem onClick={() => handleMenuClose("/subscription")}>
              Купи абонамент
            </MenuItem>
          </Menu>
          {/* Създай обява Button */}
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
            }}
            onClick={() => navigate("/create-ad")}
          >
            Създай обява
          </Button>
          {/* Logout */}
          <Button color="inherit" onClick={() => logout()}>
            Изход
          </Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
};

export default LoggedInNavbar;

