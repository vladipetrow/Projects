import { Box } from "@mui/material";
import AgencySubscription from "./AgencySubscription";
//import EmailSubscription from "./EmailSubscription";

const Subscription = () => {
  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        flexDirection: 'column',
        bgcolor: 'background.paper',
      }}>
       
        <AgencySubscription />
      </Box>
  );
};

export default Subscription;