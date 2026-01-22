import React from "react";
import Navbar from "../../Components/common/Navbar";
import Footer from "../../Components/common/Footer";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faFileContract,
  faUserCheck,
  faGraduationCap,
  faCreditCard,
  faUserShield,
  faCopyright,
  faCertificate,
  faExclamationTriangle,
  faBan,
  faLock,
  faGavel,
  faEnvelope,
} from "@fortawesome/free-solid-svg-icons";

function TermsAndConditions() {
  const sections = [
    {
      icon: faFileContract,
      title: "Acceptance of Terms",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed">
            By accessing and using NetBook Academy's platform, you agree to be
            bound by these Terms and Conditions, all applicable laws and
            regulations, and agree that you are responsible for compliance with
            any applicable local laws.
          </p>
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4">
            <p className="text-sm text-blue-800 font-medium">
              If you do not agree to these terms, please do not use our
              services. Your continued use of the platform constitutes
              acceptance of any updates to these terms.
            </p>
          </div>
        </div>
      ),
    },
    {
      icon: faUserCheck,
      title: "User Accounts",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed mb-3">
            To access certain features, you must create an account and agree to
            the following:
          </p>
          <div className="grid md:grid-cols-2 gap-4">
            {[
              {
                title: "Age Requirement",
                desc: "You must be at least 13 years old to create an account",
              },
              {
                title: "Parental Consent",
                desc: "Users under 18 must have parental or guardian consent",
              },
              {
                title: "Account Security",
                desc: "You are responsible for maintaining credential confidentiality",
              },
              {
                title: "Accurate Information",
                desc: "You must provide truthful and complete information",
              },
              {
                title: "Single Account",
                desc: "One account per user; sharing accounts is prohibited",
              },
              {
                title: "Account Activity",
                desc: "You are responsible for all activities under your account",
              },
            ].map((item, idx) => (
              <div
                key={idx}
                className="bg-gradient-to-br from-blue-50 to-purple-50 border border-blue-200 rounded-lg p-4"
              >
                <h4 className="font-semibold text-gray-800 mb-1 text-sm">
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
      icon: faGraduationCap,
      title: "Course Enrollment and Access",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed mb-3">
            Course enrollment is subject to the following terms:
          </p>
          <ul className="space-y-2 ml-6">
            {[
              "Course fees must be paid in full before accessing course materials",
              "Access to course content is granted for the duration specified at purchase",
              "Course materials are for personal, non-commercial use only and may not be shared or redistributed",
              "Live class schedules are subject to change with prior notice via email",
              "Recording, screen-capturing, or downloading of live classes is strictly prohibited",
              "Course content may be updated or modified to improve quality and relevance",
              "Enrollment in some courses may require completion of prerequisites",
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
      icon: faCreditCard,
      title: "Payment and Refunds",
      content: (
        <div className="space-y-4">
          <p className="text-gray-600 leading-relaxed">
            Payment and refund policies:
          </p>
          <div className="bg-green-50 border border-green-200 rounded-lg p-5">
            <h4 className="font-semibold text-green-800 mb-3">
              Payment Terms
            </h4>
            <ul className="space-y-2 text-sm text-gray-700">
              <li className="flex items-start">
                <span className="w-2 h-2 bg-green-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                All payments are processed through secure third-party payment
                gateways
              </li>
              <li className="flex items-start">
                <span className="w-2 h-2 bg-green-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                Prices are subject to change without prior notice
              </li>
              <li className="flex items-start">
                <span className="w-2 h-2 bg-green-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                Promotional discounts cannot be combined with other offers
              </li>
            </ul>
          </div>
          <div className="bg-yellow-50 border border-yellow-200 rounded-lg p-5">
            <h4 className="font-semibold text-yellow-800 mb-3">
              Refund Policy
            </h4>
            <ul className="space-y-2 text-sm text-gray-700">
              <li className="flex items-start">
                <span className="w-2 h-2 bg-yellow-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                Refunds are available within 7 days of purchase if less than
                30% of course content has been accessed
              </li>
              <li className="flex items-start">
                <span className="w-2 h-2 bg-yellow-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                Failed transactions will be refunded within 5-7 business days
              </li>
              <li className="flex items-start">
                <span className="w-2 h-2 bg-yellow-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                Refund requests must be submitted through official support
                channels
              </li>
            </ul>
          </div>
        </div>
      ),
    },
    {
      icon: faUserShield,
      title: "User Conduct",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed mb-3">
            All users must adhere to the following code of conduct:
          </p>
          <div className="grid gap-3">
            {[
              {
                type: "success",
                items: [
                  "Treat all users, instructors, and staff with respect and professionalism",
                  "Engage in constructive discussions and provide helpful feedback",
                  "Report any violations or inappropriate behavior promptly",
                ],
              },
              {
                type: "danger",
                items: [
                  "Do not post offensive, abusive, discriminatory, or inappropriate content",
                  "Do not engage in cheating, plagiarism, or academic dishonesty",
                  "Do not share copyrighted course materials with unauthorized parties",
                  "Do not attempt to hack, disrupt, or compromise platform security",
                  "Do not impersonate other users, instructors, or staff members",
                  "Do not use the platform for spam, advertising, or solicitation",
                ],
              },
            ].map((group, groupIdx) => (
              <div
                key={groupIdx}
                className={`border rounded-lg p-4 ${
                  group.type === "success"
                    ? "bg-green-50 border-green-200"
                    : "bg-red-50 border-red-200"
                }`}
              >
                <ul className="space-y-2">
                  {group.items.map((item, idx) => (
                    <li
                      key={idx}
                      className={`flex items-start text-sm ${
                        group.type === "success"
                          ? "text-green-800"
                          : "text-red-800"
                      }`}
                    >
                      <span
                        className={`inline-block w-2 h-2 rounded-full mt-1.5 mr-3 flex-shrink-0 ${
                          group.type === "success"
                            ? "bg-green-500"
                            : "bg-red-500"
                        }`}
                      ></span>
                      <span>{item}</span>
                    </li>
                  ))}
                </ul>
              </div>
            ))}
          </div>
        </div>
      ),
    },
    {
      icon: faCopyright,
      title: "Intellectual Property",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed">
            All course materials, including videos, documents, assignments,
            quizzes, and other content, are the intellectual property of NetBook
            Academy or its content providers and are protected by copyright
            laws.
          </p>
          <div className="bg-red-50 border border-red-200 rounded-lg p-5">
            <h4 className="font-semibold text-red-800 mb-3 flex items-center">
              <FontAwesomeIcon icon={faBan} className="mr-2" />
              Prohibited Actions
            </h4>
            <ul className="space-y-2 text-sm text-gray-700">
              {[
                "Reproduce, distribute, or publicly display course materials",
                "Create derivative works from course content",
                "Use course materials for commercial purposes",
                "Remove copyright, watermark, or proprietary notices",
                "Share login credentials or course access with others",
              ].map((item, idx) => (
                <li key={idx} className="flex items-start">
                  <FontAwesomeIcon
                    icon={faBan}
                    className="text-red-500 mt-0.5 mr-3 flex-shrink-0"
                  />
                  <span>{item}</span>
                </li>
              ))}
            </ul>
          </div>
        </div>
      ),
    },
    {
      icon: faCertificate,
      title: "Certificates and Completion",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed mb-3">
            Certificates are awarded upon successful completion of courses.
            Requirements include:
          </p>
          <div className="grid md:grid-cols-2 gap-4">
            {[
              "Completion of all course modules and lessons",
              "Achieving minimum passing scores (typically 70%) on all assessments",
              "Meeting attendance requirements for live classes (if applicable)",
              "Submission of all required assignments and projects",
            ].map((item, idx) => (
              <div
                key={idx}
                className="bg-gradient-to-br from-yellow-50 to-orange-50 border border-yellow-200 rounded-lg p-4"
              >
                <div className="flex items-start">
                  <FontAwesomeIcon
                    icon={faCertificate}
                    className="text-yellow-600 mt-1 mr-3 flex-shrink-0"
                  />
                  <p className="text-sm text-gray-700">{item}</p>
                </div>
              </div>
            ))}
          </div>
          <div className="bg-blue-50 border border-blue-200 rounded-lg p-4 mt-3">
            <p className="text-sm text-blue-800">
              <strong>Note:</strong> Certificates are issued in digital format
              and can be verified through our platform. Physical certificates
              may be available for an additional fee.
            </p>
          </div>
        </div>
      ),
    },
    {
      icon: faExclamationTriangle,
      title: "Limitation of Liability",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed mb-3">
            NetBook Academy is provided on an "as is" and "as available" basis.
            We make no warranties or guarantees regarding:
          </p>
          <div className="bg-yellow-50 border-l-4 border-yellow-500 p-5">
            <ul className="space-y-2 text-sm text-gray-700">
              {[
                "Uninterrupted, error-free, or secure service availability",
                "Specific learning outcomes, job placement, or career advancement",
                "Complete accuracy of all course materials or information",
                "Compatibility with all devices, browsers, or operating systems",
                "Third-party content, links, or services referenced in courses",
              ].map((item, idx) => (
                <li key={idx} className="flex items-start">
                  <FontAwesomeIcon
                    icon={faExclamationTriangle}
                    className="text-yellow-600 mt-0.5 mr-3 flex-shrink-0"
                  />
                  <span>{item}</span>
                </li>
              ))}
            </ul>
          </div>
        </div>
      ),
    },
    {
      icon: faBan,
      title: "Account Termination",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed">
            We reserve the right to suspend or permanently terminate your
            account immediately, without prior notice, if you:
          </p>
          <div className="bg-red-50 border border-red-200 rounded-lg p-5">
            <ul className="space-y-2 text-sm text-gray-700">
              {[
                "Violate these Terms and Conditions or our Privacy Policy",
                "Engage in fraudulent activities or payment disputes",
                "Abuse, harass, threaten, or harm other users or staff",
                "Share, resell, or provide unauthorized course access",
                "Attempt to circumvent security measures or access controls",
                "Use automated tools or bots to access the platform",
              ].map((item, idx) => (
                <li key={idx} className="flex items-start">
                  <span className="inline-block w-2 h-2 bg-red-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                  <span>{item}</span>
                </li>
              ))}
            </ul>
            <p className="text-sm text-red-800 font-medium mt-4">
              Note: Terminated accounts are not eligible for refunds.
            </p>
          </div>
        </div>
      ),
    },
    {
      icon: faLock,
      title: "Privacy and Data Protection",
      content: (
        <p className="text-gray-600 leading-relaxed">
          Your use of NetBook Academy is also governed by our Privacy Policy,
          which explains how we collect, use, store, and protect your personal
          information. We are committed to maintaining the confidentiality and
          security of your data in compliance with applicable data protection
          laws. Please review our{" "}
          <a
            href="/privacy-policy"
            className="text-blue-600 hover:text-blue-700 font-medium underline"
          >
            Privacy Policy
          </a>{" "}
          to understand your rights and our obligations.
        </p>
      ),
    },
    {
      icon: faFileContract,
      title: "Changes to Terms",
      content: (
        <div className="bg-purple-50 border border-purple-200 rounded-lg p-5">
          <p className="text-gray-700 leading-relaxed">
            We reserve the right to modify these Terms and Conditions at any
            time. Material changes will be communicated via email and/or
            prominent notice on our platform. Continued use of the platform
            after changes constitutes acceptance of the modified terms. We
            recommend reviewing these terms periodically to stay informed of any
            updates.
          </p>
          <p className="text-sm text-purple-800 font-medium mt-3">
            Last major revision: January 16, 2026
          </p>
        </div>
      ),
    },
    {
      icon: faGavel,
      title: "Governing Law and Dispute Resolution",
      content: (
        <div className="space-y-3">
          <p className="text-gray-600 leading-relaxed">
            These Terms and Conditions are governed by and construed in
            accordance with the laws of Bangladesh, without regard to its
            conflict of law provisions.
          </p>
          <div className="bg-gray-50 border border-gray-200 rounded-lg p-5">
            <h4 className="font-semibold text-gray-800 mb-3">
              Dispute Resolution
            </h4>
            <ul className="space-y-2 text-sm text-gray-700">
              <li className="flex items-start">
                <span className="inline-block w-2 h-2 bg-gray-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                Any disputes shall first be attempted to be resolved through
                good faith negotiation
              </li>
              <li className="flex items-start">
                <span className="inline-block w-2 h-2 bg-gray-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                If unresolved, disputes shall be resolved in the courts of
                Dhaka, Bangladesh
              </li>
              <li className="flex items-start">
                <span className="inline-block w-2 h-2 bg-gray-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                You agree to submit to the personal jurisdiction of such courts
              </li>
            </ul>
          </div>
        </div>
      ),
    },
    {
      icon: faEnvelope,
      title: "Contact Information",
      content: (
        <div className="space-y-4">
          <p className="text-gray-600 leading-relaxed">
            For questions, concerns, or requests regarding these Terms and
            Conditions, please contact us:
          </p>
          <div className="bg-gradient-to-br from-blue-50 to-purple-50 border border-blue-200 rounded-xl p-6">
            <div className="grid md:grid-cols-2 gap-6">
              <div>
                <p className="text-sm font-semibold text-gray-700 mb-2">
                  General Support
                </p>
                <a
                  href="mailto:support@netbookacademy.com"
                  className="text-blue-600 hover:text-blue-700 font-medium flex items-center"
                >
                  <FontAwesomeIcon icon={faEnvelope} className="mr-2" />
                  support@netbookacademy.com
                </a>
              </div>
              <div>
                <p className="text-sm font-semibold text-gray-700 mb-2">
                  Legal Inquiries
                </p>
                <a
                  href="mailto:legal@netbookacademy.com"
                  className="text-blue-600 hover:text-blue-700 font-medium flex items-center"
                >
                  <FontAwesomeIcon icon={faEnvelope} className="mr-2" />
                  legal@netbookacademy.com
                </a>
              </div>
              <div>
                <p className="text-sm font-semibold text-gray-700 mb-2">
                  Company Name
                </p>
                <p className="text-gray-900 font-bold">NetBook Academy</p>
              </div>
              <div>
                <p className="text-sm font-semibold text-gray-700 mb-2">
                  Location
                </p>
                <p className="text-gray-900">Chattogram, Bangladesh</p>
              </div>
            </div>
          </div>
        </div>
      ),
    },
  ];

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
      <Navbar page="terms" />

      {/* Hero Section */}
      <div className="bg-gradient-to-r from-purple-600 to-blue-600 text-white py-16">
        <div className="max-w-4xl mx-auto px-6 text-center">
          <div className="inline-flex items-center justify-center w-20 h-20 bg-white/20 rounded-full mb-6">
            <FontAwesomeIcon icon={faFileContract} className="text-4xl" />
          </div>
          <h1 className="text-5xl font-bold mb-4">Terms and Conditions</h1>
          <p className="text-xl text-purple-100 max-w-2xl mx-auto">
            Please read these terms carefully before using our platform. They
            govern your access to and use of NetBook Academy.
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
                <div className="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-purple-100 to-blue-100 rounded-lg flex items-center justify-center">
                  <FontAwesomeIcon
                    icon={section.icon}
                    className="text-xl text-purple-600"
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

          {/* Agreement Footer */}
          <div className="bg-gradient-to-r from-gray-50 to-gray-100 px-8 py-6">
            <div className="flex items-center justify-between flex-wrap gap-4">
              <div>
                <p className="text-sm text-gray-600">
                  <strong>Last Updated:</strong> January 16, 2026
                </p>
                <p className="text-sm text-gray-600 mt-1">
                  <strong>Effective Date:</strong> January 1, 2026
                </p>
              </div>
              <p className="text-sm text-gray-600">
                <strong>Version:</strong> 2.0
              </p>
            </div>
            <p className="text-sm text-gray-600 mt-4 pt-4 border-t border-gray-300">
              By using NetBook Academy, you acknowledge that you have read,
              understood, and agree to be bound by these Terms and Conditions
              and our Privacy Policy.
            </p>
          </div>
        </div>

        {/* CTA Box */}
        <div className="mt-8 bg-gradient-to-r from-blue-600 to-purple-600 rounded-xl p-6 text-white">
          <div className="flex items-center justify-between flex-wrap gap-4">
            <div>
              <h3 className="text-lg font-bold mb-1">Need Clarification?</h3>
              <p className="text-blue-100 text-sm">
                Our legal team is here to answer your questions about these
                terms.
              </p>
            </div>
            <a
              href="mailto:legal@netbookacademy.com"
              className="inline-flex items-center px-6 py-3 bg-white text-blue-600 font-semibold rounded-lg shadow-md hover:shadow-lg transition-all duration-300 hover:scale-105"
            >
              <FontAwesomeIcon icon={faEnvelope} className="mr-2" />
              Contact Legal Team
            </a>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
}

export default TermsAndConditions;