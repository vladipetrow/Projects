import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./auth/AuthContext";
import { ToastProvider } from "./contexts/ToastContext";
import SimpleNavbar from "./navbar/SimpleNavbar";
import Homepage from "./homepage/Homepage";
import Login from "./auth/Login";
import Register from "./auth/Register";
import CreateAd from "./ads/CreateAd";
import AdsList from "./ads/AdsList";
import AdDetail from "./ads/AdDetail";
import EditAd from "./ads/EditAd";
import Subscription from "./subscription/Subscription";
import Dashboard from "./dashboard/Dashboard";
import AgencySubscription from "./subscription/AgencySubscription";
import PasswordResetForm from "./auth/PasswordResetForm";
import NotFound from "./NotFound";

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <ToastProvider>
          <AppContent />
        </ToastProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}

function AppContent() {
  return (
    <>
      <SimpleNavbar />
      <Routes>
        <Route path="/" element={<Homepage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/password-reset" element={<PasswordResetForm />} />
        <Route path="/create-ad" element={<CreateAd />} />
        <Route path="/ads" element={<AdsList />} />
        <Route path="/ad/:id" element={<AdDetail />} />
        <Route path="/edit-ad/:id" element={<EditAd />} />
        <Route path="/dashboard" element={<Dashboard />} />
        <Route path="/subscription" element={<Subscription />} />
        <Route path="/subscription/agency" element={<AgencySubscription />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </>
  );
}

export default App;
