import React from "react";
import Navbar from "../../Components/common/Navbar";
import Footer from "../../Components/common/Footer";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faShieldAlt,
  faDatabase,
  faLock,
  faUserShield,
  faCookie,
  faEnvelope,
} from "@fortawesome/free-solid-svg-icons";

function PrivacyPolicy() {
  const sections = [
    {
      icon: faShieldAlt,
      title: "Introduction",
      content: (
        <p className="text-gray-600 leading-relaxed">
          NetBook Academy ("we", "our", or "us") is committed to protecting
          your privacy. This Privacy Policy explains how we collect, use,
          disclose, and safeguard your information when you visit our website
          and use our services. Please read this policy carefully to understand
          our practices regarding your personal data.
        </p>
      ),
    },
    {
      icon: faDatabase,
      title: "Information We Collect",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed">
            We collect information that you provide directly to us, including:
          </p>
          <ul className="space-y-2 ml-6">
            {[
              "Personal identification information (name, email address, phone number)",
              "Account credentials (username, encrypted password)",
              "Profile information (date of birth, gender, profession, location)",
              "Payment information (processed securely through third-party payment gateways)",
              "Course enrollment and progress data",
              "User-generated content (assignments, discussions, reviews, feedback)",
              "Communication preferences and marketing consents",
            ].map((item, idx) => (
              <li key={idx} className="flex items-start text-gray-600">
                <span className="inline-block w-2 h-2 bg-blue-500 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                <span>{item}</span>
              </li>
            ))}
          </ul>
        </div>
      ),
    },
    {
      icon: faLock,
      title: "How We Use Your Information",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed">
            We use the collected information for the following purposes:
          </p>
          <ul className="space-y-2 ml-6">
            {[
              "To provide, maintain, and improve our educational services and platform",
              "To process your course enrollments and manage your account securely",
              "To communicate with you about courses, updates, and promotional offers",
              "To track your learning progress and provide certificates of completion",
              "To respond to your inquiries and provide personalized customer support",
              "To analyze usage patterns and improve user experience through data insights",
              "To prevent fraud, ensure security, and protect our users",
              "To comply with legal obligations and enforce our terms of service",
            ].map((item, idx) => (
              <li key={idx} className="flex items-start text-gray-600">
                <span className="inline-block w-2 h-2 bg-purple-500 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                <span>{item}</span>
              </li>
            ))}
          </ul>
        </div>
      ),
    },
    {
      icon: faUserShield,
      title: "Information Sharing and Disclosure",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed">
            We respect your privacy and do not sell your personal information to
            third parties. We may share your information only in the following
            circumstances:
          </p>
          <ul className="space-y-2 ml-6">
            {[
              "Course instructors (limited to information relevant to course delivery and assessment)",
              "Payment processors for secure transaction processing",
              "Service providers who assist in operating our platform (hosting, analytics, customer support)",
              "Legal authorities when required by law or to protect our rights",
              "Business partners with your explicit consent for specific services",
            ].map((item, idx) => (
              <li key={idx} className="flex items-start text-gray-600">
                <span className="inline-block w-2 h-2 bg-green-500 rounded-full mt-2 mr-3 flex-shrink-0"></span>
                <span>{item}</span>
              </li>
            ))}
          </ul>
        </div>
      ),
    },
    {
      icon: faShieldAlt,
      title: "Data Security",
      content: (
        <p className="text-gray-600 leading-relaxed">
          We implement industry-standard security measures to protect your
          personal information, including encryption, secure servers, and access
          controls. However, no method of transmission over the internet is 100%
          secure. While we strive to protect your data, we cannot guarantee
          absolute security. We regularly review and update our security
          practices to ensure the highest level of protection.
        </p>
      ),
    },
    {
      icon: faCookie,
      title: "Cookies and Tracking Technologies",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed">
            We use cookies and similar tracking technologies to enhance your
            experience, analyze usage, and provide personalized content. You can
            control cookie settings through your browser preferences.
          </p>
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mt-3">
            <p className="text-sm text-blue-800 font-medium">
              Types of cookies we use:
            </p>
            <ul className="mt-2 space-y-1 text-sm text-blue-700">
              <li>• Essential cookies for platform functionality</li>
              <li>• Analytics cookies to understand user behavior</li>
              <li>• Preference cookies to remember your settings</li>
              <li>• Marketing cookies for personalized advertising</li>
            </ul>
          </div>
        </div>
      ),
    },
    {
      icon: faUserShield,
      title: "Your Rights and Choices",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed">
            You have the following rights regarding your personal information:
          </p>
          <div className="grid md:grid-cols-2 gap-4 mt-3">
            {[
              {
                title: "Access & Update",
                desc: "View and modify your personal information at any time",
              },
              {
                title: "Data Portability",
                desc: "Export your data in a portable, machine-readable format",
              },
              {
                title: "Account Deletion",
                desc: "Request permanent deletion of your account and data",
              },
              {
                title: "Marketing Opt-out",
                desc: "Unsubscribe from promotional communications",
              },
            ].map((item, idx) => (
              <div
                key={idx}
                className="bg-gradient-to-br from-purple-50 to-blue-50 border border-purple-200 rounded-lg p-4"
              >
                <h4 className="font-semibold text-gray-800 mb-1">
                  {item.title}
                </h4>
                <p className="text-sm text-gray-600">{item.desc}</p>
              </div>
            ))}
          </div>
        </div>
      ),
    },
    {
      icon: faShieldAlt,
      title: "Children's Privacy",
      content: (
        <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-5">
          <p className="text-gray-700 leading-relaxed">
            Our services are not intended for users under 13 years of age. Users
            between 13-18 years must have parental or guardian consent to use
            our platform. We do not knowingly collect personal information from
            children under 13. If we become aware of such collection, we will
            take steps to delete the information promptly.
          </p>
        </div>
      ),
    },
    {
      icon: faDatabase,
      title: "Data Retention",
      content: (
        <p className="text-gray-600 leading-relaxed">
          We retain your personal information for as long as necessary to
          provide our services, comply with legal obligations, resolve disputes,
          and enforce our agreements. When you delete your account, we will
          remove your personal information within 30 days, except where we are
          legally required to retain certain data.
        </p>
      ),
    },
    {
      icon: faShieldAlt,
      title: "Changes to Privacy Policy",
      content: (
        <p className="text-gray-600 leading-relaxed">
          We may update this Privacy Policy from time to time to reflect changes
          in our practices or legal requirements. We will notify you of any
          significant changes by posting the new policy on this page, updating
          the "Last Updated" date, and sending you an email notification when
          appropriate. Your continued use of our services after such changes
          constitutes acceptance of the updated policy.
        </p>
      ),
    },
    {
      icon: faEnvelope,
      title: "Contact Us",
      content: (
        <div className="space-y-4">
          <p className="text-gray-600 leading-relaxed">
            If you have any questions, concerns, or requests regarding this
            Privacy Policy or our data practices, please contact us:
          </p>
          <div className="bg-gradient-to-br from-blue-50 to-purple-50 border border-blue-200 rounded-xl p-6">
            <div className="grid md:grid-cols-2 gap-4">
              <div>
                <p className="text-sm font-semibold text-gray-700 mb-1">
                  Company Name
                </p>
                <p className="text-gray-900 font-bold">NetBook Academy</p>
              </div>
              <div>
                <p className="text-sm font-semibold text-gray-700 mb-1">
                  Location
                </p>
                <p className="text-gray-900">Chattogram, Bangladesh</p>
              </div>
              <div>
                <p className="text-sm font-semibold text-gray-700 mb-1">
                  Privacy Email
                </p>
                <a
                  href="mailto:privacy@netbookacademy.com"
                  className="text-blue-600 hover:text-blue-700 font-medium"
                >
                  privacy@netbookacademy.com
                </a>
              </div>
              <div>
                <p className="text-sm font-semibold text-gray-700 mb-1">
                  Support Email
                </p>
                <a
                  href="mailto:support@netbookacademy.com"
                  className="text-blue-600 hover:text-blue-700 font-medium"
                >
                  support@netbookacademy.com
                </a>
              </div>
            </div>
          </div>
        </div>
      ),
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
      <Navbar page="privacy" />

      {/* Hero Section */}
      <div className="bg-gradient-to-r from-blue-600 to-purple-600 text-white py-16">
        <div className="max-w-4xl mx-auto px-6 text-center">
          <div className="inline-flex items-center justify-center w-20 h-20 bg-white/20 rounded-full mb-6">
            <FontAwesomeIcon icon={faShieldAlt} className="text-4xl" />
          </div>
          <h1 className="text-5xl font-bold mb-4">Privacy Policy</h1>
          <p className="text-xl text-blue-100 max-w-2xl mx-auto">
            Your privacy is important to us. Learn how we collect, use, and
            protect your personal information.
          </p>
        </div>
      </div>

      {/* Content */}
      <div className="max-w-4xl mx-auto px-6 py-12">
        <div className="bg-white rounded-2xl shadow-xl overflow-hidden">
          {sections.map((section, index) => (
            <section
              key={index}
              className={`p-8 ${
                index !== sections.length - 1 ? "border-b border-gray-200" : ""
              }`}
            >
              <div className="flex items-start space-x-4 mb-4">
                <div className="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-blue-100 to-purple-100 rounded-lg flex items-center justify-center">
                  <FontAwesomeIcon
                    icon={section.icon}
                    className="text-xl text-blue-600"
                  />
                </div>
                <div className="flex-1">
                  <h2 className="text-2xl font-bold text-gray-900 mb-4">
                    {index + 1}. {section.title}
                  </h2>
                  {section.content}
                </div>
              </div>
            </section>
          ))}

          {/* Last Updated */}
          <div className="bg-gradient-to-r from-gray-50 to-gray-100 px-8 py-6">
            <div className="flex items-center justify-between">
              <p className="text-sm text-gray-600">
                <strong>Last Updated:</strong> January 16, 2026
              </p>
              <p className="text-sm text-gray-600">
                <strong>Version:</strong> 2.0
              </p>
            </div>
          </div>
        </div>

        {/* Additional Info Box */}
        <div className="mt-8 bg-blue-50 border border-blue-200 rounded-xl p-6">
          <h3 className="text-lg font-bold text-gray-900 mb-2">
            Questions About Your Privacy?
          </h3>
          <p className="text-gray-600 mb-4">
            We're here to help. If you have any questions or concerns about how
            we handle your data, don't hesitate to reach out to our privacy team.
          </p>
          <a
            href="mailto:privacy@netbookacademy.com"
            className="inline-flex items-center px-6 py-3 bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white font-semibold rounded-lg shadow-md hover:shadow-lg transition-all duration-300"
          >
            <FontAwesomeIcon icon={faEnvelope} className="mr-2" />
            Contact Privacy Team
          </a>
        </div>
      </div>

      <Footer />
    </div>
  );
}

export default PrivacyPolicy;