const TopBar = () => {
  return (
    <header className="hidden sm:block sticky top-0 bg-white/70 backdrop-blur-lg shadow-sm z-10">
      <div className="container mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-16">
          <div className="flex-shrink-0">
            <h1 className="text-2xl font-bold text-gray-900">Nonito's Food</h1>
          </div>
          <div className="hidden sm:block">
            {/* Desktop navigation items will go here */}
          </div>
        </div>
      </div>
    </header>
  );
};

export default TopBar;
