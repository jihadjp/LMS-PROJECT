import React from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCamera, faUser } from "@fortawesome/free-solid-svg-icons";

const ImgUpload = ({ onChange, src, isLoading }) => (
  <label htmlFor="photo-upload" className="cursor-pointer group">
    <div className="relative w-32 h-32 rounded-full border-4 border-white shadow-lg overflow-hidden bg-gradient-to-br from-indigo-100 to-purple-100 hover:shadow-xl transition-all duration-300">
      {isLoading ? (
        <div className="w-full h-full flex items-center justify-center bg-white">
          <div className="animate-spin rounded-full h-10 w-10 border-4 border-indigo-600 border-t-transparent"></div>
        </div>
      ) : src ? (
        <>
          <img 
            src={src} 
            alt="Profile" 
            className="object-cover w-full h-full"
            onError={(e) => {
              // If image fails to load, show placeholder
              e.target.style.display = 'none';
              e.target.parentElement.classList.add('flex', 'items-center', 'justify-center');
              e.target.parentElement.innerHTML = '<div class="text-gray-400 text-center"><svg class="w-16 h-16 mx-auto" fill="currentColor" viewBox="0 0 20 20"><path fill-rule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clip-rule="evenodd"></path></svg></div>';
            }}
          />
          {/* Hover overlay */}
          <div className="absolute inset-0 bg-black bg-opacity-0 group-hover:bg-opacity-40 transition-all duration-300 flex items-center justify-center">
            <FontAwesomeIcon 
              icon={faCamera} 
              className="text-white text-2xl opacity-0 group-hover:opacity-100 transition-opacity duration-300"
            />
          </div>
        </>
      ) : (
        <div className="w-full h-full flex flex-col items-center justify-center text-gray-500 group-hover:text-indigo-600 transition-colors">
          <FontAwesomeIcon icon={faUser} className="text-4xl mb-2" />
          <span className="text-xs font-medium">Upload Photo</span>
          <FontAwesomeIcon icon={faCamera} className="text-lg mt-1" />
        </div>
      )}
    </div>
    
    <input
      id="photo-upload"
      type="file"
      accept="image/*"
      onChange={onChange}
      className="hidden"
    />
    
    {/* Helper text */}
    <p className="text-xs text-gray-500 text-center mt-2">
      Click to {src ? 'change' : 'upload'}
    </p>
  </label>
);

export default ImgUpload;