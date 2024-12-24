import { Box, Button, FormControl, FormControlLabel, Radio, RadioGroup, TextField, Typography, Alert } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Register = () => {
  const navigate = useNavigate();
  const [isAgency, setIsAgency] = useState(false);
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [nameOfAgency, setNameOfAgency] = useState("");
  const [address, setAddress] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [error, setError] = useState("");

  const handleRegister = async () => {
    try {
      const endpoint = isAgency ? "agency/register" : "user/register";
      const data = isAgency
        ? { nameOfAgency, address, phoneNumber, email, password }
        : { firstName, lastName, email, password };

      const response = await fetch(`http://localhost:8080/${endpoint}`, {
        method: "POST",
        headers: { "Content-type": "application/json" },
        body: JSON.stringify(data),
      });

      if (!response.ok) throw new Error("Registration failed");

      navigate("/login");
    } catch (err) {
      console.error(err);
      setError("Registration failed. Please try again.");
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
        <FormControl>
          <RadioGroup row defaultValue="individual" name="radio-buttons-group">
            <FormControlLabel
              value="individual"
              control={<Radio onClick={() => setIsAgency(false)} />}
              label="Частно лице"
            />
            <FormControlLabel
              value="agency"
              control={<Radio onClick={() => setIsAgency(true)} />}
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