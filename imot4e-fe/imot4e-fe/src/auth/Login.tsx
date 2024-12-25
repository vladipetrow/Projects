import { Box, Button, TextField, Typography } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleSubmit = async () => {
    try {
      const response = await fetch("http://localhost:8080/user/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password }),
        credentials: "include",
      });

      if (!response.ok) throw new Error("Invalid login credentials");

      login(); // Update authentication state
      navigate("/",{ replace: true }); // Redirect to homepage
    } catch (err) {
      setError("Login failed. Please check your credentials.");
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
          <Typography color="error" variant="body2" align="center" sx={{ mb: 2 }}>
            {error}
          </Typography>
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
          value={password}
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
            sx={{ textDecoration: "none", color: "primary.main", "&:hover": { textDecoration: "underline" } }}
          >
            Регистрирай се
          </Typography>
        </Box>
        <Box sx={{ mt: 2, textAlign: "center" }}>
          <Typography
            variant="body2"
            component="a"
            href="/password-reset"
            sx={{ textDecoration: "none", color: "primary.main", "&:hover": { textDecoration: "underline" } }}
          >
            Забравена парола?
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};

export default Login;
