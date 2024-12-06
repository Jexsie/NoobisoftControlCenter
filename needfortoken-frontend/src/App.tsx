import { BrowserRouter, Route, Routes } from "react-router-dom";
import Aside from "./component/Aside";
import Dashboard from "./component/Dashboard";
import Home from "./component/Home";

function App() {
  return (
    <BrowserRouter>
      <main className="h-full">
        <Aside />
        <Routes>
          <Route path="/" Component={Home} />
          <Route path="/dashboard" Component={Dashboard} />
        </Routes>
      </main>
    </BrowserRouter>
  );
}

export default App;
