import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import {
  Box,
  Typography,
  Container,
  Paper,
  Grid,
  Card,
  CardMedia,
  CardContent,
  Avatar,
  Chip,
  Button,
  CircularProgress,
  Alert,
  Divider,
  IconButton,
  ImageList,
  ImageListItem,
  Modal,
  Backdrop,
  Fade
} from '@mui/material';
import { ArrowBack, LocationOn, AttachMoney, Home, CalendarToday, NavigateBefore, NavigateNext } from '@mui/icons-material';
import { red, green } from '@mui/material/colors';

interface Post {
  postId: number;
  location: string;
  price: number;
  area: number;
  description: string;
  type: string;
  userId: number;
  agencyId: number;
  imageUrls: string[];
  postDate: string;
  isPromoted: boolean;
  viewCount: number;
}

const AdDetail = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [post, setPost] = useState<Post | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentImageIndex, setCurrentImageIndex] = useState(0);
  const [isImageModalOpen, setIsImageModalOpen] = useState(false);

  useEffect(() => {
    if (id) {
      fetchPost(parseInt(id));
    }
  }, [id]);

  const fetchPost = async (postId: number) => {
    try {
      setLoading(true);
      setError(null);
      
      const response = await fetch(`http://localhost:8080/posts/${postId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        if (response.status === 404) {
          throw new Error('Обявата не е намерена');
        }
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const data = await response.json();
      setPost(data);
      
    } catch (err) {
      console.error('Error fetching post:', err);
      setError(err instanceof Error ? err.message : 'Грешка при зареждане на обявата');
    } finally {
      setLoading(false);
    }
  };

  const getInitials = (userId: number, agencyId: number) => {
    if (agencyId > 0) {
      return 'A'; // Agency
    }
    return 'U'; // User
  };

  const getAdTitle = (type: string) => {
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

  const formatPrice = (price: number) => {
    return `${price.toLocaleString('bg-BG')}лв.`;
  };

  const formatDate = (dateString: string) => {
    if (!dateString) return new Date().toLocaleDateString('bg-BG');
    
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('bg-BG');
    } catch (error) {
      return new Date().toLocaleDateString('bg-BG');
    }
  };

  const handlePreviousImage = () => {
    if (post && post.imageUrls.length > 0) {
      setCurrentImageIndex((prev) => 
        prev === 0 ? post.imageUrls.length - 1 : prev - 1
      );
    }
  };

  const handleNextImage = () => {
    if (post && post.imageUrls.length > 0) {
      setCurrentImageIndex((prev) => 
        prev === post.imageUrls.length - 1 ? 0 : prev + 1
      );
    }
  };

  const handleImageClick = () => {
    setIsImageModalOpen(true);
  };

  const handleCloseImageModal = () => {
    setIsImageModalOpen(false);
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

  if (error) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
        <Button
          variant="contained"
          startIcon={<ArrowBack />}
          onClick={() => navigate('/ads')}
        >
          Назад към обявите
        </Button>
      </Container>
    );
  }

  if (!post) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Alert severity="warning" sx={{ mb: 2 }}>
          Обявата не е намерена
        </Alert>
        <Button
          variant="contained"
          startIcon={<ArrowBack />}
          onClick={() => navigate('/ads')}
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
          onClick={() => navigate('/ads')}
          sx={{ mb: 2 }}
        >
          Назад към обявите
        </Button>
      </Box>

      <Paper elevation={3} sx={{ p: 3 }}>
        <Grid container spacing={3}>
          {/* Images Section */}
          <Grid item xs={12} md={8}>
            <Box sx={{ position: 'relative' }}>
              {post.isPromoted && (
                <Chip
                  label="PROMO"
                  color="primary"
                  size="small"
                  sx={{
                    position: 'absolute',
                    top: 16,
                    right: 16,
                    zIndex: 1,
                    fontWeight: 'bold',
                    backgroundColor: green[500],
                    color: 'white',
                    '& .MuiChip-label': {
                      fontSize: '0.75rem',
                      fontWeight: 'bold'
                    }
                  }}
                />
              )}
              
              {post.imageUrls && post.imageUrls.length > 0 ? (
                <Box sx={{ position: 'relative' }}>
                  <CardMedia
                    component="img"
                    height="400"
                    image={post.imageUrls[currentImageIndex]}
                    alt="обява"
                    sx={{ 
                      borderRadius: 2,
                      cursor: 'pointer',
                      '&:hover': {
                        opacity: 0.9,
                      }
                    }}
                    onClick={handleImageClick}
                  />
                  
                  {/* Navigation arrows for multiple images */}
                  {post.imageUrls.length > 1 && (
                    <>
                      <IconButton
                        onClick={handlePreviousImage}
                        sx={{
                          position: 'absolute',
                          left: 16,
                          top: '50%',
                          transform: 'translateY(-50%)',
                          backgroundColor: 'rgba(0,0,0,0.5)',
                          color: 'white',
                          '&:hover': {
                            backgroundColor: 'rgba(0,0,0,0.7)',
                          },
                        }}
                      >
                        <NavigateBefore />
                      </IconButton>
                      
                      <IconButton
                        onClick={handleNextImage}
                        sx={{
                          position: 'absolute',
                          right: 16,
                          top: '50%',
                          transform: 'translateY(-50%)',
                          backgroundColor: 'rgba(0,0,0,0.5)',
                          color: 'white',
                          '&:hover': {
                            backgroundColor: 'rgba(0,0,0,0.7)',
                          },
                        }}
                      >
                        <NavigateNext />
                      </IconButton>
                      
                      {/* Image counter */}
                      <Box
                        sx={{
                          position: 'absolute',
                          bottom: 16,
                          right: 16,
                          backgroundColor: 'rgba(0,0,0,0.7)',
                          color: 'white',
                          px: 2,
                          py: 1,
                          borderRadius: 1,
                          fontSize: '0.875rem',
                        }}
                      >
                        {currentImageIndex + 1} / {post.imageUrls.length}
                      </Box>
                    </>
                  )}
                </Box>
              ) : (
                <CardMedia
                  component="img"
                  height="400"
                  image="https://images.pexels.com/photos/3288100/pexels-photo-3288100.png?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
                  alt="обява"
                  sx={{ borderRadius: 2 }}
                />
              )}
            </Box>
            
            {/* Thumbnail gallery for multiple images */}
            {post.imageUrls && post.imageUrls.length > 1 && (
              <Box sx={{ mt: 2 }}>
                <ImageList
                  sx={{
                    gridTemplateColumns: {
                      xs: 'repeat(4, 1fr) !important',
                      sm: 'repeat(6, 1fr) !important',
                    },
                    gap: 1,
                  }}
                  rowHeight={80}
                >
                  {post.imageUrls.map((imageUrl, index) => (
                    <ImageListItem
                      key={index}
                      onClick={() => setCurrentImageIndex(index)}
                      sx={{
                        cursor: 'pointer',
                        border: currentImageIndex === index ? 2 : 1,
                        borderColor: currentImageIndex === index ? 'primary.main' : 'grey.300',
                        borderRadius: 1,
                        overflow: 'hidden',
                      }}
                    >
                      <img
                        src={imageUrl}
                        alt={`обява ${index + 1}`}
                        style={{
                          width: '100%',
                          height: '100%',
                          objectFit: 'cover',
                        }}
                      />
                    </ImageListItem>
                  ))}
                </ImageList>
              </Box>
            )}
          </Grid>

          {/* Details Section */}
          <Grid item xs={12} md={4}>
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
              <Avatar sx={{ bgcolor: red[500], mr: 2 }}>
                {getInitials(post.userId, post.agencyId)}
              </Avatar>
              <Box>
                <Typography variant="h6" component="h1">
                  {getAdTitle(post.type)}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {post.agencyId > 0 ? 'Агенция' : 'Частно лице'}
                </Typography>
              </Box>
            </Box>

            <Divider sx={{ my: 2 }} />

            <Box sx={{ mb: 3 }}>
              <Typography variant="h4" color="primary" sx={{ fontWeight: 'bold', mb: 1 }}>
                {formatPrice(post.price)}
              </Typography>
              
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                <LocationOn color="action" sx={{ mr: 1 }} />
                <Typography variant="body1">{post.location}</Typography>
              </Box>
              
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                <Home color="action" sx={{ mr: 1 }} />
                <Typography variant="body1">{post.area} кв.м</Typography>
              </Box>
              
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                <CalendarToday color="action" sx={{ mr: 1 }} />
                <Typography variant="body1">{formatDate(post.postDate)}</Typography>
              </Box>

              {post.viewCount > 0 && (
                <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
                  Прегледи: {post.viewCount}
                </Typography>
              )}
            </Box>

            <Button
              variant="contained"
              size="large"
              fullWidth
              sx={{ mb: 2 }}
            >
              Свържете се с нас
            </Button>
          </Grid>
        </Grid>

        {/* Description Section */}
        <Box sx={{ mt: 3 }}>
          <Typography variant="h6" gutterBottom>
            Описание
          </Typography>
          <Typography variant="body1" paragraph>
            {post.description || 'Няма предоставено описание.'}
          </Typography>
        </Box>
      </Paper>

      {/* Full-screen Image Modal */}
      <Modal
        open={isImageModalOpen}
        onClose={handleCloseImageModal}
        closeAfterTransition
        BackdropComponent={Backdrop}
        BackdropProps={{
          timeout: 500,
        }}
      >
        <Fade in={isImageModalOpen}>
          <Box
            sx={{
              position: 'absolute',
              top: '50%',
              left: '50%',
              transform: 'translate(-50%, -50%)',
              width: '90vw',
              height: '90vh',
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
              justifyContent: 'center',
              outline: 'none',
            }}
          >
            {/* Close button */}
            <IconButton
              onClick={handleCloseImageModal}
              sx={{
                position: 'absolute',
                top: 16,
                right: 16,
                zIndex: 1,
                backgroundColor: 'rgba(0,0,0,0.5)',
                color: 'white',
                '&:hover': {
                  backgroundColor: 'rgba(0,0,0,0.7)',
                },
              }}
            >
              ×
            </IconButton>

            {/* Main image */}
            <Box
              component="img"
              src={post?.imageUrls[currentImageIndex]}
              alt="обява"
              sx={{
                maxWidth: '100%',
                maxHeight: '100%',
                objectFit: 'contain',
                borderRadius: 1,
              }}
            />

            {/* Navigation for multiple images */}
            {post && post.imageUrls.length > 1 && (
              <>
                <IconButton
                  onClick={handlePreviousImage}
                  sx={{
                    position: 'absolute',
                    left: 16,
                    top: '50%',
                    transform: 'translateY(-50%)',
                    backgroundColor: 'rgba(0,0,0,0.5)',
                    color: 'white',
                    '&:hover': {
                      backgroundColor: 'rgba(0,0,0,0.7)',
                    },
                  }}
                >
                  <NavigateBefore />
                </IconButton>
                
                <IconButton
                  onClick={handleNextImage}
                  sx={{
                    position: 'absolute',
                    right: 16,
                    top: '50%',
                    transform: 'translateY(-50%)',
                    backgroundColor: 'rgba(0,0,0,0.5)',
                    color: 'white',
                    '&:hover': {
                      backgroundColor: 'rgba(0,0,0,0.7)',
                    },
                  }}
                >
                  <NavigateNext />
                </IconButton>
                
                {/* Image counter */}
                <Box
                  sx={{
                    position: 'absolute',
                    bottom: 16,
                    left: '50%',
                    transform: 'translateX(-50%)',
                    backgroundColor: 'rgba(0,0,0,0.7)',
                    color: 'white',
                    px: 2,
                    py: 1,
                    borderRadius: 1,
                    fontSize: '0.875rem',
                  }}
                >
                  {currentImageIndex + 1} / {post.imageUrls.length}
                </Box>
              </>
            )}
          </Box>
        </Fade>
      </Modal>
    </Container>
  );
};

export default AdDetail;
