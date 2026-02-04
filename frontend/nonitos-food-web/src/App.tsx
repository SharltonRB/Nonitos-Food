import { QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { queryClient } from './config/queryClient';
import { Toaster } from 'sonner';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import MainLayout from './components/layout/MainLayout';
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import ProtectedRoute from './components/shared/ProtectedRoute';

// A component to handle the root redirection logic
const Root = () => {
  const { isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return <div>Loading...</div>; // Or a proper loading spinner
  }

  return <Navigate to={isAuthenticated ? "/dashboard" : "/login"} replace />;
};

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            <Route path="/" element={<Root />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <MainLayout>
                    <div className="text-center">
                      <h1 className="text-4xl font-bold">Welcome to your Dashboard</h1>
                      <p className="mt-4 text-lg text-gray-500">
                        Here you will see your stats.
                      </p>
                    </div>
                  </MainLayout>
                </ProtectedRoute>
              }
            />
            {/* Add other protected routes here inside MainLayout */}
          </Routes>
          <Toaster position="top-right" />
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
