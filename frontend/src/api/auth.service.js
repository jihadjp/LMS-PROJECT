import { API_BASE_URL } from "./constant";

async function login(email, password) {
  try {
    const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      credentials: "include",
      body: JSON.stringify({ email, password }),
    });

    const result = await response.json();

    if (response.ok) {
      const jwtData = result.data;

      localStorage.setItem("token", jwtData.token);
      localStorage.setItem("email", jwtData.email);
      localStorage.setItem("name", jwtData.name);
      localStorage.setItem("id", jwtData.id);
      localStorage.setItem("role", jwtData.role);

      return {
        success: true,
        token: jwtData.token, // ✅ সরাসরি token return করা হচ্ছে
        user: {
          id: jwtData.id,
          name: jwtData.name,
          email: jwtData.email,
          role: jwtData.role,
        },
      };
    } else {
      return {
        success: false,
        error: result.message || "Login failed",
      };
    }
  } catch (error) {
    console.error("Login error:", error);
    return { success: false, error: "Network error. Please try again." };
  }
}

async function getUserDetails(email) {
  try {
    const token = localStorage.getItem("token");
    if (!token) return { success: false, error: "No token found" };

    const response = await fetch(
        `${API_BASE_URL}/api/users/details?email=${email}`,
        {
          method: "GET",
          headers: {
            "Authorization": `Bearer ${token}`,
            "Content-Type": "application/json",
          },
          credentials: "include",
        }
    );

    const result = await response.json();

    if (response.ok) {
      return {
        success: true,
        data: result.data || result,
      };
    } else {
      return { success: false, error: result.message || "Session invalid" };
    }
  } catch (error) {
    console.error("Get user details error:", error);
    return { success: false, error: "Network error" };
  }
}

async function logout() {
  try {
    const token = localStorage.getItem("token");
    await fetch(`${API_BASE_URL}/api/auth/logout`, {
      method: "POST",
      headers: {
        "Authorization": `Bearer ${token}`,
      },
      credentials: "include",
    });
  } catch (error) {
    console.error("Logout error:", error);
  } finally {
    localStorage.clear();
    window.location.href = "/login?logout=true";
  }
}

async function register(formData) {
  try {
    const response = await fetch(`${API_BASE_URL}/api/auth/register`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(formData),
    });
    const result = await response.json();
    return { success: response.ok, message: result.message, error: result.message };
  } catch (error) {
    return { success: false, error: "Registration failed" };
  }
}

function isAdminAuthenticated() {
  return !!localStorage.getItem("token") && localStorage.getItem("role") === "ROLE_ADMIN";
}

function getCurrentUser() {
  return {
    token: localStorage.getItem("token"),
    id: localStorage.getItem("id"),
    name: localStorage.getItem("name"),
    email: localStorage.getItem("email"),
    role: localStorage.getItem("role"),
  };
}

export const authService = {
  login,
  register,
  getUserDetails,
  logout,
  isAdminAuthenticated,
  getCurrentUser,
};