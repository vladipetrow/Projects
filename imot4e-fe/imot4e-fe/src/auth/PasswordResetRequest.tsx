import { Box, Button, Link, TextField, Typography } from "@mui/material"
import { useNavigate } from "react-router-dom";

const PasswordResetRequest = () => {
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
        }}>
        <Typography align="center" variant="h5" gutterBottom component="div">
          Забравена Парола
        </Typography>
        <TextField
          id="outlined-name"
          label="Имейл"
        />
        <Button variant="contained" onClick={() => navigate("/forgotten-password-form")}>Смени парола</Button>
        <Link href="/login" underline="hover">
          <Typography variant="body1" gutterBottom>Обратно към 'Влез в профила'</Typography>
        </Link>
      </Box>
    </Box>
  );
};

export default PasswordResetRequest;