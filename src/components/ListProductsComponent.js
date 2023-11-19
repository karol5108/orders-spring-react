import React, { useEffect, useState } from 'react';
import ProductServiceInstance from '../services/ProductService';
import OrderServiceInstance from '../services/OrderService';
import {Link} from 'react-router-dom'
import { useOrder } from '../contexts/OrderContext';

import Modal from 'react-bootstrap/Modal';
import {FaEdit, FaMinus, FaPlus, FaShoppingCart, FaTrashAlt} from "react-icons/fa"


const ListProductsComponent = () => {
  const [products, setProducts] = useState([]);
 // const [order, setOrder] = useState(null);
 const {currentOrder, setOrder } = useOrder();

 const [showModal, setShowModal] = useState(false);
  const [cancellationMessage, setCancellationMessage] = useState('');

// pokaz wszysktie
  useEffect(() => {
    getAllProducts();
  }, []);

  
// product hooks

  const getAllProducts = () => {
    ProductServiceInstance.getAllProducts()
      .then((response) => {
        setProducts(response.data);
        console.log(response.data);
      })
      .catch((error) => {
        console.log(error);
      });
  }

  const deleteProduct = (productId) => {
    ProductServiceInstance.deleteProduct(productId).then((response) =>{
      getAllProducts();
    }).catch((error) => {
      console.log(error);
    })
  }

// order hooks

  const createOrder = (productId) => {
    // tworzenie nowego jesli nie ma order w pamieci
    if(!currentOrder){
      OrderServiceInstance.newOrder(productId).then((response) =>{
        setOrder(response.data)
        console.log(response.data);
      }).catch((error) => {
        console.log(error);
      });
    // dodanie do istniejacego zamowienia
    } else{
      OrderServiceInstance.addToOrder(currentOrder.id, productId).then((response) =>{
        setOrder(response.data)
        console.log(response.data);
      }).catch((error) => {
        console.log(error);
      });
    }

  };

  const deleteOrder = (orderId) => {
    OrderServiceInstance.deleteOrder(orderId)
      .then((response) => {
        setOrder(null);
        localStorage.removeItem('currentOrder');
        setCancellationMessage(`ORDER ${orderId} CANCELED.`);
        setShowModal(true);
        getAllProducts();
      })
      .catch((error) => {
        setOrder(null);
        localStorage.removeItem('currentOrder');
        console.log('ERROR CANCELING ORDER:', error);
        setCancellationMessage(`ERROR CANCELING ORDER:  ${orderId}.`);
        setShowModal(true);
      });
  };

  const removeProduct = (orderId,productId) => {
    OrderServiceInstance.removeProduct(orderId, productId).then((response) =>{
      console.log(response.data);
      setOrder(response.data)
    }).catch((error) => {
      console.log(error);
    });
  }
  const isProductInCurrentOrder = (productId) => {
    return currentOrder && currentOrder.lines.some(line => line.product.id === productId);
  };

// okno dialogowe
  const handleClose = () => {
    setShowModal(false);
    setCancellationMessage('');
  };

  useEffect(() => {
    if (showModal && cancellationMessage) {
      setShowModal(true);
    }
  }, [showModal, cancellationMessage]);
  


  return (
    <div className="container">
      <h2 className="text-center">SHOP - PRODUCTS</h2>

      <table className="table table-bordered table-striped table-hover shadow">
      <thead>
        <tr className='text-center'>
            <th>ID</th>
            <th>PRODUCT</th>
            <th>PRICE</th>
            <th>ACTIONS ORDER</th>
            <th colSpan={2}>ACTIONS PRODUCT</th>
        </tr>
      </thead>
      <tbody>
          {products.map((product) => (
          <tr key={product.id} className='text-center'>
            <td>{product.id}</td>
            <td>{product.name}</td>
            <td>{product.price}</td>
            <td>

              <button className="btn btn-success shadow" onClick={() => createOrder(product.id)}> <FaPlus /> </button>
              
              {isProductInCurrentOrder(product.id) &&
                <button className='btn btn-danger shadow' onClick={() => removeProduct(currentOrder.id, product.id)} style={{marginLeft:"10px"}}> <FaMinus /> </button>
              }
            
            </td>
            <td>

              <Link to={`/edit-product/${product.id}`} className='btn btn-primary shadow'> <FaEdit /> </Link>

              <button className='btn btn-danger shadow' onClick={() => deleteProduct(product.id)} style={{marginLeft:"10px"}}> <FaTrashAlt /> </button>

            </td>
          </tr>
        ))}
      </tbody>
      </table>
      {currentOrder && (
        <div>

        <Link className="btn btn-info shadow" to= {`/order-summary`}> <FaShoppingCart /> </Link>

        <button className="btn btn-danger shadow" onClick={() => deleteOrder(currentOrder.id)} style={{marginLeft:"10px"}}>  <FaTrashAlt /> </button>

        <p>Order Value: {currentOrder.orderValue}zł</p>
        </div>
        
      )}    
        {showModal && (
        <div>
        <Modal show={showModal} onHide={handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>ORDER INFORMATION</Modal.Title>
          </Modal.Header>
          <Modal.Body>{cancellationMessage}</Modal.Body>
          
        </Modal>
      </div>
      )}       
    </div>
  );
};

export default ListProductsComponent;