import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Typography,
  Grid,
  Card,
  CardContent,
  CardMedia,
  Chip,
  Paper,
  Container,
  CircularProgress,
  Alert,
  Button,
  IconButton,
  CardActionArea,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions
} from '@mui/material';
import { green } from '@mui/material/colors';
import { Edit, Delete, Visibility } from '@mui/icons-material';
import { useAuth } from '../auth/AuthContext';
import { useToastContext } from '../contexts/ToastContext';
import { Post, ApartmentType } from '../types';
import { buildApiUrl, getDefaultFetchOptions } from '../config/api';
import { API_CONFIG } from '../config/api';
import AdCard from '../ads/AdCard';

const Dashboard: React.FC = () => {
  const { isAuthenticated } = useAuth();
  const { success: showSuccess, error: showError } = useToastContext();
  const navigate = useNavigate();
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
  const [postToDelete, setPostToDelete] = useState<Post | null>(null);
  const [deleting, setDeleting] = useState(false);

  console.log('Dashboard component rendering, isAuthenticated:', isAuthenticated);

  useEffect(() => {
    if (isAuthenticated) {
      fetchMyPosts();
    } else {
      setLoading(false);
    }
  }, [isAuthenticated]);

  const fetchMyPosts = async () => {
    try {
      setLoading(true);
      console.log('Fetching user posts...');
      const response = await fetch(buildApiUrl(API_CONFIG.ENDPOINTS.DASHBOARD_POSTS), {
        ...getDefaultFetchOptions()
      });

      console.log('Response status:', response.status);
      console.log('Response ok:', response.ok);

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        console.error('Error response:', errorData);
        throw new Error(errorData.error || `Failed to fetch posts: ${response.status}`);
      }

      const data = await response.json();
      console.log('Posts data:', data);
      setPosts(data.posts || []);
    } catch (err) {
      console.error('Error fetching posts:', err);
      const errorMessage = `Failed to load your posts: ${err instanceof Error ? err.message : 'Unknown error'}`;
      setError(errorMessage);
      showError(errorMessage);
    } finally {
      setLoading(false);
    }
  };

  const getInitials = (userId: number, agencyId: number): string => {
    if (userId > 0) return 'U';
    if (agencyId > 0) return 'A';
    return '?';
  };

  const getAdTitle = (type: string): string => {
    switch (type) {
      case 'BUY': return 'За покупка';
      case 'RENT': return 'За наем';
      case 'SALE': return 'За продажба';
      default: return type;
    }
  };

  const formatDate = (dateString: string): string => {
    return new Date(dateString).toLocaleDateString('bg-BG');
  };

  const getImageUrl = (post: Post): string => {
    if (post.imageUrls && post.imageUrls.length > 0) {
      return post.imageUrls[0];
    }
    return '/api/placeholder/300/200';
  };

  const formatPrice = (price: number): string => {
    return new Intl.NumberFormat('bg-BG', {
      style: 'currency',
      currency: 'BGN',
      minimumFractionDigits: 0
    }).format(price);
  };

  const handleViewPost = (postId: number) => {
    navigate(`/ad/${postId}`);
  };

  const handleEditPost = (postId: number) => {
    navigate(`/edit-ad/${postId}`);
  };

  const handleDeleteClick = (post: Post) => {
    setPostToDelete(post);
    setDeleteDialogOpen(true);
  };

  const handleDeleteConfirm = async () => {
    if (!postToDelete) return;

    try {
      setDeleting(true);
      const response = await fetch(buildApiUrl(`${API_CONFIG.ENDPOINTS.POSTS_DELETE}/${postToDelete.postId}`), {
        method: 'DELETE',
        ...getDefaultFetchOptions()
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.error || 'Failed to delete post');
      }

      // Remove the post from the local state
      setPosts(posts.filter(post => post.postId !== postToDelete.postId));
      setDeleteDialogOpen(false);
      setPostToDelete(null);
      showSuccess('Обявата е изтрита успешно!');
    } catch (err) {
      console.error('Error deleting post:', err);
      const errorMessage = err instanceof Error ? err.message : 'Грешка при изтриване на обявата. Моля, опитайте отново.';
      showError(errorMessage);
    } finally {
      setDeleting(false);
    }
  };

  const handleDeleteCancel = () => {
    setDeleteDialogOpen(false);
    setPostToDelete(null);
  };

  if (!isAuthenticated) {
    return (
      <Container maxWidth="md" sx={{ mt: 4 }}>
        <Alert severity="warning">
          Моля влезте в профила си, за да видите вашите обяви.
        </Alert>
      </Container>
    );
  }

  if (loading) {
    return (
      <Container maxWidth="md" sx={{ mt: 4, textAlign: 'center' }}>
        <CircularProgress />
        <Typography variant="h6" sx={{ mt: 2 }}>
          Зареждане на вашите обяви...
        </Typography>
      </Container>
    );
  }

  if (error) {
    return (
      <Container maxWidth="md" sx={{ mt: 4 }}>
        <Alert severity="error">{error}</Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ mt: 4, mb: 4 }}>
      <Paper elevation={3} sx={{ p: 3, mb: 3 }}>
        <Typography variant="h4" gutterBottom>
          Моите обяви
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Общо обяви: {posts.length}
        </Typography>
      </Paper>

      {posts.length === 0 ? (
        <Paper elevation={1} sx={{ p: 4, textAlign: 'center' }}>
          <Typography variant="h6" color="text.secondary">
            Нямате създадени обяви
          </Typography>
          <Typography variant="body2" color="text.secondary" sx={{ mt: 1 }}>
            Създайте първата си обява от <a href="/create-ad">тук</a>
          </Typography>
        </Paper>
      ) : (
        <Grid container spacing={3}>
          {posts.map((post) => (
            <Grid item xs={12} sm={6} md={4} key={post.postId}>
              <Card sx={{ maxWidth: 345, position: 'relative' }}>
                {post.isPromoted && (
                  <Chip
                    label="PROMO"
                    color="primary"
                    size="small"
                    sx={{
                      position: 'absolute',
                      top: 8,
                      right: 8,
                      zIndex: 2,
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

                {/* Action buttons */}
                <Box
                  sx={{
                    position: 'absolute',
                    top: 8,
                    left: 8,
                    zIndex: 2,
                    display: 'flex',
                    gap: 1,
                  }}
                >
                  <IconButton
                    size="small"
                    onClick={() => handleViewPost(post.postId)}
                    sx={{
                      backgroundColor: 'rgba(0,0,0,0.5)',
                      color: 'white',
                      '&:hover': {
                        backgroundColor: 'rgba(0,0,0,0.7)',
                      },
                    }}
                    title="Преглед"
                  >
                    <Visibility fontSize="small" />
                  </IconButton>
                </Box>
                
                <CardActionArea onClick={() => handleViewPost(post.postId)}>
                  <CardMedia
                    component="img"
                    height="200"
                    image={getImageUrl(post)}
                    alt={getAdTitle(post.type)}
                  />
                </CardActionArea>
                
                <CardContent>
                  <Typography gutterBottom variant="h6" component="div">
                    {getAdTitle(post.type)}
                  </Typography>
                  
                  <Typography variant="body2" color="text.secondary" gutterBottom>
                    📍 {post.location}
                  </Typography>
                  
                  <Typography variant="body2" color="text.secondary" gutterBottom>
                    📐 {post.area} кв.м
                  </Typography>
                  
                  <Typography variant="h6" color="primary" gutterBottom>
                    {formatPrice(post.price)}
                  </Typography>
                  
                  <Typography variant="body2" color="text.secondary" gutterBottom>
                    📅 {formatDate(post.postDate)}
                  </Typography>
                  
                  <Typography variant="body2" color="text.secondary">
                    👁️ {post.viewCount} преглеждания
                  </Typography>
                  
                  <Typography variant="body2" sx={{ mt: 1 }}>
                    {post.description.length > 100 
                      ? `${post.description.substring(0, 100)}...` 
                      : post.description
                    }
                  </Typography>

                  {/* Action buttons at bottom */}
                  <Box sx={{ mt: 2, display: 'flex', gap: 1, justifyContent: 'flex-end' }}>
                    <Button
                      size="small"
                      variant="outlined"
                      startIcon={<Edit />}
                      onClick={() => handleEditPost(post.postId)}
                    >
                      Редактирай
                    </Button>
                    <Button
                      size="small"
                      variant="outlined"
                      color="error"
                      startIcon={<Delete />}
                      onClick={() => handleDeleteClick(post)}
                    >
                      Изтрий
                    </Button>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}

      {/* Delete Confirmation Dialog */}
      <Dialog
        open={deleteDialogOpen}
        onClose={handleDeleteCancel}
        aria-labelledby="delete-dialog-title"
        aria-describedby="delete-dialog-description"
      >
        <DialogTitle id="delete-dialog-title">
          Потвърждение за изтриване
        </DialogTitle>
        <DialogContent>
          <DialogContentText id="delete-dialog-description">
            Сигурни ли сте, че искате да изтриете тази обява? Това действие не може да бъде отменено.
            {postToDelete && (
              <Box sx={{ mt: 2, p: 2, bgcolor: 'grey.100', borderRadius: 1 }}>
                <Typography variant="body2" fontWeight="bold">
                  {getAdTitle(postToDelete.type)} - {postToDelete.location}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {formatPrice(postToDelete.price)} • {postToDelete.area} кв.м
                </Typography>
              </Box>
            )}
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDeleteCancel} disabled={deleting}>
            Отказ
          </Button>
          <Button 
            onClick={handleDeleteConfirm} 
            color="error" 
            variant="contained"
            disabled={deleting}
          >
            {deleting ? 'Изтриване...' : 'Изтрий'}
          </Button>
        </DialogActions>
      </Dialog>
    </Container>
  );
};

export default Dashboard;
