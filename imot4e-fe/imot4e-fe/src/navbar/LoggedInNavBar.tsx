import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import { Menu, MenuItem } from "@mui/material";
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

          {/* Абонаменти Dropdown */}
          <Button
            id="demo-customized-button"
            aria-controls={open ? "demo-customized-menu" : undefined}
            aria-haspopup="true"
            aria-expanded={open ? "true" : undefined}
            variant="text"
            color="inherit"
            endIcon={<KeyboardArrowDownIcon />}
            onClick={handleMenuClick}
          >
            Абонаменти
          </Button>
          <Menu
            id="demo-customized-menu"
            anchorEl={anchorEl}
            open={open}
            onClose={() => setAnchorEl(null)}
            MenuListProps={{
              "aria-labelledby": "demo-customized-button",
            }}
          >
            <MenuItem
              onClick={() => handleMenuClose("/subscription/agency")}
              disableRipple
            >
              Купи абонамент
            </MenuItem>
          </Menu>

          {/* Създай обява Button */}
          <Button
            startIcon={<AddIcon />}
            variant="outlined"
            color="inherit"
            sx={{
              textTransform: "none", // Same as the original style
              borderWidth: 1, // Slightly thicker border
              "&:hover": {
                borderWidth: 2, // Consistent border width on hover
              },
            }}
            onClick={() => navigate("/create-ad")}
          >
            Създай обява
          </Button>

          <Button color="inherit" onClick={logout}>
            Изход
          </Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
};

export default LoggedInNavbar;
