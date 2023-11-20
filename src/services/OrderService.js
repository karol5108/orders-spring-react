import axios from "axios";

const ORDER_API_URL = 'http://localhost:8080/orders'

class OrderService{
    getAllOrders(){
        return axios.get(ORDER_API_URL)
    }
    newOrder(productId){
        return axios.post(ORDER_API_URL + '/new-order/' + productId)
    }
    getOrderById(orderId){
        return axios.get(ORDER_API_URL + '/' + orderId)
    }
    addToOrder(orderId, productId){
        return axios.put(ORDER_API_URL + '/' + orderId + '/add-product/' + productId )
    }
    deleteOrder(orderId){
        return axios.delete(ORDER_API_URL + '/' + orderId)
    }
    removeProduct(orderId, productId){
        return axios.put(ORDER_API_URL + '/' + orderId + "/remove-product/" + productId )
    }
    decrementProduct(orderId, productId){
        return axios.put(ORDER_API_URL + "/" + orderId + "/decrement/" + productId)
    }
    incrementProduct(orderId, productId){
        return axios.put(ORDER_API_URL + "/" + orderId + "/increment/" + productId)
    }
    changeQuantity(orderId, productId, newQuantity){
        return axios.put(ORDER_API_URL + "/" + orderId + "/change-quantity/" + productId + "/" + newQuantity )
    }
}
const OrderServiceInstance = new OrderService();
export default OrderServiceInstance;