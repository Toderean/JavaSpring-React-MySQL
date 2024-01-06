import './App.css';
import HomePage from './home/HomePage';
import LogInCassete from './LogIn/LogInCassete'
import React, { useState } from 'react';
import { Link, Route, Routes, useParams } from "react-router-dom";
import SignUp from './SignUp/SignUp';
import ItemPage from './Produse/ItemPage';
import CartPage from './CosBucket/Cos';
import Admin from './Admin/Admin';

function App() {

  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [undemerge, setUndemerge] = useState("/login");

  const handleLogin = () => {
    setIsLoggedIn(true);
  };

  const handleLogout = () => {
    setIsLoggedIn(false);
  };

  return (
    <div className="App">
      <Routes>
        <Route path="/:page" element={<PageRouter onLogin={handleLogin} onLogout={handleLogout} />} />
        <Route path="/" element={<HomePage />} />
        <Route path="/item/:id" element={<ItemPage />} />
        <Route path="/cartpage" element={<CartPage />} />
        <Route path="/users/admin" element={<Admin />} />
      </Routes>
    </div>
  );
}

function PageRouter({ onLogin, onLogout }) {
  let { page } = useParams();

  const handleLogin = () => {
    onLogin();
  };

  const handleLogout = () => {
    onLogout();
  };

  switch (page) {
    case 'home':
      return <HomePage />;
    case 'login':
      return <LogInCassete onLogin={handleLogin} onBackClick={handleLogout} />;
    case 'signup':
      return <SignUp />;

    default:
      return <HomePage />;
  }
}


export default App;
