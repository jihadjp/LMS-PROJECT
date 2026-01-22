import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { authService } from '../../src/api/auth.service';

const UserContext = createContext();

export const useUserContext = () => useContext(UserContext);

export const UserProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    const logout = useCallback(() => {
        console.log("Logging out and clearing storage...");
        localStorage.clear();
        setUser(null);
    }, []);

    useEffect(() => {
        const verifySession = async () => {
            const token = localStorage.getItem('token');
            const email = localStorage.getItem('email');

            if (token && email) {
                try {
                    console.log("Verifying session for:", email);
                    const result = await authService.getUserDetails(email);
                    
                    if (result.success) {
                        console.log("Session verified successfully");
                        setUser(result.data);
                    } else {
                        console.error("Session verification failed: Invalid Token");
                        logout();
                    }
                } catch (error) {
                    console.error("Network error during session verification:", error);
                    logout();
                }
            } else {
                console.log("No token found in localStorage");
            }
            setLoading(false);
        };

        verifySession();
    }, [logout]);

    return (
        <UserContext.Provider value={{ user, setUser, loading, logout }}>
            {children}
        </UserContext.Provider>
    );
};