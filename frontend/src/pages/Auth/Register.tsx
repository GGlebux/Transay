import { useState, useEffect} from "react";
import { Link } from "react-router-dom";
import styles from "./Auth.module.css";
import logo from "../../assets/Logo.png";
import { authApi } from "../../api/authApi";

type RegisterForm = {
    email: string;
    password: string;
    password2: string;
};



export default function Register() {
    const [form, setForm] = useState<RegisterForm>({
        email: "",
        password: "",
        password2: "",
    });

    const [showPassword, setShowPassword] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState<string>("");
    const [success, setSuccess] = useState<string>("");

    const [canResend, setCanResend] = useState(false);
    const [resendCooldown, setResendCooldown] = useState(0);

    const handleChange =
        (key: keyof RegisterForm) => (e: React.ChangeEvent<HTMLInputElement>) => {
            setForm((prev) => ({ ...prev, [key]: e.target.value }));
            if (error) setError("");
            if (success) setSuccess("");
        };

    const validateEmail = (emailRaw: string) => {
        const email = emailRaw.trim();

        if (!email) return "Введите почту.";
        if (!email.includes("@")) return "В почте должен быть символ @.";
        const [local, domain] = email.split("@");
        if (!local) return "Введите часть почты до @.";
        if (!domain) return "Введите домен после @ (например gmail.com).";
        if (!domain.includes(".")) return "В домене должна быть точка (например gmail.com).";
        if (domain.startsWith(".") || domain.endsWith(".")) return "Домен указан некорректно.";
        return "";
    };

    const validate = () => {
        const emailMsg = validateEmail(form.email);
        if (emailMsg) return emailMsg;

        const pwd = form.password;

        if (!pwd) return "Введите пароль.";
        if (/\s/.test(pwd)) return "Пароль не должен содержать пробелы.";
        if (pwd.length < 8) return "Пароль должен быть не короче 8 символов.";
        if (!/[a-z]/.test(pwd)) return "Пароль должен содержать строчную букву (a-z).";
        if (!/[A-Z]/.test(pwd)) return "Пароль должен содержать заглавную букву (A-Z).";
        if (!/\d/.test(pwd)) return "Пароль должен содержать цифру (0-9).";
        if (!/[^\w\s]/.test(pwd)) return "Пароль должен содержать спецсимвол (например !@#$%).";

        if (pwd !== form.password2) return "Пароли не совпадают.";
        return "";
    };

    useEffect(() => {
        if (resendCooldown <= 0) return;

        const timer = setInterval(() => {
            setResendCooldown((prev) => prev - 1);
        }, 1000);

        return () => clearInterval(timer);
    }, [resendCooldown]);

    const handleResend = async () => {
            if (resendCooldown > 0) return;
1
            try {
                setResendCooldown(60);

                await authApi.sendVerification(form.email.trim()); // метод нужно добавить в api

                setSuccess("Письмо отправлено повторно.");
            } catch (e: any) {
                setError(
                    e?.response?.data?.detail ||
                    "Не удалось отправить письмо повторно."
                );
            }
        };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        const msg = validate();
        if (msg) return setError(msg);



        try {
            setIsLoading(true);
            setError("");
            setSuccess("");

            await authApi.register({ email: form.email.trim(), password: form.password });

            setSuccess(
                "Регистрация принята. Мы отправили письмо для подтверждения email. Ссылка действительна 5 минут."
            );
            setCanResend(true);
            setResendCooldown(60);
        } catch (e: any) {
            const serverMsg =
                e?.response?.data?.errors?.password ||
                e?.response?.data?.detail ||
                "Не удалось зарегистрироваться. Попробуйте снова.";
            setError(serverMsg);
        } finally {
            setIsLoading(false);
        }
    };

    // Иконки лучше вынести, но можно оставить так
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
                    <h1 className={styles.title}>Регистрация</h1>
                    <p className={styles.subtitle}>Создайте аккаунт, чтобы продолжить</p>
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
                        Повтор пароля
                        <div className={styles.passwordWrap}>
                            <input
                                className={`${styles.input} ${styles.passwordInput}`}
                                type={showPassword ? "text" : "password"}
                                value={form.password2}
                                onChange={handleChange("password2")}
                                placeholder="Повторите ваш пароль"
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

                    {/* успех можно тоже оформлять как error-блок, но лучше отдельным классом */}
                    {success ? <div className={styles.success}>{success}</div> : null}
                    {canResend && (
                        <button
                            type="button"
                            className={styles.linkMutedBtn}
                            onClick={handleResend}
                            disabled={resendCooldown > 0}
                        >
                            {resendCooldown > 0
                                ? `Отправить письмо повторно через ${resendCooldown} сек`
                                : "Отправить письмо повторно"}
                        </button>
                    )}

                    <button className={styles.submit} type="submit" disabled={isLoading || canResend}>
                        {isLoading ? "Создаём..." : "Зарегистрироваться"}
                    </button>
                </form>

                <div className={styles.footerCol}>
                    <Link className={styles.link} to="/login">
                        Уже зарегистрированы? Войти
                    </Link>
                </div>
            </div>
        </div>
    );
}