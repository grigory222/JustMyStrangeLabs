import {BrowserRouter as Router, Routes, Route, Navigate} from "react-router-dom";
import {MainPage} from "./pages/MainPage.jsx";
import {LoginPage} from "./pages/LoginPage.jsx";
import {RegisterPage} from "./pages/RegisterPage.jsx";
import {NotFoundPage} from "./pages/NotFoundPage.jsx";
import {Navbar} from "./components/Navbar.jsx";

function App() {
  return (
      <Router>
        <Navbar/>
        <Routes>
            <Route
                path="/main"
                element={<MainPage />}
            />
            <Route
                path="/login"
                element={<LoginPage />}
            />
            <Route
                path="/register"
                element={<RegisterPage />}
            />
            <Route
                path="/*"
                element={<Navigate to="/404" />}
            />
            <Route
                path="/404"
                element={<NotFoundPage />}
            />
        </Routes>
      </Router>
  )
}

export default App
