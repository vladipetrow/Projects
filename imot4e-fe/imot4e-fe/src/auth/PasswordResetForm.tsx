import { Box, Button, Link, TextField, Typography } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const PasswordResetForm = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleSubmit = async () => {
    if (password !== confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    try {
      const response = await fetch("http://localhost:8080/reset-password", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        throw new Error("Failed to reset password. Please try again.");
      }

      // Display success message and redirect to login after a delay
      setSuccess("Your password has been successfully reset.");
      setTimeout(() => navigate("/login"), 3000);
    } catch (err) {
      setError(err.message); 
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "flex-start",
        bgcolor: "background.paper",
        minHeight: "100vh",
        pt: 8, 
      }}
    >
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
          width: "50%",
          maxWidth: "50em",
          gap: "1em",
          p: 2,
          bgcolor: "background.paper",
          borderRadius: 1,
          boxShadow: 3, 
        }}
      >
        <Typography align="center" variant="h5" gutterBottom component="div">
          Смяна на паролата
        </Typography>
        {error && (
          <Typography color="error" align="center" variant="body2">
            {error}
          </Typography>
        )}
        {success && (
          <Typography color="primary" align="center" variant="body2">
            {success}
          </Typography>
        )}
        <TextField
          id="email"
          label="Имейл"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          autoComplete="email"
          fullWidth
        />
        <TextField
          id="new-password"
          label="Нова парола"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          autoComplete="current-password"
          fullWidth
        />
        <TextField
          id="confirm-password"
          label="Повтори новата парола"
          type="password"
          value={confirmPassword}
          onChange={(e) => setConfirmPassword(e.target.value)}
          autoComplete="current-password"
          fullWidth
        />
        <Button
          variant="contained"
          color="primary"
          fullWidth
          sx={{ mt: 2 }}
          onClick={handleSubmit}
        >
          Смени парола
        </Button>
        <Link href="/login" underline="hover">
          <Typography variant="body1" gutterBottom>
            Отмени смяната на паролата
          </Typography>
        </Link>
      </Box>
    </Box>
  );
};

export default PasswordResetForm;