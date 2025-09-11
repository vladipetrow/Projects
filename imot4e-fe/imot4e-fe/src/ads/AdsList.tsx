import React, { useState, useEffect, useMemo, useCallback } from 'react';
import { 
  Box, 
  Typography, 
  Grid, 
  Pagination, 
  CircularProgress, 
  Alert,
  Container,
  Paper,
  Button,
  TextField,
  MenuItem,
  FormControl,
  InputLabel,
  Select
} from '@mui/material';
import AdCard from './AdCard';
import ResponsiveGrid from '../components/ResponsiveGrid';
import LoadingSpinner from '../components/LoadingSpinner';
import { Post, FilterState, PaginatedResponse } from '../types';
import { buildApiUrl, getDefaultFetchOptions } from '../config/api';
import { API_CONFIG } from '../config/api';
import { useToastContext } from '../contexts/ToastContext';
import { useDebounce } from '../hooks/useDebounce';
import { usePerformanceMonitor } from '../hooks/usePerformanceMonitor';

const AdsList = () => {
  // Performance monitoring
  usePerformanceMonitor('AdsList');
  
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [totalPosts, setTotalPosts] = useState(0);
  const { error: showError } = useToastContext();
  
  // Filter states
  const [filters, setFilters] = useState<FilterState>({
    type: 'BUY', // BUY or RENT
    location: '',
    propertyType: '',
    maxPrice: ''
  });
  
  const postsPerPage = 12; // 3 rows × 4 columns
  
  // Debounce filter changes
  const debouncedFilters = useDebounce(filters, 500);

  useEffect(() => {
    fetchPosts(currentPage);
  }, [currentPage]);

  // Trigger search when debounced filters change
  useEffect(() => {
    if (currentPage === 1) {
      fetchPosts(1);
    } else {
      setCurrentPage(1);
    }
  }, [debouncedFilters]);

  const fetchPosts = useCallback(async (page: number) => {
    try {
      setLoading(true);
      setError(null);
      
      // Build query parameters
      const params = new URLSearchParams({
        page: (page - 1).toString(),
        pageSize: postsPerPage.toString()
      });

      // Add filters if they have values
      if (debouncedFilters.location) {
        params.append('location', debouncedFilters.location);
      }
      if (debouncedFilters.propertyType) {
        params.append('type', debouncedFilters.propertyType);
      }
      if (debouncedFilters.maxPrice) {
        params.append('price', debouncedFilters.maxPrice);
      }
      // Always send transaction type
      params.append('transactionType', debouncedFilters.type);
      
      const response = await fetch(
        `${buildApiUrl(API_CONFIG.ENDPOINTS.POSTS_LIST)}?${params.toString()}`,
        {
          method: 'GET',
          ...getDefaultFetchOptions()
        }
      );

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({}));
        throw new Error(errorData.error || `HTTP error! status: ${response.status}`);
      }

      const data: PaginatedResponse<Post> = await response.json();
      setPosts(data.posts);
      setTotalPosts(data.totalCount);
      setTotalPages(data.totalPages);
      
    } catch (err) {
      console.error('Error fetching posts:', err);
      const errorMessage = err instanceof Error ? err.message : 'Грешка при зареждане на обявите. Моля, опитайте отново.';
      setError(errorMessage);
      showError(errorMessage);
    } finally {
      setLoading(false);
    }
  }, [debouncedFilters, postsPerPage, showError]);

  const handlePageChange = useCallback((event: React.ChangeEvent<unknown>, value: number) => {
    setCurrentPage(value);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }, []);

  const handleFilterChange = useCallback((field: string, value: string) => {
    setFilters(prev => ({
      ...prev,
      [field]: value
    }));
  }, []);

  const handleSearch = useCallback(() => {
    setCurrentPage(1); // Reset to first page when searching
    fetchPosts(1);
  }, [fetchPosts]);

  const handleClearFilters = useCallback(() => {
    setFilters({
      type: 'BUY',
      location: '',
      propertyType: '',
      maxPrice: ''
    });
    setCurrentPage(1);
  }, []);

  // Memoized utility functions
  const formatPrice = useCallback((price: number) => {
    return `${price.toLocaleString('bg-BG')}лв.`;
  }, []);

  const getInitials = useCallback((userId: number, agencyId: number) => {
    if (agencyId > 0) {
      return 'A'; // Agency
    }
    return 'U'; // User
  }, []);

  const getAdTitle = useCallback((type: string) => {
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
  }, []);

  const getImageUrl = useCallback((post: Post) => {
    // Use first image if available, otherwise use default
    if (post.imageUrls && post.imageUrls.length > 0) {
      return post.imageUrls[0];
    }
    return "https://images.pexels.com/photos/3288100/pexels-photo-3288100.png?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
  }, []);

  const formatDate = useCallback((dateString: string) => {
    if (!dateString) return new Date().toLocaleDateString('bg-BG');
    
    try {
      const date = new Date(dateString);
      return date.toLocaleDateString('bg-BG');
    } catch (error) {
      return new Date().toLocaleDateString('bg-BG');
    }
  }, []);

  if (loading) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <LoadingSpinner message="Зареждане на обяви..." size={60} fullHeight />
      </Container>
    );
  }

  if (error) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      </Container>
    );
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Paper elevation={1} sx={{ p: 3, mb: 3 }}>
        <Typography variant="h4" component="h1" gutterBottom align="center">
          Обяви
        </Typography>
        <Typography variant="body1" color="text.secondary" align="center" sx={{ mb: 3 }}>
          Намерени {totalPosts} обяви
        </Typography>

        {/* Search Filters */}
        <Box
          sx={{
            display: 'flex',
            flexDirection: { xs: 'column', md: 'row' },
            gap: 2,
            alignItems: 'center',
            justifyContent: 'center',
            flexWrap: 'wrap',
            p: 2,
            bgcolor: 'grey.50',
            borderRadius: 2,
            mb: 2
          }}
        >
          {/* Купи/Наеми Buttons */}
          <Box sx={{ display: 'flex', gap: 1 }}>
            <Button
              variant={filters.type === 'BUY' ? 'contained' : 'outlined'}
              onClick={() => handleFilterChange('type', 'BUY')}
              sx={{ minWidth: 80 }}
            >
              КУПИ
            </Button>
            <Button
              variant={filters.type === 'RENT' ? 'contained' : 'outlined'}
              onClick={() => handleFilterChange('type', 'RENT')}
              sx={{ minWidth: 80 }}
            >
              НАЕМИ
            </Button>
          </Box>

          {/* Район */}
          <TextField
            label="Район"
            value={filters.location}
            onChange={(e) => handleFilterChange('location', e.target.value)}
            size="small"
            sx={{ minWidth: 150 }}
            placeholder="Въведете район"
          />

          {/* Тип на имота */}
          <FormControl size="small" sx={{ minWidth: 150 }}>
            <InputLabel>Тип на имота</InputLabel>
            <Select
              value={filters.propertyType}
              label="Тип на имота"
              onChange={(e) => handleFilterChange('propertyType', e.target.value)}
            >
              <MenuItem value="">Всички типове</MenuItem>
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

          {/* Цена */}
          <TextField
            label="Макс. цена"
            type="number"
            value={filters.maxPrice}
            onChange={(e) => handleFilterChange('maxPrice', e.target.value)}
            size="small"
            sx={{ minWidth: 120 }}
            placeholder="лв."
            InputProps={{
              endAdornment: <Typography variant="body2" color="text.secondary">лв.</Typography>
            }}
          />

          {/* Търси Button */}
          <Button
            variant="contained"
            onClick={handleSearch}
            sx={{ minWidth: 100, height: 40 }}
          >
            ТЪРСИ
          </Button>

          {/* Изчисти Button */}
          <Button
            variant="outlined"
            onClick={handleClearFilters}
            sx={{ minWidth: 100, height: 40 }}
          >
            ИЗЧИСТИ
          </Button>
        </Box>
      </Paper>

      {posts.length === 0 ? (
        <Box textAlign="center" py={8}>
          <Typography variant="h6" color="text.secondary">
            Няма намерени обяви
          </Typography>
        </Box>
      ) : (
        <>
          <ResponsiveGrid 
            sx={{ mb: 4 }}
            itemSizes={{ xs: 12, sm: 6, md: 4, lg: 3 }}
          >
            {posts.map((post) => (
              <AdCard
                key={post.postId}
                postId={post.postId}
                avatarInitials={getInitials(post.userId, post.agencyId)}
                adTitle={getAdTitle(post.type)}
                dateAdded={formatDate(post.postDate)}
                imageUrl={getImageUrl(post)}
                adPrice={formatPrice(post.price)}
                isPromoted={post.isPromoted}
              />
            ))}
          </ResponsiveGrid>

          {totalPages > 1 && (
            <Box display="flex" justifyContent="center" mt={4}>
              <Pagination
                count={totalPages}
                page={currentPage}
                onChange={handlePageChange}
                color="primary"
                size="large"
                showFirstButton
                showLastButton
              />
            </Box>
          )}
        </>
      )}
    </Container>
  );
};

export default AdsList;
