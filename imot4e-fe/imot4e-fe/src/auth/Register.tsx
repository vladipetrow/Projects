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
} from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const navigate = useNavigate();
  const [isAgency, setIsAgency] = useState(false);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [passwordHash, setPassword] = useState("");
  const [nameOfAgency, setNameOfAgency] = useState("");
  const [address, setAddress] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(""); // State to store success message

  const validateFields = () => {
    if (!email || !passwordHash) {
      return "Имейла и паролата са задължителни.";
    }

    if (isAgency) {
      if (!nameOfAgency || !address || !phoneNumber) {
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
            nameOfAgency,
            address,
            phoneNumber,
            email,
            passwordHash,
          }
        : {
            firstName,
            lastName,
            email,
            passwordHash,
          };

      const response = await fetch(`http://localhost:8080/${endpoint}`, {
        method: "POST",
        headers: { "Content-type": "application/json" },
        body: JSON.stringify(data),
      });

      if (!response.ok) throw new Error("Registration failed");

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
              value={nameOfAgency}
              onChange={(e) => setNameOfAgency(e.target.value)}
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
          value={passwordHash}
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
