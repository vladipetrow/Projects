import { Box, Typography } from "@mui/material";
import AdCard from "../ads/AdCard";
import ClusteredMap from "../maps/ClusteredMap";
import SearchFilters from "./SearchFilters";

const Homepage = () => {
  return (
    <Box>
      <SearchFilters />
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'row',
          justifyContent: 'space-around',
          p: 1,
          m: 1,
          bgcolor: 'background.paper',
          borderRadius: 1,
        }}
      >
        <ClusteredMap />
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            borderRadius: 1,
          }}
        >
          <Typography align="center" variant="h6" component="div" sx={{ flexGrow: 1 }}>
            Обяви на фокус
          </Typography>
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              justifyContent: 'space-around',
              gap: '1em',
              maxWidth: '30em',
              bgcolor: 'background.paper',
              borderRadius: 1,
            }}
          >
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'row',
                gap: '1em',
                maxWidth: '30em',
                maxHeight: '20em',
                bgcolor: 'background.paper',
                borderRadius: 1,
              }}
            >
              <AdCard avatarInitials={"T"} adTitle={"Двустаен"} dateAdded={"September 14, 2021"} imageUrl="https://images.pexels.com/photos/3288100/pexels-photo-3288100.png?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="570лв." />
              <AdCard avatarInitials={"S"} adTitle={"Къща"} dateAdded={"March 12, 2022"} imageUrl="https://images.pexels.com/photos/534151/pexels-photo-534151.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="720лв." />
            </Box>
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'row',
                gap: '1em',
                maxWidth: '30em',
                maxHeight: '20em',
                bgcolor: 'background.paper',
                borderRadius: 1,
              }}
            >
              <AdCard avatarInitials={"M"} adTitle={"Многостаен"} dateAdded={"January 1, 2022"} imageUrl="https://images.pexels.com/photos/259588/pexels-photo-259588.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="699лв."/>
              <AdCard avatarInitials={"V"} adTitle={"Пентхаус"} dateAdded={"September 7, 2020"} imageUrl="https://images.pexels.com/photos/1571463/pexels-photo-1571463.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="300 000лв."/>
            </Box>
          </Box>
        </Box>
      </Box>
    </Box>
  );
};

export default Homepage;