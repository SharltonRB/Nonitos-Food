const BottomNavBar = () => {
  return (
    <nav className="sm:hidden fixed bottom-0 left-0 right-0 bg-white border-t border-gray-200 shadow-t-md z-10">
      <div className="flex justify-around h-16 items-center">
        {/* Mobile navigation items will go here */}
        <div className="text-center">
          <p className="text-sm">Home</p>
        </div>
        <div className="text-center">
          <p className="text-sm">Menu</p>
        </div>
        <div className="text-center">
          <p className="text-sm">Orders</p>
        </div>
        <div className="text-center">
          <p className="text-sm">Profile</p>
        </div>
      </div>
    </nav>
  );
};

export default BottomNavBar;
