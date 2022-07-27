import { Box, Button, Checkbox, FormControl, FormControlLabel, FormGroup, Radio, Link, RadioGroup, TextField, Typography } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

const Login = () => {
  
  const navigate = useNavigate();
  const [isAgency, setIsAgency] = useState(false);
  const[email, setEmail] = useState('');
  const[password, setPassword] = useState(''); 
  
  const handleClick = (e: { preventDefault: () => void; }) => {

    const user = {
      'email' : email,
      'password' : password};

      if( !isAgency ){ 
      console.log(user);
      fetch("http://localhost:8080/user/login",{
      method:"POST",
      headers: {"Content-type" : "application/json"},
      body: JSON.stringify(user)
    })
    .then((response) => response.json())
    .then((data) => {
      localStorage.setItem("Authorization", data.token);
      console.log(data);
    }).then((data) => {
      if (localStorage.getItem("Authorization") !== null) {
        navigate( "/");
      } else {
        navigate("http://localhost:3000/login");
      }
    });
  }
    else {
      console.log(user);
      fetch("http://localhost:8080/agency/login",{
        method:"POST",
        headers: {"Content-type" : "application/json"},
        body: JSON.stringify(user)
      })
      .then((response) => response.json())
      .then((data) => {
        localStorage.setItem("Authorization", data.token);
        console.log(data);
      }).then((data) => {
        if (localStorage.getItem("Authorization") !== null) {
          navigate( "/");
        } else {
          navigate("http://localhost:3000/login");
        }
      });
    }
   
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
          p: 2,
          m: 1,
          bgcolor: 'background.paper',
          borderRadius: 1,
        }}
      >
        <Typography align="center" variant="h5" gutterBottom component="div">
          Влез в профила
        </Typography>
        <TextField
          id="outlined-name"
          label="Имейл"
          value={email}
          onChange = {(e) => setEmail(e.target.value)}
        />
        <TextField
          id="outlined-name"
          label="Парола"
          type="password"
          autoComplete="current-password"
          value={password}
          onChange = {(e) => setPassword(e.target.value)}
        />
        <FormControl>
          <RadioGroup
            row
            defaultValue="individual"
            name="radio-buttons-group"
          >
            <FormControlLabel value="individual" control={<Radio onClick={() => setIsAgency(false)} />} label="Частно лице" />
            <FormControlLabel value="agency" control={<Radio onClick={() => setIsAgency(true)} />} label="Агенция" />
          </RadioGroup>
        </FormControl>
  
        <Button variant="contained" onClick={handleClick}>Влез</Button>
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'row',
            width: '50%',
            maxWidth: '50em',
            gap: '1em',
            p: 0.2,
            m: 0.2,
            bgcolor: 'background.paper',
            borderRadius: 1,
          }}
        >
          <Link href="/register" underline="hover">
            <Typography variant="body1" gutterBottom>Създай профил</Typography>
          </Link>
        </Box>
      </Box>
    </Box>
  );
};

export default Login;