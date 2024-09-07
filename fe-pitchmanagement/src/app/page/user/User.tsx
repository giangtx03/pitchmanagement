import React from 'react'
import SideBar from './comp/SideBar'
import { Outlet } from 'react-router-dom'

export default function User() {
  return (
    <>
        <div className="col-3">
            <SideBar/>
        </div>
        <div className="col-9">
            <Outlet/>
        </div>
    </>
  )
}
