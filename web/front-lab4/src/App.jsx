import {BrowserRouter as Router, Routes, Route, Navigate} from "react-router-dom";
import {Main} from "./Main.jsx";
import {Login} from "./Login.jsx";
import {Register} from "./Register.jsx";
import {PageNotFound} from "./PageNotFound.jsx";
import {Navbar} from "./Navbar.jsx";

function App() {
  return (
      <Router>
        <Navbar/>
        <Routes>
            <Route
                path="/main"
                element={<Main />}
            />
            <Route
                path="/login"
                element={<Login />}
            />
            <Route
                path="/register"
                element={<Register />}
            />
            <Route
                path="/*"
                element={<Navigate to="/404" />}
            />
            <Route
                path="/404"
                element={<PageNotFound />}
            />
        </Routes>
      </Router>
  )
}

export default App
