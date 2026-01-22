export function InputField({
  id,
  name,
  label,
  type = "text",
  value,
  onChange,
  placeholder,
  required,
  icon,
  error,
  helperText,
  disabled = false,
}) {
  return (
    <div className="space-y-2">
      {/* Label */}
      <label
        htmlFor={id}
        className="block text-sm font-semibold text-gray-700 mb-1.5"
      >
        {label}
        {required && <span className="text-red-500 ml-1">*</span>}
      </label>

      {/* Input Container */}
      <div className="relative">
        {/* Icon */}
        {icon && (
          <div className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none">
            {icon}
          </div>
        )}

        {/* Input Field */}
        <input
          id={id}
          type={type}
          name={name}
          value={value}
          onChange={onChange}
          required={required}
          placeholder={placeholder}
          disabled={disabled}
          className={`
            w-full px-4 py-3 
            ${icon ? "pl-12" : "px-4"}
            border rounded-lg
            text-gray-900 placeholder-gray-400
            transition-all duration-200
            ${
              error
                ? "border-red-500 focus:ring-2 focus:ring-red-500 focus:border-red-500"
                : "border-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
            }
            ${disabled ? "bg-gray-100 cursor-not-allowed" : "bg-white"}
            hover:border-gray-400
            focus:outline-none
          `}
        />
      </div>

      {/* Error Message */}
      {error && (
        <p className="text-sm text-red-600 flex items-center mt-1.5">
          <svg
            className="w-4 h-4 mr-1.5"
            fill="currentColor"
            viewBox="0 0 20 20"
          >
            <path
              fillRule="evenodd"
              d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z"
              clipRule="evenodd"
            />
          </svg>
          {error}
        </p>
      )}

      {/* Helper Text */}
      {!error && helperText && (
        <p className="text-sm text-gray-500 mt-1.5">{helperText}</p>
      )}
    </div>
  );
}

// Optional: Textarea variant
export function TextAreaField({
  id,
  name,
  label,
  value,
  onChange,
  placeholder,
  required,
  error,
  helperText,
  disabled = false,
  rows = 4,
}) {
  return (
    <div className="space-y-2">
      {/* Label */}
      <label
        htmlFor={id}
        className="block text-sm font-semibold text-gray-700 mb-1.5"
      >
        {label}
        {required && <span className="text-red-500 ml-1">*</span>}
      </label>

      {/* Textarea */}
      <textarea
        id={id}
        name={name}
        value={value}
        onChange={onChange}
        required={required}
        placeholder={placeholder}
        disabled={disabled}
        rows={rows}
        className={`
          w-full px-4 py-3 
          border rounded-lg
          text-gray-900 placeholder-gray-400
          transition-all duration-200
          resize-vertical
          ${
            error
              ? "border-red-500 focus:ring-2 focus:ring-red-500 focus:border-red-500"
              : "border-gray-300 focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
          }
          ${disabled ? "bg-gray-100 cursor-not-allowed" : "bg-white"}
          hover:border-gray-400
          focus:outline-none
        `}
      />

      {/* Error Message */}
      {error && (
        <p className="text-sm text-red-600 flex items-center mt-1.5">
          <svg
            className="w-4 h-4 mr-1.5"
            fill="currentColor"
            viewBox="0 0 20 20"
          >
            <path
              fillRule="evenodd"
              d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-7 4a1 1 0 11-2 0 1 1 0 012 0zm-1-9a1 1 0 00-1 1v4a1 1 0 102 0V6a1 1 0 00-1-1z"
              clipRule="evenodd"
            />
          </svg>
          {error}
        </p>
      )}

      {/* Helper Text */}
      {!error && helperText && (
        <p className="text-sm text-gray-500 mt-1.5">{helperText}</p>
      )}
    </div>
  );
}