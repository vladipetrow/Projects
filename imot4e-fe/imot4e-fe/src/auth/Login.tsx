import { Box, Button, TextField, Typography, Alert } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

const Login = () => {
  const [email, setEmail] = useState("");
  const [passwordHash, setPassword] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(""); // State to show success message
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async () => {
    setError("");
    setSuccess("");

    try {
      const response = await fetch("http://localhost:8080/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, passwordHash }),
        credentials: "include",
      });

      if (!response.ok) throw new Error("Invalid login credentials");

      // Show success message
      setSuccess("Входът е успешен!");
      login();

      // Redirect to home page after a brief delay
      setTimeout(() => navigate("/", { replace: true }), 2000);
    } catch (err) {
      setError("Входът не бе успешен. Моля, проверете вашите данни за достъп.");
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "flex-start",
        minHeight: "100vh",
        pt: 8,
      }}
    >
      <Box
        sx={{
          width: "100%",
          maxWidth: 400,
          bgcolor: "white",
          boxShadow: 3,
          borderRadius: 2,
          p: 4,
        }}
      >
        <Typography variant="h4" align="center" gutterBottom>
          Вход
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}
        {success && (
          <Alert severity="success" sx={{ mb: 2 }}>
            {success}
          </Alert>
        )}

        <TextField
          label="Имейл"
          variant="outlined"
          fullWidth
          margin="normal"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <TextField
          label="Парола"
          type="password"
          variant="outlined"
          fullWidth
          margin="normal"
          value={passwordHash}
          onChange={(e) => setPassword(e.target.value)}
        />
        <Button
          variant="contained"
          fullWidth
          sx={{ mt: 2 }}
          onClick={handleSubmit}
        >
          Вход
        </Button>

        <Box sx={{ mt: 2, textAlign: "center" }}>
          <Typography
            variant="body2"
            component="a"
            href="/register"
            sx={{
              textDecoration: "none",
              color: "primary.main",
              "&:hover": { textDecoration: "underline" },
            }}
          >
            Регистрирай се
          </Typography>
        </Box>
        <Box sx={{ mt: 2, textAlign: "center" }}>
          <Typography
            variant="body2"
            component="a"
            href="/password-reset"
            sx={{
              textDecoration: "none",
              color: "primary.main",
              "&:hover": { textDecoration: "underline" },
            }}
          >
            Забравена парола?
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};

export default Login;