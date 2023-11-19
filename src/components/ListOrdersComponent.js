import { useEffect, useState } from "react"
import OrderServiceInstance from "../services/OrderService";
import { useNavigate } from "react-router-dom";

const ListOrdersComponent = () =>{
    const [orders, setOrders] = useState([]);
    let navigate = useNavigate();

    //pokaz wszystkie
    useEffect(() =>{
        getAllOrders();
    }, []);

    // order hooks
    const getAllOrders = () =>{
        OrderServiceInstance.getAllOrders().then((response) =>{
            setOrders(response.data);
            console.log(response.data);
        }).catch((error) =>{
            console.log(error);
        });
    }
      //przejscie do podgladu zamowienia
    const handleRowClick = (orderId) =>{
    navigate(`/order-summary/${orderId}`)
    }


    return(
        <div className="container">
            <h2 className="text-center"> ORDERS </h2>
            <table className="table table-bordered table-hover shadow">
                <thead>
                    <tr>
                    <th> ID </th>
                    <th> VALUE</th>
                    <th> MODIFY DATE</th>
                    </tr>
                </thead>
                <tbody>
                    {orders.map((order) =>(
                        <tr key={order.id} onClick={() => handleRowClick(order.id)}>
                            <td> {order.id} </td>
                            <td> {order.orderValue} </td>
                            <td> {order.modifyDate} </td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )

}
export default ListOrdersComponent;