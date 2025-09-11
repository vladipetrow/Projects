import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Typography,
  Container,
  Paper,
  Grid,
  TextField,
  Button,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
  CircularProgress,
  Alert,
  Card,
  CardMedia,
  IconButton,
  Chip
} from '@mui/material';
import { ArrowBack, Delete, Add } from '@mui/icons-material';
import { useAuth } from '../auth/AuthContext';

interface Post {
  postId: number;
  location: string;
  price: number;
  area: number;
  description: string;
  apartmentType: string;
  type: string;
  userId: number;
  agencyId: number;
  imageUrls: string[];
  postDate: string;
  isPromoted: boolean;
  viewCount: number;
}

const EditAd = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [post, setPost] = useState<Post | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [success, setSuccess] = useState<string | null>(null);

  // Form state
  const [formData, setFormData] = useState({
    location: '',
    price: '',
    area: '',
    description: '',
    apartmentType: 'TWO_BEDROOM',
    type: 'BUY'
  });

  // Image state
  const [images, setImages] = useState<File[]>([]);
  const [existingImages, setExistingImages] = useState<string[]>([]);
  const [imagesToDelete, setImagesToDelete] = useState<string[]>([]);

  useEffect(() => {
    if (id && isAuthenticated) {
      fetchPost(parseInt(id));
    } else if (!isAuthenticated) {
      navigate('/login');
    }
  }, [id, isAuthenticated, navigate]);

  const fetchPost = async (postId: number) => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await fetch(`http://localhost:8080/posts/${postId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include'
      });

      if (!response.ok) {
        if (response.status === 404) {
          throw new Error('Обявата не е намерена');
        }
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setPost(data);
      setFormData({
        location: data.location || '',
        price: data.price?.toString() || '',
        area: data.area?.toString() || '',
        description: data.description || '',
        apartmentType: data.apartmentType || 'TWO_BEDROOM',
        type: data.type || 'BUY'
      });
      setExistingImages(data.imageUrls || []);
      
    } catch (err) {
      console.error('Error fetching post:', err);
      setError(err instanceof Error ? err.message : 'Грешка при зареждане на обявата');
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (field: string, value: string) => {
    setFormData(prev => ({
      ...prev,
      [field]: value
    }));
  };

  const handleImageUpload = (event: React.ChangeEvent<HTMLInputElement>) => {
    const files = event.target.files;
    if (files) {
      const newImages = Array.from(files);
      setImages(prev => [...prev, ...newImages]);
    }
  };

  const handleRemoveNewImage = (index: number) => {
    setImages(prev => prev.filter((_, i) => i !== index));
  };

  const handleRemoveExistingImage = (imageUrl: string) => {
    setExistingImages(prev => prev.filter(url => url !== imageUrl));
    setImagesToDelete(prev => [...prev, imageUrl]);
  };

  const handleSubmit = async (event: React.FormEvent) => {
    event.preventDefault();
    
    if (!post) return;

    try {
      setSaving(true);
      setError(null);
      setSuccess(null);

      // Validate form
      if (!formData.location.trim()) {
        throw new Error('Моля, въведете локация');
      }
      if (!formData.price || parseFloat(formData.price) <= 0) {
        throw new Error('Моля, въведете валидна цена');
      }
      if (!formData.area || parseFloat(formData.area) <= 0) {
        throw new Error('Моля, въведете валидна площ');
      }
      if (!formData.description.trim()) {
        throw new Error('Моля, въведете описание');
      }

      // Prepare form data
      const submitData = new FormData();
      submitData.append('postInput', JSON.stringify({
        location: formData.location.trim(),
        price: parseFloat(formData.price),
        area: parseFloat(formData.area),
        description: formData.description.trim(),
        apartmentType: formData.apartmentType,
        type: formData.type
      }));

      // Add new images
      images.forEach((image, index) => {
        submitData.append('images', image);
      });

      // Add images to delete
      submitData.append('imagesToDelete', JSON.stringify(imagesToDelete));

      const response = await fetch(`http://localhost:8080/posts/update/${post.postId}`, {
        method: 'PUT',
        credentials: 'include',
        body: submitData
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Грешка при обновяване: ${response.status} ${errorText}`);
      }

      setSuccess('Обявата е обновена успешно!');
      setTimeout(() => {
        navigate('/dashboard');
      }, 2000);

    } catch (err) {
      console.error('Error updating post:', err);
      setError(err instanceof Error ? err.message : 'Грешка при обновяване на обявата');
    } finally {
      setSaving(false);
    }
  };

  const getTypeLabel = (type: string) => {
    const typeMap: { [key: string]: string } = {
      'STUDIO': 'Студио',
      'ONE_BEDROOM': 'Едностаен',
      'TWO_BEDROOM': 'Двустаен',
      'THREE_BEDROOM': 'Тристаен',
      'FOUR_BEDROOM': 'Четиристаен',
      'MULTI_BEDROOM': 'Многостаен',
      'HOUSE': 'Къща',
      'PENTHOUSE': 'Пентхаус'
    };
    return typeMap[type] || type;
  };

  if (loading) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="400px">
          <CircularProgress size={60} />
        </Box>
      </Container>
    );
  }

  if (error && !post) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
        <Button
          variant="contained"
          startIcon={<ArrowBack />}
          onClick={() => navigate('/dashboard')}
        >
          Назад към обявите
        </Button>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ mb: 3 }}>
        <Button
          variant="outlined"
          startIcon={<ArrowBack />}
          onClick={() => navigate('/dashboard')}
          sx={{ mb: 2 }}
        >
          Назад към обявите
        </Button>
      </Box>

      <Paper elevation={3} sx={{ p: 3 }}>
        <Typography variant="h4" gutterBottom>
          Редактиране на обява
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

        <form onSubmit={handleSubmit}>
          <Grid container spacing={3}>
            {/* Location */}
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Локация"
                value={formData.location}
                onChange={(e) => handleInputChange('location', e.target.value)}
                required
                placeholder="Например: София, Център"
              />
            </Grid>

            {/* Price */}
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Цена (лв.)"
                type="number"
                value={formData.price}
                onChange={(e) => handleInputChange('price', e.target.value)}
                required
                inputProps={{ min: 1 }}
              />
            </Grid>

            {/* Area */}
            <Grid item xs={12} md={6}>
              <TextField
                fullWidth
                label="Площ (кв.м)"
                type="number"
                value={formData.area}
                onChange={(e) => handleInputChange('area', e.target.value)}
                required
                inputProps={{ min: 1 }}
              />
            </Grid>

            {/* Apartment Type */}
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <InputLabel>Тип на имота</InputLabel>
                <Select
                  value={formData.apartmentType}
                  label="Тип на имота"
                  onChange={(e) => handleInputChange('apartmentType', e.target.value)}
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
            </Grid>

            {/* Transaction Type */}
            <Grid item xs={12} md={6}>
              <FormControl fullWidth>
                <InputLabel>Тип на сделката</InputLabel>
                <Select
                  value={formData.type}
                  label="Тип на сделката"
                  onChange={(e) => handleInputChange('type', e.target.value)}
                >
                  <MenuItem value="BUY">Продажба</MenuItem>
                  <MenuItem value="RENT">Под наем</MenuItem>
                </Select>
              </FormControl>
            </Grid>

            {/* Description */}
            <Grid item xs={12}>
              <TextField
                fullWidth
                label="Описание"
                multiline
                rows={4}
                value={formData.description}
                onChange={(e) => handleInputChange('description', e.target.value)}
                required
                placeholder="Опишете подробно вашия имот..."
              />
            </Grid>

            {/* Existing Images */}
            {existingImages.length > 0 && (
              <Grid item xs={12}>
                <Typography variant="h6" gutterBottom>
                  Текущи снимки
                </Typography>
                <Grid container spacing={2}>
                  {existingImages.map((imageUrl, index) => (
                    <Grid item xs={12} sm={6} md={4} key={index}>
                      <Card>
                        <CardMedia
                          component="img"
                          height="200"
                          image={imageUrl}
                          alt={`Снимка ${index + 1}`}
                        />
                        <Box sx={{ p: 1, display: 'flex', justifyContent: 'center' }}>
                          <Button
                            size="small"
                            color="error"
                            startIcon={<Delete />}
                            onClick={() => handleRemoveExistingImage(imageUrl)}
                          >
                            Премахни
                          </Button>
                        </Box>
                      </Card>
                    </Grid>
                  ))}
                </Grid>
              </Grid>
            )}

            {/* New Images Upload */}
            <Grid item xs={12}>
              <Typography variant="h6" gutterBottom>
                Добави нови снимки
              </Typography>
              <input
                accept="image/*"
                style={{ display: 'none' }}
                id="image-upload"
                multiple
                type="file"
                onChange={handleImageUpload}
              />
              <label htmlFor="image-upload">
                <Button
                  variant="outlined"
                  component="span"
                  startIcon={<Add />}
                  sx={{ mb: 2 }}
                >
                  Избери снимки
                </Button>
              </label>

              {/* New Images Preview */}
              {images.length > 0 && (
                <Grid container spacing={2} sx={{ mt: 2 }}>
                  {images.map((image, index) => (
                    <Grid item xs={12} sm={6} md={4} key={index}>
                      <Card>
                        <CardMedia
                          component="img"
                          height="200"
                          image={URL.createObjectURL(image)}
                          alt={`Нова снимка ${index + 1}`}
                        />
                        <Box sx={{ p: 1, display: 'flex', justifyContent: 'center' }}>
                          <Button
                            size="small"
                            color="error"
                            startIcon={<Delete />}
                            onClick={() => handleRemoveNewImage(index)}
                          >
                            Премахни
                          </Button>
                        </Box>
                      </Card>
                    </Grid>
                  ))}
                </Grid>
              )}
            </Grid>

            {/* Submit Buttons */}
            <Grid item xs={12}>
              <Box sx={{ display: 'flex', gap: 2, justifyContent: 'flex-end' }}>
                <Button
                  variant="outlined"
                  onClick={() => navigate('/dashboard')}
                  disabled={saving}
                >
                  Отказ
                </Button>
                <Button
                  type="submit"
                  variant="contained"
                  disabled={saving}
                  startIcon={saving ? <CircularProgress size={20} /> : null}
                >
                  {saving ? 'Запазване...' : 'Запази промените'}
                </Button>
              </Box>
            </Grid>
          </Grid>
        </form>
      </Paper>
    </Container>
  );
};

export default EditAd;
