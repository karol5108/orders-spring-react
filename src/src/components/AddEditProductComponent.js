import React, { useEffect, useState } from 'react'
import {useNavigate, Link, useParams} from 'react-router-dom'
import ProductServiceInstance from '../services/ProductService';
import { FaCheck, FaTimes } from 'react-icons/fa';

const AddEditProductComponent = () => {
    const {id} = useParams();
    const [name, setName] = useState('');
    const [price, setPrice] = useState(0);

    let navigate = useNavigate();

    const AddOrEdit = (e) => {
        e.preventDefault();

        const product = {name, price}

        if(id){
            ProductServiceInstance.updateProduct(id, product).then((response) => {
                console.log(response.data);
                navigate("/");
            }).catch(error =>{
                console.log(error)
            })
        }else{
            ProductServiceInstance.addProduct(product).then((response) =>{
                console.log(response.data);
                navigate("/");
            }).catch(error =>{
                console.log(error)
            })
        }
    }

    
    useEffect(() => {
        if(id)
        ProductServiceInstance.getProductById(id).then((response) => {
            setName(response.data.name);
            setPrice(response.data.price)
        }).catch((error) => {
            console.log(error)
        })
    }, [id])
    

    const h2 = () =>{
        if(id){
            return <h2 className='text-center'> UPDATE PRODUCT </h2>
        }
        else{
            return <h2 className='text-center>'> ADD PRODUCT </h2>
        }
    } 

    return(
        <div>
            <br /> <br />
            <div className='container'>
                <div className='row'>
                    <div className='card col-md-6 offset-md-3 shadow'>
                        {h2()}
                        <div className='card-body'>
                            <form>
                                <div className='form-group mb-2 '>
                                    <label className='form-label'> Name: </label>
                                    <input
                                        type = 'text'
                                        placeholder = 'ENTER PRODUCT NAME'
                                        name = 'name'
                                        className = 'form-control'
                                        value = {name}
                                        onChange = {(e) => setName(e.target.value)}
                                    >
                                    </input> 
                                </div>

                                <div className='form-group mb-2'>
                                    <label className='form-label'> Price: </label>
                                    <input
                                        type = 'number'
                                        placeholder = 'ENTER PRODUCT PRICE'
                                        name = 'price'
                                        className = 'form-control'
                                        value = {price}
                                        onChange = {(e) => setPrice(e.target.value)}
                                    >
                                    </input> 
                                </div>
 
                                <button className='btn btn-success shadow text-center' onClick={ (e) => AddOrEdit(e)}> <FaCheck /> </button>
                                <Link to="/" className='btn btn-danger shadow' style={{marginLeft:"10px"}}> <FaTimes /> </Link>

                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
   
}
export default AddEditProductComponent