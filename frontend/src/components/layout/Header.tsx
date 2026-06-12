import "../../styles/layout.css";
import logo from "../../assets/Logo.png";

type Props = {
  onBurgerClick: () => void;
};

export default function Header({ onBurgerClick }: Props) {
  return (
    <header className="header">
      <img className="brandLogo" src={logo} alt="Logo" />
      <button className="burger" type="button" onClick={onBurgerClick} aria-label="Open menu" />

    </header>
  );
}