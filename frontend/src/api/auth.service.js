import { API_BASE_URL } from "./constant";

/**
 * ১. লগইন ফাংশন
 * এখানে JWT টোকেন সংগ্রহের পাশাপাশি Session Cookie (JSESSIONID) তৈরির জন্য 
 * credentials: "include" ব্যবহার করা হয়েছে।
 */
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
      // আপনার ব্যাকএন্ড সাধারণত ডাটা result.data এর ভেতরে পাঠায়
      const jwtData = result.data;

      localStorage.setItem("token", jwtData.token);
      localStorage.setItem("email", jwtData.email);
      localStorage.setItem("name", jwtData.name);
      localStorage.setItem("id", jwtData.id);
      localStorage.setItem("role", jwtData.role);

      return {
        success: true,
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

/**
 * ২. ইউজার ডিটেইলস সংগ্রহের ফাংশন (সেশন ভেরিফিকেশনের জন্য)
 * লগে 'Anonymous' দেখানোর কারণ হলো টোকেনটি ঠিকমতো যাচ্ছিল না। 
 */
async function getUserDetails(email) {
  try {
    const token = localStorage.getItem("token");
    if (!token) return { success: false, error: "No token found" };

    const response = await fetch(
      `${API_BASE_URL}/api/users/details?email=${email}`,
      {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${token}`, // এটি অত্যন্ত জরুরি
          "Content-Type": "application/json",
        },
        credentials: "include", // সেশন ব্রিজ বজায় রাখার জন্য
      }
    );

    const result = await response.json();

    if (response.ok) {
      // ব্যাকএন্ড যদি ApiResponse<User> রিটার্ন করে, তবে ডাটা result.data তে থাকে
      return {
        success: true,
        data: result.data || result, // ডাটা নেস্টেড থাকলে সেটি হ্যান্ডেল করবে
      };
    } else {
      return { success: false, error: result.message || "Session invalid" };
    }
  } catch (error) {
    console.error("Get user details error:", error);
    return { success: false, error: "Network error" };
  }
}

/**
 * ৩. লগআউট ফাংশন
 * এটি সার্ভার সেশন এবং লোকাল স্টোরেজ উভয়ই ক্লিয়ার করবে।
 */
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
    console.log("Backend session cleared");
  } catch (error) {
    console.error("Logout error:", error);
  } finally {
    localStorage.clear();
    // রিঅ্যাক্ট অ্যাপ রিলোড করে লগইন পেজে পাঠাবে
    window.location.href = "/login?logout=true";
  }
}

// --- হেল্পার ফাংশনসমূহ ---

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