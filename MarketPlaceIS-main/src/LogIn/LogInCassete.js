import React, { useState, useEffect } from 'react';
import './LogInCassete.css';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function LogInCassete({ onLogin, onBack }) {

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleLogin = async (event) => {
    event.preventDefault();

    // Make a POST request to the authentication endpoint
    try {
      const response = await axios.post('http://localhost:8080/users/login', {
        email: username, // Use 'email' instead of 'username'
        password,
      });

      // Assuming the backend returns a token upon successful login
      const authToken = response.data.token;
      console.log('Login successful! Token:', authToken);

      // You can save the token to the state, local storage, or a cookie for future requests
    } catch (error) {
      console.error('Login failed:', error.message);
      // Handle login failure (e.g., show an error message to the user)
    }
  };

 
  useEffect(() => {
    // Apply the class to the body element when the component mounts
    document.body.classList.add('body-login');

    // Clean up - remove the class when the component unmounts
    return () => {
      document.body.classList.remove('body-login');
    };
  }, []);



  return (
    <div>
      <div className="login-container">
        <form className="login-form" onSubmit={handleLogin}>
          <h2>Login</h2>
          <label htmlFor="username"></label>
          <input type="text"
            placeholder="Username"
            className="input-field" id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />

          <label htmlFor="password"></label>
          <input
            type="password"
            placeholder="Password"
            className="input-field"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button
            className="login-button"
            type="submit"
          >
            Login
          </button>
          <div className="signup-link">
            <p>Not a user yet? <a href="/signup">Just sign up</a></p>
          </div>
        </form>



      </div>
    </div>
  );

}

export default LogInCassete;