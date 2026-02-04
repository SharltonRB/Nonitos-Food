# Nonito's Food Frontend - Guía Completa de Desarrollo

## 📋 Información del Proyecto

### Contexto General
Nonito's Food es un sistema web completo para gestión de prep meals (comidas preparadas). El **backend está 100% completado** con Spring Boot y expone una API REST documentada con Swagger.

**Backend API Base URL:** `http://localhost:8080/api`
**Swagger UI:** `http://localhost:8080/swagger-ui.html`

### Objetivo del Frontend
Desarrollar una aplicación web moderna con React que consuma la API del backend y proporcione interfaces para:
- **Clientes:** Ver menús, hacer pedidos, gestionar perfil, realizar pagos
- **Administradores:** Dashboard, gestión de usuarios, platillos, menús, pedidos y pagos

### Stack Tecnológico Definido
- **Framework:** React 18 con TypeScript
- **Build Tool:** Vite
- **Styling:** Tailwind CSS
- **UI Components:** shadcn/ui
- **State Management:** React Query (TanStack Query) + Context API
- **Routing:** React Router v6
- **HTTP Client:** Axios
- **Forms:** React Hook Form + Zod (validación)
- **Notificaciones:** React Hot Toast o Sonner

### Estructura de Carpetas Estándar
```
frontend/nonitos-food-web/
├── public/                 # Assets estáticos
├── src/
│   ├── components/        # Componentes reutilizables
│   │   ├── ui/           # shadcn/ui components
│   │   ├── layout/       # Layout components (Header, Sidebar, Footer)
│   │   └── shared/       # Componentes compartidos
│   ├── pages/            # Páginas/Vistas principales
│   │   ├── auth/         # Login, Register
│   │   ├── client/       # Vistas de cliente
│   │   └── admin/        # Vistas de administrador
│   ├── hooks/            # Custom React hooks
│   ├── services/         # Servicios de API (axios)
│   ├── contexts/         # React contexts (AuthContext, etc.)
│   ├── lib/              # Utilidades y helpers
│   ├── types/            # TypeScript types/interfaces
│   ├── config/           # Configuración (axios, constants)
│   ├── App.tsx           # Componente principal
│   ├── main.tsx          # Entry point
│   └── index.css         # Estilos globales
├── .env.example          # Variables de entorno ejemplo
├── .env.local            # Variables de entorno local (gitignored)
├── package.json
├── tsconfig.json
├── vite.config.ts
├── tailwind.config.js
└── components.json       # shadcn/ui config
```

### Variables de Entorno Requeridas
```env
VITE_API_URL=http://localhost:8080/api
VITE_APP_NAME=Nonito's Food
VITE_APP_VERSION=1.0.0
```

---

## 📊 Estado del Proyecto

- **Inicio:** 2026-02-03
- **Última actualización:** 2026-02-03
- **Progreso:** 0/10 tareas completadas (0%)
- **Backend:** ✅ 100% Completado
- **Frontend:** 🚧 En desarrollo

---

## 🎯 ROADMAP DE TAREAS

### ✅ Tareas Completadas
Ninguna aún.

### 🚧 Tareas Pendientes
1. Task 1: Configuración Inicial del Proyecto
2. Task 2: Sistema de Autenticación
3. Task 3: Layout y Navegación Principal
4. Task 4: Dashboard de Cliente
5. Task 5: Gestión de Perfil de Usuario
6. Task 6: Catálogo de Platillos
7. Task 7: Menús Semanales
8. Task 8: Sistema de Pedidos
9. Task 9: Proceso de Pago
10. Task 10: Panel de Administración

---


# TASK 1: Configuración Inicial del Proyecto

## 📝 Descripción
Crear y configurar el proyecto frontend desde cero con todas las herramientas y dependencias necesarias. Esta tarea establece la base técnica para todo el desarrollo posterior.

## 🎯 Objetivos
1. Crear proyecto React con Vite y TypeScript
2. Configurar Tailwind CSS
3. Instalar y configurar shadcn/ui
4. Configurar React Router v6
5. Configurar React Query (TanStack Query)
6. Configurar Axios para llamadas HTTP
7. Crear estructura de carpetas
8. Configurar variables de entorno
9. Configurar ESLint y Prettier (opcional pero recomendado)

## 📋 Pasos Detallados

### Paso 1: Crear Proyecto con Vite
```bash
# Navegar a la carpeta frontend
cd frontend

# Crear proyecto con Vite (template React + TypeScript)
npm create vite@latest nonitos-food-web -- --template react-ts

# Entrar al proyecto
cd nonitos-food-web

# Instalar dependencias base
npm install
```

### Paso 2: Instalar Dependencias Principales
```bash
# Routing
npm install react-router-dom

# State Management y Data Fetching
npm install @tanstack/react-query

# HTTP Client
npm install axios

# Forms y Validación
npm install react-hook-form zod @hookform/resolvers

# UI y Styling
npm install -D tailwindcss postcss autoprefixer
npx tailwindcss init -p

# Utilidades
npm install clsx tailwind-merge
npm install lucide-react  # Iconos

# Notificaciones (elegir uno)
npm install react-hot-toast
# O alternativamente: npm install sonner
```

### Paso 3: Configurar Tailwind CSS

**Archivo: `tailwind.config.js`**
```javascript
/** @type {import('tailwindcss').Config} */
export default {
  darkMode: ["class"],
  content: [
    './pages/**/*.{ts,tsx}',
    './components/**/*.{ts,tsx}',
    './app/**/*.{ts,tsx}',
    './src/**/*.{ts,tsx}',
  ],
  prefix: "",
  theme: {
    container: {
      center: true,
      padding: "2rem",
      screens: {
        "2xl": "1400px",
      },
    },
    extend: {
      colors: {
        border: "hsl(var(--border))",
        input: "hsl(var(--input))",
        ring: "hsl(var(--ring))",
        background: "hsl(var(--background))",
        foreground: "hsl(var(--foreground))",
        primary: {
          DEFAULT: "hsl(var(--primary))",
          foreground: "hsl(var(--primary-foreground))",
        },
        secondary: {
          DEFAULT: "hsl(var(--secondary))",
          foreground: "hsl(var(--secondary-foreground))",
        },
        destructive: {
          DEFAULT: "hsl(var(--destructive))",
          foreground: "hsl(var(--destructive-foreground))",
        },
        muted: {
          DEFAULT: "hsl(var(--muted))",
          foreground: "hsl(var(--muted-foreground))",
        },
        accent: {
          DEFAULT: "hsl(var(--accent))",
          foreground: "hsl(var(--accent-foreground))",
        },
        popover: {
          DEFAULT: "hsl(var(--popover))",
          foreground: "hsl(var(--popover-foreground))",
        },
        card: {
          DEFAULT: "hsl(var(--card))",
          foreground: "hsl(var(--card-foreground))",
        },
      },
      borderRadius: {
        lg: "var(--radius)",
        md: "calc(var(--radius) - 2px)",
        sm: "calc(var(--radius) - 4px)",
      },
      keyframes: {
        "accordion-down": {
          from: { height: "0" },
          to: { height: "var(--radix-accordion-content-height)" },
        },
        "accordion-up": {
          from: { height: "var(--radix-accordion-content-height)" },
          to: { height: "0" },
        },
      },
      animation: {
        "accordion-down": "accordion-down 0.2s ease-out",
        "accordion-up": "accordion-up 0.2s ease-out",
      },
    },
  },
  plugins: [require("tailwindcss-animate")],
}
```

