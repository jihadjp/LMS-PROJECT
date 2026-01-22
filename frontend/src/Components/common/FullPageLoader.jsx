import React from 'react';
import { Loader2 } from 'lucide-react'; // এটি ব্যবহার করতে `npm install lucide-react` করুন

const FullPageLoader = () => {
  return (
    <div className="fixed inset-0 z-[10000] flex flex-col items-center justify-center bg-white/30 backdrop-blur-md transition-all duration-500">
      <div className="bg-white/80 p-10 rounded-3xl shadow-2xl flex flex-col items-center border border-white/50 scale-110">
        <Loader2 className="h-14 w-14 text-blue-600 animate-spin mb-4" />
        <h3 className="text-xl font-bold bg-gradient-to-r from-blue-600 to-purple-600 bg-clip-text text-transparent">
          Verifying Session...
        </h3>
        <p className="text-gray-500 text-sm mt-2 font-medium">Please wait a moment</p>
      </div>
    </div>
  );
};

export default FullPageLoader;