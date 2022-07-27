import { Box, Pagination } from "@mui/material";
import AdCard from "../ads/AdCard";
import SearchFilters from "../homepage/SearchFilters";

const SearchResults = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
        bgcolor: 'background.paper',
      }}>
      <SearchFilters />
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'space-around',
          gap: '1em',
          bgcolor: 'background.paper',
          borderRadius: 1,
        }}
      >
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'space-around',
            gap: '1em',
            maxHeight: '20em',
            p: 2,
            m: 3,
            bgcolor: 'background.paper',
            borderRadius: 1,
          }}
        >
          <AdCard avatarInitials={"T"} adTitle={"Къща"} dateAdded={"January 11, 2022"} imageUrl="https://images.pexels.com/photos/3288100/pexels-photo-3288100.png?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="1200лв."/>
          <AdCard avatarInitials={"S"} adTitle={"Многостаен апартамент"} dateAdded={"February 12, 2022"} imageUrl="https://images.pexels.com/photos/164522/pexels-photo-164522.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="700лв."/>
          <AdCard avatarInitials={"M"} adTitle={"Тристаен апартамент"} dateAdded={"March 13, 2022"} imageUrl="https://images.pexels.com/photos/280222/pexels-photo-280222.jpeg?cs=srgb&dl=pexels-pixabay-280222.jpg&fm=jpg" adPrice="230 440лв."/>
          <AdCard avatarInitials={"V"} adTitle={"Двустаен апартамент"} dateAdded={"April 14, 2022"} imageUrl="https://images.pexels.com/photos/2343465/pexels-photo-2343465.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="760лв."/>
        </Box>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'row',
            justifyContent: 'space-around',
            gap: '1em',
            p: 2,
            m: 3,
            maxHeight: '20em',
            bgcolor: 'background.paper',
            borderRadius: 1,
          }}
        >
          <AdCard avatarInitials={"T"} adTitle={"Двустаен апартамент"} dateAdded={"June 1, 2022"} imageUrl="https://images.pexels.com/photos/276554/pexels-photo-276554.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="600лв."/>
          <AdCard avatarInitials={"S"} adTitle={"Многостаен апартамент"} dateAdded={"July 5, 2021"} imageUrl="https://images.pexels.com/photos/280232/pexels-photo-280232.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="1600лв."/>
          <AdCard avatarInitials={"M"} adTitle={"Пентхаус апартамент"} dateAdded={"August 7, 2021"} imageUrl="https://images.pexels.com/photos/3623770/pexels-photo-3623770.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"adPrice="780лв."/>
          <AdCard avatarInitials={"V"} adTitle={"Двустаен апартамент"} dateAdded={"September 23, 2021"} imageUrl="https://images.pexels.com/photos/1330753/pexels-photo-1330753.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="1200лв."/>
        </Box>
      </Box>
      <Box sx={{
        display: 'flex',
        justifyContent: 'center',
        p: 1,
        m: 1,
        bgcolor: 'background.paper',
      }}>
        <Pagination count={1} shape="rounded" />
      </Box>
    </Box>
  );
};

export default SearchResults;