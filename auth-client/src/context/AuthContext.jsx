import { createContext, useContext, useState, useEffect } from "react";
import { authAPI, tokenUtils, userAPI } from "../utils/api";

const AuthContext = createContext();

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

export const AuthProvider = ({ children }) => {
  // 인증 상태 관리
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const checkAuthStatus = async () => {
      try {
        if (tokenUtils.isLoggedIn()) {
          // 토큰이 있으면 사용자 정보 조회 시도
          const userData = await userAPI.getCurrentUser();
          setUser(userData);
        }
      } catch (error) {
        console.error("인증 상태 확인 실패:", error.message);
        tokenUtils.clearTokens();
      } finally {
        setLoading(false);
      }
    };

    checkAuthStatus();
  }, []);

  const login = async (credentials) => {
    try {
      setLoading(true);
      setError(null);

      await authAPI.login(credentials);

      // 사용자 정보 조회
      const userData = await userAPI.getCurrentUser();
      setUser(userData);

      return true;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const register = async (userData) => {
    try {
      setLoading(true);
      setError(null);

      const success = await authAPI.register(userData);
      return success;
    } catch (error) {
      setError(error.message);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const logout = async () => {
    try {
      setLoading(true);
      setError(null);

      await authAPI.logout();
      setUser(null);
    } catch (error) {
      console.error("로그아웃 실패:", error.message);
      setUser(null);
    } finally {
      setLoading(false);
    }
  };

  /**
   * 에러 상태 초기화
   */
  const clearError = () => {
    setError(null);
  };

  /**
   * 로그인 상태 확인
   *
   * @returns {boolean} 로그인 여부
   */
  const isLoggedIn = () => {
    return !!user && tokenUtils.isLoggedIn();
  };

  const value = {
    // 상태
    user,
    loading,
    error,

    // 함수
    login,
    register,
    logout,
    clearError,
    isLoggedIn,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export default AuthContext;
