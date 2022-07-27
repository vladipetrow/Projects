import { Avatar, Box, Divider, Typography } from "@mui/material";
import { red } from "@mui/material/colors";
import StaticMap from "../maps/StaticMap";

const AdTitle = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        maxWidth: '50em',
        // justifyContent: 'space-evenly',
        bgcolor: 'background.paper',
      }}
    >
      <Typography variant="h3" component="div" gutterBottom>
        Име на обява
      </Typography>
      <Typography variant="h6" component="div" gutterBottom>
        Добавена на September 14, 2021
      </Typography>
    </Box>
  );
};

const SellerInformation = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'row',
        gap: '1em',
        bgcolor: 'background.paper',
      }}
    >
      <Avatar sx={{ bgcolor: red[500] }} aria-label="recipe">
        Т
      </Avatar>
      <Typography variant="h6" component="div" gutterBottom>
        Владислав Петров
      </Typography>
    </Box>
  );
};

const AdHeaderInformation = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        // justifyContent: 'space-evenly',
        gap: '1em',
        p: 1,
        m: 1,
        bgcolor: 'background.paper',
        borderRadius: 1,
      }}
    >
      <AdTitle />
      <Divider />
      <Typography variant="h4" component="div" gutterBottom>
        200 000лв.
      </Typography>
      <StaticMap />
      <Box
        component="div"
        sx={{
          flexDirection: 'row',
          display: 'inline-flex',
          p: 0.2,
          m: 2,
          justifyContent: 'center',
          bgcolor: (theme) => (theme.palette.mode === 'dark' ? '#101010' : '#fff'),
          color: (theme) =>
            theme.palette.mode === 'dark' ? 'grey.300' : 'grey.800',
          border: '1px solid',
          borderColor: (theme) =>
            theme.palette.mode === 'dark' ? 'grey.800' : 'grey.300',
          borderRadius: 2,
          fontSize: '0.875rem',
          fontWeight: '700',
        }}
      >
        <Typography variant="h4" component="div" gutterBottom>
          +359886016176
        </Typography>
      </Box>
      <SellerInformation />
    </Box >
  );
};

export default AdHeaderInformation;