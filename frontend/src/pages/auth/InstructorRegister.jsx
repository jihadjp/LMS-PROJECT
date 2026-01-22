import React, { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Navbar from "../../Components/common/Navbar";
import { authService } from "../../api/auth.service";
import { User, Mail, Phone, Lock, Calendar, MapPin, Briefcase, Github, Linkedin, GraduationCap, Award, BookOpen } from "lucide-react";
import { InputField } from "../../Components/common/InputFeild";

function InstructorRegister() {
  const navigate = useNavigate();
  const [error, setError] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    mobileNumber: "",
    password: "",
    dob: "",
    gender: "",
    location: "",
    profession: "",
    linkedin_url: "",
    github_url: "",
    expertise: "",
    degrees: "",
    experience: "",
    bio: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError("");

    try {
      const result = await authService.register({
        ...formData,
        role: "INSTRUCTOR",
      });

      if (result.success) {
        console.log("Instructor registration successful!");
        navigate("/login", {
          state: { message: "Registration successful! Please sign in to continue." }
        });
      } else {
        setError(result.error || "Registration failed. Please try again.");
      }
    } catch (error) {
      console.error("Registration error:", error);
      setError("An unexpected error occurred. Please try again.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-purple-50 via-white to-indigo-50">
      <Navbar />
      <div className="flex items-center justify-center py-8 px-4 sm:px-6 lg:px-8">
        <div className="max-w-4xl w-full space-y-4">
          <div className="text-center">
            <div className="mx-auto h-14 w-14 bg-gradient-to-r from-purple-600 to-indigo-600 rounded-full flex items-center justify-center mb-4 shadow-lg">
              <GraduationCap className="h-8 w-8 text-white" />
            </div>
            <h2 className="text-4xl font-bold text-gray-900 mb-2">Become an Instructor</h2>
            <p className="text-gray-600">Share your knowledge and inspire students worldwide</p>
          </div>

          <div className="bg-white shadow-2xl rounded-2xl p-8 border border-gray-100">
            <form onSubmit={handleSubmit} className="space-y-8">
              {/* Basic Information */}
              <div className="space-y-6">
                <h3 className="text-lg font-semibold text-gray-800 border-b border-gray-200 pb-2">
                  Basic Information
                </h3>
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                  {/* Full Name */}
                  <InputField
                    id="username"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    icon={<User className="h-5 w-5 text-gray-400" />}
                    label="Full Name"
                    required
                    placeholder="Enter your full name"
                  />

                  {/* Email */}
                  <InputField
                    id="email"
                    name="email"
                    type="email"
                    value={formData.email}
                    onChange={handleChange}
                    icon={<Mail className="h-5 w-5 text-gray-400" />}
                    label="Email Address"
                    required
                    placeholder="Enter your email"
                  />

                  {/* Phone */}
                  <InputField
                    id="mobileNumber"
                    name="mobileNumber"
                    type="tel"
                    value={formData.mobileNumber}
                    onChange={handleChange}
                    icon={<Phone className="h-5 w-5 text-gray-400" />}
                    label="Phone Number"
                    required
                    placeholder="Enter your phone number"
                  />

                  {/* Password */}
                  <InputField
                    id="password"
                    name="password"
                    type="password"
                    value={formData.password}
                    onChange={handleChange}
                    icon={<Lock className="h-5 w-5 text-gray-400" />}
                    label="Password"
                    required
                    placeholder="Create a strong password"
                  />
                </div>
              </div>

              {/* Personal Details */}
              <div className="space-y-6">
                <h3 className="text-lg font-semibold text-gray-800 border-b border-gray-200 pb-2">
                  Personal Details
                </h3>
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                  {/* DOB */}
                  <InputField
                    id="dob"
                    name="dob"
                    type="date"
                    value={formData.dob}
                    onChange={handleChange}
                    icon={<Calendar className="h-5 w-5 text-gray-400" />}
                    label="Date of Birth"
                  />

                  {/* Gender */}
                  <div className="space-y-2">
                    <label htmlFor="gender" className="block font-semibold bg-gradient-to-r from-indigo-500 to-purple-500 bg-clip-text text-transparent">
                      Gender
                    </label>
                    <select
                      id="gender"
                      name="gender"
                      value={formData.gender}
                      onChange={handleChange}
                      className="block w-full px-3 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-purple-500 text-gray-900 bg-white"
                    >
                      <option value="">Select Gender</option>
                      <option value="Male">Male</option>
                      <option value="Female">Female</option>
                      <option value="Other">Other</option>
                      <option value="Prefer not to say">Prefer not to say</option>
                    </select>
                  </div>

                  {/* Location */}
                  <InputField
                    id="location"
                    name="location"
                    value={formData.location}
                    onChange={handleChange}
                    icon={<MapPin className="h-5 w-5 text-gray-400" />}
                    label="Location"
                    placeholder="Enter your location"
                  />

                  {/* Profession */}
                  <InputField
                    id="profession"
                    name="profession"
                    value={formData.profession}
                    onChange={handleChange}
                    icon={<Briefcase className="h-5 w-5 text-gray-400" />}
                    label="Current Profession"
                    placeholder="Enter your profession"
                  />
                </div>
              </div>

              {/* Instructor Details */}
              <div className="space-y-6">
                <h3 className="text-lg font-semibold text-gray-800 border-b border-gray-200 pb-2">
                  Instructor Details
                </h3>
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                  {/* Expertise */}
                  <InputField
                    id="expertise"
                    name="expertise"
                    value={formData.expertise}
                    onChange={handleChange}
                    icon={<Award className="h-5 w-5 text-gray-400" />}
                    label="Field of Expertise"
                    required
                    placeholder="e.g., Web Development, Data Science, AI/ML"
                  />

                  {/* Degrees */}
                  <InputField
                    id="degrees"
                    name="degrees"
                    value={formData.degrees}
                    onChange={handleChange}
                    icon={<GraduationCap className="h-5 w-5 text-gray-400" />}
                    label="Degrees / Qualifications"
                    required
                    placeholder="e.g., B.Tech in CS, M.Sc in Data Science"
                  />

                  {/* Experience */}
                  <div className="space-y-2">
                    <label htmlFor="experience" className="block font-semibold bg-gradient-to-r from-indigo-500 to-purple-500 bg-clip-text text-transparent">
                      Years of Experience
                    </label>
                    <select
                      id="experience"
                      name="experience"
                      value={formData.experience}
                      onChange={handleChange}
                      required
                      className="block w-full px-3 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-purple-500 text-gray-900 bg-white"
                    >
                      <option value="">Select Experience</option>
                      <option value="0-1">0-1 years</option>
                      <option value="1-3">1-3 years</option>
                      <option value="3-5">3-5 years</option>
                      <option value="5-10">5-10 years</option>
                      <option value="10+">10+ years</option>
                    </select>
                  </div>

                  {/* Bio */}
                  <div className="space-y-2 lg:col-span-2">
                    <label htmlFor="bio" className="block font-semibold bg-gradient-to-r from-indigo-500 to-purple-500 bg-clip-text text-transparent">
                      Bio / About You
                    </label>
                    <div className="relative">
                      <div className="absolute left-3 top-3">
                        <BookOpen className="h-5 w-5 text-gray-400" />
                      </div>
                      <textarea
                        id="bio"
                        name="bio"
                        value={formData.bio}
                        onChange={handleChange}
                        rows="4"
                        className="block w-full pl-10 pr-3 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-purple-500 focus:border-purple-500 text-gray-900 bg-white resize-none"
                        placeholder="Tell us about yourself, your teaching experience, and what you're passionate about..."
                      />
                    </div>
                  </div>
                </div>
              </div>

              {/* Social Links */}
              <div className="space-y-6">
                <h3 className="text-lg font-semibold text-gray-800 border-b border-gray-200 pb-2">
                  Social Links
                </h3>
                <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                  <InputField
                    id="linkedin_url"
                    name="linkedin_url"
                    value={formData.linkedin_url}
                    onChange={handleChange}
                    icon={<Linkedin className="h-5 w-5 text-gray-400" />}
                    label="LinkedIn"
                    placeholder="https://linkedin.com/in/your-profile"
                  />

                  <InputField
                    id="github_url"
                    name="github_url"
                    value={formData.github_url}
                    onChange={handleChange}
                    icon={<Github className="h-5 w-5 text-gray-400" />}
                    label="GitHub"
                    placeholder="https://github.com/your-username"
                  />
                </div>
              </div>

              {error && (
                <div className="bg-red-50 border border-red-200 rounded-lg p-4">
                  <p className="text-red-800 text-sm font-medium">{error}</p>
                </div>
              )}

              <button
                type="submit"
                disabled={isLoading}
                className={`w-full py-4 px-6 rounded-lg font-semibold text-lg transition-all duration-200 shadow-lg hover:shadow-xl transform hover:-translate-y-0.5 focus:outline-none focus:ring-4 focus:ring-purple-300 ${isLoading
                    ? "bg-gray-400 text-gray-200 cursor-not-allowed"
                    : "bg-gradient-to-r from-purple-600 to-indigo-600 text-white hover:from-purple-700 hover:to-indigo-700"
                  }`}
              >
                {isLoading ? (
                  <div className="flex items-center justify-center">
                    <svg className="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                      <circle className="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" strokeWidth="4"></circle>
                      <path className="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                    </svg>
                    Creating Instructor Account...
                  </div>
                ) : (
                  "Register as Instructor"
                )}
              </button>
            </form>

            <div className="mt-8">
              <div className="relative">
                <div className="absolute inset-0 flex items-center">
                  <div className="w-full border-t border-gray-300" />
                </div>
                <div className="relative flex justify-center text-sm">
                  <span className="px-4 bg-white text-gray-500">Already have an account?</span>
                </div>
              </div>

              <div className="mt-6 text-center space-y-3">
                <p className="text-gray-600">
                  <Link
                    to="/login"
                    className="text-purple-600 font-semibold hover:text-purple-700 transition-colors text-lg"
                  >
                    Sign in here
                  </Link>
                </p>
                <p className="text-gray-600">
                  Want to learn instead?{" "}
                  <Link
                    to="/register"
                    className="text-purple-600 font-semibold hover:text-purple-700 transition-colors"
                  >
                    Register as Student
                  </Link>
                </p>
              </div>
            </div>
          </div>

          {/* Terms and Privacy */}
          <div className="text-center">
            <p className="text-sm text-gray-500">
              By creating an account, you agree to our{" "}
              <Link to="/terms-and-conditions" className="text-purple-600 hover:text-purple-700 transition-colors">Terms of Service</Link>
              {" "}and{" "}
              <Link to="/privacy-policy" className="text-purple-600 hover:text-purple-700 transition-colors">Privacy Policy</Link>
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default InstructorRegister;