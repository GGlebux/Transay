import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.tsx'
import { AuthProvider } from "./authe/AuthContext";
import { FeedbackProvider } from "./components/ui/Feedback";

createRoot(document.getElementById('root')!).render(
  <AuthProvider>
  <StrictMode>
    <FeedbackProvider>
      <App />
    </FeedbackProvider>
  </StrictMode>
  </AuthProvider>
)
