import {BrowserRouter as Router, Routes, Route, Navigate} from "react-router-dom";
import {MainPage} from "./pages/MainPage.jsx";
import {LoginPage} from "./pages/LoginPage.jsx";
import {RegisterPage} from "./pages/RegisterPage.jsx";
import {NotFoundPage} from "./pages/NotFoundPage.jsx";
import {Navbar} from "./components/Navbar.jsx";
import {useSelector} from "react-redux";
import { useAuthInit } from "./hooks/useAuthInit";

function App() {
    const { isLogged, isLoading } = useSelector(state => state.reducer.auth);
    useAuthInit();

    if (isLoading) {
        return <div>Loading...</div>;
    }
  return (
      <Router>
        <Navbar/>
        <Routes>
            <Route
                path="/"
                element={<Navigate to="/main" />}
            />
            <Route
                path="/main"
                element={isLogged ? <MainPage /> : <Navigate to="/login" />}
            />
            <Route
                path="/login"
                element={!isLogged ? <LoginPage /> : <Navigate to="/main" />}
            />
            <Route
                path="/register"
                element={!isLogged ? <RegisterPage /> : <Navigate to="/main" />}
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
