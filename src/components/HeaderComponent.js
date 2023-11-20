import React from "react"
import { Link } from "react-router-dom";

const HeaderComponent = () => {


    return(
        <div>
            <header>
                <nav className="navbar navbar-expand-lg navbar-dark bg-dark mb-5">
                    <div className="container-fluid">
                        <a href= "http://localhost:3000/" className="navbar-brand text-center">
                            SHOP ABCD
                        </a>
                        <button
					        className="navbar-toggler"
					        type="button"
					        data-bs-toggle="collapse"
					        data-bs-target="#navbarNav"
					        aria-controls="navbarNav"
					        aria-expanded="false"
					        aria-label="Toggle navigation">
					<span className="navbar-toggler-icon"></span>
				        </button>

                        <div
					        className="collapse navbar-collapse"
					        id="navbarNav">
					        <ul className="navbar-nav">
						        <li className="nav-item">
							        <Link
								        className="nav-link active"
								        aria-current="page"
								        to={"/orders"}>
								            View All Orders
							        </Link>
						        </li>
						        <li className="nav-item">
							        <Link
								        className="nav-link"
								        to={"/add-product"}>
								            Add new Product
							        </Link>
						        </li>
					        </ul>
				        </div>
                    </div>
                </nav>
            </header>
        </div>
    )
}

export default HeaderComponent