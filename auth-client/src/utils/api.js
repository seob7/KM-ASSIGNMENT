import axios from "axios";

/**
 * 서버 응답 구조
 *   "metaData": {
 *     "success": true | false,
 *     "code": <HTTP status code>,
 *     "message": "<optional message>"
 *   },
 *   "data": <payload or null>
 * }
 */

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

// Axios 인스턴스 생성
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  timeout: 10000,
  withCredentials: true,
  headers: {
    "Content-Type": "application/json",
  },
});

/**
 * 요청 인터셉터 - 토큰 자동 추가
 */
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("accessToken");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

/**
 * 응답 인터셉터 - 토큰 만료 처리
 */
apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error) => {
    const originalRequest = error.config; // 기존 요청 데이터

    // 401 에러이고 아직 재시도하지 않은 경우
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      // 토큰 재발급이 가능한 경우만 처리
      const isTokenExpired = isTokenExpiredError(error);
      const hasRefreshToken = localStorage.getItem("refreshToken");

      if (isTokenExpired && hasRefreshToken) {
        console.log("reissue");

        try {
          const refreshToken = localStorage.getItem("refreshToken");
          const response = await apiClient.post(
            "/v1/open-api/users/reissue",
            null,
            {
              headers: {
                Authorization: `Bearer ${refreshToken}`,
              },
            }
          );

          const { accessToken, refreshToken: newRefreshToken } =
            response.data.data;
          localStorage.setItem("accessToken", accessToken);
          localStorage.setItem("refreshToken", newRefreshToken);

          // 원래 요청 재시도
          originalRequest.headers.Authorization = `Bearer ${accessToken}`;
          return apiClient(originalRequest);
        } catch (refreshError) {
          // 리프레시 토큰도 만료된 경우 로그아웃 처리
          localStorage.removeItem("accessToken");
          localStorage.removeItem("refreshToken");
          window.location.href = "/login";
          return Promise.reject(refreshError);
        }
      } else {
        // 토큰 재발급이 불가능한 경우 로그아웃 처리
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        window.location.href = "/login";
      }
    }

    return Promise.reject(error);
  }
);

const isTokenExpiredError = (error) => {
  const errorMessage = error.response?.data?.metaData?.message || "";

  const tokenExpiredMessages = [
    "만료된 토큰입니다.",
    "토큰이 만료되었습니다.",
    "Token expired",
    "Expired token",
    "JWT expired",
  ];

  return tokenExpiredMessages.some((msg) =>
    errorMessage.toLowerCase().includes(msg.toLowerCase())
  );
};

/**
 * API 응답 데이터 추출 함수
 */
const extractData = (response) => {
  return response.data.data;
};

/**
 * API 에러 메시지 추출 함수
 */
const extractErrorMessage = (error) => {
  if (error.response?.data?.metaData?.message) {
    return error.response.data.metaData.message;
  }
  return error.message || "알 수 없는 오류가 발생했습니다.";
};

export const authAPI = {
  /**
   * 회원가입
   * @param {Object} userData - 회원가입 데이터
   * @param {string} userData.email - 이메일
   * @param {string} userData.password - 비밀번호
   * @param {string} userData.nickname - 닉네임
   * @returns {Promise<boolean>} 회원가입 성공 여부
   */
  register: async (userData) => {
    try {
      const response = await apiClient.post("/v1/open-api/users", userData);
      return extractData(response);
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  },

  /**
   * 로그인
   * @param {Object} credentials - 로그인 정보
   * @param {string} credentials.email - 이메일
   * @param {string} credentials.password - 비밀번호
   * @returns {Promise<Object>} 토큰 정보
   * @returns {string} returns.accessToken - 액세스 토큰
   * @returns {string} returns.refreshToken - 리프레시 토큰
   */
  login: async (credentials) => {
    try {
      const response = await apiClient.post(
        "/v1/open-api/users/login",
        credentials
      );
      const tokenData = extractData(response);

      // 토큰을 localStorage에 저장
      localStorage.setItem("accessToken", tokenData.accessToken);
      localStorage.setItem("refreshToken", tokenData.refreshToken);

      return tokenData;
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  },

  /**
   * 로그아웃
   * @returns {Promise<boolean>} 로그아웃 성공 여부
   */
  logout: async () => {
    try {
      const response = await apiClient.post("/v1/open-api/users/logout");

      // 로컬 스토리지에서 토큰 제거
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");

      return extractData(response);
    } catch (error) {
      // 에러가 발생해도 로컬 스토리지는 정리
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
      throw new Error(extractErrorMessage(error));
    }
  },

  /**
   * 토큰 재발급
   * @returns {Promise<Object>} 새로운 토큰 정보
   */
  reissueToken: async () => {
    try {
      const refreshToken = localStorage.getItem("refreshToken");
      if (!refreshToken) {
        throw new Error("리프레시 토큰이 없습니다.");
      }

      const response = await apiClient.post(
        "/v1/open-api/users/reissue",
        null,
        {
          headers: {
            Authorization: `Bearer ${refreshToken}`,
          },
        }
      );

      const tokenData = extractData(response);

      // 새로운 토큰을 localStorage에 저장
      localStorage.setItem("accessToken", tokenData.accessToken);
      localStorage.setItem("refreshToken", tokenData.refreshToken);

      return tokenData;
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  },
};

/**
 * 사용자 정보 관련 API
 */
export const userAPI = {
  /**
   * 현재 사용자 정보 조회
   * @returns {Promise<String>} 닉네임 조회
   */
  getCurrentUser: async () => {
    try {
      const response = await apiClient.get("/v1/api/users");
      return extractData(response);
    } catch (error) {
      throw new Error(extractErrorMessage(error));
    }
  },
};

/**
 * 토큰 관리 유틸리티
 */
export const tokenUtils = {
  /**
   * 현재 저장된 액세스 토큰 반환
   * @returns {string|null} 액세스 토큰
   */
  getAccessToken: () => {
    return localStorage.getItem("accessToken");
  },

  /**
   * 현재 저장된 리프레시 토큰 반환
   * @returns {string|null} 리프레시 토큰
   */
  getRefreshToken: () => {
    return localStorage.getItem("refreshToken");
  },

  /**
   * 로그인 상태 확인
   * @returns {boolean} 로그인 여부
   */
  isLoggedIn: () => {
    return !!localStorage.getItem("accessToken");
  },

  /**
   * 모든 토큰 제거
   */
  clearTokens: () => {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
  },
};
