import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { loginSchema } from '@/lib/schemas';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Form, FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form';
import { toast } from 'react-hot-toast';

type LoginFormValues = z.infer<typeof loginSchema>;

const LoginPage = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const form = useForm<LoginFormValues>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: '',
      password: '',
    },
  });

  const onSubmit = async (data: LoginFormValues) => {
    try {
      await login(data.email, data.password);
      toast.success('¡Bienvenido de vuelta!');
      navigate('/dashboard');
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Credenciales inválidas';
      toast.error(errorMessage);
      console.error('Login failed:', error);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 p-4">
      <Card className="w-full max-w-md rounded-3xl shadow-lg animate-in fade-in">
        <CardHeader className="text-center">
          <CardTitle className="text-3xl font-bold text-gray-900">¡Bienvenido de Nuevo!</CardTitle>
          <CardDescription className="text-gray-600">Ingresa a tu cuenta para continuar</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
              <FormField
                control={form.control}
                name="email"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-gray-900">Email</FormLabel>
                    <FormControl>
                      <Input placeholder="tu@email.com" {...field} className="rounded-xl" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <FormField
                control={form.control}
                name="password"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-gray-900">Contraseña</FormLabel>
                    <FormControl>
                      <Input type="password" placeholder="********" {...field} className="rounded-xl" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
              <Button
                type="submit"
                className="w-full bg-emerald-600 hover:bg-emerald-700 text-white font-bold text-base rounded-xl active:scale-95 transition-transform"
                disabled={form.formState.isSubmitting}
              >
                {form.formState.isSubmitting ? 'Ingresando...' : 'Ingresar'}
              </Button>
            </form>
          </Form>
          <div className="mt-6 text-center text-sm text-gray-600">
            ¿No tienes una cuenta?{' '}
            <Link to="/register" className="font-medium text-emerald-600 hover:text-emerald-500 transition-colors">
              Regístrate
            </Link>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default LoginPage;
