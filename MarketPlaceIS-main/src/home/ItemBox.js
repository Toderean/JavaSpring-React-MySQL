import React from 'react';
import './ItemBox.css';
import './ItemBox.css';
import { Link } from "react-router-dom";

const ItemBox = ({ id, itemName, imageUrl, itemPrice, itemCategory }) => {
  return (
    <Link to={`/item/${id}`} className="ItemBox">
      <img
        src={imageUrl}
        alt={itemName}
        style={{
          width: 80,
          height: 80,
        }}
      />
      <div>
        <h3>{itemName}</h3>
        <p>Price: ${itemPrice}</p>
        <p>Category: {itemCategory}</p>
      </div>
    </Link>
  );
};

export default ItemBox;