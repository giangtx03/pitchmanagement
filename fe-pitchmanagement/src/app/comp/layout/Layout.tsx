import React from 'react'
import { Outlet } from 'react-router-dom'
import Header from './Header'
import Footer from './Footer'

export default function Layout() {
  return (
    <div className="container">
      <div className="row">
        <Header/>
      </div>
      <div className="row">
        <Outlet/>
      </div>
      <div className="row">
        <Footer/>
      </div>
    </div>
  )
}
