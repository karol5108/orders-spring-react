import {useParams } from "react-router-dom";
import { useState, useEffect } from "react";
import OrderServiceInstance from "../services/OrderService";


const OrderViewComponent = () => {
    const {id} = useParams();
    const [createDate, setCreateDate] = useState('');
    const [modifyDate, setModifyDate] = useState('');
    const [lines, setLines] = useState([]);
    const [orderValue, setOrderValue] = useState(0);



    // podglad istniejacego zamowienia
useEffect(() =>{
    if(id){
    OrderServiceInstance.getOrderById(id).then((response) =>{
      const orderData = response.data;
      setCreateDate(orderData.createDate);
      setModifyDate(orderData.modifyDate);
      setLines(orderData.lines);
      setOrderValue(orderData.orderValue);
      
    }).catch((error) =>{
      console.log(error)
    })
    }
  }, [id])


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
            <h3>Order {id} Items</h3>
            <table className="table table-bordered table-hover shadow">
            <thead>
                <tr className="text-center">
                  <th>Product</th>
                  <th>Quantity</th>
                  <th>Value</th>
                </tr>
            </thead>
            <tbody className="text-center">
                {lines.map((line) => (
                <tr key={line.id}>
                    <td>{line.product.name}</td>
                    <td>{line.quantity}</td>
                    <td>{line.productValue}</td>
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
  
        </tfoot>     
    </div>
    </div>
  )
}
export default OrderViewComponent