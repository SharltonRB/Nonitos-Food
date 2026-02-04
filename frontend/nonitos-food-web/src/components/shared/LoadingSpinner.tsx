import { Loader2 } from 'lucide-react';

export const LoadingSpinner = () => {
  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="text-center">
        <Loader2 className="h-12 w-12 animate-spin text-emerald-600 mx-auto" />
        <p className="mt-4 text-gray-600 font-medium">Cargando...</p>
      </div>
    </div>
  );
};

export default LoadingSpinner;
