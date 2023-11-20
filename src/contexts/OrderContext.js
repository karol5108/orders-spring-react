import { createContext, useContext, useState } from 'react';
import React, { useEffect } from 'react';

export const OrderContext = createContext(); // Definiowanie kontekstu na poziomie modułu

export const OrderProvider = ({ children }) => {
  const [currentOrder, setCurrentOrder] = useState(null);

  const setOrder = (order) => {
    setCurrentOrder(order);
    localStorage.setItem('currentOrder', JSON.stringify(order));
  };

  useEffect(() => {
    const storedOrder = localStorage.getItem('currentOrder');
    if (storedOrder) {
      setCurrentOrder(JSON.parse(storedOrder));
    }
  }, []);

  return (
    <OrderContext.Provider value={{ currentOrder, setOrder }}>
      {children}
    </OrderContext.Provider>
  );
};

export const useOrder = () => {
  const context = useContext(OrderContext);

  if (!context) {
    throw new Error('useOrder must be used within an OrderProvider');
  }

  return context;
};