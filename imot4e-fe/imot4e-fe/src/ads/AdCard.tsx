import Card from '@mui/material/Card';
import CardHeader from '@mui/material/CardHeader';
import CardMedia from '@mui/material/CardMedia';
import CardContent from '@mui/material/CardContent';
import Avatar from '@mui/material/Avatar';
import Typography from '@mui/material/Typography';
import { red } from '@mui/material/colors';
import { useNavigate } from 'react-router-dom';
import { CardActionArea } from '@mui/material';

interface AdCardInfo {
  avatarInitials: string;
  adTitle: string;
  dateAdded: string;
  imageUrl: string;
  adPrice: string;
}

const AdCard = (props: AdCardInfo) => {
  const navigate = useNavigate();

  return (
    <Card sx={{ maxWidth: 345 }}>
      <CardActionArea onClick={() => navigate("/example-ad")}>
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
};

export default AdCard;