import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Navbar from './navbar/Navbar';
import Register from './auth/Register';
import Login from './auth/Login';
import PasswordResetRequest from './auth/PasswordResetRequest';
import PasswordResetForm from './auth/PasswordResetForm';
import Homepage from './homepage/Homepage';
import CreateAd from './ads/CreateAd';
import SearchResults from './search/SearchResults';
import AdListing from './ads/AdListing';
//import EmailSubscription from './subscription/EmailSubscription';
import AgencySubscription from './subscription/AgencySubscription';

function App() {
  return (
    <BrowserRouter>
    <Navbar />
    <Routes>
      <Route path="/" element={<Homepage />} />
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
      <Route path="/forgotten-password" element={<PasswordResetRequest />} />
      <Route path="/forgotten-password-form" element={<PasswordResetForm />} />
      <Route path="/create-ad" element={<CreateAd />} />
      <Route path="/subscription/agency" element={<AgencySubscription />} />
      <Route path="/search" element={<SearchResults />} />
      <Route path="/example-ad" element={<AdListing />} />
    </Routes>
  </BrowserRouter>
  );
}

export default App;
