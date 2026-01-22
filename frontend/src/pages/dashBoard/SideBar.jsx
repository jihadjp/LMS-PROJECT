import img1 from "../../assets/images/user.png";

function SideBar({ current, onSelect, onLogout }) {
  const menuItems = [
    { key: "dashboard", label: "Dashboard", icon: "bx bxs-dashboard" },
    { key: "user", label: "Users", icon: "bx bxs-group" },
    { key: "courses", label: "Courses", icon: "bx bxs-book" },
  ];

  return (
    <div className="bg-white shadow-lg flex flex-col p-4 px-10">
      <div
        className="flex items-center gap-3 px-3 py-5 border-b border-gray-200 cursor-pointer"
        onClick={() => onSelect("dashboard")}
      >
        <img src={img1} alt="Admin Logo" className="w-10 h-10 rounded-full" />
        <span className="text-lg font-semibold text-blue-900">Admin Panel</span>
      </div>
      <ul className="flex flex-col mt-6">
        {menuItems.map((item) => (
          <li key={item.key}>
            <button
              onClick={() => onSelect(item.key)}
              className={`w-full flex items-center gap-3 p-3 transition-colors rounded-lg mx-3 mb-3 text-left ${
                current === item.key
                  ? "bg-blue-500 text-white shadow-md"
                  : "text-gray-700 hover:bg-gray-100"
              }`}
            >
              <i className={`${item.icon} text-lg`} />
              <span className="font-medium">{item.label}</span>
            </button>
          </li>
        ))}
      </ul>
      <button
        onClick={onLogout}
        className="logout-button mt-auto p-3 text-red-600 hover:bg-red-50 rounded-lg transition-colors"
      >
        Logout
      </button>
    </div>
  );
}

export default SideBar;
