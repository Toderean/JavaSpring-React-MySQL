import React, { useState, useEffect } from 'react';
import './SignUp.css';
import axios from 'axios';


function SignUp() {
  const [email, setEmail] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [password, setPassword] = useState('');

  const handleSignUp = async (event) => {
    event.preventDefault();
    console.log(email,firstName,lastName,password);
        try {
            const response = await axios.post('http://localhost:8080/users',{
            email:email,firstName:firstName,lastName:lastName,password:password,
            });
            const authToken = response.data.authToken;
            console.log('SignIn succesful ', authToken);
        } catch (error) {
            console.error('SignIn failed: ' , error);
        }
  };




  useEffect(() => {
    // Apply the class to the body element when the component mounts
    document.body.classList.add('body-signup');

    // Clean up - remove the class when the component unmounts
    return () => {
      document.body.classList.remove('body-signup');
    };
  }, []);



  return (
    <div >
      <div className="signup-container">

        <form className="signup-form" onSubmit={handleSignUp}>
          <h2>Sign Up</h2>

          <label htmlFor="email"> </label>
          <input
            type="text"
            id="email"
            placeholder="Email"
            className="input-field"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
          />

          <label htmlFor="firstName"> </label>
          <input
            type="text"
            placeholder="First Name"
            className="input-field"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
          />

          <label htmlFor="lastName"> </label>
          <input
            type="text"
            placeholder="Last Name"
            className="input-field"
            value={lastName}
            onChange={(e) => setLastName(e.target.value)}
          />

          <label htmlFor="password"> </label>
          <input
            type="password"
            placeholder="Password"
            className="input-field"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
          <button className="signup-button" type="submit">
            Sign Up
          </button>
          <div className="login-link">
            <p>Already a user? <a href="/login">Just Log in</a></p>
          </div>
        </form>
      </div>
    </div>
  );
}

export default SignUp;