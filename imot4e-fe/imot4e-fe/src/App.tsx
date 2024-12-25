import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider, useAuth } from "./auth/AuthContext";
import Navbar from "./navbar/Navbar";
import LoggedInNavbar from "./navbar/LoggedInNavBar";
import Homepage from "./homepage/Homepage";
import Login from "./auth/Login";
import Register from "./auth/Register";
import CreateAd from "./ads/CreateAd";
import AgencySubscription from "./subscription/AgencySubscription";
import PasswordResetForm from "./auth/PasswordResetForm";

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppContent />
      </AuthProvider>
    </BrowserRouter>
  );
}

function AppContent() {
  const { isAuthenticated } = useAuth();

  return (
    <>
      {isAuthenticated ? <LoggedInNavbar /> : <Navbar />}
      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/password-reset" element={<PasswordResetForm />} />
        <Route path="/create-ad" element={<CreateAd />} />
        <Route path="/subscription/agency" element={<AgencySubscription />} />
      </Routes>
    </>
  );
}

export default App;
