import { useEffect, useState } from "react";
import Sidebar from "./Sidebar";
import Header from "./Header";
import "../../styles/layout.css";



export default function Layout({ children }: { children: React.ReactNode }) {
  
  const [sidebarOpen, setSidebarOpen] = useState(false);

  // если расширили экран — закрываем моб.меню
  useEffect(() => {
    const onResize = () => {
      if (window.innerWidth > 870) setSidebarOpen(false);
    };
    window.addEventListener("resize", onResize);
    return () => window.removeEventListener("resize", onResize);
  }, []);

  return (
    <div className="layout">
      <Sidebar isOpen={sidebarOpen} onClose={() => setSidebarOpen(false)} />

      <div className="layout__main">
        <Header onBurgerClick={() => setSidebarOpen((v) => !v)} />
        <main className="layout__content">{children}</main>
      </div>
    </div>
  );
}