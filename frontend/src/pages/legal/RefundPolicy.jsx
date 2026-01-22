import React from "react";
import Navbar from "../../Components/common/Navbar";
import Footer from "../../Components/common/Footer";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faMoneyBillWave,
  faCalendarCheck,
  faExclamationCircle,
  faClock,
  faCheckCircle,
  faTimesCircle,
  faEnvelope,
  faInfoCircle,
  faShieldAlt,
} from "@fortawesome/free-solid-svg-icons";

function RefundPolicy() {
  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 via-white to-purple-50">
      <Navbar page="refund" />

      {/* Hero Section */}
      <div className="bg-gradient-to-r from-green-600 to-blue-600 text-white py-16">
        <div className="max-w-4xl mx-auto px-6 text-center">
          <div className="inline-flex items-center justify-center w-20 h-20 bg-white/20 rounded-full mb-6">
            <FontAwesomeIcon icon={faMoneyBillWave} className="text-4xl" />
          </div>
          <h1 className="text-5xl font-bold mb-4">Refund & Cancellation Policy</h1>
          <p className="text-xl text-green-100 max-w-2xl mx-auto">
            Understanding our refund policy helps ensure a smooth experience for all learners
          </p>
        </div>
      </div>

      {/* Content */}
      <div className="max-w-4xl mx-auto px-6 py-12">
        {/* Important Notice */}
        <div className="bg-yellow-50 border-l-4 border-yellow-500 rounded-lg p-6 mb-8 shadow-md">
          <div className="flex items-start">
            <FontAwesomeIcon
              icon={faExclamationCircle}
              className="text-yellow-600 text-2xl mr-4 mt-1"
            />
            <div>
              <h3 className="text-lg font-bold text-yellow-800 mb-2">
                Important Notice
              </h3>
              <p className="text-yellow-700 leading-relaxed">
                Please read this refund policy carefully before enrolling in any course. 
                By enrolling, you agree to these terms and conditions.
              </p>
            </div>
          </div>
        </div>

        {/* Main Content Card */}
        <div className="bg-white rounded-2xl shadow-xl overflow-hidden">
          {/* Section 1: Before Course Starts */}
          <section className="p-8 border-b border-gray-200">
            <div className="flex items-start space-x-4 mb-4">
              <div className="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-red-100 to-pink-100 rounded-lg flex items-center justify-center">
                <FontAwesomeIcon icon={faTimesCircle} className="text-xl text-red-600" />
              </div>
              <div className="flex-1">
                <h2 className="text-2xl font-bold text-gray-900 mb-4">
                  1. No Refund Before Course Starts
                </h2>
                <div className="bg-red-50 border border-red-200 rounded-lg p-5">
                  <p className="text-gray-700 leading-relaxed mb-3">
                    If you enroll in any course at NetBook Academy, you{" "}
                    <strong className="text-red-600">cannot request a refund</strong>{" "}
                    (full or partial) before your main live class or batch begins.
                  </p>
                  <div className="flex items-start mt-3">
                    <FontAwesomeIcon
                      icon={faInfoCircle}
                      className="text-red-500 mt-1 mr-3 flex-shrink-0"
                    />
                    <p className="text-sm text-gray-600">
                      This policy ensures commitment and helps us plan resources effectively 
                      for each batch.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </section>

          {/* Section 2: First Week No Refund */}
          <section className="p-8 border-b border-gray-200">
            <div className="flex items-start space-x-4 mb-4">
              <div className="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-orange-100 to-yellow-100 rounded-lg flex items-center justify-center">
                <FontAwesomeIcon icon={faCalendarCheck} className="text-xl text-orange-600" />
              </div>
              <div className="flex-1">
                <h2 className="text-2xl font-bold text-gray-900 mb-4">
                  2. First Module/Week Completion
                </h2>
                <div className="bg-orange-50 border border-orange-200 rounded-lg p-5">
                  <p className="text-gray-700 leading-relaxed mb-4">
                    Once your course's main live class or batch starts, you will{" "}
                    <strong className="text-orange-600">not receive any refund</strong>{" "}
                    until the first module (or 1 week) is completed.
                  </p>
                  <div className="grid md:grid-cols-2 gap-4 mt-4">
                    <div className="bg-white border border-orange-200 rounded-lg p-4">
                      <div className="flex items-center mb-2">
                        <FontAwesomeIcon
                          icon={faClock}
                          className="text-orange-500 mr-2"
                        />
                        <span className="font-semibold text-gray-800">Timeline</span>
                      </div>
                      <p className="text-sm text-gray-600">
                        First module or 1 week must be completed
                      </p>
                    </div>
                    <div className="bg-white border border-orange-200 rounded-lg p-4">
                      <div className="flex items-center mb-2">
                        <FontAwesomeIcon
                          icon={faShieldAlt}
                          className="text-orange-500 mr-2"
                        />
                        <span className="font-semibold text-gray-800">Purpose</span>
                      </div>
                      <p className="text-sm text-gray-600">
                        Evaluate course quality and relevance
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          {/* Section 3: Refund Eligibility */}
          <section className="p-8 border-b border-gray-200">
            <div className="flex items-start space-x-4 mb-4">
              <div className="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-green-100 to-emerald-100 rounded-lg flex items-center justify-center">
                <FontAwesomeIcon icon={faCheckCircle} className="text-xl text-green-600" />
              </div>
              <div className="flex-1">
                <h2 className="text-2xl font-bold text-gray-900 mb-4">
                  3. When You Can Request a Refund
                </h2>
                <div className="bg-green-50 border border-green-200 rounded-lg p-5">
                  <p className="text-gray-700 leading-relaxed mb-4">
                    After completing 1 module or 1 week of your course, you may request 
                    a refund <strong className="text-green-600">subject to conditions</strong>.
                  </p>
                  <div className="bg-yellow-50 border-l-4 border-yellow-500 p-4 rounded">
                    <div className="flex items-start">
                      <FontAwesomeIcon
                        icon={faExclamationCircle}
                        className="text-yellow-600 mt-1 mr-3 flex-shrink-0"
                      />
                      <div>
                        <p className="font-semibold text-yellow-800 mb-1">
                          Limited Time Window
                        </p>
                        <p className="text-sm text-yellow-700">
                          You have only <strong>1 week (7 days)</strong> to claim your refund 
                          after the first module/week is completed. After this period, refund 
                          requests will not be accepted.
                        </p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          {/* Section 4: Promo Code Policy */}
          <section className="p-8 border-b border-gray-200">
            <div className="flex items-start space-x-4 mb-4">
              <div className="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-purple-100 to-pink-100 rounded-lg flex items-center justify-center">
                <FontAwesomeIcon icon={faInfoCircle} className="text-xl text-purple-600" />
              </div>
              <div className="flex-1">
                <h2 className="text-2xl font-bold text-gray-900 mb-4">
                  4. Promotional Code Policy
                </h2>
                <div className="bg-purple-50 border border-purple-200 rounded-lg p-5">
                  <p className="text-gray-700 leading-relaxed">
                    If you enroll without applying a promotional code, you{" "}
                    <strong className="text-purple-600">cannot request a refund</strong>{" "}
                    equivalent to the promotional discount amount later.
                  </p>
                  <div className="mt-4 flex items-start">
                    <FontAwesomeIcon
                      icon={faInfoCircle}
                      className="text-purple-500 mt-1 mr-3 flex-shrink-0"
                    />
                    <p className="text-sm text-gray-600">
                      Please ensure you apply all available promotional codes before 
                      completing your enrollment.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </section>

          {/* Section 5: Refund Conditions */}
          <section className="p-8 border-b border-gray-200">
            <div className="flex items-start space-x-4 mb-4">
              <div className="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-blue-100 to-indigo-100 rounded-lg flex items-center justify-center">
                <FontAwesomeIcon icon={faShieldAlt} className="text-xl text-blue-600" />
              </div>
              <div className="flex-1">
                <h2 className="text-2xl font-bold text-gray-900 mb-4">
                  5. Refund Eligibility Conditions
                </h2>
                <div className="space-y-4">
                  <p className="text-gray-600 leading-relaxed">
                    To be eligible for a refund, the following conditions must be met:
                  </p>

                  {/* Condition Cards */}
                  <div className="grid gap-4">
                    <div className="bg-gradient-to-br from-blue-50 to-indigo-50 border border-blue-200 rounded-lg p-5">
                      <div className="flex items-start">
                        <div className="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center flex-shrink-0 mr-3">
                          <span className="text-white font-bold text-sm">1</span>
                        </div>
                        <div>
                          <h4 className="font-semibold text-gray-800 mb-1">
                            Full Payment Required
                          </h4>
                          <p className="text-sm text-gray-600">
                            Your course must be fully paid. Partial payments are not 
                            eligible for refund requests.
                          </p>
                        </div>
                      </div>
                    </div>

                    <div className="bg-gradient-to-br from-blue-50 to-indigo-50 border border-blue-200 rounded-lg p-5">
                      <div className="flex items-start">
                        <div className="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center flex-shrink-0 mr-3">
                          <span className="text-white font-bold text-sm">2</span>
                        </div>
                        <div>
                          <h4 className="font-semibold text-gray-800 mb-1">
                            Valid Reason Required
                          </h4>
                          <p className="text-sm text-gray-600">
                            You must provide a valid and reasonable explanation for 
                            requesting a refund.
                          </p>
                        </div>
                      </div>
                    </div>

                    <div className="bg-gradient-to-br from-blue-50 to-indigo-50 border border-blue-200 rounded-lg p-5">
                      <div className="flex items-start">
                        <div className="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center flex-shrink-0 mr-3">
                          <span className="text-white font-bold text-sm">3</span>
                        </div>
                        <div>
                          <h4 className="font-semibold text-gray-800 mb-1">
                            Service-Related Issues Only
                          </h4>
                          <p className="text-sm text-gray-600">
                            Refunds are only granted if NetBook Academy fails to deliver 
                            the services promised before course enrollment.
                          </p>
                        </div>
                      </div>
                    </div>

                    <div className="bg-gradient-to-br from-blue-50 to-indigo-50 border border-blue-200 rounded-lg p-5">
                      <div className="flex items-start">
                        <div className="w-8 h-8 bg-blue-500 rounded-full flex items-center justify-center flex-shrink-0 mr-3">
                          <span className="text-white font-bold text-sm">4</span>
                        </div>
                        <div>
                          <h4 className="font-semibold text-gray-800 mb-1">
                            7-Day Request Window
                          </h4>
                          <p className="text-sm text-gray-600">
                            You have only 1 week (7 days) after the first module/week to 
                            submit your refund request. Requests after this period will be 
                            rejected.
                          </p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          {/* Section 6: Processing Time */}
          <section className="p-8 border-b border-gray-200">
            <div className="flex items-start space-x-4 mb-4">
              <div className="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-teal-100 to-cyan-100 rounded-lg flex items-center justify-center">
                <FontAwesomeIcon icon={faClock} className="text-xl text-teal-600" />
              </div>
              <div className="flex-1">
                <h2 className="text-2xl font-bold text-gray-900 mb-4">
                  6. Refund Processing Timeline
                </h2>
                <div className="bg-teal-50 border border-teal-200 rounded-lg p-5">
                  <p className="text-gray-700 leading-relaxed mb-4">
                    If your refund request is approved after verification, the refund 
                    will be processed within the following timeline:
                  </p>
                  <div className="bg-white border border-teal-300 rounded-lg p-6">
                    <div className="flex items-center justify-center mb-4">
                      <div className="text-center">
                        <div className="text-4xl font-bold text-teal-600 mb-2">
                          14-21 Days
                        </div>
                        <p className="text-sm text-gray-600">
                          From approval to refund completion
                        </p>
                      </div>
                    </div>
                    <div className="flex items-start mt-4 pt-4 border-t border-gray-200">
                      <FontAwesomeIcon
                        icon={faInfoCircle}
                        className="text-teal-500 mt-1 mr-3 flex-shrink-0"
                      />
                      <p className="text-sm text-gray-600">
                        The refund will be credited to your original payment method. 
                        Processing times may vary based on your bank or payment provider.
                      </p>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          {/* Section 7: Contact Information */}
          <section className="p-8">
            <div className="flex items-start space-x-4 mb-4">
              <div className="flex-shrink-0 w-12 h-12 bg-gradient-to-br from-indigo-100 to-purple-100 rounded-lg flex items-center justify-center">
                <FontAwesomeIcon icon={faEnvelope} className="text-xl text-indigo-600" />
              </div>
              <div className="flex-1">
                <h2 className="text-2xl font-bold text-gray-900 mb-4">
                  7. How to Request a Refund
                </h2>
                <div className="bg-gradient-to-br from-indigo-50 to-purple-50 border border-indigo-200 rounded-xl p-6">
                  <p className="text-gray-700 leading-relaxed mb-4">
                    To request a refund, please email us with the following information:
                  </p>
                  <ul className="space-y-2 mb-6 text-sm text-gray-600">
                    <li className="flex items-start">
                      <span className="inline-block w-2 h-2 bg-indigo-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                      Your full name and registered email address
                    </li>
                    <li className="flex items-start">
                      <span className="inline-block w-2 h-2 bg-indigo-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                      Course name and enrollment date
                    </li>
                    <li className="flex items-start">
                      <span className="inline-block w-2 h-2 bg-indigo-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                      Detailed reason for refund request
                    </li>
                    <li className="flex items-start">
                      <span className="inline-block w-2 h-2 bg-indigo-500 rounded-full mt-1.5 mr-3 flex-shrink-0"></span>
                      Transaction ID or payment receipt
                    </li>
                  </ul>
                  <div className="bg-white border border-indigo-300 rounded-lg p-5">
                    <div className="flex items-center justify-between flex-wrap gap-4">
                      <div>
                        <p className="text-sm font-semibold text-gray-700 mb-1">
                          Refund Support Email
                        </p>
                        <a
                          href="mailto:support@netbookacademy.com"
                          className="text-indigo-600 hover:text-indigo-700 font-bold text-lg"
                        >
                          support@netbookacademy.com
                        </a>
                      </div>
                      <a
                        href="mailto:support@netbookacademy.com"
                        className="inline-flex items-center px-6 py-3 bg-gradient-to-r from-indigo-600 to-purple-600 hover:from-indigo-700 hover:to-purple-700 text-white font-semibold rounded-lg shadow-md hover:shadow-lg transition-all duration-300"
                      >
                        <FontAwesomeIcon icon={faEnvelope} className="mr-2" />
                        Request Refund
                      </a>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </section>

          {/* Footer */}
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
                <strong>Version:</strong> 1.0
              </p>
            </div>
          </div>
        </div>

        {/* Quick Summary Box */}
        <div className="mt-8 bg-gradient-to-r from-blue-600 to-purple-600 rounded-xl p-6 text-white">
          <h3 className="text-xl font-bold mb-4 flex items-center">
            <FontAwesomeIcon icon={faInfoCircle} className="mr-3" />
            Quick Summary
          </h3>
          <div className="grid md:grid-cols-3 gap-4">
            <div className="bg-white/10 backdrop-blur-sm rounded-lg p-4">
              <div className="text-3xl font-bold mb-2">7 Days</div>
              <p className="text-sm text-blue-100">Request window after first week</p>
            </div>
            <div className="bg-white/10 backdrop-blur-sm rounded-lg p-4">
              <div className="text-3xl font-bold mb-2">14-21 Days</div>
              <p className="text-sm text-blue-100">Refund processing time</p>
            </div>
            <div className="bg-white/10 backdrop-blur-sm rounded-lg p-4">
              <div className="text-3xl font-bold mb-2">100%</div>
              <p className="text-sm text-blue-100">Full payment required</p>
            </div>
          </div>
        </div>

        {/* Additional Help */}
        <div className="mt-8 bg-blue-50 border border-blue-200 rounded-xl p-6">
          <h3 className="text-lg font-bold text-gray-900 mb-2">
            Need Help Understanding Our Refund Policy?
          </h3>
          <p className="text-gray-600 mb-4">
            Our support team is available to answer any questions you may have about 
            refunds and cancellations.
          </p>
          <div className="flex flex-wrap gap-3">
            <a
              href="mailto:support@netbookacademy.com"
              className="inline-flex items-center px-5 py-2.5 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-lg shadow-md hover:shadow-lg transition-all duration-300"
            >
              <FontAwesomeIcon icon={faEnvelope} className="mr-2" />
              Email Support
            </a>
            <a
              href="/terms-and-conditions"
              className="inline-flex items-center px-5 py-2.5 bg-white hover:bg-gray-50 text-blue-600 font-semibold rounded-lg border-2 border-blue-600 transition-all duration-300"
            >
              View Terms & Conditions
            </a>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
}

export default RefundPolicy;