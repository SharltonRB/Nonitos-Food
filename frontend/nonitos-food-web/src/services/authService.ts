import axiosInstance from '@/config/axios';
import { ApiResponse, AuthResponse, LoginRequest, RegisterRequest } from '@/types/api';

export const authService = {
  login: async (credentials: LoginRequest): Promise<AuthResponse> => {
    const response = await axiosInstance.post<ApiResponse<AuthResponse>>(
      '/auth/login',
      credentials
    );
    return response.data.data;
  },

  register: async (data: RegisterRequest): Promise<AuthResponse> => {
    const response = await axiosInstance.post<ApiResponse<AuthResponse>>(
      '/auth/register',
      data
    );
    return response.data.data;
  },

  refresh: async (refreshToken: string): Promise<string> => {
    const response = await axiosInstance.post<ApiResponse<{ accessToken: string }>>(
      '/auth/refresh',
      { refreshToken }
    );
    return response.data.data.accessToken;
  },

  logout: () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
  },
};
