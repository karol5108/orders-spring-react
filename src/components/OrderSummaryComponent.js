import { useEffect, useState } from "react"
import OrderServiceInstance from "../services/OrderService"

import { useOrder } from '../contexts/OrderContext';

import Modal from 'react-bootstrap/Modal';

import {useNavigate} from 'react-router-dom'
import { FaAmazonPay, FaMinus, FaPlus, FaTrash, FaTrashAlt } from "react-icons/fa";

const OrderSummaryComponent = () =>{
   const { currentOrder, setOrder } = useOrder();
   const [createDate, setCreateDate] = useState('');
   const [modifyDate, setModifyDate] = useState('');
   const [lines, setLines] = useState([]);
   const [orderValue, setOrderValue] = useState(0);

   const [showModal, setShowModal] = useState(false);
   const [cancellationMessage, setCancellationMessage] = useState('');

   const [changeQuantity, setChangeQuantity] = useState(false);
  

   let navigate = useNavigate();

   useEffect(() => {
       // Funkcja pobierająca dane zamówienia z serwera
       const fetchOrderData = async () => {
           try {
               const response = await OrderServiceInstance.getOrderById(currentOrder.id);
               const orderData = response.data;
               setCreateDate(orderData.createDate);
               setModifyDate(orderData.modifyDate);
               setLines(orderData.lines);
               setOrderValue(orderData.orderValue);
           } catch (error) {
               console.error("Error fetching order data:", error);
           }
       };

       if (currentOrder) {
           fetchOrderData(); // Pobierz dane zamówienia tylko jeśli istnieje aktualne zamówienie
       }
   }, [currentOrder, setOrder]);


// konczenie zmaowienia

   const deleteOrder = (orderId) => {
    OrderServiceInstance.deleteOrder(orderId)
      .then((response) => {
        setOrder(null);
        localStorage.removeItem('currentOrder');
        setCancellationMessage(`ORDER ${orderId} CANCELED.`);
        setShowModal(true);
      })
      .catch((error) => {
        
        console.log('ERRORCANCELING ORDER', error);
        setCancellationMessage(`ERROR CANCELING ORDER: ${orderId}.`);
        setShowModal(true);
      });
  };

  const endOrder = (orderId) => {
        setOrder(null);
        localStorage.removeItem('currentOrder');
        setCancellationMessage(`ORDER  ${orderId} CONFIRMED.`);
        setShowModal(true);
  }

  const handleClose = () => {
    setShowModal(false);
    setCancellationMessage('');
    navigate("/")
  };

  useEffect(() => {
    if (showModal && cancellationMessage) {
      setShowModal(true);
    }
  }, [showModal, cancellationMessage]);

// inkrementacja/dekrementacja

const decrementProduct = (orderId, productId) => {
    OrderServiceInstance.decrementProduct(orderId, productId).then((response) =>{
      console.log(response)
      setOrder(response.data)
      if (currentOrder.lines.length === 1) {
        deleteOrder(orderId);
      }
    }).catch((error) =>{
      console.log(error)
    })
}

const incrementProduct = (orderId, productId) => {
    OrderServiceInstance.incrementProduct(orderId, productId).then((response) =>{
      console.log(response)
      setOrder(response.data)
    }).catch((error) =>{
      console.log(error)
    })
}

// actions produkt w zmaowieniu
const removeProduct = (orderId,productId) => {
  if (currentOrder.lines.length === 1) {
     deleteOrder(orderId);
  }else{

  OrderServiceInstance.removeProduct(orderId, productId).then((response) =>{
    console.log(response.data);
    setOrder(response.data)
  }).catch((error) => {
    console.log(error);
  });
  }
}

const toChangeQuantity = () =>{
  setChangeQuantity(true);
}

const handleToChangeQuantity = (orderId, productId, newQuantity) =>{
 
    
    if (currentOrder.lines.length === 1 && newQuantity < 1){
      deleteOrder(orderId);
      setChangeQuantity(false);
    }else{
      OrderServiceInstance.changeQuantity(orderId, productId, newQuantity).then((response) =>{
      setOrder(response.data);
      setChangeQuantity(false);  
    }).catch((error) => {
        console.log(error);
      })
    }
} 

const tdToChangeQuantity = (lineProductId) =>{
  if(changeQuantity === true){
    return  <input
              type="number"
              onChange={(e) =>
              handleToChangeQuantity(currentOrder.id, lineProductId, e.target.value)
              }
            />    
  }else{
    return <button className="btn btn-warning" onClick={() => toChangeQuantity()}>
            CHANGE QUANTITY
           </button>
          

  }
}


return(
    <div>
        <br/> <br/>
        <div className="container">
            <div className="form-group mb-2">
                <label className="form-label"> CREATE DATE: </label>
                <span className="read-only"> {createDate} </span>    
            </div>
            
            <div className="form-group mb-2">
                <label className="form-label"> MODIFY DATE: </label>
                <span className="read-only"> {modifyDate} </span>    
            </div>

            <div>
                <h3>Order  Items</h3>
                <table className="table table-bordered table-hover shadow">
                <thead>
                    <tr className="text-center">
                      <th>Product</th>
                      <th colSpan={3}>Quantity</th>
                      <th>Value</th>
                      <th colSpan={2}>ACTIONS</th>
                    </tr>
                </thead>
                <tbody className="text-center">
                    {lines.map((line, index) => (
                    <tr key={index}>
                        <td>{line.product.name}</td>
                        <th> 
                          <button className="btn btn-dark" onClick={() => decrementProduct(currentOrder.id, line.product.id)}>
                              <FaMinus />
                          </button>
                        </th>
                        <td>{line.quantity}</td>
                        <td> 
                          <button className="btn btn-dark" onClick={() => incrementProduct(currentOrder.id, line.product.id)}>
                              <FaPlus />
                          </button>
                        </td>
                        <td>{line.productValue}</td>

                        <td> 
                            {tdToChangeQuantity(line.product.id)}
                        </td> 

                        <td> 
                          <button className="btn btn-danger" onClick={() => removeProduct(currentOrder.id, line.product.id)}>
                              <FaTrash />
                          </button>
                        </td>  
                     </tr>
                    ))}
                </tbody>
                </table>
            </div>
            <tfoot>
                
                <td>
                    <strong>ORDER VALUE: </strong>
                </td>
                <td>
                    <span className="read-only" style={{marginLeft:"10px"}}>{orderValue}zł</span>
                </td>
                <td>
                    <button className="btn btn-info" onClick={ () => endOrder(currentOrder.id)} style={{marginLeft:"10px"}}> <FaAmazonPay />  </button>
                </td>
                <td>
                    <button className="btn btn-danger" onClick={() => deleteOrder (currentOrder.id)} style={{marginLeft:"10px"}}> <FaTrashAlt /> </button>
                </td>
      
            </tfoot>     
         </div>

        {showModal && (
        <div>
        <Modal show={showModal} onHide={handleClose}>
          <Modal.Header closeButton>
            <Modal.Title>Informacja o zamowieniu</Modal.Title>
          </Modal.Header>
          <Modal.Body>{cancellationMessage}</Modal.Body>
        </Modal>
        </div>
        )}  



    </div>
    
)
}
export default OrderSummaryComponent