**Archivo: `src/index.css`**
```css
@tailwind base;
@tailwind components;
@tailwind utilities;

@layer base {
  :root {
    --background: 0 0% 100%;
    --foreground: 222.2 84% 4.9%;
    --card: 0 0% 100%;
    --card-foreground: 222.2 84% 4.9%;
    --popover: 0 0% 100%;
    --popover-foreground: 222.2 84% 4.9%;
    --primary: 222.2 47.4% 11.2%;
    --primary-foreground: 210 40% 98%;
    --secondary: 210 40% 96.1%;
    --secondary-foreground: 222.2 47.4% 11.2%;
    --muted: 210 40% 96.1%;
    --muted-foreground: 215.4 16.3% 46.9%;
    --accent: 210 40% 96.1%;
    --accent-foreground: 222.2 47.4% 11.2%;
    --destructive: 0 84.2% 60.2%;
    --destructive-foreground: 210 40% 98%;
    --border: 214.3 31.8% 91.4%;
    --input: 214.3 31.8% 91.4%;
    --ring: 222.2 84% 4.9%;
    --radius: 0.5rem;
  }

  .dark {
    --background: 222.2 84% 4.9%;
    --foreground: 210 40% 98%;
    --card: 222.2 84% 4.9%;
    --card-foreground: 210 40% 98%;
    --popover: 222.2 84% 4.9%;
    --popover-foreground: 210 40% 98%;
    --primary: 210 40% 98%;
    --primary-foreground: 222.2 47.4% 11.2%;
    --secondary: 217.2 32.6% 17.5%;
    --secondary-foreground: 210 40% 98%;
    --muted: 217.2 32.6% 17.5%;
    --muted-foreground: 215 20.2% 65.1%;
    --accent: 217.2 32.6% 17.5%;
    --accent-foreground: 210 40% 98%;
    --destructive: 0 62.8% 30.6%;
    --destructive-foreground: 210 40% 98%;
    --border: 217.2 32.6% 17.5%;
    --input: 217.2 32.6% 17.5%;
    --ring: 212.7 26.8% 83.9%;
  }
}

@layer base {
  * {
    @apply border-border;
  }
  body {
    @apply bg-background text-foreground;
  }
}
```

### Paso 4: Instalar shadcn/ui

```bash
# Instalar CLI de shadcn/ui
npx shadcn-ui@latest init

# Cuando pregunte, seleccionar:
# - Style: Default
# - Base color: Slate
# - CSS variables: Yes

# Instalar componentes básicos que usaremos
npx shadcn-ui@latest add button
npx shadcn-ui@latest add input
npx shadcn-ui@latest add label
npx shadcn-ui@latest add card
npx shadcn-ui@latest add dialog
npx shadcn-ui@latest add dropdown-menu
npx shadcn-ui@latest add select
npx shadcn-ui@latest add table
npx shadcn-ui@latest add tabs
npx shadcn-ui@latest add toast
npx shadcn-ui@latest add form
npx shadcn-ui@latest add avatar
npx shadcn-ui@latest add badge
npx shadcn-ui@latest add separator
```

### Paso 5: Crear Estructura de Carpetas

```bash
# Desde la raíz del proyecto (nonitos-food-web)
mkdir -p src/components/ui
mkdir -p src/components/layout
mkdir -p src/components/shared
mkdir -p src/pages/auth
mkdir -p src/pages/client
mkdir -p src/pages/admin
mkdir -p src/hooks
mkdir -p src/services
mkdir -p src/contexts
mkdir -p src/lib
mkdir -p src/types
mkdir -p src/config
```

### Paso 6: Configurar Variables de Entorno

**Archivo: `.env.example`**
```env
VITE_API_URL=http://localhost:8080/api
VITE_APP_NAME=Nonito's Food
VITE_APP_VERSION=1.0.0
```

**Archivo: `.env.local`** (crear y agregar a .gitignore)
```env
VITE_API_URL=http://localhost:8080/api
VITE_APP_NAME=Nonito's Food
VITE_APP_VERSION=1.0.0
```

**Actualizar `.gitignore`:**
```
# Agregar si no existe
.env.local
.env.*.local
```

### Paso 7: Configurar Axios

**Archivo: `src/config/axios.ts`**
```typescript
import axios from 'axios';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export const axiosInstance = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor para agregar token JWT
axiosInstance.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Interceptor para manejar errores de respuesta
axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Si el error es 401 y no hemos intentado refresh
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        const refreshToken = localStorage.getItem('refreshToken');
        const response = await axios.post(`${API_URL}/auth/refresh`, {
          refreshToken,
        });

        const { accessToken } = response.data.data;
        localStorage.setItem('accessToken', accessToken);

        originalRequest.headers.Authorization = `Bearer ${accessToken}`;
        return axiosInstance(originalRequest);
      } catch (refreshError) {
        // Si el refresh falla, limpiar tokens y redirigir a login
        localStorage.removeItem('accessToken');
        localStorage.removeItem('refreshToken');
        window.location.href = '/login';
        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
```

### Paso 8: Configurar React Query

**Archivo: `src/config/queryClient.ts`**
```typescript
import { QueryClient } from '@tanstack/react-query';

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000, // 5 minutos
    },
  },
});
```

### Paso 9: Crear Utilidades Básicas

**Archivo: `src/lib/utils.ts`**
```typescript
import { type ClassValue, clsx } from "clsx"
import { twMerge } from "tailwind-merge"

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatCurrency(amount: number): string {
  return new Intl.NumberFormat('es-CR', {
    style: 'currency',
    currency: 'CRC',
  }).format(amount);
}

export function formatDate(date: string | Date): string {
  return new Intl.DateFormat('es-CR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  }).format(new Date(date));
}
```

### Paso 10: Configurar App Principal

**Archivo: `src/App.tsx`**
```typescript
import { QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter } from 'react-router-dom';
import { queryClient } from './config/queryClient';
import { Toaster } from 'react-hot-toast';

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <div className="min-h-screen bg-background">
          <h1 className="text-4xl font-bold text-center py-8">
            Nonito's Food
          </h1>
          <p className="text-center text-muted-foreground">
            Frontend en construcción...
          </p>
        </div>
        <Toaster position="top-right" />
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
```

