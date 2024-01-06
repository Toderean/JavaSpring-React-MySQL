import React, { useState, useEffect } from "react";
import axios from 'axios';
import './Cos.css'; // Import your CartPage CSS file here

function CartPage() {
  const [cartItems, setCartItems] = useState([]);
  const [totalQuantity, setTotalQuantity] = useState(0); // State to store total quantity

  useEffect(() => {
    // Fetch items from API
    axios.get('http://localhost:8080/cart/get-items')
      .then(response => {
        const itemsWithQuantity = response.data.map(item => ({ ...item, quantity: 1 }));
        setCartItems(itemsWithQuantity);
        // setCartItems(response.data); // Update cart items with fetched data
      })
      .catch(error => {
        console.error('Error fetching data:', error);
      });
  }, []);

  const finalizeCommand = async (event) => {
    event.preventDefault();
    try{
        const response = await axios.post(`http://localhost:8080/cart/finalize-command`);
      const authToken = response.data.authToken;
      window.location.reload();
        console.log('Finalize command succesful: ', authToken);
    } catch(error) {
      console.error('Finalize command failed: ', error);
    }
  };

  useEffect(() => {
    // Apply the class to the body element when the component mounts
    document.body.classList.add('body-cos');

    // Clean up - remove the class when the component unmounts
    return () => {
      document.body.classList.remove('body-cos');
    };
  }, []);

  useEffect(() => {
    // Calculate total quantity
    const sum = cartItems.reduce((total, item) => total + item.quantity, 0);
    setTotalQuantity(sum);
  }, [cartItems]);

  const handleQuantityChange = (id, event) => {
    const updatedCartItems = cartItems.map(item => {
      if (item.id === id) {
        return { ...item, quantity: event.target.value };
      }
      return item;
    });
    setCartItems(updatedCartItems);
  };

  return (
    <div>
      <div className="header-container">
        <h1>Your Current Cart</h1>
      </div>

      <div className="cart-container">
        {cartItems.map((item) => (
          <div className="cart-item" key={item.id}>
            <div className="item-detailscos">
              <img src={item.poster} alt={item.name} className="item-image" />
              <div className="item-info">
                <span className="item-name">{item.name}</span>
                <span className="item-quantity">{1 /* Replace with item.quantity */}</span>
                <span className="item-price">${item.price}</span>
              </div>
            </div>
            {/* <div className="cassette"></div> */}
          </div>
        ))}

      </div>
      <div className="total-quantity">
        Total Quantity: {totalQuantity}
      </div>
        <form onSubmit = {finalizeCommand}>
      <div className="button-container">
          <button className="finalize-button">Finalizare Comanda</button>
        </div>
        </form>
    </div>
  );
}

export default CartPage;