import { Box, Button, Link, TextField, Typography } from "@mui/material"
import { useNavigate } from "react-router-dom";

const PasswordResetForm = () => {
  const navigate = useNavigate();
  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        bgcolor: 'background.paper',
      }}>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          width: '50%',
          maxWidth: '50em',
          gap: '1em',
          p: 2,
          m: 1,
          bgcolor: 'background.paper',
          borderRadius: 1,
        }}
      >
        <Typography align="center" variant="h5" gutterBottom component="div">
          Въведи нова парола
        </Typography>
        <TextField
          id="outlined-name"
          label="Нова парола"
          type="password"
          autoComplete="current-password"
        />
        <TextField
          id="outlined-name"
          label="Повтори новата парола"
          type="password"
          autoComplete="current-password"
        />
        <Button variant="contained" onClick={() => navigate("/")}>Смени парола</Button>
        <Link href="/login" underline="hover">
          <Typography variant="body1" gutterBottom>Отмени смяната на паролата</Typography>
        </Link>
      </Box>
    </Box>
  );
};

export default PasswordResetForm;