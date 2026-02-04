import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '@/contexts/AuthContext';
import { registerSchema } from '@/lib/schemas';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Input } from '@/components/ui/input';
import { Form, FormField, FormItem, FormLabel, FormControl, FormMessage } from '@/components/ui/form';
import { toast } from 'react-hot-toast';

type RegisterFormValues = z.infer<typeof registerSchema>;

const RegisterPage = () => {
  const navigate = useNavigate();
  const { register } = useAuth();
  const form = useForm<RegisterFormValues>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      email: '',
      password: '',
      fullName: '',
      phoneNumber: '',
    },
  });

  const onSubmit = async (data: RegisterFormValues) => {
    try {
      await register(data);
      toast.success('¡Cuenta creada exitosamente!');
      navigate('/dashboard');
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'Error al crear la cuenta';
      toast.error(errorMessage);
      console.error('Registration failed:', error);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50 p-4">
      <Card className="w-full max-w-md rounded-3xl shadow-lg animate-in fade-in">
        <CardHeader className="text-center">
          <CardTitle className="text-3xl font-bold text-gray-900">Crea tu Cuenta</CardTitle>
          <CardDescription className="text-gray-600">Empieza tu viaje hacia una vida más saludable</CardDescription>
        </CardHeader>
        <CardContent>
          <Form {...form}>
            <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
              <FormField
                control={form.control}
                name="fullName"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-gray-900">Nombre Completo</FormLabel>
                    <FormControl>
                      <Input placeholder="John Doe" {...field} className="rounded-xl" />
                    </FormControl>
                    <FormMessage />
                  </FormItem>
                )}
              />
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
                name="phoneNumber"
                render={({ field }) => (
                  <FormItem>
                    <FormLabel className="text-gray-900">Teléfono (Opcional)</FormLabel>
                    <FormControl>
                      <Input placeholder="+506 12345678" {...field} className="rounded-xl" />
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
                {form.formState.isSubmitting ? 'Creando cuenta...' : 'Crear Cuenta'}
              </Button>
            </form>
          </Form>
          <div className="mt-6 text-center text-sm text-gray-600">
            ¿Ya tienes una cuenta?{' '}
            <Link to="/login" className="font-medium text-emerald-600 hover:text-emerald-500 transition-colors">
              Inicia Sesión
            </Link>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default RegisterPage;
