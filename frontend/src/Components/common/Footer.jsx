import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faFacebookF,
  faInstagram,
  faLinkedinIn,
  faTwitter,
  faYoutube,
} from "@fortawesome/free-brands-svg-icons";
import {
  faEnvelope,
  faPhone,
  faMapMarkerAlt,
} from "@fortawesome/free-solid-svg-icons";
import { Link } from "react-router-dom";

function Footer() {
  const currentYear = new Date().getFullYear();

  // Function to handle smooth scroll to top
  const handleLinkClick = () => {
    window.scrollTo({
      top: 0,
      left: 0,
      behavior: "smooth"
    });
  };

  return (
    <footer className="bg-gradient-to-br from-gray-900 via-gray-800 to-gray-900 text-gray-300">
      {/* Main Footer Content */}
      <div className="max-w-7xl mx-auto px-6 py-12">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 mb-12">
          {/* About Section */}
          <div className="space-y-4">
            <h3 className="text-xl font-bold text-white mb-4 relative inline-block">
              NetBook Academy
              <span className="absolute bottom-0 left-0 w-12 h-1 bg-gradient-to-r from-blue-500 to-purple-500 rounded"></span>
            </h3>
            <p className="text-gray-400 text-sm leading-relaxed">
              Empowering learners worldwide with cutting-edge courses in
              technology, programming, and professional development.
            </p>
            <div className="flex space-x-3 pt-2">
              <a
                href="#"
                aria-label="Facebook"
                className="w-10 h-10 flex items-center justify-center rounded-full bg-gray-800 hover:bg-blue-600 text-white transition-all duration-300 transform hover:scale-110"
              >
                <FontAwesomeIcon icon={faFacebookF} />
              </a>
              <a
                href="#"
                aria-label="Instagram"
                className="w-10 h-10 flex items-center justify-center rounded-full bg-gray-800 hover:bg-pink-500 text-white transition-all duration-300 transform hover:scale-110"
              >
                <FontAwesomeIcon icon={faInstagram} />
              </a>
              <a
                href="#"
                aria-label="LinkedIn"
                className="w-10 h-10 flex items-center justify-center rounded-full bg-gray-800 hover:bg-blue-500 text-white transition-all duration-300 transform hover:scale-110"
              >
                <FontAwesomeIcon icon={faLinkedinIn} />
              </a>
              <a
                href="#"
                aria-label="Twitter"
                className="w-10 h-10 flex items-center justify-center rounded-full bg-gray-800 hover:bg-blue-400 text-white transition-all duration-300 transform hover:scale-110"
              >
                <FontAwesomeIcon icon={faTwitter} />
              </a>
              <a
                href="#"
                aria-label="YouTube"
                className="w-10 h-10 flex items-center justify-center rounded-full bg-gray-800 hover:bg-red-600 text-white transition-all duration-300 transform hover:scale-110"
              >
                <FontAwesomeIcon icon={faYoutube} />
              </a>
            </div>
          </div>

          {/* Popular Courses */}
          <div>
            <h3 className="text-lg font-bold text-white mb-4 relative inline-block">
              Popular Courses
              <span className="absolute bottom-0 left-0 w-12 h-1 bg-gradient-to-r from-blue-500 to-purple-500 rounded"></span>
            </h3>
            <ul className="space-y-3">
              <li>
                <a
                  href="#"
                  className="text-gray-400 hover:text-white hover:translate-x-1 transition-all duration-200 inline-flex items-center group"
                >
                  <span className="w-1.5 h-1.5 bg-blue-500 rounded-full mr-2 group-hover:bg-purple-500"></span>
                  Web Development
                </a>
              </li>
              <li>
                <a
                  href="#"
                  className="text-gray-400 hover:text-white hover:translate-x-1 transition-all duration-200 inline-flex items-center group"
                >
                  <span className="w-1.5 h-1.5 bg-blue-500 rounded-full mr-2 group-hover:bg-purple-500"></span>
                  Data Science
                </a>
              </li>
              <li>
                <a
                  href="#"
                  className="text-gray-400 hover:text-white hover:translate-x-1 transition-all duration-200 inline-flex items-center group"
                >
                  <span className="w-1.5 h-1.5 bg-blue-500 rounded-full mr-2 group-hover:bg-purple-500"></span>
                  Machine Learning
                </a>
              </li>
              <li>
                <a
                  href="#"
                  className="text-gray-400 hover:text-white hover:translate-x-1 transition-all duration-200 inline-flex items-center group"
                >
                  <span className="w-1.5 h-1.5 bg-blue-500 rounded-full mr-2 group-hover:bg-purple-500"></span>
                  Mobile Development
                </a>
              </li>
              <li>
                <a
                  href="#"
                  className="text-gray-400 hover:text-white hover:translate-x-1 transition-all duration-200 inline-flex items-center group"
                >
                  <span className="w-1.5 h-1.5 bg-blue-500 rounded-full mr-2 group-hover:bg-purple-500"></span>
                  Cloud Computing
                </a>
              </li>
            </ul>
          </div>

          {/* Quick Links */}
          <div>
            <h3 className="text-lg font-bold text-white mb-4 relative inline-block">
              Quick Links
              <span className="absolute bottom-0 left-0 w-12 h-1 bg-gradient-to-r from-blue-500 to-purple-500 rounded"></span>
            </h3>
            <ul className="space-y-3">
              <li>
                <Link
                  to="/"
                  onClick={handleLinkClick}
                  className="text-gray-400 hover:text-white hover:translate-x-1 transition-all duration-200 inline-flex items-center group"
                >
                  <span className="w-1.5 h-1.5 bg-blue-500 rounded-full mr-2 group-hover:bg-purple-500"></span>
                  Home
                </Link>
              </li>
              <li>
                <Link
                  to="/courses"
                  onClick={handleLinkClick}
                  className="text-gray-400 hover:text-white hover:translate-x-1 transition-all duration-200 inline-flex items-center group"
                >
                  <span className="w-1.5 h-1.5 bg-blue-500 rounded-full mr-2 group-hover:bg-purple-500"></span>
                  All Courses
                </Link>
              </li>
              <li>
                <a
                  href="#"
                  className="text-gray-400 hover:text-white hover:translate-x-1 transition-all duration-200 inline-flex items-center group"
                >
                  <span className="w-1.5 h-1.5 bg-blue-500 rounded-full mr-2 group-hover:bg-purple-500"></span>
                  About Us
                </a>
              </li>
              <li>
                <a
                  href="#"
                  className="text-gray-400 hover:text-white hover:translate-x-1 transition-all duration-200 inline-flex items-center group"
                >
                  <span className="w-1.5 h-1.5 bg-blue-500 rounded-full mr-2 group-hover:bg-purple-500"></span>
                  Become Instructor
                </a>
              </li>
              <li>
                <a
                  href="#"
                  className="text-gray-400 hover:text-white hover:translate-x-1 transition-all duration-200 inline-flex items-center group"
                >
                  <span className="w-1.5 h-1.5 bg-blue-500 rounded-full mr-2 group-hover:bg-purple-500"></span>
                  Contact Us
                </a>
              </li>
            </ul>
          </div>

          {/* Contact Info */}
          <div>
            <h3 className="text-lg font-bold text-white mb-4 relative inline-block">
              Contact Us
              <span className="absolute bottom-0 left-0 w-12 h-1 bg-gradient-to-r from-blue-500 to-purple-500 rounded"></span>
            </h3>
            <ul className="space-y-4">
              <li className="flex items-start space-x-3 text-gray-400 hover:text-white transition-colors">
                <FontAwesomeIcon
                  icon={faMapMarkerAlt}
                  className="mt-1 text-blue-500"
                />
                <span className="text-sm">Chattogram, Bangladesh</span>
              </li>
              <li className="flex items-center space-x-3 text-gray-400 hover:text-white transition-colors">
                <FontAwesomeIcon icon={faEnvelope} className="text-blue-500" />
                <a href="mailto:support@netbookacademy.com" className="text-sm">
                  support@netbookacademy.com
                </a>
              </li>
              <li className="flex items-center space-x-3 text-gray-400 hover:text-white transition-colors">
                <FontAwesomeIcon icon={faPhone} className="text-blue-500" />
                <a href="tel:+8801234567890" className="text-sm">
                  +880 123-456-7890
                </a>
              </li>
            </ul>
          </div>
        </div>

        {/* Bottom Bar */}
        <div className="border-t border-gray-700 pt-8">
          <div className="flex flex-col md:flex-row justify-between items-center space-y-4 md:space-y-0">
            <p className="text-sm text-gray-400">
              © {currentYear} NetBook Academy. All rights reserved.
            </p>
            <div className="flex space-x-6 text-sm">
              <Link
                to="/privacy-policy"
                onClick={handleLinkClick}
                className="text-gray-400 hover:text-white transition-colors"
              >
                Privacy Policy
              </Link>
              <Link
                to="/terms-and-conditions"
                onClick={handleLinkClick}
                className="text-gray-400 hover:text-white transition-colors"
              >
                Terms & Conditions
              </Link>
              <Link
                to="/refund-policy"
                onClick={handleLinkClick}
                className="text-gray-400 hover:text-white transition-colors"
              >
                Refund Policy
              </Link>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
}

export default Footer;