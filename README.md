# Spring Sklep Koszyk - React App

Projekt składa się z dwóch części: części backendowej napisanej w Spring Boot obsługującej operacje na zamówieniach i produktach, oraz części frontendowej napisanej w React.js dostarczającej interfejs użytkownika do zarządzania zamówieniami.

## Technologie

- **Backend:**
  - Java 17
  - Spring Boot 3.1.4
  - Hibernate
  - Maven

- **Frontend:**
  - React 18.2.0
  - Axios 1.6.0
  - Bootstrap 5.3.2
  - React Router Dom 6.18.0 
  - React Icons 4.12.0

## Uruchamianie projektu

1. **Backend:**

   - Otwórz projekt
   - Uruchom `SpringSklepKoszykApplication` w głównym pakiecie.

2. **Frontend:**

   - Przejdź do katalogu `react-app` w terminalu.
   - Uruchom `npm install` w celu zainstalowania wszystkich zależności.
   - Uruchom `npm start` aby uruchomić serwer deweloperski React.

   Aplikacja frontendowa będzie dostępna pod adresem http://localhost:3000.

## Backend API

- **Zamówienia:**
  - `GET /orders`: Pobiera listę wszystkich zamówień.
  - `GET /orders/{id}`: Pobiera szczegóły zamówienia o określonym ID.
  - `POST /orders/new-order/{productId}`: Tworzy nowe zamówienie lub dodaje produkt do istniejącego zamówienia.
  - `PUT /orders/{orderId}/increment/{productId}`: Zwiększa ilość produktu w zamówieniu o jeden.
  - `PUT /orders/{orderId}/decrement/{productId}`: Zmniejsza ilość produktu w zamówieniu o jeden.
  - `PUT /orders/{orderId}/add-product/{productId}`: Dodaje produkt do zamówienia lub zwiększa ilość, jeśli produkt już istnieje.
  - `PUT /orders/{orderId}/remove-product/{productId}`: Usuwa produkt z zamówienia.
  - `PUT /orders/{orderId}/change-quantity/{productId}/{newQuantity}`: Zmienia ilość produktu w zamówieniu.

- **Produkty:**
  - `GET /products`: Pobiera listę wszystkich produktów.
  - `GET /products/{id}`: Pobiera szczegóły produktu o określonym ID.
  - `POST /products/add-new-product`: Dodaje nowy produkt.
  - `PUT /products/{id}`: Aktualizuje nazwę i cenę produktu o określonym ID.
  - `DELETE /products/{id}`: Usuwa produkt o określonym ID.

Więcej szczegółów na temat dostępnych operacji znajdziesz w [ProductController](src/main/java/pl/edu/wszib/springsklepkoszyk/controller/ProductController.java) i [OrderController](src/main/java/pl/edu/wszib/springsklepkoszyk/controller/OrderController.java).

## Autor

**Karol Bazgier**
	karol5108@gmail.com
	kbazgier@student.wszib.edu.pl

