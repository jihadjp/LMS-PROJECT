import { useState, useEffect } from "react";
import Courses from "./DCourses";
import Dashboard from "./Dashboard";
import SideBar from "./SideBar";
import Users from "./DUsers";
import { authService } from "../../api/auth.service";
import { useNavigate } from "react-router-dom";

function AdminDashboard() {
  const [current, setCurrent] = useState("dashboard");
  const navigate = useNavigate();

  // Component mount হওয়ার সময় check করবে admin কিনা
  useEffect(() => {
    const isAdmin = authService.isAdminAuthenticated();
    if (!isAdmin) {
      // যদি admin না হয়, তাহলে home page এ redirect
      navigate("/");
    }
  }, [navigate]);

  const handleLogout = async () => {
    await authService.logout();
    // authService.logout() already handles redirect to /login
  };

  const renderContent = () => {
    switch (current) {
      case "dashboard":
        return <Dashboard />;
      case "user":
        return <Users />;
      case "courses":
        return <Courses />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <div className="flex min-h-screen">
      <SideBar current={current} onSelect={setCurrent} onLogout={handleLogout} />
      <section className="flex-1 bg-gradient-to-br from-slate-50 via-indigo-50 to-purple-100 transition-all duration-300">
        <main className="p-8 font-poppins">{renderContent()}</main>
      </section>
    </div>
  );
}

export default AdminDashboard;