**Archivo: `src/main.tsx`**
```typescript
import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.tsx'
import './index.css'

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>,
)
```

### Paso 11: Crear Types Básicos

**Archivo: `src/types/api.ts`**
```typescript
// Estructura de respuesta de la API
export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

// Tipos de usuario
export enum UserRole {
  CLIENT = 'CLIENT',
  ADMIN = 'ADMIN',
}

export interface User {
  id: number;
  email: string;
  fullName: string;
  phoneNumber?: string;
  role: UserRole;
  isEmailVerified: boolean;
  createdAt: string;
}

// Auth
export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  fullName: string;
  phoneNumber?: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  user: User;
}
```

### Paso 12: Verificar Instalación

```bash
# Ejecutar el proyecto
npm run dev

# Debería abrir en http://localhost:5173
# Verificar que se ve la página con "Nonito's Food" y estilos de Tailwind
```

## ✅ Criterios de Aceptación

- [ ] Proyecto creado con Vite + React + TypeScript
- [ ] Tailwind CSS configurado y funcionando
- [ ] shadcn/ui instalado con componentes básicos
- [ ] React Router configurado
- [ ] React Query configurado
- [ ] Axios configurado con interceptors
- [ ] Estructura de carpetas creada
- [ ] Variables de entorno configuradas
- [ ] Utilidades básicas creadas
- [ ] App.tsx renderiza correctamente
- [ ] `npm run dev` funciona sin errores

## 📦 Archivos Creados

1. `package.json` - Dependencias
2. `vite.config.ts` - Configuración de Vite
3. `tailwind.config.js` - Configuración de Tailwind
4. `tsconfig.json` - Configuración de TypeScript
5. `src/index.css` - Estilos globales
6. `src/App.tsx` - Componente principal
7. `src/main.tsx` - Entry point
8. `src/config/axios.ts` - Configuración de Axios
9. `src/config/queryClient.ts` - Configuración de React Query
10. `src/lib/utils.ts` - Utilidades
11. `src/types/api.ts` - Types de TypeScript
12. `.env.example` - Variables de entorno ejemplo
13. `.env.local` - Variables de entorno local

## 🔗 Referencias

