import { Box, Button, FormControl, Input, InputAdornment, InputLabel, Stack, styled, TextField, Typography } from "@mui/material"
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const PictureInput = styled('input')({
  display: 'none',
});

const CreateAd = () => {

  const navigate = useNavigate();
  const [location, setLocation] = useState('');
  const [price, setPrice] = useState(0);
  const [area, setArea] = useState(0);
  const [description, setDescription] = useState('');
 // const [type_of_apart_id, set_type_of_apart_id] = useState(0);
  const [isForSale, setIsForSale] = useState(true);

  const handleClick = (e: { preventDefault: () => void; }) => {
    e.preventDefault();
    const post = {
      'location': location,
      'price': price, 
      'area' : area, 
      'description'  : description,
      'type' : isForSale ? "BUY" : "RENT",
    }
    console.log(post);
    fetch("http://localhost:8080/post/add",{
    method: "POST",
    headers: {
      "Content-type" : "application/json" , 
      "Authorization" : localStorage.getItem("Authorization")!
    },
    body: JSON.stringify(post)
  }).then( () => navigate("/"));
    
  }

  return (
    <Box
      sx={{
        display: 'flex',
        justifyContent: 'center',
        bgcolor: 'background.paper',
      }}>
      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          width: '50%',
          maxWidth: '50em',
          gap: '1em',
          p: 1,
          m: 1,
          bgcolor: 'background.paper',
          borderRadius: 1,
        }}
      >
        <Typography align="center" variant="h5" gutterBottom component="div">
          Създай обява
        </Typography>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'row',
            width: '50%',
            maxWidth: '50em',
            bgcolor: 'background.paper',
            borderRadius: 1,
          }}
        >
          <Button variant={isForSale ? "contained" : "outlined"} size="large"  onClick={() => setIsForSale(true)}>Продажба</Button>
          <Button variant={!isForSale ? "contained" : "outlined"} size="large" onClick={() => setIsForSale(false)}>Под наем</Button>
        </Box>
        <TextField
          id="outlined-name"
          label="Местоположение"
          value={location}
          onChange = {(e) => setLocation(e.target.value)}
        />
        <TextField
          id="outlined-name"
          label="Описание"
          multiline
          minRows={4}
          maxRows={6}
          value={description}
          onChange = {(e) => setDescription(e.target.value)}
        />
        <FormControl fullWidth sx={{ m: 1 }} variant="standard">
          <InputLabel htmlFor="standard-adornment-amount">Цена</InputLabel>
          <Input
            id="standard-adornment-amount"
            value={price}
            onChange = {(e) => setPrice(parseInt(e.target.value))}
            type="number"
            startAdornment={<InputAdornment position="start">лв.</InputAdornment>}
          />
        </FormControl>
        <TextField
          id="outlined-name"
          label="Квадратура на имота"
          value={area}
          onChange = {(e) => setArea(parseInt(e.target.value))}
        />

        {/* <Stack direction="row" alignItems="center" spacing={2}>
          <label htmlFor="contained-button-file">
            <PictureInput accept="image/*" id="contained-button-file" multiple type="file" />
            <Button variant="contained" component="span">
              Добави снимки
            </Button>
          </label>
        </Stack> */}

        <Button variant="contained" onClick={handleClick }>Създай обява</Button>
      </Box>
    </Box>
  );
};

export default CreateAd;