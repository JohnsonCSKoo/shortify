import "./App.css";
import {BrowserRouter, Routes, Route} from "react-router";
import MainPage from "@/components/MainPage.tsx";
import RedirectPage from "@/components/RedirectPage.tsx";

const App: React.FC = () => {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/" element={<MainPage />} />
                <Route path="/:id" element={<RedirectPage />} />
            </Routes>
        </BrowserRouter>
    )
}

export default App;