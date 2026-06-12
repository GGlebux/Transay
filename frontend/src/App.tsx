import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";

import Layout from "./components/layout/Layout";

import Login from "./pages/Auth/Login";
import Register from "./pages/Auth/Register";
import VerifyPage from "./pages/Auth/verify";
import ForgotPassword from "./pages/Auth/ForgotPassword";

import Cabinet from "./pages/cabinet/Dashboard";
import Measures from "./pages/Person";

import AdminLayout from "./pages/admin/AdminLayout";
import Users from "./pages/admin/Users";
import PeopleList from "./pages/admin/PeopleList";
import Indicators from "./pages/admin/Indicators";
import Groups from "./pages/admin/Groups";
import Transcripts from "./pages/admin/Transcripts";
import Reasons from "./pages/admin/Reasons";
import Units from "./pages/admin/Units";

import { RequireAuth } from "./authe/RequireAuth";
import { RequireRole } from "./authe/RequireRole";

export default function App() {
  return (
    <Router>
      <Routes>
        {/* PUBLIC */}
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/verify" element={<VerifyPage />} />
        <Route path="/forgot-password" element={<ForgotPassword />} />

        {/* CABINET (профиль) */}
        <Route
          path="/cabinet"
          element={
            <RequireAuth>
              <Layout>
                <Cabinet />
              </Layout>
            </RequireAuth>
          }
        />

        {/* СВОИ АНАЛИЗЫ (сводная таблица + расшифровка) */}
        <Route
          path="/measures"
          element={
            <RequireAuth>
              <Layout>
                <Measures />
              </Layout>
            </RequireAuth>
          }
        />

        {/* АДМИНКА */}
        <Route
          path="/admin"
          element={
            <RequireRole roleRequired={["ADMIN", "EDITOR"]}>
              <Layout>
                <AdminLayout />
              </Layout>
            </RequireRole>
          }
        >
          <Route index element={<Navigate to="indicators" replace />} />
          <Route path="users" element={<Users />} />
          <Route path="people" element={<PeopleList />} />
          <Route path="indicators" element={<Indicators />} />
          <Route path="groups" element={<Groups />} />
          <Route path="transcripts" element={<Transcripts />} />
          <Route path="reasons" element={<Reasons />} />
          <Route path="units" element={<Units />} />
        </Route>

        {/* DEFAULT */}
        <Route path="/" element={<Navigate to="/cabinet" replace />} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </Router>
  );
}
