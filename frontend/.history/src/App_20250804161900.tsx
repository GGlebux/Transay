import './styles/forms.css';
import IndicatorForm from '.src/components/IndicatorForm';
import TranscriptForm from './components/TranscriptForm';

function App() {
  return (
    <div className="container">
      <h1>Медицинские формы</h1>
      <IndicatorForm />
      <TranscriptForm />
    </div>
  );
}

export default App;
