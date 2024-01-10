import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import axios from "axios";
import "./Item.css"; // Assuming your CSS file is named Item.css

function ItemPage() {
  const { id } = useParams();
  const [item, setItem] = useState(null);

  useEffect(() => {
    // Fetch item details
    axios
      .get(`http://localhost:8080/items/item/id=${id}`) // Assuming this endpoint returns an item
      .then((response) => {
        setItem(response.data);
      })
      .catch((error) => {
        console.error("Error fetching item:", error);
      });
  }, [id]);

  const handleAddToCart = () => {
    console.log(item.id);
    axios
      .post(`http://localhost:8080/cart/add-to-cart/${item.id}`)
      .then((response) => {
        console.log("Item added to cart successfully");
      })
      .catch((error) => {
        console.error("Error updating cart:", error);
      });
  };

  useEffect(() => {
    // Apply the class to the body element when the component mounts
    document.body.classList.add("body-gradient");

    // Clean up - remove the class when the component unmounts
    return () => {
      document.body.classList.remove("body-gradient");
    };
  }, []);

  if (!item) {
    return <div>Loading...</div>;
  }

  return (
    <div className="product-page">
      <div className="product-container">
        <div className="product-item" key={item.id}>
          <div className="item-details">
            <img
              src={item.poster}
              alt={item.name}
              style={{
                width: 200,
                height: 300,
              }}
            />
            <h3>{item.name}</h3>
            <p>Price: ${item.price}</p>
            <p>
              Availability: {item.cantity > 0 ? "In stock" : "Out of stock"}
            </p>
            <p>Category: {item.category}</p>
            <div>
              <button className="add-to-basket" onClick={handleAddToCart}>
                +
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ItemPage;
