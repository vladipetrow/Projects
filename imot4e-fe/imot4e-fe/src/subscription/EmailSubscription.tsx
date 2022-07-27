// import { Box, Button, TextField, Typography } from "@mui/material"
 import { useState } from "react";

// const EmailSubscription = () => {
//   const [isForSale, setIsForSale] = useState(true);

//   return (
//     <Box
//       sx={{
//         display: 'flex',
//         justifyContent: 'center',
//         bgcolor: 'background.paper',
//       }}>
//       <Box
//         sx={{
//           display: 'flex',
//           flexDirection: 'column',
//           maxWidth: '30em',
//           gap: '1em',
//           p: 1,
//           m: 2,
//           bgcolor: 'background.paper',
//           borderRadius: 1,
//         }}
//       >
//         <Typography align="center" variant="h5" gutterBottom component="div">
//           Възполвай се от ежедневен имейл бюлетин
//         </Typography>
//         <Box
//           sx={{
//             display: 'flex',
//             flexDirection: 'row',
//             width: '25%',
//             maxWidth: '50em',
//             bgcolor: 'background.paper',
//             borderRadius: 1,
//           }}
//         >
//           <Button variant={isForSale ? "contained" : "outlined"} onClick={() => setIsForSale(true)}>Купи</Button>
//           <Button variant={!isForSale ? "contained" : "outlined"} onClick={() => setIsForSale(false)}>Наеми</Button>
//         </Box>
//         <TextField
//           id="outlined-name"
//           label="Район"
//         />
//         <TextField
//           id="outlined-name"
//           label="Бюджет"
//         />
//         <Button variant="contained" color="success">Абонирай се за ежедневни имейл известия</Button>
//       </Box>
//     </Box>

//   );
// };

// export default EmailSubscription;