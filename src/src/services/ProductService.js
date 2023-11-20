import axios from 'axios'

const PRODUCT_API_URL = 'http://localhost:8080/products'

class ProductService{
    getAllProducts(){
        return axios.get(PRODUCT_API_URL)
    }
    deleteProduct(productId){
        return axios.delete(PRODUCT_API_URL + "/" + productId)
    }
    getProductById(productId){
        return axios.get(PRODUCT_API_URL + "/" + productId)
    }
    updateProduct(productId, product){
        return axios.put(PRODUCT_API_URL + "/" + productId, product)
    }
    addProduct(product){
        return axios.post(PRODUCT_API_URL + "/add-new-product", product)
    }

}

const ProductServiceInstance = new ProductService();
export default ProductServiceInstance;