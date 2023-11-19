//style css
import "../node_modules/bootstrap/dist/css/bootstrap.min.css"
import "../node_modules/bootstrap/dist/js/bootstrap.min.js"

import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import HeaderComponent from './components/HeaderComponent';
import ListProductsComponent from './components/ListProductsComponent';
import OrderSummaryComponent from './components/OrderSummaryComponent';
import AddEditProductComponent from './components/AddEditProductComponent.js';
import { OrderProvider } from "./contexts/OrderContext.js";
import ListOrdersComponent from "./components/ListOrdersComponent.js";
import OrderViewComponent from "./components/OrderViewComponent.js";

function App() {
  return (
   <OrderProvider>
   <Router>
      <div>
        <HeaderComponent/>
        <Routes>
            <Route exac path = "" element={ < ListProductsComponent />} />
            <Route path = "/order-summary" element= { <OrderSummaryComponent />} />
            <Route path = "/order-summary/:id" element= { <OrderViewComponent />} />
            <Route path = "/add-product" element= {<AddEditProductComponent />} />
            <Route path = "/edit-product/:id" element= {<AddEditProductComponent />} />
            <Route path = "/orders" element= {<ListOrdersComponent />} /> 
        </Routes>
      </div>
   </Router>
   </OrderProvider>
  );
}

export default App;
