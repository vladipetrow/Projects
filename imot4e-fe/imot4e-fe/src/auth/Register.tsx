import {
  Box,
  Button,
  FormControl,
  FormControlLabel,
  Radio,
  RadioGroup,
  TextField,
  Typography,
  Alert,
  CircularProgress,
} from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useToastContext } from "../contexts/ToastContext";
import { useFormValidation, ValidationRules } from "../hooks/useFormValidation";
import { buildApiUrl, getDefaultFetchOptions } from "../config/api";
import { API_CONFIG } from "../config/api";
import { UserInput, AgencyInput } from "../types";

const Register = () => {
  const navigate = useNavigate();
  const { success: showSuccess, error: showError } = useToastContext();
  const [isAgency, setIsAgency] = useState(false);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [agencyName, setAgencyName] = useState("");
  const [address, setAddress] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  // Validation rules
  const validationRules: ValidationRules = {
    firstName: { required: true, minLength: 2 },
    lastName: { required: true, minLength: 2 },
    email: { 
      required: true, 
      pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
      custom: (value) => {
        if (!value.includes('@')) return 'Невалиден имейл адрес';
        return null;
      }
    },
    password: { required: true, minLength: 6 },
    agencyName: { required: true, minLength: 2 },
    address: { required: true, minLength: 5 },
    phoneNumber: { 
      required: true, 
      pattern: /^[0-9+\-\s()]+$/,
      custom: (value) => {
        if (value.length < 8) return 'Телефонният номер трябва да бъде поне 8 цифри';
        return null;
      }
    }
  };

  const { validateForm, getFieldError, hasErrors } = useFormValidation(validationRules);

  const validateFields = () => {
    if (!email || !password) {
      return "Имейла и паролата са задължителни.";
    }

    if (isAgency) {
      if (!agencyName || !address || !phoneNumber) {
        return "Всички полета трябва да бъдат попълнени за успешна регистрация.";
      }
      if (!/^\d{10}$/.test(phoneNumber)) {
        return "Телефонният номер трябва да е точно 10 цифри.";
      }
    } else {
      if (!firstName || !lastName) {
        return "Името и Фамилията са задължителни.";
      }
    }

    return null;
  };

  const handleRegister = async () => {
    setError("");
    setSuccess("");

    const validationError = validateFields();
    if (validationError) {
      setError(validationError);
      return;
    }

    try {
      const endpoint = isAgency ? "agency/register" : "user/register";
      const data = isAgency
        ? {
            agencyName,
            address,
            phoneNumber,
            email,
            password,
          }
        : {
            firstName,
            lastName,
            email,
            password,
          };

      const response = await fetch(`http://localhost:8080/${endpoint}`, {
        method: "POST",
        headers: { "Content-type": "application/json" },
        body: JSON.stringify(data),
      });

      const responseData = await response.json();

      if (!response.ok) {
        // Check if it's a duplicate email error
        if (response.status === 409 && responseData.error?.includes("Email already exists")) {
          setError("Този имейл вече е регистриран. Моля използвайте друг имейл адрес.");
        } else {
          setError(responseData.error || "Регистрацията не бе успешна. Опитайте отново.");
        }
        return;
      }

      // Show success message
      setSuccess("Успешна регистрация!");
      setTimeout(() => {
        navigate("/login"); // Redirect after 2 seconds
      }, 2000);
    } catch (err) {
      console.error(err);
      setError("Регистрацията не бе успешна. Опитайте отново.");
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        bgcolor: "background.paper",
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
          m: 1,
          bgcolor: "background.paper",
          borderRadius: 1,
        }}
      >
        <Typography align="center" variant="h5" gutterBottom>
          Създай профил
        </Typography>
        {error && <Alert severity="error">{error}</Alert>}
        {success && <Alert severity="success">{success}</Alert>}
        <FormControl>
          <RadioGroup
            row
            defaultValue="individual"
            name="radio-buttons-group"
            onChange={(e) => setIsAgency(e.target.value === "agency")}
          >
            <FormControlLabel
              value="individual"
              control={<Radio />}
              label="Частно лице"
            />
            <FormControlLabel
              value="agency"
              control={<Radio />}
              label="Агенция"
            />
          </RadioGroup>
        </FormControl>
        {!isAgency ? (
          <>
            <TextField
              id="first-name"
              label="Име"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
            />
            <TextField
              id="last-name"
              label="Фамилия"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
            />
          </>
        ) : (
          <>
            <TextField
              id="agency-name"
              label="Име на агенция"
              value={agencyName}
              onChange={(e) => setAgencyName(e.target.value)}
            />
            <TextField
              id="address"
              label="Адрес"
              value={address}
              onChange={(e) => setAddress(e.target.value)}
            />
            <TextField
              id="phone-number"
              label="Телефон"
              value={phoneNumber}
              onChange={(e) => setPhoneNumber(e.target.value)}
            />
          </>
        )}
        <TextField
          id="email"
          label="Имейл"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <TextField
          id="password"
          label="Парола"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <Button variant="contained" onClick={handleRegister}>
          Създай профил
        </Button>
        <Typography>
          <a href="/login">Вече имаш профил?</a>
        </Typography>
      </Box>
    </Box>
  );
};

export default Register;