- [Vite](https://vitejs.dev/)
- [React](https://react.dev/)
- [Tailwind CSS](https://tailwindcss.com/)
- [shadcn/ui](https://ui.shadcn.com/)
- [React Router](https://reactrouter.com/)
- [TanStack Query](https://tanstack.com/query/latest)
- [Axios](https://axios-http.com/)

## ⏭️ Siguiente Tarea

Una vez completada esta tarea, continuar con **Task 2: Sistema de Autenticación**.

---


# TASK 2: Sistema de Autenticación

## 📝 Descripción
Implementar el sistema completo de autenticación que permita a los usuarios registrarse, iniciar sesión y mantener su sesión activa. Este sistema debe integrarse con los endpoints de autenticación del backend.

## 🎯 Objetivos
1. Crear páginas de Login y Registro
2. Implementar AuthContext para gestión de estado de autenticación
3. Crear servicios de API para auth
4. Implementar sistema de rutas protegidas
5. Manejar tokens JWT (access y refresh)
6. Implementar logout
7. Persistir sesión del usuario

## 📋 Endpoints del Backend a Consumir

### POST /api/auth/register
**Request:**
```json
{
  "email": "user@example.com",
  "password": "Password123!",
  "fullName": "John Doe",
  "phoneNumber": "+50612345678"
}
```
**Response:**
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "accessToken": "eyJhbGc...",
    "refreshToken": "eyJhbGc...",
    "user": {
      "id": 1,
      "email": "user@example.com",
      "fullName": "John Doe",
      "role": "CLIENT",
      "isEmailVerified": false
    }
  }
}
```

### POST /api/auth/login
**Request:**
```json
{
  "email": "user@example.com",
  "password": "Password123!"
}
```
**Response:** Igual que register

### POST /api/auth/refresh
**Request:**
```json
{
  "refreshToken": "eyJhbGc..."
}
```
**Response:**
```json
{
  "success": true,
  "data": {
    "accessToken": "eyJhbGc..."
  }
}
```

## 🔧 Implementación Sugerida

### Paso 1: Crear Servicio de Autenticación

**Archivo: `src/services/authService.ts`**
```typescript
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
```

### Paso 2: Crear AuthContext

**Archivo: `src/contexts/AuthContext.tsx`**
```typescript
import React, { createContext, useContext, useState, useEffect } from 'react';
import { User } from '@/types/api';
import { authService } from '@/services/authService';

interface AuthContextType {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (email: string, password: string) => Promise<void>;
  register: (data: any) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Cargar usuario del localStorage al iniciar
    const storedUser = localStorage.getItem('user');
    const accessToken = localStorage.getItem('accessToken');
    
    if (storedUser && accessToken) {
      setUser(JSON.parse(storedUser));
    }
    setIsLoading(false);
  }, []);

  const login = async (email: string, password: string) => {
    const response = await authService.login({ email, password });
    
    localStorage.setItem('accessToken', response.accessToken);
    localStorage.setItem('refreshToken', response.refreshToken);
    localStorage.setItem('user', JSON.stringify(response.user));
    
    setUser(response.user);
  };

  const register = async (data: any) => {
    const response = await authService.register(data);
    
    localStorage.setItem('accessToken', response.accessToken);
    localStorage.setItem('refreshToken', response.refreshToken);
    localStorage.setItem('user', JSON.stringify(response.user));
    
    setUser(response.user);
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        isLoading,
        login,
        register,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};
```

### Paso 3: Crear Componente de Ruta Protegida

**Archivo: `src/components/shared/ProtectedRoute.tsx`**
```typescript
import { Navigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
  requiredRole?: 'CLIENT' | 'ADMIN';
}

export const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ 
  children, 
  requiredRole 
}) => {
  const { isAuthenticated, user, isLoading } = useAuth();

  if (isLoading) {
    return <div>Loading...</div>; // Puedes crear un componente de loading mejor
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (requiredRole && user?.role !== requiredRole) {
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
};
```

### Paso 4: Crear Páginas de Login y Registro

**Nota:** Para las páginas de Login y Registro, tienes libertad creativa en el diseño UI. Aquí te proporciono la estructura funcional y los requisitos, pero **tú decides**:
- Layout y distribución de elementos
- Colores y estilos específicos
- Animaciones y transiciones
- Imágenes o ilustraciones
- Mensajes de error y validación visual
- Responsive design approach

**Requisitos funcionales mínimos:**

**Página de Login (`src/pages/auth/LoginPage.tsx`):**
- Formulario con campos: email, password
- Validación con React Hook Form + Zod
- Botón de submit que llame a `login()` del AuthContext
- Link para ir a página de registro
- Manejo de errores (mostrar mensaje si credenciales inválidas)
- Redirección automática después de login exitoso

**Validaciones requeridas:**
- Email: formato válido
- Password: mínimo 8 caracteres

**Página de Registro (`src/pages/auth/RegisterPage.tsx`):**
- Formulario con campos: email, password, fullName, phoneNumber (opcional)
- Validación con React Hook Form + Zod
- Botón de submit que llame a `register()` del AuthContext
- Link para ir a página de login
- Manejo de errores (ej: email ya registrado)
- Redirección automática después de registro exitoso

**Validaciones requeridas:**
- Email: formato válido
- Password: mínimo 8 caracteres, debe contener mayúscula, minúscula y número
- FullName: requerido, máximo 100 caracteres
- PhoneNumber: opcional, máximo 20 caracteres

**Ejemplo de esquema de validación con Zod:**
```typescript
import { z } from 'zod';

export const loginSchema = z.object({
  email: z.string().email('Email inválido'),
  password: z.string().min(8, 'Mínimo 8 caracteres'),
});

export const registerSchema = z.object({
  email: z.string().email('Email inválido'),
  password: z
    .string()
    .min(8, 'Mínimo 8 caracteres')
    .regex(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)/, 
      'Debe contener mayúscula, minúscula y número'),
  fullName: z.string().min(1, 'Nombre requerido').max(100),
  phoneNumber: z.string().max(20).optional(),
});
```

### Paso 5: Configurar Rutas

**Archivo: `src/App.tsx`** (actualizar)
```typescript
import { QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { queryClient } from './config/queryClient';
import { AuthProvider } from './contexts/AuthContext';
import { Toaster } from 'react-hot-toast';
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import { ProtectedRoute } from './components/shared/ProtectedRoute';

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            {/* Rutas públicas */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            
            {/* Rutas protegidas - se implementarán en siguientes tasks */}
            <Route
              path="/dashboard"
              element={
                <ProtectedRoute>
                  <div>Dashboard (próximamente)</div>
                </ProtectedRoute>
              }
            />
            
            {/* Redirect por defecto */}
            <Route path="/" element={<Navigate to="/login" replace />} />
          </Routes>
          <Toaster position="top-right" />
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
```

## ✅ Criterios de Aceptación

- [ ] AuthContext implementado y funcionando
- [ ] Servicio de autenticación conectado al backend
- [ ] Página de login funcional con validaciones
- [ ] Página de registro funcional con validaciones
- [ ] Tokens JWT se guardan en localStorage
- [ ] Redirección automática después de login/registro
- [ ] ProtectedRoute funciona correctamente
- [ ] Logout limpia tokens y redirige a login
- [ ] Manejo de errores (credenciales inválidas, email duplicado, etc.)
- [ ] Sesión persiste al recargar página
- [ ] Refresh token funciona automáticamente (ya configurado en axios interceptor)

## 🎨 Libertad Creativa

Tienes total libertad para decidir:
- **Diseño visual:** Colores, tipografía, espaciado
- **Layout:** Formulario centrado, split screen, con imagen de fondo, etc.
- **Componentes UI:** Usa los componentes de shadcn/ui que prefieras
- **Animaciones:** Transiciones, loading states, etc.
- **Responsive:** Approach mobile-first o desktop-first
- **Mensajes:** Tono y estilo de mensajes de error/éxito
- **Extras:** Logo, ilustraciones, dark mode toggle, etc.

## 📦 Archivos a Crear

1. `src/services/authService.ts`
2. `src/contexts/AuthContext.tsx`
3. `src/components/shared/ProtectedRoute.tsx`
4. `src/pages/auth/LoginPage.tsx`
5. `src/pages/auth/RegisterPage.tsx`
6. Actualizar `src/App.tsx`

## 🧪 Testing Manual

1. Registrar un nuevo usuario
2. Verificar que se guarden tokens en localStorage
3. Verificar redirección a dashboard
4. Recargar página y verificar que sesión persiste
5. Hacer logout y verificar que tokens se limpien
6. Intentar login con credenciales inválidas
7. Intentar registrar con email duplicado
8. Intentar acceder a ruta protegida sin autenticación

## ⏭️ Siguiente Tarea

Una vez completada esta tarea, continuar con **Task 3: Layout y Navegación Principal**.

---


# TASK 3: Layout y Navegación Principal

## 📝 Descripción
Crear el layout principal de la aplicación con navegación, header, sidebar y estructura responsive. Este layout será la base para todas las páginas protegidas de la aplicación.

## 🎯 Objetivos
1. Crear componente de Layout principal
2. Implementar Header con información de usuario
3. Implementar Sidebar con navegación
4. Crear navegación responsive (mobile menu)
5. Implementar indicador de notificaciones
6. Crear Footer (opcional)
7. Diferenciar layout para Cliente vs Admin

## 📋 Estructura de Navegación Requerida

### Para Clientes (CLIENT):
- Dashboard / Inicio
- Menús Semanales
- Mis Pedidos
- Mi Perfil
- Notificaciones

### Para Administradores (ADMIN):
- Dashboard Admin
- Gestión de Usuarios
- Gestión de Platillos
- Gestión de Menús
- Gestión de Pedidos
- Verificación de Pagos

## 🔧 Implementación Sugerida

### Paso 1: Crear Componente de Layout Base

**Archivo: `src/components/layout/MainLayout.tsx`**

Este componente debe incluir:
- Header fijo en la parte superior
- Sidebar colapsable (desktop) / drawer (mobile)
- Área de contenido principal
- Manejo de estado para sidebar abierto/cerrado
- Responsive design

**Elementos requeridos en el Header:**
- Logo o nombre de la app
- Botón para toggle del sidebar (mobile)
- Indicador de notificaciones con contador
- Dropdown de usuario con:
  - Nombre y email del usuario
  - Link a perfil
  - Botón de logout

**Elementos requeridos en el Sidebar:**
- Lista de navegación según rol del usuario
- Iconos para cada item (usa lucide-react)
- Indicador de ruta activa
- Botón de logout al final (opcional)

### Paso 2: Crear Hook para Navegación

**Archivo: `src/hooks/useNavigation.ts`**
```typescript
import { useAuth } from '@/contexts/AuthContext';
import { 
  Home, 
  Calendar, 
  ShoppingBag, 
  User, 
  Bell,
  Users,
  UtensilsCrossed,
  CalendarDays,
  Package,
  CreditCard
} from 'lucide-react';

export interface NavItem {
  label: string;
  path: string;
  icon: any;
}

export const useNavigation = () => {
  const { user } = useAuth();

  const clientNavigation: NavItem[] = [
    { label: 'Inicio', path: '/dashboard', icon: Home },
    { label: 'Menús Semanales', path: '/menus', icon: Calendar },
    { label: 'Mis Pedidos', path: '/orders', icon: ShoppingBag },
    { label: 'Mi Perfil', path: '/profile', icon: User },
    { label: 'Notificaciones', path: '/notifications', icon: Bell },
  ];

  const adminNavigation: NavItem[] = [
    { label: 'Dashboard', path: '/admin/dashboard', icon: Home },
    { label: 'Usuarios', path: '/admin/users', icon: Users },
    { label: 'Platillos', path: '/admin/dishes', icon: UtensilsCrossed },
    { label: 'Menús', path: '/admin/menus', icon: CalendarDays },
    { label: 'Pedidos', path: '/admin/orders', icon: Package },
    { label: 'Pagos', path: '/admin/payments', icon: CreditCard },
  ];

  return user?.role === 'ADMIN' ? adminNavigation : clientNavigation;
};
```

### Paso 3: Crear Servicio de Notificaciones

**Archivo: `src/services/notificationService.ts`**
```typescript
import axiosInstance from '@/config/axios';
import { ApiResponse } from '@/types/api';

export interface Notification {
  id: number;
  type: string;
  title: string;
  message: string;
  isRead: boolean;
  createdAt: string;
}

export const notificationService = {
  getUnreadCount: async (): Promise<number> => {
    const response = await axiosInstance.get<ApiResponse<number>>(
      '/notifications/unread/count'
    );
    return response.data.data;
  },

  getUnread: async (): Promise<Notification[]> => {
    const response = await axiosInstance.get<ApiResponse<Notification[]>>(
      '/notifications/unread'
    );
    return response.data.data;
  },

  markAsRead: async (id: number): Promise<void> => {
    await axiosInstance.put(`/notifications/${id}/read`);
  },

  markAllAsRead: async (): Promise<void> => {
    await axiosInstance.put('/notifications/read-all');
  },
};
```

### Paso 4: Crear Hook para Notificaciones

**Archivo: `src/hooks/useNotifications.ts`**
```typescript
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { notificationService } from '@/services/notificationService';

export const useNotifications = () => {
  const queryClient = useQueryClient();

  const { data: unreadCount = 0 } = useQuery({
    queryKey: ['notifications', 'unread-count'],
    queryFn: notificationService.getUnreadCount,
    refetchInterval: 30000, // Refetch cada 30 segundos
  });

  const { data: unreadNotifications = [] } = useQuery({
    queryKey: ['notifications', 'unread'],
    queryFn: notificationService.getUnread,
  });

  const markAsReadMutation = useMutation({
    mutationFn: notificationService.markAsRead,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notifications'] });
    },
  });

  const markAllAsReadMutation = useMutation({
    mutationFn: notificationService.markAllAsRead,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notifications'] });
    },
  });

  return {
    unreadCount,
    unreadNotifications,
    markAsRead: markAsReadMutation.mutate,
    markAllAsRead: markAllAsReadMutation.mutate,
  };
};
```

### Paso 5: Actualizar App.tsx con Layout

**Archivo: `src/App.tsx`** (actualizar)
```typescript
import { QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { queryClient } from './config/queryClient';
import { AuthProvider } from './contexts/AuthContext';
import { Toaster } from 'react-hot-toast';
import LoginPage from './pages/auth/LoginPage';
import RegisterPage from './pages/auth/RegisterPage';
import { ProtectedRoute } from './components/shared/ProtectedRoute';
import MainLayout from './components/layout/MainLayout';

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Routes>
            {/* Rutas públicas */}
            <Route path="/login" element={<LoginPage />} />
            <Route path="/register" element={<RegisterPage />} />
            
            {/* Rutas protegidas con layout */}
            <Route
              path="/*"
              element={
                <ProtectedRoute>
                  <MainLayout />
                </ProtectedRoute>
              }
            >
              {/* Las rutas específicas se agregarán en siguientes tasks */}
              <Route path="dashboard" element={<div>Dashboard</div>} />
              <Route path="menus" element={<div>Menús</div>} />
              <Route path="orders" element={<div>Pedidos</div>} />
              <Route path="profile" element={<div>Perfil</div>} />
              <Route path="notifications" element={<div>Notificaciones</div>} />
            </Route>
            
            {/* Redirect por defecto */}
            <Route path="/" element={<Navigate to="/dashboard" replace />} />
          </Routes>
          <Toaster position="top-right" />
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  );
}

export default App;
```

## 🎨 Libertad Creativa

Tienes total libertad para decidir:

**Diseño del Layout:**
- Sidebar siempre visible vs colapsable
- Ancho del sidebar
- Posición de elementos en el header
- Colores y estilos del sidebar/header
- Animaciones de transición

**Header:**
- Diseño del dropdown de usuario
- Estilo del indicador de notificaciones
- Logo o texto
- Búsqueda global (opcional)

**Sidebar:**
- Iconos y su estilo
- Hover effects
- Indicador de ruta activa (color, borde, background)
- Agrupación de items (opcional)
- Mini sidebar colapsado (solo iconos)

**Responsive:**
- Breakpoint para cambiar a mobile
- Tipo de menú mobile (drawer, bottom nav, etc.)
- Animaciones de apertura/cierre

**Extras opcionales:**
- Dark mode toggle
- Breadcrumbs
- Footer con información
- Búsqueda global
- Shortcuts de teclado

## ✅ Criterios de Aceptación

- [ ] MainLayout renderiza correctamente
- [ ] Header muestra información del usuario
- [ ] Sidebar muestra navegación según rol
- [ ] Navegación funciona (cambio de rutas)
- [ ] Indicador de ruta activa funciona
- [ ] Contador de notificaciones se muestra
- [ ] Dropdown de usuario funciona
- [ ] Logout desde header funciona
- [ ] Layout es responsive (funciona en mobile)
- [ ] Sidebar se puede abrir/cerrar en mobile
- [ ] Contenido principal se renderiza correctamente

## 📦 Archivos a Crear

1. `src/components/layout/MainLayout.tsx`
2. `src/components/layout/Header.tsx` (opcional, puede estar en MainLayout)
3. `src/components/layout/Sidebar.tsx` (opcional, puede estar en MainLayout)
4. `src/hooks/useNavigation.ts`
5. `src/hooks/useNotifications.ts`
6. `src/services/notificationService.ts`
7. Actualizar `src/App.tsx`
8. Actualizar `src/types/api.ts` (agregar interface Notification)

## 🧪 Testing Manual

1. Login como cliente y verificar navegación de cliente
2. Login como admin y verificar navegación de admin
3. Verificar que contador de notificaciones aparece
4. Probar navegación entre páginas
5. Verificar indicador de ruta activa
6. Probar logout desde dropdown de usuario
7. Probar en mobile (sidebar debe ser drawer)
8. Verificar que contenido se renderiza correctamente

## 💡 Sugerencias de Componentes shadcn/ui

Componentes útiles que podrías usar:
- `Sheet` - Para sidebar mobile (drawer)
- `DropdownMenu` - Para menú de usuario
- `Badge` - Para contador de notificaciones
- `Separator` - Para separar secciones
- `ScrollArea` - Para sidebar con muchos items
- `Avatar` - Para foto de usuario

Instalar si es necesario:
```bash
npx shadcn-ui@latest add sheet
npx shadcn-ui@latest add scroll-area
npx shadcn-ui@latest add avatar
```

## ⏭️ Siguiente Tarea

Una vez completada esta tarea, continuar con **Task 4: Dashboard de Cliente**.

---


# TASK 4: Dashboard de Cliente

## 📝 Descripción
Crear el dashboard principal para clientes que muestre un resumen de información relevante: menú semanal actual, pedidos activos, notificaciones recientes y accesos rápidos.

## 🎯 Objetivos
1. Vista de menú semanal actual (resumen)
2. Lista de pedidos activos/recientes
3. Notificaciones recientes
4. Cards con accesos rápidos
5. Estadísticas personales (opcional)

## 📋 Endpoints del Backend

- `GET /api/menus/published` - Menús publicados
- `GET /api/orders/my-orders` - Mis pedidos
- `GET /api/notifications/unread` - Notificaciones no leídas

## 🔧 Componentes Sugeridos

**Estructura del Dashboard:**
- Grid responsive con cards
- Card de "Menú de esta semana" con preview
- Card de "Mis pedidos activos" con lista
- Card de "Notificaciones recientes"
- Botones de acción rápida (Hacer pedido, Ver menú completo, etc.)

**Servicios a crear:**
```typescript
// src/services/menuService.ts
export const menuService = {
  getPublishedMenus: async () => { /* ... */ },
  getMenuById: async (id: number) => { /* ... */ },
};

// src/services/orderService.ts
export const orderService = {
  getMyOrders: async () => { /* ... */ },
  getOrderById: async (id: number) => { /* ... */ },
  createOrder: async (data: any) => { /* ... */ },
  cancelOrder: async (id: number, reason: string) => { /* ... */ },
};
```

## 🎨 Libertad Creativa
- Layout del dashboard (grid, flex, etc.)
- Diseño de cards
- Colores y estilos
- Gráficos o estadísticas (opcional)
- Animaciones y transiciones
- Empty states cuando no hay datos

## ✅ Criterios de Aceptación
- [ ] Dashboard muestra menú semanal actual
- [ ] Dashboard muestra pedidos activos
- [ ] Dashboard muestra notificaciones recientes
- [ ] Links de navegación funcionan
- [ ] Loading states mientras carga datos
- [ ] Empty states cuando no hay datos
- [ ] Responsive design

---

# TASK 5: Gestión de Perfil de Usuario

## 📝 Descripción
Implementar la gestión completa del perfil del usuario: información personal, alergias, restricciones dietéticas, preferencias nutricionales y cambio de contraseña.

## 🎯 Objetivos
1. Ver y editar información personal
2. Gestionar alergias (agregar/eliminar)
3. Gestionar restricciones dietéticas
4. Configurar preferencias nutricionales
5. Cambiar contraseña

## 📋 Endpoints del Backend

- `GET /api/profile` - Obtener perfil
- `PUT /api/profile` - Actualizar perfil
- `POST /api/profile/allergies` - Agregar alergia
- `DELETE /api/profile/allergies/{id}` - Eliminar alergia
- `POST /api/profile/restrictions` - Agregar restricción
- `DELETE /api/profile/restrictions/{id}` - Eliminar restricción
- `PUT /api/profile/preferences` - Actualizar preferencias
- `GET /api/catalogs/allergies` - Catálogo de alergias
- `GET /api/catalogs/restrictions` - Catálogo de restricciones

## 🔧 Estructura Sugerida

**Página de Perfil con Tabs:**
- Tab 1: Información Personal (nombre, email, teléfono, contacto emergencia)
- Tab 2: Alergias (lista actual + agregar nuevas con severidad)
- Tab 3: Restricciones Dietéticas (lista actual + agregar nuevas)
- Tab 4: Preferencias (objetivos nutricionales, comidas favoritas)
- Tab 5: Seguridad (cambiar contraseña)

**Tipos de datos:**
```typescript
export interface ClientProfile {
  id: number;
  userId: number;
  emergencyContactName?: string;
  emergencyContactPhone?: string;
  allergies: ClientAllergy[];
  restrictions: ClientRestriction[];
  preferences?: ClientPreferences;
}

export interface ClientAllergy {
  id: number;
  allergyId: number;
  allergyName: string;
  severity: 'LOW' | 'MEDIUM' | 'HIGH';
}

export interface ClientPreferences {
  nutritionalGoal?: string;
  favoriteMeals?: string;
  additionalNotes?: string;
}
```

## 🎨 Libertad Creativa
- Diseño de tabs o secciones
- Formularios inline vs modales
- Visualización de alergias/restricciones (cards, lista, badges)
- Indicadores de severidad de alergias
- Confirmaciones antes de eliminar
- Animaciones

## ✅ Criterios de Aceptación
- [ ] Ver información del perfil
- [ ] Editar información personal
- [ ] Agregar/eliminar alergias con severidad
- [ ] Agregar/eliminar restricciones dietéticas
- [ ] Actualizar preferencias nutricionales
- [ ] Validaciones en formularios
- [ ] Mensajes de éxito/error
- [ ] Cambios se reflejan inmediatamente

---

# TASK 6: Catálogo de Platillos

## 📝 Descripción
Crear la vista del catálogo de platillos con filtros, búsqueda, paginación y vista detalle de cada platillo.

## 🎯 Objetivos
1. Lista de platillos con paginación
2. Filtros (categoría, tags, alérgenos)
3. Búsqueda por nombre
4. Vista detalle de platillo
5. Mostrar información nutricional
6. Mostrar imágenes del platillo

## 📋 Endpoints del Backend

- `GET /api/dishes?page=0&size=20&category=LUNCH&isActive=true` - Lista con filtros
- `GET /api/dishes/{id}` - Detalle de platillo
- `GET /api/catalogs/tags` - Tags disponibles

## 🔧 Estructura Sugerida

**Página de Catálogo:**
- Barra de búsqueda
- Filtros (sidebar o top bar)
- Grid de cards de platillos
- Paginación
- Botón para ver detalle

**Modal/Página de Detalle:**
- Galería de imágenes
- Nombre y descripción
- Categoría y tags
- Información nutricional (calorías, proteínas, carbohidratos, grasas)
- Lista de alérgenos
- Precio (si aplica)

**Filtros disponibles:**
- Categoría: BREAKFAST, LUNCH, DINNER
- Tags: High Protein, Low Carb, Vegan, etc.
- Búsqueda por nombre
- Rango de calorías (opcional)

## 🎨 Libertad Creativa
- Layout de grid (2, 3, 4 columnas)
- Diseño de cards de platillos
- Posición de filtros
- Diseño del modal/página de detalle
- Galería de imágenes (carousel, grid)
- Badges para tags y categorías
- Loading skeletons

## ✅ Criterios de Aceptación
- [ ] Lista de platillos se muestra correctamente
- [ ] Filtros funcionan
- [ ] Búsqueda funciona
- [ ] Paginación funciona
- [ ] Vista detalle muestra toda la información
- [ ] Imágenes se cargan correctamente
- [ ] Responsive design
- [ ] Loading states

---

# TASK 7: Menús Semanales

## 📝 Descripción
Implementar la vista de menús semanales publicados con calendario, detalle de platillos por día y resumen nutricional.

## 🎯 Objetivos
1. Lista de menús publicados
2. Vista de calendario semanal
3. Detalle de platillos por día y comida
4. Resumen nutricional del menú
5. Botón para crear pedido desde menú

## 📋 Endpoints del Backend

- `GET /api/menus/published` - Menús publicados
- `GET /api/menus/{id}` - Detalle de menú

**Estructura de respuesta:**
```typescript
export interface WeeklyMenu {
  id: number;
  weekStartDate: string; // "2024-01-15"
  weekEndDate: string;
  status: 'PUBLISHED';
  menuDays: MenuDay[];
  nutritionalSummary: {
    avgCalories: number;
    avgProtein: number;
    avgCarbs: number;
    avgFat: number;
  };
}

export interface MenuDay {
  dayOfWeek: 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY' | 'SATURDAY' | 'SUNDAY';
  date: string;
  breakfast?: Dish;
  lunch?: Dish;
  dinner?: Dish;
}
```

## 🔧 Estructura Sugerida

**Página de Menús:**
- Selector de semana (si hay múltiples menús)
- Calendario/Grid semanal (7 días)
- Cada día muestra: Desayuno, Almuerzo, Cena
- Click en platillo abre detalle
- Card con resumen nutricional
- Botón "Hacer Pedido" prominente

**Vista de Calendario:**
- Puede ser tabla, grid, o cards por día
- Mostrar nombre e imagen pequeña de cada platillo
- Indicadores visuales (iconos de comida)

## 🎨 Libertad Creativa
- Diseño del calendario (tabla, grid, timeline)
- Visualización de platillos (cards, lista, imágenes)
- Colores por tipo de comida
- Resumen nutricional (gráficos, números, barras)
- Navegación entre semanas
- Vista mobile (scroll horizontal, accordion, etc.)

## ✅ Criterios de Aceptación
- [ ] Menús publicados se muestran
- [ ] Calendario semanal funciona
- [ ] Se muestran platillos por día y comida
- [ ] Click en platillo muestra detalle
- [ ] Resumen nutricional se muestra
- [ ] Botón de hacer pedido navega correctamente
- [ ] Responsive design
- [ ] Manejo de días sin platillos

---

# TASK 8: Sistema de Pedidos

## 📝 Descripción
Implementar el flujo completo de pedidos: crear pedido desde menú, ver historial, detalle con QR, y cancelación.

## 🎯 Objetivos
1. Crear pedido desde menú semanal
2. Seleccionar comidas (desayuno, almuerzo, cena)
3. Ver resumen antes de confirmar
4. Historial de pedidos
5. Detalle de pedido con QR
6. Cancelar pedido (con política de 24h)

## 📋 Endpoints del Backend

- `POST /api/orders` - Crear pedido
- `GET /api/orders/my-orders` - Mis pedidos
- `GET /api/orders/{id}` - Detalle de pedido
- `POST /api/orders/{id}/cancel` - Cancelar pedido

**Request para crear pedido:**
```typescript
{
  "weeklyMenuId": 1,
  "mealsPerDay": 3,
  "includeBreakfast": true,
  "includeLunch": true,
  "includeDinner": true,
  "pickupDateTime": "2024-01-22T10:00:00",
  "specialInstructions": "Sin cebolla"
}
```

**Response incluye:**
- orderCode (código único)
- qrCode (string del QR)
- status
- totalAmount
- pickupDateTime

## 🔧 Flujo Sugerido

**Paso 1: Selección de Comidas**
- Checkboxes para seleccionar: Desayuno, Almuerzo, Cena
- Mostrar precio por comida
- Calcular total en tiempo real

**Paso 2: Configuración**
- Fecha y hora de pickup (DatePicker)
- Instrucciones especiales (textarea)

**Paso 3: Resumen**
- Menú seleccionado
- Comidas incluidas
- Total a pagar
- Botón "Confirmar Pedido"

**Página de Mis Pedidos:**
- Lista de pedidos con estado
- Filtros por estado (todos, activos, completados, cancelados)
- Click en pedido abre detalle

**Detalle de Pedido:**
- Código de pedido
- QR code (mostrar imagen o generar con librería)
- Estado actual
- Información del menú
- Fecha de pickup
- Total pagado
- Botón de cancelar (si aplica)

## 🎨 Libertad Creativa
- Diseño del wizard/stepper
- Visualización del QR (tamaño, posición)
- Cards de pedidos en historial
- Estados visuales (colores, iconos)
- Confirmación de cancelación
- Animaciones de transición

## ✅ Criterios de Aceptación
- [ ] Crear pedido funciona
- [ ] Selección de comidas actualiza total
- [ ] Validaciones (fecha futura, al menos una comida)
- [ ] Pedido se crea y redirige a detalle
- [ ] Historial muestra todos los pedidos
- [ ] Detalle muestra QR code
- [ ] Cancelar pedido funciona (con validación de 24h)
- [ ] Estados de pedido se muestran correctamente
- [ ] Responsive design

---

# TASK 9: Proceso de Pago

## 📝 Descripción
Implementar el flujo de pago con selección de método (tarjeta mock, transferencia, SINPE Móvil) y confirmación.

## 🎯 Objetivos
1. Seleccionar método de pago
2. Formulario de pago con tarjeta (mock)
3. Formulario de pago manual (transferencia/SINPE)
4. Confirmación de pago
5. Ver comprobante

## 📋 Endpoints del Backend

- `POST /api/payments/credit-card` - Pago con tarjeta
- `POST /api/payments/manual` - Pago manual

**Request pago con tarjeta:**
```typescript
{
  "orderId": 1,
  "cardNumber": "4111111111111111",
  "cardHolderName": "John Doe",
  "expiryMonth": "12",
  "expiryYear": "2025",
  "cvv": "123"
}
```

**Request pago manual:**
```typescript
{
  "orderId": 1,
  "paymentMethod": "BANK_TRANSFER" | "SINPE_MOVIL",
  "referenceNumber": "REF123456",
  "proofImageUrl": "https://..."
}
```

## 🔧 Flujo Sugerido

**Paso 1: Selección de Método**
- Radio buttons o cards para elegir:
  - Tarjeta de crédito/débito
  - Transferencia bancaria
  - SINPE Móvil

**Paso 2: Formulario según método**

**Si es Tarjeta:**
- Número de tarjeta (con formato)
- Nombre del titular
- Fecha de expiración
- CVV
- Nota: "Este es un pago simulado"

**Si es Manual:**
- Mostrar datos bancarios para transferir
- Campo para número de referencia
- Upload de comprobante (opcional o requerido)
- Nota: "El pago será verificado por un administrador"

**Paso 3: Confirmación**
- Resumen del pago
- Método usado
- Monto
- Estado (PENDING o COMPLETED)
- Botón para volver a pedidos

## 🎨 Libertad Creativa
- Diseño de selección de método
- Formulario de tarjeta (puede usar librerías como react-credit-cards)
- Visualización de datos bancarios
- Upload de comprobante (drag & drop, button)
- Página de confirmación (success animation, confetti, etc.)
- Validaciones visuales

## ✅ Criterios de Aceptación
- [ ] Selección de método funciona
- [ ] Formulario de tarjeta valida datos
- [ ] Pago con tarjeta se procesa (mock)
- [ ] Formulario manual permite ingresar datos
- [ ] Upload de comprobante funciona (si aplica)
- [ ] Pago manual se registra como PENDING
- [ ] Confirmación muestra información correcta
- [ ] Redirección después de pago
- [ ] Manejo de errores

---

# TASK 10: Panel de Administración

## 📝 Descripción
Implementar el panel completo de administración con dashboard, gestión de usuarios, platillos, menús, pedidos y verificación de pagos.

## 🎯 Objetivos
1. Dashboard admin con métricas
2. Gestión de usuarios (CRUD)
3. Gestión de platillos (CRUD)
4. Gestión de menús semanales (CRUD)
5. Gestión de pedidos (ver, actualizar estado)
6. Verificación de pagos manuales

## 📋 Endpoints del Backend

**Dashboard:**
- `GET /api/admin/dashboard/metrics`

**Usuarios:**
- `GET /api/admin/users?page=0&size=20`
- `GET /api/admin/users/{id}`
- `PUT /api/admin/users/{id}`
- `DELETE /api/admin/users/{id}`

**Platillos:**
- `POST /api/dishes` - Crear
- `PUT /api/dishes/{id}` - Actualizar
- `DELETE /api/dishes/{id}` - Eliminar

**Menús:**
- `POST /api/menus` - Crear
- `PUT /api/menus/{id}` - Actualizar
- `POST /api/menus/{id}/publish` - Publicar
- `DELETE /api/menus/{id}` - Eliminar

**Pedidos:**
- `GET /api/orders` - Todos los pedidos
- `PUT /api/orders/{id}/status` - Actualizar estado

**Pagos:**
- `GET /api/payments/order/{orderId}` - Transacciones
- `POST /api/payments/{id}/verify` - Verificar pago manual

## 🔧 Estructura Sugerida

**Dashboard Admin:**
- Cards con métricas:
  - Total usuarios
  - Total pedidos (pendientes, completados)
  - Ingresos (total, pendiente)
  - Total platillos activos
  - Menús publicados
- Gráficos (opcional)
- Lista de pedidos recientes
- Lista de pagos pendientes de verificación

**Gestión de Usuarios:**
- Tabla con paginación
- Filtros (rol, estado)
- Búsqueda por email/nombre
- Acciones: Ver, Editar, Eliminar
- Modal/página para editar

**Gestión de Platillos:**
- Tabla/Grid con imágenes
- Filtros (categoría, activo)
- Acciones: Ver, Editar, Eliminar, Crear
- Formulario completo:
  - Nombre, descripción, categoría
  - Precio
  - Info nutricional
  - Tags
  - Alérgenos
  - Imágenes (upload múltiple)

**Gestión de Menús:**
- Lista de menús (draft, published, archived)
- Crear nuevo menú:
  - Seleccionar fecha de inicio (lunes)
  - Asignar platillos por día y comida
  - Ver resumen nutricional
  - Publicar
- Editar menú existente
- Archivar menú

**Gestión de Pedidos:**
- Tabla con todos los pedidos
- Filtros por estado
- Ver detalle
- Actualizar estado (dropdown)
- Ver información del cliente

**Verificación de Pagos:**
- Lista de pagos pendientes
- Ver comprobante
- Botones: Aprobar / Rechazar
- Agregar nota (opcional)

## 🎨 Libertad Creativa
- Layout del dashboard (grid, flex)
- Diseño de tablas (shadcn/ui table, custom)
- Formularios (modales vs páginas completas)
- Visualización de métricas (cards, gráficos)
- Confirmaciones de acciones destructivas
- Drag & drop para asignar platillos a menús (opcional)
- Filtros avanzados
- Exportar datos (opcional)

## ✅ Criterios de Aceptación
- [ ] Dashboard muestra métricas correctas
- [ ] CRUD de usuarios funciona
- [ ] CRUD de platillos funciona (con upload de imágenes)
- [ ] CRUD de menús funciona
- [ ] Asignación de platillos a menús funciona
- [ ] Publicar menú funciona
- [ ] Ver y actualizar estado de pedidos funciona
- [ ] Verificar pagos manuales funciona
- [ ] Todas las tablas tienen paginación
- [ ] Filtros y búsquedas funcionan
- [ ] Validaciones en formularios
- [ ] Confirmaciones antes de eliminar
- [ ] Responsive design (al menos funcional)

## 💡 Consideraciones Especiales

**Upload de Imágenes:**
- Puedes usar un servicio como Cloudinary, ImgBB, o similar
- O simular con URLs de placeholder
- Validar tipo y tamaño de archivo

**Asignación de Platillos a Menús:**
- Puede ser con selects por día y comida
- O drag & drop desde lista de platillos
- Mostrar preview del menú

**Validaciones Importantes:**
- Menú solo puede iniciar en lunes
- Solo un menú publicado por semana
- No eliminar platillos que están en menús activos
- Validar fechas de pickup en pedidos

---

# 🎉 PROYECTO COMPLETADO

Una vez completadas las 10 tareas, el frontend estará 100% funcional con:

✅ Autenticación completa
✅ Layout y navegación
✅ Dashboard de cliente
✅ Gestión de perfil
✅ Catálogo de platillos
✅ Menús semanales
✅ Sistema de pedidos
✅ Proceso de pago
✅ Panel de administración completo

## 📚 Recursos Finales

- [Documentación del Backend API](../backend/nonitos-food-api/API_DOCUMENTATION.md)
- [Swagger UI](http://localhost:8080/swagger-ui.html)
- [React Query Docs](https://tanstack.com/query/latest)
- [shadcn/ui Components](https://ui.shadcn.com/)
- [Tailwind CSS](https://tailwindcss.com/)

## 🚀 Deployment

Para deployment, considera:
- Vercel o Netlify para el frontend
- Variables de entorno de producción
- Build optimizado (`npm run build`)
- Configurar CORS en el backend para el dominio de producción

---

**¡Éxito con el desarrollo del frontend!** 🎨✨

