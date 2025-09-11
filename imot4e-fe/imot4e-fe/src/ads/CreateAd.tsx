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
  Card,
  CardMedia,
  IconButton,
  Grid,
  Paper,
  Select,
  MenuItem,
  CircularProgress,
} from "@mui/material";
import { Add as AddIcon, Delete as DeleteIcon } from "@mui/icons-material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { PostInput, ApartmentType, TransactionType } from "../types";
import { buildApiUrl, getFileUploadOptions } from "../config/api";
import { API_CONFIG } from "../config/api";
import { useToastContext } from "../contexts/ToastContext";

const CreateAd = () => {
  const navigate = useNavigate();
  const { success: showSuccess, error: showError } = useToastContext();
  const [location, setLocation] = useState("");
  const [price, setPrice] = useState(0);
  const [area, setArea] = useState(0);
  const [description, setDescription] = useState("");
  const [apartmentType, setApartmentType] = useState<ApartmentType>("TWO_BEDROOM");
  const [transactionType, setTransactionType] = useState<TransactionType>("BUY");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [images, setImages] = useState<File[]>([]);
  const [imagePreviews, setImagePreviews] = useState<string[]>([]);

  const validateFields = () => {
    if (!location) return "Местоположението е задължително.";
    if (price <= 0) return "Цената трябва да бъде положително число.";
    if (area <= 0) return "Квадратурата трябва да бъде положително число.";
    if (!description || description.trim().length < 10)
      return "Описанието трябва да съдържа поне 10 символа.";
    return null;
  };

  const handleImageUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
    const files = event.target.files;
    if (files) {
      const newImages = Array.from(files);
      const updatedImages = [...images, ...newImages];
      
      // Limit to 5 images
      if (updatedImages.length > 5) {
        setError("Можете да качите максимум 5 снимки.");
        return;
      }
      
      setImages(updatedImages);
      
      // Create previews
      const newPreviews = newImages.map(file => URL.createObjectURL(file));
      setImagePreviews(prev => [...prev, ...newPreviews]);
    }
  };

  const removeImage = (index: number) => {
    const updatedImages = images.filter((_, i) => i !== index);
    const updatedPreviews = imagePreviews.filter((_, i) => i !== index);
    
    setImages(updatedImages);
    setImagePreviews(updatedPreviews);
  };

  const handleClick = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");
    setIsLoading(true);

    const validationError = validateFields();
    if (validationError) {
      setError(validationError);
      showError(validationError);
      setIsLoading(false);
      return;
    }

    try {
      const postInput: PostInput = {
        location,
        price,
        area,
        description,
        apartmentType,
        type: transactionType,
      };

      const formData = new FormData();
      formData.append("postInput", JSON.stringify(postInput));
      
      // Add images to form data
      images.forEach((image) => {
        formData.append("images", image);
      });

      const response = await fetch(buildApiUrl(API_CONFIG.ENDPOINTS.POSTS_ADD), {
        method: "POST",
        body: formData,
        ...getFileUploadOptions(),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.error || "Failed to create ad");
      }
      
      showSuccess("Обявата е създадена успешно!");
      setTimeout(() => navigate("/"), 2000);
    } catch (err) {
      console.error(err);
      const errorMessage = err instanceof Error ? err.message : "Възникна грешка при създаването на обявата. Опитайте отново.";
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
            variant={transactionType === "BUY" ? "contained" : "outlined"}
            size="large"
            onClick={() => setTransactionType("BUY")}
          >
            Продажба
          </Button>
          <Button
            variant={transactionType === "RENT" ? "contained" : "outlined"}
            size="large"
            onClick={() => setTransactionType("RENT")}
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
        
        {/* Apartment Type Selector */}
        <FormControl fullWidth>
          <InputLabel id="apartment-type-label">Тип на имота</InputLabel>
          <Select
            labelId="apartment-type-label"
            id="apartment-type"
            value={apartmentType}
            label="Тип на имота"
            onChange={(e) => setApartmentType(e.target.value)}
          >
            <MenuItem value="STUDIO">Студио</MenuItem>
            <MenuItem value="ONE_BEDROOM">Едностаен</MenuItem>
            <MenuItem value="TWO_BEDROOM">Двустаен</MenuItem>
            <MenuItem value="THREE_BEDROOM">Тристаен</MenuItem>
            <MenuItem value="FOUR_BEDROOM">Четиристаен</MenuItem>
            <MenuItem value="MULTI_BEDROOM">Многостаен</MenuItem>
            <MenuItem value="HOUSE">Къща</MenuItem>
            <MenuItem value="PENTHOUSE">Пентхаус</MenuItem>
          </Select>
        </FormControl>
        
        {/* Image Upload Section */}
        <Box sx={{ mt: 2 }}>
          <Typography variant="h6" gutterBottom>
            Снимки ({images.length}/5)
          </Typography>
          
          {/* Upload Button */}
          <input
            accept="image/*"
            style={{ display: 'none' }}
            id="image-upload"
            type="file"
            multiple
            onChange={handleImageUpload}
          />
          <label htmlFor="image-upload">
            <Button
              variant="outlined"
              component="span"
              startIcon={<AddIcon />}
              disabled={images.length >= 5}
              sx={{ mb: 2 }}
            >
              Добави снимки
            </Button>
          </label>
          
          {/* Image Previews */}
          {imagePreviews.length > 0 && (
            <Grid container spacing={2} sx={{ mt: 1 }}>
              {imagePreviews.map((preview, index) => (
                <Grid item xs={12} sm={6} md={4} key={index}>
                  <Card sx={{ position: 'relative' }}>
                    <CardMedia
                      component="img"
                      height="140"
                      image={preview}
                      alt={`Preview ${index + 1}`}
                    />
                    <IconButton
                      onClick={() => removeImage(index)}
                      sx={{
                        position: 'absolute',
                        top: 8,
                        right: 8,
                        backgroundColor: 'rgba(0,0,0,0.5)',
                        color: 'white',
                        '&:hover': {
                          backgroundColor: 'rgba(0,0,0,0.7)',
                        },
                      }}
                    >
                      <DeleteIcon />
                    </IconButton>
                  </Card>
                </Grid>
              ))}
            </Grid>
          )}
        </Box>
        
        <Button 
          variant="contained" 
          onClick={handleClick} 
          disabled={isLoading}
          sx={{ mt: 2 }}
        >
          {isLoading ? (
            <>
              <CircularProgress size={20} sx={{ mr: 1 }} />
              Създаване...
            </>
          ) : (
            "Създай обява"
          )}
        </Button>
      </Box>
    </Box>
  );
};

export default CreateAd;

