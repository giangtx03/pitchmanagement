import React from "react";

export default function Home() {
  return (
    <div className="d-flex justify-content-center align-items-center">
      <h1>Home</h1>
      <div className="dropdown">
        <button
          type="button"
          className="btn btn-primary dropdown-toggle"
          data-bs-toggle="dropdown"
        >
          Dropdown button
        </button>
        <ul className="dropdown-menu">
          <li>
            <a className="dropdown-item" href="#">
              Link 1
            </a>
          </li>
          <li>
            <a className="dropdown-item" href="#">
              Link 2
            </a>
          </li>
          <li>
            <a className="dropdown-item" href="#">
              Link 3
            </a>
          </li>
        </ul>
      </div>
    </div>
  );
}
