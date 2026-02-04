import TopBar from './TopBar';
import BottomNavBar from './BottomNavBar';

interface MainLayoutProps {
  children: React.ReactNode;
}

const MainLayout = ({ children }: MainLayoutProps) => {
  return (
    <div className="min-h-screen flex flex-col">
      <TopBar />
      <main className="flex-grow container mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {children}
      </main>
      <BottomNavBar />
    </div>
  );
};

export default MainLayout;
