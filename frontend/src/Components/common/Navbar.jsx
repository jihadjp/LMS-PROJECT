import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import logo from "../../assets/images/logo.jpg";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faUser, faChalkboardUser, faSignOutAlt, faChartLine } from "@fortawesome/free-solid-svg-icons";
import { authService } from "../../api/auth.service";
import { useUserContext } from "../../contexts/UserContext";
import { API_BASE_URL } from "../../api/constant";

function Navbar({ page }) {
  const navigate = useNavigate();
  const { user, loading } = useUserContext(); 
  const [isMobileMenuOpen, setIsMobileMenuOpen] = useState(false);

  const isAuthenticated = !!user;

  const handleLogOut = async () => {
    await authService.logout();
    window.location.href = "/login"; // সেশন সিঙ্ক্রোনাইজেশনের জন্য সরাসরি রিলোড করা ভালো
  };

  const toggleMobileMenu = () => setIsMobileMenuOpen(!isMobileMenuOpen);
  const closeMobileMenu = () => setIsMobileMenuOpen(false);

  // প্রোফাইল লিঙ্কের গন্তব্য নির্ধারণের ফাংশন

  const getProfileTarget = () => {
    if (user.role === "ROLE_ADMIN") return `${API_BASE_URL}/admin/dashboard`;
    if (user.role === "ROLE_INSTRUCTOR") return `${API_BASE_URL}/instructor/dashboard`;
    return "/profile";
  };

  const handleProfileNavigation = (e) => {
    closeMobileMenu();
    const target = getProfileTarget();
    
    // যদি টার্গেটটি পূর্ণাঙ্গ ইউআরএল (http) হয়, তবে window.location ব্যবহার করবে
    if (target.startsWith("http")) {
      e.preventDefault();
      window.location.href = target;
    }
    // অন্যথায় সাধারণ লিঙ্ক হিসেবে কাজ করবে
  };

  const getLinkClasses = (linkName) => {
    const base = "no-underline text-[17px] font-bold transition-all duration-300 px-[12px] py-[4px] block rounded cursor-pointer";
    const active = "bg-gradient-to-r from-blue-600 to-purple-600 text-white shadow-md";
    const inactive = "text-[rgb(21,21,100)] hover:text-yellow-500";
    return `${base} ${page === linkName ? active : inactive}`;
  };

  if (loading) {
    return (
      <nav className="bg-white w-full flex flex-row justify-between items-center px-[4vw] py-2 shadow-md z-[999] sticky top-0">
        <div className="flex items-center">
          <Link to="/">
            <img src={logo} alt="LMS Logo" className="w-[200px] h-auto cursor-pointer" />
          </Link>
        </div>
        <div className="animate-pulse font-bold text-blue-600">Syncing Session...</div>
      </nav>
    );
  }

  return (
    <nav className="bg-white w-full flex flex-row justify-between items-center px-[4vw] py-2 shadow-md z-[999] sticky top-0">
      <div className="flex items-center">
        <Link to="/" onClick={closeMobileMenu}>
          <img src={logo} alt="LMS Logo" className="w-[200px] h-auto cursor-pointer" />
        </Link>
      </div>

      <div className="flex items-center">
        <div className="md:hidden text-2xl cursor-pointer mr-4" onClick={toggleMobileMenu}>
          &#9776;
        </div>

        <ul className={`flex items-center gap-4 list-none m-0 p-0 ${isMobileMenuOpen ? "absolute top-[70px] left-0 w-full bg-white flex-col p-5 shadow-xl" : "hidden md:flex"}`}>
          
          <li><Link to="/" className={getLinkClasses("home")} onClick={closeMobileMenu}>Home</Link></li>
          <li><Link to="/courses" className={getLinkClasses("courses")} onClick={closeMobileMenu}>Courses</Link></li>

          {isAuthenticated && (
            <>
              {/* এডমিন/ইন্সট্রাক্টর হলে সরাসরি ড্যাশবোর্ড লিঙ্ক */}
              {(user.role === "ROLE_ADMIN" || user.role === "ROLE_INSTRUCTOR") && (
                <li>
                  <button
                    onClick={() => window.location.href = getProfileTarget()}
                    className="no-underline text-[17px] font-bold transition-all duration-300 px-[12px] py-[4px] block rounded bg-blue-50 text-blue-700 hover:bg-blue-100 border-0 cursor-pointer"
                  >
                    Dashboard <FontAwesomeIcon icon={faChartLine} className="ml-1 text-sm" />
                  </button>
                </li>
              )}

              {/* প্রোফাইল লিঙ্ক: রোল অনুযায়ী গন্তব্য পরিবর্তন হবে */}
              <li>
                <Link 
                  to={getProfileTarget()} 
                  className={getLinkClasses("profile")} 
                  onClick={handleProfileNavigation}
                >
                  Profile <FontAwesomeIcon icon={faUser} className="ml-1 text-sm" />
                </Link>
              </li>

              <li>
                <Link to="/learnings" className={getLinkClasses("learnings")} onClick={closeMobileMenu}>
                  Learnings <FontAwesomeIcon icon={faChalkboardUser} className="ml-1 text-sm" />
                </Link>
              </li>
            </>
          )}

          <li className="ml-2">
            {isAuthenticated ? (
              <div className="flex items-center gap-3">
                {user.role === "ROLE_ADMIN" && (
                  <span className="text-[10px] bg-red-100 text-red-600 px-2 py-1 rounded font-bold border border-red-200">ADMIN</span>
                )}
                
                <button
                  onClick={handleLogOut}
                  className="bg-red-500 hover:bg-red-600 text-white px-5 py-2 rounded-lg font-bold flex items-center gap-2 transition-all border-0 cursor-pointer shadow-sm active:scale-95"
                >
                  <FontAwesomeIcon icon={faSignOutAlt} /> Sign Out
                </button>
              </div>
            ) : (
              <button
                onClick={() => { navigate("/login"); closeMobileMenu(); }}
                className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg font-bold shadow-md transition-all border-0 cursor-pointer active:scale-95"
              >
                Login / Sign Up
              </button>
            )}
          </li>
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;