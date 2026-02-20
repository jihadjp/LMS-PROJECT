import React, { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import { useUserContext } from "../../contexts/UserContext";
import Navbar from "../../Components/common/Navbar";
import { authService } from "../../api/auth.service";
import { Mail, Lock, LogIn } from "lucide-react";
import { InputField } from "../../Components/common/InputFeild";
import { message } from "antd";
import { API_BASE_URL } from "../../api/constant";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();
  const { setUser, logout } = useUserContext();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    if (params.get("logout") === "true" || params.get("session_expired") === "true") {
      logout();
      if (params.get("session_expired") === "true") {
        message.warning("Your session has expired. Please login again.");
      } else {
        message.success("Logged out successfully.");
      }
      navigate("/login", { replace: true });
    }
  }, [location, logout, navigate]);

  const handleLogin = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    try {
      const result = await authService.login(email, password);

      if (result.success && result.user) {
        setUser(result.user);

        const userRole = result.user.role;
        const token = result.token;

        if (userRole === "ROLE_ADMIN" || userRole === "ROLE_INSTRUCTOR") {
          // ✅ /api/auth/login (AJAX) নয়, সরাসরি backend /auth/redirect এ browser navigate করবে
          // এতে backend session cookie সঠিকভাবে set হবে
          window.location.href = `${API_BASE_URL}/auth/redirect?token=${token}&role=${userRole}`;
        } else {
          message.success("Login successful!");
          navigate("/courses");
        }
      } else {
        setError(result.error || "Invalid email or password.");
      }
    } catch (err) {
      console.error("Login error:", err);
      setError("Server connection failed. Please try again later.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
      <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
        <Navbar page="login" />
        <div className="flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
          <div className="max-w-md w-full space-y-6">
            <div className="text-center">
              <div className="mx-auto h-16 w-16 bg-gradient-to-r from-blue-600 to-purple-600 rounded-2xl flex items-center justify-center mb-4 shadow-xl rotate-3 hover:rotate-0 transition-transform duration-300">
                <LogIn className="h-9 w-9 text-white" />
              </div>
              <h2 className="text-4xl font-extrabold text-gray-900 tracking-tight">Welcome Back!</h2>
              <p className="mt-2 text-gray-600 font-medium">Please sign in to access your dashboard</p>
            </div>

            <div className="bg-white/80 backdrop-blur-lg shadow-2xl rounded-3xl p-10 border border-white/50">
              <form onSubmit={handleLogin} className="space-y-6" autoComplete="off">
                <InputField
                    id="email" name="email" type="email" label="Email Address"
                    value={email} onChange={(e) => setEmail(e.target.value)}
                    required placeholder="name@example.com"
                    icon={<Mail className="h-5 w-5 text-blue-500" />}
                />
                <InputField
                    id="password" name="password" type="password" label="Password"
                    value={password} onChange={(e) => setPassword(e.target.value)}
                    required placeholder="••••••••"
                    icon={<Lock className="h-5 w-5 text-purple-500" />}
                />
                <div className="flex items-center justify-end">
                  <Link to="/forgot-password" className="text-sm font-semibold text-blue-600 hover:text-blue-800 transition-colors">
                    Forgot Password?
                  </Link>
                </div>
                {error && (
                    <div className="bg-red-50 border-l-4 border-red-500 rounded-md p-4">
                      <p className="text-red-700 text-sm font-bold flex items-center">
                        <i className="fas fa-exclamation-circle mr-2"></i> {error}
                      </p>
                    </div>
                )}
                <button type="submit" disabled={isLoading}
                        className={`w-full py-4 px-6 rounded-xl font-bold text-lg transition-all duration-300 shadow-lg transform active:scale-95 ${
                            isLoading ? "bg-gray-400 cursor-wait"
                                : "bg-gradient-to-r from-blue-600 to-purple-600 text-white hover:shadow-blue-200 hover:shadow-2xl"
                        }`}
                >
                  {isLoading ? (
                      <span className="flex items-center justify-center">
                        <svg className="animate-spin h-5 w-5 mr-3 text-white" viewBox="0 0 24 24">
                          <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4" fill="none"></circle>
                          <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"></path>
                        </svg>
                        Signing In...
                      </span>
                  ) : "Sign In"}
                </button>
              </form>

              <div className="mt-10">
                <div className="relative">
                  <div className="absolute inset-0 flex items-center"><div className="w-full border-t border-gray-200"></div></div>
                  <div className="relative flex justify-center text-sm"><span className="px-4 bg-white text-gray-400 font-medium">New to NetBook Academy?</span></div>
                </div>
                <div className="mt-6 grid grid-cols-1 gap-3">
                  <Link to="/register" className="w-full flex justify-center py-3 px-4 border border-blue-200 rounded-xl text-sm font-bold text-blue-700 bg-blue-50 hover:bg-blue-100 transition-all">
                    Register as Student
                  </Link>
                  <Link to="/instructor-register" className="w-full flex justify-center py-3 px-4 border border-purple-200 rounded-xl text-sm font-bold text-purple-700 bg-purple-50 hover:bg-purple-100 transition-all">
                    Join as Instructor
                  </Link>
                </div>
              </div>
            </div>

            <div className="text-center">
              <p className="text-sm text-gray-500">
                By signing in, you agree to our{" "}
                <a href="/terms-and-conditions" className="text-blue-600 hover:text-blue-700 transition-colors">Terms of Service</a>
                {" "}and{" "}
                <a href="/privacy-policy" className="text-blue-600 hover:text-blue-700 transition-colors">Privacy Policy</a>
              </p>
            </div>
          </div>
        </div>
      </div>
  );
}

export default Login;