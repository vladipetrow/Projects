import {
  Box,
  Button,
  FormControl,
  Input,
  InputAdornment,
  InputLabel,
  TextField,
  Typography,
  Alert,
} from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const CreateAd = () => {
  const navigate = useNavigate();
  const [location, setLocation] = useState("");
  const [price, setPrice] = useState(0);
  const [area, setArea] = useState(0);
  const [description, setDescription] = useState("");
  const [isForSale, setIsForSale] = useState(true);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState(""); // State for success message

  const validateFields = () => {
    if (!location) return "Местоположението е задължително.";
    if (price <= 0) return "Цената трябва да бъде положително число.";
    if (area <= 0) return "Квадратурата трябва да бъде положително число.";
    if (!description || description.trim().length < 10)
      return "Описанието трябва да съдържа поне 10 символа.";
    return null;
  };

  const handleClick = (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    const validationError = validateFields();
    if (validationError) {
      setError(validationError);
      return;
    }

    const post = {
      location,
      price,
      area,
      description,
      type: isForSale ? "BUY" : "RENT",
    };

    fetch("http://localhost:8080/posts/add", {
      method: "POST",
      headers: {
        "Content-type": "application/json",
        "Authorization": localStorage.getItem("Authorization") || "",
      },
      body: JSON.stringify(post),
      credentials: "include",
    })
      .then((response) => {
        if (!response.ok) throw new Error("Failed to create ad");
        return response.json();
      })
      .then(() => {
        setSuccess("Обявата е създадена успешно!");
        setTimeout(() => navigate("/"), 2000); // Redirect after 2 seconds
      })
      .catch((err) => {
        console.error(err);
        setError("Възникна грешка при създаването на обявата. Опитайте отново.");
      });
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
          p: 1,
          m: 1,
          bgcolor: "background.paper",
          borderRadius: 1,
        }}
      >
        <Typography align="center" variant="h5" gutterBottom component="div">
          Създай обява
        </Typography>
        {error && <Alert severity="error">{error}</Alert>}
        {success && <Alert severity="success">{success}</Alert>}
        <Box
          sx={{
            display: "flex",
            flexDirection: "row",
            width: "50%",
            maxWidth: "50em",
            bgcolor: "background.paper",
            borderRadius: 1,
          }}
        >
          <Button
            variant={isForSale ? "contained" : "outlined"}
            size="large"
            onClick={() => setIsForSale(true)}
          >
            Продажба
          </Button>
          <Button
            variant={!isForSale ? "contained" : "outlined"}
            size="large"
            onClick={() => setIsForSale(false)}
          >
            Под наем
          </Button>
        </Box>
        <TextField
          id="location"
          label="Местоположение"
          value={location}
          onChange={(e) => setLocation(e.target.value)}
        />
        <TextField
          id="description"
          label="Описание"
          multiline
          minRows={4}
          maxRows={6}
          value={description}
          onChange={(e) => setDescription(e.target.value)}
        />
        <FormControl fullWidth sx={{ m: 1 }} variant="standard">
          <InputLabel htmlFor="price">Цена</InputLabel>
          <Input
            id="price"
            value={price}
            onChange={(e) => setPrice(parseInt(e.target.value) || 0)}
            type="number"
            startAdornment={<InputAdornment position="start">лв.</InputAdornment>}
          />
        </FormControl>
        <TextField
          id="area"
          label="Квадратура на имота"
          type="number"
          value={area}
          onChange={(e) => setArea(parseInt(e.target.value) || 0)}
        />
        <Button variant="contained" onClick={handleClick}>
          Създай обява
        </Button>
      </Box>
    </Box>
  );
};

export default CreateAd;

