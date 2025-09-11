import { Box, Card, CardContent, Typography } from "@mui/material"
import AdCard from "./AdCard";
import AdHeaderInformation from "./AdHeaderInformation";
import AdImageList from "./AdImageList";
import ConvenienceMap from "../maps/ConvenienceMap";

const AdListing = () => {
  return (
    <Card sx={{ minWidth: 275 }}>
      <CardContent>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            justifyContent: 'space-around',
            gap: '1em',
            p: 1,
            m: 1,
            bgcolor: 'background.paper',
            borderRadius: 1,
          }}
        >
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'row',
              justifyContent: 'space-evenly',
              gap: '1em',
              p: 1,
              m: 1,
              bgcolor: 'background.paper',
              borderRadius: 1,
            }}
          >
            <AdImageList />
            <AdHeaderInformation />
          </Box>
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'column',
              // justifyContent: 'space-evenly',
              maxWidth: '120em',
              gap: '1em',
              p: 5,
              m: 10,
              bgcolor: 'background.paper',
              borderRadius: 1,
            }}
          >
            <Typography variant="h5" component="div" gutterBottom>
              Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec gravida mattis orci, lacinia mollis est interdum in. Cras tempor est orci, non faucibus orci viverra non. Sed eget sem id magna ultrices laoreet. Curabitur bibendum leo vel nulla commodo lacinia. Nunc ut est scelerisque, blandit velit sed, gravida leo. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Praesent congue nunc vel lorem feugiat, vel condimentum urna scelerisque. Vivamus vitae purus magna.
            </Typography>
            <ConvenienceMap />
          </Box>
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'row',
              justifyContent: 'center',
              gap: '1em',
              maxHeight: '30em',
              p: 1,
              m: 1,
              bgcolor: 'background.paper',
              borderRadius: 1,
            }}
          >
            <Card sx={{ maxWidth: '100em' }}>
              <CardContent>
                <Box
                  sx={{
                    display: 'flex',
                    flexDirection: 'column',
                    gap: '1em',
                    p: 1,
                    m: 1,
                    bgcolor: 'background.paper',
                    borderRadius: 1,
                  }}
                >
                  <Typography variant="h5" component="div" gutterBottom>
                    Подобни обяви
                  </Typography>
                  <Box
                    sx={{
                      display: 'flex',
                      flexDirection: 'row',
                      gap: '1em',
                      p: 1,
                      m: 1,
                      bgcolor: 'background.paper',
                      borderRadius: 1,
                    }}
                  >
                    <AdCard postId={1} avatarInitials={"T"} adTitle={"Двустаен апартамент"} dateAdded={"September 14, 2021"} imageUrl="https://images.pexels.com/photos/3288100/pexels-photo-3288100.png?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="600лв."/>
                    <AdCard postId={2} avatarInitials={"S"} adTitle={"Тристаен апартамент"} dateAdded={"August 12, 2021"} imageUrl="https://images.pexels.com/photos/106399/pexels-photo-106399.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="550лв."/>
                    <AdCard postId={3} avatarInitials={"M"} adTitle={"Двустаен апартамент"} dateAdded={"January 16, 2022"} imageUrl="https://images.pexels.com/photos/87223/pexels-photo-87223.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="900лв."/>
                    <AdCard postId={4} avatarInitials={"V"} adTitle={"Многостаен апартамент"} dateAdded={"November 29, 2021"} imageUrl="https://images.pexels.com/photos/534151/pexels-photo-534151.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1" adPrice="200 000лв."/>
                  </Box>
                </Box>
              </CardContent>
            </Card>
          </Box>

        </Box>
      </CardContent>
    </Card>
  );
};

export default AdListing;