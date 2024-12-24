import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Button from "@mui/material/Button";
import { useNavigate } from "react-router-dom";

const Navbar = () => {
  const navigate = useNavigate();

  return (
    <Box sx={{ flexGrow: 1 }}>
      <AppBar position="static">
        <Toolbar>
          <Typography
            variant="h6"
            component="div"
            sx={{ flexGrow: 1, cursor: "pointer" }}
            onClick={() => navigate("/")}
          >
            CryptoMoti
          </Typography>
          <Button color="inherit" onClick={() => navigate("/login")}>
            Вход
          </Button>
          <Button color="inherit" onClick={() => navigate("/register")}>
            Регистрирай се
          </Button>
        </Toolbar>
      </AppBar>
    </Box>
  );
};

export default Navbar;
