import React, { useState, useEffect } from "react";
import axios from 'axios';
import ItemBox from './ItemBox';
import Home from './Home.css';
import NavBar from '../Navbar/Navbar';

function HomePage() {
  const [items, setItems] = useState([]);

  useEffect(() => {
    axios.get('http://localhost:8080/items/all/filter=0').then(response => {
      setItems(response.data);
    }).catch(error => {
      console.error('Error fetching data:', error);
    });
  }, []);

  useEffect(() => {
    // Apply the class to the body element when the component mounts
    document.body.classList.add('body-Home');

    // Clean up - remove the class when the component unmounts
    return () => {
      document.body.classList.remove('body-Home');
    };
  }, []);

  return (
    <div>
      <div className="home">

        <h1 className="title"> Altex De Buget </h1>
        <NavBar />
        <div className="item-container">
          {items.map((item, index) => (
            <ItemBox
              key={index}
              id={item.id}
              itemName={item.name}
              imageUrl={item.poster}
              itemPrice={item.price}
              itemCategory={item.category}
            />
          ))}
        </div>

        <div className="swatch" >
          <div>
            <p>ADMIN<a href="/users/admin">  GOTO</a></p>
          </div>
          <div></div>
          <div></div>
          <div>
            <p>CART<a href="/cartpage">  SHOW</a></p>
          </div>
          <div>
            <p>SIGNUP<a href="/signup">  GOTO</a></p>
          </div>
          <div>
            <p>LOGIN<a href="/login">  GOTO</a></p>
          </div>
        </div>
      </div>

    </div>



  );
}

export default HomePage;
