import React from 'react'
import { Outlet } from 'react-router-dom'

export default function Layout() {
  return (
    <div className="container-fluid">
      <div className="row">
        header
      </div>
      <div className="row">
        <Outlet/>
      </div>
      <div className="row">
        footer
      </div>
    </div>
  )
}
