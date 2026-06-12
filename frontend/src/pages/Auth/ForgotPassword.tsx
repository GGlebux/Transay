import { useEffect, useState } from "react";
import { Link, useSearchParams, useNavigate } from "react-router-dom";
import styles from "./Auth.module.css";
import logo from "../../assets/Logo.png";
import { authApi } from "../../api/authApi";

type Step = "start" | "end";

export default function ForgotPassword() {
    const [params] = useSearchParams();
    const navigate = useNavigate();

    const [step, setStep] = useState<Step>("start");
    const [email, setEmail] = useState("");
    const [token, setToken] = useState("");
    const [newPassword, setNewPassword] = useState("");
    const [password2, setPassword2] = useState("");
    const [showPassword, setShowPassword] = useState(false);

    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");

    // если token пришел в url, сразу открываем шаг 2
    useEffect(() => {
        const t = params.get("token");
        if (t) {
            setToken(t);
            setStep("end");
        }
    }, [params]);

    const validateEmail = (emailRaw: string) => {
        const v = emailRaw.trim();
        if (!v) return "Введите почту.";
        if (!v.includes("@")) return "В почте должен быть символ @.";
        const parts = v.split("@");
        if (parts.length !== 2 || !parts[0] || !parts[1]) return "Введите корректную почту.";
        if (!parts[1].includes(".")) return "В домене должна быть точка (например gmail.com).";
        return "";
    };

    const validatePassword = (pwd: string) => {
        if (!pwd) return "Введите новый пароль.";
        if (/\s/.test(pwd)) return "Пароль не должен содержать пробелы.";
        if (pwd.length < 8) return "Пароль должен быть не короче 8 символов.";
        if (!/[a-z]/.test(pwd)) return "Пароль должен содержать строчную букву (a-z).";
        if (!/[A-Z]/.test(pwd)) return "Пароль должен содержать заглавную букву (A-Z).";
        if (!/\d/.test(pwd)) return "Пароль должен содержать цифру (0-9).";
        if (!/[^\w\s]/.test(pwd)) return "Пароль должен содержать спецсимвол (например !@#$%).";
        return "";
    };

    const startReset = async (e: React.FormEvent) => {
        e.preventDefault();
        const msg = validateEmail(email);
        if (msg) return setError(msg);

        try {
            setLoading(true);
            setError("");
            setSuccess("");

            await authApi.resetStart(email.trim());

            setSuccess("Мы отправили письмо для восстановления пароля. Проверьте почту.");
        } catch (e: any) {
            setError(e?.response?.data?.detail || "Не удалось отправить письмо. Попробуйте снова.");
        } finally {
            setLoading(false);
        }
    };

    const endReset = async (e: React.FormEvent) => {
        e.preventDefault();

        const pwdMsg = validatePassword(newPassword);
        if (pwdMsg) return setError(pwdMsg);
        if (newPassword !== password2)
            return setError("Пароли не совпадают.");

        try {
            setLoading(true);
            setError("");
            setSuccess("");

            await authApi.resetEnd(token.trim(), newPassword);

            setSuccess("Пароль успешно изменён. Теперь можно войти.");
            navigate("/login", { replace: true });
        } catch (e: any) {
            setError(e?.response?.data?.detail || "Не удалось изменить пароль. Проверьте token и попробуйте снова.");
        } finally {
            setLoading(false);
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
                <path d="M12 15a3 3 0 1 0 0-6 3 3 0 0 0 0 6Z" stroke="currentColor" strokeWidth="2" />
            </svg>
        );
    }

    function EyeOffIcon() {
        return (
            <svg className={styles.eyeIcon} viewBox="0 0 24 24" fill="none" aria-hidden="true">
                <path d="M3 3l18 18" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
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
                    <h1 className={styles.title}>Восстановление пароля</h1>
                    <p className={styles.subtitle}>
                        {step === "start"
                            ? "Введите почту — мы отправим письмо."
                            : "Введите новый пароль."}
                    </p>
                </div>

                {step === "start" ? (
                    <form className={styles.form} onSubmit={startReset}>
                        <label className={styles.label}>
                            Почта
                            <input
                                className={styles.input}
                                type="email"
                                value={email}
                                onChange={(e) => {
                                    setEmail(e.target.value);
                                    if (error) setError("");
                                    if (success) setSuccess("");
                                }}
                                placeholder="name@example.com"
                                autoComplete="email"
                                required
                            />
                        </label>

                        {error ? <div className={styles.error}>{error}</div> : null}
                        {success ? <div className={styles.success}>{success}</div> : null}

                        <button className={styles.submit} type="submit" disabled={loading}>
                            {loading ? "Отправляем..." : "Отправить письмо"}
                        </button>
                    </form>
                ) : (
                    <form className={styles.form} onSubmit={endReset}>

                        <label className={styles.label}>
                            Новый пароль
                            <div className={styles.passwordWrap}>
                                <input
                                    className={`${styles.input} ${styles.passwordInput}`}
                                    type={showPassword ? "text" : "password"}
                                    value={newPassword}
                                    onChange={(e) => {
                                        setNewPassword(e.target.value);
                                        if (error) setError("");
                                        if (success) setSuccess("");
                                    }}
                                    placeholder="Введите новый пароль"
                                    autoComplete="new-password"
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

                        <label className={styles.label}>
                            Повторите пароль
                            <div className={styles.passwordWrap}>
                                <input
                                    className={`${styles.input} ${styles.passwordInput}`}
                                    type={showPassword ? "text" : "password"}
                                    value={password2}
                                    onChange={(e) => {
                                        setPassword2(e.target.value);
                                        if (error) setError("");
                                        if (success) setSuccess("");
                                    }}
                                    placeholder="Повторите новый пароль"
                                    autoComplete="new-password"
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

                        {error ? <div className={styles.error}>{error}</div> : null}
                        {success ? <div className={styles.success}>{success}</div> : null}

                        <button className={styles.submit} type="submit" disabled={loading}>
                            {loading ? "Сохраняем..." : "Сменить пароль"}
                        </button>
                    </form>
                )}

                <div className={styles.footerCol}>
                    <Link className={styles.link} to="/login">
                        Назад ко входу
                    </Link>
                    {step === "end" ? (
                        <button
                            type="button"
                            className={styles.linkMutedBtn}
                            onClick={() => {
                                setStep("start");
                                setError("");
                                setSuccess("");
                            }}
                        >
                            Отправить письмо ещё раз
                        </button>
                    ) : null}
                </div>
            </div>
        </div>
    );
}