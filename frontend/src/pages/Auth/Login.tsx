import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import styles from "./Auth.module.css";
import logo from "../../assets/Logo.png";
import { useAuth } from "../../authe/AuthContext";


import { authApi } from "../../api/authApi";



type LoginForm = {
    email: string;
    password: string;
};




export default function Login() {

    const { login } = useAuth();
    const navigate = useNavigate();

    const [form, setForm] = useState<LoginForm>({ email: "", password: "" });
    const [showPassword, setShowPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string>("");

    const handleChange =
        (key: keyof LoginForm) => (e: React.ChangeEvent<HTMLInputElement>) => {
            setForm((prev) => ({ ...prev, [key]: e.target.value }));
            if (error) setError("");
        };

    const validate = () => {
        if (!form.email.trim()) return "Введите почту.";
        if (!/^\S+@\S+\.\S+$/.test(form.email.trim())) return "Введите корректную почту.";
        if (!form.password) return "Введите пароль.";
        if (form.password.length < 6) return "Пароль должен быть не короче 6 символов.";
        return "";
    };




    const handleSubmit = async (e: React.FormEvent) => {
  e.preventDefault();
  const msg = validate();
  if (msg) return setError(msg);

  try {
    setIsLoading(true);
    setError("");

    const res = await authApi.login({
      email: form.email.trim(),
      password: form.password,
    });

    const user = await login(res.accessToken);

    // ✅ редирект здесь
    if (user.role === "ADMIN") {
      navigate("/admin/users", { replace: true });
      return;
    }

    if (user.role === "USER") {
      if (!user.personId) {
        navigate("/cabinet/create-profile", { replace: true });
      } else {
        navigate("/cabinet", { replace: true });
      }
    }

  } catch {
    setError("Не удалось войти. Проверьте данные и попробуйте снова.");
  } finally {
    setIsLoading(false);
  }
};


    function EyeIcon() {
        return (
            <svg className={styles.eyeIcon} viewBox="0 0 24 24" fill="none" aria-hidden="true">
                <path
                    d="M2 12s3.5-7 10-7 10 7 10 7-3.5 7-10 7S2 12 2 12Z"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinejoin="round"
                />
                <path
                    d="M12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z"
                    stroke="currentColor"
                    strokeWidth="2"
                />
            </svg>
        );
    }

    function EyeOffIcon() {
        return (
            <svg className={styles.eyeIcon} viewBox="0 0 24 24" fill="none" aria-hidden="true">
                <path
                    d="M3 3l18 18"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinecap="round"
                />
                <path
                    d="M10.6 10.6A2.5 2.5 0 0 0 12 14.5a2.5 2.5 0 0 0 2.4-3.2"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinecap="round"
                />
                <path
                    d="M6.4 6.4C4.3 7.8 2.9 9.9 2 12c1.7 4 5.4 7 10 7 2 0 3.8-.6 5.3-1.5"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinecap="round"
                />
                <path
                    d="M9.6 4.6C10.4 4.3 11.2 4 12 4c4.6 0 8.3 3 10 8-.5 1.2-1.1 2.3-1.9 3.3"
                    stroke="currentColor"
                    strokeWidth="2"
                    strokeLinecap="round"
                />
            </svg>
        );
    }

    return (
        <div className={styles.page}>
            <div className={styles.brand}>
                <img className={styles.brandLogo} src={logo} alt="Logo" />
            </div>
            <div className={styles.card}>
                <div className={styles.header}>
                    <h1 className={styles.title}>Вход</h1>
                    <p className={styles.subtitle}>Введите почту и пароль, чтобы продолжить</p>
                </div>

                <form className={styles.form} onSubmit={handleSubmit}>
                    <label className={styles.label}>
                        Почта
                        <input
                            className={styles.input}
                            type="email"
                            value={form.email}
                            onChange={handleChange("email")}
                            placeholder="name@example.com"
                            autoComplete="email"
                            inputMode="email"
                            required
                        />
                    </label>

                    <label className={styles.label}>
                        Пароль
                        <div className={styles.passwordWrap}>
                            <input
                                className={`${styles.input} ${styles.passwordInput}`}
                                type={showPassword ? "text" : "password"}
                                value={form.password}
                                onChange={handleChange("password")}
                                placeholder="Введите ваш пароль"
                                autoComplete="current-password"
                                required
                            />

                            <button
                                type="button"
                                className={styles.eyeIconButton}
                                onClick={() => setShowPassword((v) => !v)}
                                aria-label={showPassword ? "Скрыть пароль" : "Показать пароль"}
                                title={showPassword ? "Скрыть пароль" : "Показать пароль"}
                            >
                                {showPassword ? <EyeOffIcon /> : <EyeIcon />}
                            </button>
                        </div>
                    </label>

                    <div className={styles.rowBetween}>
                        <Link className={styles.smallLink} to="/forgot-password">
                            Забыли пароль?
                        </Link>
                    </div>

                    {error ? <div className={styles.error}>{error}</div> : null}

                    <button className={styles.submit} type="submit" disabled={isLoading}>
                        {isLoading ? "Входим..." : "Войти"}
                    </button>
                </form>

                <div className={styles.footerCol}>
                    <Link className={styles.link} to="/register">
                        Нет аккаунта? Зарегистрироваться
                    </Link>
                </div>
            </div>
        </div>
    );
}