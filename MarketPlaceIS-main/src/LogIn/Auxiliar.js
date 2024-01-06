// Import necessary dependencies
import React, { useState } from 'react';
import log from './Log.css'
import axios from 'axios';

function Login(){
  // State variables to hold username and password
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  // Function to handle form submission
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

  return (
    <div>
      <h2>Login</h2>
      <form onSubmit={handleLogin}>
        <div>
          <label htmlFor="username">Username:</label>
          <input
            type="text"
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div>
          <label htmlFor="password">Password:</label>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <div>
          <button type="submit">Login</button>
        </div>
      </form>
    </div>
  );
};

export default Login;