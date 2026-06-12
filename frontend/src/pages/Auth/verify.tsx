import { useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { authApi } from "../../api/authApi";

export default function VerifyPage() {
  const [params] = useSearchParams();
  const navigate = useNavigate();

  useEffect(() => {
    const token = params.get("token");

    // если токена нет — сразу на логин
    if (!token) {
      navigate("/login", { replace: true });
      return;
    }

    authApi
      .verifyEmail(token)
      .then(() => {
        navigate("/login", { replace: true });
      })
      .catch(() => {
        // можно отправлять на /register — как тебе удобнее
        navigate("/login", { replace: true });
      });
  }, [params, navigate]);

  // НИЧЕГО не показываем
  return null;
}