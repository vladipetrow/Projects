import { Box, Button, TextField } from "@mui/material"
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const SearchFilters = () => {
  const [isForSale, setIsForSale] = useState(true);
  const navigate = useNavigate();

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'row',
        width: '50%',
        maxWidth: '50em',
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
          width: '25%',
          maxWidth: '50em',
          bgcolor: 'background.paper',
          borderRadius: 1,
        }}
      >
        <Button variant={isForSale ? "contained" : "outlined"} onClick={() => setIsForSale(true)}>Купи</Button>
        <Button variant={!isForSale ? "contained" : "outlined"} onClick={() => setIsForSale(false)}>Наеми</Button>
      </Box>
      <TextField
        label="Район"
      />
      <TextField
        label="Тип на имота"
      />
      <TextField
        label="Цена"
      />
      <Button variant="contained" onClick={() => navigate("/search")}>Търси</Button>
    </Box>
  );
};

export default SearchFilters;