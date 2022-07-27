import { Box, Button, FormControl, FormControlLabel, Link, Radio, RadioGroup, Stack, styled, TextField, Typography } from "@mui/material";
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { UserStatus} from "./UserType";
const Input = styled('input')({
  display: 'none',
});

const Register = () => { 

  const navigate = useNavigate();
  const [status, setUserStatus] = useState(UserStatus);
  const [isAgency, setIsAgency] = useState(false);
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [nameOfAgency, setNameOfAgency] = useState('');
  const [address, setAddress] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');

  const handleClick = (e: { preventDefault: () => void; }) => {
    e.preventDefault();
    const user = {
      'first_name': firstName,
      'last_name': lastName, 
      'email' : email, 
      'password'  : password
    }
    const agency = {
      'name_of_agency' : nameOfAgency,
      'address' : address,
      'phone_number' : phoneNumber,
      'email' : email,
      'password'  : password
    };
    if(!isAgency){
    console.log(user);
    fetch("http://localhost:8080/user/register",{
    method: "POST",
    headers: {"Content-type" : "application/json"},
    body: JSON.stringify(user)
  }).then( () => navigate("/"));
    }
    else {
      console.log(agency);
      fetch("http://localhost:8080/agency/register",{
      method: "POST",
      headers: {"Content-type" : "application/json"},
      body: JSON.stringify(agency)
    }).then( () => navigate("/"));
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
          justifyContent: 'center',
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
          Създай профил
        </Typography>
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
        {
          !isAgency ?
            (
              <>
                <TextField
                  id="outlined-name"
                  label="Име"
                  value={firstName}
                  onChange = {(e) => setFirstName(e.target.value)}
                />
                <TextField
                  id="outlined-name"
                  label="Фамилия"
                  value={lastName}
                  onChange = {(e) => setLastName(e.target.value)}
                />
                <TextField
                  id="outlined-name"
                  label="Имейл"
                  value={email}
                  onChange = {(e) => setEmail(e.target.value)}
                />
              </>
            ) :
            (
              <>
                <TextField
                  id="outlined-name"
                  label="Име на агенция"
                  value={nameOfAgency}
                  onChange = {(e) => setNameOfAgency(e.target.value)}
                />
                <TextField
                  id="outlined-name"
                  label="Адрес на офис"
                  value={address}
                  onChange = {(e) => setAddress(e.target.value)}
                />
                <TextField
                  id="outlined-name"
                  label="Телефон"
                  value = {phoneNumber}
                  onChange = {(e) => setPhoneNumber(e.target.value)}
                />
                <TextField
                  id="outlined-name"
                  label="Служебен имейл"
                  value = {email}
                  onChange = {(e) => setEmail(e.target.value)}
                />
              </>
            )
        }
        <TextField
          id="outlined-name"
          label="Парола"
          type="password"
          autoComplete="current-password"
          value={password}
          onChange = {(e) => setPassword(e.target.value)}
        />
        {/* <TextField
          id="outlined-name"
          label="Повтори Парола"
          type="password"
          autoComplete="current-password"
        /> */}
        <Button variant="contained"  onClick={handleClick}>Създай профил</Button>
        <Link href="/login" underline="hover">
          <Typography variant="body1" gutterBottom>Вече имаш профил?</Typography>
        </Link>
      </Box>
    </Box>
  );
};

export default Register;