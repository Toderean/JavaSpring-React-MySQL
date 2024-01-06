import React, { useState, useEffect } from 'react';
import './Admin.css'; // Create or import your CSS file for styling
import axios from 'axios';

function Admin() {

  const [id, setId] = useState('');
  const [firstname, setFirstname] = useState('');
  const [lastname, setLastname] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [admin, setAdmin] = useState('');
  
  const [idI, setItems] = useState('');
  const [name, setName] = useState('');
  const [poster, setPost] = useState('');
  const [price, setPrice] = useState('');
  const [trailer, setTrailer] = useState('');
  const [cantity, setCantity] = useState('');
  const [category, setCategory] = useState('');
  


  const handleUserUpdate =async (event) => {
    event.preventDefault();
    try {
      const response = await axios.post(`http://localhost:8080/users/update/id=${id}`,{
          firstName:firstname,lastName:lastname, email,password,adminFlag:admin
      });
      console.log(response);
      const authToken = response.data.authToken;
      console.log('Update succesful ', authToken);
  } catch (error) {
      console.error('Update failed: ', error);  
  }
  };



  const handleItemUpdate = async (event) => {
    event.preventDefault();
    console.log(idI,name,cantity,category,price,poster,trailer);
    try {
          const response = await axios.post(`http://localhost:8080/items/act/id=${idI}`,{
          name:name,cantity:cantity, category:category,price: price,poster:poster,trailerLink:trailer
      });
      
      const authToken = response.data.authToken;
      console.log('Update Item succesful ', authToken);
  } catch (error) {
      console.error('Update Item failed: ', error);  
  }
  };

  const handleUserDelete = async (event) => {
    event.preventDefault();
    try {
      const response = await axios.delete(`http://localhost:8080/users/id=${id}`);
      console.log(response);
      console.log('Deleted User succesfully ');
  } catch (error) {
      console.error('Delete failed: ', error);  
  }
  };


  const handleItemDelete = async (event) => {
   // event.preventDefault();
    console.log(idI);
    try {
      const response = await axios.delete(`http://localhost:8080/admin/deleteItems=${idI}`);
      console.log(response);
      console.log('Deleted Item succesfully ');
  } catch (error) {
      console.error('Delete failed: ', error);  
  }
  };




  useEffect(() => {
    // Apply the class to the body element when the component mounts
    document.body.classList.add('body-admin');

    // Clean up - remove the class when the component unmounts
    return () => {
      document.body.classList.remove('body-admin');
    };
  }, []);

  return (
    <div className="admin-container">
      <div className="update-user-form">
        <form onSubmit={handleUserUpdate}>
          <h2>Update User</h2>
          <label htmlFor="id">ID:</label>
          <input type="number"
            id="id"
            value={id}
            onChange={(e) => setId(e.target.value)} />
          
          <label htmlFor="firstname">First Name:</label>
          <input type="text"
            id="firstname"
            value={firstname}
            onChange={(e) => setFirstname(e.target.value)} />

          <label htmlFor="lastname">Last Name:</label>
          <input type="text"
            id="lastname"
            value={lastname}
            onChange={(e) => setLastname(e.target.value)} />

          <label htmlFor="email">Email:</label>
          <input type="text"
            // id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)} />

          <label htmlFor="password">Password:</label>
          <input type="password"
            //id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)} />

          <label htmlFor="admin">Admin or not:</label>
          <input type="number"
            //id="admin"
            value={admin}
            onChange={(e) => setAdmin(e.target.value)} />

          <div className="buttons" >
            <button className="green-button" type="submit"> ^
              <i className="fas fa-arrow-up"></i>
            </button>
            <button className="red-button" onClick={handleUserDelete}>
              <i className="fas fa-times"></i>
            </button>
          </div>
        </form>
      </div>

      <div className="update-item-form">
        <form onSubmit={handleItemUpdate}>
          <h2>Update Item</h2>
          <label htmlFor="itemId">ID:</label>
          <input type="number" id="itemId" value={idI} onChange={(e) => setItems(e.target.value)} />
          
          <label htmlFor="Name">Name:</label>
          <input type="text" id="Name" value={name} onChange={ (e)=>setName(e.target.value)} />

          <label htmlFor="poster">Poster:</label>
          <input type="text" id="poster" value={poster} onChange={(e) => setPost(e.target.value)} />

          <label htmlFor="price">Price:</label>
          <input type="number" id="price" value={price} onChange={(e) => setPrice(e.target.value)} />

          <label htmlFor="trailer">Trailer:</label>
          <input type="text" id="trailer" value={trailer} onChange={(e) => setTrailer(e.target.value)} />
        
          <label htmlFor="cantity">Quantity:</label>
          <input type="number" id="cantity" value={cantity} onChange={(e) => setCantity(e.target.value)} />
        
          <label htmlFor="category">Category:</label>
          <input type="text" id="category" value={category} onChange={(e) => setCategory(e.target.value)} />

         

          <div className="buttons">
            <button className="green-button" type="submit"> ^
              <i className="fas fa-arrow-up"></i>
            </button>
            <button className="red-button"onClick={handleItemDelete}>
              <i className="fas fa-times"></i>
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default Admin;