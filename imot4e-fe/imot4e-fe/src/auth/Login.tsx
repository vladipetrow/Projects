import { Box, Button, TextField, Typography, Alert } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../auth/AuthContext";
import { useToastContext } from "../contexts/ToastContext";
import { buildApiUrl, getDefaultFetchOptions } from "../config/api";
import { API_CONFIG } from "../config/api";

const Login = () => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const { login } = useAuth();
  const { success: showSuccess, error: showError } = useToastContext();
  const navigate = useNavigate();

  const handleSubmit = async () => {
    setError("");
    setIsLoading(true);

    try {
      // Try user login first
      let response = await fetch(buildApiUrl(API_CONFIG.ENDPOINTS.USER_LOGIN), {
        method: "POST",
        ...getDefaultFetchOptions(),
        body: JSON.stringify({ email, password }),
      });

      let role = "ROLE_USER";
      
      // If user login fails, try agency login
      if (!response.ok) {
        response = await fetch(buildApiUrl(API_CONFIG.ENDPOINTS.AGENCY_LOGIN), {
          method: "POST",
          ...getDefaultFetchOptions(),
          body: JSON.stringify({ email, password }),
        });
        role = "ROLE_AGENCY";
      }

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.error || "Invalid login credentials");
      }

      // Show success message
      showSuccess("Входът е успешен!");
      
      // Update auth state
      login();
      
      // Redirect to home page
      navigate("/", { replace: true });
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : "Входът не бе успешен. Моля, проверете вашите данни за достъп.";
      setError(errorMessage);
      showError(errorMessage);
    } finally {
      setIsLoading(false);
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
          disabled={isLoading}
        >
          {isLoading ? "Влизане..." : "Вход"}
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