import React, { useState, useEffect } from "react";
// import { useNavigate } from "react-router-dom";
import Navbar from "../../Components/common/Navbar";
import ImgUpload from "./ImgUpload";
import Performance from "./Performance";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faGithub,
  faLinkedin
} from "@fortawesome/free-brands-svg-icons";
import {
  faUser,
  faEnvelope,
  faPhone,
  faVenus,
  faMars,
  faCalendar,
  faBriefcase,
  faMapMarkerAlt,
  faBookOpen,
  faEdit,
  faTrophy
} from "@fortawesome/free-solid-svg-icons";
import { profileService } from "../../api/profile.service";
import EditProfileModal from "./EditProfileModal";
import { message } from "antd";

function Profile() {
  const id = localStorage.getItem("id");
  const [userDetails, setUserDetails] = useState(null);
  const [profileImage, setProfileImage] = useState(null);
  const [loadingImage, setLoadingImage] = useState(true);
  const [loadingUser, setLoadingUser] = useState(true);
  const [activeTab, setActiveTab] = useState("overview");
  const [isEditModalVisible, setIsEditModalVisible] = useState(false);

  useEffect(() => {
    async function fetchUserDetails() {
      if (!id) {
        message.error("User ID not found");
        return;
      }

      try {
        // Fetch user details
        const userRes = await profileService.getUserDetails(id);
        if (userRes.success) {
          setUserDetails(userRes.data);
        } else {
          message.error(userRes.error || "Failed to load user details");
        }
      } catch (error) {
        console.error("Error fetching user details:", error);
        message.error("Failed to load user details");
      } finally {
        setLoadingUser(false);
      }

      // Fetch profile image separately
      try {
        const imgRes = await profileService.getProfileImage(id);
        if (imgRes.success) {
          setProfileImage(imgRes.data);
        }
      } catch (error) {
        console.error("Error fetching profile image:", error);
        // Don't show error message for image - it's optional
      } finally {
        setLoadingImage(false);
      }
    }

    fetchUserDetails();
  }, [id]);

  const updateUser = async (updatedData) => {
    try {
      const res = await profileService.updateUser(id, updatedData);

      if (res.success) {
        setUserDetails(prevDetails => ({
          ...prevDetails,
          ...updatedData
        }));
        return true;
      } else {
        message.error(res.error || "Failed to update profile");
        return false;
      }
    } catch (err) {
      console.error("Error updating user:", err);
      message.error("Failed to update profile");
      return false;
    }
  };

  const handleEditProfile = () => {
    setIsEditModalVisible(true);
  };

  const handleModalClose = () => {
    setIsEditModalVisible(false);
  };

  const handleProfileUpdate = async (updatedData) => {
    const success = await updateUser(updatedData);
    return success;
  };

  const handleImageChange = async (event) => {
    const file = event.target.files[0];
    if (!file) return;

    // Validate file type
    if (!file.type.startsWith('image/')) {
      message.error('Please upload a valid image file');
      return;
    }

    // Validate file size (max 5MB)
    if (file.size > 5 * 1024 * 1024) {
      message.error('Image size should be less than 5MB');
      return;
    }

    try {
      setLoadingImage(true);
      const res = await profileService.uploadProfileImage(id, file);
      
      if (res.success) {
        const imageUrl = URL.createObjectURL(file);
        setProfileImage(imageUrl);
        message.success("Profile image updated successfully");
      } else {
        message.error(res.error || "Failed to upload image");
      }
    } catch (error) {
      console.error("Error uploading image:", error);
      message.error("Failed to upload image");
    } finally {
      setLoadingImage(false);
    }
  };

  const getGenderIcon = (gender) => {
    if (!gender) return faUser;
    const lowerGender = gender.toLowerCase();
    if (lowerGender === 'female') return faVenus;
    if (lowerGender === 'male') return faMars;
    return faUser;
  };

  // Show loading state
  if (loadingUser) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-indigo-100 via-white to-purple-100">
        <Navbar page="profile" />
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-600 mx-auto mb-4"></div>
            <p className="text-gray-600">Loading profile...</p>
          </div>
        </div>
      </div>
    );
  }

  // Show error if user not found
  if (!userDetails) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-indigo-100 via-white to-purple-100">
        <Navbar page="profile" />
        <div className="flex items-center justify-center h-96">
          <div className="text-center">
            <p className="text-red-600 text-lg">Failed to load profile data</p>
            <button 
              onClick={() => window.location.reload()} 
              className="mt-4 px-6 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700"
            >
              Retry
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-indigo-100 via-white to-purple-100">
      <Navbar page="profile" />

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">

        {/* Profile Header Card */}
        <div className="bg-white rounded-3xl shadow-xl overflow-hidden mb-8">
          {/* Profile Info */}
          <div className="relative px-8 pb-8 pt-8">
            {/* Profile Picture */}
            <div className="flex flex-col sm:flex-row items-start sm:items-end mb-6">
              <div className="relative z-10">
                <ImgUpload
                  onChange={handleImageChange}
                  src={profileImage}
                  isLoading={loadingImage}
                />
              </div>

              <div className="mt-5 sm:mt-5 sm:ml-6 flex-1">
                <div className="flex flex-column sm:flex-row sm:items-center sm:justify-between">
                  <div>
                    <h2 className="text-3xl font-bold text-gray-900 mb-1">
                      {userDetails.username || "User"}
                    </h2>
                    <p className="text-gray-600 text-lg">{userDetails.profession || "Learner"}</p>
                    {userDetails.location && (
                      <div className="flex items-center text-gray-500 mt-1">
                        <FontAwesomeIcon icon={faMapMarkerAlt} className="mr-2 text-sm" />
                        {userDetails.location}
                      </div>
                    )}
                  </div>

                  <button
                    onClick={handleEditProfile}
                    className="mt-4 sm:mt-0 inline-flex items-center px-4 py-2 bg-indigo-600 hover:bg-indigo-700 text-white rounded-lg font-medium transition-colors"
                  >
                    <FontAwesomeIcon icon={faEdit} className="mr-2" />
                    Edit Profile
                  </button>
                </div>
              </div>
            </div>

            {/* Social Links */}
            {(userDetails.linkedin_url || userDetails.github_url) && (
              <div className="flex gap-4 mb-6">
                {userDetails.linkedin_url && (
                  <a
                    href={userDetails.linkedin_url}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="flex items-center gap-2 px-4 py-2 bg-blue-50 hover:bg-blue-100 text-blue-600 rounded-lg transition-colors"
                  >
                    <FontAwesomeIcon icon={faLinkedin} />
                    LinkedIn
                  </a>
                )}
                {userDetails.github_url && (
                  <a
                    href={userDetails.github_url}
                    target="_blank"
                    rel="noopener noreferrer"
                    className="flex items-center gap-2 px-4 py-2 bg-gray-50 hover:bg-gray-100 text-gray-700 rounded-lg transition-colors"
                  >
                    <FontAwesomeIcon icon={faGithub} />
                    GitHub
                  </a>
                )}
              </div>
            )}

            {/* Tab Navigation */}
            <div className="flex space-x-1 bg-gray-100 rounded-xl p-1">
              <button
                onClick={() => setActiveTab("overview")}
                className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium transition-all ${
                  activeTab === "overview"
                    ? "bg-white text-indigo-600 shadow-sm"
                    : "text-gray-600 hover:text-gray-800"
                }`}
              >
                <FontAwesomeIcon icon={faUser} />
                Overview
              </button>
              <button
                onClick={() => setActiveTab("performance")}
                className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium transition-all ${
                  activeTab === "performance"
                    ? "bg-white text-indigo-600 shadow-sm"
                    : "text-gray-600 hover:text-gray-800"
                }`}
              >
                <FontAwesomeIcon icon={faTrophy} />
                Performance
              </button>
            </div>
          </div>
        </div>

        {activeTab === "overview" ? (
          <div className="lg:col-span-2">
            <div className="bg-white rounded-2xl shadow-lg p-6 mb-6">
              <h3 className="text-xl font-bold text-gray-900 mb-6 flex items-center gap-2">
                <FontAwesomeIcon icon={faUser} className="text-indigo-600" />
                Personal Information
              </h3>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <InfoCard
                  icon={faEnvelope}
                  label="Email Address"
                  value={userDetails.email}
                  iconColor="text-red-500"
                />
                <InfoCard
                  icon={faPhone}
                  label="Phone Number"
                  value={userDetails.mobileNumber}
                  iconColor="text-green-500"
                />
                <InfoCard
                  icon={getGenderIcon(userDetails.gender)}
                  label="Gender"
                  value={userDetails.gender}
                  iconColor="text-purple-500"
                />
                <InfoCard
                  icon={faCalendar}
                  label="Date of Birth"
                  value={userDetails.dob}
                  iconColor="text-blue-500"
                />
                <InfoCard
                  icon={faBriefcase}
                  label="Profession"
                  value={userDetails.profession}
                  iconColor="text-orange-500"
                />
                <InfoCard
                  icon={faBookOpen}
                  label="Learning Courses"
                  value={userDetails.learningCourses?.length || 0}
                  iconColor="text-indigo-500"
                />
              </div>
            </div>
          </div>
        ) : (
          <Performance />
        )}
      </div>

      <EditProfileModal
        visible={isEditModalVisible}
        onCancel={handleModalClose}
        userDetails={userDetails}
        onUpdate={handleProfileUpdate}
      />
    </div>
  );
}

function InfoCard({ icon, label, value, iconColor = "text-gray-400" }) {
  return (
    <div className="group p-4 bg-gray-50 hover:bg-gray-100 rounded-xl transition-colors border border-gray-100">
      <div className="flex items-start gap-3">
        <div className={`mt-1 ${iconColor}`}>
          <FontAwesomeIcon icon={icon} className="text-lg" />
        </div>
        <div className="flex-1">
          <h4 className="text-sm font-medium text-gray-600 mb-1">{label}</h4>
          <p className="text-gray-900 group-hover:text-indigo-600 transition-colors">
            {value || "Not specified"}
          </p>
        </div>
      </div>
    </div>
  );
}

export default Profile;