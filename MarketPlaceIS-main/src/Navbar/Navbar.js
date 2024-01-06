import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import axios from 'axios';


function NavBar() {
  const [searchQuery, setSearchQuery] = useState("");
  const navigate = useNavigate();

  const handleSearch = async () => {
    try {
      const response = await axios.get(`http://localhost:8080/items/search=${encodeURIComponent(searchQuery)}`);
      if (response.data) {
        console.log("Object found.");
        console.log(response.data);
        navigate(`/item/${response.data[0].id}`);
      } else {
        throw new Error("Object not found");
      }
    } catch (error) {
      console.error("Error searching:", error);
    }
  };

  return (
    <div className="nav-bar">
      <div className="search-bar">
        <input
          type="text"
          placeholder="Search"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
        <button onClick={handleSearch}>Search</button>
      </div>
    </div>
  );
};

export default NavBar;