import React from 'react';
import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import { red, green } from '@mui/material/colors';
import { useNavigate } from 'react-router-dom';
import { CardActionArea, Box, Chip } from '@mui/material';

interface AdCardInfo {
  postId: number;
  avatarInitials: string;
  adTitle: string;
  dateAdded: string;
  imageUrl: string;
  adPrice: string;
  isPromoted?: boolean;
}

const AdCard = React.memo((props: AdCardInfo) => {
  const navigate = useNavigate();

  return (
    <Card sx={{ maxWidth: 345, position: 'relative' }}>
      {props.isPromoted && (
        <Chip
          label="PROMO"
          color="primary"
          size="small"
          sx={{
            position: 'absolute',
            top: 8,
            right: 8,
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
      <CardActionArea onClick={() => navigate(`/ad/${props.postId}`)}>
        <CardHeader
          avatar={
            <Avatar sx={{ bgcolor: red[500] }} aria-label="recipe">
              {props.avatarInitials}
            </Avatar>
          }
          title={props.adTitle}
          subheader={props.dateAdded}
        />
        <CardMedia
          component="img"
          height="194"
          image={props.imageUrl}
          alt="обява"
        />
      </CardActionArea>
      <CardContent>
        <Typography variant="body2" color="text.secondary">
          {props.adPrice}
        </Typography>
      </CardContent>
    </Card>
  );
});

AdCard.displayName = 'AdCard';

export default AdCard;